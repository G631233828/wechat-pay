package zhongchiedu.controller.general;


import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import zhongchiedu.common.utils.Common;
import zhongchiedu.common.utils.Contents;
import zhongchiedu.common.utils.FileOperateUtil;
import zhongchiedu.general.pojo.User;
import zhongchiedu.general.service.UserService;

@Controller
public class LoginController {
	private static final Logger log = LoggerFactory.getLogger(LoginController.class);

	@Autowired
	private UserService userService;
	@Autowired
	private FileOperateUtil fileOperateUtil;

	@RequestMapping("/tologin")
	public String login(User user, HttpServletRequest request, Map<String, Object> map, HttpSession session)
			throws Exception {
		if(Common.isEmpty(user.getAccountName())||Common.isEmpty(user.getPassWord())){
			return "login";
		}
		String msg = "";
			/*
			 * boolean rememberMe = false; if (remember != "") { rememberMe =
			 * true; }
			 */
			String accountName = user.getAccountName();
			String password = user.getPassWord();
			if (accountName != "" && password != "") {
				UsernamePasswordToken token = new UsernamePasswordToken(accountName, password);
				// token.setRememberMe(rememberMe);
				Subject subject = SecurityUtils.getSubject();// 获得主体
				try {
					subject.login(token);
					if (subject.isAuthenticated()) {
						return "redirect:/toindex";
					} else {
						msg = "登录失败";
					}
				} catch (IncorrectCredentialsException e) {
					msg = "登录密码错误. Password for account " + token.getPrincipal() + " wasincorrect.";
				} catch (ExcessiveAttemptsException e) {
					msg = "登录失败次数过多";
				} catch (LockedAccountException e) {
					msg = "帐号已被锁定. The account for username " + token.getPrincipal() + " was locked.";
				} catch (DisabledAccountException e) {
					msg = "帐号已被禁用. The account for username " + token.getPrincipal() + "  was disabled.";
				} catch (ExpiredCredentialsException e) {
					msg = "帐号已过期. the account for username " + token.getPrincipal() + "  was expired.";
				} catch (UnknownAccountException e) {
					msg = "帐号不存在. There is no user with username of " + token.getPrincipal();
				} catch (UnauthorizedException e) {
					msg = "您没有得到相应的授权！" + e.getMessage();
				} finally {
					// attr.addFlashAttribute("msg", msg);
				}
				return "login";
			}

		return "redirect:/toindex";
	}

	/**
	 * 登出
	 * 
	 * @return
	 */
	@RequestMapping(value = "/loginOut")
	public String loginOut(HttpSession session,HttpServletResponse resp) {
		Subject subject = SecurityUtils.getSubject();// 获得主体
		session.removeAttribute(Contents.USER_SESSION);//删除cookie
		Cookie co = new Cookie("accountName", "");
		co.setMaxAge(0);// 设置立即过期
		co.setPath("/");// 根目录，整个网站有效
		resp.addCookie(co);
		subject.logout();
		return "login";
	}

	@RequestMapping(value="/toindex")
	public String toindex(){
		return "index";
	}
	

}
