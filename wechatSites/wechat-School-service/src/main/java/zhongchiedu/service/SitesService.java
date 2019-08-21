package zhongchiedu.service;

import java.util.List;
import java.util.Map;

import zhongchiedu.common.utils.BasicDataResult;
import zhongchiedu.framework.service.GeneralService;
import zhongchiedu.school.pojo.Activitys;
import zhongchiedu.school.pojo.Clazz;
import zhongchiedu.school.pojo.Sites;


public interface SitesService extends GeneralService<Sites>{

	void SaveOrUpdateSites(Sites sites);//添加或修改活动

	BasicDataResult sitesDisable(String id); //禁用站点

	List<Sites> findsitesByisDisable(); //查询在使用状态下的班级
	
	


}
