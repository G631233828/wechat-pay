package zhongchiedu.wechat.binding;


import zhongchiedu.wechat.templates.util.sendTemplateMessage_Data_ValueAndColor;

/**
 * 
 *用户绑定成功通知
 */
public class BindingNotifcation {
	//中赤：br6xFEvgV5i7x2XHOqojPP87qipxusiQL2mj6fDprAI
	public static String template_id = "br6xFEvgV5i7x2XHOqojPP87qipxusiQL2mj6fDprAI";
	public sendTemplateMessage_Data_ValueAndColor first;
	public sendTemplateMessage_Data_ValueAndColor keyword1;
	public sendTemplateMessage_Data_ValueAndColor keyword2;
	public sendTemplateMessage_Data_ValueAndColor remark;

	public static String getTemplate_id() {
		return template_id;
	}

	public static void setTemplate_id(String templateId) {
		template_id = templateId;
	}

	public sendTemplateMessage_Data_ValueAndColor getFirst() {
		return first;
	}

	public void setFirst(sendTemplateMessage_Data_ValueAndColor first) {
		this.first = first;
	}

	public sendTemplateMessage_Data_ValueAndColor getKeyword1() {
		return keyword1;
	}

	public void setKeyword1(sendTemplateMessage_Data_ValueAndColor keyword1) {
		this.keyword1 = keyword1;
	}

	public sendTemplateMessage_Data_ValueAndColor getKeyword2() {
		return keyword2;
	}

	public void setKeyword2(sendTemplateMessage_Data_ValueAndColor keyword2) {
		this.keyword2 = keyword2;
	}

	public sendTemplateMessage_Data_ValueAndColor getRemark() {
		return remark;
	}

	public void setRemark(sendTemplateMessage_Data_ValueAndColor remark) {
		this.remark = remark;
	}

	@Override
	public String toString() {
		return "BindingNotifcation [first=" + first + ", keyword1=" + keyword1
				+ ", keyword2=" + keyword2 + ", remark=" + remark + "]";
	}

}
