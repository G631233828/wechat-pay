package zhongchiedu.controller.school;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import zhongchiedu.common.utils.BasicDataResult;
import zhongchiedu.common.utils.Common;
import zhongchiedu.framework.pagination.Pagination;
import zhongchiedu.log.annotation.SystemControllerLog;
import zhongchiedu.school.pojo.Activitys;
import zhongchiedu.school.pojo.Sites;
import zhongchiedu.service.ActivitysService;
import zhongchiedu.service.SitesService;

@Controller
public class SitesController {

	private static final Logger log = LoggerFactory.getLogger(SitesController.class);

	@Autowired
	private SitesService sitesService;

	private @Autowired ActivitysService activitysService;


	@GetMapping("/sitess")
	@RequiresPermissions(value = "sites:list")
	@SystemControllerLog(description = "查询出发站点")
	public String list(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo, Model model,
			@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, HttpSession session) {

		// 分页查询数据
		Pagination<Sites> pagination;
		try {
			Query query = new Query();
			List<Order> listsort = new ArrayList();
			listsort.add(new Order(Direction.DESC, "createDate"));
			query.with(new Sort(listsort));
			pagination = this.sitesService.findPaginationByQuery(query, pageNo, pageSize, Sites.class);
			if (pagination == null)
				pagination = new Pagination<Sites>();

			model.addAttribute("pageList", pagination);
		} catch (Exception e) {
			log.info("查询站点信息失败——————————》" + e.toString());
			e.printStackTrace();
		}

		return "schools/sites/list";
	}

	@PostMapping("/sites")
	@RequiresPermissions(value = "sites:add")
	@SystemControllerLog(description = "添加站点")
	public String addclazz(HttpServletRequest request, @Valid Sites sites) {

		this.sitesService.SaveOrUpdateSites(sites);

		return "redirect:sitess";
	}

	/**
	 * restful请求put
	 * 
	 * @param school
	 * @return
	 */

	@PutMapping("/sites")
	@RequiresPermissions(value = "sites:edit")
	@SystemControllerLog(description = "修改站点")
	public String editUser(@Valid Sites sites) {
		this.sitesService.SaveOrUpdateSites(sites);
		return "redirect:sitess";
	}

	/**
	 * 跳转到编辑界面
	 * 
	 * @return
	 */
	@GetMapping("/sites{id}")
	@RequiresPermissions(value = "sites:edit")
	@SystemControllerLog(description = "查看站点")
	public String toeditPage(@PathVariable String id, Model model) {
		Sites sites = this.sitesService.findOneById(id, Sites.class);
		model.addAttribute("sites", sites);

		// 获取所有活动
		List<Activitys> list = this.activitysService.findActivitysByisDisable();

		model.addAttribute("activitys", list);
		return "schools/sites/add";

	}

	@DeleteMapping("/sites/{id}")
	@RequiresPermissions(value = "sites:delete")
	@SystemControllerLog(description = "删除站点")
	public String delete(@PathVariable String id) {
		String[] strids = id.split(",");
		for (String delids : strids) {
			log.info("删除---》" + delids);
			Sites sites = this.sitesService.findOneById(delids, Sites.class);
			this.sitesService.remove(sites);// 删除某个id
		}
		return "redirect:/sitess";
	}

	@RequestMapping(value = "/sites/disable", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public BasicDataResult clazzDisable(@RequestParam(value = "id", defaultValue = "") String id) {

		return this.sitesService.sitesDisable(id);

	}
	
	
	

	
	
	
	
	
	
	
	
	
	

}
