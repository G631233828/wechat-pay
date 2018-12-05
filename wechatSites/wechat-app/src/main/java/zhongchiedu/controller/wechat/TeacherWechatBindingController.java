package zhongchiedu.controller.wechat;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import zhongchiedu.common.utils.BasicDataResult;
import zhongchiedu.common.utils.Common;
import zhongchiedu.common.utils.Contents;
import zhongchiedu.framework.pagination.Pagination;
import zhongchiedu.school.pojo.Clazz;
import zhongchiedu.school.pojo.Leave;
import zhongchiedu.school.pojo.Teacher;
import zhongchiedu.school.pojo.WeChatbanding;
import zhongchiedu.school.pojo.WeChatbandingStudent;
import zhongchiedu.service.ClazzService;
import zhongchiedu.service.LeaveService;
import zhongchiedu.service.TeacherService;
import zhongchiedu.service.WeChatbandingService;
import zhongchiedu.wechat.oauto2.NSNUserInfo;

@RequestMapping("/wechat/teacher")
@Controller
public class TeacherWechatBindingController {

	private static final Logger log = LoggerFactory.getLogger(TeacherWechatBindingController.class);

	@Autowired
	private TeacherService teacherService;

	@Autowired
	private WeChatbandingService weChatbandingService;

	@Autowired
	private ClazzService clazzService;
	
	@Autowired
	private LeaveService leaveService;

	/**
	 * 老师微信授权验证
	 * 
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping("/weChatAuth")
	public ModelAndView wechatAuth(HttpSession session, HttpServletRequest request) {
		log.info("教师登陆授权");
		ModelAndView model = new ModelAndView();
		// 1.用户访问获取code如果没有获取到code则重定向
		String code = request.getParameter("code");
		if (Common.isEmpty(code)) {
			String redirect_uri = Contents.URL + "/wechat-app/wechat/teacher/weChatAuth";
			return new ModelAndView(new RedirectView("https://open.weixin.qq.com/connect/oauth2/authorize?" + "appid="
					+ Contents.APPID + "&redirect_uri=" + redirect_uri
					+ "&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect"));
		} else {
			// 2.通过用户code获取用户的信息，openId
			NSNUserInfo nsn = this.weChatbandingService.findWechatNsn(code);
			if (Common.isEmpty(nsn)) {
				// 授权信息为空重定向到登陆页面
				model.setViewName("redirect:weChatAuth");
				return model;
			}
			// 3.通过用户的openId在老师信息表中进行查询，
			Teacher teacher = this.teacherService.findTeacherByOpenId(nsn.getOpenid());
			log.info("老师用户：" + nsn.getNickname() + "正在登陆！openId=" + nsn.getOpenid());
			if (Common.isNotEmpty(teacher)) {
				// 跳转到index
				model.setViewName("redirect:index");
				return model;
			} else {
				// 跳转到绑定界面
				model.setViewName("redirect:toAuthor");
				return model;
			}
		}
	}

	/**
	 * 跳转到登录界面
	 * 
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping("/toAuthor")
	public ModelAndView toAuthor(HttpSession session, HttpServletRequest request) {
		String code = request.getParameter("code");
		if (Common.isEmpty(code)) {
			String redirect_uri = Contents.URL + "/wechat-app/wechat/teacher/toAuthor";
			// 登录失败，用户名或者密码错误（已经发生了更改）
			return new ModelAndView(new RedirectView("https://open.weixin.qq.com/connect/oauth2/authorize?" + "appid="
					+ Contents.APPID + "&redirect_uri=" + redirect_uri
					+ "&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect"));
		}
		ModelAndView model = new ModelAndView();
		// 获取所有的班级
		List<Clazz> list = this.clazzService.findClazzsByisDisable();
		model.addObject("listClazz", list);

		model.setViewName("wechat/front/teacher/login");
		return model;
	}

	/**
	 * 登录成功，跳转到首页
	 * 
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping("/index")
	public ModelAndView index(HttpSession session, HttpServletRequest request) {
		ModelAndView model = new ModelAndView();
		String code = request.getParameter("code");
		if (Common.isEmpty(code)) {
			String redirect_uri = Contents.URL + "/wechat-app/wechat/teacher/index";
			// 登录失败，用户名或者密码错误（已经发生了更改）
			return new ModelAndView(new RedirectView("https://open.weixin.qq.com/connect/oauth2/authorize?" + "appid="
					+ Contents.APPID + "&redirect_uri=" + redirect_uri
					+ "&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect"));
		}

		NSNUserInfo nsn = this.weChatbandingService.findWechatNsn(code);
		if (Common.isEmpty(nsn)) {
			// 授权信息为空重定向到登陆页面
			model.setViewName("redirect:weChatAuth");
			return model;
		}

		Teacher teacher = this.teacherService.findTeacherByOpenId(nsn.getOpenid());
		if (Common.isEmpty(teacher)) {
			model.setViewName("redirect:toAuthor");
			return model;
		}

		model.addObject("teacher", teacher);
		model.setViewName("wechat/front/teacher/index");
		return model;
	}
	
	
	
	/**
	 * 进行绑定操作/添加绑定, @Valid Clazz clazz
	 * 
	 * @return
	 */
	@RequestMapping("/toBinding")
	@ResponseBody
	public BasicDataResult toBinding(String account, String password, String code,HttpServletRequest request) {
		log.info("正在绑定:account"+account+"code"+code);
	
		try{
		//验证输入的信息是否为空
		if(Common.isNotEmpty(account)&&Common.isNotEmpty(password)&&Common.isNotEmpty(code)){
			//通过用户的code获取到用户的信息
			NSNUserInfo nsn = this.weChatbandingService.findWechatNsn(code);
			//通过微信openId查询该微信是否已经绑定过
			Teacher t = this.teacherService.findTeacherByOpenId(nsn.getOpenid());
			if(Common.isNotEmpty(t)){
				return BasicDataResult.build(400, "您的微信已经绑定过一个教师账号了！", null);
			}
			
			
			Teacher teacher = this.teacherService.findTeacherByAccount(account, password);
			if(Common.isEmpty(teacher)){
				return BasicDataResult.build(400,"输入的账号或密码错误",null);
			}
			if(Common.isNotEmpty(teacher.getOpenId())){
				//当前老师已经绑定过了
				return BasicDataResult.build(400,"当前账号已经绑定过微信了，无法绑定其它微信账号",null);
			}
			
			if(Common.isEmpty(nsn)){
				return BasicDataResult.build(400,"请使用微信客户端进行打开",null);
			}
			
			teacher.setNsnUserInfo(nsn);
			teacher.setOpenId(nsn.getOpenid());
			this.teacherService.save(teacher);
			return BasicDataResult.build(200, "用户绑定成功", null);
		}
		}catch(Exception e){
			e.printStackTrace();
			// 验证输入的信息是否为空
			return BasicDataResult.build(400, "绑定过程中出现未知异常，请联系管理员！", null);
		}
		return BasicDataResult.build(400, "绑定过程中出现未知异常，请联系管理员！", null);
	}
	
	
	
