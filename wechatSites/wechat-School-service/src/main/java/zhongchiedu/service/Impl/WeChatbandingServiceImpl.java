package zhongchiedu.service.Impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import zhongchiedu.common.utils.Common;
import zhongchiedu.common.utils.Contents;
import zhongchiedu.framework.service.GeneralServiceImpl;
import zhongchiedu.school.pojo.WeChatbanding;
import zhongchiedu.school.pojo.WeChatbandingStudent;
import zhongchiedu.service.WeChatbandingService;
import zhongchiedu.wechat.oauto2.NSNUserInfo;
import zhongchiedu.wechat.util.WeixinUtil;

@Service
public class WeChatbandingServiceImpl extends GeneralServiceImpl<WeChatbanding> implements WeChatbandingService {

	private static final Logger log = LoggerFactory.getLogger(WeChatbandingServiceImpl.class);

	/**
	 * 通过用户的openId来查看是否已经存在
	 */
	@Override
	public void SaveOrUpdateWeChatbanding(WeChatbanding weChatbanding) {
		if (Common.isNotEmpty(weChatbanding)) {
			if (weChatbanding.getId() != null) {
				WeChatbanding bd = this.findOneById(weChatbanding.getId(), WeChatbanding.class);
				if(Common.isNotEmpty(bd)){
					BeanUtils.copyProperties(weChatbanding, bd);
					this.save(bd);
				}else{
					WeChatbanding ed = new WeChatbanding();
					BeanUtils.copyProperties(weChatbanding, ed);
					// 执行添加操作
					this.insert(ed);
				}
			}else{
				WeChatbanding ed = new WeChatbanding();
				BeanUtils.copyProperties(weChatbanding, ed);
				// 执行添加操作
				this.insert(ed);
			}
		}

	}

	@Override
	public WeChatbanding findWeChatbandingByOpenId(String openId) {
		WeChatbanding weChatbanding = null;
		if (Common.isNotEmpty(openId)) {
			Query query = new Query();
			query.addCriteria(Criteria.where("openId").is(openId));
			weChatbanding = this.findOneByQuery(query, WeChatbanding.class);
		}
		return weChatbanding != null ? weChatbanding : null;
	}

	
	
	@Override
	public NSNUserInfo findWechatNsn(String code) {
		NSNUserInfo nsn = WeixinUtil.baseWeChatLogin(Contents.APPID, Contents.APPSECRET, code);
		return Common.isNotEmpty(nsn) ? nsn : null;

	}

	/**
	 * 根据学生的账号查询
	 */
	@Override
	public void updateStudentName(WeChatbanding we, String newName) {
		Update update = new Update();
		update.set("studentName", newName);
//		update.set("listbandings.studentName", newName);
		Query query = new Query();
		query.addCriteria(Criteria.where("studentAccount").is(we.getStudentAccount()));
		this.updateAllByQuery(query, update, WeChatbanding.class);
		
		List<WeChatbanding> bds = this.find(query, WeChatbanding.class);
		
		for(WeChatbanding wchat:bds){
			List<WeChatbandingStudent> wlist = new ArrayList<>();
			
			for(WeChatbandingStudent stu:wchat.getListbandings()){
				
				if(stu.getStudentAccount().equals(we.getStudentAccount())){
					stu.setStudentName(newName);
				}
					wlist.add(stu);
			}
			
			wchat.setListbandings(wlist);
					this.save(wchat);
			
		}
	}

	@Override
	public NSNUserInfo findnsnByStudentAccount(String account) {
		Query query = new Query();
		query.addCriteria(Criteria.where("listbandings.studentAccount").is(account));
		WeChatbanding wb = this.findOneByQuery(query, WeChatbanding.class);
		return Common.isNotEmpty(wb)?wb.getNsnUserInfo():null;
	}
	
	
	

}
