package zhongchiedu.school.pojo;

import java.util.List;

import org.springframework.data.annotation.Transient;
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
	private List<WeChatbandingStudent> listbandings;//添加多个账号用于切换
	private NSNUserInfo nsnUserInfo; //绑定微信用户信息
	private String studentName;	    //绑定学生姓名
	private String studentClass;    //绑定学生班级
	private String studentAccount;  //登录帐号
	private String password;	
	@Transient
	private int clazzYear;//班级年份
	@Transient
	private int clazzNum;//班级号
	
	
	

}
