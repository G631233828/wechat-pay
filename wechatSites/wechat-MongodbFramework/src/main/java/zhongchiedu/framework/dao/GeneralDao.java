package zhongchiedu.framework.dao;

import java.util.List;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import zhongchiedu.framework.pagination.Pagination;


public interface GeneralDao<T> {

	/**
	 * 通过ID查询当前实体
	 * 
	 * @param id
	 * @return T
	 */
	public T findOneById(String id,Class<T> classes);

	/**
	 * 通过查询查询并分页
	 * 
	 * @param query
	 *            条件
	 * @param pageNo
	 *            起始条数
	 * @param pageSize
	 *            查询多少条
	 * @return 返回分页后Pagination实体
	 */
	public Pagination<T> findPaginationByQuery(Query query, int pageNo,
			int pageSize,Class<T> classes);

	/**
	 * 插入
	 * 
	 * @param t
	 */
	public void insert(T t);

	/**
	 * 保存
	 * 
	 * @param t
	 */
	public void save(T t);

	/**
	 * 查询并且修改记录
	 * 
	 * @param query
	 * @param update
	 * @return 返回修改后的实体
	 */
	public T findAndModify(Query query, Update update,Class<T> classes);

	/**
	 * 查询并删除当前记录
	 * 
	 * @param query
	 * @return 返回删除的实体
	 */
	public T findAndRemove(Query query,Class<T> classes);

	/**
	 * 查询删除
	 * 
	 * @param T当前删除的对象
	 */
	public void remove(T t);
    
	public void remove(Class<T> classes,Query query);
	
	/**
	 * 修改查询后的第一条记录
	 * @param query
	 * @param update
	 */
	public void updateFirst(Query query, Update update,Class<T> classes);

	/**
	 * 通过条件查询所有的记录
	 * @param query
	 * @return
	 */
	public List<T> find(Query query,Class<T> classes);

	/**
	 * 通过ID获取记录,并且指定了集合名
	 * @param id
	 * @param collectionName
	 * @return
	 */
	public T findByIdAndCollectionName(String id, String collectionName,Class<T> classes);
	
	
	/**
	 * 通过条件find当前T
	 */
	public T findOneByQuery(Query query,Class<T> classes);
	
	/**
	 * 通过条件修改所有的记录
	 * @param query
	 * @param update
	 */
	public void updateAllByQuery(Query query,Update update,Class<T> classes);
	
	/**
	 * 查询总条数
	 * @param query
	 * @return
	 */
	public Integer findCountByQuery(Query query,Class<T> classes);
	
	public boolean exists(Query query ,Class<T> classes);


}
