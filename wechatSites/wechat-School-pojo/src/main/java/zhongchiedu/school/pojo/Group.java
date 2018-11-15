package zhongchiedu.school.pojo;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import zhongchiedu.framework.pojo.GeneralBean;

@Document
@Setter
@Getter
@ToString
public class Group extends GeneralBean<Group>{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4522239115994991098L;
	private String groupName;	    //组名
	private Set<String> team;       //组员
	
	public Group(){
		this.team=new HashSet<>();
	}
	@DBRef
	private Teacher leader;			//组长
	@DBRef
	private Teacher vice;			//副组长
	
	private String teamName;
	
	

}
