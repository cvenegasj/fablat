package org.fablat.resource.model.daoimpl;

import java.util.List;

import org.fablat.resource.entities.SubGroup;
import org.fablat.resource.model.dao.SubGroupDAO;
import org.springframework.transaction.annotation.Transactional;

public class SubGroupDAOImpl extends GenericDAOImpl<SubGroup, Integer> implements SubGroupDAO {

	@SuppressWarnings("unchecked")
	@Transactional
	public List<SubGroup> findAllOrderedAsc() {
		List<SubGroup> list = null;
		
		list = (List<SubGroup>) getSession()
				.createQuery("from " + getDomainClassName() + " x " 
						+ "order by x.name asc")
				.list();

		return list;
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public List<SubGroup> findAllByGroup(Integer idGroup) {
		List<SubGroup> list = null;
		
		list = (List<SubGroup>) getSession()
				.createQuery("from " + getDomainClassName() + " x "
						+ "where x.group.id = :idGroup "
						+ "order by x.name asc")
				.setParameter("idGroup", idGroup)
				.list();

		return list;
	}

	@Transactional
	public Integer getMembersCount(Integer idSubGroup) {
		Long count = getSession()
				.createQuery(
						"select count(distinct x) from SubGroupMember x "
								+ "where x.subGroup.id = :idSubGroup", Long.class)
				.setParameter("idSubGroup", idSubGroup)
				.getSingleResult();
		
		return count.intValue();
	}

	@Transactional
	public Integer getWorkshopsCount(Integer idSubGroup) {
		Long count = getSession()
				.createQuery(
						"select count(distinct x) from Workshop x "
								+ "where x.subGroup.id = :idSubGroup", Long.class)
				.setParameter("idSubGroup", idSubGroup)
				.getSingleResult();
		
		return count.intValue();
	}
	
}
