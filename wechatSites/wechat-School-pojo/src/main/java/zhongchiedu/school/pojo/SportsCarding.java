package zhongchiedu.school.pojo;

import org.springframework.data.mongodb.core.mapping.DBRef;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import zhongchiedu.framework.pojo.GeneralBean;
import zhongchiedu.wechat.oauto2.NSNUserInfo;

/**
 * 运动打卡
 * @author fliay
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper=false)
public class SportsCarding extends GeneralBean<SportsCarding>{
	

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@DBRef
	private Student student;
	@DBRef
	private Clazz clazz;
	private NSNUserInfo nsnUserInfo; //绑定微信用户信息
	private Double distance;//运动距离
	private String sportsDate;
	
	
	
	
}
