package zhongchiedu.controller.school;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import zhongchiedu.common.utils.BasicDataResult;
import zhongchiedu.common.utils.Common;
import zhongchiedu.framework.pagination.Pagination;
import zhongchiedu.log.annotation.SystemControllerLog;
import zhongchiedu.school.pojo.WeChatbanding;
import zhongchiedu.service.SchoolService;
import zhongchiedu.service.WeChatbandingService;


@Controller
public class SchoolBandingController {
	

	private static final Logger log = LoggerFactory.getLogger(SchoolBandingController.class);

	@Autowired
	private SchoolService schoolService;

	
	@Autowired
	private WeChatbandingService weChatbandingService;

	
	@GetMapping("/bindings")
	@RequiresPermissions(value = "binding:list")
	@SystemControllerLog(description = "查询所有binding")
	public String list(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo, Model model,
			@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, HttpSession session,
			@ModelAttribute("errorImport") String errorImport) {
		if(Common.isNotEmpty(errorImport)){
			model.addAttribute("errorImport", errorImport);
		}
		// 分页查询数据
		Pagination<WeChatbanding> pagination;
		try {
			pagination = this.weChatbandingService.findPaginationByQuery(new Query(), pageNo, pageSize, WeChatbanding.class);
			if (pagination == null)
				pagination = new Pagination<WeChatbanding>();
			model.addAttribute("pageList", pagination);
			System.out.println(pagination.toString());
		} catch (Exception e) {
			log.info("查询bindinglist失败——————————》" + e.toString());
			e.printStackTrace();
		}
		return "schools/bind/list";
	}

	
	@DeleteMapping("/binding/{id}")
	@SystemControllerLog(description = "删除binding")
	public String delete(@PathVariable String id) {
		String[] strids = id.split(",");
		for (String delids : strids) {
			log.info("删除binding---》" + delids);
			WeChatbanding bind = this.weChatbandingService.findOneById(delids, WeChatbanding.class);
			this.weChatbandingService.remove(bind);// 删除某个id
		}
		return "redirect:/wechat";
	}
	
	@GetMapping("/add")
	@ResponseBody
	public BasicDataResult addbinding() {
		WeChatbanding we=new WeChatbanding();
		we.setOpenId("1321213321321213321asdasdasd");
		we.setStudentClass("1年级1班");
		we.setStudentName("陈豪1");
		we.setStudentAccount("123");
		weChatbandingService.insert(we);
		return BasicDataResult.build(200, "add,ok", we);
	}
}
