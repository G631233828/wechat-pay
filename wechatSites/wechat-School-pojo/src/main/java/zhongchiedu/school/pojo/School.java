package zhongchiedu.school.pojo;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import zhongchiedu.framework.pojo.GeneralBean;

@Getter
@Setter
@ToString
@Document
public class School extends GeneralBean<School> {

	private static final long serialVersionUID = 5419852964729823329L;

	public School() {
	}
	// 学校全名（企业名称）
	private String name;
	// 联系人
	private String contact;
	private String shortName;// 学校简称
	private String address;// 学校地址
	private String contactName;// 联系人
	private String contactNumber;// 联系电话
	private String email;// 邮箱地址
	private String province;// 省
	private String city;// 市
	private String area;// 区
	// 微信appid
	private String appid;
	// 微信appSecret
	private String appSecret;
	private String doMainName;//域名
	private String record;//公安备案号
	private String icpRecord;//ICP备案号
	private String serviceQQ;//服务QQ
	private String telPhone;//座机
	private String fax;//传真
	private String qRcode;//企业二维码
	private String logo;//学校logo

}
