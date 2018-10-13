package zhongchiedu.service.Impl;


import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import zhongchiedu.common.utils.BasicDataResult;
import zhongchiedu.common.utils.Common;
import zhongchiedu.common.utils.PinyinTool;
import zhongchiedu.common.utils.PinyinTool.Type;
import zhongchiedu.framework.service.GeneralServiceImpl;
import zhongchiedu.school.pojo.Teacher;
import zhongchiedu.service.TeacherService;

@Service
public class TeacherServiceImpl extends GeneralServiceImpl<Teacher> implements TeacherService {

	@Autowired
	private PinyinTool pinyinTool;
	
	
	@Override
	public void SaveOrUpdateTeacher(Teacher teacher) {
		if (Common.isNotEmpty(teacher)) {
			try {
				teacher.setNamePinyin(pinyinTool.toPinYin(teacher.getName(), "", PinyinTool.Type.LOWERCASE));
			} catch (BadHanyuPinyinOutputFormatCombination e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (teacher.getId() != null) {
				// 执行修改操作
				Teacher ed = this.findOneById(teacher.getId(), Teacher.class);
				if (ed == null)
					ed = new Teacher();
				teacher.setIsDisable(false);
				BeanUtils.copyProperties(teacher, ed);
				this.save(ed);
			} else {
				Teacher ed = new Teacher();
				BeanUtils.copyProperties(teacher, ed);
				// 执行添加操作
				this.insert(ed);
			}
		}
	}

	@Override
	public BasicDataResult teacherDisable(String id) {
		if (Common.isEmpty(id)) {
			return BasicDataResult.build(400, "无法禁用，请求出现问题，请刷新界面!", null);
		}
		Teacher teacher = this.findOneById(id, Teacher.class);
		if (teacher == null) {
			return BasicDataResult.build(400, "无法获取到老师信息，该老师可能已经被删除", null);
		}

		teacher.setIsDisable(teacher.getIsDisable().equals(true) ? false : true);
		this.save(teacher);

		return BasicDataResult.build(200, teacher.getIsDisable().equals(true) ? "老师禁用成功" : "老师启用成功",
				teacher.getIsDisable());
	}

	/**
	 * 获取所有非禁用的老师信息
	 */
	@Override
	public List<Teacher> findTeachersByisDisable() {
		Query query = new Query();
		query.addCriteria(Criteria.where("isDisable").is(false));
		List<Teacher> list = this.find(query, Teacher.class);
		return list.size() > 0 ? list : null;
	}
	
	
	

}
