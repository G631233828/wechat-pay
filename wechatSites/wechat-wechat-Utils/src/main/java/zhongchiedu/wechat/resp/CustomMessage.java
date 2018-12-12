package zhongchiedu.wechat.resp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class CustomMessage  {
	
	private String touser;//接受者appid
	private String  msgtype;//接收类型
	private String content;//文本消息内天
	private String title;//标题
	private String description;//描述
	private String url;//图文消息被点击后跳转的链接
	private String  picurl;//图文消息的图片链接，支持jpg,png叫好的效果为大图640*320，小图80*80
	private NewsMessage news;//图文消息内容
	private TextMessage text;//发送文字消息
}
