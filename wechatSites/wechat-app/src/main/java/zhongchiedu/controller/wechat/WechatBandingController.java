package zhongchiedu.controller.wechat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import zhongchiedu.common.utils.BasicDataResult;
import zhongchiedu.common.utils.Common;
import zhongchiedu.framework.pagination.Pagination;
import zhongchiedu.log.annotation.SystemControllerLog;
import zhongchiedu.school.pojo.HomeWork;
import zhongchiedu.school.pojo.School;
import zhongchiedu.school.pojo.Student;
import zhongchiedu.school.pojo.WeChatbanding;
import zhongchiedu.service.HomeWorkService;
import zhongchiedu.service.SchoolService;
import zhongchiedu.service.WeChatbandingService;
import zhongchiedu.wechat.oauto2.NSNUserInfo;
import zhongchiedu.wechat.util.WeixinUtil;


@RequestMapping("/wechat")
@Controller
public class WechatBandingController {
	

	private static final Logger log = LoggerFactory.getLogger(WechatBandingController.class);

	@Autowired
	private SchoolService schoolService;
	@Autowired
	private HomeWorkService homeWorkService;
	
	@Autowired
	private WeChatbandingService weChatbandingService;
	
	/**
	 * 微信授权验证
	 * 
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping("/weChatAuth")
	public ModelAndView wechatAuth(HttpSession session, HttpServletRequest request) {

		ModelAndView model = new ModelAndView();

		model.setViewName("wechat/front/index");

		String code = request.getParameter("code");
		log.info("weChat Code: " + code);

		if (Common.isEmpty(code)) {
			School school = this.schoolService.findOneByQuery(new Query(), School.class);
			String appid = school.getAppid();	//获取appid
			String url = school.getDoMainName();//获取域名
			String redirect_uri = url+"/wechat-app/wechat/toAuthor";
			System.out.println("url:"+url);
			log.info("未获取请用户的openId,请求微信授权");
			// 注意：基于snsapi_base和snsapi_userinfo获取用户信息是不需要关注公众号。对于已关注公众号的用户，如果用户从公众号的会话或者自定义菜单进入本公众号的网页授权页，即使是scope为snsapi_userinfo，也是静默授权，用户无感知。
			return new ModelAndView(new RedirectView("https://open.weixin.qq.com/connect/oauth2/authorize?"
					+ "appid="+appid+"&redirect_uri="+redirect_uri
					+ "&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect"));
		}
		//通过code获取用户信息
        NSNUserInfo nsn=WeixinUtil.baseWeChatLogin(code);
        model.addObject("openId", nsn.getOpenid());
         System.out.println(nsn.getOpenid());
		return model;
	}

	
	/**
	 * 跳转到支付成功
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping("/toAuthor")
	public ModelAndView toAuthor(HttpSession session, HttpServletRequest request) {
		String code = request.getParameter("code");
		log.info("successPay Code" + code);
		ModelAndView model = new ModelAndView();
		model.setViewName("wechat/front/login");
		return model;
	}
	
	
	/**
	 * 跳转到支付成功
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping("/index")
	public ModelAndView index(HttpSession session, HttpServletRequest request) {
		String code = request.getParameter("code");
		log.info("successPay Code" + code);
		ModelAndView model = new ModelAndView();
		model.setViewName("wechat/front/index");
		return model;
	}
	
	
	
	/**
	 * 作业
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping("/homework")
	public ModelAndView homework(HttpSession session, HttpServletRequest request) {
		String code = request.getParameter("code");
		log.info("successPay Code" + code);
		ModelAndView model = new ModelAndView();
		model.setViewName("wechat/front/homework");
		return model;
	}
	
	
	
	
	
	
	
	
	/**
	 * 进行绑定操作
	 * @return
	 */
	@RequestMapping("/toBinding")
	@ResponseBody
	public BasicDataResult  toBinding(String account,String password,HttpServletRequest request){
//		String code = request.getParameter("code");
//		//验证输入的信息是否为空
//		if(Common.isNotEmpty(account)&&Common.isNotEmpty(password)){
//			//1.验证帐号密码是否是正确的，如果正确则对帐号进行登录然后解析
//			String result = this.homeWorkService.checkLogin(account, password);
//			if(result.equals("error")){
//				return BasicDataResult.build(400, "帐号或密码错误", null);
//			}else{
//				//2.获取当前访问用户的code，并且通过code去获取用户的信息
//				NSNUserInfo nsn=WeixinUtil.baseWeChatLogin(code);
//				WeChatbanding banding = new WeChatbanding(); 
//				HomeWork homeWork =this.homeWorkService.jsoupGetHomeWork(result);
//				banding.setNsnUserInfo(nsn);
//				banding.setStudentAccount(account);
//				banding.setPassword(password);
//				banding.setOpenId(nsn.getOpenid());
//				banding.setStudentClass(homeWork.getGradeName()+homeWork.getClassName());
//				banding.setStudentName(homeWork.getStudentName());
//				//3.保存绑定信息
//				this.weChatbandingService.SaveOrUpdateWeChatbanding(banding);
//				return BasicDataResult.build(200, "用户绑定成功", null);
//			}
//		}
//		
		System.out.println(account);
		return BasicDataResult.build(400, "帐号或密码有误", null);
	}
	
	public static void main(String[] args) {
		
		
		
	}
	
	@GetMapping("")
	@RequiresPermissions(value = "binding:list")
	@SystemControllerLog(description = "查询所有binding")
	public String list(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo, Model model,
			@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, HttpSession session,
			@ModelAttribute("errorImport") String errorImport) {
		if(Common.isNotEmpty(errorImport)){
			model.addAttribute("errorImport", errorImport);
		}
		// 分页查询数据
		Pagination<WeChatbanding> pagination;
		try {
			pagination = this.weChatbandingService.findPaginationByQuery(new Query(), pageNo, pageSize, WeChatbanding.class);
			if (pagination == null)
				pagination = new Pagination<WeChatbanding>();
			model.addAttribute("pageList", pagination);
			System.out.println(pagination.toString());
		} catch (Exception e) {
			log.info("查询bindinglist失败——————————》" + e.toString());
			e.printStackTrace();
		}
		return "schools/bind/list";
	}

	
	@DeleteMapping("/binding/{id}")
	@SystemControllerLog(description = "删除binding")
	public String delete(@PathVariable String id) {
		String[] strids = id.split(",");
		for (String delids : strids) {
			log.info("删除binding---》" + delids);
			WeChatbanding bind = this.weChatbandingService.findOneById(delids, WeChatbanding.class);
			this.weChatbandingService.remove(bind);// 删除某个id
		}
		return "redirect:/wechat";
	}
	
	@GetMapping("/add")
	@ResponseBody
	public BasicDataResult addbinding() {
		WeChatbanding we=new WeChatbanding();
		we.setOpenId("1321213321321213321asdasdasd");
		we.setStudentClass("1年级1班");
		we.setStudentName("陈豪1");
		we.setStudentAccount("123");
		weChatbandingService.insert(we);
		return BasicDataResult.build(200, "add,ok", we);
	}
}
