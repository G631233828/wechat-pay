package zhongchiedu.school.pojo;

import org.springframework.data.mongodb.core.mapping.DBRef;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import zhongchiedu.framework.pojo.GeneralBean;

/**
 * 站点    与上海之间的距离 
 * @author fliay
 * x, y 坐标值  使用 http://api.map.baidu.com/lbsapi/createmap/index.html
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(callSuper=false)
public class Sites extends GeneralBean<Sites> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7248358740597274059L;
	
	private String siteName;//站点名称
	private double distance;//距离
	private float  x;//x坐标
	private float  y;//y坐标
	private String parendId;//上一个地点id  如果是出发地 则是0
	private String nextId;//下一个地点id    如果是终点下一个地点显示null
	private String sitesId;//出发地Id     如果是出发地则是0
	@DBRef
	private Activitys activitys;

}