	//TODO
	
	 /*
	 * 根据老师的id查询班级请假信息 默认是查询当天的请假
	 */
	@RequestMapping("/findleave")
	public ModelAndView findleave(String id, @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
			@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, @RequestParam(value = "serach", defaultValue = "")String serach) {
		ModelAndView model = new ModelAndView();
		Clazz clazz = this.clazzService.findHeadMaster(id);
		log.info("老师id"+id+"当前班级信息"+clazz);
		
		Pagination<Leave> pagination = this.leaveService.findLeaveByPagination(clazz, pageNo, pageSize,serach);
		if(Common.isEmpty(pagination)){
			pagination = new Pagination<>();
		}
		model.addObject("pageList", pagination);
		model.addObject("clazz", clazz);
		model.addObject("id", id);
		model.addObject("serach", serach);
		
		
		model.setViewName("wechat/front/teacher/findLeave");
		return model;
	}
	
	
	

	/**
	 * 解除绑定
	 * 
	 * @param code
	 * @return
	 */
	@RequestMapping("/unbinding")
	public ModelAndView unbinding(HttpServletRequest request) {
		String code = request.getParameter("code");
		ModelAndView model = new ModelAndView();
		if (Common.isEmpty(code)) {
			String redirect_uri = Contents.URL + "/wechat-app/wechat/teacher/unbinding";
			return new ModelAndView(new RedirectView(
					"https://open.weixin.qq.com/connect/oauth2/authorize?" + "appid=" + Contents.APPID + "&redirect_uri="
							+ redirect_uri + "&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect"));
		}
		NSNUserInfo nsn = this.weChatbandingService.findWechatNsn(code);
		if(Common.isEmpty(nsn)){
			model.setViewName("redirect:toAuthor");
			return model;
		}
		Teacher teacher = this.teacherService.findTeacherByOpenId(nsn.getOpenid());
		log.info("正在解除"+teacher.getName()+"的微信绑定！");
		if(Common.isNotEmpty(teacher)){
			teacher.setNsnUserInfo(null);
			teacher.setOpenId(null);
			this.teacherService.save(teacher);
		}
		model.setViewName("redirect:toAuthor");
		return model;
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	

	@RequestMapping("/test")
	public ModelAndView test(String id) {
		//测试
		 id = "ooiMKv7cqR-2EgkeC9LdATpr-mbY";
		 ModelAndView model = new ModelAndView();
		
		System.out.println("id"+id);
		Clazz clazz = this.clazzService.findHeadMaster(id);
		log.info("老师id"+id+"当前班级信息"+clazz);
		
//		Pagination<Leave> pagination = this.leaveService.findLeaveByPagination(clazz, pageNo, pageSize,serach);
		Pagination<Leave> pagination = this.leaveService.findPaginationByQuery(new Query(), 1, 10, Leave.class);
		if(Common.isEmpty(pagination)){
			pagination = new Pagination<>();
		}
		model.addObject("pageList", pagination);
		model.addObject("clazz", clazz);
		model.addObject("openId", id);
		
		System.out.println(pagination);
		System.out.println(pagination);
		System.out.println(pagination);
		
		model.setViewName("wechat/front/teacher/findLeave");
return model;
	}
}
