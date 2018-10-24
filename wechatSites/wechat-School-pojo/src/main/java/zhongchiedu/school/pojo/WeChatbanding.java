package zhongchiedu.school.pojo;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import zhongchiedu.framework.pojo.GeneralBean;
import zhongchiedu.wechat.oauto2.NSNUserInfo;

@Document
@Setter
@Getter
@ToString
public class WeChatbanding extends GeneralBean<WeChatbanding>{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4522239115994991098L;
	private String openId;		    //绑定微信openId
	private String studentName;	    //绑定学生姓名
	private String studentClass;    //绑定学生班级
	private String studentAccount;  //登录帐号
	private String password;		//登录密码
	private NSNUserInfo nsnUserInfo; //绑定微信用户信息
	
	
	
	

}
