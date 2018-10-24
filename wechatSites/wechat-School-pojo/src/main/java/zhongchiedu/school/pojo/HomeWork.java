package zhongchiedu.school.pojo;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class HomeWork {
	
	private String classzz;
	private Map<String,String> homework;
	private String schoolName;
	private String gradeName;
	private String className;
	private String date;
	private String content;    //您当前查看的是 2018-10-22 瑞华校区 三年级 1班 的家庭作业
	private String studentName;//学生名字
	

	
}
