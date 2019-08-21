package zhongchiedu.service.Impl;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

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

		query.addCriteria(Criteria.where("activitys.$id").is(activityId));
		
		List<Trips>  list = this.find(query, Trips.class);
		
		return list;
	}

	
}
