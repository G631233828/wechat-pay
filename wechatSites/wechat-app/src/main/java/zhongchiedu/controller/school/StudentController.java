package zhongchiedu.controller.school;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

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
import zhongchiedu.school.pojo.Clazz;
import zhongchiedu.school.pojo.Student;
import zhongchiedu.school.pojo.Teacher;
import zhongchiedu.school.pojo.WeChatbanding;
import zhongchiedu.school.pojo.WeChatbandingStudent;
import zhongchiedu.service.Impl.ClazzServiceImpl;
import zhongchiedu.service.Impl.StudentServiceImpl;
import zhongchiedu.service.Impl.TeacherServiceImpl;
import zhongchiedu.service.Impl.WeChatbandingServiceImpl;

@Controller
public class StudentController {

	@Autowired
	private StudentServiceImpl studentService;

	@Autowired
	private ClazzServiceImpl clazzService;

	@Autowired
	private TeacherServiceImpl teacherService;

	@Autowired
	private WeChatbandingServiceImpl weChatbandingService;

	private static final Logger log = LoggerFactory.getLogger(StudentController.class);

	/**
	 * 查询所有学生
	 * 
	 * @param pageNo
	 * @param model
	 * @param pageSize
	 * @param session
	 * @param errorImport
	 * @return
	 */
	@GetMapping("/students")
	@RequiresPermissions(value = "student:list")
	@SystemControllerLog(description = "查询所有学生")
	public String list(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo, Model model,
			@RequestParam(value = "pageSize", defaultValue = "50") Integer pageSize, HttpSession session,
			@ModelAttribute("errorImport") String errorImport) {

		if (Common.isNotEmpty(errorImport)) {

			model.addAttribute("errorImport", errorImport);
		}

		//获取所有的班级
		List<Clazz> clazzList = clazzService.findClazzsByisDisable();
		model.addAttribute("clazzList", clazzList);
		
		//获取所有的绑定
		List<WeChatbanding> bds = this.weChatbandingService.find(new Query(), WeChatbanding.class);
		// 分页查询数据
		Pagination<Student> pagination;
		try {
			pagination = this.studentService.findPaginationByQuery(new Query(), pageNo, pageSize, Student.class);
			if (pagination == null)
				pagination = new Pagination<Student>();

			List<Student> students = pagination.getDatas();
			for(Student s:students){
				boolean flag = false;
				for(WeChatbanding wechatbanding:bds){
					for(WeChatbandingStudent ws:wechatbanding.getListbandings()){
						if(ws.getStudentAccount().equals(s.getAccount())){
							flag = true;
						}
					}
				}
				s.setState(flag);
			}
			model.addAttribute("pageList", pagination);
		} catch (Exception e) {
			log.info("查询学生信息失败——————————》" + e.toString());
			e.printStackTrace();
		}

		return "schools/student/list";
	}

	/**
	 * 添加学生
	 * 
	 * @param request
	 * @param student
	 * @return
	 */
	@PostMapping("/student")
	@RequiresPermissions(value = "student:add")
	@SystemControllerLog(description = "添加学生")
	public String addclazz(HttpServletRequest request, @Valid Student student) {
		this.studentService.SaveOrUpdateStudent(student);
		return "redirect:students";
	}

	/**
	 * 编辑跳转
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@GetMapping("/student{id}")
	@RequiresPermissions(value = "student:edit")
	@SystemControllerLog(description = "查看")
	public String toeditPage(@PathVariable String id, Model model) {

/*		List<Clazz> clazzList = clazzService.findClazzsByisDisable();
		model.addAttribute("clazzList", clazzList);

		List<Teacher> teacherList = this.teacherService.findTeachersByisDisable();
		model.addAttribute("teacherList", teacherList);
*/
		Student student = this.studentService.findOneById(id, Student.class);
		model.addAttribute("student", student);
		return "schools/student/add";

	}

	/**
	 * 修改学生
	 * 
	 * @param student
	 * @return
	 */
	@PutMapping("/student")
	@RequiresPermissions(value = "student:edit")
	@SystemControllerLog(description = "修改学生")
	public String editUser(@ModelAttribute("student") Student student) {
		this.studentService.SaveOrUpdateStudent(student);
		return "redirect:students";
	}

	/**
	 * 删除学生
	 * 
	 * @param id
	 * @return
	 */
	@DeleteMapping("/student/{id}")
	@RequiresPermissions(value = "student:delete")
	@SystemControllerLog(description = "删除学生")
	public String delete(@PathVariable String id) {
		String[] strids = id.split(",");
		for (String delids : strids) {
			log.info("删除学生---》" + delids);
			Student student = this.studentService.findOneById(delids, Student.class);
			this.studentService.remove(student);// 删除某个id
		}
		return "redirect:/students";
	}

	/**
	 * 禁用
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/student/disable", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public BasicDataResult studentDisable(@RequestParam(value = "id", defaultValue = "") String id) {
		return this.studentService.studentDisable(id);
	}

	/**
	 * 模版下载
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/student/download")
	@SystemControllerLog(description = "下载学校信息导入模版")
	public ModelAndView download(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String storeName = "学生信息模版.xlsx";
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
	@RequestMapping(value = "/student/upload")
	@SystemControllerLog(description = "批量导入学生信息")
	public ModelAndView upload(Student student, HttpServletRequest request, HttpSession session,
			RedirectAttributes attr) {
		log.info("开始上传文件");
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("redirect:/students");
		String error = "";
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			// 别名
			String upname = File.separator + "FileUpload" + File.separator + "student";

			// 可以上传的文件格式
			log.info("准备上传学生数据");
			String filetype[] = { "xls,xlsx" };
			List<Map<String, Object>> result = FileOperateUtil.upload(request, upname, filetype);
			log.info("上传文件成功");
			boolean has = (Boolean) result.get(0).get("hassuffix");

			if (has != false) {
				// 获得上传的xls文件路径
				String path = (String) result.get(0).get("savepath");
				File file = new File(path);
				// 知道导入返回导入结果
				error = this.studentService.BatchImport(file, 1, session);
				attr.addFlashAttribute("errorImport", error);
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
	@RequestMapping(value = "/student/uploadprocess")
	@ResponseBody
	public Object process(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return this.studentService.findproInfo(request);
	}

	@RequestMapping(value = "/student/findStudentByRegisterNum", method = RequestMethod.POST)
	@ResponseBody
	public BasicDataResult findStudentByRegisterNum(String registerNumber) {

		return this.studentService.findStudentByRegisterNum(registerNumber);
	}

	// 将绑定的学生同步

	@RequestMapping("/student/tongbu")
	@ResponseBody
	public BasicDataResult tongbu() {
		try{
			
		
		log.info("正在同步学生信息");
		// 1.获取所有绑定好的学生
		List<WeChatbanding> bds = this.weChatbandingService.find(new Query(), WeChatbanding.class);
		if (bds.size() > 0) {
			for (WeChatbanding we : bds) {
				for (WeChatbandingStudent stu : we.getListbandings()) {
					log.info("正在同步："+stu);
					this.studentService.saveStudentByRegisterNum(stu);
				}
			}
		}
		log.info("同步完成");
		return BasicDataResult.build(200, "同步完成", null);
		}catch(Exception e){
			e.printStackTrace();
			return BasicDataResult.build(400, "同步过程中出现异常", null);
		}
	}

}
