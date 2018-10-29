package zhongchiedu.service.Impl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import zhongchiedu.common.utils.Common;
import zhongchiedu.framework.service.GeneralServiceImpl;
import zhongchiedu.school.pojo.WeChatbanding;
import zhongchiedu.service.WeChatbandingService;

@Service
public class WeChatbandingServiceImpl extends GeneralServiceImpl<WeChatbanding> implements WeChatbandingService {
	
	private static final Logger log = LoggerFactory.getLogger(WeChatbandingServiceImpl.class);

	/**
	 * 通过用户的openId来查看是否已经存在
	 */
	@Override
	public void SaveOrUpdateWeChatbanding(WeChatbanding weChatbanding) {
		if(Common.isNotEmpty(weChatbanding)){
			weChatbanding.setIsDisable(false);
			//通过openId判断是否存在数据，如果存在则执行修改
			Query query = new Query();
			query.addCriteria(Criteria.where("openId").is(weChatbanding.getOpenId()));
			WeChatbanding bd = this.findOneByQuery(query, WeChatbanding.class);
			if(Common.isNotEmpty(bd)){
				BeanUtils.copyProperties(weChatbanding, bd);
				this.save(bd);
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
		if(Common.isNotEmpty(openId)){
			Query query = new Query();
			query.addCriteria(Criteria.where("openId").is(openId));
	  		weChatbanding = this.findOneByQuery(query, WeChatbanding.class);
		}
		return weChatbanding!=null?weChatbanding:null;
	}


}
