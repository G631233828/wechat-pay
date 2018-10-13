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
public class Teacher  extends GeneralBean<Teacher>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5204252968360555416L;
	
	
	private String name; //老师姓名
	private String namePinyin;//老师名字拼音
	private String contactsmobile;//老师联系方式
	private String teacherEmail;//老师的邮箱
	private String cardType;//证件类型
    private String cardId;// 身份证Id（证件号）
	@DBRef
	private School school;
    



	
	
}