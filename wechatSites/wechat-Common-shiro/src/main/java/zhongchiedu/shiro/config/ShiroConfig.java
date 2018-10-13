package zhongchiedu.shiro.config;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置shiro
 * 
 * @author fliay
 *
 */
@Configuration
public class ShiroConfig {

	private static final Logger log = LoggerFactory.getLogger(ShiroConfig.class);

	@Bean
	public ShiroFilterFactoryBean ShiroFilter(SecurityManager securityManager) {
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		shiroFilterFactoryBean.setSecurityManager(securityManager);
		// 拦截器.
		Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
		// 配置不会被拦截的链接 顺序判断
		filterChainDefinitionMap.put("/static/**", "anon");

		filterChainDefinitionMap.put("/api/**", "anon");

		filterChainDefinitionMap.put("/assets/**", "anon");
		filterChainDefinitionMap.put("/init/**", "anon");
		filterChainDefinitionMap.put("/tologin", "anon");
		filterChainDefinitionMap.put("/img/**", "anon");
		filterChainDefinitionMap.put("/weChat/**", "anon");
		// 配置退出 过滤器,其中的具体的退出代码Shiro已经替我们实现了
		// filterChainDefinitionMap.put("/loginout", "loginout");
		// <!-- 过滤链定义，从上向下顺序执行，一般将/**放在最为下边 -->:这是一个坑呢，一不小心代码就不好使了;
		// <!-- authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问-->
		filterChainDefinitionMap.put("/**", "authc");
		// 如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面
		// 登录成功后要跳转的链接
		// shiroFilterFactoryBean.setSuccessUrl("/website/index");
		shiroFilterFactoryBean.setLoginUrl("/tologin");
		// // 登录成功后要跳转的链接
		shiroFilterFactoryBean.setSuccessUrl("/toindex");
		// 未授权界面;
		shiroFilterFactoryBean.setUnauthorizedUrl("/403");
		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
		return shiroFilterFactoryBean;

	}

	/**
	 * 密码匹配凭证管理器
	 * 
	 * @return
	 */
	@Bean
	public HashedCredentialsMatcher hashedCredentialsMatcher() {
		// log.info("hashedCredentialsMatcher()");
		HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
		hashedCredentialsMatcher.setHashAlgorithmName("MD5");// 散列算法:这里使用MD5算法;
		hashedCredentialsMatcher.setHashIterations(1024);// 散列的次数，比如散列两次，相当于
															// md5(md5(""));
		return hashedCredentialsMatcher;
	}

	/**
	 * 开启shiro aop注解支持. 使用代理方式;所以需要开启代码支持; Controller才能使用@RequiresPermissions
	 * 
	 * @param securityManager
	 * @return
	 */
	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(
			@Qualifier("securityManager") SecurityManager securityManager) {
		// log.info("authorizationAttributeSourceAdvisor()");
		AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
		authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
		return authorizationAttributeSourceAdvisor;
	}

	/**
	 * shiro缓存管理器; 需要注入对应的其它的实体类中： 1、安全管理器：securityManager
	 * 可见securityManager是整个shiro的核心；
	 * 
	 * @return
	 */

	@Bean
	public EhCacheManager ehCacheManager() {
		System.out.println("ShiroConfiguration.getEhCacheManager()");
		EhCacheManager cacheManager = new EhCacheManager();
		cacheManager.setCacheManagerConfigFile("classpath:ehcache-shiro.xml");
		return cacheManager;

	}

	@Bean
	public MongoDBRealm mongoDBRealm() {
		MongoDBRealm mongoDBRealm = new MongoDBRealm();
		return mongoDBRealm;
	}

	@Bean
	public SecurityManager securityManager() {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		securityManager.setRealm(mongoDBRealm());
		// 注入缓存管理器;
		securityManager.setCacheManager(ehCacheManager());// 这个如果执行多次，也是同样的一个对象;

		return securityManager;
	}

}
