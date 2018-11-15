package zhongchiedu.wechat.templates.bindingnotifcation;


import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.json.JSONObject;
import zhongchiedu.common.utils.ReadProperties;
import zhongchiedu.wechat.templates.util.sendTemplatMessage;
import zhongchiedu.wechat.templates.util.sendTemplateMessage_Data_ValueAndColor;
import zhongchiedu.wechat.util.WeixinUtil;
import zhongchiedu.wechat.util.accessToken.AccessToken;
import zhongchiedu.wechat.util.token.WeChatToken;


public class BindingTempPlate {
	
	private static Log log = LogFactory.getLog(BindingTempPlate.class);
	//发送模板信息
	public static String sendTemplateMessageUrl="https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";
	
	static WeixinUtil t=new WeixinUtil();
	// TODO Auto-generated method stub
	@SuppressWarnings("static-access")
	public static void main(String[] args) {  
				//中赤
				//appid:wx40d294a89bcd9fcb
				//appSecret:8718b53e61cfbdc946bf43f8557ec45b
		//TODO
		AccessToken at= WeChatToken.getInstance().getAccessToken();
		//绑定成功提示.
		bindingNotifcation("ooiMKv7cqR-2EgkeC9LdATpr-mbY",
				at.getToken(),"您已经成功绑定了微信公众号");
		log.info("success send template to user");
		
	} 
	
	
	//绑定用户信息成功.
		public static String bindingNotifcation_test(
				String toUser,String accessToken){
			// 拼装创建菜单的url
			String url = sendTemplateMessageUrl.replace("ACCESS_TOKEN", accessToken);
			// 将菜单对象转换成json字符串
			sendTemplatMessage<BindingNotifcation> sendTM=new sendTemplatMessage<BindingNotifcation>();
			sendTM.setTouser(toUser);
			sendTM.setTemplate_id(BindingNotifcation.template_id);
			sendTM.setUrl("http://weixin.qq.com/download");
			sendTM.setTopcolor("#FF0000");

//			您已成功绑定某某系统
//			用户账号：test@test.com
//			绑定时间：2015-02-16
//			绑定成功后可使用微信登录某某系统
//			sendTemplateMessage_Data_ValueAndColor sdv=new sendTemplateMessage_Data_ValueAndColor();
			SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			BindingNotifcation datas=new BindingNotifcation();
			datas.setFirst(new sendTemplateMessage_Data_ValueAndColor("您已成功绑定微信公告号成功."));
			datas.setKeyword1(new sendTemplateMessage_Data_ValueAndColor("fliay"));
			datas.setKeyword2( new sendTemplateMessage_Data_ValueAndColor(sdf.format(new Date())));
			datas.setRemark(new sendTemplateMessage_Data_ValueAndColor("绑定成功后可使用微信登录某某系统"));
			sendTM.setData(datas);

			String jsontemplate = JSONObject.fromObject(sendTM).toString();
			log.info(jsontemplate);
			// 调用接口创建菜单
			JSONObject jsonObject = t.httpRequest(url, "POST", jsontemplate);
			log.info("返回结果:"+jsonObject);
			if (null != jsonObject) {
				if (0 != jsonObject.getInt("errcode")) {
					  jsonObject.getInt("errcode");
					;
				}
			}

			return null;
		}




//绑定用户信息成功.
		public static String bindingNotifcation(String toUser,String accessToken,
				String name){
			// 拼装创建菜单的url
			String url = sendTemplateMessageUrl.replace("ACCESS_TOKEN", accessToken);
			// 将菜单对象转换成json字符串
			sendTemplatMessage<BindingNotifcation> sendTM=new sendTemplatMessage<BindingNotifcation>();
			sendTM.setTouser(toUser);
			sendTM.setTemplate_id(BindingNotifcation.template_id);
//			sendTM.setUrl("http://weixin.qq.com/download");
			sendTM.setTopcolor("#FF0000");
			
//			您已成功绑定某某系统
//			用户账号：test@test.com
//			绑定时间：2015-02-16
//			绑定成功后可使用微信登录某某系统
//			sendTemplateMessage_Data_ValueAndColor sdv=new sendTemplateMessage_Data_ValueAndColor();
			SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			BindingNotifcation datas=new BindingNotifcation();
			datas.setFirst(new sendTemplateMessage_Data_ValueAndColor("您已成功绑定微信公告号成功."));
			datas.setKeyword1(new sendTemplateMessage_Data_ValueAndColor(name));
			datas.setKeyword2( new sendTemplateMessage_Data_ValueAndColor(sdf.format(new Date())));
			datas.setRemark(new sendTemplateMessage_Data_ValueAndColor("绑定成功后可使用微信登录某某系统"));
			sendTM.setData(datas);
			
			String jsontemplate = JSONObject.fromObject(sendTM).toString();
			log.info(jsontemplate);
			// 调用接口创建菜单
			JSONObject jsonObject = t.httpRequest(url, "POST", jsontemplate);
			log.info("返回结果:"+jsonObject);
			if (null != jsonObject) {
				if (0 != jsonObject.getInt("errcode")) {
					  jsonObject.getInt("errcode");
					;
				}
			}
			
			return null;
		}

}

