package zhongchiedu.wechat.templates.leavenotifcation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONObject;
import zhongchiedu.common.utils.ReadProperties;
import zhongchiedu.wechat.templates.schoolnotifcation.SchoolTemplateMessage;
import zhongchiedu.wechat.templates.schoolnotifcation.SwipingSchoolTemplate;
import zhongchiedu.wechat.templates.util.sendTemplatMessage;
import zhongchiedu.wechat.templates.util.sendTemplateMessage_Data_ValueAndColor;
import zhongchiedu.wechat.util.WeixinUtil;
import zhongchiedu.wechat.util.accessToken.AccessToken;
import zhongchiedu.wechat.util.token.WeChatToken;
import zhongchiedu.wechat.util.token.WeChatToken.InnerSingletion;


/**
 * 请假申请推送
 * @author fliay
 *
 */
public class SwipingLeaveTemplate {

	
	private static final Logger log = LoggerFactory.getLogger(SwipingLeaveTemplate.class);
	//发送模板信息
	public static String sendTemplateMessageUrl="https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";

	static WeixinUtil t = new WeixinUtil();
	
	public static String swipingLeaveNotifcation(String link,String toUser,String accessToken,LeaveTemplateMessage leave){
//		String templateId = "OpYtn49XUgytmQus8vDRguvLAQjRldG2Hyy1ZuiwV6M";
		String templateId = ReadProperties.getObjectProperties("application.properties", "leave.template");
		log.info("templateId"+templateId);
		//发送菜单的url
		String url = sendTemplateMessageUrl.replace("ACCESS_TOKEN", accessToken);
		//发送数据json
		sendTemplatMessage<SwipingLeaveNotifcation> send = new sendTemplatMessage<>();
		log.info("请假申请发送");
		send.setTouser(toUser);
		send.setTemplate_id(templateId);
		send.setUrl(link);
		//send.setTopcolor("#FF0000");
		
		SwipingLeaveNotifcation le =new SwipingLeaveNotifcation();
		le.setFirst(new sendTemplateMessage_Data_ValueAndColor(leave.getTitle()));
		le.setKeyword1(new sendTemplateMessage_Data_ValueAndColor(leave.getName()));
		le.setKeyword2(new sendTemplateMessage_Data_ValueAndColor(leave.getDate()));
		le.setKeyword3(new sendTemplateMessage_Data_ValueAndColor(leave.getReason()));
		le.setRemark(new sendTemplateMessage_Data_ValueAndColor(leave.getRemark()));
		send.setData(le);
		
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
		
		LeaveTemplateMessage leave = new LeaveTemplateMessage();
		leave.setTitle("<img src='https://images2017.cnblogs.com/q/37026/201709/37026-20170904162851085-594461350.jpg' alt=''>");
		leave.setName("张三");
		leave.setReason("感冒");
		leave.setDate("2018-12-12");
		leave.setRemark("备注");
		String link = "http://zhongchiedu.com/wechat-app/wechat/weChatCore";
		AccessToken at= WeChatToken.getInstance().getAccessToken();
		//ooiMKvywAoyhK1gF29qrq1tllE6I
		//ooiMKv7cqR-2EgkeC9LdATpr-mbY
		SwipingLeaveTemplate.swipingLeaveNotifcation(link, "ooiMKvywAoyhK1gF29qrq1tllE6I".toString(), at.getToken(), leave);
		
	}
	
	
}
