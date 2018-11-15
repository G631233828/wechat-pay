package zhongchiedu.general.pojo;

import java.util.List;

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
public class Role extends GeneralBean<Role> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6971871578588624227L;
	private String roleName; // 角色名称
	private String roleKey; // 角色key
	@DBRef
	private List<Resource> resource;

	public Role(String roleName, String roleKey, List<Resource> resource) {
		super();
		this.roleName = roleName;
		this.roleKey = roleKey;
		this.resource = resource;
	}
	public Role() {
	}
	
	
}
