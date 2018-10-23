package zhongchiedu.service;

import zhongchiedu.framework.service.GeneralService;
import zhongchiedu.school.pojo.WeChatbanding;


public interface WeChatbandingService extends GeneralService<WeChatbanding>{
	
	void SaveOrUpdateWeChatbanding(WeChatbanding weChatbanding);
	
	WeChatbanding findWeChatbandingByOpenId(String openId);
	
	
	
	
	
}
