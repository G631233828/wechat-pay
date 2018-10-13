package zhongchiedu.school.pojo2;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import zhongchiedu.framework.pojo.GeneralBean;

//不需要
@Getter
@Setter
@ToString
@Document
public class Parent extends GeneralBean<Parent>{
	
	public Parent() {
	}
	
    private String name;// 姓名
    @DBRef
    private ClassRoom classRoom ;// 班级id
    private String cardId;// 身份证Id（证件号）
    private String code;// 学号    (用户登录帐号）
    private String address;// 居住地址
    private String contactsPerson;// 联系人
    private String mobile;// 联系手机   （用户登录密码）
}
