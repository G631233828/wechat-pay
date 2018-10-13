package zhongchiedu.wechat.req;
/**
 * 图片消息
 * 
 * @author fliay
 * @date 2018年8月13日 11:00:23
 */
public class ImageMessage extends BaseMessage {
	// 图片链接
	private String PicUrl;

	public String getPicUrl() {
		return PicUrl;
	}

	public void setPicUrl(String picUrl) {
		PicUrl = picUrl;
	}
}