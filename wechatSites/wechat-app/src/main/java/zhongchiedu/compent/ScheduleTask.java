package zhongchiedu.compent;

import java.time.LocalDate;

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
	
	//@Scheduled(cron = "0/20 * * * * ?")
@Scheduled(cron = "0 50 23 * * ?")
	public void todoSchedule() {
		log.info("开始统计数据" +LocalDate.now().toString());
		scheduleCardingStatisticsService.autoStatistics();
		log.info("统计完成");
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
