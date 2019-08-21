package zhongchiedu.controller.school;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import zhongchiedu.log.annotation.SystemControllerLog;
import zhongchiedu.school.pojo.Activitys;
import zhongchiedu.school.pojo.Trips;
import zhongchiedu.service.ActivitysService;
import zhongchiedu.service.TripsService;

@Controller
public class TripsController {

	private static final Logger log = LoggerFactory.getLogger(TripsController.class);

	@Autowired
	private TripsService tripsService;
	@Autowired
	private ActivitysService activitysService;
	@Autowired
//todo 添加站点的service注入  	

	@GetMapping("/triplist{id}")
	@SystemControllerLog(description = "查询所有活动")
	public String list(@PathVariable String id, Model model, HttpSession session) {
		// 根据活动id查询该活动下的行程
		List<Trips> list = this.tripsService.findTripsByActivityId(id);

		model.addAttribute("list", list);
		model.addAttribute("activityId", id);
		
		

		return "schools/trips/list";
	}

	@GetMapping("/trips{id}")
	@SystemControllerLog(description = "添加站点")
	public String toeditPage(@PathVariable String id, Model model) {
		//id为活动的id
		
		
		
		
//		Trips trips = this.tripsService.findOneById(id, Trips.class);
//model.addAttribute("trips", trips);
		// 获取所有活动
	//	List<Activitys> list = this.activitysService.findActivitysByisDisable();
		//model.addAttribute("activitys", list);
		return "schools/trips/add";

	}

	@PostMapping("/trips")
	@RequiresPermissions(value = "trips:add")
	@SystemControllerLog(description = "添加活动")
	public String addclazz(HttpServletRequest request, @Valid Trips trips) {
		String activityId = trips.getActivitys().getId();

		this.tripsService.SaveOrUpdateTrips(trips);

		return "redirect:triplist"+activityId;
	}

	/**
	 * restful请求put
	 * 
	 * @param school
	 * @return
	 */
	//
	// @PutMapping("/trips")
	// @RequiresPermissions(value = "trips:edit")
	// @SystemControllerLog(description = "修改活动")
	// public String editUser(@Valid Trips trips) {
	// this.tripsService.SaveOrUpdateTrips(trips);
	// return "redirect:tripss";
	// }
	//
	// /**
	// * 跳转到编辑界面
	// *
	// * @return
	// */
	// @GetMapping("/trips{id}")
	// @RequiresPermissions(value = "trips:edit")
	// @SystemControllerLog(description = "查看活动")
	// public String toeditPage(@PathVariable String id, Model model) {
	// Trips trips = this.tripsService.findOneById(id, Trips.class);
	// model.addAttribute("trips", trips);
	//
	// return "schools/trips/add";
	//
	// }
	//
	// @DeleteMapping("/trips/{id}")
	// @RequiresPermissions(value = "trips:delete")
	// @SystemControllerLog(description = "删除活动")
	// public String delete(@PathVariable String id) {
	// String[] strids = id.split(",");
	// for (String delids : strids) {
	// log.info("删除---》" + delids);
	// Trips trips = this.tripsService.findOneById(delids, Trips.class);
	// this.tripsService.remove(trips);// 删除某个id
	// }
	// return "redirect:/tripss";
	// }
	//
	// @RequestMapping(value = "/trips/disable", method = RequestMethod.POST,
	// produces = "application/json;charset=UTF-8")
	// @ResponseBody
	// public BasicDataResult clazzDisable(@RequestParam(value = "id",
	// defaultValue = "") String id) {
	//
	// return this.tripsService.tripsDisable(id);
	//
	// }

}
