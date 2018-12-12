package zhongchiedu.service.Impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import net.sf.json.JSONObject;
import zhongchiedu.common.utils.BasicDataResult;
import zhongchiedu.common.utils.Common;
import zhongchiedu.framework.service.GeneralServiceImpl;
import zhongchiedu.general.pojo.MultiMedia;
import zhongchiedu.general.service.MultiMediaService;
import zhongchiedu.school.pojo.News;
import zhongchiedu.service.NewsService;
import zhongchiedu.service.executor.ExecutorsQueue;
import zhongchiedu.wechat.resp.Article;
import zhongchiedu.wechat.resp.CustomMessage;
import zhongchiedu.wechat.resp.NewsMessage;
import zhongchiedu.wechat.resp.UserGet;
import zhongchiedu.wechat.util.MessageUtil;
import zhongchiedu.wechat.util.WeixinUtil;
import zhongchiedu.wechat.util.accessToken.AccessToken;
import zhongchiedu.wechat.util.token.WeChatToken;

@Service
public class NewsServiceImpl extends GeneralServiceImpl<News> implements NewsService {
	@Autowired
	private MultiMediaService multiMediaSerice;
	
	
	private static Logger log = (Logger) LoggerFactory.getLogger(NewsServiceImpl.class);

	@Override
	public void SaveOrUpdateNews(News news, MultipartFile[] filenews, String oldnewsImg, String path, String dir,String editorValue) {
		if (Common.isNotEmpty(news)) {
			News ed = null;
			// 上传图片
			List<MultiMedia> newsImg = this.multiMediaSerice.uploadPictures(filenews, dir, path, "NEWS");

			if (Common.isNotEmpty(news.getId())) {
				ed = this.findOneById(news.getId(), News.class);
				news.setViews(ed.getViews());
				if (Common.isNotEmpty(ed)) {
					news.setNewsImg(Common.isNotEmpty(oldnewsImg) ? ed.getNewsImg() : null);
				}
			}
			if (newsImg.size() > 0) {
				news.setNewsImg(newsImg.get(0));
			}
			if(Common.isNotEmpty(editorValue)){
				news.setContent(editorValue);
			}
			if (Common.isNotEmpty(ed)) {
				BeanUtils.copyProperties(news, ed);
				this.save(ed);
			} else {
				this.insert(news);
			}
		}

	}

	@Override
	public BasicDataResult scheduledRelease(String time) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BasicDataResult newsDisable(String id) {
		if (Common.isEmpty(id)) {
			return BasicDataResult.build(400, "无法禁用，请求出现问题，请刷新界面!", null);
		}
		News news = this.findOneById(id, News.class);
		if (news == null) {
			return BasicDataResult.build(400, "无法获取到新闻信息，该新闻可能已经被删除", null);
		}
		news.setIsDisable(news.getIsDisable().equals(true) ? false : true);
		this.save(news);
		return BasicDataResult.build(200, news.getIsDisable().equals(true) ? "新闻禁用成功" : "新闻启用成功", news.getIsDisable());
	}


	/**
	 * 通过新闻的id进行发布新闻
	 */
	@Override
	public void release(News news,String serverUrl,String contextpath,ExecutorsQueue executorsQueue) {
		// 通过新闻的id获取新闻信息
			if (Common.isNotEmpty(news)) {
				// 执行发布流程,获取接收者列表
				AccessToken at = WeChatToken.getInstance().getAccessToken();
				// 执行发布流程,获取接收者列表
				Object[] openids = this.findOpenIds(at);

				try{
					NewsMessage newsMessage = new NewsMessage();
					newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
					//创建线程池发送
					for (int i = 0; i < openids.length; i++) {
					executorsQueue.Executor(newsMessage, news, openids[i].toString(), serverUrl, contextpath, at);
					}
				}catch(Exception e) {
					e.printStackTrace();
				}
				
			}

	}

	//返回openid列表
	public Object[] findOpenIds(AccessToken at){
		
		// 获取到用户列表
		UserGet user = WeixinUtil.getusers(at.getToken(), null);

		Object[] openids = user.getData().getOpenid();
		int total = user.getTotal();
		if (user.getCount() > 10000) {

			for (int i = 0; i < total / 10000; i++) {
				user = WeixinUtil.getusers(at.getToken(), user.getNext_openid());
				if (Common.isNotEmpty(user)) {
					Object[] arr2 = user.getData().getOpenid();
					openids = Arrays.copyOf(openids, openids.length + arr2.length);
					System.arraycopy(arr2, 0, openids, openids.length, arr2.length);
				}
			}
		}
		return openids;
	}

	/**
	 * 访问量+1
	 */
	@Override
	public void updateNewsVisit(String id) {
	
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				updateVisitByThread(id);
			}
		}).start();

	}
	
	public synchronized void updateVisitByThread(String id) {
		News news = this.findOneById(id, News.class);
		if(Common.isNotEmpty(news)){
			news.setViews(news.getViews()+1);
			this.save(news);
		}
	}
	
	
	
	

}
