package zhongchiedu.school.pojo;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import zhongchiedu.framework.pojo.GeneralBean;
import zhongchiedu.general.pojo.MultiMedia;

@Getter
@Setter
@ToString
@Document
public class News  extends GeneralBean<News>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6042767705007136781L;
	private String author;//作者
	private String title; //标题
	private String content;//新闻内容
	private MultiMedia newsImg;	//显示推送的图片
	private int views;//访问量
	private int top;//置顶
	private String releaseDate;//发布日期
	
}
