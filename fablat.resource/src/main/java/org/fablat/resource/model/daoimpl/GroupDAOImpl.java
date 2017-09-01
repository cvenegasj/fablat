package org.fablat.resource.model.daoimpl;

import java.util.List;

import org.fablat.resource.entities.Group;
import org.fablat.resource.model.dao.GroupDAO;
import org.springframework.transaction.annotation.Transactional;

public class GroupDAOImpl extends GenericDAOImpl<Group, Integer> implements GroupDAO {

	@SuppressWarnings("unchecked")
	@Transactional
	public List<Group> findByTerm(String term) {
		List<Group> list = null;
		list = (List<Group>) getSession()
				.createQuery(
						"select x from " + getDomainClassName() + " x "
								+ "where lower(x.name) like :term")
				.setParameter("term", "%" + term.toLowerCase() + "%")
				.list();
		
		return list;
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public List<Group> findAllOrderDate() {
		List<Group> list = null;
		list = (List<Group>) getSession()
				.createQuery(
						"select x from " + getDomainClassName() + " x "
								+ "order by date(x.creationDateTime) asc")
				.list();
		
		return list;
	}

}
