package zhongchiedu.wechat.util.jsapi_Ticket;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 微信通用接口凭证
 *
 * @author fliay
 * @date 2018-05-03
 */
public class Jsapi_Ticket {
    private static final Logger log = LoggerFactory.getLogger(Jsapi_Ticket.class);

    // 获取到的凭证
    private String ticket;
    // 凭证有效时间，单位：秒
    private int expiresIn;
	public String getTicket() {
		return ticket;
	}
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	public int getExpiresIn() {
		return expiresIn;
	}
	public void setExpiresIn(int expiresIn) {
		this.expiresIn = expiresIn;
	}
    

}