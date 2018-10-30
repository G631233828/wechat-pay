package zhongchiedu.controller.wechat;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("toast")
@Controller
public class WechatToastController {

	
		@RequestMapping("index")
		public String toastIndex() {
			return "wechat/front/toast/index";	
		}
}
