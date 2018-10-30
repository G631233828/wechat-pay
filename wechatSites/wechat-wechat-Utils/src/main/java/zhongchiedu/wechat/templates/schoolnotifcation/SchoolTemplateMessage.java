package zhongchiedu.wechat.templates.schoolnotifcation;

import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class SchoolTemplateMessage {
	
	
	private String title;//通知标题
	
	private String school;//学校
	
	private String notifications;//通知人
	
	private String time;//通知时间
	
	private String content;//通知内容
	
	private String remark;//备注

	public SchoolTemplateMessage() {
		
		 SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 this.time = sdf.format(new Date());
		 
	}
	
	

	
	
}
