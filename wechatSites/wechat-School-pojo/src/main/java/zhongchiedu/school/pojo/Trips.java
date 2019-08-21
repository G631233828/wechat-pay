package zhongchiedu.school.pojo;

import org.springframework.data.mongodb.core.mapping.DBRef;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import zhongchiedu.framework.pojo.GeneralBean;

/**
 * 行程类
 * @author fliay
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(callSuper=false)
public class Trips extends GeneralBean<Trips> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8402632545596888540L;
	@DBRef
	private Activitys activitys;
	@DBRef
	private Sites sites;
	private double distance;//与上一站距离
	private int sorts;//排序
	
	
	
	
	
	
	
	
	
	

}
