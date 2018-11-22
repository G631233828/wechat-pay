package zhongchiedu.framework.pojo;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.annotation.Id;

import lombok.Getter;
import lombok.Setter;
import lombok.Singular;
import lombok.ToString;

@Setter
@Getter
@ToString
public class GeneralBean<T> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	private String id;
	private Boolean isDelete=false;//是否删除
	private Date createTime = new Date();
	private String createDate = new Date().toString();//创建时间
	private Boolean isDisable=false;//禁用
	private String sort= "0";//排序
	private String description;//描述
	
	
}
