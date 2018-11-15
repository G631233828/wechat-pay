package zhongchiedu.wechat.util.jsapi_Ticket;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import zhongchiedu.wechat.util.WeixinUtil;
import zhongchiedu.wechat.util.accessToken.AccessToken;
import zhongchiedu.wechat.util.token.WeChatToken;

/**
 * 获取企业Token.信息。
 */
public class WeChatJsapi_ticket {

	private static final Logger log = LoggerFactory.getLogger(WeChatJsapi_ticket.class);

	private AccessToken accessToken;

	private Jsapi_Ticket jsapi_Ticket;
	
	private static String appid;
	private static String appsecret;

	public AccessToken getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(AccessToken accessToken) {
		this.accessToken = accessToken;
	}

	public Jsapi_Ticket getJsapi_Ticket() {
		return jsapi_Ticket;
	}

	public void setJsapi_Ticket(Jsapi_Ticket jsapi_Ticket) {
		this.jsapi_Ticket = jsapi_Ticket;
	}

	// 过期时间
	private static String outofTimeJsapi_ticket = "0";

	static SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public WeChatJsapi_ticket(String appid,String appsecret) {
		WeixinUtil weixinUtil = new WeixinUtil();
		this.appid= appid;
		this.appsecret=appsecret;

		try {
			Date date = new Date();
			Calendar dar = Calendar.getInstance();
			dar.setTime(date);
			dar.add(java.util.Calendar.MINUTE, 50);

			// 设置过期时间
			outofTimeJsapi_ticket = dft.format(dar.getTime());
			log.info("当前的outofTimeJsapi_ticket过期时间为：" + outofTimeJsapi_ticket);
			
			accessToken = WeChatToken.InnerSingletion.single.getAccessToken();
			
			log.info("WeChatJsapi_ticket accessToken"+accessToken.getToken());
			log.info("accessToken != null:"+(accessToken.getToken()!=null));
			log.info("accessToken != '':"+(accessToken.getToken()!=""));

			if (accessToken.getToken()!=null||accessToken.getToken()!="") {

				jsapi_Ticket = weixinUtil.getJsapi_Ticket(accessToken.getToken(),appid,appsecret);
				
				log.info("获取到的jsapi_Ticket"+jsapi_Ticket);
			}

			log.info("load jsapi_Ticket success");
		} catch (Exception e) {
			log.error("Load jsapi_Ticket Error");
		}
	}

	
	
	public static class InnerSingletion {
		public static WeChatJsapi_ticket single = new WeChatJsapi_ticket(appid , appsecret);
	}

	public static WeChatJsapi_ticket getInstance() {

		Calendar dar = Calendar.getInstance();
		String dateimeNow = dft.format(dar.getTime());
		boolean flag = false;

		if (!outofTimeJsapi_ticket.equals("0")) {
			flag = compare_date(dateimeNow, outofTimeJsapi_ticket);
		}
		log.info("Jsapi_ticket状态：" + flag);

		// 设定时间戳，7200,
		if (flag || outofTimeJsapi_ticket.equals("0")) {
			InnerSingletion.single = new WeChatJsapi_ticket(appid,appsecret);
		}
		return WeChatJsapi_ticket.InnerSingletion.single;

	}

	public static void main(String[] args) throws InterruptedException {

		SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		Calendar dar = Calendar.getInstance();
		dar.setTime(date);

		dar.add(java.util.Calendar.HOUR_OF_DAY, 2);
		System.out.println(dft.format(dar.getTime()));
		String dateime1 = dft.format(dar.getTime());
		String datetime2 = "2018-04-27 18:55:30";

		boolean i = compare_date(dateime1, datetime2);
		System.out.println(i);
	}

	public static boolean compare_date(String DATE1, String DATE2) {

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		try {
			Date dt1 = df.parse(DATE1);
			Date dt2 = df.parse(DATE2);
			if (dt1.getTime() > dt2.getTime()) {
				log.info("Jsapi_ticket已经过期！");
				return true;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}

		return false;
	}

}
