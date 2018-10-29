package zhongchiedu.controller.school;

import java.util.HashSet;
import java.util.List;

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
import org.springframework.web.bind.annotation.RequestParam;

import zhongchiedu.common.utils.Common;
import zhongchiedu.framework.pagination.Pagination;
import zhongchiedu.log.annotation.SystemControllerLog;
import zhongchiedu.school.pojo.Clazz;
import zhongchiedu.school.pojo.Group;
import zhongchiedu.school.pojo.Student;
import zhongchiedu.school.pojo.Teacher;
import zhongchiedu.school.util.ZtreeNode;
import zhongchiedu.service.TeacherService;
import zhongchiedu.service.Impl.GroupServiceImpl;
import zhongchiedu.service.Impl.TeacherServiceImpl;

@Controller
public class GroupController {
	private static final Logger log = LoggerFactory.getLogger(GroupController.class);

	@Autowired
	private GroupServiceImpl groupService;
	
	@Autowired
	private TeacherServiceImpl teacherService;
	
	
	@GetMapping("/groups")
	@RequiresPermissions(value = "group:list")
	@SystemControllerLog(description = "查询所有组")
	public String toedit(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo, Model model,
			@RequestParam(value = "pageSize", defaultValue = "10")Integer pageSize, HttpSession session,
			@ModelAttribute("errorImport") String errorImport) {
	if(Common.isNotEmpty(errorImport)){
			
			model.addAttribute("errorImport", errorImport);
		}
		
		// 分页查询数据
		Pagination<Group> pagination;
		try {
			pagination = this.groupService.findPaginationByQuery(new Query(), pageNo, pageSize, Group.class);
			if (pagination == null)
				pagination = new Pagination<Group>();

			model.addAttribute("pageList", pagination);
			System.out.println(pagination.toString());
		} catch (Exception e) {
			log.info("查询组信息失败——————————》" + e.toString());
			e.printStackTrace();
		}
		return "schools/group/list";
	}
	
	/**
	 * 编辑跳转
	 * @param id
	 * @param model
	 * @return
	 */
	@GetMapping("/group{id}")
	@RequiresPermissions(value = "group:edit")
	@SystemControllerLog(description = "查看")
	public String toeditPage(@PathVariable String id, Model model) {
		List<Teacher> teacherList=this.teacherService.findTeachersByisDisable();
		model.addAttribute("teacherList", teacherList);
		Group group = this.groupService.findOneById(id, Group.class);
		List<ZtreeNode> zNodes=this.groupService.getAllTeacherNode(teacherList, group!=null?group.getTeam():new HashSet<>());
		model.addAttribute("zNodes", zNodes);
		model.addAttribute("group", group);
		return "schools/group/add";
	}
	
	/**
	 * 
	 * @param group			组
	 * @param leaderId		组长id
	 * @param viceId		副组长id
	 * @param ids			组员的字符串
	 * @return
	 */
	@PutMapping("/group")
	@RequiresPermissions(value = "group:edit")
	@SystemControllerLog(description = "修改组")
	public String editgroup(@ModelAttribute("group") Group group,@RequestParam(value = "leaderId", defaultValue = "")String leaderId
			,@RequestParam(value = "viceId", defaultValue = "")String viceId,@RequestParam(value = "ids", defaultValue = "")String ids) {
		this.groupService.saveorupdate(group,leaderId,viceId,ids);
		return "redirect:groups";
	}
	
	/**
	 * 
	 * @param group			组
	 * @param leaderId		组长id
	 * @param viceId		副组长id
	 * @param ids			组员的字符串
	 * @return
	 */
	@PostMapping("/group")
	@RequiresPermissions(value = "group:add")
	@SystemControllerLog(description = "添加组")
	public String addgroup(@ModelAttribute("group") Group group,@RequestParam(value = "leaderId", defaultValue = "")String leaderId
			,@RequestParam(value = "viceId", defaultValue = "")String viceId,@RequestParam(value = "ids", defaultValue = "")String ids) {
		this.groupService.saveorupdate(group,leaderId,viceId,ids);
		return "redirect:groups";
	}
	
	/**
	 * 删除
	 * @param id
	 * @return
	 */
	@DeleteMapping("/group/{id}")
	@RequiresPermissions(value = "group:delete")
	@SystemControllerLog(description = "删除组")
	public String delete(@PathVariable String id) {
		String[] strids = id.split(",");
		for (String delids : strids) {
			log.info("删除班级---》" + delids);
			 Group group= this.groupService.findOneById(delids, Group.class);
			this.groupService.remove(group);// 删除某个id
		}
		return "redirect:/groups";
	}
	
	
}
