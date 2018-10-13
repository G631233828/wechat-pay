package zhongchiedu.wechat.util.accessToken;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 微信通用接口凭证
 *
 * @author fliay
 * @date 2018年8月13日 10:36:09
 */
public class AccessToken {
    private static final Logger log = LoggerFactory.getLogger(AccessToken.class);

    // 获取到的凭证
    private String token;
    // 凭证有效时间，单位：秒
    private int expiresIn;



    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }
}