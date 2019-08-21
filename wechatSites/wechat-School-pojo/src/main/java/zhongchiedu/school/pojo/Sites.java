package zhongchiedu.school.pojo;

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
	private float  x;//x坐标
	private float  y;//y坐标


}
