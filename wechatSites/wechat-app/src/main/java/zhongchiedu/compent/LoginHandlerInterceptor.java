package zhongchiedu.compent;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import zhongchiedu.common.utils.Common;
import zhongchiedu.common.utils.Contents;

@Component
public class LoginHandlerInterceptor implements HandlerInterceptor {


	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		HttpSession session = request.getSession();
		String urlid = request.getParameter("urlid");
		if(Common.isNotEmpty(urlid)){
			session.setAttribute(Contents.MENU_ID, urlid);
		}
		
		
		/*
		if(Common.isNotEmpty(urlid)){
			Resource sub  = this.resourceService.findOneById(urlid, Resource.class);
			if (sub != null) {
			session.setAttribute(Contents.SUB_RESOURCE, sub);
			if (sub.getParentId().equals("1") && Common.isNotEmpty(sub.getParentId())) {
				Resource sup = this.resourceService.findOneById(sub.getParentId(), Resource.class);
				if (sup != null) {
					session.setAttribute(Contents.SUP_RESOURCE, sup);
				}
			}
		}
		
	}
		
	*/	return true;
		// String username = (String)
		// request.getSession().getAttribute(Contents.USER_SESSION);
		// if(username == null){
		// //返回登录页面
		// request.setAttribute("msg", "未获得权限");
		// request.getRequestDispatcher("/index.html").forward(request,
		// response);
		// return false;
		// }else{
		// //登录成功
		// return true;
		// }

	}

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// 通过urlid查询父目录以及urlid所在的资源
		
	}

}
