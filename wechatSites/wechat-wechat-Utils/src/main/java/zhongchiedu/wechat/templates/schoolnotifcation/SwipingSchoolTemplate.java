package zhongchiedu.wechat.templates.schoolnotifcation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONObject;
import zhongchiedu.common.utils.ReadProperties;
import zhongchiedu.wechat.templates.util.sendTemplatMessage;
import zhongchiedu.wechat.templates.util.sendTemplateMessage_Data_ValueAndColor;
import zhongchiedu.wechat.util.WeixinUtil;
import zhongchiedu.wechat.util.accessToken.AccessToken;
import zhongchiedu.wechat.util.token.WeChatToken;
import zhongchiedu.wechat.util.token.WeChatToken.InnerSingletion;


/**
 * 学校消息推送
 * @author fliay
 *
 */
public class SwipingSchoolTemplate {
	
	private static final Logger log = LoggerFactory.getLogger(SwipingSchoolTemplate.class);

	//发送模板信息
	public static String sendTemplateMessageUrl="https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";
	
	static WeixinUtil t = new WeixinUtil();
	
	public static String swipingSchoolNotifcation(String toUser,String accessToken,SchoolTemplateMessage schoolTemplateMessage){
		//String templateId = "FoNU2P8hDJkPh8f2NUGqxhrAXs9h42_0LsNDhUUyCJc";
		String templateId = ReadProperties.getObjectProperties("application.properties", "school.template");
		log.info("通知模版id"+templateId);
		//发送菜单的url
		String url = sendTemplateMessageUrl.replace("ACCESS_TOKEN", accessToken);
		//拼装发送json字符串
		sendTemplatMessage<SwipingSchoolNotifcation> send = new sendTemplatMessage<>();
		log.info("通知模板");
		send.setTouser(toUser);
		send.setTemplate_id(templateId);
		send.setTopcolor("#FF0000");
		
		SwipingSchoolNotifcation scn = new SwipingSchoolNotifcation();
		scn.setFirst(new sendTemplateMessage_Data_ValueAndColor(schoolTemplateMessage.getTitle()));
		scn.setKeyword1(new sendTemplateMessage_Data_ValueAndColor(schoolTemplateMessage.getSchool()));
		scn.setKeyword2(new sendTemplateMessage_Data_ValueAndColor(schoolTemplateMessage.getNotifications()));
		scn.setKeyword3(new sendTemplateMessage_Data_ValueAndColor(schoolTemplateMessage.getTime()));
		scn.setKeyword4(new sendTemplateMessage_Data_ValueAndColor(schoolTemplateMessage.getContent()));
		scn.setRemark(new sendTemplateMessage_Data_ValueAndColor(schoolTemplateMessage.getRemark()));
		send.setData(scn);
		
		String jsontemplate = JSONObject.fromObject(send).toString();
		log.info("返回结果："+jsontemplate);
		JSONObject jsonObject = t.httpRequest(url, "POST", jsontemplate);
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
	
	
	
	public static void main(String[] args) {
		
		SchoolTemplateMessage school = new SchoolTemplateMessage();
		school.setTitle("采摘童话的斑斓 叩开文学的心扉 “我爱阅读，我会探究，我善表达”阅读系列活动");
		school.setNotifications("张通昌");
		school.setSchool("福山外国语小学");
		AccessToken at= WeChatToken.getInstance().getAccessToken();
		//ooiMKvywAoyhK1gF29qrq1tllE6I
		//ooiMKv7cqR-2EgkeC9LdATpr-mbY
		SwipingSchoolTemplate.swipingSchoolNotifcation("ooiMKv7cqR-2EgkeC9LdATpr-mbY",at.getToken(),school);
		
	}
	
	
	
	
	
	
	
	
	
	
	
}
