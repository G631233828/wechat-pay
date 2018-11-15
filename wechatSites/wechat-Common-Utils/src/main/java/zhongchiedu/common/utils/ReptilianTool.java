package zhongchiedu.common.utils;

import java.io.IOException;
import java.net.MalformedURLException;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class ReptilianTool {

	/** 登录页面 */
	// private static String LOGIN_URL = "http://www.fushanedu.cn/jxq/jxq.aspx";
	// private static String URL= "http://www.fushanedu.cn";
	/** 任务列表页面 */
	// private static String TASK_LIST_URL =
	// "http://www.fushanedu.cn/jxq/jxq_User.aspx";

	/***
	 * http://www.fushanedu.cn/jxq/jxq_User_jtzyck.aspx
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		String LOGIN_URL = "http://www.fushanedu.cn/jxq/jxq.aspx";
		String URL = "http://www.fushanedu.cn";
		String HOMEWORK_URL = "http://www.fushanedu.cn/jxq/jxq_User_jtzyck.aspx";
		String usernameId = "login_tbxUserName";
		String username = "20160101";
		String passwordId = "login_tbxPassword";
		String password = "197905";
		String loginBtnId = ".//*[@id='login_btnlogin']";
		String checkLoginId = "login_lblmsg";
		String target = "__EVENTTARGET";
		String argument = "__EVENTARGUMENT";
		String login = checkLogin(LOGIN_URL, HOMEWORK_URL, usernameId, username, passwordId, password, loginBtnId,
				checkLoginId,null);

		// System.out.println(login);
	}

	/**
	 * 执行登陆判断
	 * 
	 * @param LOGIN_URL
	 * @param HOMEWORK_URL
	 * @param usernameId
	 * @param username
	 * @param passwordId
	 * @param password
	 * @param loginBtnId
	 * @param date 
	 * @return
	 * @throws FailingHttpStatusCodeException
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	@SuppressWarnings("resource")
	public static HtmlPage LoginAndGetHtml(String LOGIN_URL, String HOMEWORK_URL, String usernameId, String username,
			String passwordId, String password, String loginBtnId, String date)
			throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		final WebClient webClient = new WebClient(BrowserVersion.CHROME);// 使用谷歌浏览器内核
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.getOptions().setUseInsecureSSL(true);// 接受任何主机连接 无论是否有有效证书
		webClient.getOptions().setCssEnabled(false); // 禁用css支持
		webClient.getOptions().setJavaScriptEnabled(true);// 设置支持javascript脚本
		webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
		webClient.getOptions().setTimeout(30000);
		// 获取登陆
		HtmlPage page = (HtmlPage) webClient.getPage(LOGIN_URL);
		if(Common.isEmpty(page.getHtmlElementById(usernameId))){
			return null;
		}
		
		HtmlInput in = page.getHtmlElementById(usernameId);
		HtmlInput pass = page.getHtmlElementById(passwordId);
		HtmlInput btn = page.getFirstByXPath(loginBtnId);
		in.setAttribute("value", username);
		pass.setAttribute("value", password);
		btn.click();

		if(Common.isNotEmpty(date)){
			long days = Common.getBetweenDays("2000-01-01", date);
			 HtmlPage page1 = webClient.getPage(HOMEWORK_URL);
			 HtmlAnchor a = (HtmlAnchor)
			 page1.getFirstByXPath("//a[@style='color:Black']");
			 a.setAttribute("href",  "javascript:__doPostBack('MyCalendar','"+days+"')");
			 HtmlPage page2 = a.click();
//			 webClient.close();
			 return page2;
		}
		
//		webClient.close();
		return webClient.getPage(HOMEWORK_URL);
	}

	/**
	 * 
	 * @param LOGIN_URL
	 *            登陆链接地址
	 * @param HOMEWORK_URL
	 *            登陆成功跳转页面
	 * @param usernameId
	 *            账号id
	 * @param username
	 *            账号
	 * @param passwordId
	 *            密码id
	 * @param password
	 *            密码
	 * @param loginBtnId
	 *            登陆按钮id
	 * @param checkLoginId
	 *            校验登陆成功
	 * @return
	 * @throws IOException
	 * @throws MalformedURLException
	 * @throws FailingHttpStatusCodeException
	 */
	public static String checkLogin(String LOGIN_URL, String HOMEWORK_URL, String usernameId, String username,
			String passwordId, String password, String loginBtnId, String checkLoginId,String date)
			throws FailingHttpStatusCodeException, MalformedURLException, IOException {

		HtmlPage page = LoginAndGetHtml(LOGIN_URL, HOMEWORK_URL, usernameId, username, passwordId, password,
				loginBtnId, date);
		String checklogin = page.getElementById(checkLoginId).asText();
		if (checklogin.length() == 0 || checklogin.contains("错误")) {
			// 登陆失败
			return "error";
		} else {
			// 返回html
			
			return page.asXml();
		}

	}

}
