package zhongchiedu.service;

import java.util.List;

import zhongchiedu.common.utils.BasicDataResult;
import zhongchiedu.framework.service.GeneralService;
import zhongchiedu.school.pojo.Trips;


public interface TripsService extends GeneralService<Trips>{

	void SaveOrUpdateTrips(Trips trips);//添加或修改活动

	List<Trips> findTripsByActivityId(String activityId);
	
	BasicDataResult findAndEditTrips(String id, Integer sorts,Double distance);


}
