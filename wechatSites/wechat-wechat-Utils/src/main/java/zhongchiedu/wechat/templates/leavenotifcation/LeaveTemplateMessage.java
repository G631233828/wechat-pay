package zhongchiedu.wechat.templates.leavenotifcation;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class LeaveTemplateMessage {
	
	
	private String title;//请假title
	private String  name;//请假学生
	private String date;//请假日期
	private String reason;//请假原因
	private String remark;//备注
	
	
}
