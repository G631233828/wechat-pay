package zhongchiedu.common.utils;
import java.io.IOException;
import java.net.MalformedURLException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;



public class ReptilianTool {
	
	/** 登录页面 */
//	private static  String LOGIN_URL = "http://www.fushanedu.cn/jxq/jxq.aspx";
//	private static  String URL= "http://www.fushanedu.cn";
	/** 任务列表页面 */
//	private static  String TASK_LIST_URL = "http://www.fushanedu.cn/jxq/jxq_User.aspx";

	/***http://www.fushanedu.cn/jxq/jxq_User_jtzyck.aspx
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		String LOGIN_URL = "http://www.fushanedu.cn/jxq/jxq.aspx";
		String URL= "http://www.fushanedu.cn";
		String HOMEWORK_URL = "http://www.fushanedu.cn/jxq/jxq_User_jtzyck.aspx";
		String usernameId = "login_tbxUserName";
		String username = "20160101";
		String passwordId = "login_tbxPassword";
		String password = "197905";
		String loginBtnId = ".//*[@id='login_btnlogin']";
		String checkLoginId ="login_lblmsg";
		String login = checkLogin(LOGIN_URL, HOMEWORK_URL, usernameId, username, passwordId, password, loginBtnId, checkLoginId);
		System.out.println(login);
	}

	
	/**
	 * 执行登陆判断
	 * @param LOGIN_URL
	 * @param HOMEWORK_URL
	 * @param usernameId
	 * @param username
	 * @param passwordId
	 * @param password
	 * @param loginBtnId
	 * @return
	 * @throws FailingHttpStatusCodeException
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public static HtmlPage LoginAndGetHtml(String LOGIN_URL,String HOMEWORK_URL,String usernameId,
			String username ,String passwordId,String password,String loginBtnId) throws FailingHttpStatusCodeException, MalformedURLException, IOException{
		final WebClient webClient = new WebClient(BrowserVersion.CHROME);//使用谷歌浏览器内核
		   webClient.getOptions().setThrowExceptionOnScriptError(false);
	       webClient.getOptions().setCssEnabled(false);
	       webClient.getOptions().setJavaScriptEnabled(false);
	       webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
	       webClient.getOptions().setTimeout(30000);
	       // 获取登陆
			HtmlPage page = (HtmlPage) webClient.getPage(LOGIN_URL);
			HtmlInput in = page.getHtmlElementById(usernameId);
			HtmlInput pass = page.getHtmlElementById(passwordId);
		    HtmlInput btn = page.getFirstByXPath(loginBtnId);
		    in.setAttribute("value", username);
			pass.setAttribute("value",password);
			btn.click();
			webClient.close();
			return  webClient.getPage(HOMEWORK_URL);
	}
	
	/**
	 * 
	 * @param LOGIN_URL      登陆链接地址 
	 * @param HOMEWORK_URL   登陆成功跳转页面
	 * @param usernameId     账号id
	 * @param username       账号
	 * @param passwordId     密码id
	 * @param password       密码
	 * @param loginBtnId     登陆按钮id
	 * @param checkLoginId   校验登陆成功
	 * @return
	 * @throws IOException 
	 * @throws MalformedURLException 
	 * @throws FailingHttpStatusCodeException 
	 */
	public static String checkLogin(String LOGIN_URL,String HOMEWORK_URL,String usernameId,
			String username ,String passwordId,String password,String loginBtnId,String checkLoginId) throws FailingHttpStatusCodeException, MalformedURLException, IOException{
		
		HtmlPage page = LoginAndGetHtml(LOGIN_URL, HOMEWORK_URL, usernameId, username, passwordId, password, loginBtnId);
		
				String checklogin = page.getElementById(checkLoginId).asText();
				if(checklogin.length()==0||checklogin.contains("错误")){
					//登陆失败
					return "error";
				}else{
					//返回html
					return page.asXml();
				}
	}
	
	

}
