package zhongchiedu.wechat.pojo.createMenu;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import zhongchiedu.common.utils.Types.menuType;

/**
 * 按钮名称
 * @author fliay
 *
 */
@Getter
@Setter
@ToString
public class Button {
	private String name;
	private List sub_button;
	private menuType type;//button的类型
	private String url;//url链接
	private String key;
	
	
	
}
