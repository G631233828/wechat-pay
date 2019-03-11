package zhongchiedu.controller.wechat;

import java.time.LocalDate;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import zhongchiedu.common.utils.BasicDataResult;
import zhongchiedu.common.utils.Common;
import zhongchiedu.framework.pagination.Pagination;
import zhongchiedu.school.pojo.Leave;
import zhongchiedu.school.pojo.Student;
import zhongchiedu.school.pojo.WeChatbanding;
import zhongchiedu.service.LeaveService;
import zhongchiedu.service.StudentService;
import zhongchiedu.service.WeChatbandingService;

@RequestMapping("/wechat")
@Controller
public class WechatLeaveController {

	@Autowired
	private WeChatbandingService weChatbandingService;
	@Autowired
	private LeaveService leaveService;
	@Autowired
	private StudentService studentService;

	/**
	 * 跳转到登录界面
	 * 
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping("/toleave")
	public ModelAndView toleave(String openId,String id) {
		ModelAndView model = new ModelAndView();
		Leave leave = null;
		if(Common.isNotEmpty(id)){
			 leave = this.leaveService.findOneById(id, Leave.class);
			if(Common.isNotEmpty(leave)){
				model.addObject("leave", leave);
			}
		}
		// 测试使用
		//openId = "ooiMKv7cqR-2EgkeC9LdATpr-mbY";
		if (Common.isNotEmpty(openId)) {
			WeChatbanding bd = this.weChatbandingService.findWeChatbandingByOpenId(openId);
			if (Common.isNotEmpty(bd)) {
				String clazzYear = bd.getStudentAccount().substring(0, 4);
				String clazzNum = bd.getStudentAccount().substring(4, 6);
				// String code = bd.getStudentAccount().substring(6, 8);
				bd.setClazzYear(Integer.valueOf(clazzYear));
				bd.setClazzNum(Integer.valueOf(clazzNum));
				model.addObject("wechatbanding", bd);
			}
		}
		model.addObject("openId", openId);
		model.setViewName("wechat/front/leave");
		if(Common.isNotEmpty(leave)){
			LocalDate today = LocalDate.now();
			String leaveDay = leave.getStartLeave();
			int i =Common.compare_date(today.toString(), leaveDay);
			model.addObject("flag", i<0?true:false);
		}else{
			model.addObject("flag",false);
		}
		
		return model;
	}

	
	
	
	
	@Value("${server.servlet.context-path}")
	private String path;
	
	@Value("${wechat.serverUrl}")
	private String serverUrl;
	/**
	 * 请假
	 * 
	 * @return
	 */
	@RequestMapping("/leave")
	@ResponseBody
	public BasicDataResult leave(String openId, Leave leave) {
		
		if(Common.isNotEmpty(leave.getId())){
			//能够获取到请假的id则进行修改请假原因
			Leave ed = this.leaveService.findOneById(leave.getId(), Leave.class);
			if(Common.isEmpty(ed)){
				return BasicDataResult.build(400, "数据异常，请刷新后在试！", null);
			}
		}else{
			//针对请假的时间进行验证查看是否在该时间段内已经有请假
			List<Leave> list = this.leaveService.findLeaveByAccount(openId, leave.getStartLeave());
			if(list.size()>0){
				return BasicDataResult.build(400, "该时间段已经有一个请假了，请取消后再试", null);
			}
		}
		try {
			String link = serverUrl+path+"/wechat/teacher/findleave?id=";
			this.leaveService.SaveOrUpdateClazz(leave, openId,link);
			return BasicDataResult.build(200, "请假成功",null);
		} catch (Exception e) {
			e.printStackTrace();
			return BasicDataResult.build(400, "请假失败，请与管理员联系",null);
		}

	}
	
	
	
	
	@RequestMapping("/leavehistory")
	public ModelAndView toLeavehistory(String openId) {
		//openId = "ooiMKv7cqR-2EgkeC9LdATpr-mbY";
		ModelAndView model = new ModelAndView();
		// 测试使用
		if (Common.isNotEmpty(openId)) {
			WeChatbanding bd = this.weChatbandingService.findWeChatbandingByOpenId(openId);
			if (Common.isNotEmpty(bd)) {
				String clazzYear = bd.getStudentAccount().substring(0, 4);
				String clazzNum = bd.getStudentAccount().substring(4, 6);
				bd.setClazzYear(Integer.valueOf(clazzYear));
				bd.setClazzNum(Integer.valueOf(clazzNum));
				model.addObject("wechatbanding", bd);
			}
		}
		model.addObject("openId", openId);
		model.setViewName("wechat/front/leavehistory");
		return model;
	}

	

	
	
	
	@RequestMapping("/getleave")
	@ResponseBody
	public BasicDataResult getleave(String page , String size ,String openId) {
		
		if(Common.isNotEmpty(openId)){
			WeChatbanding we = this.weChatbandingService.findWeChatbandingByOpenId(openId);
			if(Common.isNotEmpty(we)){
				//通过学生的学籍号获取学生的信息
				Student student = this.studentService.findStudentByRegisterNum2(we.getStudentAccount());
				if(Common.isNotEmpty(student)){
					//获取学生的请假信息
				List<Leave> list = this.leaveService.findLeavesByStudentId(student,page,size);
					
					return BasicDataResult.build(200, "查询成功", list);
				}
			}
		}
				
		return BasicDataResult.build(400, "查询失败", null);
	}
	
	
	

	
	
	@RequestMapping("/cancelLeave")
	@ResponseBody
	public BasicDataResult cancelLeave(String openId, Leave leave) {
		
		if(Common.isEmpty(leave)){
			return BasicDataResult.build(400, "取消请假失败", null);
		}
		if(Common.isEmpty(leave.getId())){
			return BasicDataResult.build(400, "取消失败，未能获取到请假信息", null);
		}
		//根据id获取请假信息
	    Leave del = this.leaveService.findOneById(leave.getId(), Leave.class);
	    //获取取消请假的日期 不能取消过去的请假
	    //TODO 
	    
	    
	    if(Common.isNotEmpty(del)){
	    	this.leaveService.remove(del);
	    	//发送通知通知老师取消请假的信息
	    	//TODO
	    	return BasicDataResult.build(200, "取消请假成功", null);
	    }
		return BasicDataResult.build(400, "取消请假失败", null);
		
		
	}
	
	
	
	
	
	
	
	

}
