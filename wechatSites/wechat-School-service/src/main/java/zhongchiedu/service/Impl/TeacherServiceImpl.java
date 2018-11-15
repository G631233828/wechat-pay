package zhongchiedu.service.Impl;


import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import zhongchiedu.common.utils.BasicDataResult;
import zhongchiedu.common.utils.Common;
import zhongchiedu.common.utils.ExcelReadUtil;
import zhongchiedu.common.utils.PinyinTool;
import zhongchiedu.common.utils.PinyinTool.Type;
import zhongchiedu.framework.service.GeneralServiceImpl;
import zhongchiedu.school.pojo.Clazz;
import zhongchiedu.school.pojo.Teacher;
import zhongchiedu.service.TeacherService;

@Service
public class TeacherServiceImpl extends GeneralServiceImpl<Teacher> implements TeacherService {
	
	private static final Logger log = LoggerFactory.getLogger(TeacherServiceImpl.class);

	@Autowired
	private PinyinTool pinyinTool;
	@Autowired
	private ExcelReadUtil excelReadUtil;
	
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
	 * @throws IOException
	 * 
	 * @Title: BatchImport @Description: TODO(批量导入) @param @param
	 * file @param @param row @param @return 设定文件 @return String 返回类型 @throws
	 */
	public String BatchImport(File file, int row, HttpSession session) throws IOException {

		String error = "";
		String[][] resultexcel;

		resultexcel = excelReadUtil.readExcel(file, row);
		int rowLength = resultexcel.length;
		ProcessInfo pri = new ProcessInfo();
		pri.allnum = rowLength;
		for (int i = 0; i < rowLength; i++) {
			Query query = new Query();
			Teacher importTeacher = new Teacher();

			pri.nownum = i;
			pri.lastnum = rowLength - i;
			session.setAttribute("proInfo", pri);

			int j = 0;
			try {
				importTeacher.setName(resultexcel[i][j]);
				importTeacher.setContactsmobile(resultexcel[i][j+1]);
				importTeacher.setCardType(resultexcel[i][j+2]);
				importTeacher.setCardId(resultexcel[i][j+3]);
				importTeacher.setTeacherEmail(resultexcel[i][j+4]);
				

				String telPhone = importTeacher.getContactsmobile();
				// 判断导入的手机号是否为空，如果为空的话添加error信息
				log.info("绑定人手机号" + telPhone);
				if (Common.isEmpty(telPhone)) {
					error += "<span class='entypo-attention'></span>导入文件过程中出现错误第<b>&nbsp&nbsp" + (i + 2)
							+ "&nbsp&nbsp</b>行出现错误内容为<b>&nbsp&nbsp导入教师中联系电话不能为空&nbsp&nbsp</b></br>";
					continue;
				}

				log.info(telPhone);
				query.addCriteria(Criteria.where("contactsmobile").is(telPhone));
				// 通过用户的手机号查询是否存在该信息
				Teacher teacher = this.findOneByQuery(query, Teacher.class);
				
				if(teacher !=null){
					
					error += "<span class='entypo-attention'></span>导入文件过程中出现已经存在的手机号码，第<b>&nbsp&nbsp" + (i + 2)
							+ "&nbsp&nbsp</b>行出现重复内容为<b>&nbsp&nbsp导入联系人手机号:"+telPhone+"请手动去修改该条信息！&nbsp&nbsp</b></br>";
					continue;
				}

				if (teacher == null) {
				
					Teacher ed = new Teacher();
					importTeacher.setNamePinyin(pinyinTool.toPinYin(importTeacher.getName()));
					BeanUtils.copyProperties(importTeacher, ed);
					this.insert(ed);
				}
				// 捕捉批量导入过程中遇到的错误，记录错误行数继续执行下去
			} catch (Exception e) {
				log.debug("导入文件过程中出现错误第" + (i + 2) + "行出现错误" + e);
				String aa = e.getLocalizedMessage();
				String b = aa.substring(aa.indexOf(":") + 1, aa.length()).replaceAll("\"", "");

				error += "<span class='entypo-attention'></span>导入文件过程中出现错误第<b>&nbsp&nbsp" + (i + 2) + "&nbsp&nbsp</b>行出现错误内容为<b>&nbsp&nbsp" + b
						+ "&nbsp&nbsp</b></br>";
				if ((i + 1) < rowLength) {
					continue;
				}

			}
		}
		log.info(error);

		return error;
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
	

	/**
	 * 
	 * @Title: findproInfo @Description: TODO(获取上传进度) @param @param
	 * request @param @return 设定文件 @return ProcessInfo 返回类型 @throws
	 */
	public ProcessInfo findproInfo(HttpServletRequest request) {

		return (ProcessInfo) request.getSession().getAttribute("proInfo");

	}

	class ProcessInfo {
		public long allnum = 0;// 导入数据总数
		public long nownum = 0;// 当前导入第几条
		public long lastnum = 0;// 还剩几条数据
	}

	
	

	

}
