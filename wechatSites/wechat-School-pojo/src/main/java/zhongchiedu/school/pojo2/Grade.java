package zhongchiedu.school.pojo2;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import zhongchiedu.framework.pojo.GeneralBean;

@Getter
@Setter
@ToString
@Document
public class Grade extends GeneralBean<Grade>{
	private String name;
    private List<ClassRoom> listClass;
	
   
   
}
