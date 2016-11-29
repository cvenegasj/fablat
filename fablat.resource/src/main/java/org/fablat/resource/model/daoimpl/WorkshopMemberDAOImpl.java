package org.fablat.resource.model.daoimpl;

import org.fablat.resource.entities.WorkshopMember;
import org.fablat.resource.model.dao.WorkshopMemberDAO;
import org.springframework.transaction.annotation.Transactional;

public class WorkshopMemberDAOImpl extends GenericDAOImpl<WorkshopMember, Integer> implements WorkshopMemberDAO {

	@Transactional
	@Override
	public WorkshopMember findByWorkshopAndFabber(Integer idWorkshop, String username) {

		WorkshopMember wm = null;

		wm = (WorkshopMember) getSession()
				.createQuery(
						"from " + getDomainClassName() + " x where x.workshop.id = :idWorkshop "
								+ "and x.groupMember.fabber.username = :username").setInteger("idWorkshop", idWorkshop)
				.setString("username", username).setMaxResults(1).uniqueResult();

		return wm;
	}

}
