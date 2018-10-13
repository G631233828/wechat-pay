package zhongchiedu.common.utils;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreadPool {

	private static final Logger LOG = LoggerFactory
			.getLogger(ThreadPool.class);
	private ExecutorService executorService;
	/* 默认池中线程数 */
	public static int worker_num = 5;
	private static ThreadPool instance = ThreadPool.getInstance();

	private ThreadPool() {
		// cpu数量+1
		worker_num = Runtime.getRuntime().availableProcessors() + 1;
		executorService = Executors.newFixedThreadPool(worker_num);
	}

	public static synchronized ThreadPool getInstance() {
		if (instance == null)
			return new ThreadPool();
		return instance;
	}

	public void addTask(Task newTask) {
		executorService.execute(newTask);
		LOG.info("线程池增加线程,线程描述: " + newTask.info());
	}

	public synchronized void destroy() {
		executorService.shutdown();
	}

}
