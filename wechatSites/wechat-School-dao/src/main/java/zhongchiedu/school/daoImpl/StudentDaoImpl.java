package zhongchiedu.school.daoImpl;



import org.springframework.stereotype.Repository;

import zhongchiedu.framework.dao.GeneralDaoImpl;
import zhongchiedu.school.dao.StudentDao;
@Repository
public class StudentDaoImpl extends GeneralDaoImpl<zhongchiedu.school.pojo.Student> implements StudentDao {

}
