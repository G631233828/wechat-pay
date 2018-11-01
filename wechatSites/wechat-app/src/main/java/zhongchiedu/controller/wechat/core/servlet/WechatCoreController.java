package zhongchiedu.controller.wechat.core.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import zhongchiedu.controller.wechat.core.service.CoreService;
import zhongchiedu.school.pojo.School;

/**
 * 微信核心请求处理
 * 
 * @author fliay
 *
 */

@Controller
@RequestMapping("/weChat/weChatCore")
public class WechatCoreController extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5697616411205492433L;

	private static final Logger log = LoggerFactory.getLogger(WechatCoreController.class);

	/**
	 * 校验信息是否是从微信服务器发过来的。
	 * 
	 * @param weChat
	 * @param out
	 */
	@RequestMapping(method = { RequestMethod.GET }, produces = "application/json;charset=UTF-8")
	public void doget(HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws IOException {
		log.info("微信验证");
		// 微信加密签名
		String signature = request.getParameter("signature");
		// 时间戳
		String timestamp = request.getParameter("timestamp");
		// 随机数
		String nonce = request.getParameter("nonce");
		// 随机字符串
		String echostr = request.getParameter("echostr");
		PrintWriter out = response.getWriter();
		// weChatServer.getToken()
		try {
			// WeChatServer
			// weChatServer=this.weChatServerService.findOneBySchoolId(ss.getId());
			// log.info("获取到"+ss.getName()+"学校Token（令牌）"+weChatServer.getToken());
			// 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
			if (SignUtil.checkSignature(signature, timestamp, nonce)) {
				out.print(echostr);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		out.close();
		out = null;
	}

	/**
	 * 微信消息的处理
	 * 
	 * @param request
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(method = { RequestMethod.POST }, produces = "application/xml;charset=UTF-8")
	public void dopost(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
		// 将请求、响应的编码均设置为UTF-8（防止中文乱码）
		request.setCharacterEncoding("UTF-8"); // 微信服务器POST消息时用的是UTF-8编码，在接收时也要用同样的编码，否则中文会乱码；
		log.info("进入微信方法！");
		String respMessage = CoreService.processRequest(request);// 调用CoreService类的processRequest方法接收、处理消息，并得到处理结果；
		PrintWriter out = response.getWriter();
		out.print(respMessage);

		out.close();

	}

}
