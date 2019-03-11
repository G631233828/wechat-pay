package zhongchiedu.service.executor;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import zhongchiedu.wechat.templates.schoolnotifcation.SchoolTemplateMessage;

@Repository
public class ExecutorsWechatNotice {

	
	
	private static final Logger log = LoggerFactory.getLogger(ExecutorsWechatNotice.class);
	

	BlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(5000);

	ExecutorService executor = new ThreadPoolExecutor(20, 20, 20,
			TimeUnit.MINUTES, queue);


	public void Executor(String openId,String token,SchoolTemplateMessage school) {
		log.info("发送通知");
		this.executor.execute(new WechatNoticeTask(openId,token,school,queue));
		
	}

	
}
