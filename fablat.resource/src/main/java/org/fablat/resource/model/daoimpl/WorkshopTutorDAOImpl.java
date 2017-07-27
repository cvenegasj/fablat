package org.fablat.resource.model.daoimpl;

import org.fablat.resource.entities.WorkshopTutor;
import org.fablat.resource.model.dao.WorkshopTutorDAO;
import org.springframework.transaction.annotation.Transactional;

public class WorkshopTutorDAOImpl extends GenericDAOImpl<WorkshopTutor, Integer> implements WorkshopTutorDAO {

	@Transactional
	public Integer countAllByFabber(String email) {
		Integer count = null;
		count = ((Long) getSession()
				.createQuery(
						"select count(x) from " + getDomainClassName() + " x "
								+ "where x.subGroupMember.groupMember.fabber.email = :email").setString("email", email)
				.iterate().next()).intValue();
		return count;
	}

}
