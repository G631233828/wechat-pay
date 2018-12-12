package zhongchiedu.service;

import java.util.List;

import zhongchiedu.common.utils.BasicDataResult;
import zhongchiedu.framework.service.GeneralService;
import zhongchiedu.school.pojo.Clazz;
import zhongchiedu.school.pojo.Student;

public interface StudentService extends GeneralService<zhongchiedu.school.pojo.Student>{

	 void SaveOrUpdateStudent(Student student);
	 
	 BasicDataResult studentDisable(String id);
	 
	  Student findStudentByRegisterNum2(String registerNumber);
	  
	  List<Student> findStudentByClazz(Clazz clazz);
	  
	  
}
