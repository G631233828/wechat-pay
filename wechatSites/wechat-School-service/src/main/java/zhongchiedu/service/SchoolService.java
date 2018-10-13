package zhongchiedu.service;

import zhongchiedu.common.utils.BasicDataResult;
import zhongchiedu.framework.service.GeneralService;
import zhongchiedu.school.pojo.School;
import zhongchiedu.school.pojo2.Parent;


public interface SchoolService extends GeneralService<School>{
	
	
	/**
	 * �޸Ļ����
	 * @param school
	 */
	void SaveOrUpdateSchool(School school);

	BasicDataResult schoolDisable(String id);
}
