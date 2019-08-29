package zhongchiedu.service;

import java.util.List;
import java.util.Map;

import zhongchiedu.common.utils.BasicDataResult;
import zhongchiedu.framework.service.GeneralService;
import zhongchiedu.school.pojo.Clazz;


public interface ClazzService extends GeneralService<Clazz>{

	void SaveOrUpdateClazz(Clazz clazz);//添加或修改班级
	

	BasicDataResult clazzDisable(String id); //禁用班级

	List<Clazz> findClazzsByisDisable(); //查询在使用状态下的班级

	void autoBatchClazz(int year, int num);
	
	 Clazz findClazzByYearNum(int year, int num);
	 
	 Clazz findHeadMaster(String id);
	 
	 List<Clazz> findClazzsWhereInSchool(List<Integer> inschool);
	 
	 
}
