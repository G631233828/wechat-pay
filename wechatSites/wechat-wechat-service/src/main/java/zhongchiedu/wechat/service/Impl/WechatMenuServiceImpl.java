package zhongchiedu.wechat.service.Impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import zhongchiedu.common.utils.BasicDataResult;
import zhongchiedu.common.utils.Common;
import zhongchiedu.common.utils.Types.menuType;
import zhongchiedu.framework.service.GeneralServiceImpl;
import zhongchiedu.pojo.WechatMenu;
import zhongchiedu.school.pojo.School;
import zhongchiedu.service.SchoolService;
import zhongchiedu.wechat.pojo.createMenu.Button;
import zhongchiedu.wechat.pojo.createMenu.Menu;
import zhongchiedu.wechat.util.WeixinUtil;
import zhongchiedu.wechat.util.accessToken.AccessToken;

@Repository
public class WechatMenuServiceImpl extends GeneralServiceImpl<WechatMenu> {
	
	
	@Autowired
	private SchoolService schoolService;
	
	private static final Logger log = LoggerFactory.getLogger(WechatMenuServiceImpl.class);
	/**
	 * 根据学校的session获取当前登录帐号绑定学校的微信菜单
	 * 
	 * @param session
	 * @return
	 */
	public List<WechatMenu> findWeChatMenus() {
			Query query = new Query();
			query.with(new Sort(Sort.Direction.ASC, "sort"));
			
			List<WechatMenu> list = this.find(query, WechatMenu.class);
			return list.size() > 0 ? list : null;
	}

	/**
	 * 添加学校的微信菜单
	 * 
	 * @param wechatmenu
	 * @param session
	 */
	public void saveOrUpdateUser(WechatMenu wechatmenu, HttpSession session) {

		if (Common.isNotEmpty(wechatmenu.getParentId())) {
			String parentId = wechatmenu.getParentId();
			if (Common.isNotEmpty(wechatmenu.getId())) {
				// 修改菜单
				WechatMenu ed = this.findOneById(wechatmenu.getId(), WechatMenu.class);
				BeanUtils.copyProperties(wechatmenu, ed);
				this.save(ed);
			} else {
				int size = this.getParentSize(parentId);
				if (!parentId.equals("0")) {
					// 添加子菜单
					if (size < 5) {
						wechatmenu.setParentId(parentId);
						WechatMenu menu = new WechatMenu();
						wechatmenu.setId(null);
						BeanUtils.copyProperties(wechatmenu, menu);
						this.insert(wechatmenu);
					}
				} else {
					// 添加父菜单
					if (size < 3) {
						wechatmenu.setParentId("0");
						WechatMenu menu = new WechatMenu();
						wechatmenu.setId(null);
						BeanUtils.copyProperties(wechatmenu, menu);
						this.insert(wechatmenu);
					}
				}
			}
		}

	}

	public int getParentSize(String parentId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("parentId").is(parentId));
		List<WechatMenu> list = this.find(query, WechatMenu.class);
		return list.size();

	}

	/**
	 * 通过id查询菜单
	 */
	public BasicDataResult findWechatMenuByid(String id) {

		if (Common.isEmpty(id)) {
			return BasicDataResult.build(400, "获取菜单信息失败", null);
		}

		WechatMenu wechatMenu = this.findOneById(id, WechatMenu.class);
		if (Common.isNotEmpty(wechatMenu)) {
			return BasicDataResult.build(200, "查询成功", wechatMenu);
		}
		return BasicDataResult.build(400, "获取菜单信息失败", null);
	}

	/**
	 * 删除微信菜单
	 * 
	 * @param id
	 */
	public void deleteWechatMenuById(String id) {
		WechatMenu wechatMenu = this.findOneById(id, WechatMenu.class);

		if (Common.isNotEmpty(wechatMenu)) {
			if (wechatMenu.getParentId().equals("0")) {
				Query query = new Query();
				query.addCriteria(Criteria.where("parentId").is(wechatMenu.getId()));
				List<WechatMenu> list = this.find(query, WechatMenu.class);
				if (list.size() > 0) {
					for (WechatMenu menu : list) {
						this.remove(menu);
					}
				}
			}
			this.remove(wechatMenu);
		}

	}
	
	
	
	
	
	

	/**
	 * 发布微信菜单
	 * 
	 * @param session
	 * @return
	 */
	public BasicDataResult release() {
		
		School school = this.schoolService.findOneByQuery(new Query(),School.class);
		
		Menu menu = this.getMenu();
		
		if(Common.isEmpty(menu)){
			return BasicDataResult.build(203, "请先配置学校的微信菜单", null);
		}
		
		String appid = school.getAppid();
		String appsecret = school.getAppSecret();
		if(Common.isEmpty(appid)||Common.isEmpty(appsecret)){
			return BasicDataResult.build(203, "请先到学校管理中配置学校的appId,appSecretId", null);
		}
		AccessToken at =  WeixinUtil.getAccessToken(appid, appsecret);
		if(Common.isEmpty(at)){
			return BasicDataResult.build(400, "认证失败，请检查appid,appSecretId", null);
		}
		int result = WeixinUtil.createMenu(menu, at.getToken());
		
		if(0 == result){
			return BasicDataResult.build(200, "菜单创建成功", null);
		}else{
			return BasicDataResult.build(400, "菜单创建失败，请与管理员联系", null);
		}
	}
	
	
	/**
	 * 获取学校的微信菜单
	 * @param school
	 * @return
	 */
	public Menu getMenu(){
		Menu menu = new Menu();
		// 父按钮
		List listfb = new ArrayList();
	
			Query query = new Query();
			query.addCriteria(Criteria.where("parentId").is("0")).with(new Sort(Sort.Direction.ASC, "sort"));
			List<WechatMenu> listmenu = this.find(query, WechatMenu.class);
			for (WechatMenu wechat : listmenu) {
				Button supB = new Button();
				// 获得所有parentId为0的wechatMenu
				query = new Query();
				query.addCriteria(Criteria.where("parentId").is(wechat.getId()))
						.with(new Sort(Sort.Direction.ASC, "sort"));
				List<WechatMenu> listSubMenu = this.find(query, WechatMenu.class);
				
				if (listSubMenu.size() > 0) {
					// 获取所有子按钮
					List listsub = new ArrayList();
					for(WechatMenu sub:listSubMenu){
						// 父按钮有子菜单
						Button bt = new Button();
						bt.setName(sub.getName());
						bt.setType(sub.getType());
						if(sub.getType().equals(menuType.click)){
							bt.setKey(sub.getKey());
						}else if(sub.getType().equals(menuType.view)){
							bt.setUrl(sub.getUrl());
						}
						listsub.add(bt);
					}
					supB.setName(wechat.getName());
					supB.setSub_button(listsub);
					listfb.add(supB);
				} else {
					// 父按钮没有子菜单
					Button bt = new Button();
					bt.setName(wechat.getName());
					bt.setType(wechat.getType());
					if(wechat.getType().equals(menuType.click)){
						bt.setKey(wechat.getKey());
					}else if(wechat.getType().equals(menuType.view)){
						bt.setUrl(wechat.getUrl());
					}
					listfb.add(bt);
				}
			}
			menu.setButton(listfb);
		
		return menu;
	}
	
	
	
	
	
	
	
	
	
	
	
	

}
