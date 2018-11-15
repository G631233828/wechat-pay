package zhongchiedu.controller.general;


import java.io.PrintWriter;
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

import com.google.gson.Gson;

import zhongchiedu.common.utils.BasicDataResult;
import zhongchiedu.common.utils.Common;
import zhongchiedu.framework.pagination.Pagination;
import zhongchiedu.general.pojo.Resource;
import zhongchiedu.general.pojo.Role;
import zhongchiedu.general.pojo.User;
import zhongchiedu.general.service.Impl.ResourceServiceImpl;
import zhongchiedu.log.annotation.SystemControllerLog;

@Controller
public class ResourceController {

	private static final Logger log = LoggerFactory.getLogger(ResourceController.class);

	@Autowired
	private ResourceServiceImpl resourceService;
	
	
	@GetMapping("resources")
	@RequiresPermissions(value = "resource:list")
	@SystemControllerLog(description = "查询所有用户")
	public String list(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo, Model model,
			@RequestParam(value = "pageSize", defaultValue = "9999") Integer pageSize, HttpSession session) {
		
		// 分页查询数据
		Pagination<Resource> pagination;
		try {
			pagination = resourceService.findPaginationByQuery(new Query(), pageNo, pageSize, Resource.class);
			if (pagination == null)
				pagination = new Pagination<Resource>();

			model.addAttribute("pageList", pagination);
			
		} catch (Exception e) {
			log.info("查询所有资源信息失败——————————》" + e.toString());
			e.printStackTrace();
		}

		return "admin/resource/list";
	}

	
	
	/**
	 * 跳转到添加页面
	 */
	@GetMapping("/resource")
	public String addUserPage(Model model) {
		
		//获取所有的启用的资源目录
		List<Resource> list = this.resourceService.findResourcesByType(0);
		model.addAttribute("resList", list);
		return "admin/resource/add";
	}
	
	
	@PostMapping("/resource")
	@RequiresPermissions(value = "resource:add")
	@SystemControllerLog(description = "添加资源")
	public String addResource(
			@ModelAttribute("resource") Resource resource,
			@RequestParam(value = "parentSubId", defaultValue = "") String parentSubId) {
		if(Common.isNotEmpty(parentSubId)){
			resource.setParentId(parentSubId);
		}
		if(resource.getType() == 0){
			resource.setParentId("0");
		}
		this.resourceService.saveOrUpdateUser(resource);
		return "redirect:resources";
	}
	
	@PutMapping("/resource")
	@RequiresPermissions(value = "resource:edit")
	@SystemControllerLog(description = "修改资源")
	public String editResource(
			@ModelAttribute("resource") Resource resource,
			@RequestParam(value = "parentSubId", defaultValue = "") String parentSubId) {
		if(Common.isNotEmpty(parentSubId)){
			resource.setParentId(parentSubId);
		}
		if(resource.getType() == 0){
			resource.setParentId("0");
		}
		this.resourceService.saveOrUpdateUser(resource);
		return "redirect:resources";
	}
	
	
	
	
	
	/**
	 * 根据选择的目录获取菜单
	 * @param parentId
	 * @return
	 */
	@RequestMapping(value="/getparentmenu",method=RequestMethod.POST,produces="application/json;charset=UTF-8")
	@ResponseBody
	public BasicDataResult getparentmenu(@RequestParam(value = "parentId", defaultValue = "") String parentId){

		List<Resource> list = this.resourceService.findResourceMenu(parentId);
		
		return list!=null?BasicDataResult.build(200, "success",list):BasicDataResult.build(400, "error",null);
	}
	
	
	/**
	 * 跳转到编辑界面
	 * 
	 * @return
	 */
	@GetMapping("/resource{id}")
	public String toeditPage(@PathVariable String id, Model model) {
		
		Resource resource = this.resourceService.findResourceById(id);
		//resource获取到之后需要查看资源
		if(resource!=null){
			int type = resource.getType();
			//如果type是1需要获取所有目录
			List resList = this.resourceService.findResourcesByType(0);
			List resmenu = null;
			//如果type是2需要获取所有目录与菜单
			if(type == 2){
				resmenu = this.resourceService.findResourceMenuByid(resource.getParentId());
				model.addAttribute("resourssubmenuId", resource.getParentId());
				Resource ressup = this.resourceService.findResourceById(resource.getParentId());
				model.addAttribute("resourssupmenuId", ressup.getParentId());
			}
			if(type == 1){
				model.addAttribute("resourssupmenuId", resource.getParentId());
			}
			model.addAttribute("resList", resList);
			model.addAttribute("resmenu", resmenu);
		}
		model.addAttribute("resource", resource);
		

		return "admin/resource/add";

	}
	



	
	@DeleteMapping("/resource/{id}")
	@RequiresPermissions(value = "resource:delete")
	@SystemControllerLog(description = "删除资源")
	public String delete(@PathVariable String id){
		
		String[] strids = id.split(",");
		for (String delids : strids) {
			log.info("删除资源---》" + delids);
			Resource rm = this.resourceService.findOneById(delids, Resource.class);
			this.resourceService.remove(rm);// 删除某个id
		}
		
		return "redirect:/resources";
	}
	

	@RequestMapping(value = "/resource/disable", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public BasicDataResult resourceDisable(@RequestParam(value = "id", defaultValue = "") String id) {
		
	return this.resourceService.resourceDisable(id);
		
	}
	
	



}
