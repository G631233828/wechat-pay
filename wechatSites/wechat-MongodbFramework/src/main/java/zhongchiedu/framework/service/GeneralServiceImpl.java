package zhongchiedu.framework.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import zhongchiedu.framework.dao.GeneralDao;
import zhongchiedu.framework.pagination.Pagination;

@Repository("generalService")
public class GeneralServiceImpl<T> implements GeneralService<T> {

	@Autowired
	private GeneralDao<T> generalDao;
	
	public GeneralDao<T> getGeneralDao() {
		return generalDao;
	}

	public void setGeneralDao(GeneralDao<T> generalDao) {
		this.generalDao = generalDao;
	}
	

	public List<T> find(Query query,Class<T> classes) {
		return generalDao.find(query,classes);
	}

	
	public Pagination<T> findPaginationByQuery(Query query, int pageNo,
			int pageSize,Class<T> classes) {
		if (query == null) {
			query = new Query();
		}
		long totalCount = this.generalDao.findCountByQuery(query, classes);
		Pagination<T> page = new Pagination<T>(pageNo, pageSize, totalCount);
		query.skip(page.getFirstResult());// skip相当于从那条记录
		query.limit(pageSize);// 从多少条开始,取多少条记录
		List<T> datas = this.find(query,classes);
		page.setDatas(datas);
		return page;
	}

	
	public void insert(T t) {
		this.generalDao.insert(t);
	}

	
	public void save(T t) {
		this.generalDao.save(t);
	}

	
	public void remove(T t) {
		this.generalDao.remove(t);

	}

	
	public void updateFirst(Query query, Update update,Class<T> classes) {
		this.generalDao.updateFirst(query, update, classes);
	}

	
	public T findOneById(String id,Class<T> classes) {

		return this.generalDao.findOneById(id, classes);
	}

	
	public T findAndModify(Query query, Update update,Class<T> classes) {
		return this.generalDao.findAndModify(query, update,
				classes);
	}

	
	public T findAndRemove(Query query,Class<T> classes) {
		return this.generalDao.findAndRemove(query,classes);
	}

	
	public T findByIdAndCollectionName(String id, String collectionName,Class<T> classes) {
		return this.generalDao.findByIdAndCollectionName(id,
				collectionName,classes);
	}

	
	public T findOneByQuery(Query query,Class<T> classes) {
		return this.generalDao.findOneByQuery(query,classes);
	}

	
	public void updateAllByQuery(Query query, Update update,Class<T> classes) {
		
		this.generalDao.updateAllByQuery(query, update, classes);

	}

	
	public Integer findCountByQuery(Query query,Class<T> classes) {
		return  this.generalDao.findCountByQuery(query, classes);
	}

	
	public boolean exists(Query query, Class<T> classes) {
		
		return this.generalDao.exists(query, classes);
	}

	public void remove(Class<T> classes, Query query) {
		this.generalDao.remove(classes, query);
		
	}


}
