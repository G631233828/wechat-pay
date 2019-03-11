package zhongchiedu.service.executor;

import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import zhongchiedu.wechat.templates.schoolnotifcation.SchoolTemplateMessage;
import zhongchiedu.wechat.templates.schoolnotifcation.SwipingSchoolTemplate;

public class WechatNoticeTask implements Runnable {
	
	 private static final Logger log = LoggerFactory.getLogger(WechatNoticeTask.class);
		
	private String openId;
	private String token;
	private SchoolTemplateMessage school;
	BlockingQueue<Runnable> queue;
	
	public WechatNoticeTask(String openId, String token, SchoolTemplateMessage school,BlockingQueue<Runnable> queue) {
		this.openId=openId;
		this.token= token;
		this.school = school;
		this.queue = queue;
	}

	@Override
	public void run() {
		log.info("启用线程发送");
		SwipingSchoolTemplate te = new SwipingSchoolTemplate();
		te.swipingSchoolNotifcation(openId,token,school);
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.info("剩余推送数量"+queue.size());
	}

}
