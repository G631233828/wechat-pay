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
import zhongchiedu.framework.pagination.Pagination;
import zhongchiedu.log.annotation.SystemControllerLog;
import zhongchiedu.school.pojo.Activitys;
import zhongchiedu.service.ActivitysService;

@Controller
public class ActivitysController {

	private static final Logger log = LoggerFactory.getLogger(ActivitysController.class);

	@Autowired
	private ActivitysService activitysService;

	@GetMapping("/activityss")
	@RequiresPermissions(value = "activitys:list")
	@SystemControllerLog(description = "查询所有活动")
	public String list(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo, Model model,
			@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, HttpSession session) {

		// 分页查询数据
		Pagination<Activitys> pagination;
		try {
			Query query = new Query();
			List<Order> listsort = new ArrayList();
			listsort.add(new Order(Direction.DESC, "createDate"));
			query.with(new Sort(listsort));
			pagination = this.activitysService.findPaginationByQuery(query, pageNo, pageSize, Activitys.class);
			if (pagination == null)
				pagination = new Pagination<Activitys>();

			model.addAttribute("pageList", pagination);
		} catch (Exception e) {
			log.info("查询活动信息失败——————————》" + e.toString());
			e.printStackTrace();
		}

		return "schools/activitys/list";
	}

	@PostMapping("/activitys")
	@RequiresPermissions(value = "activitys:add")
	@SystemControllerLog(description = "添加活动")
	public String addclazz(HttpServletRequest request, @Valid Activitys activitys) {

		this.activitysService.SaveOrUpdateActivitys(activitys);

		return "redirect:activityss";
	}

	/**
	 * restful请求put
	 * 
	 * @param school
	 * @return
	 */

	@PutMapping("/activitys")
	@RequiresPermissions(value = "activitys:edit")
	@SystemControllerLog(description = "修改活动")
	public String editUser(@Valid Activitys activitys) {
		this.activitysService.SaveOrUpdateActivitys(activitys);
		return "redirect:activityss";
	}

	/**
	 * 跳转到编辑界面
	 * 
	 * @return
	 */
	@GetMapping("/activitys{id}")
	@RequiresPermissions(value = "activitys:edit")
	@SystemControllerLog(description = "查看活动")
	public String toeditPage(@PathVariable String id, Model model) {
		Activitys activitys = this.activitysService.findOneById(id, Activitys.class);
		model.addAttribute("activitys", activitys);

		return "schools/activitys/add";

	}

	@DeleteMapping("/activitys/{id}")
	@RequiresPermissions(value = "activitys:delete")
	@SystemControllerLog(description = "删除活动")
	public String delete(@PathVariable String id) {
		String[] strids = id.split(",");
		for (String delids : strids) {
			log.info("删除---》" + delids);
			Activitys activitys = this.activitysService.findOneById(delids, Activitys.class);
			this.activitysService.remove(activitys);// 删除某个id
		}
		return "redirect:/activityss";
	}

	@RequestMapping(value = "/activitys/disable", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public BasicDataResult clazzDisable(@RequestParam(value = "id", defaultValue = "") String id) {

		return this.activitysService.activitysDisable(id);

	}

}
