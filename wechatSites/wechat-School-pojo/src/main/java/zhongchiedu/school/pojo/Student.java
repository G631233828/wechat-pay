package zhongchiedu.school.pojo;


import java.util.List;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import zhongchiedu.framework.pojo.GeneralBean;


@Getter
@Setter
@ToString
@Document
public class Student extends GeneralBean<Student>{

	private static final long serialVersionUID = 8104686504073557950L;
	private String name;  //姓名 
	private List<WeChatbanding> listbandingOpenid;//所有绑定该学生的帐号 
	@DBRef
	private Clazz clazz;//班级
	private String account;//账号
	private String password;//密码
	private String registerNumber;//学籍号
	private String code;//班级学号
	@DBRef
	private Teacher headMaster;//班主任
	@DBRef
	private Teacher deputyHeadMaster;//副班主任
	@Transient
	private boolean state;//绑定状态
	
	public Student(){}
}
