package zhongchiedu.school.pojo;

import java.util.List;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import zhongchiedu.framework.pojo.GeneralBean;

/**
 * 打卡统计，用户每天系统自动统计每个班级今日打卡情况，并且当前站点
 * @author fliay
 *
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class CardingStatistics extends GeneralBean<CardingStatistics> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -438679435457964152L;
	private double totalMileage;//班级总里程
	private double allMileage;//总里程
	@DBRef
	private Clazz clazz;//班级
	@DBRef
	private Sites site;//当前里程所在站点
	@DBRef
	private Activitys activitys;//对应活动
	@DBRef
	private List<Trips> trips;
	private int cardingStudent;//打卡学生数
	private String cardingDate;//记录日期
	private List<SportsCarding> studentSportsCarding;//统计所有学生的运动排名
	@Transient
	List<Sites> sites;//所有站点
	
	
	
	

}
