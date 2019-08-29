package zhongchiedu.controller.wechat;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;
import zhongchiedu.common.utils.BasicDataResult;
import zhongchiedu.common.utils.Common;
import zhongchiedu.framework.pagination.Pagination;
import zhongchiedu.school.pojo.Activitys;
import zhongchiedu.school.pojo.CardingStatistics;
import zhongchiedu.school.pojo.Clazz;
import zhongchiedu.school.pojo.Leave;
import zhongchiedu.school.pojo.SportsCarding;
import zhongchiedu.school.pojo.WeChatbanding;
import zhongchiedu.service.ActivitysService;
import zhongchiedu.service.CardingStatisticsService;
import zhongchiedu.service.ClazzService;
import zhongchiedu.service.SportsCardingService;
import zhongchiedu.service.TripsService;
import zhongchiedu.service.WeChatbandingService;

/**
 * 运动打卡
 * 
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
	@Autowired
	private ClazzService clazzService;
	@Autowired
	private ActivitysService activitysService;// 获取所有活动
	@Autowired
	private CardingStatisticsService cardingStatisticsService;

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
		if (Common.isNotEmpty(openId)) {

		}
		// 测试使用
		//openId = "ooiMKv7cqR-2EgkeC9LdATpr-mbY";
		if (Common.isNotEmpty(openId)) {

			sportsCarding = this.sportsCardingService.findSportsCardingToday(openId);
			if (Common.isNotEmpty(sportsCarding)) {
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

		List<Activitys> activitys = this.activitysService.findActivitysByisDisable();

		model.addObject("openId", openId);
		model.addObject("activitys", activitys);
		model.setViewName("wechat/front/sportsCarding/sportsCarding");
		model.addObject("today", LocalDate.now());

		return model;
	}

	@RequestMapping("/sports")
	@ResponseBody
	public BasicDataResult sports(String openId, SportsCarding sportsCarding) {

		if (Common.isNotEmpty(sportsCarding.getId())) {
			// 能够获取到今日运动的id则进行修改
			SportsCarding ed = this.sportsCardingService.findOneById(sportsCarding.getId(), SportsCarding.class);
			if (Common.isEmpty(ed)) {
				return BasicDataResult.build(400, "数据异常，请刷新后在试！", null);
			}
		}
		// 通过活动id获取活动设置的每日个人运动打卡上限

		Activitys activity = this.activitysService.findOneById(sportsCarding.getActivitys().getId(), Activitys.class);
		if (sportsCarding.getDistance() > activity.getUpperLimit()) {

			return BasicDataResult.build(400, "打卡数据异常，请输入有效的值！", null);
		}

		try {
			this.sportsCardingService.SaveOrUpdateSportsCarding(sportsCarding, openId);
			return BasicDataResult.build(200, "打卡成功", null);
		} catch (Exception e) {
			e.printStackTrace();
			return BasicDataResult.build(400, "打卡失败，请与管理员联系", null);
		}

	}

	/*
	 * 通过openid 查到学生的Id ，通过学生的id查到该学生的运动记录
	 */
	@RequestMapping("/findsportsCarding")
	public ModelAndView findsportsCarding(String openId,
			@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
			@RequestParam(value = "pageSize", defaultValue = "50") Integer pageSize) {
		ModelAndView model = new ModelAndView();

		// 模拟openid生产删除
		//openId = "ooiMKv7cqR-2EgkeC9LdATpr-mbY";
		if (Common.isNotEmpty(openId)) {

			WeChatbanding bd = this.weChatbandingService.findWeChatbandingByOpenId(openId);
			if (Common.isNotEmpty(bd)) {
				String clazzYear = bd.getStudentAccount().substring(0, 4);
				String clazzNum = bd.getStudentAccount().substring(4, 6);
				bd.setClazzYear(Integer.valueOf(clazzYear));
				bd.setClazzNum(Integer.valueOf(clazzNum));
				model.addObject("wechatbanding", bd);
			}
			// 通过班级年份班级号获取班级
			Clazz clazz = this.clazzService.findClazzByYearNum(bd.getClazzYear(), bd.getClazzNum());
			Pagination<SportsCarding> pagination = this.sportsCardingService.findSportsCardings(clazz, pageNo,
					pageSize);
			model.addObject("pageList", pagination);
		}
		model.addObject("openId", openId);
		model.setViewName("wechat/front/sportsCarding/findsportsCarding");
		return model;
	}

	/*
	 * 通过查看百度地图中班级排名情况
	 */
	@RequestMapping("/findByMap")
	public ModelAndView findByMap(String openId) {
		ModelAndView model = new ModelAndView();
		List<Activitys> activitys = this.activitysService.findActivitysByisDisable();
		model.addObject("activitys", activitys);

		// 模拟openid生产删除
		//openId = "ooiMKv7cqR-2EgkeC9LdATpr-mbY";
		model.addObject("openId", openId);
		model.setViewName("wechat/front/sportsCarding/map");
		return model;
	}

	/**
	 * 通过活动id生成活动行程地图
	 * 
	 * @param activityId
	 * @return
	 */
	@RequestMapping("/findMap")
	@ResponseBody
	public BasicDataResult findMap(String activityId, String openId) {
		return this.sportsCardingService.findMap(activityId, openId);
	}

	/*
	 * 通过openid 查到学生的Id ，通过学生的id查到该学生的运动记录
	 */
	@RequestMapping("/findClassSportsCarding")
	public ModelAndView findsportsCarding(String openId) {
		ModelAndView model = new ModelAndView();

//		// 模拟openid生产删除
//		openId = "ooiMKv7cqR-2EgkeC9LdATpr-mbY";
		if (Common.isNotEmpty(openId)) {

			WeChatbanding bd = this.weChatbandingService.findWeChatbandingByOpenId(openId);
			if (Common.isNotEmpty(bd)) {
				String clazzYear = bd.getStudentAccount().substring(0, 4);
				String clazzNum = bd.getStudentAccount().substring(4, 6);
				bd.setClazzYear(Integer.valueOf(clazzYear));
				bd.setClazzNum(Integer.valueOf(clazzNum));
				model.addObject("wechatbanding", bd);
			}
			// 通过班级年份班级号获取班级
			Clazz clazz = this.clazzService.findClazzByYearNum(bd.getClazzYear(), bd.getClazzNum());
			// 通过班级ID查看排名统计
			CardingStatistics cardingStatistics = this.cardingStatisticsService.findByClazzId(clazz.getId());

			model.addObject("cardingStatistics", cardingStatistics);
		}
		model.addObject("openId", openId);
		model.setViewName("wechat/front/sportsCarding/findClassSportsCarding");
		return model;
	}

}
