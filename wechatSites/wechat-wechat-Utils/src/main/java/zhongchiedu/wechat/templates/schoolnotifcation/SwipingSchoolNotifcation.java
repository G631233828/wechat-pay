package zhongchiedu.wechat.templates.schoolnotifcation;

import lombok.Getter;
import lombok.Setter;
import zhongchiedu.wechat.templates.util.sendTemplateMessage_Data_ValueAndColor;

/**
 * 学校的消息通知
 * @author fliay
 *
 */
@Getter
@Setter
public class SwipingSchoolNotifcation {

	
	//日期
	private sendTemplateMessage_Data_ValueAndColor first;
	//学校
	private sendTemplateMessage_Data_ValueAndColor keyword1;
	//通知人
	private sendTemplateMessage_Data_ValueAndColor keyword2;
	//时间
	private sendTemplateMessage_Data_ValueAndColor keyword3;
	//通知内容
	private sendTemplateMessage_Data_ValueAndColor keyword4;
	//备注
	private sendTemplateMessage_Data_ValueAndColor remark;
	
	
	
	
	public SwipingSchoolNotifcation() {
	}


	public SwipingSchoolNotifcation(sendTemplateMessage_Data_ValueAndColor first,
			sendTemplateMessage_Data_ValueAndColor keyword1, sendTemplateMessage_Data_ValueAndColor keyword2,
			sendTemplateMessage_Data_ValueAndColor keyword3, sendTemplateMessage_Data_ValueAndColor keyword4,
			sendTemplateMessage_Data_ValueAndColor remark) {
		this.first = first;
		this.keyword1 = keyword1;
		this.keyword2 = keyword2;
		this.keyword3 = keyword3;
		this.keyword4 = keyword4;
		this.remark = remark;
	}


	
	
}
