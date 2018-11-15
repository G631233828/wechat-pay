package zhongchiedu.log.aspect;


import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.google.gson.Gson;

import zhongchiedu.common.utils.Common;
import zhongchiedu.common.utils.Contents;
import zhongchiedu.general.pojo.User;
import zhongchiedu.log.annotation.SystemControllerLog;
import zhongchiedu.log.annotation.SystemServiceLog;
import zhongchiedu.log.dao.LogDaoImpl;
import zhongchiedu.log.pojo.Log;

@Aspect    
@Component  
public class SystemLogAspect {
	  //注入Service用于把日志保存数据库    
	@Autowired    
     private LogDaoImpl LogDao;    
    //本地异常日志记录对象    
     private  static  final Logger logger = LoggerFactory.getLogger(SystemLogAspect. class);    
    
    //Service层切点    
    @Pointcut("@annotation(zhongchiedu.log.annotation.SystemServiceLog)")    
     public  void serviceAspect() {    
    }    
    
    //Controller层切点    
    @Pointcut("@annotation(zhongchiedu.log.annotation.SystemControllerLog)")    
     public  void controllerAspect() {    
    }    
    
    /**  
     * 前置通知 用于拦截Controller层记录用户的操作  
     *  
     * @param joinPoint 切点  
     */    
    @Before("controllerAspect()")    
     public  void doBefore(JoinPoint joinPoint) { 
    	//获取request
    	ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        HttpSession session = request.getSession();    
        //读取session中的用户    
        User user = (User) session.getAttribute(Contents.USER_SESSION);    
        //请求的IP    
        String ip = request.getRemoteAddr();    
         try {    
            //*========控制台输出=========*//    
            System.out.println("=====前置通知开始=====");    
            System.out.println("请求方法:" + (joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()"));    
            System.out.println("方法描述:" + getControllerMethodDescription(joinPoint));    
            System.out.println("请求人:" + user.getAccountName());    
            System.out.println("请求IP:" + ip);    
            //*========数据库日志=========*//    
            Log log = new Log();
            log.setDescription(getControllerMethodDescription(joinPoint));    
            log.setMethod((joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()"));    
            log.setType("0");    
            log.setRequestIp(ip);    
            log.setExceptionCode( null);    
            log.setExceptionDetail( null);    
            log.setParams( null);    
            log.setCreateby(user.getAccountName());    
            log.setCreateDate(Common.fromDateH());    
            this.LogDao.insert(log);
            System.out.println("=====前置通知结束=====");    
        }  catch (Exception e) {    
            //记录本地异常日志    
            logger.error("==前置通知异常==");    
            logger.error("异常信息:{}", e.getMessage());    
        }    
    }    
    
    /**  
     * 异常通知 用于拦截service层记录异常日志  
     *  
     * @param joinPoint  
     * @param e  
     */    
    @AfterThrowing(pointcut = "controllerAspect()", throwing = "e")    
     public  void doAfterThrowing(JoinPoint joinPoint, Throwable e) {    
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();    
        HttpSession session = request.getSession();    
        Gson gson = new Gson();
        //读取session中的用户    
        User user = (User) session.getAttribute(Contents.USER_SESSION);    
        //获取请求ip    
        String ip = request.getRemoteAddr();    
        //获取用户请求方法的参数并序列化为JSON格式字符串    
        String params = "";    
         if (joinPoint.getArgs() !=  null && joinPoint.getArgs().length > 0) {    
             for ( int i = 0; i < joinPoint.getArgs().length; i++) {    
                params += gson.toJson(joinPoint.getArgs()[i]) + ";";    
            }    
        }    
         try {    
              /*========控制台输出=========*/    
//            System.out.println("=====异常通知开始=====");    
//            System.out.println("异常代码:" + e.getClass().getName());    
//            System.out.println("异常信息:" + e.getMessage());    
//            System.out.println("异常方法:" + (joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()"));    
//            System.out.println("方法描述:" + getControllerMethodDescription(joinPoint));    
//            System.out.println("请求人:" + user.getAccountName());    
//            System.out.println("请求IP:" + ip);    
//            System.out.println("请求参数:" + params);    
               /*==========数据库日志=========*/    
            Log log = new Log();    
            log.setDescription(getControllerMethodDescription(joinPoint));    
            log.setExceptionCode(e.getClass().getName());    
            log.setType("1");    
            log.setExceptionDetail(e.getMessage());    
            log.setMethod((joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()"));    
            log.setParams(params);    
            log.setCreateby(user.getAccountName());    
            log.setCreateDate(Common.fromDateH());    
            log.setRequestIp(ip);    
            //保存数据库    
           this.LogDao.insert(log); 
            System.out.println("=====异常通知结束=====");    
        }  catch (Exception ex) {    
            //记录本地异常日志    
            logger.error("==异常通知异常==");    
            logger.error("异常信息:{}", ex.getMessage());    
        }    
         /*==========记录本地异常日志==========*/    
        logger.error("异常方法:{}异常代码:{}异常信息:{}参数:{}", joinPoint.getTarget().getClass().getName() + joinPoint.getSignature().getName(), e.getClass().getName(), e.getMessage(), params);    
    
    }    
    
    
    /**  
     * 获取注解中对方法的描述信息 用于service层注解  
     *  
     * @param joinPoint 切点  
     * @return 方法描述  
     * @throws Exception  
     */    
     public  static String getServiceMthodDescription(JoinPoint joinPoint)    
             throws Exception {    
        String targetName = joinPoint.getTarget().getClass().getName();    
        String methodName = joinPoint.getSignature().getName();    
        Object[] arguments = joinPoint.getArgs();    
        Class targetClass = Class.forName(targetName);    
        Method[] methods = targetClass.getMethods();    
        String description = "";    
         for (Method method : methods) {    
             if (method.getName().equals(methodName)) {    
                Class[] clazzs = method.getParameterTypes();    
                 if (clazzs.length == arguments.length) {    
                    description = method.getAnnotation(SystemServiceLog.class).description();    
                     break;    
                }    
            }    
        }    
         return description;    
    }    
    
    /**  
     * 获取注解中对方法的描述信息 用于Controller层注解  
     *  
     * @param joinPoint 切点  
     * @return 方法描述  
     * @throws Exception  
     */    
     public  static String getControllerMethodDescription(JoinPoint joinPoint)  throws Exception {    
        String targetName = joinPoint.getTarget().getClass().getName();    
        
        String methodName = joinPoint.getSignature().getName();    
        
        Object[] arguments = joinPoint.getArgs();   
        
        Class targetClass = Class.forName(targetName);   
        
        Method[] methods = targetClass.getMethods();    
        
        
        String description = "";    
         for (Method method : methods) {    
             if (method.getName().equals(methodName)) {    
                Class[] clazzs = method.getParameterTypes();    
                 if (clazzs.length == arguments.length) {    
                    description = method.getAnnotation(SystemControllerLog.class).description();    
                     break;    
                }    
            }    
        }    
         return description;    
    }    
}
