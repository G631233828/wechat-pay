package zhongchiedu.service;

import org.springframework.web.multipart.MultipartFile;

import zhongchiedu.common.utils.BasicDataResult;
import zhongchiedu.framework.service.GeneralService;
import zhongchiedu.school.pojo.News;
import zhongchiedu.service.executor.ExecutorsQueue;

public interface NewsService  extends GeneralService<News>{

	 void SaveOrUpdateNews(News news, MultipartFile[] filenews, String oldnewsImg, String path,String dir,String editorValue);
	 
	 BasicDataResult scheduledRelease(String time);

	 BasicDataResult newsDisable(String id);
	 
	 void release(News news,String serverUrl,String contextpath,ExecutorsQueue executorsQueue);
	 
	 void updateNewsVisit(String id); 
	 
	 
	
}
