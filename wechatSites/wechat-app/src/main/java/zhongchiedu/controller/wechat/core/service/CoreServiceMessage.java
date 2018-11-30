package zhongchiedu.controller.wechat.core.service;



import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.management.Notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import net.sf.json.JSONObject;
import zhongchiedu.framework.pagination.Pagination;
import zhongchiedu.school.pojo.News;
import zhongchiedu.wechat.resp.Article;
import zhongchiedu.wechat.resp.CustomMessage;
import zhongchiedu.wechat.resp.NewsMessage;
import zhongchiedu.wechat.resp.TextMessage;
import zhongchiedu.wechat.util.MessageUtil;


public class CoreServiceMessage {
	static String address="http://www.zhongchiedu.com";
	private static final Logger log = LoggerFactory
			.getLogger(CoreServiceMessage.class);
	
//	private static NewsService newsService;//新闻
//	private static NotificationService notificationService;//学校通知
//	private static PermissionRoleService permissionRoleService;//角色
//	
//	private static List<WechatMenuWeb> listWeChatMenu;
//	
//	private static ApplicationContext ac;
	
//	static{
//		ac = new ClassPathXmlApplicationContext("school-application-config.xml");
//		newsService = (NewsService) ac.getBean("newsService");//新闻
//		notificationService = (NotificationService) ac.getBean("notificationService");//通知
//		permissionRoleService = (PermissionRoleService) ac.getBean("permissionRoleService");//菜单
//		log.info("新闻："+newsService);
//		log.info("通知："+notificationService);
//		log.info("权限菜单："+permissionRoleService);
//		listWeChatMenu=getRoleMenuForWechat();
//	}

