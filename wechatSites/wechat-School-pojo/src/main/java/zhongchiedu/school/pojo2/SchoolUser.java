package zhongchiedu.school.pojo2;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import zhongchiedu.framework.pojo.GeneralBean;
import zhongchiedu.school.util.Type;

/**
 * 
 * @author fliay
 *
 */

/**
 * 学校账号管理
 * @author fliay
 *
 */
@Getter
@Setter
@ToString
@Document
public class SchoolUser extends GeneralBean<SchoolUser>{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8587917890208565294L;
	private String accountName;
	private String password;
	private String number;
	@DBRef
	private Student student;//账号绑定学生信息
	@DBRef
	private ClassRoom classRoom;//绑定班级  如果登陆的是老师的账号,可以查看绑定的班级
	private Type.AccountType accountType;//账号类型
	
    
}
