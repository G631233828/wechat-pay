package zhongchiedu.controller.wechat;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import zhongchiedu.common.utils.BasicDataResult;
import zhongchiedu.common.utils.Common;
import zhongchiedu.log.annotation.SystemControllerLog;
import zhongchiedu.pojo.WechatMenu;
import zhongchiedu.wechat.service.Impl.WechatMenuServiceImpl;

@Controller
public class WeChatMenuController {

	private static final Logger log = LoggerFactory.getLogger(WeChatMenuController.class);

	@Autowired
	private WechatMenuServiceImpl wechatMenuService;

	@GetMapping("/wechatmenus")
	@RequiresPermissions(value = "wechatmenu:list")
	@SystemControllerLog(description = "查询微信菜单")
	public String wechatmenus(Model model, HttpSession session) {
		List<WechatMenu> list = this.wechatMenuService.findWeChatMenus();
		model.addAttribute("wechatMenu", list);
		if (Common.isNotEmpty(list)) {
			int size = 0;
			for (WechatMenu m : list) {
				if (m.getParentId().equals("0")) {
					size++;
				}
			}
			model.addAttribute("supMenuSize", size);
		}

		return "wechat/wechatMenu/list";
	}

	@PostMapping("/wechatmenu")
	// @RequiresPermissions(value = "wechatmenu:add")
	@SystemControllerLog(description = "添加微信菜单")
	public String addWechatMenu(@ModelAttribute("wechatmenu") WechatMenu wechatmenu, HttpSession session) {

		this.wechatMenuService.saveOrUpdateUser(wechatmenu,session);

		return "redirect:/wechatmenus";
	}

	@RequestMapping("/wechatmenu/editMenu")
	// @RequiresPermissions(value = "wechatmenu:edit")
	@SystemControllerLog(description = "微信子菜单编辑")
	@ResponseBody
	public BasicDataResult editMenu(@RequestParam(value = "id", defaultValue = "") String id) {

		return this.wechatMenuService.findWechatMenuByid(id);

	}

	@DeleteMapping("/wechatmenu/{id}")
	// @RequiresPermissions(value = "wechatmenu:delete")
	@SystemControllerLog(description = "删除菜单")
	public String delete(@PathVariable String id) {

		this.wechatMenuService.deleteWechatMenuById(id);

		return "redirect:/wechatmenus";
	}

	@RequestMapping("/wechatmenu/release")
	// @RequiresPermissions(value = "wechatmenu:delete")
	@SystemControllerLog(description = "菜单发布")
	@ResponseBody
	public BasicDataResult release(HttpSession session) {
		
		BasicDataResult result = this.wechatMenuService.release();
		
		return result;
	}

}
