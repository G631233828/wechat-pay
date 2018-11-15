package zhongchiedu.school.pojo2;

import java.util.Set;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import zhongchiedu.framework.pojo.GeneralBean;
/**
 * 消息类目
 * @author cd
 *
 */
@Getter
@Setter
@ToString
@Document
public class Toast  extends GeneralBean<Toast>{
    
	/**
	 * 
	 */
	private static final long serialVersionUID = -5703925587973289520L;
	//图片url
	private Set<String>  imgurls;
	//标题
	private String title;
	//HTML信息
	private String msg;
	//纯文本信息
	private String text;
	//作者
	private String author;
	//通知类型
	private String type;
	//接收人的id字符串,获取处理
	private String sendIds;
	//接收人的名字,前台展示
	private String sendName;
	
}
