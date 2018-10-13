package zhongchiedu.wechat.templates.util;


public class sendTemplateMessage_Data_ValueAndColor {
	public String value;
	public String color;
	
	
	
	
	
	public sendTemplateMessage_Data_ValueAndColor(String value) {
		this.color = "#173177";
		this.value=value;
	}
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	

	
}
