package zhongchiedu.service;

import java.util.List;

import zhongchiedu.common.utils.BasicDataResult;
import zhongchiedu.school.pojo.HomeWork;

public interface HomeWorkService {
	
	
	 String checkLogin(String username,String password);
	 
	 BasicDataResult getHomeWork(String username,String password);
	
	 HomeWork jsoupGetHomeWork(String page);
}
