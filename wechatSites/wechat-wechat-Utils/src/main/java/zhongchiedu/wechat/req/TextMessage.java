package zhongchiedu.wechat.req;


/**
 * 文本消息
 * 
 * @author fliay	
 * @date 2018年8月13日 11:01:56
 */
public class TextMessage extends BaseMessage {
	// 消息内容
	private String Content;

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}
}
