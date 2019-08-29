package zhongchiedu.compent;

import org.quartz.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import zhongchiedu.service.ScheduleCardingStatisticsService;


@Slf4j
@Component
public class ScheduleTask  {
	
	@Autowired
	private ScheduleCardingStatisticsService scheduleCardingStatisticsService;
	
	@Scheduled(cron = "0/2 * * * * ?")
	//@Scheduled(cron = "00 59 23 * * ?")
	public void todoSchedule() {
		System.out.println(scheduleCardingStatisticsService);
		scheduleCardingStatisticsService.autoStatistics();
	}


//	@Override
//	public void execute(JobExecutionContext arg0) throws JobExecutionException {
//		log.info("开始执行定时任务"+LocalDate.now());
//		this.scheduleCardingStatisticsService.autoStatistics();
//		log.info("定时任务执行完毕");
//		
//		
//	}
}
