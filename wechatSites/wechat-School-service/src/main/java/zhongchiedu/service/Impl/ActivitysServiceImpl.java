package zhongchiedu.service.Impl;

import java.util.ArrayList;
import java.util.List;

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
import zhongchiedu.school.pojo.Activitys;
import zhongchiedu.school.pojo.Clazz;
import zhongchiedu.service.ActivitysService;

@Service
public class ActivitysServiceImpl extends GeneralServiceImpl<Activitys> implements ActivitysService {

	@Override
	public void SaveOrUpdateActivitys(Activitys activitys) {
		if (Common.isNotEmpty(activitys)) {
			if (activitys.getId() != null) {
				// 执行修改操作
				Activitys ed = this.findOneById(activitys.getId(), Activitys.class);
				if (ed == null)
					ed = new Activitys();
				activitys.setIsDisable(false);
				BeanUtils.copyProperties(activitys, ed);
				this.save(ed);
			} else {
				Activitys ed = new Activitys();
				BeanUtils.copyProperties(activitys, ed);
				// 执行添加操作
				this.insert(ed);
			}
		}

	}

	@Override
	public BasicDataResult activitysDisable(String id) {
		if (Common.isEmpty(id)) {
			return BasicDataResult.build(400, "无法禁用，请求出现问题，请刷新界面!", null);
		}
		Activitys activitys = this.findOneById(id, Activitys.class);
		if (activitys == null) {
			return BasicDataResult.build(400, "无法获取到数据，可能已经被删除", null);
		}

		activitys.setIsDisable(activitys.getIsDisable().equals(true) ? false : true);
		this.save(activitys);

		return BasicDataResult.build(200, activitys.getIsDisable().equals(true) ? "禁用成功" : "启用成功",
				activitys.getIsDisable());
	}

	@Override
	public List<Activitys> findActivitysByisDisable() {
		Query query = new Query();
		List<Order> listsort = new ArrayList();
		listsort.add(new Order(Direction.ASC, "createDate"));
		query.with(new Sort(listsort));
		query.addCriteria(Criteria.where("isDisable").is(false));
		List<Activitys> list = this.find(query, Activitys.class);
		return list.size() > 0 ? list : null;
	}

}
