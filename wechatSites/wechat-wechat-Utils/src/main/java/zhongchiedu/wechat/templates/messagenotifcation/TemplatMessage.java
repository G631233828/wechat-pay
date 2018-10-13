package zhongchiedu.wechat.templates.messagenotifcation;


import java.text.SimpleDateFormat;
import java.util.Date;

public class TemplatMessage {

    private String title;//标题

    private String name;//名称

    private String dateTime; //时间

    private String schoolAddress;//学校地址

    private String remark;//备注

    private String weChatToken;//openID,

    SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public TemplatMessage(){
        title="您好，您的子女已到校";
        dateTime=sdf.format(new Date());
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getSchoolAddress() {
        return schoolAddress;
    }

    public void setSchoolAddress(String schoolAddress) {
        this.schoolAddress = schoolAddress;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getWeChatToken() {
        return weChatToken;
    }

    public void setWeChatToken(String weChatToken) {
        this.weChatToken = weChatToken;
    }

}
