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

	@Override
	public void SaveOrUpdateWeChatbanding(WeChatbanding weChatbanding) {
		if (Common.isNotEmpty(weChatbanding)) {
			if (weChatbanding.getId() != null) {
				Query query = new Query();
				query.addCriteria(Criteria.where("openId").is(weChatbanding.getOpenId()));
				// 执行修改操作
				WeChatbanding ed = this.findOneByQuery(query, WeChatbanding.class);
				if (ed == null)
					ed = new WeChatbanding();
				weChatbanding.setIsDisable(false);
				BeanUtils.copyProperties(weChatbanding, ed);
				this.save(ed);
			} else {
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
