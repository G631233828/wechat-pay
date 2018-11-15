package zhongchiedu.common.utils;



import java.io.Serializable;
import java.util.Date;

/**
 * 所有任务接口 其他任务必须继承访类
 * 
 * @author obullxl
 */
public abstract class Task implements Runnable, Serializable {
	private static final long serialVersionUID = 6033183192402850875L;

	// private static Logger logger = Logger.getLogger(Task.class);

	public Task() {
	}

	/**
	 * 任务执行入口
	 */
	public void run() {
		/**
		 * 相关执行代码
		 * 
		 * beginTransaction();
		 * 
		 * 执行过程中可能产生新的任务 subtask = taskCore();
		 * 
		 * commitTransaction();
		 * 
		 * 增加新产生的任务 ThreadPool.getInstance().batchAddTask(taskCore());
		 */
	}

	/**
	 * 任务信息
	 * 
	 * @return String
	 */
	public abstract String info();

}
