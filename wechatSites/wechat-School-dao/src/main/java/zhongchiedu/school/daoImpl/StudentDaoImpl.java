package zhongchiedu.school.daoImpl;



import org.springframework.stereotype.Repository;

import zhongchiedu.framework.dao.GeneralDaoImpl;
import zhongchiedu.school.dao.StudentDao;
import zhongchiedu.school.pojo2.Student;
@Repository
public class StudentDaoImpl extends GeneralDaoImpl<Student> implements StudentDao {

}
