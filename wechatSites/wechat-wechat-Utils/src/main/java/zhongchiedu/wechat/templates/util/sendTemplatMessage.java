package zhongchiedu.wechat.templates.util;


/**
 * 发送模板信息
 */
public class sendTemplatMessage<T> {
	public String touser;
	public String template_id;
	public String url;
	public String topcolor;
	public T data;

	public String getTouser() {
		return touser;
	}
	public void setTouser(String touser) {
		this.touser = touser;
	}
	public String getTemplate_id() {
		return template_id;
	}
	public void setTemplate_id(String templateId) {
		template_id = templateId;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getTopcolor() {
		return topcolor;
	}
	public void setTopcolor(String topcolor) {
		this.topcolor = topcolor;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}

	
//    {
//        "touser":"OPENID",
//        "template_id":"ngqIpbwh8bUfcSsECmogfXcV14J0tQlEpBO27izEYtY",
//        "url":"http://weixin.qq.com/download",
//        "topcolor":"#FF0000",
//        "data":{
//                "first": {
//                    "value":"恭喜你购买成功！",
//                    "color":"#173177"
//                },
//                "keynote1":{
//                    "value":"巧克力",
//                    "color":"#173177"
//                },
//                "keynote2": {
//                    "value":"39.8元",
//                    "color":"#173177"
//                },
//                "keynote3": {
//                    "value":"2014年9月16日",
//                    "color":"#173177"
//                },
//                "remark":{
//                    "value":"欢迎再次购买！",
//                    "color":"#173177"
//                }
//        }
//    }
	
	
	
	
}
