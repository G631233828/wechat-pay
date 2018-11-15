package zhongchiedu.wechat.pojo.createMenu;

import zhongchiedu.common.utils.Types.menuType;

/**
 * 显示的button 
 * @author fliay
 *
 */
public class ViewButton extends Button {
	private menuType type;//button的类型
	private String url;//url链接


	public menuType getType() {
		return type;
	}

	public void setType(menuType type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
