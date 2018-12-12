package zhongchiedu.school.dao.Impl;

import org.springframework.stereotype.Repository;

import zhongchiedu.framework.dao.GeneralDaoImpl;
import zhongchiedu.school.dao.NewsDao;
import zhongchiedu.school.pojo.News;

@Repository
public class NewsDaoImpl  extends GeneralDaoImpl<News> implements NewsDao{

}
