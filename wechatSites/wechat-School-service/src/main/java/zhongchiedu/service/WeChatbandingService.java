package zhongchiedu.service;

import zhongchiedu.framework.service.GeneralService;
import zhongchiedu.school.pojo.WeChatbanding;
import zhongchiedu.wechat.oauto2.NSNUserInfo;


public interface WeChatbandingService extends GeneralService<WeChatbanding>{
	
	void SaveOrUpdateWeChatbanding(WeChatbanding weChatbanding);
	
	WeChatbanding findWeChatbandingByOpenId(String openId);

	NSNUserInfo findWechatNsn(String code);
	
	public void updateStudentName(WeChatbanding we ,String newName);
	
	
	
}
