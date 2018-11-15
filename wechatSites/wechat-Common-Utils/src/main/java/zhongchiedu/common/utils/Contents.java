package zhongchiedu.common.utils;

import java.io.File;

/**
 * 系统常量配置
 * @author fliay
 *
 */
public class Contents {
    

	public static final String USER_REDIS_SESSION = "user-redis-session";//api session
	public static final String USER_SESSION = "user_session";//后台用户session
    public static final String USER_SESSION_ID = "usersessionId";
    public static final String RESOURCES_LIST = "resourceslist";
    public static final String MENU_ID="menuid";
    public static final String SCHOOL_SESSION = "school_session";//学校session
    
    /**
     * Realm名称
     */
    public static final String REALMNAME = "SHIRONAME";

	/**
	 * 地址栏id
	 */
    public static final String URLID="urlid";
    
    /**
     * 子目录资源
     */
    public static final String SUB_RESOURCE="subresource";
    /**
     * 父目录资源
     */
    public static final String SUP_RESOURCE="supresource";
    
    /**
     * 上次文件路径
     */
    public static final String NEWSFILE = File.separator+"assets"+File.separator+"FileUpload"+File.separator+"Img"+File.separator;
	
    
    /**
     * 上传文件
     */
    public static final String UPLOADPATH="uploadpath";
    public static final String FILENAME="filename";
    public static final String SAVEPATH="savepath";
    public static final String UPLOADDIR="uploaddir";//上传目录
    public static final String SUFFIXNAME="suffixname";//后缀名
    public static final String ERROR ="error";
    		
    
    
    /**
     * 微信配置文件读取
     */
    
    public static final String URL = ReadProperties.getObjectProperties("application.properties", "wechat.serverUrl");
    public static final String APPSECRET = ReadProperties.getObjectProperties("application.properties", "wechat.appsecret");
    public static final String APPID = ReadProperties.getObjectProperties("application.properties", "wechat.appid");
    
    
    
    
}
