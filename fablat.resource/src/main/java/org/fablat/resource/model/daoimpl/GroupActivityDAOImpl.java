package org.fablat.resource.model.daoimpl;

import java.util.List;

import org.fablat.resource.entities.GroupActivity;
import org.fablat.resource.model.dao.GroupActivityDAO;
import org.springframework.transaction.annotation.Transactional;

public class GroupActivityDAOImpl extends GenericDAOImpl<GroupActivity, Integer> implements GroupActivityDAO {

	@SuppressWarnings("unchecked")
	@Transactional
	public List<GroupActivity> findAllExternal() {
		List<GroupActivity> list = null;
		list = (List<GroupActivity>) getSession()
				.createQuery(
						"select x from " + getDomainClassName() + " x "
						+ "where x.visibility = 'EXTERNAL' "
						+ "order by date(x.creationDateTime) desc")
				.list();
		
		return list;
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public List<GroupActivity> findAllByGroup(Integer idGroup) {
		List<GroupActivity> list = null;
		list = (List<GroupActivity>) getSession()
				.createQuery(
						"select x from " + getDomainClassName() + " x "
						+ "where x.visibility = 'EXTERNAL' "
						+ "and x.group.id = :idGroup "
						+ "order by date(x.creationDateTime) desc")
				.setInteger("idGroup", idGroup)
				.list();
		
		return list;
	}

}
