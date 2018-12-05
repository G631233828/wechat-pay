package zhongchiedu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import zhongchiedu.wechat.templates.leavenotifcation.LeaveTemplateMessage;
import zhongchiedu.wechat.templates.leavenotifcation.SwipingLeaveTemplate;
import zhongchiedu.wechat.util.accessToken.AccessToken;
import zhongchiedu.wechat.util.token.WeChatToken;

@Controller
@RequestMapping("/wechat")
public class test {

	@RequestMapping("test")
	public void test123(){
		LeaveTemplateMessage leave = new LeaveTemplateMessage();
		//leave.setTitle("请假申请");
		leave.setName("张三");
		leave.setReason("感冒");
		leave.setDate("2018-12-12");
		//leave.setRemark("备注");
		String link = "http://www.baidu.com";
		AccessToken at= WeChatToken.getInstance().getAccessToken();
		//ooiMKvywAoyhK1gF29qrq1tllE6I
		//ooiMKv7cqR-2EgkeC9LdATpr-mbY
		SwipingLeaveTemplate.swipingLeaveNotifcation(link, "ooiMKv7cqR-2EgkeC9LdATpr-mbY", at.getToken(), leave);
	}
	

	
}
