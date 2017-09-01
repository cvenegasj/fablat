package org.fablat.resource.model.daoimpl;

import org.fablat.resource.entities.Role;
import org.fablat.resource.model.dao.RoleDAO;
import org.springframework.transaction.annotation.Transactional;

public class RoleDAOImpl extends GenericDAOImpl<Role, Integer> implements RoleDAO {

	@Transactional
	public Role findByName(String name) {
		Role role = null;
		role = (Role) getSession().createQuery("from " + getDomainClassName() + " x where x.name = :name")
				.setParameter("name", name)
				.setMaxResults(1).uniqueResult();

		return role;
	}

}
