package zhongchiedu.service.Impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import zhongchiedu.common.utils.BasicDataResult;
import zhongchiedu.common.utils.Common;
import zhongchiedu.framework.pagination.Pagination;
import zhongchiedu.framework.service.GeneralServiceImpl;
import zhongchiedu.school.pojo.CardingStatistics;
import zhongchiedu.school.pojo.Clazz;
import zhongchiedu.school.pojo.Sites;
import zhongchiedu.school.pojo.SportsCarding;
import zhongchiedu.school.pojo.Student;
import zhongchiedu.school.pojo.Trips;
import zhongchiedu.school.pojo.WeChatbanding;
import zhongchiedu.service.CardingStatisticsService;
import zhongchiedu.service.ClazzService;
import zhongchiedu.service.SportsCardingService;
import zhongchiedu.service.StudentService;
import zhongchiedu.service.TripsService;
import zhongchiedu.service.WeChatbandingService;

@Service
public class SportsCardingServiceImpl extends GeneralServiceImpl<SportsCarding> implements SportsCardingService {
	@Autowired
	private StudentService studentService;
	@Autowired
	private ClazzService clazzSercice;
	@Autowired
	private WeChatbandingService weChatbandingService;
	@Autowired
	private TripsService tripsService;// 活动行程
	@Autowired
	private CardingStatisticsService cardingStatisticsService;

	@Override
	public void SaveOrUpdateSportsCarding(SportsCarding sportsCarding, String openId) {
		Clazz clazz = null;
		if (Common.isNotEmpty(openId)) {
			WeChatbanding we = this.weChatbandingService.findWeChatbandingByOpenId(openId);
			sportsCarding.setNsnUserInfo(we.getNsnUserInfo());
			String account = we.getStudentAccount();
			if (Common.isNotEmpty(account)) {
				int year = Integer.valueOf(account.substring(0, 4));
				int num = Integer.valueOf(account.substring(4, 6));
				clazz = this.clazzSercice.findClazzByYearNum(year, num);
				sportsCarding.setClazz(Common.isNotEmpty(clazz) ? clazz : null);
				Student student = this.studentService.findStudentByRegisterNum2(account);
				sportsCarding.setStudent(Common.isNotEmpty(student) ? student : null);
			}
		}
		sportsCarding.setSportsDate(LocalDate.now().toString());
		SportsCarding ed = null;
		if (Common.isNotEmpty(sportsCarding)) {
			sportsCarding.setSportsDate(LocalDate.now().toString());
			if (Common.isNotEmpty(sportsCarding.getId())) {
				ed = this.findOneById(sportsCarding.getId(), SportsCarding.class);
				if (ed == null)
					ed = new SportsCarding();
				BeanUtils.copyProperties(sportsCarding, ed);
				this.save(ed);
			} else {
				ed = new SportsCarding();
				BeanUtils.copyProperties(sportsCarding, ed);
				this.insert(ed);
			}
		}

	}

	@Override
	public SportsCarding findSportsCardingToday(String openId) {
		SportsCarding sportsCarding = null;
		if (Common.isNotEmpty(openId)) {
			WeChatbanding we = this.weChatbandingService.findWeChatbandingByOpenId(openId);
			if (Common.isNotEmpty(we)) {
				Student student = this.studentService.findStudentByRegisterNum2(we.getStudentAccount());
				Query query = new Query();
				query.addCriteria(Criteria.where("student.$id").is(new ObjectId(student.getId())));
				query.addCriteria(Criteria.where("sportsDate").is(LocalDate.now().toString()));
				sportsCarding = this.findOneByQuery(query, SportsCarding.class);
			}
		}
		return sportsCarding;
	}

	@Override
	public Pagination<SportsCarding> findSportsCardings(Clazz clazz, Integer pageNo, Integer pageSize) {

		Query query = new Query();
		query.addCriteria(Criteria.where("clazz.$id").is(new ObjectId(clazz.getId())));
		query.with(new Sort(new Order(Direction.DESC, "sportsDate")));
		Pagination<SportsCarding> list = this.findPaginationByQuery(query, pageNo, pageSize, SportsCarding.class);
		return list;
	}

	@Override
	public BasicDataResult findMap(String activityId, String openId) {
		if (Common.isEmpty(activityId)) {
			return BasicDataResult.build(400, "未能找到活动信息！", null);
		}
		if (Common.isEmpty(openId)) {
			return BasicDataResult.build(400, "未能匹配到用户信息！！", null);
		}

		WeChatbanding bd = this.weChatbandingService.findWeChatbandingByOpenId(openId);

		Clazz clazz = this.findClazz(bd);

		// 在自动统计的的打卡信息中获取信息，如果没获取到则返回空

		// 通过班级id活动id获取数据
		CardingStatistics cardingStatistics = this.cardingStatisticsService.findByClazzIdAndActivityId(clazz.getId(),
				activityId);
		if (Common.isEmpty(cardingStatistics)) {
			return BasicDataResult.build(204, "打卡统计正在生成中！！", null);
		}

		// 通过活动id获取所有的站点
		List<Trips> list = cardingStatistics.getTrips();
		List<Sites> sites = new ArrayList<>();
		list.forEach(trips -> {
			sites.add(trips.getSites());
		});

		cardingStatistics.setSites(sites);// 设置所有站点

		return BasicDataResult.build(200, "查询成功", cardingStatistics);
	}

	/**
	 * 获取班级信息
	 * 
	 * @param bd
	 * @return
	 */
	public Clazz findClazz(WeChatbanding bd) {
		Clazz clazz = null;
		if (Common.isNotEmpty(bd)) {
			String clazzYear = bd.getStudentAccount().substring(0, 4);
			String clazzNum = bd.getStudentAccount().substring(4, 6);
			clazz = this.clazzSercice.findClazzByYearNum(Integer.valueOf(clazzYear), Integer.valueOf(clazzNum));
		}

		return clazz;
	}

	/**
	 * 通过班级id 跟 活动id查询 班级的运动数据
	 * 
	 * @param activityId
	 * @param clazzId
	 * @return
	 */
	@Override
	public List<SportsCarding> findSportsCardingByActivityIdAndClazzId(String clazzId, String activityId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("clazz.$id").is(new ObjectId(clazzId)))
				.addCriteria(Criteria.where("activitys.$id").is(new ObjectId(activityId)));
		return this.find(query, SportsCarding.class);

	}

	@Override
	public int findTodaySportsCardings(String clazzId, String activityId, String date) {
		Query query = new Query();
		query.addCriteria(Criteria.where("clazz.$id").is(new ObjectId(clazzId)))
				.addCriteria(Criteria.where("sportsDate").is(date))
				.addCriteria(Criteria.where("activitys.$id").is(new ObjectId(activityId)));
		return this.findCountByQuery(query, SportsCarding.class);
	}

	@Override
	public List<SportsCarding> findSportsCardingByActivityIdAndStudentId(String activityId,
			String studentId) {

		Query query = new Query();
		query.addCriteria(Criteria.where("activitys.$id").is(new ObjectId(activityId)))
				.addCriteria(Criteria.where("student.$id").is(new ObjectId(studentId)));
		return this.find(query, SportsCarding.class);
	}

	

}
