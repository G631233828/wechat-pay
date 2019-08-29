//package zhongchiedu.config;
//
//import org.quartz.SchedulerException;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
//import org.springframework.scheduling.quartz.JobDetailFactoryBean;
//import org.springframework.scheduling.quartz.SchedulerFactoryBean;
//
//import zhongchiedu.compent.ScheduleTask;
//
///**
// * Quartz配置类
// * 
// * @author fliay
// *
// */
//@Configuration
//public class QuartzConfig {
//
//	/**
//	 * 1.创建job对象 2.创建Trigger对象 3.创建scheduler对象
//	 */
//
//	@Bean
//	public JobDetailFactoryBean jobDetail() {
//		JobDetailFactoryBean factory = new JobDetailFactoryBean();
//		//关联job类
//		factory.setJobClass(ScheduleTask.class);
//		return factory;
//	}
//
//	/**
//	 * 创建简单Scheduler
//	 * @param jobDetail
//	 * @return
//	 */
////	@Bean
////	public SimpleTriggerFactoryBean trigger(JobDetailFactoryBean jobDetail) {
////		SimpleTriggerFactoryBean factory = new SimpleTriggerFactoryBean();
////		//关联jobdetail对象
////		factory.setJobDetail(jobDetail.getObject());
////		//设置触发次数
////		factory.setRepeatCount(5);
////		//设置触毫秒数
////		factory.setRepeatInterval(2000);
////		return factory;
////	}
//	
//	@Bean
//	public CronTriggerFactoryBean trigger(JobDetailFactoryBean jobDetail){
//		CronTriggerFactoryBean factory = new CronTriggerFactoryBean();
//		factory.setJobDetail(jobDetail.getObject());
//		factory.setCronExpression("0/20 * * * * ?");
//		return factory;
//	}
//	
//
//	@Bean
//	public SchedulerFactoryBean scheduler(CronTriggerFactoryBean tr,MyAdaptableJobFactory myAdaptableJobFactory) throws SchedulerException {
//		SchedulerFactoryBean factory = new SchedulerFactoryBean();
//		factory.setTriggers(tr.getObject());
//		factory.setJobFactory(myAdaptableJobFactory);
//		return factory;
//	}
//
//}
