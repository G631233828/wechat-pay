package zhongchiedu.wechat.oauto2;


import java.util.List;

/**
 * 用户信息
 */
public class NSNUserInfo {
	private String openid;
	
	private String nickname;
	
	private int sex;
	
	private String country;
	
	private String province;
	
	private String city;
	
	private String headImgUrl;
	
	private List<String> privilegeList;

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getHeadImgUrl() {
		return headImgUrl;
	}

	public void setHeadImgUrl(String headImgUrl) {
		this.headImgUrl = headImgUrl;
	}

	public List<String> getPrivilegeList() {
		return privilegeList;
	}

	public void setPrivilegeList(List<String> privilegeList) {
		this.privilegeList = privilegeList;
	}

	@Override
	public String toString() {
		return "NSNUserInfo [city=" + city + ", country=" + country
				+ ", headImgUrl=" + headImgUrl + ", nickname=" + nickname
				+ ", openid=" + openid + ", privilegeList=" + privilegeList
				+ ", province=" + province + ", sex=" + sex + "]";
	}
	
	
	
	

}
