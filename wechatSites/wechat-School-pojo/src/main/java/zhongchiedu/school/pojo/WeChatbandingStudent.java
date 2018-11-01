package zhongchiedu.school.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class WeChatbandingStudent {
	private String studentName;	    //绑定学生姓名
	private String studentClass;    //绑定学生班级
	private String studentAccount;  //登录帐号
	private String password;		//登录密码
	
}
