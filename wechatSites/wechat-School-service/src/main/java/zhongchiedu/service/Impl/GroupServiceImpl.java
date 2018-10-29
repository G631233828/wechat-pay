package zhongchiedu.service.Impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import zhongchiedu.framework.service.GeneralServiceImpl;
import zhongchiedu.school.daoImpl.GroupDaoImpl;
import zhongchiedu.school.pojo.Group;
import zhongchiedu.school.pojo.School;
import zhongchiedu.school.pojo.Teacher;
import zhongchiedu.school.util.ZtreeNode;

@Service
public class GroupServiceImpl extends GeneralServiceImpl<Group>{
	
	
	@Autowired
	private TeacherServiceImpl teacherService;
	
	@Autowired
	private GroupDaoImpl groupDao;
	
	@Autowired
	private SchoolServiceImpl schoolService;

	/**
	 * 添加组员id
	 * @param id
	 * @param group
	 */
	public void addmember(String id,Group group) {
		group.getTeam().add(id);
	}
	
	/**
	 * 添加或修改
	 * @param group
	 * @param leaderId
	 * @param viceId
	 * @param ids
	 */
	public void saveorupdate(Group group,String leaderId,String viceId,String ids) {
		Teacher leader=teacherService.findOneById(leaderId, Teacher.class);
		Teacher vice=teacherService.findOneById(viceId, Teacher.class);
		group.setLeader(leader);
		group.setVice(vice);
		if(ids!=null&&!"".equals(ids)) {
			String[] id=ids.split(",");
			for (String string : id) {
				this.addmember(string, group);
			}
		}
		//这一步修改没变动组员
		if(group.getId()!=null&&"".equals(ids)) {
			group.setTeam(this.groupDao.findOneById(group.getId(), Group.class).getTeam());
		}
		if(group.getId()!=null) {
			this.groupDao.save(group);
		}else {
			Group ed=new Group();
			BeanUtils.copyProperties(group, ed);
			this.groupDao.insert(ed);
		}
	}
	
	
	/**
	 * 加载所有老师
	 * @param Lt
	 * @param team
	 * @return
	 */
	public List<ZtreeNode> getAllTeacherNode(List<Teacher> Lt,Set<String> team){
		List<ZtreeNode> Lz=new ArrayList<>();
		List<School> ls=this.schoolService.find(new Query(),School.class);
		ZtreeNode z=new ZtreeNode();
		z.setName("教师组");
		z.setpId("0");
		z.setParent(true);
		z.setNocheck(true);
		z.setId("1");
		if(Lt!=null&&Lt.size()>0) {
			z.setId(ls.get(0).getId());
			ZtreeNode zn=null;
			for (Teacher t:Lt) {
				zn=new ZtreeNode();
				zn.setId(t.getId());
				zn.setName(t.getName());
				zn.setpId("1");		
				zn.setChecked(isInTeam(t.getId(), team));	
				Lz.add(zn);
				zn.setpId(ls.get(0).getId());
			}
				Lz.add(z);
		}
		return Lz;
	}
	
	
	/**
	 * 判断id是否在team中	
	 * @param id
	 * @param team
	 * @return
	 */
	public boolean isInTeam(String id,Set<String> team) {
		return team.contains(id);
	}
	
}
