//package zhongchiedu.controller.wechat;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpSession;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.servlet.ModelAndView;
//import org.springframework.web.servlet.view.RedirectView;
//
//import zhongchiedu.common.utils.BasicDataResult;
//import zhongchiedu.common.utils.Common;
//import zhongchiedu.common.utils.Contents;
//import zhongchiedu.school.pojo.HomeWork;
//import zhongchiedu.school.pojo.WeChatbanding;
//import zhongchiedu.school.pojo.WeChatbandingStudent;
//import zhongchiedu.service.HomeWorkService;
//import zhongchiedu.service.WeChatbandingService;
//import zhongchiedu.service.Impl.StudentServiceImpl;
//import zhongchiedu.wechat.oauto2.NSNUserInfo;
//
//@RequestMapping("/wechat")
//@Controller
//public class WechatBandingControllerback {
//
//	private static final Logger log = LoggerFactory.getLogger(WechatBandingControllerback.class);
//
//	@Autowired
//	private HomeWorkService homeWorkService;
//
//	@Autowired
//	private WeChatbandingService weChatbandingService;
//	
//	@Autowired
//	private StudentServiceImpl studentService;
//
//	/**
//	 * 微信授权验证
//	 * 
//	 * @param session
//	 * @param request
//	 * @return
//	 */
//	@RequestMapping("/weChatAuth")
//	public ModelAndView wechatAuth(HttpSession session, HttpServletRequest request) {
//		ModelAndView model = new ModelAndView();
//		// 1.用户访问获取code如果没有获取到code则重定向
//		String code = request.getParameter("code");
//		if (Common.isEmpty(code)) {
//			String redirect_uri = Contents.URL + "/wechat-app/wechat/weChatAuth";
//			return new ModelAndView(new RedirectView("https://open.weixin.qq.com/connect/oauth2/authorize?" + "appid="
//					+ Contents.APPID + "&redirect_uri=" + redirect_uri
//					+ "&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect"));
//		} else {
//			// 2.通过用户code获取用户的信息，openId
//			NSNUserInfo nsn = this.weChatbandingService.findWechatNsn(code);
//
//			if (Common.isEmpty(nsn)) {
//				// 授权信息为空重定向到登陆页面
//				model.setViewName("redirect:weChatAuth");
//				return model;
//			}
//			// 3.通过用户的openId在wechatbanding表中进行查询，
//			WeChatbanding weChatbinding = this.weChatbandingService.findWeChatbandingByOpenId(nsn.getOpenid());
//			log.info("用户："+nsn.getNickname()+"正在登陆！openId="+nsn.getOpenid());
//			if (Common.isNotEmpty(weChatbinding)) {
//				// 4.如果获取的wechat绑定不为空，那么获取用户名密码
//				String account = weChatbinding.getStudentAccount();
//				String password = weChatbinding.getPassword();
//				// 使用帐号密码进行登录
//				String result = this.homeWorkService.checkLogin(account, password, null);
//				//更新绑定学生的信息
//				
//				
//				if (result.equals("error")) {
//					//密码过期，删除该用户2019年3月7日 13:41:57
//					this.weChatbandingService.remove(weChatbinding);
//					// 登录失败，跳转到登录界面
//					model.setViewName("redirect:toAuthor");
//					return model;
//				} else {
//					model.setViewName("redirect:index");
//					return model;
//				}
//			} else {
//				model.setViewName("redirect:toAuthor");
//				return model;
//			}
//		}
//
//	}
//
//	/**
//	 * 跳转到登录界面
//	 * 
//	 * @param session
//	 * @param request
//	 * @return
//	 */
//	@RequestMapping("/toAuthor")
//	public ModelAndView toAuthor(HttpSession session, HttpServletRequest request) {
//		String code = request.getParameter("code");
//		if (Common.isEmpty(code)) {
//			String redirect_uri = Contents.URL + "/wechat-app/wechat/toAuthor";
//			// 登录失败，用户名或者密码错误（已经发生了更改）
//			return new ModelAndView(new RedirectView(
//					"https://open.weixin.qq.com/connect/oauth2/authorize?" + "appid=" + Contents.APPID + "&redirect_uri="
//							+ redirect_uri + "&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect"));
//		}
//		ModelAndView model = new ModelAndView();
//		model.setViewName("wechat/front/login");
//		return model;
//	}
//
//	/**
//	 * 登录成功，跳转到首页
//	 * 
//	 * @param session
//	 * @param request
//	 * @return
//	 */
//	@RequestMapping("/index")
//	public ModelAndView index(HttpSession session, HttpServletRequest request) {
//		ModelAndView model = new ModelAndView();
//		String code = request.getParameter("code");
//		if (Common.isEmpty(code)) {
//			String redirect_uri = Contents.URL + "/wechat-app/wechat/index";
//			// 登录失败，用户名或者密码错误（已经发生了更改）
//			return new ModelAndView(new RedirectView(
//					"https://open.weixin.qq.com/connect/oauth2/authorize?" + "appid=" + Contents.APPID + "&redirect_uri="
//							+ redirect_uri + "&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect"));
//		}
//		
//		NSNUserInfo nsn = this.weChatbandingService.findWechatNsn(code);
//		if (Common.isEmpty(nsn)) {
//			// 授权信息为空重定向到登陆页面
//			model.setViewName("redirect:weChatAuth");
//			return model;
//		}
//		
//		WeChatbanding we = this.weChatbandingService.findWeChatbandingByOpenId(nsn.getOpenid());
//		if(Common.isEmpty(we)){
//			model.setViewName("redirect:toAuthor");
//			return model;
//		}
//		model.addObject("wechatbinding", we);
//		model.addObject("clazz",we.getStudentAccount().substring(0, 4)+"年"+we.getStudentAccount().substring(5,6)+"班");
//		model.setViewName("wechat/front/index");
//		return model;
//	}
//
//	/**
//	 * 作业
//	 * 
//	 * @param session
//	 * @param request
//	 * @return
//	 */
//	@RequestMapping("/homework")
//	public ModelAndView homework(HttpSession session, HttpServletRequest request) {
//		ModelAndView model = new ModelAndView();
//		String state = request.getParameter("state");
//		String date = Common.isNotEmpty(request.getParameter("date")) ? request.getParameter("date") : null;
//		if (Common.isNotEmpty(state) && !state.equals("STATE")) {
//			date = state;
//		}
//		String code = request.getParameter("code");
//		if (Common.isEmpty(code)) {
//			String redirect_uri = Contents.URL + "/wechat-app/wechat/homework";
//			if (Common.isEmpty(date)) {
//				date = "STATE";
//			}
//			return new ModelAndView(new RedirectView("https://open.weixin.qq.com/connect/oauth2/authorize?" + "appid="
//					+ Contents.APPID + "&redirect_uri=" + redirect_uri + "&response_type=code&scope=snsapi_userinfo&state="
//					+ date + "#wechat_redirect"));
//		}
//		// 2.通过用户code获取用户的信息，openId
//		NSNUserInfo nsn = this.weChatbandingService.findWechatNsn(code);
//		//修复刷新页面出现报错页面
//		if(Common.isEmpty(nsn)){
//			String redirect_uri = Contents.URL + "/wechat-app/wechat/homework";
//			if (Common.isEmpty(date)) {
//				date = "STATE";
//			}
//			return new ModelAndView(new RedirectView("https://open.weixin.qq.com/connect/oauth2/authorize?" + "appid="
//					+ Contents.APPID + "&redirect_uri=" + redirect_uri + "&response_type=code&scope=snsapi_userinfo&state="
//					+ date + "#wechat_redirect"));
//		}
//		// 3.通过用户的openId在wechatbanding表中进行查询，
//		WeChatbanding we = this.weChatbandingService.findWeChatbandingByOpenId(nsn.getOpenid());
//		if(Common.isEmpty(we)){
//			model.setViewName("redirect:toAuthor");
//			return model;
//		}
//		
//		log.info("用户："+nsn.getNickname()+"查看作业"+we.getStudentName()+"openId="+nsn.getOpenid());
//		// 4.如果能获取到用户的绑定信息
//		if (Common.isNotEmpty(we)) {
//			String account = we.getStudentAccount();
//			String password = we.getPassword();
//			// 使用帐号密码进行登录
//			String result = this.homeWorkService.checkLogin(account, password, date);
//			if (result.equals("error")) {
//				// 登录失败，跳转到登录界面
//				model.setViewName("redirect:toAuthor");
//				return model;
//			} else {
//				// 开始解析返回信息，然后返回到页面
//				HomeWork homeWork = this.homeWorkService.jsoupGetHomeWork(result);
//				model.addObject("homeWork", homeWork);
//				model.addObject("date", date);
//				model.setViewName("wechat/front/homework");
//				return model;
//			}
//		} else {
//			model.setViewName("redirect:toAuthor");
//			return model;
//
//		}
//
//	}
//
//	/**
//	 * 进行绑定操作/添加绑定
//	 * 
//	 * @return
//	 */
//	@RequestMapping("/toBinding")
//	@ResponseBody
//	public BasicDataResult toBinding(String account, String password, String code, HttpServletRequest request) {
//		
//		// 验证输入的信息是否为空
//		if (Common.isNotEmpty(account) && Common.isNotEmpty(password) && Common.isNotEmpty(code)) {
//			// 1.验证帐号密码是否是正确的，如果正确则对帐号进行登录然后解析
//			String result = this.homeWorkService.checkLogin(account, password, null);
//			if (result.equals("error")) {
//				return BasicDataResult.build(400, "帐号或密码错误", null);
//			} else {
//				// 2.获取当前访问用户的code，并且通过code去获取用户的信息
//				NSNUserInfo nsn = this.weChatbandingService.findWechatNsn(code);
//				if (Common.isEmpty(nsn)) {
//					// 登录失败，用户名或者密码错误（已经发生了更改）
//					
//					return BasicDataResult.build(400, "页面过期，请刷新后再试！", null);
//				}
//				HomeWork homeWork = this.homeWorkService.jsoupGetHomeWork(result);
//				//new
//				//获取openid,根据openid查看是否已经存在该openId
//				WeChatbanding wcb = this.weChatbandingService.findWeChatbandingByOpenId(nsn.getOpenid());
//				
//				if(Common.isNotEmpty(wcb)){
//					List<WeChatbandingStudent> bds = wcb.getListbandings();//获取绑定的学生
//					if(bds.size()>0){
//						for(WeChatbandingStudent student:bds){
//							if(student.getStudentAccount().equals(account)){
//								return BasicDataResult.build(400, "请不要重复绑定相同的账号！", null);
//							}
//						}
//					}
//					//已经绑定过，添加绑定
//					List<WeChatbandingStudent> students = wcb.getListbandings();
//					WeChatbandingStudent student = new WeChatbandingStudent();
//					student.setPassword(password);
//					student.setStudentAccount(account);
//					student.setStudentName(homeWork.getStudentName());
//					student.setStudentClass(homeWork.getGradeName() + homeWork.getClassName());
//					//对学生列表中的该数据进行跟新
//					this.studentService.saveStudentByRegisterNum(student);
//					students.add(student);
//					wcb.setListbandings(students);
//					this.weChatbandingService.SaveOrUpdateWeChatbanding(wcb);
//					return BasicDataResult.build(200, "添加绑定成功", null);
//				}else{
//					//第一次绑定
//					WeChatbandingStudent student = new WeChatbandingStudent();
//					List<WeChatbandingStudent> listbds = new ArrayList<>();
//					WeChatbanding banding = new WeChatbanding();
//					student.setPassword(password);
//					student.setStudentAccount(account);
//					student.setStudentName(homeWork.getStudentName());
//					student.setStudentClass(homeWork.getGradeName() + homeWork.getClassName());
//					listbds.add(student);
//					banding.setNsnUserInfo(nsn);
//					banding.setStudentAccount(account);
//					banding.setPassword(password);
//					banding.setOpenId(nsn.getOpenid());
//					banding.setStudentClass(homeWork.getGradeName() + homeWork.getClassName());
//					banding.setStudentName(homeWork.getStudentName());
//					banding.setListbandings(listbds);
//					//对学生列表中的该数据进行跟新
//					this.studentService.saveStudentByRegisterNum(student);
//					// 3.保存绑定信息
//					this.weChatbandingService.SaveOrUpdateWeChatbanding(banding);
//					return BasicDataResult.build(200, "用户绑定成功！", null);
//					
//				}
//				
//			}
//		}
//		return BasicDataResult.build(400, "绑定过程中出现未知异常，请联系管理员！", null);
//	}
//	
//	
//	
//	/**
//	 * 切换默认帐号
//	 * @param openId
//	 * @param account
//	 * @return
//	 */
//	@RequestMapping("/selectAccount")
//	@ResponseBody
//	public BasicDataResult selectAccount(String openId,String account){
//		
//		if(Common.isEmpty(openId)||Common.isEmpty(account)){
//			return BasicDataResult.build(400, "未能获取到您的信息，请刷新后重试！", null);
//		}
//		
//		//通过用户的openId进行查询
//		WeChatbanding weChatbanding = this.weChatbandingService.findWeChatbandingByOpenId(openId);
//		if(Common.isEmpty(weChatbanding)){
//			return BasicDataResult.build(400, "请先进行绑定后在试！",null);
//		}
//		List<WeChatbandingStudent> wEchatbandingStudentlist = weChatbanding.getListbandings();
//		if(wEchatbandingStudentlist.size()==0){
//			return BasicDataResult.build(400, "您的微信帐号尚未绑定学生，请先绑定后在试！",null);
//		}
//		
//		for(WeChatbandingStudent we:wEchatbandingStudentlist){
//			if(we.getStudentAccount().equals(account)){
//				weChatbanding.setStudentAccount(we.getStudentAccount());
//				weChatbanding.setPassword(we.getPassword());
//				weChatbanding.setStudentClass(we.getStudentClass());
//				weChatbanding.setStudentName(we.getStudentName());
//			}
//		}
//		this.weChatbandingService.save(weChatbanding);
//		return BasicDataResult.build(200, "切换帐号成功！",null);
//	}
//	
//
//	/**
//	 * 解除绑定
//	 * 
//	 * @param code
//	 * @return
//	 */
//	@RequestMapping("/unbinding")
//	public ModelAndView unbinding(HttpServletRequest request) {
//		String code = request.getParameter("code");
//		ModelAndView model = new ModelAndView();
//		if (Common.isEmpty(code)) {
//			String redirect_uri = Contents.URL + "/wechat-app/wechat/unbinding";
//			return new ModelAndView(new RedirectView(
//					"https://open.weixin.qq.com/connect/oauth2/authorize?" + "appid=" + Contents.APPID + "&redirect_uri="
//							+ redirect_uri + "&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect"));
//		}
//		NSNUserInfo nsn = this.weChatbandingService.findWechatNsn(code);
//		if(Common.isEmpty(nsn)){
//			model.setViewName("redirect:toAuthor");
//			return model;
//		}
//		WeChatbanding bd = this.weChatbandingService.findWeChatbandingByOpenId(nsn.getOpenid());
//		
//		if(Common.isEmpty(bd)){
//			model.setViewName("redirect:toAuthor");
//			return model;
//		}
//		
//		if(bd.getListbandings().size()==0){
//			model.setViewName("redirect:toAuthor");
//			return model;
//		}
//		
//		List<WeChatbandingStudent> list = bd.getListbandings();
//		
//		if(list.size()==1){
//			if (Common.isNotEmpty(bd)) {
//				this.weChatbandingService.remove(bd);
//			}
//			model.setViewName("redirect:toAuthor");
//			return model;
//		}else if(list.size()>1){
//			//获取当前正在使用的帐号，并且删除
//			String account = bd.getStudentAccount();
//			List<WeChatbandingStudent> newlist = new ArrayList<>();
//			for(WeChatbandingStudent we:list){
//				if(!we.getStudentAccount().equals(account)){
//					newlist.add(we);
//				}
//			}
//			
//			bd.setStudentAccount(newlist.get(0).getStudentAccount());
//			bd.setPassword(newlist.get(0).getPassword());
//			bd.setStudentName(newlist.get(0).getStudentName());
//			bd.setStudentClass(newlist.get(0).getStudentClass());
//			bd.setListbandings(newlist);
//			this.weChatbandingService.save(bd);
//			model.setViewName("redirect:index");
//			return model;
//		}else {
//			model.setViewName("redirect:toAuthor");
//			return model;
//		}
//		
//		
//	
//	}
//	/**
//	 * 修改学生
//	 * 
//	 * @param code
//	 * @return
//	 */
//	@RequestMapping("/editStudent")
//	public ModelAndView editStudent(HttpServletRequest request,String id,String name) {
//		ModelAndView model = new ModelAndView();
//		
//		if(Common.isEmpty(id)||Common.isEmpty(name)){
//			model.setViewName("redirect:index");
//			return model;
//		}
//		
//	
//		if(Common.isNotEmpty(id)){
//			WeChatbanding we = this.weChatbandingService.findOneById(id, WeChatbanding.class);
//			//更新学生信息
//			this.weChatbandingService.updateStudentName(we, name);
//		}
//		
//		model.setViewName("redirect:index");
//		return model;
//
//	}
//	
//	
//// 	
////	 修复数据库绑定用
////	/**
////	 * 修复左右的账号信息
////	 * @return
////	 */
////	@RequestMapping("/repair")
////	public String repair(){
////		
////		List<WeChatbanding> list = this.weChatbandingService.find(new Query(), WeChatbanding.class);
////		
////		for(WeChatbanding we :list){
////			
////			WeChatbandingStudent ws = new WeChatbandingStudent();
////			List<WeChatbandingStudent> l = new ArrayList<>();
////			ws.setPassword(we.getPassword());
////			ws.setStudentAccount(we.getStudentAccount());
////			ws.setStudentClass(we.getStudentClass());
////			ws.setStudentName(we.getStudentName());
////			l.add(ws);
////			we.setListbandings(l);
////			log.info("修复"+we);
////			this.weChatbandingService.save(we);
////		}
////		
////		
////		
////		
////	
////		return "修复完成";
////	}
////	
//	
//	
//	
//	
//	
//	
//	
//	
//
//	/**
//	 * 跳转到编辑学生信息页面
//	 * 
//	 * @param session
//	 * @param request
//	 * @return
//	 */
//	@RequestMapping("/editpage")
//	public ModelAndView editpage(HttpServletRequest request) {
//		String code = request.getParameter("code");
//		ModelAndView model = new ModelAndView();
//		if (Common.isEmpty(code)) {
//			String redirect_uri = Contents.URL + "/wechat-app/wechat/editpage";
//			return new ModelAndView(new RedirectView(
//					"https://open.weixin.qq.com/connect/oauth2/authorize?" + "appid=" + Contents.APPID + "&redirect_uri="
//							+ redirect_uri + "&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect"));
//		}
//		NSNUserInfo nsn = this.weChatbandingService.findWechatNsn(code);
//		if (Common.isEmpty(nsn)) {
//			// 授权信息为空重定向到登陆页面
//			model.setViewName("redirect:editpage");
//			return model;
//		}
//		WeChatbanding bd = this.weChatbandingService.findWeChatbandingByOpenId(nsn.getOpenid());
//		if (Common.isEmpty(bd)) {
//			model.setViewName("redirect:weChatAuth");
//			return model;
//		}
//		model.addObject("bd", bd);
//		model.setViewName("wechat/front/edit");
//		return model;
//	}
//	
//	
//	
//	
//	@RequestMapping("/bindManagement")
//	public ModelAndView bindManagement( HttpServletRequest request,String openId){
//		ModelAndView model = new ModelAndView();
//		model.setViewName("wechat/front/binds");
//		String code = request.getParameter("code");
//		String state = request.getParameter("state");
//		if (Common.isEmpty(code)) {
//			String redirect_uri = Contents.URL + "/wechat-app/wechat/bindManagement";
//			return new ModelAndView(new RedirectView(
//					"https://open.weixin.qq.com/connect/oauth2/authorize?" + "appid=" + Contents.APPID + "&redirect_uri="
//							+ redirect_uri + "&response_type=code&scope=snsapi_userinfo&state="+openId+"#wechat_redirect"));
//		}
//		if(Common.isNotEmpty(state)){
//			openId = state;
//		}
////		NSNUserInfo nsn = this.weChatbandingService.findWechatNsn(code);
////		if (Common.isEmpty(nsn)) {
////			// 授权信息为空重定向到登陆页面
////			model.setViewName("redirect:toAuthor");
////			return model;
////		}
//		
//		WeChatbanding bd = this.weChatbandingService.findWeChatbandingByOpenId(openId);
//		if (Common.isEmpty(bd)) {
//			model.setViewName("redirect:weChatAuth");
//			return model;
//		}
//		model.addObject("bd", bd);
//		return model;
//	}
//	
//	
//	
//	
//	
//	
//	
//
//}
