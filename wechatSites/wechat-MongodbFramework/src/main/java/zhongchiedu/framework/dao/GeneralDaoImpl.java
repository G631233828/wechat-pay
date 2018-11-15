package zhongchiedu.framework.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import zhongchiedu.framework.pagination.Pagination;

@Repository("generalDao")
public class GeneralDaoImpl<T> implements GeneralDao<T> {

	@Autowired
	private MongoTemplate mongoTemplate;

	
	public List<T> find(Query query,Class<T> classes) {
		return mongoTemplate.find(query,classes);
	}

	
	public Pagination<T> findPaginationByQuery(Query query, int pageNo,
			int pageSize,Class<T> classes) {
		if (query == null) {
			query = new Query();
		}
		long totalCount = this.mongoTemplate
				.count(query, classes);
		Pagination<T> page = new Pagination<T>(pageNo, pageSize, totalCount);
		query.skip(page.getFirstResult());// skip相当于从那条记录
		query.limit(pageSize);// 从多少条开始,取多少条记录
		List<T> datas = this.find(query,classes);
		page.setDatas(datas);
		return page;
	}

	
	public void insert(T t) {
		this.mongoTemplate.insert(t);
	}

	
	public void save(T t) {
		this.mongoTemplate.save(t);
	}

	
	public void remove(T t) {
		this.mongoTemplate.remove(t);

	}

	
	public void updateFirst(Query query, Update update,Class<T> classes) {
		this.mongoTemplate.updateFirst(query, update, classes);
	}

	
	public T findOneById(String id,Class<T> classes) {

		return this.mongoTemplate.findById(id, classes);
	}

	
	public T findAndModify(Query query, Update update,Class<T> classes) {
		return this.mongoTemplate.findAndModify(query, update,
				classes);
	}

	
	public T findAndRemove(Query query,Class<T> classes) {
		return this.mongoTemplate.findAndRemove(query,classes);
	}

	
	public T findByIdAndCollectionName(String id, String collectionName,Class<T> classes) {
		return this.mongoTemplate.findById(id,classes,
				collectionName);
	}

	
	public T findOneByQuery(Query query,Class<T> classes) {
		return this.mongoTemplate.findOne(query,classes);
	}

	
	public void updateAllByQuery(Query query, Update update,Class<T> classes) {
		
		this.mongoTemplate.updateMulti(query, update, classes);

	}

	
	public Integer findCountByQuery(Query query,Class<T> classes) {
		Long totalCount = this.mongoTemplate
				.count(query, classes);
		return Integer.parseInt(String.valueOf(totalCount));
	}

	
	public boolean exists(Query query, Class<T> classes) {
		
		return this.mongoTemplate.exists(query, classes);
	}


	public void remove(Class<T>  classes, Query query) {
		 this.mongoTemplate.remove(query, classes);
		
	}

}
