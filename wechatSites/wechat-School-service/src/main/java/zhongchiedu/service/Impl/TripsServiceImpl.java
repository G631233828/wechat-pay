package zhongchiedu.service.Impl;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import zhongchiedu.common.utils.BasicDataResult;
import zhongchiedu.common.utils.Common;
import zhongchiedu.framework.service.GeneralServiceImpl;
import zhongchiedu.school.pojo.Trips;
import zhongchiedu.service.TripsService;

@Service
public class TripsServiceImpl extends GeneralServiceImpl<Trips> implements TripsService {

	@Override
	public void SaveOrUpdateTrips(Trips trips) {
		if (Common.isNotEmpty(trips)) {
			if (trips.getId() != null) {
				// 执行修改操作
				Trips ed = this.findOneById(trips.getId(), Trips.class);
				if (ed == null)
					ed = new Trips();
				trips.setIsDisable(false);
				BeanUtils.copyProperties(trips, ed);
				this.save(ed);
			} else {
				Trips ed = new Trips();
				BeanUtils.copyProperties(trips, ed);
				// 执行添加操作
				this.insert(ed);
			}
		}

	}

	@Override
	public List<Trips> findTripsByActivityId(String activityId) {
		
		Query query = new Query();

		query.addCriteria(Criteria.where("activitys.$id").is(new ObjectId(activityId)));
		query.with(new Sort(new Order(Direction.ASC, "sorts")));
		
		List<Trips>  list = this.find(query, Trips.class);
		
		return list;
	}

	@Override
	public BasicDataResult findAndEditTrips(String id, Integer sorts, Double distance) {

		Trips trips = this.findOneById(id, Trips.class);
		
		if(Common.isNotEmpty(trips)){
			if(Common.isNotEmpty(sorts)){
				trips.setSorts(sorts);
			}
			if(Common.isNotEmpty(distance)){
				trips.setDistance(distance);
			}
			this.save(trips);
			return BasicDataResult.build(200, trips.getActivitys().getId(), null);
		}
		
		
		return BasicDataResult.build(400, "未能获取到数据", null);
	}

	
}
