package zhongchiedu.school.pojo;


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
	@DBRef
	private Clazz clazz;//班级
	private String studentCode;//学籍号
}
