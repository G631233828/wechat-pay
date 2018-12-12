package zhongchiedu.service;

import java.util.List;

import zhongchiedu.framework.pagination.Pagination;
import zhongchiedu.framework.service.GeneralService;
import zhongchiedu.school.pojo.Clazz;
import zhongchiedu.school.pojo.Leave;
import zhongchiedu.school.pojo.Student;

public interface LeaveService extends GeneralService<Leave> {

	void SaveOrUpdateClazz(Leave leave,String openId,String link);//发起请假和修改

	List<Leave> findLeaves(String clazzId,String startDate,String endDate);//通过班级id查看班级的请假情况
	
	List<Leave> findLeaveByAccount(String openId ,String startDate);
	
	List<Leave> findLeavesByStudentId(Student student,String page,String size);
	
	Pagination<Leave> findLeaveByPagination(Clazz clazz,Integer pageNo,Integer pageSize,String serach);
	

} 
