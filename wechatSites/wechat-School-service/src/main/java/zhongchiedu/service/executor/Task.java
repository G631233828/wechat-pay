package zhongchiedu.service.executor;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONObject;
import zhongchiedu.common.utils.Common;
import zhongchiedu.school.pojo.News;
import zhongchiedu.wechat.resp.Article;
import zhongchiedu.wechat.resp.CustomMessage;
import zhongchiedu.wechat.resp.NewsMessage;
import zhongchiedu.wechat.util.MessageUtil;
import zhongchiedu.wechat.util.WeixinUtil;
import zhongchiedu.wechat.util.accessToken.AccessToken;

public class Task implements Runnable {

	 private static final Logger log = LoggerFactory.getLogger(Task.class);
	
	 private NewsMessage newsMessage;
	 private News news;
	 private String openId;
	 private String serverUrl;
	 private String contextpath;
	 private AccessToken at;
	 
	 public Task(NewsMessage newsMessage,News news,String openId,String serverUrl,String contextpath,AccessToken at){
		 this.newsMessage=newsMessage;
		 this.news = news;
		 this.openId = openId;
		 this.serverUrl= serverUrl;
		 this.contextpath= contextpath;
		 this.at = at;
	 }
	 
	
	
	@Override
	public void run() {
		log.info("开始处理数据");
		String json = this.send(newsMessage, news,openId,serverUrl,contextpath);
		JSONObject j = WeixinUtil.send(at.getToken(), json);
		log.info("发送给"+openId+"发送返回结果"+j);
	}

	
	//将发送的数据转换成json
	public static String send(NewsMessage newsMessage, News news, String touser,String serverUrl,String contextpath) {

		List<Article> articleList = new ArrayList<Article>();
		Article article = new Article();
		article.setTitle(news.getTitle());
		article.setDescription(news.getDescription());
		if(Common.isNotEmpty(news.getNewsImg())){
			article.setPicurl(serverUrl+contextpath+"/"+ news.getNewsImg().getSavePath()+news.getNewsImg().getCompressPicName());
		}else{
			article.setPicurl("");
		}
		article.setUrl(serverUrl +contextpath+ "/news/view/" + news.getId());
		articleList.add(article);
		// 设置图文消息个数
		newsMessage.setArticleCount(articleList.size());
		// 设置图文消息包含的图文集合
		newsMessage.setArticles(articleList);
		CustomMessage cus = new CustomMessage();
		cus.setMsgtype(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
		cus.setNews(newsMessage);
		cus.setTouser(touser);
		// 将图文消息对象转换成xml字符串
		return JSONObject.fromObject(cus).toString();
	}
	
}
