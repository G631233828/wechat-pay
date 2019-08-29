/**package zhongchiedu.config;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.scheduling.quartz.AdaptableJobFactory;
import org.springframework.stereotype.Component;

@Component
public class MyAdaptableJobFactory extends AdaptableJobFactory {

	// 将对象添加到SpringIOC容器中，并且完成对象属性注入
	@Autowired
	private AutowireCapableBeanFactory autowireCapableBeanFactory;

	/**
	 * 该方法需要实例化的任务对象，手动添加到SpringIoc容器中，并且完成注入
	

	@Override
	protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {

		Object obj = super.createJobInstance(bundle);
		this.autowireCapableBeanFactory.autowireBean(obj);
		return obj;

	}

}**/