package zhongchiedu.controller.wechat;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import net.sf.json.JSONObject;
import zhongchiedu.common.utils.Common;
import zhongchiedu.common.utils.HttpUtil;
import zhongchiedu.common.utils.XMLClient;
import zhongchiedu.wechat.oauto2.NSNUserInfo;
import zhongchiedu.wechat.pay.WXPayConfig;
import zhongchiedu.wechat.pay.WXPayUtil;
import zhongchiedu.wechat.util.WeixinUtil;

@RequestMapping("/wechat")
@Controller
public class WeChatTest {

	private static final Logger log = LoggerFactory.getLogger(WeChatTest.class);

	// wx40d294a89bcd9fcb
	// e48b8b33730cb8bf7ed2aa26e671549b
	// openId ooiMKv7cqR-2EgkeC9LdATpr-mbY
	/**
	 * 微信授权验证
	 * 
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping("/weChatAuth")
	public ModelAndView wechatAuth(HttpSession session, HttpServletRequest request) {
		System.out.println(11);
		System.out.println(11);
		System.out.println(11);

		ModelAndView model = new ModelAndView();

		model.setViewName("test/toauthor");

		String code = request.getParameter("code");
		log.info("weChat Code: " + code);

		if (Common.isEmpty(code)) {
			log.info("未获取请用户的openId,请求微信授权");
			// 注意：基于snsapi_base和snsapi_userinfo获取用户信息是不需要关注公众号。对于已关注公众号的用户，如果用户从公众号的会话或者自定义菜单进入本公众号的网页授权页，即使是scope为snsapi_userinfo，也是静默授权，用户无感知。
			return new ModelAndView(new RedirectView("https://open.weixin.qq.com/connect/oauth2/authorize?"
					+ "appid=wx40d294a89bcd9fcb&redirect_uri=http://zhongchiedu.com/wechat-app/wechat/successPay"
					+ "&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect"));
		}
		
		//通过code获取用户信息
        NSNUserInfo nsn=WeixinUtil.baseWeChatLogin(code);
        model.addObject("openId", nsn.getOpenid());
         System.out.println(nsn.getOpenid());
         System.out.println(nsn.getOpenid());
         System.out.println(nsn.getOpenid());
         System.out.println(nsn.getOpenid());

        
		
		return model;
	}

	/**
	 * 测试访问页面
	 * 
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping("/test")
	public ModelAndView test(HttpSession session, HttpServletRequest request) {

		System.out.println(22);
		System.out.println(22);
		System.out.println(22);
		ModelAndView model = new ModelAndView();

		model.setViewName("test/toauthor");

		return model;
	}
	
	/**
	 * 跳转到支付成功
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping("/successPay")
	public ModelAndView successpay(HttpSession session, HttpServletRequest request) {

		System.out.println("successPay");
		System.out.println("successPay");
		System.out.println("successPay");
		System.out.println("successPay");

		String code = request.getParameter("code");
		log.info("successPay Code" + code);
		log.info("successPay Code" + code);
		log.info("successPay Code" + code);
		log.info("successPay Code" + code);
		ModelAndView model = new ModelAndView();

		model.setViewName("test/success");

		return model;
	}
	
	
	
	
	@RequestMapping("/getopenId")
	public ModelAndView getopenId(HttpSession session, HttpServletRequest request) throws Exception {
	ModelAndView model = new ModelAndView();
	model.setViewName("test/success");


	Map<String, String> reqData = new HashMap<>();
	String openId = "ooiMKv7cqR-2EgkeC9LdATpr-mbY";
	String nonce_str = WXPayUtil.generateNonceStr();
	String out_trade_no = Common.getUUID();
	String timeStamp = WXPayUtil.getCurrentTimestamp() + "";

	Map<String, String> pay = new HashMap<>();
	pay.put("money", "200");
	String attach = WXPayUtil.mapToXml(pay);
	reqData.put("appid", "wx40d294a89bcd9fcb");
	reqData.put("mch_id","1516332071");//微信支付商户号==登陆微信支付后台，即可看到
	reqData.put("device_info","WEB");//设备号==终端设备号(门店号或收银设备ID)，注意：PC网页或公众号内支付请传"WEB"
	reqData.put("body", "test");//商品描述==商品或支付单简要描述（建议刚开始最好传英文，尽量先别传汉子，要不之后签名出问题都没法查）
	reqData.put("trade_type", "JSAPI");//交易类型==取值如下：JSAPI，NATIVE，APP。我们这里使用的JSAPI。
	reqData.put("nonce_str", nonce_str);
	//异步接收微信支付结果通知的回调地址，通知url必须为外网可以访问的url，并且不能携带参数
	reqData.put("notify_url", "http://zhongchiedu.com/wechat-app/wechat/successPay");
	reqData.put("out_trade_no","000001");//商户系统内部订单号，要求32个字符内只能是数字，大小写字母_-|*@
	reqData.put("total_fee","1");//支付1分钱
	reqData.put("attach",attach);//自定义参数
	reqData.put("sign",WXPayUtil.generateSignature(reqData,"zhongchixinxijishushanghaigongsh"));//签名

	String reqDataXml = WXPayUtil.mapToXml(reqData);
	
	String a = XMLClient.sendXMLDataByPost("https://api.mch.weixin.qq.com/pay/unifiedorder", reqDataXml);
	System.out.println("-------------------" + a);
	System.out.println("-------------------" + a);
	System.out.println("-------------------" + a);
		
		return model;
	}
	
	
	
	
	
	
	

//	public void wechatPayTest() {
//		try {
//			Map<String, String> reqData = new HashMap<>();
//			String openId = "ooiMKv7cqR-2EgkeC9LdATpr-mbY";
//			String nonce_str = WXPayUtil.generateNonceStr();
//			String out_trade_no = Common.getUUID();
//			String timeStamp = WXPayUtil.getCurrentTimestamp() + "";
//
//			Map<String, String> pay = new HashMap<>();
//			pay.put("money", "200");
//			String attach = WXPayUtil.mapToXml(pay);
//			reqData.put("appid", "wx40d294a89bcd9fcb");
//			reqData.put("mch_id","1516332071");//微信支付商户号==登陆微信支付后台，即可看到
//			reqData.put("device_info","WEB");//设备号==终端设备号(门店号或收银设备ID)，注意：PC网页或公众号内支付请传"WEB"
//			reqData.put("body", "test");//商品描述==商品或支付单简要描述（建议刚开始最好传英文，尽量先别传汉子，要不之后签名出问题都没法查）
//			reqData.put("trade_type", "JSAPI");//交易类型==取值如下：JSAPI，NATIVE，APP。我们这里使用的JSAPI。
//			reqData.put("nonce_str", nonce_str);
//			//异步接收微信支付结果通知的回调地址，通知url必须为外网可以访问的url，并且不能携带参数
//			reqData.put("notify_url", "http://zhongchiedu.com/wechat-app/wechat/successPay");
//			reqData.put("out_trade_no","000001");//商户系统内部订单号，要求32个字符内只能是数字，大小写字母_-|*@
//			reqData.put("total_fee","1");//支付1分钱
//			reqData.put("sign",WXPayUtil.generateSignature(reqData,"zhongchixinxijishushanghaigongsh"));//签名
//		
//			String reqDataXml = WXPayUtil.mapToXml(reqData);
//			
//			
//			JSONObject jsonObject = httpRequest("https://api.mch.weixin.qq.com/pay/unifiedorder", "GET", null);
//			System.out.println(reqDataXml);
//			System.out.println(reqDataXml);
//			System.out.println(reqDataXml);
//			System.out.println(reqDataXml);
//			
//			
//		//	Map<String,String> resDate = WXPayUtil.xmlToMap(strXML)
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	
	
	public static void main(String[] args) throws Exception {

		
		
		Map<String, String> reqData = new HashMap<>();
		String openId = "ooiMKv7cqR-2EgkeC9LdATpr-mbY";
		String nonce_str = WXPayUtil.generateNonceStr();
		String out_trade_no = Common.getUUID();
		String timeStamp = WXPayUtil.getCurrentTimestamp() + "";

		Map<String, String> pay = new HashMap<>();
		pay.put("money", "200");
		String attach = WXPayUtil.mapToXml(pay);
		reqData.put("appid", "wx40d294a89bcd9fcb");
		reqData.put("mch_id","1516332071");//微信支付商户号==登陆微信支付后台，即可看到
		reqData.put("device_info","WEB");//设备号==终端设备号(门店号或收银设备ID)，注意：PC网页或公众号内支付请传"WEB"
		reqData.put("body", "test");//商品描述==商品或支付单简要描述（建议刚开始最好传英文，尽量先别传汉子，要不之后签名出问题都没法查）
		reqData.put("trade_type", "JSAPI");//交易类型==取值如下：JSAPI，NATIVE，APP。我们这里使用的JSAPI。
		reqData.put("nonce_str", nonce_str);
		//异步接收微信支付结果通知的回调地址，通知url必须为外网可以访问的url，并且不能携带参数
		reqData.put("notify_url", "http://zhongchiedu.com/wechat-app/wechat/successPay");
		reqData.put("out_trade_no","000001");//商户系统内部订单号，要求32个字符内只能是数字，大小写字母_-|*@
		reqData.put("total_fee","1");//支付1分钱
		reqData.put("attach",attach);//自定义参数
		reqData.put("sign",WXPayUtil.generateSignature(reqData,"zhongchixinxijishushanghaigongsh"));//签名
	
		String reqDataXml = WXPayUtil.mapToXml(reqData);
		
		System.out.println(reqDataXml);
		
		String a = XMLClient.sendXMLDataByPost("https://api.mch.weixin.qq.com/pay/unifiedorder", reqDataXml);
		System.out.println("-------------------" + a);
	}
	
	
	
	
	
	
	

}
