package zhongchiedu.controller.general;


import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import zhongchiedu.common.utils.BasicDataResult;
import zhongchiedu.framework.pagination.Pagination;
import zhongchiedu.general.pojo.Resource;
import zhongchiedu.general.pojo.Role;
import zhongchiedu.general.pojo.User;
import zhongchiedu.general.service.Impl.ResourceServiceImpl;
import zhongchiedu.general.service.Impl.RoleServiceImpl;
import zhongchiedu.log.annotation.SystemControllerLog;

@Controller
public class RoleController {

	private static final Logger log = LoggerFactory.getLogger(RoleController.class);

	@Autowired
	private RoleServiceImpl roleService;

	@Autowired
	private ResourceServiceImpl resourceService;

	@GetMapping("roles")
	@RequiresPermissions(value = "role:list")
	@SystemControllerLog(description = "查询所有用户")
	public String list(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo, Model model,
			@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, HttpSession session) {
		// 分页查询数据
		Pagination<Role> pagination;
		pagination = this.roleService.findPaginationByQuery(new Query(), pageNo, pageSize, Role.class);
		
		if (pagination == null)
			pagination = new Pagination<Role>();

		model.addAttribute("pageList", pagination);

		List<Resource> listResource = this.resourceService.findAllResourceByUsed();

		model.addAttribute("listResource", listResource);

		return "admin/role/list";
	}
	
	
	/**
	 * 跳转到添加页面
	 */
	@GetMapping("/role")
	public String addRolePage(Model model) {
		return "admin/role/add";
	}

	
	
	@PostMapping("/role")
	@RequiresPermissions(value = "role:add")
	@SystemControllerLog(description = "添加角色")
	public String addRole(HttpServletRequest request, @ModelAttribute("role") Role role) {
		this.roleService.saveOrUpdateRole(role);
		return "redirect:roles";
	}
	
	
	@PutMapping("/role")
	@RequiresPermissions(value = "role:edit")
	@SystemControllerLog(description = "修改角色")
	public String editRole(HttpServletRequest request, @ModelAttribute("role") Role role) {
		this.roleService.saveOrUpdateRole(role);
		return "redirect:roles";
	}
	
	
	
	/**
	 * 跳转到编辑界面
	 * 
	 * @return
	 */
	@GetMapping("/role{id}")
	public String toeditPage(@PathVariable String id, Model model) {


		Role role = this.roleService.findOneById(id, Role.class);

		model.addAttribute("role", role);

		return "admin/role/add";

	}
	
	
	@DeleteMapping("/role/{id}")
	@RequiresPermissions(value = "role:delete")
	@SystemControllerLog(description = "删除角色")
	public String delete(@PathVariable String id){
		String[] strids = id.split(",");
		for (String delids : strids) {
			log.info("删除角色---》" + delids);
			Role rm = this.roleService.findOneById(delids,Role.class);
			this.roleService.remove(rm);// 删除某个id
		}
		return "redirect:/roles";
	}
	
	@ResponseBody
	@RequestMapping("/author")
	public BasicDataResult author(HttpSession session, @RequestParam(defaultValue = "", value = "id") String id,
			@RequestParam(value = "checkallPermission", defaultValue = "") String checkallPermission) {
		 
		BasicDataResult result = this.roleService.author(id,checkallPermission);
		
		return result;
	}
	
	
	
	@ResponseBody
	@RequestMapping("/getAuthor")
	public BasicDataResult getAuthor(@RequestParam(defaultValue = "", value = "id") String id){
		
		BasicDataResult result = this.roleService.getAuthor(id);
		return result;
		
	}
	
	

	/**
	 * 通过ajax获取是否存在重复账号的信息
	 * 
	 * @param printWriter
	 * @param session
	 * @param response
	 */
	@RequestMapping(value = "/role/ajaxgetRepletes", method = RequestMethod.POST)
	@ResponseBody
	public BasicDataResult ajaxgetRepletes(@RequestParam(value = "roleName", defaultValue = "") String roleName) {
			
		return	this.roleService.ajaxgetRepletes(roleName);
		
	}

	@RequestMapping(value = "/role/disable", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public BasicDataResult resourceDisable(@RequestParam(value = "id", defaultValue = "") String id) {
		
	return this.roleService.roleDisable(id);
		
	}
	
	
	
	

}
