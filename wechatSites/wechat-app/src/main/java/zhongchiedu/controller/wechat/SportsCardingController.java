package zhongchiedu.controller.wechat;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;
import zhongchiedu.common.utils.BasicDataResult;
import zhongchiedu.common.utils.Common;
import zhongchiedu.school.pojo.Leave;
import zhongchiedu.school.pojo.SportsCarding;
import zhongchiedu.school.pojo.WeChatbanding;
import zhongchiedu.service.SportsCardingService;
import zhongchiedu.service.WeChatbandingService;

/**
 * 运动打卡
 * @author fliay
 *
 */
@RequestMapping("/wechat")
@Controller
@Slf4j
public class SportsCardingController {
	
	@Autowired
	private SportsCardingService sportsCardingService;
	@Autowired
	private WeChatbandingService weChatbandingService;
	
	/**
	 * 跳转到打卡界面
	 * 
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping("/tosports")
	public ModelAndView tosports(String openId) {
		ModelAndView model = new ModelAndView();
		SportsCarding sportsCarding = null;
		if(Common.isNotEmpty(openId)){
			
		}
		// 测试使用
		openId = "ooiMKv7cqR-2EgkeC9LdATpr-mbY";
		if (Common.isNotEmpty(openId)) {
			
			sportsCarding = this.sportsCardingService.findSportsCardingToday(openId);
			if(Common.isNotEmpty(sportsCarding)){
				model.addObject("sportsCarding", sportsCarding);
			}
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
		model.setViewName("wechat/front/sportsCarding/sportsCarding");
		model.addObject("today", LocalDate.now());
		
		return model;
	}

	
	
	@RequestMapping("/sports")
	@ResponseBody
	public BasicDataResult sports(String openId, SportsCarding sportsCarding) {
		
		if(Common.isNotEmpty(sportsCarding.getId())){
			//能够获取到今日运动的id则进行修改
			SportsCarding ed = this.sportsCardingService.findOneById(sportsCarding.getId(), SportsCarding.class);
			if(Common.isEmpty(ed)){
				return BasicDataResult.build(400, "数据异常，请刷新后在试！", null);
			}
		}/*else{
			//查看今日是否已经打卡
			SportsCarding today = this.sportsCardingService.findSportsCardingToday(openId);
			
			if(Common.isNotEmpty(today)){
				return BasicDataResult.build(400, "今天已经打过卡了，无法新建打卡记录", null);
			}
		}*/
		try {
			this.sportsCardingService.SaveOrUpdateSportsCarding(sportsCarding, openId);
			return BasicDataResult.build(200, "打卡成功",null);
		} catch (Exception e) {
			e.printStackTrace();
			return BasicDataResult.build(400, "打卡失败，请与管理员联系",null);
		}


	}
	
	
	
	
	
	

}
