package zhongchiedu.service;

import java.util.List;
import java.util.Map;

import zhongchiedu.common.utils.BasicDataResult;
import zhongchiedu.framework.service.GeneralService;
import zhongchiedu.school.pojo.Activitys;
import zhongchiedu.school.pojo.Clazz;


public interface ActivitysService extends GeneralService<Activitys>{

	void SaveOrUpdateActivitys(Activitys activitys);//添加或修改活动

	BasicDataResult activitysDisable(String id); //禁用活动

	List<Activitys> findActivitysByisDisable(); //查询在使用状态的活动

}
