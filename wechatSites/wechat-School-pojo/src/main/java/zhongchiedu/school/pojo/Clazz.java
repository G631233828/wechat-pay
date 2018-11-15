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
public class Clazz  extends GeneralBean<Clazz>{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8755425027157273832L;
	
	
	
	private int clazzYear;//班级年份
	private int clazzNum;//班级号
	@DBRef
	private Teacher clazzTeacher;//班主任
	@DBRef
	private Teacher deputyClazzTeacher;//副班主任
	
	
}
