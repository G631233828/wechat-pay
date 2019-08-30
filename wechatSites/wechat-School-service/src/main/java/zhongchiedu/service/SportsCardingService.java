package zhongchiedu.service;

import java.util.List;

import zhongchiedu.common.utils.BasicDataResult;
import zhongchiedu.framework.pagination.Pagination;
import zhongchiedu.framework.service.GeneralService;
import zhongchiedu.school.pojo.Clazz;
import zhongchiedu.school.pojo.SportsCarding;

public interface SportsCardingService extends GeneralService<SportsCarding> {

	void SaveOrUpdateSportsCarding(SportsCarding sportsCarding, String openId);//添加或修改今日运动记录

	SportsCarding findSportsCardingToday(String openId);

	Pagination<SportsCarding>  findSportsCardings(String account,Clazz clazz,Integer pageNo,Integer pageSize);
	
	BasicDataResult findMap(String activityId, String openId);
	
	List<SportsCarding> findSportsCardingByActivityIdAndClazzId(String clazzId,String activityId);
	
	List<SportsCarding> findSportsCardingByActivityIdAndStudentId(String activityId,String studentId);
	
	int findTodaySportsCardings(String clazzId,String activityId,String date);
	

} 
