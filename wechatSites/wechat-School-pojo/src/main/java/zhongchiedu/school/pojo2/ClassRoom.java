package zhongchiedu.school.pojo2;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import zhongchiedu.framework.pojo.GeneralBean;
import zhongchiedu.school.pojo.Teacher;

@Getter
@Setter
@ToString
@Document
public class ClassRoom  extends GeneralBean<ClassRoom>{
	@DBRef
	private Grade grade;
	private String name; 
	@DBRef
	private List<Teacher> teacher;
	@DBRef
	private List<Student> students;
    
	
 
}
