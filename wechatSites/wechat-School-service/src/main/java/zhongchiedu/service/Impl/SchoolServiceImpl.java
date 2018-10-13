package zhongchiedu.service.Impl;



import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import zhongchiedu.common.utils.BasicDataResult;
import zhongchiedu.common.utils.Common;
import zhongchiedu.framework.service.GeneralServiceImpl;
import zhongchiedu.school.pojo.School;
import zhongchiedu.service.SchoolService;


@Service
public class SchoolServiceImpl extends GeneralServiceImpl<School> implements SchoolService {


	
	
	@Override
	public void SaveOrUpdateSchool(School school) {
   		if(school!=null) {
   				if (school.getId()!=null) {
   					// 执行修改操作
                    School edschool=this.findOneById(school.getId(), School.class);
   					if (edschool == null)
   						edschool = new School();
   						school.setIsDisable(false);
//   					edschool.setName(school.getName());
//   					edschool.setAddress(school.getAddress());
//   					edschool.setProvince(school.getProvince());
//   					edschool.setContact(school.getContact());
   					BeanUtils.copyProperties(school, edschool);
   					this.save(edschool);
   				} else {
   					School edschool = new School();
//   					edschool.setName(school.getName());
//   					edschool.setAddress(school.getAddress());
//   					edschool.setProvince(school.getProvince());
//   					edschool.setContact(school.getContact());
   					BeanUtils.copyProperties(school, edschool);
   					// 执行添加操作
   					this.insert(edschool);
   			}		
   		}
	}
	
	
	/**
	 * 查询所有在使用状态下的学校
	 */
	public List<School> findSchoolsByisDisable(){
		Query query = new Query();
		query.addCriteria(Criteria.where("isDisable").is(false));
		List<School> list = this.find(query, School.class);
		return list.size()>0?list:null; 
	}


	@Override
	public BasicDataResult schoolDisable(String id) {
		if(Common.isEmpty(id)){
			return BasicDataResult.build(400, "无法禁用，请求出现问题，请刷新界面!", null);
		}
		School school = this.findOneById(id, School.class);
		if(school == null){
			return BasicDataResult.build(400, "无法获取到学校信息，该学校可能已经被删除", null);
		}
		
		school.setIsDisable(school.getIsDisable().equals(true)?false:true);
		this.save(school);
		
		return BasicDataResult.build(200, school.getIsDisable().equals(true)?"学校禁用成功":"学校启用成功",school.getIsDisable());
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
