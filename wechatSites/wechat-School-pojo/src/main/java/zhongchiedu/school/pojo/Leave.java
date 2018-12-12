package zhongchiedu.school.pojo;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import zhongchiedu.framework.pojo.GeneralBean;
import zhongchiedu.wechat.oauto2.NSNUserInfo;

/**
 * 请假
 * 
 * @author fliay
 *
 */
@Getter
@Setter
@ToString
@Document
public class Leave extends GeneralBean<Leave> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5826480145727576109L;
	@DBRef
	private Student student;
	@DBRef
	private Clazz clazz;
	private int leaveDays;// 请假时间
	private String startLeave;// 请假开始时间
	private String reason;// 请假原因
    private boolean state = false;// 查看状态， true已读，false未读
    private boolean cancel = false;//取消状态     true取消，false未取消
	private NSNUserInfo nsnUserInfo; //绑定微信用户信息
}
