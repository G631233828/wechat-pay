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
import zhongchiedu.school.pojo.Sites;
import zhongchiedu.service.SitesService;

@Service
public class SitesServiceImpl extends GeneralServiceImpl<Sites> implements SitesService {

	@Override
	public void SaveOrUpdateSites(Sites sites) {
		if (Common.isNotEmpty(sites)) {
			if (sites.getId() != null) {
				// 执行修改操作
				Sites ed = this.findOneById(sites.getId(), Sites.class);
				if (ed == null)
					ed = new Sites();
				sites.setIsDisable(false);
				BeanUtils.copyProperties(sites, ed);
				this.save(ed);
			} else {
				Sites ed = new Sites();
				sites.setNextId(null);//下个地点ID
				sites.setParendId("0");//上个地点ID
				sites.setSitesId(null);//出发地ID
				sites.setDistance(0.0);//上一站距离
				BeanUtils.copyProperties(sites, ed);
				// 执行添加操作
				this.insert(ed);
			}
		}

	}

	@Override
	public BasicDataResult sitesDisable(String id) {
		if (Common.isEmpty(id)) {
			return BasicDataResult.build(400, "无法禁用，请求出现问题，请刷新界面!", null);
		}
		Sites Sites = this.findOneById(id, Sites.class);
		if (Sites == null) {
			return BasicDataResult.build(400, "无法获取到数据，可能已经被删除", null);
		}

		Sites.setIsDisable(Sites.getIsDisable().equals(true) ? false : true);
		this.save(Sites);

		return BasicDataResult.build(200, Sites.getIsDisable().equals(true) ? "禁用成功" : "启用成功",
				Sites.getIsDisable());
	}

	@Override
	public List<Sites> findsitesByisDisable() {
		Query query = new Query();
		List<Order> listsort = new ArrayList();
		listsort.add(new Order(Direction.ASC, "createDate"));
		query.with(new Sort(listsort));
		query.addCriteria(Criteria.where("isDisable").is(false));
		List<Sites> list = this.find(query, Sites.class);
		return list.size() > 0 ? list : null;
	}

}
