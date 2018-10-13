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
import zhongchiedu.school.pojo.Clazz;
import zhongchiedu.school.pojo.Teacher;
import zhongchiedu.service.ClazzService;
import zhongchiedu.service.TeacherService;

@Controller
public class TeacherController {

	private static final Logger log = LoggerFactory.getLogger(TeacherController.class);

	@Autowired
	private TeacherService teacherService;

	@GetMapping("/teachers")
	@RequiresPermissions(value = "teacher:list")
	@SystemControllerLog(description = "查询所有教师")
	public String list(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo, Model model,
			@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, HttpSession session) {

		// 分页查询数据
		Pagination<Teacher> pagination;
		try {
			pagination = this.teacherService.findPaginationByQuery(new Query(), pageNo, pageSize, Teacher.class);
			if (pagination == null)
				pagination = new Pagination<Teacher>();

			model.addAttribute("pageList", pagination);
			System.out.println(pagination.toString());
		} catch (Exception e) {
			log.info("查询教师信息失败——————————》" + e.toString());
			e.printStackTrace();
		}

		return "schools/teacher/list";
	}

	@PostMapping("/teacher")
	@RequiresPermissions(value = "teacher:add")
	@SystemControllerLog(description = "添加老师")
	public String addclazz(HttpServletRequest request, @ModelAttribute("teacher") Teacher teacher) {

		this.teacherService.SaveOrUpdateTeacher(teacher);

		return "redirect:teachers";
	}

	/**
	 * restful请求put 修改老师
	 * 
	 * @param school
	 * @return
	 */

	@PutMapping("/teacher")
	@RequiresPermissions(value = "teacher:edit")
	@SystemControllerLog(description = "修改老师")
	public String editUser(@ModelAttribute("teacher") Teacher teacher) {

		this.teacherService.SaveOrUpdateTeacher(teacher);
		return "redirect:teachers";
	}

	/**
	 * 跳转到编辑界面
	 * 
	 * @return
	 */
	@GetMapping("/teacher{id}")
	@RequiresPermissions(value = "teacher:edit")
	@SystemControllerLog(description = "查看老师")
	public String toeditPage(@PathVariable String id, Model model) {

		Teacher teacher = this.teacherService.findOneById(id, Teacher.class);
		
		model.addAttribute("teacher", teacher);

		return "schools/teacher/add";

	}

	@DeleteMapping("/teacher/{id}")
	@RequiresPermissions(value = "teacher:delete")
	@SystemControllerLog(description = "删除教师")
	public String delete(@PathVariable String id) {
		String[] strids = id.split(",");
		for (String delids : strids) {
			log.info("删除教师---》" + delids);
			Teacher teacher = this.teacherService.findOneById(delids, Teacher.class);
			this.teacherService.remove(teacher);// 删除某个id
		}
		return "redirect:/teachers";
	}

	@RequestMapping(value = "/teacher/disable", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public BasicDataResult teacherDisable(@RequestParam(value = "id", defaultValue = "") String id) {

		return this.teacherService.teacherDisable(id);

	}


}
