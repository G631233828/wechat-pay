package zhongchiedu.school.pojo;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;
import zhongchiedu.framework.pojo.GeneralBean;

@Document
@Getter
@Setter
public class Notice  extends GeneralBean<Notice>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5333775289429028797L;

	private String title;//通知主题
	@DBRef
	private Clazz clazz;
	@DBRef
	private Teacher teacher;
	private String content;//消息内容
	
	
	
}
