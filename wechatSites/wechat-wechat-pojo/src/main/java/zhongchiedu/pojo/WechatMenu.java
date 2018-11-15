package zhongchiedu.pojo;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import zhongchiedu.common.utils.Types.menuType;
import zhongchiedu.framework.pojo.GeneralBean;

@Getter
@Setter
@ToString
@Document
public class WechatMenu  extends GeneralBean<WechatMenu>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1590688859764823518L;
	private String name; //菜单名称
	private menuType type;//菜单类型
	private String url;  //菜单跳转地址 
	private String key;  //菜单key
	private String parentId;// 0为父id 
	private List<WechatMenu> list;
	
	
	
	



	public WechatMenu(String name, menuType type, String url, String key, String parentId) {
		this.name = name;
		this.type = type;
		this.url = url;
		this.key = key;
		this.parentId = parentId;
	}



	public WechatMenu() {
	}

	
}
