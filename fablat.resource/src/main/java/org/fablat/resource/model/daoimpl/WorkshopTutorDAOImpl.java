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

	@Transactional
	public WorkshopTutor findByWorkshopAndFabber(Integer idWorkshop, String email) {
		WorkshopTutor e = null;
		e = (WorkshopTutor) getSession()
				.createQuery(
						"from " + getDomainClassName() + " x where x.workshop.id = :idWorkshop "
								+ "and x.subGroupMember.groupMember.fabber.email = :email")
				.setInteger("idWorkshop", idWorkshop)
				.setString("email", email).setMaxResults(1).uniqueResult();

		return e;
	}

}
