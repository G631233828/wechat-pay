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
import org.springframework.validation.BindingResult;
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

import zhongchiedu.common.utils.BasicDataResult;
import zhongchiedu.common.utils.Common;
import zhongchiedu.framework.pagination.Pagination;
import zhongchiedu.log.annotation.SystemControllerLog;
import zhongchiedu.school.pojo.Clazz;
import zhongchiedu.school.pojo.Teacher;
import zhongchiedu.service.ClazzService;
import zhongchiedu.service.TeacherService;

@Controller
public class ClazzController {

	private static final Logger log = LoggerFactory.getLogger(ClazzController.class);

	@Autowired
	private ClazzService clazzService;

	@Autowired
	private TeacherService teacherService;

	@GetMapping("/clazzs")
	@RequiresPermissions(value = "clazz:list")
	@SystemControllerLog(description = "查询所有班级")
	public String list(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo, Model model,
			@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, HttpSession session) {

		// 分页查询数据
		Pagination<Clazz> pagination;
		try {
			Query query = new Query();
			List<Order> listsort = new ArrayList();
			listsort.add(new Order(Direction.DESC, "clazzYear"));
			listsort.add(new Order(Direction.ASC, "clazzNum"));
			query.with(new Sort(listsort));
			pagination = this.clazzService.findPaginationByQuery(query, pageNo, pageSize, Clazz.class);
			if (pagination == null)
				pagination = new Pagination<Clazz>();

			model.addAttribute("pageList", pagination);
		} catch (Exception e) {
			log.info("查询班级信息失败——————————》" + e.toString());
			e.printStackTrace();
		}

		return "schools/clazz/list";
	}

	@PostMapping("/clazz")
	@RequiresPermissions(value = "clazz:add")
	@SystemControllerLog(description = "添加班级")
	public String addclazz(HttpServletRequest request, @Valid Clazz clazz) {

		this.clazzService.SaveOrUpdateClazz(clazz);

		return "redirect:clazzs";
	}

	/**
	 * restful请求put 修改学校
	 * 
	 * @param school
	 * @return
	 */

	@PutMapping("/clazz")
	@RequiresPermissions(value = "clazz:edit")
	@SystemControllerLog(description = "修改班级")
	public String editUser(@Valid Clazz clazz) {
		this.clazzService.SaveOrUpdateClazz(clazz);
		return "redirect:clazzs";
	}

	/**
	 * 跳转到编辑界面
	 * 
	 * @return
	 */
	@GetMapping("/clazz{id}")
	@RequiresPermissions(value = "clazz:edit")
	@SystemControllerLog(description = "查看班级")
	public String toeditPage(@PathVariable String id, Model model) {

		Clazz clazz = this.clazzService.findOneById(id, Clazz.class);
		model.addAttribute("clazz", clazz);
		
		
		// 获取到所有的已经绑定班主任的班级信息

		List<Clazz> listclazz = this.clazzService.findClazzsByisDisable();
		List<Teacher> list = new ArrayList<>();
		for (Clazz c : listclazz) {
			if (Common.isNotEmpty(c.getHeadMaster())) {
				list.add(c.getHeadMaster());
			}
		}
		List<Teacher> teacherList = this.teacherService.findTeachersByisDisable();
		
		List<Teacher> lists = new ArrayList<>();
		if(Common.isNotEmpty(clazz)){
			lists.add(clazz.getHeadMaster());
		}
		
		for(Teacher t1:teacherList){
			boolean flag = false;
			for(Teacher t2:list){
				if(t1.getContactsmobile().equals(t2.getContactsmobile())){
					flag = true;
				}		
			}
			
			if(!flag){
				lists.add(t1);
			}
			
		}
		
		
		model.addAttribute("teacherList", lists);

		return "schools/clazz/add";

	}

	@DeleteMapping("/clazz/{id}")
	@RequiresPermissions(value = "clazz:delete")
	@SystemControllerLog(description = "删除班级")
	public String delete(@PathVariable String id) {
		String[] strids = id.split(",");
		for (String delids : strids) {
			log.info("删除班级---》" + delids);
			Clazz clazz = this.clazzService.findOneById(delids, Clazz.class);
			this.clazzService.remove(clazz);// 删除某个id
		}
		return "redirect:/clazzs";
	}

	@RequestMapping(value = "/clazz/disable", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public BasicDataResult clazzDisable(@RequestParam(value = "id", defaultValue = "") String id) {

		return this.clazzService.clazzDisable(id);

	}

	@GetMapping("/clazz/batch")
	@RequiresPermissions(value = "clazz:batch")
	@SystemControllerLog(description = "批量生成班级")
	public String batch(int clazzYear, int clazzNum) {
		this.clazzService.autoBatchClazz(clazzYear, clazzNum);
		return "redirect:/clazzs";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
