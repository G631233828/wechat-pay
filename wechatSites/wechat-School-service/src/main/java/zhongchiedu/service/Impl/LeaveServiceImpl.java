package zhongchiedu.service.Impl;

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

import zhongchiedu.common.utils.Common;
import zhongchiedu.framework.pagination.Pagination;
import zhongchiedu.framework.service.GeneralServiceImpl;
import zhongchiedu.school.dao.LeaveDao;
import zhongchiedu.school.pojo.Clazz;
import zhongchiedu.school.pojo.Leave;
import zhongchiedu.school.pojo.Student;
import zhongchiedu.school.pojo.WeChatbanding;
import zhongchiedu.service.ClazzService;
import zhongchiedu.service.LeaveService;
import zhongchiedu.service.StudentService;
import zhongchiedu.service.WeChatbandingService;
import zhongchiedu.wechat.templates.leavenotifcation.LeaveTemplateMessage;
import zhongchiedu.wechat.templates.leavenotifcation.SwipingLeaveTemplate;
import zhongchiedu.wechat.util.accessToken.AccessToken;
import zhongchiedu.wechat.util.token.WeChatToken;

@Service
public class LeaveServiceImpl extends GeneralServiceImpl<Leave> implements LeaveService {
	@Autowired
	private StudentService studentService;
	@Autowired
	private ClazzService clazzSercice;
	@Autowired
	private WeChatbandingService weChatbandingService;

	@Override
	public void SaveOrUpdateClazz(Leave leave, String openId, String link) {
		Clazz clazz = null;
		if (Common.isNotEmpty(openId)) {
			WeChatbanding we = this.weChatbandingService.findWeChatbandingByOpenId(openId);
			leave.setNsnUserInfo(we.getNsnUserInfo());
			String account = we.getStudentAccount();
			if (Common.isNotEmpty(account)) {
				int year = Integer.valueOf(account.substring(0, 4));
				int num = Integer.valueOf(account.substring(4, 6));
				clazz = this.clazzSercice.findClazzByYearNum(year, num);
				leave.setClazz(Common.isNotEmpty(clazz) ? clazz : null);
				Student student = this.studentService.findStudentByRegisterNum2(account);
				leave.setStudent(Common.isNotEmpty(student) ? student : null);
			}
		}
		Leave ed = null;
		if (Common.isNotEmpty(leave)) {
			if (Common.isNotEmpty(leave.getId())) {
				ed = this.findOneById(leave.getId(), Leave.class);
				if (ed == null)
					ed = new Leave();
				BeanUtils.copyProperties(leave, ed);
				this.save(ed);
			} else {
				ed = new Leave();
				BeanUtils.copyProperties(leave, ed);
				this.insert(ed);
			}
			// 执行请假通知推送给老师
			LeaveTemplateMessage let = new LeaveTemplateMessage();
			let.setName(ed.getStudent().getName());
			let.setReason(ed.getReason());
			let.setDate(ed.getStartLeave());
			// 获取班主任的openid
			String teacherOpenid = clazz.getHeadMaster().getOpenId();
			String headMasterid = clazz.getHeadMaster().getId();
			AccessToken at = WeChatToken.getInstance().getAccessToken();
			SwipingLeaveTemplate.swipingLeaveNotifcation(link + headMasterid, teacherOpenid, at.getToken(), let);

		}

	}

	/**
	 * 根据班级查询请假
	 */
	@Override
	public List<Leave> findLeaves(String clazzId, String startDate, String endDate) {
		Query query = new Query();
		query.addCriteria(Criteria.where("clazz.$id").is(clazzId));
		if (Common.isNotEmpty(startDate)) {
			query.addCriteria(Criteria.where("startLeave").gte(startDate));
		}
		if (Common.isNotEmpty(endDate)) {
			query.addCriteria(Criteria.where("endLeave").lte(endDate));
		}
		List<Leave> list = this.find(query, Leave.class);
		return Common.isNotEmpty(list) ? list : null;
	}

	@Override
	public List<Leave> findLeaveByAccount(String openId, String startDate) {
		List<Leave> list = null;
		if (Common.isNotEmpty(openId)) {
			WeChatbanding we = this.weChatbandingService.findWeChatbandingByOpenId(openId);
			if (Common.isNotEmpty(we)) {
				Student student = this.studentService.findStudentByRegisterNum2(we.getStudentAccount());
				Query query = new Query();
				query.addCriteria(Criteria.where("student.$id").is(new ObjectId(student.getId())));
				query.addCriteria(Criteria.where("startLeave").is(startDate));
				list = this.find(query, Leave.class);
			}
		}
		return list;
	}

	@Override
	public List<Leave> findLeavesByStudentId(Student student, String page, String size) {
		Pagination<Leave> leaves = null;
		if (Common.isNotEmpty(student)) {
			Query query = new Query();
			query.addCriteria(Criteria.where("student.$id").is(new ObjectId(student.getId())));
			query.with(new Sort(new Order(Direction.DESC, "startLeave")));
			leaves = this.findPaginationByQuery(query, Integer.valueOf(page), Integer.valueOf(size), Leave.class);
		}
		return leaves.getDatas();
	}

	/**
	 * 通过老师的id进行查询
	 */
	@Override
	public Pagination<Leave> findLeaveByPagination(Clazz clazz, Integer pageNo, Integer pageSize, String serach) {
		// 通过班级id查询学生
		Query query = new Query();
		if (Common.isNotEmpty(serach)) {

			// 根据班级id去查下匹配的学生
			Query stu = new Query();
			Criteria cr = new Criteria();
			Criteria ca1 = null;
			Criteria ca2 = null;
			ca1 = Criteria.where("name").regex(serach);
			ca2 = Criteria.where("code").regex(serach);
			stu.addCriteria(Criteria.where("clazz.$id").is(new ObjectId(clazz.getId())));
			stu.addCriteria(cr.orOperator(ca1, ca2));
			List<Student> list = this.studentService.find(stu, Student.class);
			List listids = new ArrayList();
			if (list.size() > 0) {
				for (Student s : list) {
					listids.add(new ObjectId(s.getId()));
				}
			}
			Criteria cr1 = new Criteria();
			Criteria ca3 = null;
			Criteria ca4 = null;
			ca3 = Criteria.where("startLeave").regex(serach);
			ca4 = Criteria.where("student.$id").in(listids);
			query.addCriteria(cr1.orOperator(ca3, ca4));
		}

		query.with(new Sort(new Order(Direction.DESC, "startLeave")));
		query.addCriteria(Criteria.where("clazz.$id").is(new ObjectId(clazz.getId())));
		return this.findPaginationByQuery(query, pageNo, pageSize, Leave.class);
	}

}
