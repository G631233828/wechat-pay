package zhongchiedu.school.util;

import lombok.Getter;
import lombok.Setter;

/**
 * ztree控件对应的ajax对象
 * @author cd
 *
 */

public class ZtreeNode {
	public ZtreeNode() {
		this.nocheck=false;
	}
	//自身id
	@Getter
	@Setter
	private String id;
    //父id，父节点为0
	private String pId;
    //节点文本
	@Getter
	@Setter
	private String name;
    //是否为父节点
	@Setter
    private boolean isParent;

    //是否选中
    @Getter
    @Setter
    private boolean checked;
    
    //是否有勾选框
    @Getter
    @Setter
    private boolean nocheck;
    
    public boolean getIsParent() {
    	return isParent;
    }

	public String getpId() {
		return pId;
	}

	public void setpId(String pId) {
		this.pId = pId;
	}
    
}
