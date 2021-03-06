package zhongchiedu.service.Impl;


import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import zhongchiedu.common.utils.BasicDataResult;
import zhongchiedu.common.utils.Common;
import zhongchiedu.common.utils.ExcelReadUtil;
import zhongchiedu.framework.service.GeneralServiceImpl;
import zhongchiedu.school.dao.StudentDao;
import zhongchiedu.school.pojo.Clazz;
import zhongchiedu.school.pojo.Student;
import zhongchiedu.service.StudentService;

@Service
public class StudentServiceImpl extends GeneralServiceImpl<Student> implements StudentService {

	
	private static final Logger log = LoggerFactory.getLogger(StudentServiceImpl.class);

	@Autowired
	private  StudentDao studentDao;
	@Autowired
	private ClazzServiceImpl clazzService;
	
	@Autowired
	private ExcelReadUtil excelReadUtil;
	//添加或是修改学生
	@Override
	public void SaveOrUpdateStudent(Student student) {
		System.out.println(student.getClazz().getId());
//		Clazz clazz=null;
//		if(clazzId!=null) {
//			clazz=this.clazzService.findOneById(clazzId, Clazz.class);
//		}
//		student.setClazz(clazz);
		if (Common.isNotEmpty(student)) {
			if (student.getId() != null) {
				// 执行修改操作
				Student ed = this.studentDao.findOneById(student.getId(), Student.class);
				if (ed == null)
					ed = new Student();
				student.setIsDisable(false);
				BeanUtils.copyProperties(student, ed);
				this.save(ed);
			} else {
				Student ed=new Student();
				BeanUtils.copyProperties(student, ed);
				// 执行添加操作
				this.studentDao.insert(ed);
			}
		}
	}

	
	//禁用学生
	@Override
	public BasicDataResult studentDisable(String id) {
		if (Common.isEmpty(id)) {
			return BasicDataResult.build(400, "无法禁用，请求出现问题，请刷新界面!", null);
		}
		Student student = this.findOneById(id, Student.class);
		if (student == null) {
			return BasicDataResult.build(400, "无法获取到学生信息，该学生可能已经被删除", null);
		}

		student.setIsDisable(student.getIsDisable().equals(true) ? false : true);
		this.save(student);

		return BasicDataResult.build(200, student.getIsDisable().equals(true) ? "学生禁用成功" : "学生启用成功",
				student.getIsDisable());
	}


	/**
	 * 学生批量导入
	 * @param file
	 * @param row
	 * @param session
	 * @return
	 */
	public String BatchImport(File file, int row, HttpSession session)  {
		String error = "";
		int clazzYear=0;
		int clazzNum=0;
		String[][] resultexcel=null;
		try {
			resultexcel = excelReadUtil.readExcel(file, row);
		} catch (IOException e) {
			e.printStackTrace();
		}
		int rowLength = resultexcel.length;
		StudentProcessInfo pri = new StudentProcessInfo();
		pri.allnum = rowLength;
		for (int i = 0; i < rowLength; i++) {
			Query query = new Query();
			Student importStudent = new Student();

			pri.nownum = i;
			pri.lastnum = rowLength - i;
			session.setAttribute("proInfo", pri);
			int j = 0;
			try {
				clazzYear=Integer.parseInt(resultexcel[i][j+2]);
				clazzNum=Integer.parseInt(resultexcel[i][j+3]);
			}catch (Exception e) {
				error += "<span class='entypo-attention'></span>导入文件过程中出现错误第<b>&nbsp&nbsp" + (i + 2) + "&nbsp&nbsp</b>行出现错误年级或班级应为整数&nbsp&nbsp</b></br>";
				if ((i + 1) < rowLength) {
					continue;
				}
			}
			try {
				importStudent.setName(resultexcel[i][j]);
				importStudent.setRegisterNumber(resultexcel[i][j+1]);
				//importStudent.setStudentCode(resultexcel[i][j+1]);
				Clazz clazz=clazzService.findClazzByYearNum(clazzYear,clazzNum);
				if(clazz==null) {
					clazz=new Clazz();
					clazz.setClazzNum(clazzNum);
					clazz.setClazzYear(clazzYear);
					clazzService.insert(clazz);
				}
				importStudent.setClazz(clazz);
				query.addCriteria(Criteria.where("studentCode").is(importStudent.getRegisterNumber()));
				String studentCode=importStudent.getRegisterNumber();
				// 通过学籍号是否存在该信息
				Student student = this.findOneByQuery(query, Student.class);
				if(student !=null){
					error += "<span class='entypo-attention'></span>导入文件过程中出现已经存在的学籍号码，第<b>&nbsp&nbsp" + (i + 2)
							+ "&nbsp&nbsp</b>行出现重复内容为<b>&nbsp&nbsp导入学籍号:"+studentCode+"请手动去修改该条信息！&nbsp&nbsp</b></br>";
					continue;
				}
				if (student == null) {
					Student ed = new Student();
					BeanUtils.copyProperties(importStudent, ed);
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
	 * 
	 * @Title: findproInfo @Description: TODO(获取上传进度) @param @param
	 * request @param @return 设定文件 @return ProcessInfo 返回类型 @throws
	 */
	public StudentProcessInfo findproInfo(HttpServletRequest request) {

		return  (StudentProcessInfo) request.getSession().getAttribute("proInfo");

	}
	
	/**
	 * 判断学生是否存在
	 * @param registerNumber
	 * @return
	 */
	public BasicDataResult findStudentByRegisterNum(String registerNumber){
		if(Common.isNotEmpty(registerNumber)){
			Query query = new Query();
			query.addCriteria(Criteria.where("registerNumber").is(registerNumber));
			Student student = this.findOneByQuery(query, Student.class);
			return BasicDataResult.build(200, Common.isNotEmpty(student)?"true":"false", null);
		}
		return BasicDataResult.build(200, "false", null);
		
	}
	
	
	

	class StudentProcessInfo {
		public long allnum = 0;// 导入数据总数
		public long nownum = 0;// 当前导入第几条
		public long lastnum = 0;// 还剩几条数据
	}

		
	
	
}
