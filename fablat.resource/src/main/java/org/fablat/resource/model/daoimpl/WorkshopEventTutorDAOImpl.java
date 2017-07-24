package org.fablat.resource.model.daoimpl;

import org.fablat.resource.entities.WorkshopEventTutor;
import org.fablat.resource.model.dao.WorkshopEventTutorDAO;
import org.springframework.transaction.annotation.Transactional;

public class WorkshopEventTutorDAOImpl extends GenericDAOImpl<WorkshopEventTutor, Integer> implements WorkshopEventTutorDAO {

	@Transactional
	@Override
	public WorkshopEventTutor findByWorkshopAndFabber(Integer idWorkshop, String username) {

		WorkshopEventTutor wm = null;

		wm = (WorkshopEventTutor) getSession()
				.createQuery(
						"from " + getDomainClassName() + " x where x.workshopEvent.id = :idWorkshop "
								+ "and x.subGroupMember.groupMember.fabber.username = :username")
				.setInteger("idWorkshop", idWorkshop)
				.setString("username", username).setMaxResults(1).uniqueResult();

		return wm;
	}

}
