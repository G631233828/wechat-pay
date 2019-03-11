package zhongchiedu.controller.school;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import zhongchiedu.common.utils.BasicDataResult;
import zhongchiedu.common.utils.Common;
import zhongchiedu.common.utils.FileOperateUtil;
import zhongchiedu.framework.pagination.Pagination;
import zhongchiedu.log.annotation.SystemControllerLog;
import zhongchiedu.school.pojo.Teacher;
import zhongchiedu.service.TeacherService;
import zhongchiedu.service.Impl.TeacherServiceImpl;

@Controller
public class TeacherController {

	private static final Logger log = LoggerFactory.getLogger(TeacherController.class);

	@Autowired
	private TeacherServiceImpl teacherService;

	@GetMapping("/teachers")
	@RequiresPermissions(value = "teacher:list")
	@SystemControllerLog(description = "查询所有教师")
	public String list(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo, Model model,
			@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, HttpSession session,
			@ModelAttribute("errorImport") String errorImport) {

		if(Common.isNotEmpty(errorImport)){
			
			model.addAttribute("errorImport", errorImport);
		}
		
		// 分页查询数据
		Pagination<Teacher> pagination;
		try {
			pagination = this.teacherService.findPaginationByQuery(new Query(), pageNo, pageSize, Teacher.class);
			if (pagination == null)
				pagination = new Pagination<Teacher>();

			model.addAttribute("pageList", pagination);
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

	
	
	
	/**
	 * 文件下载
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/teacher/download")
	@SystemControllerLog(description = "下载老师信息导入模版")
	public ModelAndView download(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String storeName = "老师信息导入模板.xlsx";
		String contentType = "application/octet-stream";
		String UPLOAD = "Templates/";
		FileOperateUtil.download(request, response, storeName, contentType, UPLOAD);

		return null;
	}
	
	
	
	
	
	/***
	 * 文件上传
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "static-access", "unused" })
	@RequestMapping(value = "/teacher/upload")
	@SystemControllerLog(description = "批量导入老师信息")
	public ModelAndView upload(Teacher teacher, HttpServletRequest request, HttpSession session,
			RedirectAttributes attr) {

		log.info("开始上传文件");
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("redirect:/teachers");
		String error = "";
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			// 别名
			String upname =  File.separator + "FileUpload" + File.separator + "teacher";

			// 可以上传的文件格式
			log.info("准备上传企业单位数据");
			String filetype[] = { "xls,xlsx" };
			List<Map<String, Object>> result = FileOperateUtil.upload(request, upname, filetype);
			log.info("上传文件成功");
			boolean has = (Boolean) result.get(0).get("hassuffix");

			if (has != false) {
				// 获得上传的xls文件路径
				String path = (String) result.get(0).get("savepath");
				File file = new File(path);
				// 知道导入返回导入结果
				error = this.teacherService.BatchImport(file, 1, session);

				attr.addFlashAttribute("errorImport", error);
				// map.put("result", result);
			}
		} catch (Exception e) {
			modelAndView.addObject("errorImport", e);
			return modelAndView;

		}

		return modelAndView;

	}

	
	/**
	 * process 获取进度
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/teacher/uploadprocess")
	@ResponseBody
	public Object process(HttpServletRequest request, HttpServletResponse response) throws Exception {

		return this.teacherService.findproInfo(request);
	}

	
	
	
	
	
	
	
	
	
	
	
	
	

}
