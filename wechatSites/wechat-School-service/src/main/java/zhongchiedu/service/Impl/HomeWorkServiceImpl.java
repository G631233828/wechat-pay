package zhongchiedu.service.Impl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;

import zhongchiedu.common.utils.BasicDataResult;
import zhongchiedu.common.utils.ReptilianTool;
import zhongchiedu.school.pojo.HomeWork;
import zhongchiedu.service.HomeWorkService;

@Service
public class HomeWorkServiceImpl implements HomeWorkService {

	@Override
	/**
	 * 获取作业
	 */
	public String checkLogin(String username, String password) {

		String LOGIN_URL = "http://www.fushanedu.cn/jxq/jxq.aspx";
		String HOMEWORK_URL = "http://www.fushanedu.cn/jxq/jxq_User_jtzyck.aspx";
		String usernameId = "login_tbxUserName";
		String passwordId = "login_tbxPassword";
		String loginBtnId = ".//*[@id='login_btnlogin']";
		String checkLoginId = "login_lblmsg";
		String login = null;
		try {
			login = ReptilianTool.checkLogin(LOGIN_URL, HOMEWORK_URL, usernameId, username, passwordId, password,
					loginBtnId, checkLoginId);
		} catch (FailingHttpStatusCodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return login;
	}

	@Override
	public BasicDataResult getHomeWork(String username, String password) {
	
		String msg = this.checkLogin(username, password);
		if (msg.equals("error")) {
			return BasicDataResult.build(400, "用户名或密码错误", null);
		} else {
			// 使用jsoup开始解析

			System.out.println(this.jsoupGetHomeWork(msg));
		}
		return null;

	}
	
	@Override
	public HomeWork jsoupGetHomeWork(String page) {
		HomeWork homework = new HomeWork();
		String URL = "http://www.fushanedu.cn";
		Document doc = Jsoup.parse(page);

		String schoolName = doc.getElementById("SchoolName").text();
		String gradeName = doc.getElementById("GradeName").text();
		String classzz = doc.getElementById("ClassName").text();
		String content = doc.getElementById("MyMSG").text();
		String studentName =doc.getElementById("login_lblmsg").text();
		
		
		homework.setSchoolName(schoolName);
		homework.setGradeName(gradeName);
		homework.setClasszz(classzz);
		homework.setContent(content);
		homework.setStudentName(studentName.substring(0, studentName.indexOf("(")));
		System.out.println(2222);
		// 获得需要解析的节点
		Element e = doc.select("form table tbody tr:eq(1) table:eq(3) tbody tr td:eq(1) table").first();
		
		Document doc1 = Jsoup.parse(e.toString());
		
		String newdoc = e.toString();
		
		Elements links=doc1.select("a[href]");
		//将链接设置为全路径
		for(Element link:links){
			String oldLink = link.attr("href");
			String newLink = URL+oldLink;
			newdoc =newdoc.replace(oldLink, newLink);
		}
		//替换所有的<br>
		Document doc2 = Jsoup.parse(newdoc.toString());
		
		Map<String ,String> map = new HashMap<>();
		
		String homeworks[] = {"语文作业","数学作业","英语作业"};
		
		Elements elements = doc2.select("td");
		
		for(int i=0;i<elements.size();i++){
			for(int j=0;j<homeworks.length;j++){
				if(elements.get(i).text().equals(homeworks[j])){
					map.put(elements.get(i).text(), elements.get(i+1).html());
				}
			}
		}
		homework.setHomework(map);
		
		return homework;
	}
	
	
	
	public static void main(String[] args) {
		HomeWorkServiceImpl a = new HomeWorkServiceImpl();
		a.getHomeWork("20160101","1979015");
	}
	
	

}