	// 用户订阅
	public static String Subscribe(NewsMessage newsMessage) {
		List<Article> articleList = new ArrayList<Article>();
		Article article = new Article();
			article.setTitle("中赤信息技术欢迎您！");
			article.setDescription("从事教育行业产品的研发，针对中小学管理系统的研发，目前主要产品有：学前教育管理系统，全媒体资源管理系统，学校多媒体教室一体化建设，学校网站建设等！");
			article.setPicurl(address+"web/assets/images/school.png");
			article.setUrl("http://www.baidu.com");
		articleList.add(article);
		// 设置图文消息个数
		newsMessage.setArticleCount(articleList.size());
		// 设置图文消息包含的图文集合
		newsMessage.setArticles(articleList);
		// 将图文消息对象转换成xml字符串
		return MessageUtil.newsMessageToXml(newsMessage);
	}
	//消息群发
	public static String send(NewsMessage newsMessage,News news) {
		List<Article> articleList = new ArrayList<Article>();
		Article article = new Article();
		article.setTitle("中赤信息技术欢迎您！");
		article.setDescription("从事教育行业产品的研发，针对中小学管理系统的研发，目前主要产品有：学前教育管理系统，全媒体资源管理系统，学校多媒体教室一体化建设，学校网站建设等！");
		article.setPicurl("http://zhongchiedu.com/wechat-app/assets/1.png");
		article.setUrl("http://zhongchiedu.com/wechat-app/assets/1.png");
		articleList.add(article);
		// 设置图文消息个数
		newsMessage.setArticleCount(articleList.size());
		// 设置图文消息包含的图文集合
		newsMessage.setArticles(articleList);
		
		CustomMessage cus = new CustomMessage();
		cus.setMsgtype(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
		cus.setNews(newsMessage);
		cus.setTouser("ooiMKv7cqR-2EgkeC9LdATpr-mbY");
		// 将图文消息对象转换成xml字符串
		return  JSONObject.fromObject(cus).toString();
	}
	
	
	


	// 检查当前的微信用户是否已经绑定
//	public static WeChatUser checkUserBinding(String userToken) {
//		Query query = new Query();
//		query.addCriteria(Criteria.where("weChatId").is(userToken.trim()));
//		return weChatUserDao.findOneByQuery(query);
//	}

//	// 家校通知.
//	public static String homeSchollNotifcation(String schoolId,
//			NewsMessage newsMessage) {
//		List<T_notifications> list = selectSchollNotifcation(schoolId, "1");
//		if (list != null) {
//			return sendMessage(list, newsMessage,"1");
//		} else {
//			// 未发布通知.
//			return null;
//		}
//	}

	// 校内通知.
//	public static  String inSchollNotifcation(NewsMessage newsMessage) {
//		Pagination<Notification>  pageList=notificationService.findPaginationByQuery(new Query(), 1, 5, Notification.class);
//		if(pageList!=null){
//			return sendMessage(pageList.getDatas(), newsMessage,"2");
//		}else{
//			return null;
//		}
//		
//	}
	
	//学校简介
//	public static String SchoolInfo(NewsMessage newsMessage,String title,String titleurl,int newid){
//		try {
//			JDBCSQL dcm = new JDBCSQL();
//			Connection conn = dcm.getConnection();
//			String sql="select top 5 id,subject,min(linkurl) as titfrom from eduforummsg" +
//					" where id="+newid+" group by id,subject";
////			String sql = "select top 5 * from eduforummsg where id="+newid;
//			List<NewsPojo> list=dcm.selectNewsList(conn, sql);
//			
//			
//			if(list!=null){
//				NewsPojo np=null;
//			for(int i=0;i<list.size();i++){
//				np=list.get(i);
//				break;
//			}
//			
//			List<Article> articleList = new ArrayList<Article>();
//			Article article = new Article();
//			article.setTitle(np.getTitle());
//			article.setDescription("上海市建平实验小学，其前身是上海市建平实验学校，创建于1999年7月，当初是九年一贯制学校，" +
//					"随着办学声誉的提高及社区规模的发展，到2007年，学校已经发展为一所拥有110个教学班，5000多名学生，近300名教职员工的大型学校。" +
//					"为了学校更好的发展2008年6月，上级部门将学校一分为二，原来的小学部独立建制办学，为上海市浦东新区建平实验小学。");
//			article.setPicUrl(c.getServiceAddress()+"weChatWeb/weChatImg/wlcom.jpg");
//			article.setUrl(c.getServiceAddress()+"weChat!schoolNewsDetail.do?id=106");//106是学校信息
//			articleList.add(article);
//			// 设置图文消息个数
//			newsMessage.setArticleCount(articleList.size());
//			// 设置图文消息包含的图文集合
//			newsMessage.setArticles(articleList);
//			// 将图文消息对象转换成xml字符串
//			return MessageUtil.newsMessageToXml(newsMessage);
//			
//			}
//			return null;
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return null;
//		
//	}
	
	
	
//	// 学校新闻
//	public static String news(NewsMessage newsMessage,String title,String titleurl,int newid) {
//		List<NewsPojo> list = selectnews(newid);
//		System.out.println("数据库查询成功");
//		if (list != null) {
//			return sendMessageNews(list, newsMessage, title, titleurl,newid);
//		} else {
//			// 未发布通知.
//			return null;
//		}
//	}

			
//	public static void main(String[] args) {
//		selectSchollNotifcation("5224006f11867e25e401329d","2");
//	}
	// 查找最新的3-5条信息.
//	public static List<T_notifications> selectSchollNotifcation(
//			String SchoolId, String type) {
//		Query query = new Query();
//		query.addCriteria(Criteria.where("schoolid").is(SchoolId.trim()));
//		query.addCriteria(Criteria.where("_isdeleted").is(false));
//		// type 1:家校知通(KEY:4),2:校内通知
//		if (type.equals("1"))
//			query.addCriteria(Criteria.where("reciverrole").is(4));
//		else
//			query.addCriteria(Criteria.where("reciverrole").in(1, 2, 3, 5));
//		List<T_notifications> l = notification.find(query);
//		if (l != null) {
//			return l;
//		}
//		return null;
//	}
	
	//发送信息.   news 表：学校简介    教师发，  校园新闻
//		public static String sendMessageNews(List<News> listNotif,
//				NewsMessage newsMessage,String name,String id, boolean superid) {
//			List<Article> articleList = new ArrayList<Article>();
//			
//				Article arttitle = new Article();  
//				arttitle.setTitle(name);  
//				arttitle.setPicUrl(address+"app-web-portal/assets/weChatImg/buld.jpg");
//				if(superid){//多级子菜单
//					arttitle.setUrl(address+"app-web-portal/weChat/application/newslist?id="+id+"&superid=trues");// 跳转的地址.
//				}else{
//					arttitle.setUrl(address+"app-web-portal/weChat/application/newslist?id="+id+"&superid=flases");// 跳转的地址.
//				}
//				articleList.add(arttitle);
//	        
//	        
//			for (int i = 0; i < listNotif.size(); i++) {
//				if (i < 5) {// 只加载最新的三条信息.
//					News t = listNotif.get(i);
//					Article art = new Article();
//					art.setTitle(t.getTitle());
//					art.setDescription(t.getContent());
//					if(t.getBananerPic()==null){
//						art.setPicUrl(address+"app-web-portal/assets/weChatImg/inc/in.png");
//					}else{
//						art.setPicUrl(address+"app-web-portal/files/"+t.getBananerPic());// 图片.
//					}
//						
//					art.setUrl(address+"app-web-portal/weChat/application/newsDetail?id="+t.getId());// 跳转的地址.
//					articleList.add(art);
//				} else
//					break;
//			}
//			// 设置图文消息个数
//			newsMessage.setArticleCount(articleList.size());
//			// 设置图文消息包含的图文集合
//			newsMessage.setArticles(articleList);
//			// 将图文消息对象转换成xml字符串
//			return MessageUtil.newsMessageToXml(newsMessage);
//
//		}
//	
//	
//	//发送信息.   学校通知
//	public static String sendMessage(List<Notification> listNotif,
//			NewsMessage newsMessage,String type) {
//		List<Article> articleList = new ArrayList<Article>();
//		
//			Article arttitle = new Article();  
//			arttitle.setTitle("校内通知");  
//			arttitle.setDescription("校园内部通知及时请求通知");
//			arttitle.setPicUrl(address+"app-web-portal/assets/weChatImg/class_Q.jpg");
//			
//        articleList.add(arttitle);
//        
//		for (int i = 0; i < listNotif.size(); i++) {
//			if (i < 3) {// 只加载最新的三条信息.
//				Notification t = listNotif.get(i);
//				Article art = new Article();
//				art.setTitle(t.getTitle());
//				art.setDescription(t.getContent());
//				art.setPicUrl(address+"app-web-portal/assets/weChatImg/inc/home.png");// 图片.
////				art.setUrl(address+"weChatNot!findNotifcationDetailed.do?id="+t.getId());// 跳转的地址.//TODO
//				articleList.add(art);
//			} else
//				break;
//		}
//		// 设置图文消息个数
//		newsMessage.setArticleCount(articleList.size());
//		// 设置图文消息包含的图文集合
//		newsMessage.setArticles(articleList);
//		// 将图文消息对象转换成xml字符串
//		return MessageUtil.newsMessageToXml(newsMessage);
//
//	}
//	
//	
//	// 提示用户绑定.
//	public static String UserBingding(NewsMessage newsMessage,String userToken) {
//		List<Article> articleList = new ArrayList<Article>();
//		Article article = new Article();
//		article.setTitle("奉贤区四团小学欢迎您欢迎您");
//		article.setDescription("您需要先绑定才能请求通知.(点击绑定)");
//		article.setPicUrl(address+"app-web-portal/assets/weChatImg/wlcom.jpg");
//		article.setUrl(address+"weChat!alertLoadLogin.do?userToken="+userToken);
//		articleList.add(article);
//		// 设置图文消息个数
//		newsMessage.setArticleCount(articleList.size());
//		// 设置图文消息包含的图文集合
//		newsMessage.setArticles(articleList);
//		// 将图文消息对象转换成xml字符串
//		return MessageUtil.newsMessageToXml(newsMessage);
//	}
//
//	
//	
//	/**
//	 * /发送信息.学校新闻信息
//	*@author   此方法为成员属性方法,作者:Aaron Liu
//	*@version  版本:v1.0版本 
//	*@see      对类、属性、方法的说明 参考转向，也就是相关主题 
//	*@param listNotif
//	*@param newsMessage
//	*@param type
//	*@return
//	*@return    返回值,请具体看类代码
//	*@exception
//	 */
////	public static String sendMessageNews(List<NewsPojo> listNotif,
////			NewsMessage newsMessage,String title,String url,Integer id) {
////		List<Article> articleList = new ArrayList<Article>();
////		
////		Article arttitle = new Article();  
////			arttitle.setTitle(title);  
////			//arttitle.setDescription("学校与家长的沟通桥梁");  
////			String frestImg=listNotif.get(0).getImg();
////			if(frestImg!=null && !frestImg.equals("") && frestImg.length()>0)
////				arttitle.setPicUrl(frestImg); 
////			else
////				arttitle.setPicUrl(c.getServiceAddress()+"weChatWeb/weChatImg/buld.jpg");  
//////            if(url==null)
////            	arttitle.setUrl(c.getServiceAddress()+"weChat!schoolNewsList.do?superid="+id);
//////            else
//////            	arttitle.setUrl(c.getServiceAddress()+"weChatNot!findNotifcationList.do?type=1");
//////		
////        articleList.add(arttitle);
////        
////		for (int i = 0; i < listNotif.size(); i++) {
////				NewsPojo t = listNotif.get(i);
////				Article art = new Article();
////				art.setTitle(t.getTitle());
////				art.setDescription(t.getContent());
////				if(t.getImg()!=null && !t.getImg().equals("") && t.getImg().length()>0)
////					art.setPicUrl(t.getImg());// 图片.
////				else
////					art.setPicUrl(c.getServiceAddress()+"weChatWeb/weChatImg/inc/home.png");// 图片.
////				//weChatNot!findNotifcationDetailed.do?id=54e0192411867e97ec211e67
////				art.setUrl(c.getServiceAddress()+"weChat!schoolNewsDetail.do?id="+t.getId());// 跳转的地址.//TODO
////				articleList.add(art);
////		}
////		// 设置图文消息个数
////		newsMessage.setArticleCount(articleList.size());
////		// 设置图文消息包含的图文集合
////		newsMessage.setArticles(articleList);
////		// 将图文消息对象转换成xml字符串
////		System.out.println("推送信息成功");
////		return MessageUtil.newsMessageToXml(newsMessage);
////
////	}
//	
//	
//	
//	
//	//Util 查找对应的菜单
//
//	public static List<PermissionRole> loadPermissionRoleList(){
//		Query query =new Query();
//		query.addCriteria(Criteria.where("roleName").is("WeChat"));
//		return  permissionRoleService.find(query,
//				PermissionRole.class);
//	}
//	
//	
//	//获取页面需要查询的集合数据
//	public static  List<WechatMenuWeb> getRoleMenuForWechat(){
//		
//		List<PermissionRole> listRole=loadPermissionRoleList();
//		//获取菜单数据
//		List<WechatMenuWeb> listMenuId=new ArrayList<WechatMenuWeb>();
//		log.info("select list Menu:"+listRole.toString());
//		for (PermissionRole pr : listRole) {
//			for (PermissionMenu pm : pr.getListMenu()) {
//					for (PermissionSubMenu psmsuper : pm.getListPermissionSubMenu()) {
//					WechatMenuWeb wmb = new WechatMenuWeb();
//					wmb.setId(psmsuper.getId());
//					wmb.setName(psmsuper.getMenuName());
//					wmb.setSuperid(false);//父菜单，需要子菜单
//					listMenuId.add(wmb);// 获取这几个菜单的ID
//					
//				}
//			}
//		}
//		log.info("获取的菜单:"+listMenuId.toString());
//		return listMenuId;
//	}
//	

}
