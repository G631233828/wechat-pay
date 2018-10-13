package zhongchiedu.school.util;

import java.util.HashMap;
import java.util.Map;

public class ToastType {
	
	public static  Map<String, String> getToastTypeList(){
		Map<String, String> map=new HashMap<>();
	       map.put("homework", "学生作业");
	       map.put("classmsg", "班级消息");
	       map.put("schoolmsg", "学校通知");
	       map.put("news", "校园动态");
	       return map;
	}
  }
