package zhongchiedu.wechat.util;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.UUID;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import zhongchiedu.common.utils.Common;
import zhongchiedu.wechat.oauto2.NSNUserInfo;
import zhongchiedu.wechat.oauto2.WeixinOauth2Token;
import zhongchiedu.wechat.pojo.createMenu.Menu;
import zhongchiedu.wechat.resp.Data;
import zhongchiedu.wechat.resp.UserGet;
import zhongchiedu.wechat.util.accessToken.AccessToken;
import zhongchiedu.wechat.util.jsapi_Ticket.Jsapi_Ticket;
import zhongchiedu.wechat.util.token.WeChatToken;
import zhongchiedu.wechat.util.token.WeChatToken.InnerSingletion;

/**
 * 公众平台通用接口工具类
 * 
 * @author fliay
 * @date 2018年8月13日 10:57:35
 */
@Repository
public class WeixinUtil {
	
	
	private static Logger log = (Logger) LoggerFactory.getLogger(WeixinUtil.class);

	// 获取access_token的接口地址（GET） 限200（次/天）
	public final static String access_token_url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
	
	public final static String jsapi_ticket_url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESSTOKEN&type=jsapi";// 这个url链接和参数不能变
	//通过access_token获取创建的微信菜单
	public final static String wechat_menu = "https://api.weixin.qq.com/cgi-bin/get_current_selfmenu_info?access_token=ACCESS_TOKEN";
	//客服消息发送，可用于消息推送等
	public final static String send_url = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=ACCESS_TOKEN";
	
	public final static String user_get_url="https://api.weixin.qq.com/cgi-bin/user/get?access_token=ACCESS_TOKEN";
	
	/**
	 * 获取access_token
	 * 
	 * @param appid
	 *            凭证
	 * @param appsecret
	 *            密钥
	 * @return
	 */
	public static AccessToken getAccessToken(String appid, String appsecret) {
		AccessToken accessToken = null;

		String requestUrl = access_token_url.replace("APPID", appid).replace("APPSECRET", appsecret);
		JSONObject jsonObject = httpRequest(requestUrl, "GET", null);
		// 如果请求成功
		if (null != jsonObject) {
			try {
				accessToken = new AccessToken();
				accessToken.setToken(jsonObject.getString("access_token"));
				accessToken.setExpiresIn(jsonObject.getInt("expires_in"));
			} catch (JSONException e) {
				accessToken = null;
				// 获取token失败
				log.error("获取token失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"),
						jsonObject.getString("errmsg"));
			}
		}
		return accessToken;
	}
	
	
