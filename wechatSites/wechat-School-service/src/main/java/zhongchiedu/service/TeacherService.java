package zhongchiedu.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import zhongchiedu.common.utils.BasicDataResult;
import zhongchiedu.framework.service.GeneralService;
import zhongchiedu.school.pojo.Teacher;


public interface TeacherService extends GeneralService<Teacher>{
	
	void SaveOrUpdateTeacher(Teacher teacher);
	
	BasicDataResult teacherDisable(String id); //禁用班级

	List<Teacher> findTeachersByisDisable(); //查询在使用状态下的班级

	String BatchImport(File file, int row, HttpSession session) throws IOException;
	
	Teacher findTeacherByOpenId(String openId);
	
	Teacher findTeacherByAccount(String account,String password);
	
	BasicDataResult CheckTeacherPassword(String id,String password);
	
	BasicDataResult ChangeTeacherPassword(String id ,String password);
	
	
	
}
