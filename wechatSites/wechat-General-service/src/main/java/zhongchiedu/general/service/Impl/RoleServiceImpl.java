package zhongchiedu.general.service.Impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import zhongchiedu.common.utils.BasicDataResult;
import zhongchiedu.common.utils.Common;
import zhongchiedu.framework.service.GeneralServiceImpl;
import zhongchiedu.general.dao.RoleDao;
import zhongchiedu.general.pojo.Resource;
import zhongchiedu.general.pojo.Role;
import zhongchiedu.general.pojo.User;
import zhongchiedu.general.service.RoleService;

@Service
public class RoleServiceImpl extends GeneralServiceImpl<Role> implements RoleService {

	@Autowired
	private RoleDao roleDao;
	@Autowired
	private ResourceServiceImpl resourceService;

	/**
	 * 根据d查询角色
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Role findRoleById(String id) {
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(id));
		// User user= this.userDao.findOneById(id);
		Role o = this.roleDao.findOneByQuery(query, Role.class);
		return o!=null?o:null;
	}

	/**
	 * 根据角色名称查询是否存在重复
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Role findRoleByRoleName(String roleName){
		Query query = new Query();
		query.addCriteria(Criteria.where("roleName").is(roleName));
		// User user= this.userDao.findOneById(id);
		Role o = this.roleDao.findOneByQuery(query, Role.class);
		
		return o!=null?o:null;
	}

	/***
	 * 获取所有使用状态下的角色
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<Role> findAllRoleByisDisable() {
		Query query = new Query();
		query.addCriteria(Criteria.where("isDisable").is(false));
		List<Role> listRole = this.roleDao.find(query, Role.class);
		return listRole != null ? listRole : null;

	}

	/**
	 * 获取所有角色
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<Role> findAllRole() throws Exception {
		List<Role> listRole = this.roleDao.find(new Query(), Role.class);
		if (listRole != null)
			return listRole;
		else
			return null;
	}

	public void saveOrUpdateRole(Role role) {

		// 判断用户信息不为空，并且用户id不为空
		if (role != null) {
			if (Common.isNotEmpty(role.getId())) {
				// 执行修改操作
				Role ed = this.findRoleById(role.getId());
				if (ed == null)
					ed = new Role();
				ed.setRoleKey(role.getRoleKey());
				ed.setRoleName(role.getRoleName());
				ed.setIsDisable(role.getIsDisable());
				ed.setDescription(role.getDescription());
				this.save(ed);
			} else {
				Role ad = new Role();
				ad.setRoleKey(role.getRoleKey());
				ad.setRoleName(role.getRoleName());
				ad.setIsDisable(role.getIsDisable());
				ad.setDescription(role.getDescription());
				this.insert(ad);
			}
		}
	}

	public BasicDataResult author(String id, String checkallPermission) {
		try {
			List<Resource> listres = new ArrayList<Resource>();
			Role role = this.findRoleById(id);
			
			String[] strids = checkallPermission.split(",");
			
			TreeSet<Object> t = Common.toRepeat(strids);
			
			Iterator i = t.iterator();
			
			while(i.hasNext()){
				// 通过资源的id获取资源
				Resource res = this.resourceService.findResourceById(i.next().toString());
				listres.add(res);
			}
	/*		
			for (String resids : strids) {
				// 通过资源的id获取资源
				Resource res = this.resourceService.findResourceById(resids);
				listres.add(res);
			}*/
			role.setResource(new ArrayList<Resource>());
			this.save(role);
			role.setResource(listres);
			this.save(role);
			return BasicDataResult.build(200, "权限分配成功", null);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return BasicDataResult.build(400, "权限分配失败", null);
		}

	}

	/**
	 * 查询已有权限
	 * @param id
	 * @return
	 */
	public BasicDataResult getAuthor(String id) {
		
		if(Common.isEmpty(id)){
			
			return BasicDataResult.build(400, "未能找到角色的相关信息", null);
		}
	
		Role role = this.findRoleById(id);
		
		if(role!=null){
			
			List<Resource> list = role.getResource();
			return BasicDataResult.build(200, "获取权限成功",list);
			
		}
		return BasicDataResult.build(200, "为设置权限", null);
		
	}

	public BasicDataResult ajaxgetRepletes(String roleName) {

		if(Common.isNotEmpty(roleName)){
			Role role = this.findRoleByRoleName(roleName);
			
			if(role!=null){
				return BasicDataResult.build(206,"当前角色名称已经存在", null);
			}
			
			return BasicDataResult.ok();
		}
		
		return BasicDataResult.build(400,"未能获取到请求的信息", null);
		
		
	}

	public BasicDataResult roleDisable(String id) {
		
		if(Common.isEmpty(id)){
			return BasicDataResult.build(400, "无法禁用，请求出现问题，请刷新界面!", null);
		}
		Role role = this.findRoleById(id);
		if(role == null){
			return BasicDataResult.build(400, "无法获取到资源信息，该角色可能已经被删除", null);
		}
		
		role.setIsDisable(role.getIsDisable().equals(true)?false:true);
		this.save(role);
		
		return BasicDataResult.build(200, role.getIsDisable().equals(true)?"角色禁用成功":"角色启用成功",role.getIsDisable());
		
	}

}
