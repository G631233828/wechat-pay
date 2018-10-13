package zhongchiedu.wechat.templates.messagenotifcation;

import zhongchiedu.wechat.templates.util.sendTemplateMessage_Data_ValueAndColor;

/**
 * 刷卡通知
 */
public class SwipingCardNotifcation {
	
	
//	private String template_id = "vIKw29oQ-Vix92GrpsSGXKdzAPw7MPxqZcEGDCfoS0Y";
	
	
	public static String template_id =""; //Configure.getInstance().getValueString("swipingCardNotifcation.template_id");
    //详细内容
    public sendTemplateMessage_Data_ValueAndColor first;
    //姓名
    public sendTemplateMessage_Data_ValueAndColor name;
    //刷卡时间
    public sendTemplateMessage_Data_ValueAndColor time;
    //地址（某学校校门口）
    public sendTemplateMessage_Data_ValueAndColor location;
    //备注
    public sendTemplateMessage_Data_ValueAndColor remark;

    public SwipingCardNotifcation() {
    }

    public SwipingCardNotifcation(sendTemplateMessage_Data_ValueAndColor first, sendTemplateMessage_Data_ValueAndColor name, sendTemplateMessage_Data_ValueAndColor time, sendTemplateMessage_Data_ValueAndColor location, sendTemplateMessage_Data_ValueAndColor remark) {
        this.first = first;
        this.name = name;
        this.time = time;
        this.location = location;
        this.remark = remark;
    }

    public static String getTemplate_id() {
        return template_id;
    }

    public static void setTemplate_id(String template_id) {
        SwipingCardNotifcation.template_id = template_id;
    }

    public sendTemplateMessage_Data_ValueAndColor getFirst() {
        return first;
    }

    public void setFirst(sendTemplateMessage_Data_ValueAndColor first) {
        this.first = first;
    }

    public sendTemplateMessage_Data_ValueAndColor getName() {
        return name;
    }

    public void setName(sendTemplateMessage_Data_ValueAndColor name) {
        this.name = name;
    }

    public sendTemplateMessage_Data_ValueAndColor getTime() {
        return time;
    }

    public void setTime(sendTemplateMessage_Data_ValueAndColor time) {
        this.time = time;
    }

    public sendTemplateMessage_Data_ValueAndColor getLocation() {
        return location;
    }

    public void setLocation(sendTemplateMessage_Data_ValueAndColor location) {
        this.location = location;
    }

    public sendTemplateMessage_Data_ValueAndColor getRemark() {
        return remark;
    }

    public void setRemark(sendTemplateMessage_Data_ValueAndColor remark) {
        this.remark = remark;
    }
}
