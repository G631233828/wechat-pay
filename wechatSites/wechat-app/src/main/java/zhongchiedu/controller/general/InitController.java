package zhongchiedu.controller.general;



import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import zhongchiedu.general.pojo.Resource;
import zhongchiedu.general.pojo.Role;
import zhongchiedu.general.pojo.User;
import zhongchiedu.general.service.Impl.ResourceServiceImpl;
import zhongchiedu.general.service.Impl.RoleServiceImpl;
import zhongchiedu.general.service.Impl.UserServiceImpl;
import zhongchiedu.log.annotation.SystemControllerLog;

@Controller
@RequestMapping("/init")
public class InitController {

    @Autowired
    private ResourceServiceImpl resourceService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private RoleServiceImpl roleService;

    @RequestMapping("/toinitPage")
    public String toinitPage() {

	return "initialize";
    }

    @RequestMapping(value = "/toInit")
    @SystemControllerLog(description="系统资源初始化")
    public void initauthor(PrintWriter printWriter, HttpSession session, HttpServletResponse response,
	    @RequestParam(defaultValue = "", value = "accountName") String accountName,
	    @RequestParam(defaultValue = "", value = "passWord") String passWord) throws InterruptedException {
	// 如果帐号密码不为空
	if (!("").equals(accountName) && !("").equals(passWord)) {
		  String rtmsg = "";
	    // 帐号密码正确，开始初始化
	    if (accountName.equals("631233828@qq.com") && passWord.equals("fliay1008")) {
		// 1.清除原有数据
		try {
		    List<Resource> listRes = this.resourceService.findAllResource();
		    for (Resource rs : listRes) {
			this.resourceService.remove(rs);
		    }
		    List<User> listUser = this.userService.findAllUser();
		    for (User us : listUser) {
			this.userService.remove(us);
		    }
		    List<Role> listRole = this.roleService.findAllRole();
		    for (Role ro : listRole) {
			this.roleService.remove(ro);
		    }

		} catch (Exception e1) {
		    // TODO Auto-generated catch block
		    e1.printStackTrace();
		}

		// 1.初始化资源信息
		try {
		    Resource res = new Resource();
		    res.setName("系统基础管理");
		    res.setDescription("系统基础管理");
		    res.setIcon("icon-cog");
		    res.setIsDisable(false);
		    res.setParentId("0");// 父目录id 如果id为0则为主目录
		    res.setResKey("system");
		    res.setResUrl("system");
		    res.setType(0);// 0为目录 //0为父目录 1为菜单 2为操作功能（添加删除修改）
		    this.resourceService.insert(res);
		    String parentId1 = res.getId();

		    res = new Resource();
		    res.setName("用户管理");
		    res.setDescription("用户管理");
		    res.setIcon("icon");
		    res.setIsDisable(false);
		    res.setParentId(parentId1);// 父目录id 如果id为0则为主目录
		    res.setResKey("user:list");
		    res.setResUrl("/user/list");
		    res.setType(1);// 0为目录 //0为父目录 1为菜单 2为操作功能（添加删除修改）
		    this.resourceService.insert(res);
		    String parentId2 = res.getId();

		    res = new Resource();
		    res.setName("添加");
		    res.setDescription("添加按钮");
		    res.setIcon("icon");
		    res.setIsDisable(false);
		    res.setParentId(parentId2);// 父目录id 如果id为0则为主目录
		    res.setResKey("user:add");
		    res.setResUrl("user/add");
		    res.setType(2);// 0为目录 //0为父目录 1为菜单 2为操作功能（添加删除修改）按钮
		    this.resourceService.insert(res);

		    res = new Resource();
		    res.setName("删除");
		    res.setDescription("删除按钮");
		    res.setIcon("icon");
		    res.setIsDisable(false);
		    res.setParentId(parentId2);// 父目录id 如果id为0则为主目录
		    res.setResKey("user:delete");
		    res.setResUrl("user/delete");
		    res.setType(2);// 0为目录 //0为父目录 1为菜单 2为操作功能（添加删除修改）按钮
		    this.resourceService.insert(res);

		    res = new Resource();
		    res.setName("权限分配");
		    res.setDescription("权限分配");
		    res.setIcon("icon");
		    res.setIsDisable(false);
		    res.setParentId(parentId2);// 父目录id 如果id为0则为主目录
		    res.setResKey("user:author");
		    res.setResUrl("user/toauthorPage");
		    res.setType(2);// 0为目录 //0为父目录 1为菜单 2为操作功能（添加删除修改）按钮
		    this.resourceService.insert(res);

		    res = new Resource();
		    res.setName("修改");
		    res.setDescription("修改按钮");
		    res.setIcon("icon");
		    res.setIsDisable(false);
		    res.setParentId(parentId2);// 父目录id 如果id为0则为主目录
		    res.setResKey("user:edit");
		    res.setResUrl("user/edit");
		    res.setType(2);// 0为目录 //0为父目录 1为菜单 2为操作功能（添加删除修改）按钮
		    this.resourceService.insert(res);

		    res = new Resource();
		    res.setName("资源管理");
		    res.setDescription("资源管理");
		    res.setIcon("icon");
		    res.setIsDisable(false);
		    res.setParentId(parentId1);// 父目录id 如果id为0则为主目录
		    res.setResKey("resource:list");
		    res.setResUrl("resource/list");
		    res.setType(1);// 0为目录 //0为父目录 1为菜单 2为操作功能（添加删除修改）
		    this.resourceService.insert(res);

		    String parentId3 = res.getId();
		    res = new Resource();
		    res.setName("添加");
		    res.setDescription("添加按钮");
		    res.setIcon("icon");
		    res.setIsDisable(false);
		    res.setParentId(parentId3);// 父目录id 如果id为0则为主目录
		    res.setResKey("resource:add");
		    res.setResUrl("resource/add");
		    res.setType(2);// 0为目录 //0为父目录 1为菜单 2为操作功能（添加删除修改）按钮
		    this.resourceService.insert(res);

		    res = new Resource();
		    res.setName("删除");
		    res.setDescription("删除按钮");
		    res.setIcon("icon");
		    res.setIsDisable(false);
		    res.setParentId(parentId3);// 父目录id 如果id为0则为主目录
		    res.setResKey("resource:delete");
		    res.setResUrl("resource/delete");
		    res.setType(2);// 0为目录 //0为父目录 1为菜单 2为操作功能（添加删除修改）按钮
		    this.resourceService.insert(res);

		    res = new Resource();
		    res.setName("修改");
		    res.setDescription("修改按钮");
		    res.setIcon("icon");
		    res.setIsDisable(false);
		    res.setParentId(parentId3);// 父目录id 如果id为0则为主目录
		    res.setResKey("resource:edit");
		    res.setResUrl("resource/edit");
		    res.setType(2);// 0为目录 //0为父目录 1为菜单 2为操作功能（添加删除修改）按钮
		    this.resourceService.insert(res);

		    res = new Resource();
		    res.setName("角色管理");
		    res.setDescription("角色管理");
		    res.setIcon("icon");
		    res.setIsDisable(false);
		    res.setParentId(parentId1);// 父目录id 如果id为0则为主目录
		    res.setResKey("role:list");
		    res.setResUrl("role/list");
		    res.setType(1);// 0为目录 //0为父目录 1为菜单 2为操作功能（添加删除修改）
		    this.resourceService.insert(res);

		    String parentId4 = res.getId();
		    res = new Resource();
		    res.setName("添加");
		    res.setDescription("添加按钮");
		    res.setIcon("icon");
		    res.setIsDisable(false);
		    res.setParentId(parentId4);// 父目录id 如果id为0则为主目录
		    res.setResKey("role:add");
		    res.setResUrl("role/add");
		    res.setType(2);// 0为目录 //0为父目录 1为菜单 2为操作功能（添加删除修改）按钮
		    this.resourceService.insert(res);

		    res = new Resource();
		    res.setName("删除");
		    res.setDescription("删除按钮");
		    res.setIcon("icon");
		    res.setIsDisable(false);
		    res.setParentId(parentId4);// 父目录id 如果id为0则为主目录
		    res.setResKey("role:delete");
		    res.setResUrl("role/delete");
		    res.setType(2);// 0为目录 //0为父目录 1为菜单 2为操作功能（添加删除修改）按钮
		    this.resourceService.insert(res);

		    res = new Resource();
		    res.setName("修改");
		    res.setDescription("修改按钮");
		    res.setIcon("icon");
		    res.setIsDisable(false);
		    res.setParentId(parentId4);// 父目录id 如果id为0则为主目录
		    res.setResKey("role:edit");
		    res.setResUrl("role/edit");
		    res.setType(2);// 0为目录 //0为父目录 1为菜单 2为操作功能（添加删除修改）按钮
		    this.resourceService.insert(res);

		    res = new Resource();
		    res.setName("系统监控");
		    res.setDescription("系统监控");
		    res.setIcon("icon-dashboard");
		    res.setIsDisable(false);
		    res.setParentId("0");// 父目录id 如果id为0则为主目录
		    res.setResKey("systemlog");
		    res.setResUrl("systemlog");
		    res.setType(0);// 0为目录 //0为父目录 1为菜单 2为操作功能（添加删除修改）
		    this.resourceService.insert(res);

		    String parentId5 = res.getId();

		    res = new Resource();
		    res.setName("日志监控");
		    res.setDescription("日志管理");
		    res.setIcon("icon");
		    res.setIsDisable(false);
		    res.setParentId(parentId5);// 父目录id 如果id为0则为主目录
		    res.setResKey("log:list");
		    res.setResUrl("log/list");
		    res.setType(1);// 0为目录 //0为父目录 1为菜单 2为操作功能（添加删除修改）按钮
		    this.resourceService.insert(res);

		    res = new Resource();
		    res.setName("校园管理");
		    res.setDescription("校园管理");
		    res.setIcon("icon-group");
		    res.setIsDisable(false);
		    res.setParentId("0");// 父目录id 如果id为0则为主目录
		    res.setResKey("systemlog");
		    res.setResUrl("systemlog");
		    res.setType(0);// 0为目录 //0为父目录 1为菜单 2为操作功能（添加删除修改）
		    this.resourceService.insert(res);

		    String parentId6 = res.getId();
		    res = new Resource();
		    res.setName("学校管理");
		    res.setDescription("学校管理");
		    res.setIcon("icon");
		    res.setIsDisable(false);
		    res.setParentId(parentId6);// 父目录id 如果id为0则为主目录
		    res.setResKey("school:list");
		    res.setResUrl("school/list");
		    res.setType(1);// 0为目录 //0为父目录 1为菜单 2为操作功能（添加删除修改）
		    this.resourceService.insert(res);
		    //添加区域模版
		    res = new Resource();
		    res.setName("区域管理");
		    res.setDescription("区域管理");
		    res.setIcon("icon");
		    res.setIsDisable(false);
		    res.setParentId(parentId6);// 父目录id 如果id为0则为主目录
		    res.setResKey("area:list");
		    res.setResUrl("area/list");
		    res.setType(1);// 0为目录 //0为父目录 1为菜单 2为操作功能（添加删除修改）
		    this.resourceService.insert(res);
		    
		    

		    String parentId7 = res.getId();

		    res = new Resource();
		    res.setName("添加");
		    res.setDescription("添加按钮");
		    res.setIcon("icon");
		    res.setIsDisable(false);
		    res.setParentId(parentId7);// 父目录id 如果id为0则为主目录
		    res.setResKey("school:add");
		    res.setResUrl("school/add");
		    res.setType(2);// 0为目录 //0为父目录 1为菜单 2为操作功能（添加删除修改）按钮
		    this.resourceService.insert(res);

		    res = new Resource();
		    res.setName("删除");
		    res.setDescription("删除按钮");
		    res.setIcon("icon");
		    res.setIsDisable(false);
		    res.setParentId(parentId7);// 父目录id 如果id为0则为主目录
		    res.setResKey("school:delete");
		    res.setResUrl("school/delete");
		    res.setType(2);// 0为目录 //0为父目录 1为菜单 2为操作功能（添加删除修改）按钮
		    this.resourceService.insert(res);

		    res = new Resource();
		    res.setName("修改");
		    res.setDescription("修改按钮");
		    res.setIcon("icon");
		    res.setIsDisable(false);
		    res.setParentId(parentId7);// 父目录id 如果id为0则为主目录
		    res.setResKey("school:edit");
		    res.setResUrl("school/edit");
		    res.setType(2);// 0为目录 //0为父目录 1为菜单 2为操作功能（添加删除修改）按钮
		    this.resourceService.insert(res);

		    // 初始化资源完成
		    List<Resource> list = this.resourceService.find(new Query(),Resource.class);
		    // 2.初始化角色
		    Role role = new Role();
		    role.setRoleKey("supermanager");
		    role.setDescription("超级管理员");
		    role.setRoleName("超级管理员");
		    role.setCreateTime(new Date());
		    role.setIsDisable(false);
		    role.setResource(list);
		    this.roleService.insert(role);
		    String roleId = role.getId();

		    // 3.初始化用户

		    User user = new User();
		    user.setAccountName("admin");
		    user.setCardId("123456");
		    user.setCardType("身份证");
		    user.setCreateTime(new Date());
		    user.setPassWord("123456");
		    user.setIsDisable(false);
		    user.setLastLoginIp(null);
		    user.setPhotograph("/user/uploadImg/aaa.jpg");
		    user.setUserName("admin");
		   
		    user.setResource(list);
		    Role r = this.roleService.findOneById(roleId,Role.class);
		    user.setRole(r);
		    this.userService.insert(user);

		    
		    res = new Resource();
		    res.setName("测试");
		    res.setDescription("修改按钮");
		    res.setIcon("icon");
		    res.setIsDisable(false);
		    res.setParentId(parentId7);// 父目录id 如果id为0则为主目录
		    res.setResKey("test:test123");
		    res.setResUrl("test1/test123");
		    res.setType(1);// 0为目录 //0为父目录 1为菜单 2为操作功能（添加删除修改）按钮
		    this.resourceService.insert(res);
		    
		    
		    rtmsg = "初始化成功！您的登录帐号为：" + user.getAccountName() + ",您的登录密码为：" + user.getPassWord() + " ,请及时登录修改";

		} catch (Exception e) {
		    rtmsg = "在初始化的过程中发生了未知错误，请与管理员联系" + e.getMessage();
		    e.printStackTrace();
		}

	    } else {
		rtmsg = "您输入的初始化数据帐号或密码有误！";

	    }
	    printWriter.write(rtmsg);
	    printWriter.flush();
	    printWriter.close();
	}

    }
}