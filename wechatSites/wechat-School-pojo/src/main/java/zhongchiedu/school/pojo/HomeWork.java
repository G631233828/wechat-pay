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
	private String content;
	

}
