package zhongchiedu.service.Impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import zhongchiedu.school.pojo.Clazz;
import zhongchiedu.service.ClazzService;

@Service
public class ClazzServiceImpl extends GeneralServiceImpl<Clazz> implements ClazzService {

	@Override
	public void SaveOrUpdateClazz(Clazz clazz) {
		if (Common.isNotEmpty(clazz)) {
			if (clazz.getId() != null) {
				// 执行修改操作
				Clazz ed = this.findOneById(clazz.getId(), Clazz.class);
				if (ed == null)
					ed = new Clazz();
				clazz.setIsDisable(false);
				BeanUtils.copyProperties(clazz, ed);
				this.save(ed);
			} else {
				Clazz ed = new Clazz();
				BeanUtils.copyProperties(clazz, ed);
				// 执行添加操作
				this.insert(ed);
			}
		}
	}

	/**
	 * 查询所有在使用状态下的班级
	 */
	@Override
	public List<Clazz> findClazzsByisDisable() {
		Query query = new Query();
		List<Order> listsort = new ArrayList();
		listsort.add(new Order(Direction.ASC, "clazzYear"));
		listsort.add(new Order(Direction.ASC, "clazzNum"));
		query.with(new Sort(listsort));
		query.addCriteria(Criteria.where("isDisable").is(false));
		List<Clazz> list = this.find(query, Clazz.class);
		return list.size() > 0 ? list : null;
	}

	@Override
	public BasicDataResult clazzDisable(String id) {
		if (Common.isEmpty(id)) {
			return BasicDataResult.build(400, "无法禁用，请求出现问题，请刷新界面!", null);
		}
		Clazz clazz = this.findOneById(id, Clazz.class);
		if (clazz == null) {
			return BasicDataResult.build(400, "无法获取到班级，该班级可能已经被删除", null);
		}

		clazz.setIsDisable(clazz.getIsDisable().equals(true) ? false : true);
		this.save(clazz);

		return BasicDataResult.build(200, clazz.getIsDisable().equals(true) ? "班级禁用成功" : "班级启用成功",
				clazz.getIsDisable());
	}

	/**
	 * 使用年份，需要生成的班级数量进行生成班级
	 */
	@Override
	public void autoBatchClazz(int year, int num) {
		if (num > 0) {
			for (int i = 1; i <=num; i++) {
				Clazz getClazz = this.findClazzByYearNum(year, i);
				if (Common.isEmpty(getClazz)) {
					// 执行添加
					Clazz clazz = new Clazz();
					clazz.setClazzNum(i);
					clazz.setClazzYear(year);
					this.insert(clazz);
				} 
			}
		}
	}

	/**
	 * 通过年份跟班级号进行查询查看是否存在
	 * 
	 * @param year
	 * @param num
	 * @return
	 */
	@Override
	public Clazz findClazzByYearNum(int year, int num) {

		Query query = new Query();

		query.addCriteria(Criteria.where("clazzYear").is(year)).addCriteria(Criteria.where("clazzNum").is(num));

		return this.findOneByQuery(query, Clazz.class);

	}

	@Override
	public Clazz findHeadMaster(String id) {
		//通过老师的id查询班级
		Query query = new Query();
		query.addCriteria(Criteria.where("headMaster.$id").is(new ObjectId(id)));
		Clazz clazz = this.findOneByQuery(query, Clazz.class);
		return clazz;
	}

}
