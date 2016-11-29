package org.fablat.resource.model.daoimpl;

import org.fablat.resource.entities.Fabber;
import org.fablat.resource.model.dao.FabberDAO;
import org.springframework.transaction.annotation.Transactional;

public class FabberDAOImpl extends GenericDAOImpl<Fabber, Long> implements FabberDAO {

	@Transactional
	public Fabber findByUsername(String username) {

		Fabber fabber = null;

		fabber = (Fabber) getSession().createQuery("from " + getDomainClassName() + " f where f.username = :username")
				.setString("username", username).setMaxResults(1).uniqueResult();

		return fabber;
	}
}
