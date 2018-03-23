package org.fablat.resource.model.daoimpl;

import java.util.List;

import org.fablat.resource.entities.Fabber;
import org.fablat.resource.model.dao.FabberDAO;
import org.springframework.transaction.annotation.Transactional;

public class FabberDAOImpl extends GenericDAOImpl<Fabber, Integer> implements FabberDAO {

	@Transactional
	public Fabber findByEmail(String email) {
		Fabber fabber = null;
		fabber = (Fabber) getSession().createQuery("from " + getDomainClassName() + " f where f.email = :email")
				.setParameter("email", email).setMaxResults(1).uniqueResult();
		return fabber;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public List<Fabber> findByTerm(String term) {
		List<Fabber> list = null;
		list = (List<Fabber>) getSession()
				.createQuery(
						"select x from " + getDomainClassName() + " x "
								+ "where lower(concat(x.firstName, ' ', x.lastName)) like :term "
								+ "or x.email like :term ")
				.setParameter("term", "%" + term.toLowerCase() + "%")
				.list();
		
		return list;
	}

	@Transactional
	public Integer countAll() {
		Integer count = null;
		count = (Integer) getSession()
				.createQuery("select count(x) from " + getDomainClassName() + " x ")
				.uniqueResult();
		return count;
	}
	
}
