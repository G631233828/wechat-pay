package zhongchiedu.service;

import zhongchiedu.school.pojo.HomeWork;

public interface HomeWorkService {
	
	
	 String checkLogin(String username,String password,String date);
	 
	 HomeWork jsoupGetHomeWork(String page);
}
