package zhongchiedu.wechat.templates.messagenotifcation;


import net.sf.json.JSONObject;
import zhongchiedu.wechat.templates.util.sendTemplatMessage;
import zhongchiedu.wechat.templates.util.sendTemplateMessage_Data_ValueAndColor;
import zhongchiedu.wechat.util.WeixinUtil;
import zhongchiedu.wechat.util.accessToken.AccessToken;
import zhongchiedu.wechat.util.token.WeChatToken;
import zhongchiedu.wechat.util.token.WeChatToken.InnerSingletion;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SwipingCardTempPlate {
	
	private static Log log = LogFactory.getLog(SwipingCardTempPlate.class);
	//发送模板信息
	public static String sendTemplateMessageUrl="https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";
	
	static WeixinUtil t=new WeixinUtil();
	@SuppressWarnings("static-access")
	public static void main(String[] args) {  

//		//刘庆 ooiMKv-Em5FWT0WRYD7ZnHrga4_M
//		AccessToken a = t.getAccessToken(Configure.getInstance().getValueString(""),
//		"8718b53e61cfbdc946bf43f8557ec45b");
//		//绑定成功提示.
//		swipingCardNotifcation("ooiMKv-Em5FWT0WRYD7ZnHrga4_M",a.getToken());
//		log.info("success send template to user");
		//获取Token信息
		TemplatMessage templatMessage=new TemplatMessage();
		templatMessage.setName("刘庆");
		templatMessage.setSchoolAddress("华亭学校校门");
		templatMessage.setRemark("到校");
		AccessToken at= WeChatToken.getInstance().getAccessToken();
		//TODO 此处，调用模板通知  该toUser = weChatID (ooiMKv-Em5FWT0WRYD7ZnHrga4_M)
		SwipingCardTempPlate.swipingCardNotifcation("ooiMKv-Em5FWT0WRYD7ZnHrga4_M",at.getToken(),templatMessage);

//
	} 
	
	
	//TemplatMessage
	public static String swipingCardNotifcation(String toUser,String accessToken,TemplatMessage templatMessage){
			// 拼装创建菜单的url
			String url = sendTemplateMessageUrl.replace("ACCESS_TOKEN", accessToken);
			// 将菜单对象转换成json字符串
			sendTemplatMessage<SwipingCardNotifcation> sendTM=new sendTemplatMessage<SwipingCardNotifcation>();
			sendTM.setTouser(toUser);
			log.info("刷卡考勤模版："+SwipingCardNotifcation.template_id);
			sendTM.setTemplate_id(SwipingCardNotifcation.template_id);
			sendTM.setUrl("http://weixin.qq.com/download");
			sendTM.setTopcolor("#FF0000");

//			//您好，您的子女已到校。TemplatMessage{{first.DATA}}
			//学生姓名：叶萌{{childName.DATA}}
			//时间：2013年11月18日8时30分{{time.DATA}}
			//考勤状态：已到校{{status.DATA}}
			//提示：收到该信息的还有：爸爸、妈妈、奶奶{{remark.DATA}}
//			sendTemplateMessage_Data_ValueAndColor sdv=new sendTemplateMessage_Data_ValueAndColor();
			SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			SwipingCardNotifcation datas=new SwipingCardNotifcation();

			datas.setFirst(new sendTemplateMessage_Data_ValueAndColor(templatMessage.getTitle()));
			datas.setName(new sendTemplateMessage_Data_ValueAndColor(templatMessage.getName()));
			datas.setTime( new sendTemplateMessage_Data_ValueAndColor(templatMessage.getDateTime()));
			datas.setLocation(new sendTemplateMessage_Data_ValueAndColor(templatMessage.getSchoolAddress()));
			datas.setRemark(new sendTemplateMessage_Data_ValueAndColor(templatMessage.getRemark()));
			sendTM.setData(datas);
			
			String jsontemplate = JSONObject.fromObject(sendTM).toString();
			log.info(jsontemplate);
			// 调用接口创建菜单
			JSONObject jsonObject = t.httpRequest(url, "POST", jsontemplate);
			
			log.info("返回结果:"+jsonObject);
			int errorCode = jsonObject.getInt("errcode");
			
			//如果错误代码是42001说明access_token过期
			if(errorCode == 42001){
				log.info("access_token过期");
				//重新获取ACCESS_token
				InnerSingletion.single=new WeChatToken();
				log.info("重新获取access_token成功"+InnerSingletion.single.getAccessToken().getToken());
				//重新发送数据
				jsonObject = t.httpRequest(url, "POST", jsontemplate);
				log.info("重新发送返回结果："+jsonObject);
			}
			
			if (null != jsonObject) {
				if (0 != jsonObject.getInt("errcode")) {
					  jsonObject.getInt("errcode");
				}
			}
			
			return null;
		}





}

