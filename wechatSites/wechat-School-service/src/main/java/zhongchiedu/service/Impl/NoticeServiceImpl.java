package zhongchiedu.service.Impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhongchiedu.common.utils.Common;
import zhongchiedu.framework.service.GeneralServiceImpl;
import zhongchiedu.school.pojo.Clazz;
import zhongchiedu.school.pojo.Notice;
import zhongchiedu.school.pojo.Student;
import zhongchiedu.service.NoticeService;
import zhongchiedu.service.StudentService;
import zhongchiedu.service.WeChatbandingService;
import zhongchiedu.wechat.oauto2.NSNUserInfo;
import zhongchiedu.wechat.templates.schoolnotifcation.SchoolTemplateMessage;
import zhongchiedu.wechat.templates.schoolnotifcation.SwipingSchoolTemplate;
import zhongchiedu.wechat.util.accessToken.AccessToken;
import zhongchiedu.wechat.util.token.WeChatToken;

@Service
public class NoticeServiceImpl extends GeneralServiceImpl<Notice> implements NoticeService {

	private static final Logger log = LoggerFactory.getLogger(NoticeServiceImpl.class);
	@Autowired
	private StudentService studentService;
	
	@Autowired
	private WeChatbandingService weChatbandingService;
	
	
	@Override
	public void SaveOrUpdateClazz(Notice notice, Clazz clazz) {
		Notice ed = null;
		notice.setClazz(clazz);
		notice.setTeacher(clazz.getHeadMaster());
		if (Common.isNotEmpty(notice.getId())) {
			// 执行修改
			ed = this.findOneById(notice.getId(), Notice.class);
			if (ed == null)
				ed = new Notice();
			BeanUtils.copyProperties(notice, ed);
			this.save(ed);
		} else {
			ed = new Notice();
			BeanUtils.copyProperties(notice, ed);
			this.insert(ed);
		}
		
		log.info("发送数据保存成功！"+ed);
		//通过班级获取所有的学生
		List<Student> list = this.studentService.findStudentByClazz(clazz);
		if(list.size()>0){
			list.forEach(student->{
				NSNUserInfo nsnUserInfo = this.weChatbandingService.findnsnByStudentAccount(student.getAccount());
				if(Common.isNotEmpty(nsnUserInfo)){
					//添加完成，执行发布操作
					SchoolTemplateMessage school = new SchoolTemplateMessage();
					school.setTitle(notice.getTitle());
					school.setContent(notice.getContent());
					school.setNotifications(clazz.getHeadMaster().getName());
					school.setSchool("上海福山外国语小学");
					AccessToken at= WeChatToken.getInstance().getAccessToken();
					//ooiMKvywAoyhK1gF29qrq1tllE6I
					//ooiMKv7cqR-2EgkeC9LdATpr-mbY
					SwipingSchoolTemplate.swipingSchoolNotifcation(nsnUserInfo.getOpenid(),at.getToken(),school);
				}
					
			});
		}
	
		
		
		
		
	
		
	 
		
		
		
		
		
		
		

	}

}
