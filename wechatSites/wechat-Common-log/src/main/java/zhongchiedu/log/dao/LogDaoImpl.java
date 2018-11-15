package zhongchiedu.log.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import zhongchiedu.framework.dao.GeneralDaoImpl;
import zhongchiedu.log.pojo.Log;

@Repository
public class LogDaoImpl  extends GeneralDaoImpl<Log> implements LogDao{
	
	private static final Logger log = LoggerFactory.getLogger(LogDaoImpl.class);
	
	/***
	 * 时间倒叙查询所有的日志信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<Log> findAllLog() throws Exception {
		Query query = new Query();
		query.with(new Sort(new Order(Direction.DESC, "createDate")));

		List<Log> loglist = this.find(query, Log.class);

		if (loglist == null)
			return null;
		else
			return loglist;
	}

	/**
	 * 
	 * @Title: findAllLogbyQuery @Description: TODO(查询所有日志) @param @param type
	 * 0为操作日志 1为异常日志 @param @return 设定文件 @return Query 返回类型 @throws
	 */
	public Query findAllLogbyQuery(String type) {

		Query query = new Query();
		query.addCriteria(Criteria.where("type").is(type));
		query.with(new Sort(new Order(Direction.DESC, "createDate")));
		return query;
	}
	
	
}
