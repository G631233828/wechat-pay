package zhongchiedu.service.Impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhongchiedu.common.utils.Common;
import zhongchiedu.framework.service.GeneralServiceImpl;
import zhongchiedu.school.pojo.Activitys;
import zhongchiedu.school.pojo.CardingStatistics;
import zhongchiedu.school.pojo.Clazz;
import zhongchiedu.school.pojo.Sites;
import zhongchiedu.school.pojo.SportsCarding;
import zhongchiedu.school.pojo.Student;
import zhongchiedu.school.pojo.Trips;
import zhongchiedu.service.ActivitysService;
import zhongchiedu.service.CardingStatisticsService;
import zhongchiedu.service.ClazzService;
import zhongchiedu.service.ScheduleCardingStatisticsService;
import zhongchiedu.service.SitesService;
import zhongchiedu.service.SportsCardingService;
import zhongchiedu.service.StudentService;
import zhongchiedu.service.TripsService;

/*
 * 统计班级打卡以及当前站点信息
 */
@Service
public class ScheduleCardingStatisticsServiceImpl extends GeneralServiceImpl<CardingStatistics>
		implements ScheduleCardingStatisticsService {

	@Autowired
	public ClazzService clazzService;// 班级

	@Autowired
	private ActivitysService activitysService;// 活动

	@Autowired
	private SitesService sitesService;// 站点

	@Autowired
	private TripsService tripsService;// 站点行程

	@Autowired
	private SportsCardingService sportsCardingService;

	@Autowired
	private CardingStatisticsService cardingStatisticsService;

	@Autowired
	private StudentService studentService;

	@Override
	public void autoStatistics() {
		// 1.获取到所有的非禁用的活动
		List<Activitys> activitys = this.activitysService.findActivitysByisDisable();
		// 遍历活动
		activitys.forEach(activity -> {
			List<Clazz> clazzs = this.clazzService.findClazzsWhereInSchool(Common.findInSchoolYear());
			// 遍历班级
			clazzs.forEach(clazz -> {
				// 通过活动id跟班级id获取所有运动打卡记录
				List<SportsCarding> sports = this.sportsCardingService
						.findSportsCardingByActivityIdAndClazzId(clazz.getId(), activity.getId());
				Double allMileage = 0.0;// 统计班级学生总里程
				for (SportsCarding sport : sports) {
					allMileage += sport.getDistance();
				}
				Sites getSite = null;
				Double totalMileage = 0.0;// 总里程
				// 通过活动id查看所有站点信息 用来统计当前已经到哪个站点了
				List<Trips> trips = this.tripsService.findTripsByActivityId(activity.getId());
				// 默认站点为出发站
				getSite = trips.get(0).getSites();
				for (Trips trip : trips) {
					totalMileage += trip.getDistance();// 每个站点的距离
					// 如果班级里的行程已经大于站点直接的行程，那么已经到了下一站
					if (allMileage > totalMileage) {
						getSite = trip.getSites();
					}
				}
				// 根据班级获取所有学生
				List<Student> students = this.studentService.findStudentByClazz(clazz);
				// 根据班级，活动 学生 id获取每个学生的打卡情况

				List<SportsCarding> studentSportsCarding = new ArrayList<>();

				students.forEach(student -> {
					// 获取一个学生的所有打卡记录，并且统计运动量
					List<SportsCarding> lists = this.sportsCardingService
							.findSportsCardingByActivityIdAndStudentId(activity.getId(), student.getId());
					// 统计运动量
					double allDistance = 0.0;// 个人总运动量
					for (SportsCarding sportsCarding : lists) {
						allDistance += sportsCarding.getDistance();// 统计个人运动总量
					}
					SportsCarding sc = new SportsCarding();
					sc.setStudent(student);
					sc.setClazz(clazz);
					sc.setActivitys(activity);
					sc.setSportsDate(LocalDate.now().toString());
					sc.setAllDistance(allDistance);
					studentSportsCarding.add(sc);
				});

				List<SportsCarding> ls = this.toSort(studentSportsCarding);
				
				
				
				// 保存每个活动每个班级的数据，通过活动ID跟班级ID区分
				CardingStatistics cardingStatistics = this.cardingStatisticsService
						.findByClazzIdAndActivityId(clazz.getId(), activity.getId());
				if (Common.isEmpty(cardingStatistics)) {
					// 添加
					cardingStatistics = new CardingStatistics();
					cardingStatistics.setTotalMileage(totalMileage);// 班级总里程
					cardingStatistics.setAllMileage(allMileage);// 总里程
					cardingStatistics.setClazz(clazz);
					cardingStatistics.setSite(getSite);
					cardingStatistics.setActivitys(activity);
					cardingStatistics.setCardingDate(LocalDate.now().toString());
					cardingStatistics.setTrips(trips);
					int cardingStudents = this.sportsCardingService.findTodaySportsCardings(clazz.getId(),
							activity.getId(), LocalDate.now().toString());
					cardingStatistics.setStudentSportsCarding(ls);
					cardingStatistics.setCardingStudent(cardingStudents);
					this.insert(cardingStatistics);
				} else {
					// 更新
					cardingStatistics.setTotalMileage(totalMileage);// 班级总里程
					cardingStatistics.setAllMileage(allMileage);// 总里程
					cardingStatistics.setClazz(clazz);
					cardingStatistics.setSite(getSite);
					cardingStatistics.setActivitys(activity);
					cardingStatistics.setCardingDate(LocalDate.now().toString());
					cardingStatistics.setTrips(trips);
					int cardingStudents = this.sportsCardingService.findTodaySportsCardings(clazz.getId(),
							activity.getId(), LocalDate.now().toString());
					cardingStatistics.setCardingStudent(cardingStudents);
					cardingStatistics.setStudentSportsCarding(ls);
					this.save(cardingStatistics);
				}
			});

		});

	}

	public  List<SportsCarding> toSort(List<SportsCarding> list) {
		Collections.sort(list, new Comparator<SportsCarding>() {
			@Override
			public int compare(SportsCarding o1, SportsCarding o2) {
				if (o1.getAllDistance() > o2.getAllDistance()) {
					return -1;
				} else if (o1.getAllDistance() == o2.getAllDistance()) {
					return 0;
				} else {
					return 1;
				}
			}
		});
		return list;
	}


	
	
	
	
	
}
