package zhongchiedu.service;

import zhongchiedu.framework.service.GeneralService;
import zhongchiedu.school.pojo.Clazz;
import zhongchiedu.school.pojo.Notice;

public interface NoticeService extends GeneralService<Notice> {

	void SaveOrUpdateClazz(Notice notice,Clazz clazz);//发起请假和修改


} 
