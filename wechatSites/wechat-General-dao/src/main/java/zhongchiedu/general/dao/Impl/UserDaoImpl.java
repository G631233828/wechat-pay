package zhongchiedu.general.dao.Impl;

import org.springframework.stereotype.Repository;

import zhongchiedu.framework.dao.GeneralDaoImpl;
import zhongchiedu.general.dao.UserDao;
import zhongchiedu.general.pojo.User;

@Repository
public class UserDaoImpl  extends GeneralDaoImpl<User> implements UserDao{

}
