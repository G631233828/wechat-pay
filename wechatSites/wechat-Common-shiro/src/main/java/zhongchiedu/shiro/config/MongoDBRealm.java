package zhongchiedu.shiro.config;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import zhongchiedu.common.utils.Contents;
import zhongchiedu.general.pojo.Resource;
import zhongchiedu.general.pojo.User;
import zhongchiedu.general.service.Impl.UserServiceImpl;

public class MongoDBRealm extends AuthorizingRealm {
//	private static final Logger log = LoggerFactory.getLogger(MongoDBRealm.class);
	@Autowired
	private UserServiceImpl userService;
	

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		// 获取登录的用户名
		String accountName = SecurityUtils.getSubject().getPrincipal().toString();
		// 获取shirosession
//		log.info("当前登录用户：" + accountName);
		if (accountName != null || accountName != "") {
			Session session = SecurityUtils.getSubject().getSession();
			try {
				User user = (User) session.getAttribute(Contents.USER_SESSION);

				if (user != null) {

					User u = this.userService.findUserById(user.getId());

					// 学校的session
//
					//session.setAttribute(Contents.SCHOOL_SESSION, s);
//					log.info("生成school session" + s);

					// 获取所有角色的权限
					List<Resource> rs = u.getRole().getResource();

					// 权限信息对象info，用来存放查出的用户所有角色（role）及权限（permission）
					SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
					// 设置权限名称
					info.addRole(u.getRole().getRoleName());

					for (Resource r : rs) {

//						log.info("资源：" + r.getName() + ":" + r.getResUrl());

						info.addStringPermission(r.getResKey());

						session.setAttribute(Contents.RESOURCES_LIST, rs);

//						log.info("当前登录用户访问资源权限：" + info);
					}
					// ----------------------------------------

					session.setAttribute(Contents.RESOURCES_LIST, rs);
//					log.info("当前登录用户访问资源权限：" + info);
					return info;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		// 获取用户的名称
		UsernamePasswordToken usertoken = (UsernamePasswordToken) token;
		try {

			// 根据获取到的用户信息从数据库查询是否存在该用户名下的信息
			User user = this.userService.findUserByAccountName(usertoken.getUsername());
			if (user != null) {
				// 当验证都通过后，把用户信息放在session里
				Session session = SecurityUtils.getSubject().getSession();

				User u = this.userService.findUserById(user.getId());
				if (u != null) {
					// 通过集合获取资源
					List<Resource> rs = u.getRole().getResource();
					// List<Resource> rs = u.getResource();
					session.setAttribute(Contents.USER_SESSION, user);
					session.setAttribute(Contents.USER_SESSION_ID, user.getId());
					session.setAttribute(Contents.RESOURCES_LIST, rs);
					return new SimpleAuthenticationInfo(user.getAccountName(), user.getPassWord(), Contents.REALMNAME);
				}
			} else {
				return null;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

}
