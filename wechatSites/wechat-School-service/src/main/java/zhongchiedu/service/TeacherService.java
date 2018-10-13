package zhongchiedu.service;

import java.util.List;

import zhongchiedu.common.utils.BasicDataResult;
import zhongchiedu.framework.service.GeneralService;
import zhongchiedu.school.pojo.Teacher;


public interface TeacherService extends GeneralService<Teacher>{
	
	void SaveOrUpdateTeacher(Teacher teacher);
	
	BasicDataResult teacherDisable(String id); //禁用班级

	List<Teacher> findTeachersByisDisable(); //查询在使用状态下的班级

	
	
	
	
	
	
	
}
