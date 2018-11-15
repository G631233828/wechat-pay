package zhongchiedu.shiro.errorException;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.AuthorizationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class MyAuthorizationException{

	/**
	 * 全局异常处理捕捉未授权异常返回403页面
	 * @param req
	 * @param e
	 * @return
	 */
	@ExceptionHandler(value=AuthorizationException.class)
	public String handlerAuthorException(HttpServletRequest req ,Exception e){
		return "403";
	}
	

}
