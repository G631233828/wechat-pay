package zhongchiedu.service.Impl;

import java.time.LocalDate;

import org.bson.types.ObjectId;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import zhongchiedu.common.utils.Common;
import zhongchiedu.framework.service.GeneralServiceImpl;
import zhongchiedu.school.pojo.Clazz;
import zhongchiedu.school.pojo.SportsCarding;
import zhongchiedu.school.pojo.Student;
import zhongchiedu.school.pojo.WeChatbanding;
import zhongchiedu.service.ClazzService;
import zhongchiedu.service.SportsCardingService;
import zhongchiedu.service.StudentService;
import zhongchiedu.service.WeChatbandingService;

@Service
public class SportsCardingServiceImpl extends GeneralServiceImpl<SportsCarding> implements SportsCardingService {
	@Autowired
	private StudentService studentService;
	@Autowired
	private ClazzService clazzSercice;
	@Autowired
	private WeChatbandingService weChatbandingService;

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
		if(Common.isNotEmpty(openId)){
			WeChatbanding we = this.weChatbandingService.findWeChatbandingByOpenId(openId);
			if(Common.isNotEmpty(we)){
				Student student = this.studentService.findStudentByRegisterNum2(we.getStudentAccount());
				Query query = new Query();
				query.addCriteria(Criteria.where("student.$id").is(new ObjectId(student.getId())));
				query.addCriteria(Criteria.where("sportsDate").is( LocalDate.now().toString()));
				sportsCarding = this.findOneByQuery(query, SportsCarding.class);
			}
		}
		return sportsCarding;
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
