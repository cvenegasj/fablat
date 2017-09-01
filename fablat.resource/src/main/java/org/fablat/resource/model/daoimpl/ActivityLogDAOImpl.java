package org.fablat.resource.model.daoimpl;

import java.util.List;

import org.fablat.resource.entities.ActivityLog;
import org.fablat.resource.model.dao.ActivityLogDAO;
import org.springframework.transaction.annotation.Transactional;

public class ActivityLogDAOImpl extends GenericDAOImpl<ActivityLog, Integer> implements ActivityLogDAO {

	@SuppressWarnings("unchecked")
	@Transactional
	public List<ActivityLog> findAllExternal() {
		List<ActivityLog> list = null;
		list = (List<ActivityLog>) getSession()
				.createQuery(
						"select x from " + getDomainClassName() + " x "
						+ "where x.visibility = 'EXTERNAL' "
						+ "order by x.id desc")
				.list();
		
		return list;
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public List<ActivityLog> findAllByGroup(Integer idGroup) {
		List<ActivityLog> list = null;
		list = (List<ActivityLog>) getSession()
				.createQuery(
						"select x from " + getDomainClassName() + " x "
						+ "where (x.visibility = 'INTERNAL' or x.visibility = 'EXTERNAL') "
						+ "and x.level = 'GROUP' "
						+ "and x.group.id = :idGroup "
						+ "order by date(x.creationDateTime) desc")
				.setParameter("idGroup", idGroup)
				.list();
		
		return list;
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public List<ActivityLog> findAllBySubGroup(Integer idSubGroup) {
		List<ActivityLog> list = null;
		list = (List<ActivityLog>) getSession()
				.createQuery(
						"select x from " + getDomainClassName() + " x "
						+ "where (x.visibility = 'INTERNAL' or x.visibility = 'EXTERNAL') "
						+ "and x.level = 'SUBGROUP' "
						+ "and x.subGroup.id = :idSubGroup "
						+ "order by date(x.creationDateTime) desc")
				.setParameter("idSubGroup", idSubGroup)
				.list();
		
		return list;
	}

}
