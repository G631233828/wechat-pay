package zhongchiedu.service;

import zhongchiedu.common.utils.BasicDataResult;
import zhongchiedu.framework.service.GeneralService;
import zhongchiedu.school.pojo.Student;

public interface StudentService extends GeneralService<zhongchiedu.school.pojo.Student>{

	 void SaveOrUpdateStudent(Student student);
	 
	 BasicDataResult studentDisable(String id);
}
