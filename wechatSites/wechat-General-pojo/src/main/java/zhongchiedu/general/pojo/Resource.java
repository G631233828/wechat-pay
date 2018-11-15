package zhongchiedu.general.pojo;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import zhongchiedu.framework.pojo.GeneralBean;

@Getter
@Setter
@Document
public class Resource  extends GeneralBean<Resource>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4908945960493140321L;
	private String name; // 资源名称
	private String parentId; // 父目录id 没有父目录则为0
	private String resKey; // 资源key
	private int type; // 资源类型 0 表示目录 1表示菜单链接 2表示操作功能（添加删除修改）
	private String resUrl; // 资源链接
	private String icon; // 资源图标
	                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                
}
