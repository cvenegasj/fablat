package org.fablat.resource.model.daoimpl;

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

	@Transactional
	public Integer countAll() {
		Integer count = null;
		count = (Integer) getSession()
				.createQuery("select count(x) from " + getDomainClassName() + " x ")
				.uniqueResult();
		return count;
	}
}
