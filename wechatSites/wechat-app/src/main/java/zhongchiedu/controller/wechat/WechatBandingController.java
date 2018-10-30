package zhongchiedu.controller.wechat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import zhongchiedu.common.utils.BasicDataResult;
import zhongchiedu.common.utils.Common;
import zhongchiedu.common.utils.ReadProperties;
import zhongchiedu.school.pojo.HomeWork;
import zhongchiedu.school.pojo.School;
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
		// 1.用户访问获取code如果没有获取到code则重定向
		String code = request.getParameter("code");
//		School school = this.schoolService.findOneByQuery(new Query(), School.class);
//		String appid = school.getAppid(); // 获取appid
//		String url = school.getDoMainName();// 获取域名
		
		String appid = ReadProperties.getObjectProperties("application.properties","wechat.appid");
		String appsecret = ReadProperties.getObjectProperties("application.properties","wechat.appsecret");
		String url =ReadProperties.getObjectProperties("application.properties","wechat.serverUrl");
		if (Common.isEmpty(code)) {
			String redirect_uri = url + "/wechat-app/wechat/weChatAuth";
			// 注意：基于snsapi_base和snsapi_userinfo获取用户信息是不需要关注公众号。对于已关注公众号的用户，如果用户从公众号的会话或者自定义菜单进入本公众号的网页授权页，即使是scope为snsapi_userinfo，也是静默授权，用户无感知。
			return new ModelAndView(new RedirectView(
					"https://open.weixin.qq.com/connect/oauth2/authorize?" + "appid=" + appid + "&redirect_uri="
							+ redirect_uri + "&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect"));
		} else {
			// 2.通过用户code获取用户的信息，openId
			NSNUserInfo nsn = WeixinUtil.baseWeChatLogin(appid,appsecret, code);
			String openId = nsn.getOpenid();
			// 3.通过用户的openId在wechatbanding表中进行查询，
			WeChatbanding weChatbinding = this.weChatbandingService.findWeChatbandingByOpenId(openId);
			if (Common.isNotEmpty(weChatbinding)) {
				// 4.如果获取的wechat绑定不为空，那么获取用户名密码
				String account = weChatbinding.getStudentAccount();
				String password = weChatbinding.getPassword();
				// 使用帐号密码进行登录
				String result = this.homeWorkService.checkLogin(account, password,null);
				if (result.equals("error")) {
					// 登录失败，跳转到登录界面
					String redirect_uri = url + "/wechat-app/wechat/toAuthor";
					// 登录失败，用户名或者密码错误（已经发生了更改）
					return new ModelAndView(new RedirectView("https://open.weixin.qq.com/connect/oauth2/authorize?"
							+ "appid=" + appid + "&redirect_uri=" + redirect_uri
							+ "&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect"));
				} else {
					// 跳转到登录成功界面
					String redirect_uri = url + "/wechat-app/wechat/index";
					// 登录成功
					return new ModelAndView(new RedirectView("https://open.weixin.qq.com/connect/oauth2/authorize?"
							+ "appid=" + appid + "&redirect_uri=" + redirect_uri
							+ "&response_type=code&scope=snsapi_userinfo&state="+openId+"#wechat_redirect"));
				}
			} else {
				// 跳转到登录成功界面
				String redirect_uri = url + "/wechat-app/wechat/toAuthor";
				// 登录成功
				return new ModelAndView(new RedirectView("https://open.weixin.qq.com/connect/oauth2/authorize?"
						+ "appid=" + appid + "&redirect_uri=" + redirect_uri
						+ "&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect"));
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
//		School school = this.schoolService.findOneByQuery(new Query(), School.class);
//		String appid = school.getAppid(); // 获取appid
//		String url = school.getDoMainName();// 获取域名
		String appid = ReadProperties.getObjectProperties("application.properties","wechat.appid");
		//String appsecret = ReadProperties.getObjectProperties("application.properties","wechat.appsecret");
		String url =ReadProperties.getObjectProperties("application.properties","wechat.serverUrl");
		if(Common.isEmpty(code)){
			String redirect_uri = url + "/wechat-app/wechat/toAuthor";
			// 登录失败，用户名或者密码错误（已经发生了更改）
			return new ModelAndView(new RedirectView("https://open.weixin.qq.com/connect/oauth2/authorize?"
					+ "appid=" + appid + "&redirect_uri=" + redirect_uri
					+ "&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect"));
		}
		ModelAndView model = new ModelAndView();
		model.setViewName("wechat/front/login");
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
		String code = request.getParameter("code");
		//获取登录用户的信息
//		School school = this.schoolService.findOneByQuery(new Query(), School.class);
//		String appid = school.getAppid(); // 获取appid
//		String url = school.getDoMainName();// 获取域名
		String appid = ReadProperties.getObjectProperties("application.properties","wechat.appid");
		String appsecret = ReadProperties.getObjectProperties("application.properties","wechat.appsecret");
		String url =ReadProperties.getObjectProperties("application.properties","wechat.serverUrl");
		NSNUserInfo nsn = WeixinUtil.baseWeChatLogin(appid, appsecret, code);
		String openId = nsn.getOpenid();
		if(Common.isEmpty(code)){
			String redirect_uri = url + "/wechat-app/wechat/toAuthor";
			// 登录失败，用户名或者密码错误（已经发生了更改）
			return new ModelAndView(new RedirectView("https://open.weixin.qq.com/connect/oauth2/authorize?"
					+ "appid=" + appid + "&redirect_uri=" + redirect_uri
					+ "&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect"));
		}
		ModelAndView model = new ModelAndView();
		WeChatbanding we = this.weChatbandingService.findWeChatbandingByOpenId(openId);
		if(Common.isNotEmpty(we)){
			model.addObject("wechatbinding", we);
		}else{
			String redirect_uri = url + "/wechat-app/wechat/toAuthor";
			// 登录失败，用户名或者密码错误（已经发生了更改）
			return new ModelAndView(new RedirectView("https://open.weixin.qq.com/connect/oauth2/authorize?"
					+ "appid=" + appid + "&redirect_uri=" + redirect_uri
					+ "&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect"));
		}
		model.addObject("code", code);
		model.setViewName("wechat/front/index");
		return model;
	}

	/**
	 * 作业
	 * 
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping("/homework")
	public ModelAndView homework(HttpSession session, HttpServletRequest request) {
		ModelAndView model = new ModelAndView();
		String state = request.getParameter("state");
		String date = Common.isNotEmpty(request.getParameter("date"))?request.getParameter("date"):null;
		if(Common.isNotEmpty(state)&&!state.equals("STATE")){
			date= state;
		}
		String code = request.getParameter("code");
		
//		School school = this.schoolService.findOneByQuery(new Query(), School.class);
//		String appid = school.getAppid(); // 获取appid
//		String url = school.getDoMainName();// 获取域名
		
		String appid = ReadProperties.getObjectProperties("application.properties","wechat.appid");
		String appsecret = ReadProperties.getObjectProperties("application.properties","wechat.appsecret");
		String url =ReadProperties.getObjectProperties("application.properties","wechat.serverUrl");
		if(Common.isEmpty(code)){
			String redirect_uri = url + "/wechat-app/wechat/homework";
			if(Common.isEmpty(date)){
				date ="STATE";
			}
				
			return new ModelAndView(new RedirectView("https://open.weixin.qq.com/connect/oauth2/authorize?"
					+ "appid=" + appid + "&redirect_uri=" + redirect_uri
					+ "&response_type=code&scope=snsapi_userinfo&state="+date+"#wechat_redirect"));
		}
		// 2.通过用户code获取用户的信息，openId
		NSNUserInfo nsn = WeixinUtil.baseWeChatLogin(appid, appsecret, code);
		String openId = nsn.getOpenid();
		// 3.通过用户的openId在wechatbanding表中进行查询，
		WeChatbanding weChatbinding = this.weChatbandingService.findWeChatbandingByOpenId(openId);
		// 4.如果能获取到用户的绑定信息
		if (Common.isNotEmpty(weChatbinding)) {
			String account = weChatbinding.getStudentAccount();
			String password = weChatbinding.getPassword();
			// 使用帐号密码进行登录
			String result = this.homeWorkService.checkLogin(account,password,date);
			if (result.equals("error")) {
				// 登录失败，跳转到登录界面
				String redirect_uri = url + "/wechat-app/wechat/toAuthor";
				// 登录失败，用户名或者密码错误（已经发生了更改）
				return new ModelAndView(new RedirectView("https://open.weixin.qq.com/connect/oauth2/authorize?"
						+ "appid=" + appid + "&redirect_uri=" + redirect_uri
						+ "&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect"));
			}else{
				//开始解析返回信息，然后返回到页面
				HomeWork homeWork = this.homeWorkService.jsoupGetHomeWork(result);
				model.addObject("homeWork", homeWork);
				model.addObject("date", date);
				model.setViewName("wechat/front/homework");
				return model;
			}
		}else{
			// 登录失败，跳转到登录界面
			String redirect_uri = url + "/wechat-app/wechat/toAuthor";
			// 登录失败，用户名或者密码错误（已经发生了更改）
			return new ModelAndView(new RedirectView("https://open.weixin.qq.com/connect/oauth2/authorize?"
					+ "appid=" + appid + "&redirect_uri=" + redirect_uri
					+ "&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect"));
			
		}
	
	}

	/**
	 * 进行绑定操作
	 * 
	 * @return
	 */
	@RequestMapping("/toBinding")
	@ResponseBody
	public BasicDataResult toBinding(String account, String password, String code, HttpServletRequest request) {
//		School school = this.schoolService.findOneByQuery(new Query(), School.class);
		String appid = ReadProperties.getObjectProperties("application.properties","wechat.appid");
		String appsecret = ReadProperties.getObjectProperties("application.properties","wechat.appsecret");
		String url =ReadProperties.getObjectProperties("application.properties","wechat.serverUrl");
		// 验证输入的信息是否为空
		if (Common.isNotEmpty(account) && Common.isNotEmpty(password)&&Common.isNotEmpty(code)) {
			// 1.验证帐号密码是否是正确的，如果正确则对帐号进行登录然后解析
			String result = this.homeWorkService.checkLogin(account, password,null);
			if (result.equals("error")) {
				return BasicDataResult.build(400, "帐号或密码错误", null);
			} else {
				// 2.获取当前访问用户的code，并且通过code去获取用户的信息
				NSNUserInfo nsn = WeixinUtil.baseWeChatLogin(appid, appsecret, code);
				if(Common.isEmpty(nsn)){
					return BasicDataResult.build(400, "正在进行绑定，请不要重复提交！", null);
				}
				WeChatbanding banding = new WeChatbanding();
				HomeWork homeWork = this.homeWorkService.jsoupGetHomeWork(result);
				banding.setNsnUserInfo(nsn);
				banding.setStudentAccount(account);
				banding.setPassword(password);
				banding.setOpenId(nsn.getOpenid());
				banding.setStudentClass(homeWork.getGradeName() + homeWork.getClassName());
				banding.setStudentName(homeWork.getStudentName());
				// 3.保存绑定信息
				this.weChatbandingService.SaveOrUpdateWeChatbanding(banding);
				return BasicDataResult.build(200, "用户绑定成功", null);
			}
		}
		return BasicDataResult.build(400, "绑定过程中出现未知异常，请联系管理员", null);
	}

	
	
	
	/**
	 * 解除绑定
	 * @param code
	 * @return
	 */
	@RequestMapping("/unbinding")
	public ModelAndView unbinding(HttpServletRequest request){
		
//		School school = this.schoolService.findOneByQuery(new Query(), School.class);
//		String appid = school.getAppid(); // 获取appid
//		String url = school.getDoMainName();// 获取域名
		
		String appid = ReadProperties.getObjectProperties("application.properties","wechat.appid");
		String appsecret = ReadProperties.getObjectProperties("application.properties","wechat.appsecret");
		String url =ReadProperties.getObjectProperties("application.properties","wechat.serverUrl");
		
		String code = request.getParameter("code");
		if(Common.isEmpty(code)){
			String redirect_uri = url + "/wechat-app/wechat/unbinding";
			return new ModelAndView(new RedirectView("https://open.weixin.qq.com/connect/oauth2/authorize?"
					+ "appid=" + appid + "&redirect_uri=" + redirect_uri
					+ "&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect"));
		}
		NSNUserInfo nsn = WeixinUtil.baseWeChatLogin(appid,appsecret, code);
		String openId = nsn.getOpenid();
		WeChatbanding bd = this.weChatbandingService.findWeChatbandingByOpenId(openId);
		if(Common.isNotEmpty(bd)){
			this.weChatbandingService.remove(bd);
		}
		String redirect_uri = url + "/wechat-app/wechat/toAuthor";
		// 登录成功
		return new ModelAndView(new RedirectView("https://open.weixin.qq.com/connect/oauth2/authorize?"
				+ "appid=" + appid + "&redirect_uri=" + redirect_uri
				+ "&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect"));
	}
	
	

	
	
	
	
	
}
