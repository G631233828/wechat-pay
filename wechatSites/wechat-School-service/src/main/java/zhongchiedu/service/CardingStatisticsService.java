package zhongchiedu.service;

import zhongchiedu.framework.service.GeneralService;
import zhongchiedu.school.pojo.CardingStatistics;


public interface CardingStatisticsService extends GeneralService<CardingStatistics>{

	
	CardingStatistics findByClazzIdAndActivityId(String clazzId,String activityId);
	
	CardingStatistics findByClazzId(String clazzId);
}
