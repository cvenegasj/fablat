package org.fablat.auth.model.daoimpl;

import org.fablat.auth.entities.Fabber;
import org.fablat.auth.model.dao.FabberDAO;
import org.springframework.transaction.annotation.Transactional;

public class FabberDAOImpl extends GenericDAOImpl<Fabber, Integer> implements FabberDAO {

	@Transactional
	public Fabber findByEmail(String email) {

		Fabber fabber = null;

		fabber = (Fabber) getSession().createQuery("from " + getDomainClassName() + " f where f.email = :email")
				.setString("email", email).setMaxResults(1).uniqueResult();

		return fabber;
	}
}