/**
 * 获取微信jsapi
 * @param accessToken
 * @return
 */
	public static Jsapi_Ticket getJsapi_Ticket(String accessToken,String appid,String appsecret) {

		Jsapi_Ticket jsapi_Ticket = null;

		String requestUrl = jsapi_ticket_url.replace("ACCESSTOKEN", accessToken);

		JSONObject jsonObject = httpRequest(requestUrl, "GET", null);
		log.info("Jsapi_Ticket:+jsonObject");
		
		//如果返回的信息中的errorcode为42001说明accessToken无效需要重新获取accessToken
		int errorcode = jsonObject.getInt("errcode");
		log.info("getJsapi_Ticket errorcode:"+errorcode);
		if(errorcode == 42001){
			log.info("accessToken无效或过期，重新获取accessToken");
			WeChatToken.InnerSingletion.single=new WeChatToken();
			log.info("获取accessToken成功"+ WeChatToken.InnerSingletion.single.getAccessToken().getToken());
			requestUrl = jsapi_ticket_url.replace("ACCESSTOKEN", WeChatToken.InnerSingletion.single.getAccessToken().getToken());
			//需要重新发送
			jsonObject = httpRequest(requestUrl, "GET", null);
		}
		
		// 如果請求成功
		if (jsonObject != null) {
			try {
				jsapi_Ticket = new Jsapi_Ticket();
				jsapi_Ticket.setTicket(jsonObject.getString("ticket"));
				jsapi_Ticket.setExpiresIn(jsonObject.getInt("expires_in"));
			} catch (JSONException e) {
				jsapi_Ticket = null;
				log.error("获取jsapi_ticket失败errorcode:{} errmsg:{}", jsonObject.getInt("errorcode"),
						jsonObject.getString("errormsg"));
			}
		}

		return jsapi_Ticket;

	}

	// 菜单创建（POST） 限100（次/天）
	// appid:wx82359986ad1549fa
	// appSecret:3e938067550f786ec872efc7ea00cdf3
	public static String menu_create_url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";

	/**
	 * 创建菜单
	 * 
	 * @param menu
	 *            菜单实例
	 * @param accessToken
	 *            有效的access_token
	 * @return 0表示成功，其他值表示失败
	 */
	public static int createMenu(Menu menu, String accessToken) {
		int result = 0;

		// 拼装创建菜单的url
		String url = menu_create_url.replace("ACCESS_TOKEN", accessToken);
		// 将菜单对象转换成json字符串
		String jsonMenu = JSONObject.fromObject(menu).toString();
		// 调用接口创建菜单
		JSONObject jsonObject = httpRequest(url, "POST", jsonMenu);

		if (null != jsonObject) {
			if (0 != jsonObject.getInt("errcode")) {
				result = jsonObject.getInt("errcode");
				log.error("创建菜单失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));
			}
		}

		return result;
	}

	/**
	 * 发起https请求并获取结果
	 * 
	 * @param requestUrl
	 *            请求地址
	 * @param requestMethod
	 *            请求方式（GET、POST）
	 * @param outputStr
	 *            提交的数据
	 * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
	 */
	public static JSONObject httpRequest(String requestUrl, String requestMethod, String outputStr) {
		JSONObject jsonObject = null;
		StringBuffer buffer = new StringBuffer();
		try {
			// 创建SSLContext对象，并使用我们指定的信任管理器初始化
			TrustManager[] tm = { new MyX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();

			URL url = new URL(requestUrl);
			HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
			httpUrlConn.setSSLSocketFactory(ssf);

			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);
			// 设置请求方式（GET/POST）
			httpUrlConn.setRequestMethod(requestMethod);

			if ("GET".equalsIgnoreCase(requestMethod))
				httpUrlConn.connect();

			// 当有数据需要提交时
			if (null != outputStr) {
				OutputStream outputStream = httpUrlConn.getOutputStream();
				// 注意编码格式，防止中文乱码
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}

			// 将返回的输入流转换成字符串
			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();
			// 释放资源
			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();
			jsonObject = JSONObject.fromObject(buffer.toString());
		} catch (ConnectException ce) {
			log.error("Weixin server connection timed out.");
		} catch (Exception e) {
			log.error("https request error:{}", e);
		}
		return jsonObject;
	}

	public static String EnCode(String url) throws UnsupportedEncodingException {
		String result = url;
		result = java.net.URLEncoder.encode(url, "utf-8");
		return result;
	}

	/**
	 * 
	 * 获取网页受权凭证.
	 * 
	 * @author 此方法为成员属性方法,作者:Aaron Liu
	 * @version 版本:v1.0版本
	 * @see 对类、属性、方法的说明 参考转向，也就是相关主题
	 * @param appid
	 * @param appSecret
	 * @param code
	 * @return
	 * @return 返回值,请具体看类代码
	 * @exception https
	 *                ://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret
	 *                =SECRET&code=CODE&grant_type=authorization_code
	 */
	public static WeixinOauth2Token getOauth2AccessToken(String appid, String appSecret, String code) {
		WeixinOauth2Token wat = null;

		String requestUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";

		requestUrl = requestUrl.replace("APPID", appid);
		requestUrl = requestUrl.replace("SECRET", appSecret);
		requestUrl = requestUrl.replace("CODE", code);

		JSONObject jsonObject = httpRequest(requestUrl, "GET", null);

		if (null != jsonObject) {
			try {
				wat = new WeixinOauth2Token();

				wat.setAccessToken(jsonObject.getString("access_token"));
				wat.setExpiresIn(jsonObject.getInt("expires_in"));
				wat.setRefreshToken(jsonObject.getString("refresh_token"));
				wat.setOpenId(jsonObject.getString("openid"));
				wat.setScope(jsonObject.getString("scope"));

			} catch (Exception e) {
				wat = null;
				log.error("Error");
			}
		}
		return wat;

	}

	/**
	 * 刷新网页授权凭证.
	 * 
	 * @author 此方法为成员属性方法,作者:Aaron Liu @version 版本:v1.0版本 @see 对类、属性、方法的说明
	 *         参考转向，也就是相关主题 @return @return 返回值,请具体看类代码 @exception
	 */
	public static WeixinOauth2Token refreshOauth2AccessToken(String appid, String refreshToken) {

		WeixinOauth2Token wat = null;
		String requestUrl = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=APPID&grant_type=refresh_token&refresh_token=REFRESH_TOKEN";

		requestUrl = requestUrl.replace("APPID", appid);
		requestUrl = requestUrl.replace("REFRESH_TOKEN", refreshToken);

		JSONObject jsonObject = httpRequest(requestUrl, "GET", null);

		if (null != jsonObject) {
			try {
				wat = new WeixinOauth2Token();

				wat.setAccessToken(jsonObject.getString("access_token"));
				wat.setExpiresIn(jsonObject.getInt("expires_in"));
				wat.setRefreshToken(jsonObject.getString("refresh_token"));
				wat.setOpenId(jsonObject.getString("openid"));
				wat.setScope(jsonObject.getString("scope"));

			} catch (Exception e) {
				wat = null;
				log.error("Error");
			}
		}
		return wat;

	}

	
	/**
	 * 获取用户信息
	 * @param accessToken
	 * @param openid
	 * @return
	 */
	public static NSNUserInfo getSNSUserInfo(String accessToken, String openid) {
		NSNUserInfo snsUser = null;
		String requestUrl = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";

		requestUrl = requestUrl.replace("ACCESS_TOKEN", accessToken);
		requestUrl = requestUrl.replace("OPENID", openid);

		JSONObject j = httpRequest(requestUrl, "GET", null);

		if (null != j) {
			try {
				snsUser = new NSNUserInfo();
				snsUser.setOpenid(j.getString("openid"));
				snsUser.setNickname(j.getString("nickname"));

				snsUser.setSex(j.getInt("sex"));
				snsUser.setCountry(j.getString("country"));
				snsUser.setProvince(j.getString("province"));
				snsUser.setCity(j.getString("city"));
				snsUser.setHeadImgUrl(j.getString("headimgurl"));

				snsUser.setPrivilegeList(JSONArray.toList(j.getJSONArray("privilege"), List.class));
			} catch (Exception e) {
				log.error("Error");
			}
		}
		return snsUser;
	}

	@SuppressWarnings("rawtypes")
	public static String createSignBySha1(SortedMap<Object, Object> params) {
		StringBuffer sb = new StringBuffer();
		Set es = params.entrySet();
		Iterator it = es.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			String v = (String) entry.getValue();
			if (v != null && !v.equals("")) {
				sb.append(k + "=" + v + "&");
			}
		}
		String result = sb.toString().substring(0, sb.toString().length() - 1);
		return SHA1(result);
	}

	/**
	 * 获取时间戳(秒)
	 */
	public static String getTimestamp() {
		return String.valueOf(System.currentTimeMillis() / 1000);
	}

	/**
	 * 取出一个指定长度大小的随机正整数.
	 * 
	 * @param length
	 *            int 设定所取出随机数的长度。length小于11
	 * @return int 返回生成的随机数。
	 */
	public static int buildRandom(int length) {
		int num = 1;
		double random = Math.random();
		if (random < 0.1) {
			random = random + 0.1;
		}
		for (int i = 0; i < length; i++) {
			num = num * 10;
		}
		return (int) ((random * num));
	}

	/**WeChatToken
	 * 获取当前时间 yyyyMMddHHmmss
	 */
	public static String getCurrTime() {
		Date now = new Date();
		SimpleDateFormat outFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String s = outFormat.format(now);
		return s;
	}

	/**
	 * 生成随机字符串
	 */
	public static String getNonceStr() {
		// String currTime = getCurrTime();
		// String strTime = currTime.substring(8, currTime.length());
		// String strRandom = buildRandom(4) + "";
		// return strTime + strRandom;
		return UUID.randomUUID().toString().replace("-", "").substring(0, 16);
	}

	/**
	 * sha1加密算法
	 * 
	 * @param decript
	 * @return
	 */

	public static String SHA1(String decript) {
		try {
			MessageDigest digest = java.security.MessageDigest.getInstance("SHA-1");
			digest.update(decript.getBytes());
			byte messageDigest[] = digest.digest();
			// Create Hex String
			StringBuffer hexString = new StringBuffer();
			// 字节数组转换为 十六进制 数
			for (int i = 0; i < messageDigest.length; i++) {
				String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
				if (shaHex.length() < 2) {
					hexString.append(0);
				}
				hexString.append(shaHex);
			}
			return hexString.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static void main(String[] args) {
		// APPID=wxcab148803e627d91
		// APPSECRET=bd70520f4f3eb98bea66d638efd8b247
		// //1、获取AccessToken
		AccessToken accessToken =  WeChatToken.getInstance().getAccessToken();
		UserGet get =getusers(accessToken.getToken(),null);
		System.out.println(get);
		
//		// 2、获取Ticket
//		String jsapi_ticket = getTicket(accessToken.getToken());
//
//		// 3、时间戳和随机字符串
//		String noncestr = UUID.randomUUID().toString().replace("-", "").substring(0, 16);// 随机字符串
//		String timestamp = String.valueOf(System.currentTimeMillis() / 1000);// 时间戳
//
//		System.out.println("accessToken:" + accessToken + "\njsapi_ticket:" + jsapi_ticket + "\n时间戳：" + timestamp
//				+ "\n随机字符串：" + noncestr);
//
//		// 4、获取url
//		String url = "http://www.luiyang.com/add.html";
//		/*
//		 * 根据JSSDK上面的规则进行计算，这里比较简单，我就手动写啦 String[] ArrTmp =
//		 * {"jsapi_ticket","timestamp","nonce","url"}; Arrays.sort(ArrTmp);
//		 * StringBuffer sf = new StringBuffer(); for(int
//		 * i=0;i<ArrTmp.length;i++){ sf.append(ArrTmp[i]); }
//		 */
//
//		// 5、将参数排序并拼接字符串
//		String str = "jsapi_ticket=" + jsapi_ticket + "&noncestr=" + noncestr + "&timestamp=" + timestamp + "&url="
//				+ url;
//
//		// 6、将字符串进行sha1加密
//		String signature = SHA1(str);
//		System.out.println("参数：" + str + "\n签名：" + signature);

	}

	/**
	 * 生成ticket
	 * @param access_token
	 * @return
	 */
	public static String getTicket(String access_token) {
		String ticket = null;
		String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=" + access_token + "&type=jsapi";// 这个url链接和参数不能变
		try {
			URL urlGet = new URL(url);
			HttpURLConnection http = (HttpURLConnection) urlGet.openConnection();
			http.setRequestMethod("GET");
			http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			http.setDoOutput(true);
			http.setDoInput(true);
			System.setProperty("sun.net.client.defaultConnectTimeout", "30000");// 连接超时30秒
			System.setProperty("sun.net.client.defaultReadTimeout", "30000"); // 读取超时30秒
			http.connect();
			InputStream is = http.getInputStream();
			int size = is.available();
			byte[] jsonBytes = new byte[size];
			is.read(jsonBytes);
			String message = new String(jsonBytes, "UTF-8");
			JSONObject demoJson = JSONObject.fromObject(message);
			log.info("JSON字符串：" + demoJson);
			ticket = demoJson.getString("ticket");
			is.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ticket;

	}
	
	/**
	 * 通过code获取用户的信息
	 * @param code
	 * @return
	 */
	// wx40d294a89bcd9fcb
		// e48b8b33730cb8bf7ed2aa26e671549b
	 public static NSNUserInfo baseWeChatLogin(String appid ,String appSecret,String code) {
	        try{
//	        	log.info("微信用户授权:" + code);
	            String openId = "";
	            if (!"authdeny".equals(code)) {
	                WeixinOauth2Token weixinOauth2Token = WeixinUtil.getOauth2AccessToken(appid,appSecret, code);
/*	                WeixinOauth2Token weixinOauth2Token = WeixinUtil.getOauth2AccessToken(
	                		ReadProperties.getObjectProperties("config.properties","APPID" ),
	                		ReadProperties.getObjectProperties("config.properties","APPSECRET" ), code);
*/
	                String accessToken = weixinOauth2Token.getAccessToken();
	                openId = weixinOauth2Token.getOpenId();

	                NSNUserInfo snsUser = WeixinUtil
	                        .getSNSUserInfo(accessToken, openId);

	                if (snsUser != null)
	                    return snsUser;
	            }
	        }catch(Exception e){
	            log.info("请求微信信息失败:"+e.toString());
	        }
	        return null;
	    }


	 
	 /**
	  * 微信客服消息发送
	  * @param accessToken
	  * @param json
	  * @return
	  */
	 public static JSONObject send(String accessToken , String json){
		 JSONObject jsonObject =  WeixinUtil.httpRequest(send_url.replace("ACCESS_TOKEN", accessToken), "POST", json);
		 log.info("消息发送返回"+jsonObject);
		int errorCode = jsonObject.getInt("errcode");
			//如果错误代码是42001说明access_token过期
			if(errorCode == 42001){
				log.info("access_token过期");
				//重新获取ACCESS_token
				InnerSingletion.single=new WeChatToken();
				log.info("重新获取access_token成功"+InnerSingletion.single.getAccessToken().getToken());
				//重新发送数据
				jsonObject = WeixinUtil.httpRequest(send_url.replace("ACCESS_TOKEN", accessToken), "POST", json);
				log.info("重新发送返回结果："+jsonObject);
			}
			
			if (null != jsonObject) {
				if (0 != jsonObject.getInt("errcode")) {
					  jsonObject.getInt("errcode");
				}
			}
		 
		 
		 return jsonObject;
	 }
	 /**
	  * 获取用户openid列表
	  * @param accessToken
	  * @param nextOpenId
	  * @return
	  */
	 public static  UserGet getusers(String accessToken,String nextOpenId){
		 String nextopenid = "&next_openid=NEXT_OPENID";
		
		 
		 
		String requestUrl =  user_get_url.replace("ACCESS_TOKEN", accessToken); 
		
		 if(Common.isNotEmpty(nextOpenId)){
			 //如果关注的用户超过10000个需要多次获取
			 nextopenid = nextopenid.replace("NEXT_OPENID", nextOpenId);
			 requestUrl = requestUrl+nextopenid;
			 
		 }
		
		 JSONObject jsonObject =  WeixinUtil.httpRequest(requestUrl, "GET", null);
		 UserGet user = null;
		 if (null != jsonObject) {
				try {
					user = new UserGet();
					user.setCount(jsonObject.getInt("count"));
					user.setTotal(jsonObject.getInt("total"));
					user.setNext_openid(jsonObject.getString("next_openid"));
					JSONObject j =jsonObject.getJSONObject("data");
					JSONArray array = j.getJSONArray("openid");
					Data d = new Data();
					d.setOpenid(array.toArray());
					user.setData(d);
				} catch (Exception e) {
					log.error("获取openid列表出现错误");
					return null;
				}
			}
		 return user;
	 }
	 
	 
	 
	 
	 
	 
	 
	
	
	

}