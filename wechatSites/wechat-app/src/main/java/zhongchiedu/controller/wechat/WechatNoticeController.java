package zhongchiedu.controller.wechat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import zhongchiedu.common.utils.BasicDataResult;
import zhongchiedu.common.utils.Common;
import zhongchiedu.school.pojo.Clazz;
import zhongchiedu.school.pojo.Notice;
import zhongchiedu.school.pojo.Teacher;
import zhongchiedu.service.ClazzService;
import zhongchiedu.service.NoticeService;
import zhongchiedu.service.TeacherService;
import zhongchiedu.wechat.templates.schoolnotifcation.SchoolTemplateMessage;
import zhongchiedu.wechat.templates.schoolnotifcation.SwipingSchoolTemplate;
import zhongchiedu.wechat.util.accessToken.AccessToken;
import zhongchiedu.wechat.util.token.WeChatToken;

@Controller
@RequestMapping("/wechat")
public class WechatNoticeController {


	@Autowired
	private NoticeService noticeService;
	
	@Autowired
	private TeacherService teacherService;
	
	@Autowired
	private ClazzService clazzService;
	
	/**
	 * 跳转到发布通知页面
	 * @param openId
	 * @param id
	 * @return
	 */
	@RequestMapping("/teacher/tonotice")
	public ModelAndView toNotice(String id) {
		ModelAndView model = new ModelAndView();
		if(Common.isNotEmpty(id)){
			Teacher teacher = this.teacherService.findOneById(id, Teacher.class);  
			model.addObject("teacher", teacher);
		}
		model.setViewName("wechat/front/teacher/notice");
		return model;
	}
	
	
	@RequestMapping("/teacher/notice")
	@ResponseBody
	public BasicDataResult notice(Notice notice,String teacherId){
		//通过老师的id查询班级
		if(Common.isNotEmpty(teacherId)){
			Clazz clazz = this.clazzService.findHeadMaster(teacherId);
			if(Common.isNotEmpty(clazz)){
			 this.noticeService.SaveOrUpdateClazz(notice,clazz);
				
			 return BasicDataResult.build(200, "发布成功", null);
			}
		}
		
		return BasicDataResult.build(400, "发布失败，请联系管理员", null);
	}
	
	
	
	
	
	
	
}
