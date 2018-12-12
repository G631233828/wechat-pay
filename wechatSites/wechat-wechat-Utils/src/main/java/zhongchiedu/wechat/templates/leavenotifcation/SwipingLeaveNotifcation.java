package zhongchiedu.wechat.templates.leavenotifcation;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import zhongchiedu.wechat.templates.util.sendTemplateMessage_Data_ValueAndColor;

@Getter
@Setter
@ToString
public class SwipingLeaveNotifcation {

	private sendTemplateMessage_Data_ValueAndColor first;
	private sendTemplateMessage_Data_ValueAndColor keyword1;
	private sendTemplateMessage_Data_ValueAndColor keyword2;
	private sendTemplateMessage_Data_ValueAndColor keyword3;
	private sendTemplateMessage_Data_ValueAndColor remark;
	
	public SwipingLeaveNotifcation() {
	}

	public SwipingLeaveNotifcation(sendTemplateMessage_Data_ValueAndColor first,
			sendTemplateMessage_Data_ValueAndColor keyword1, sendTemplateMessage_Data_ValueAndColor keyword2,
			sendTemplateMessage_Data_ValueAndColor keyword3, sendTemplateMessage_Data_ValueAndColor remark) {
		super();
		this.first = first;
		this.keyword1 = keyword1;
		this.keyword2 = keyword2;
		this.keyword3 = keyword3;
		this.remark = remark;
	}

	
	
}
