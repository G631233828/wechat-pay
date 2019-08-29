package zhongchiedu.service.Impl;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import zhongchiedu.framework.service.GeneralServiceImpl;
import zhongchiedu.school.pojo.CardingStatistics;
import zhongchiedu.service.CardingStatisticsService;

/*
 * 统计班级打卡以及当前站点信息
 */
@Service
public class CardingStatisticsServiceImpl extends GeneralServiceImpl<CardingStatistics>
		implements CardingStatisticsService {

	@Override
	public CardingStatistics findByClazzIdAndActivityId(String clazzId, String activityId) {

		Query query = new Query();
		query.addCriteria(Criteria.where("clazz.$id").is(new ObjectId(clazzId)));
		query.addCriteria(Criteria.where("activitys.$id").is(new ObjectId(activityId)));
		return this.findOneByQuery(query, CardingStatistics.class);
	}

	@Override
	public CardingStatistics findByClazzId(String clazzId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("clazz.$id").is(new ObjectId(clazzId)));
		return this.findOneByQuery(query, CardingStatistics.class);
	}

}
