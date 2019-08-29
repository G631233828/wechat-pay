package zhongchiedu.compent;

import java.time.LocalDate;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import lombok.extern.slf4j.Slf4j;
import zhongchiedu.service.ScheduleCardingStatisticsService;


@Slf4j
public class ScheduleTask implements Job {
	
	@Autowired
	private ScheduleCardingStatisticsService scheduleCardingStatisticsService;
	
//	//Test @Scheduled(cron = "0/50 * * * * ?")
//	@Scheduled(cron = "00 59 23 * * ?")
//	public void todoSchedule() {
//	
//	}


	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		log.info("开始执行定时任务"+LocalDate.now());
		this.scheduleCardingStatisticsService.autoStatistics();
		log.info("定时任务执行完毕");
		
		
	}
}
