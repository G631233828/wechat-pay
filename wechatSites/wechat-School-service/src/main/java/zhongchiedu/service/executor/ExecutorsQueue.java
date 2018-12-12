package zhongchiedu.service.executor;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import zhongchiedu.school.pojo.News;
import zhongchiedu.wechat.resp.NewsMessage;
import zhongchiedu.wechat.util.accessToken.AccessToken;

@Repository
public class ExecutorsQueue {

	private static final Logger log = LoggerFactory.getLogger(ExecutorsQueue.class);

	BlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(20000);

	ExecutorService executor = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(), 20, 20,
			TimeUnit.MINUTES, queue);

	public void Executor(NewsMessage newsMessage,News news,String openId,String serverUrl,String contextpath,AccessToken at) {

		log.info("当前队列大小：" + queue.size());

		this.executor.execute(new Task( newsMessage, news, openId, serverUrl, contextpath, at));
	}

}
