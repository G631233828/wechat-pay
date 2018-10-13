package zhongchiedu.general.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import zhongchiedu.common.utils.BasicDataResult;
import zhongchiedu.common.utils.Common;
import zhongchiedu.framework.service.GeneralServiceImpl;
import zhongchiedu.general.dao.ResourceDao;
import zhongchiedu.general.pojo.Resource;
import zhongchiedu.general.pojo.User;
import zhongchiedu.general.service.ResourceService;

@Service
public class ResourceServiceImpl extends GeneralServiceImpl<Resource> implements ResourceService{
	
	@Autowired
	private ResourceDao resourceDao;
	
	/**
	 * 通过用户id查询所有的资源
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public List<Resource> findlistResource(String userId) throws Exception {
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(userId));
		return this.resourceDao.find(query,Resource.class);

	}

	/**
	 * 根据资源Id查询资源
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Resource findResourceById(String id){
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(id));
		Resource rs= this.resourceDao.findOneByQuery(query,Resource.class);
		return rs!=null?rs:null;
	}
	
	
	
	
	/**
	 * 根据资源Id查询资源
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Resource findResourceByResUrl(String url) throws Exception{
		Query query = new Query();
		query.addCriteria(Criteria.where("resUrl").is(url));
		//User user= this.userDao.findOneById(id);
		Resource rs= this.resourceDao.findOneByQuery(query,Resource.class);
		if(rs!=null)
			return rs;
		else 
			return null;
	}
	/**
	 * 查询所有1级菜单
	 * @return
	 * @throws Exception
	 */
	public List<Resource> findParentResource() throws Exception{
		Query query= new Query();
		query.addCriteria(Criteria.where("parentId").is("0"));
		query.addCriteria(Criteria.where("isDisable").is(false));
		List<Resource> list = this.resourceDao.find(query,Resource.class);
		if(list!=null)
			return list;
		else
			return null;
	}
	/**
	 * 查询所有2级菜单
	 * @return
	 * @throws Exception
	 */
	public List<Resource> findResourceMenu(String parentId){
		Query query= new Query();
		query.addCriteria(Criteria.where("parentId").is(parentId));
		query.addCriteria(Criteria.where("isDisable").is(false));
		List<Resource> list = this.resourceDao.find(query,Resource.class);
		return list.size()>0?list:null;
	}
	
	/**
	 * 查询所有2级菜单
	 * @return
	 * @throws Exception
	 */
	public List<Resource> findResourceMenuByid(String parentId){
		Query query= new Query();
		query.addCriteria(Criteria.where("_id").is(parentId));
		query.addCriteria(Criteria.where("isDisable").is(false));
		List<Resource> list = this.resourceDao.find(query,Resource.class);
		return list.size()>0?list:null;
	}
	

	/**
	 * 获取所有的资源
	 * @return
	 * @throws Exception
	 */
	public List<Resource> findAllResource(){
	    List<Resource> listRes = this.find(new Query(),Resource.class);
	    if(listRes != null)
		return listRes;
	    else
		return null;
	}

	
	/**
	 * 获取所有非禁用状态的resource
	 * @return
	 */
	public List<Resource> findAllResourceByUsed(){
		Query query = new Query();
		query.addCriteria(Criteria.where("isDisable").is(false));
		 List<Resource> listRes = this.find(query,Resource.class);
		 return listRes.size()>0?listRes:null;
	}
	
	
	
	/**
	 * 查询根据菜单的类型来进行查询
	 * @param type
	 * @return
	 */
	public List<Resource> findResourcesByType(int type){
		Query query = new Query();
		query.addCriteria(Criteria.where("isDisable").is(false))
		.addCriteria(Criteria.where("type").is(type));
		List<Resource> list = this.find(query, Resource.class);
		return list.size()>0?list:null;
	}
	



	/**
	 * 资源添加修改
	 * @param resource
	 */
	public void saveOrUpdateUser(Resource rs) {
		
		if(Common.isNotEmpty(rs)){
			if(Common.isNotEmpty(rs.getId())){
				// 执行修改操作
				Resource ed = this.findResourceById(rs.getId());
				if (ed == null)
					ed = new Resource();
				ed.setDescription(rs.getDescription());
				ed.setIcon(rs.getIcon());
				ed.setIsDisable(rs.getIsDisable());
				ed.setName(rs.getName());
				ed.setParentId(rs.getParentId());
				ed.setResKey(rs.getResKey());
				ed.setResUrl(rs.getResUrl());
				ed.setType(rs.getType());
				this.save(ed);
			}else{
				Resource ad = new Resource();
				ad.setDescription(rs.getDescription());
				ad.setIcon(rs.getIcon());
				ad.setIsDisable(rs.getIsDisable());
				ad.setName(rs.getName());
				ad.setParentId(rs.getParentId());
				ad.setResKey(rs.getResKey());
				ad.setResUrl(rs.getResUrl());
				ad.setType(rs.getType());
				/*this.save(ad);*/
				// 执行添加操作
				this.insert(ad);
			}
		}
	}

	/**
	 * 禁用启用
	 * @param id
	 * @return
	 */
	public BasicDataResult resourceDisable(String id) {
		
		if(Common.isEmpty(id)){
			return BasicDataResult.build(400, "无法禁用，请求出现问题，请刷新界面!", null);
		}
		Resource resource = this.findResourceById(id);
		if(resource == null){
			return BasicDataResult.build(400, "无法获取到资源信息，该资源可能已经被删除", null);
		}
		
		resource.setIsDisable(resource.getIsDisable().equals(true)?false:true);
		this.save(resource);
		
		return BasicDataResult.build(200, resource.getIsDisable().equals(true)?"资源禁用成功":"资源启用成功",resource.getIsDisable());
		
	}
	
	
}
