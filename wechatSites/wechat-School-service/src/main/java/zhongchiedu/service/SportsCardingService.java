package zhongchiedu.service;

import zhongchiedu.framework.service.GeneralService;
import zhongchiedu.school.pojo.SportsCarding;

public interface SportsCardingService extends GeneralService<SportsCarding> {

	void SaveOrUpdateSportsCarding(SportsCarding sportsCarding, String openId);//添加或修改今日运动记录

	SportsCarding findSportsCardingToday(String openId);


	

} 
