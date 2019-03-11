package test;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import zhongchiedu.application.Application;
import zhongchiedu.general.pojo.User;
import zhongchiedu.general.service.Impl.UserServiceImpl;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class UserTest {

	    @Autowired
	    private UserServiceImpl userService;

	
	@Test
	public void addUser(){
		
		User user = this.userService.findOneById("5bd926f294fd7337ecd84b36", User.class);
//		
//		Update update = new Update();
//		update.set("passWord", "admin123");
//		
//		//update.update("passWord", "admin123");
//		
//		Query query = new Query();
//		
//		query.addCriteria(Criteria.where("passWord").is("aaaaa"));
//		
//		this.userService.updateAllByQuery(query, update, User.class);
//		
//		for(int i =0;i<5;i++){
//			
//			 User user = new User();
//			    user.setAccountName("adminaaa");
//			    user.setCardId("123456");
//			    user.setCardType("���֤");
//			    user.setCreateTime(new Date());
//			    user.setPassWord("123456");
//			    user.setIsDisable(false);
//			    user.setLastLoginIp(null);
//			    user.setPhotograph("/user/uploadImg/aaa.jpg");
//			    user.setUserName("admin");
//			   
//			    this.userService.insert(user);
//		
//		}

	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
