package zhongchiedu.wechat.resp;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
/**
 * 如果关注者超过10000 nextopenid无效
 * {"errcode":40013,"errmsg":"invalid appid"}
 * 
 * @author fliay
 *
 */
public class UserGet {

	private int total;//关注公众号的总用户数
	private int count;//拉去的openId个数，最大值为10000
	private Data data;//拉取到的openId集合
	private String next_openid;//第一个拉取的openId，默认为从头开始
	
	
	
	
	

}

