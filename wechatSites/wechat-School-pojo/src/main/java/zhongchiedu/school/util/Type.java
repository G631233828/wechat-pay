package zhongchiedu.school.util;

import java.util.Arrays;
import java.util.List;

/**
 * 区域性质
 * @author cd
 *
 */
public class Type {
	/**
	 * 返回type的性质 
	 * @return
	 */
	public static List<String> getTypeList(){
		List<String> list=Arrays.asList("中学","小学","幼儿园","中小学","高中","九年制");
	    return list;
	}
	
	
	/**
	 * 学校性质
	 * @author fliay
	 *
	 */
	public enum SchoolNature{
		幼儿园,小学,中学,中小学,高中,九年制
	}
	

	/**
	 * 
	 * @author fliay
	 * parent 家长
	 * teacher 老师
	 * schoolManager 学校管理员
	 * superManager 超级管理员
	 */
	public enum AccountType{
		parent,teacher,schoolManager,superManager
	}
}
