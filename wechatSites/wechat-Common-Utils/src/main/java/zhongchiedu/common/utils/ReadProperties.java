package zhongchiedu.common.utils;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class ReadProperties {

	
	
	public static String getObjectProperties(String propertiesPath,String key){
		
		Properties p = null;
	    try {
	    	 p = new Properties();
	    	// 使用ClassLoader加载properties配置文件生成对应的输入流
	    	InputStream in = ReadProperties.class.getClassLoader().getResourceAsStream(propertiesPath);
	    	// 使用properties对象加载输入流
			p.load(in);
			
			String o1 = p.getProperty(key);
			
//			url = p.getProperty("active.url");
//			user = p.getProperty("active.user");
//			password = p.getProperty("active.password");
//			queue = p.getProperty("active.queue");
			in.close();
			return o1;
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) {
		
		
		String url = getObjectProperties("application.properties","wechat.appid" );
		
		System.out.println(url);
		
		
	}
	
	

}
