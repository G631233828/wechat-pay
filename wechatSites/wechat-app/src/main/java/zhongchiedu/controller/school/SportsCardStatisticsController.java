package zhongchiedu.controller.school;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import zhongchiedu.common.utils.Common;
import zhongchiedu.framework.pagination.Pagination;
import zhongchiedu.log.annotation.SystemControllerLog;
import zhongchiedu.school.pojo.Activitys;
import zhongchiedu.school.pojo.CardingStatistics;
import zhongchiedu.school.pojo.Clazz;
import zhongchiedu.school.pojo.SportsCarding;
import zhongchiedu.service.ActivitysService;
import zhongchiedu.service.CardingStatisticsService;
import zhongchiedu.service.ClazzService;

/**
 * 运动打卡统计
 * @author fliay
 *
 */
@Controller
public class SportsCardStatisticsController {
	
	@Autowired
	private CardingStatisticsService cardingStatisticsService;
	@Autowired
	private ActivitysService activitysService;
	@Autowired
	private ClazzService clazzService;
	
	
	
	@GetMapping("/sportsCard")
	@RequiresPermissions(value = "sportsCard:list")
	@SystemControllerLog(description = "查询打卡记录")
	public String list(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
			@RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize, 
			Model model,String activity,String grade,String clazzId,
			HttpSession session) {
		// 根据活动id查询该活动下的行程
		Pagination<CardingStatistics> pagination  = this.cardingStatisticsService.findCardingStatistics(pageNo,pageSize,activity,grade,clazzId);
		model.addAttribute("pageList", pagination);
		
		
		//获取所有活动
		List<Activitys> activitys = this.activitysService.findActivitysByisDisable();
		model.addAttribute("activitys", activitys);
		
		List<Clazz> clazzs = this.clazzService.findClazzsWhereInSchool(Common.findInSchoolYear());
		model.addAttribute("clazzs", clazzs);
		
		List<Integer> grades = Common.findInSchoolYear();
		model.addAttribute("grades", grades);
		model.addAttribute("selectActivity", Common.isNotEmpty(activity)?activity:"");
		model.addAttribute("selectGrade", Common.isNotEmpty(grade)?grade:"");
		model.addAttribute("selectClazz", Common.isNotEmpty(clazzId)?clazzId:"");
		return "schools/sportsCard/list";
	}
	
	
	
	
	@GetMapping("/sportsCardStudents{id}")
	@SystemControllerLog(description = "查询详细信息")
	public String sportsCardStudents( Model model,@PathVariable String id, HttpSession session) {

		
		List<SportsCarding> students = this.cardingStatisticsService.sportsCardings(id);
		
		model.addAttribute("students", students);
		
		return "schools/sportsCard/cardsList";
	}
	
	
	@RequestMapping(value = "/sportsCardStudents/export", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@SystemControllerLog(description = "导出统计")
	public void exportStock( HttpSession session,String activity,String grade,String clazzId, HttpServletResponse response) {
		try {
			HSSFWorkbook wb = this.cardingStatisticsService.export(activity, grade, clazzId);
			response.setContentType("application/vnd.ms-excel");
			String fileName = new String(("打卡统计").getBytes("gb2312"), "ISO8859-1");
			response.setHeader("Content-disposition", "attachment;filename=" + fileName+".xls");
			OutputStream ouputStream = response.getOutputStream();
			wb.write(ouputStream);
			ouputStream.flush();
			ouputStream.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	
	
	
	
	
	
	

}
