package zhongchiedu.service;

import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import zhongchiedu.framework.pagination.Pagination;
import zhongchiedu.framework.service.GeneralService;
import zhongchiedu.school.pojo.CardingStatistics;
import zhongchiedu.school.pojo.SportsCarding;


public interface CardingStatisticsService extends GeneralService<CardingStatistics>{

	
	CardingStatistics findByClazzIdAndActivityId(String clazzId,String activityId);
	
	CardingStatistics findByClazzId(String clazzId);
	
	Pagination<CardingStatistics> findCardingStatistics(Integer pageNo,Integer pageSize,String activity,String grade,String clazzId);
	
	List<SportsCarding> sportsCardings(String id);
	
	public HSSFWorkbook export(String activity,String grade,String clazzId);
}
