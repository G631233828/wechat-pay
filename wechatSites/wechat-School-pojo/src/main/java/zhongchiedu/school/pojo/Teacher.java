package zhongchiedu.school.pojo;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import zhongchiedu.framework.pojo.GeneralBean;
import zhongchiedu.wechat.oauto2.NSNUserInfo;

@Getter
@Setter
@ToString
@Document
public class Teacher  extends GeneralBean<Teacher>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5204252968360555416L;
	
	
	private String name; //老师姓名
	private String namePinyin;//老师名字拼音
	private String password="changeme";  //绑定微信需要用到的密码
	private String contactsmobile;//老师联系方式
	private String teacherEmail;//老师的邮箱
	private String cardType;//证件类型
    private String cardId;// 身份证Id（证件号）
    private NSNUserInfo nsnUserInfo; //绑定微信用户信息
    private String openId;//老师的openId
    private Clazz clazz;//绑定班级
//    private boolean clazzTeacher;//true班主任 false非班主任
    @DBRef
	private School school;
    



	
	
}