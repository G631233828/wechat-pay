package zhongchiedu.wechat.util.token;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import zhongchiedu.common.utils.ReadProperties;
import zhongchiedu.wechat.util.WeixinUtil;
import zhongchiedu.wechat.util.accessToken.AccessToken;

/**
 * 获取企业Token.信息。
 */
public class WeChatToken {

	private static final Logger log = LoggerFactory.getLogger(WeChatToken.class);

	private AccessToken accessToken;

	// 过期时间
	private static String outofTimeAccessToken="0";
	static SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public WeChatToken() {
		WeixinUtil weixinUtil = new WeixinUtil();

		try {
			Date date = new Date();
			Calendar dar = Calendar.getInstance();
			dar.setTime(date);
			dar.add(java.util.Calendar.HOUR_OF_DAY, 1);

			// 设置过期时间
			outofTimeAccessToken = dft.format(dar.getTime());
			log.info("当前的outofTimeAccessToken过期时间为：" + outofTimeAccessToken);
			
			accessToken = weixinUtil.getAccessToken(ReadProperties.getObjectProperties("application.properties","wechat.appid"),
					ReadProperties.getObjectProperties("application.properties","wechat.appsecret"));
//			accessToken = weixinUtil.getAccessToken("wx40d294a89bcd9fcb",
//					"e48b8b33730cb8bf7ed2aa26e671549b");
//			accessToken = weixinUtil.getAccessToken(Configure.getInstance().getValueString("APPID"),
//					Configure.getInstance().getValueString("APPSECRET"));"wx40d294a89bcd9fcb","e48b8b33730cb8bf7ed2aa26e671549b"

			log.info("load Token success");
		} catch (Exception e) {
			log.error("Load Token Error");
		}
	}

	public AccessToken getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(AccessToken accessToken) {
		this.accessToken = accessToken;
	}

	public static class InnerSingletion {
		public static WeChatToken single = new WeChatToken();
	}

	public static WeChatToken getInstance() {

		Calendar dar = Calendar.getInstance();
		String dateimeNow = dft.format(dar.getTime());
		
		boolean flag  = false;
		
		if(!outofTimeAccessToken.equals("0")){
			flag= compare_date(dateimeNow, outofTimeAccessToken);
		}
		log.info("access_Token状态："+flag);

		// 设定时间戳，7200,
		if (flag||outofTimeAccessToken.equals("0")) {
			InnerSingletion.single = new WeChatToken();
		}
		return WeChatToken.InnerSingletion.single;

	}


	public static boolean compare_date(String DATE1, String DATE2) {

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		try {
			Date dt1 = df.parse(DATE1);
			Date dt2 = df.parse(DATE2);
			if (dt1.getTime() > dt2.getTime()) {
				log.info("access_token已经过期！");
				return true;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}

		return false;
	}

}
