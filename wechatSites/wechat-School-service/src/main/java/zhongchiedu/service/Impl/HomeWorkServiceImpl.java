package zhongchiedu.service.Impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import zhongchiedu.common.utils.Common;
import zhongchiedu.common.utils.ReptilianTool;
import zhongchiedu.school.pojo.HomeWork;
import zhongchiedu.service.HomeWorkService;

@Service
public class HomeWorkServiceImpl implements HomeWorkService {

	@Override
	/**
	 * 获取作业
	 */
	public String checkLogin(String username, String password, String date) {

		String LOGIN_URL = "http://www.fushanedu.cn/jxq/jxq.aspx";
		String HOMEWORK_URL = "http://www.fushanedu.cn/jxq/jxq_User_jtzyck.aspx";
		String usernameId = "login_tbxUserName";
		String passwordId = "login_tbxPassword"; 
		String loginBtnId = ".//*[@id='login_btnlogin']";
		String checkLoginId = "login_lblmsg";
		String page = null;
		try {
			page = ReptilianTool.checkLogin(LOGIN_URL, HOMEWORK_URL, usernameId, username, passwordId, password,
					loginBtnId, checkLoginId,date);
			
			
		} catch (FailingHttpStatusCodeException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return page;

	}

	@Override
	public HomeWork jsoupGetHomeWork(String page) {
		HomeWork homework = new HomeWork();
		String URL = "http://www.fushanedu.cn";
		Document doc = Jsoup.parse(page);

		String schoolName = doc.getElementById("SchoolName").text();
		String gradeName = doc.getElementById("GradeName").text();
		String className = doc.getElementById("ClassName").text();
		String content = doc.getElementById("MyMSG").text();
		String studentName = doc.getElementById("login_lblmsg").text();

		homework.setSchoolName(schoolName);
		homework.setGradeName(gradeName);
		homework.setClassName(className);
		homework.setContent(content);
		homework.setDate(content.substring(content.indexOf("-") - 4, content.lastIndexOf("-") + 3));
		homework.setStudentName(studentName.substring(0, studentName.indexOf("(")));
		// 获得需要解析的节点
		Element e = doc.select("form table tbody tr:eq(1) table:eq(3) tbody tr td:eq(1) table").first();

		Document doc1 = Jsoup.parse(e.toString());

		String newdoc = e.toString();

		Elements links = doc1.select("a[href]");
		// 将链接设置为全路径
		for (Element link : links) {
			String oldLink = link.attr("href");
			String newLink = URL + oldLink;
			if(oldLink.contains(URL)){
				continue;
			}
			newdoc = newdoc.replace(oldLink, newLink);
		}
		// 替换所有的<br>
		Document doc2 = Jsoup.parse(newdoc.toString());

		Map<String, String> map = new HashMap<>();

		String homeworks[] = { "语文作业", "数学作业", "英语作业" };

		Elements elements = doc2.select("td");

		for (int i = 0; i < elements.size(); i++) {
			for (int j = 0; j < homeworks.length; j++) {
				if (elements.get(i).text().equals(homeworks[j])) {
					map.put(elements.get(i).text(), elements.get(i + 1).html());
				}
			}
		}
		homework.setHomework(map);

		return homework;
	}

	public static void main(String[] args) throws UnsupportedEncodingException {
		HomeWorkServiceImpl a = new HomeWorkServiceImpl();
		String aa = a.checkLogin("20170110", "Renqin771023", "2018-10-23");
		HomeWork work = a.jsoupGetHomeWork(aa);
		System.out.println(work.getClassName());
		System.out.println(work.getGradeName());
		System.out.println(work.getStudentName());
		
	}

}
