package zhongchiedu.controller.school;


import javax.servlet.http.HttpServletRequest;
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

import zhongchiedu.common.utils.BasicDataResult;
import zhongchiedu.framework.pagination.Pagination;
import zhongchiedu.log.annotation.SystemControllerLog;
import zhongchiedu.school.pojo.School;
import zhongchiedu.service.SchoolService;


@Controller
public class SchoolController  {

	private static final Logger log = LoggerFactory.getLogger(SchoolController.class);
	
	
	@Autowired
	private SchoolService schoolService; 
	
	
	@GetMapping("schools")
	@RequiresPermissions(value = "school:list")
	@SystemControllerLog(description = "查询所有学校")
	public String list(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo, Model model,
			@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, HttpSession session) {
		// 分页查询数据
		Pagination<School> pagination;
		try {
			pagination = this.schoolService.findPaginationByQuery(new Query(), pageNo, pageSize, School.class);
			if (pagination == null)
				pagination = new Pagination<School>();

			model.addAttribute("pageList", pagination);
			System.out.println(pagination.toString());
		} catch (Exception e) {
			log.info("查询学校信息失败——————————》" + e.toString());
			e.printStackTrace();
		}

		return "schools/school/list";
	}
	
	@PostMapping("/school")
	@RequiresPermissions(value = "school:add")
	@SystemControllerLog(description = "添加学校")
	public String addschool(HttpServletRequest request, @ModelAttribute("school") School school) {
		this.schoolService.SaveOrUpdateSchool(school);
		return "redirect:schools";
	}
	
	
	/**
	 * restful请求put
	 * 修改学校
	 * @param school
	 * @return
	 */
	
	@PutMapping("/school")
	@RequiresPermissions(value = "school:edit")
	@SystemControllerLog(description = "修改学校")
	public String editUser(@ModelAttribute("school") School school){
		this.schoolService.SaveOrUpdateSchool(school);
		return "redirect:schools";
	}
	
	
	
	
	
	/**
	 * 跳转到编辑界面
	 * 
	 * @return
	 */
	@GetMapping("/school{id}")
	@RequiresPermissions(value = "school:edit")
	@SystemControllerLog(description = "查看学校")
	public String toeditPage(@PathVariable String id, Model model) {

		School school = this.schoolService.findOneById(id, School.class);

		model.addAttribute("school", school);

		return "schools/school/add";

	}

	
	
	@DeleteMapping("/school/{id}")
	@RequiresPermissions(value = "school:delete")
	@SystemControllerLog(description = "删除学校")
	public String delete(@PathVariable String id){
		String[] strids = id.split(",");
		for (String delids : strids) {
			log.info("删除学校---》" + delids);
			School school = this.schoolService.findOneById(delids,School.class);
			this.schoolService.remove(school);// 删除某个id
		}
		return "redirect:/schools";
	}
	
	
	
/*	@RequestMapping(value = "/school/disable", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public BasicDataResult schoolDisable(@RequestParam(value = "id", defaultValue = "") String id) {
		
	return this.schoolService.schoolDisable(id);
		
	}
	*/
	
	
}
