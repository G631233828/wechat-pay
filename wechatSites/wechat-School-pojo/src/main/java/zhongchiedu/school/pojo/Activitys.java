package zhongchiedu.school.pojo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import zhongchiedu.framework.pojo.GeneralBean;

/**
 * 活动列表
 * @author fliay
 *
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class Activitys extends GeneralBean<Activitys> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8611510320920045417L;
	private String activityName;//活动名称（例如：响应改革开发70周年）
	private String author;//活动创建者
	private List<Trips> trips;
	private double upperLimit;//每日上限
	
	
}
