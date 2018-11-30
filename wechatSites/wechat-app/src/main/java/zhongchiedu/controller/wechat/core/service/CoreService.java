package zhongchiedu.controller.wechat.core.service;



import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


import zhongchiedu.wechat.resp.Article;
import zhongchiedu.wechat.resp.NewsMessage;
import zhongchiedu.wechat.resp.TextMessage;
import zhongchiedu.wechat.util.MessageUtil;

/**
 * 核心服务
 * @author fliay
 *
 */
public class CoreService {

	
	
	
	
	
	
	
	/**
	 * 处理微信发来的请求
	 * 
	 * @param request
	 * @return
	 */
	public static String processRequest(HttpServletRequest request) {
		String respMessage = null;
		try {
			// 默认返回的文本消息内容
			String respContent = "请求处理异常，请稍候尝试！";

			// xml请求解析
			Map<String, String> requestMap = MessageUtil.parseXml(request);

			// 发送方帐号（open_id）
//			String fromUserName = requestMap.get("FromUserName");
//			// 公众帐号
//			String toUserName = requestMap.get("ToUserName");
			// 消息类型
			String msgType = requestMap.get("MsgType");
			
			String fromUserName = "ooiMKv7cqR-2EgkeC9LdATpr-mbY";
			String toUserName ="gh_8f67927a39ba";

			// 回复文本消息
			TextMessage textMessage = new TextMessage();
			textMessage.setToUserName(fromUserName);
			System.out.println("fromUserName:" + fromUserName);
			textMessage.setFromUserName(toUserName);
			System.out.println("toUserName:" + toUserName);
			textMessage.setCreateTime(new Date().getTime());
			textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
			textMessage.setFuncFlag(0);

			List<Article> articleList = new ArrayList<Article>();

			// 文本消息
			if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {
				respContent = "您发送的是文本消息！";
			}
			// 图片消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_IMAGE)) {
				respContent = "您发送的是图片消息！";
			}
			// 地理位置消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LOCATION)) {
				respContent = "您发送的是地理位置消息！";
			}
			// 链接消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LINK)) {
				respContent = "您发送的是链接消息！";
			}
			// 音频消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VOICE)) {
				respContent = "您发送的是音频消息！";
			}
			// 事件推送
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {
				System.out.println("事件推送");
				// 事件类型
				String eventType = requestMap.get("Event");
				// 创建图文消息
				NewsMessage newsMessage = new NewsMessage();
				newsMessage.setToUserName(fromUserName);
				newsMessage.setFromUserName(toUserName);
				newsMessage.setCreateTime(new Date().getTime());
				newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
				newsMessage.setFuncFlag(0);
				// 订阅
				if (eventType.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) {
					// 返回订阅信息.
					return CoreServiceMessage.Subscribe(newsMessage);
				}
				// 取消订阅
				else if (eventType.equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) {
					// TODO 取消订阅后用户再收不到公众号发送的消息，因此不需要回复消息
				}
				// 自定义菜单点击事件
				else if (eventType.equals(MessageUtil.EVENT_TYPE_CLICK)) {
					System.out.println("go to message!");
					// 事件KEY值，与创建自定义菜单时指定的KEY值对应
					String eventKey = requestMap.get("EventKey");

					// 检查当前的用户是否已经绑定,1:如果没有绑定(提示用户需要绑定.)2.加载相关通知信息.
					//System.out.println(wechatUser.toString());
					/* if(eventKey.equals("11")){
						//微办公－校内通知
						 return CoreServic eMessage.inSchollNotifcation( newsMessage);
					}else if(eventKey.equals("30")){
						//学校简介－学校概况
						 return CoreServiceMessage.schoolProfile(newsMessage, "学校简介");
					}else if(eventKey.equals("31")){
						//教师发展
						 return CoreServiceMessage.schoolProfile(newsMessage, "教师发展");
					}else if(eventKey.equals("32")){
						//校园新闻
						 return CoreServiceMessage.schoolProfile(newsMessage, "校园新闻");
					}else if(eventKey.equals("33")){
						//德育经纬
						 return CoreServiceMessage.schoolProfile(newsMessage, "德育经纬");
					}else if(eventKey.equals("34")){
						//雏鹰成长
						 return CoreServiceMessage.schoolProfile(newsMessage, "雏鹰成长");
					}else */if(eventKey.equals("21")){
						//微互动 家校通知
						 respContent = "待上线－二期开发";
					}else if(eventKey.equals("22")){
						//问卷调查
						 respContent = "待上线－问卷调查";
					}else if(eventKey.equals("23")){
						//在线选课
						 respContent = "待上线－在线选课";
					}else if(eventKey.equals("23")){
						//班级社区
						 respContent = "待上线－班级社区";
					}else {
						 respContent = "未知";
					}
					 
					
				}
			}

			textMessage.setContent(respContent);
			respMessage = MessageUtil.textMessageToXml(textMessage);
		} catch (Exception e) {
			System.out.println(e.toString());
		}

		return respMessage;
	}

	/**
	 * emoji表情转换(hex -> utf-16)
	 * 
	 * @param hexEmoji
	 * @return
	 */
	public static String emoji(int hexEmoji) {
		return String.valueOf(Character.toChars(hexEmoji));
	}

	

}