package zhongchiedu.school.pojo2;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import zhongchiedu.framework.pojo.GeneralBean;
import zhongchiedu.school.pojo.School;

@Getter
@Setter
@ToString
@Document
/**
 * 学生管理类
 * @author fliay
 *
 */
public class Student extends GeneralBean<Student> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8104686504073557950L;
	private String name;  //姓名 
	private int age;      //年龄
	@DBRef
	private School school;
	private boolean sex; //0为女孩 1 为男孩 
	private String entranceYear; //入学年份
	private String cardId;//身份证号码
	private String studentCode;//学籍号
	private String nativePlace;//籍贯
	private String classCode; //班级学号
	private String permanentAddress;//户籍地址
	private String residentialAddress;//居住地址
	private String emergencyPerson;//紧急联系人
	private String emergencyConnect;//紧急联系人
	@DBRef
	private List<SchoolUser> schoolUser;//学生绑定的用户
	
}
