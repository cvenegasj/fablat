package org.fablat.resource.model.daoimpl;

import org.fablat.resource.entities.SubGroupMember;
import org.fablat.resource.model.dao.SubGroupMemberDAO;
import org.springframework.transaction.annotation.Transactional;

public class SubGroupMemberDAOImpl extends GenericDAOImpl<SubGroupMember, Integer> implements SubGroupMemberDAO {

	@Transactional
	public Integer countAllByFabberAsCoordinator(String email) {
		Integer count = null;
		count = ((Long) getSession()
				.createQuery(
						"select count(x) from " + getDomainClassName() + " x "
								+ "where x.isCoordinator = 1 and x.groupMember.fabber.email = :email")
				.setString("email", email)
				.iterate().next()).intValue();
		
		return count;
	}

	@Transactional
	public Integer countAllByFabberAsCollaborator(String email) {
		Integer count = null;
		count = ((Long) getSession()
				.createQuery(
						"select count(x) from " + getDomainClassName() + " x "
								+ "where x.isCoordinator = 0 and x.groupMember.fabber.email = :email")
				.setString("email", email)
				.iterate().next()).intValue();
		
		return count;
	}
	
	@Transactional
	public SubGroupMember findBySubGroupAndFabber(Integer idSubGroup, String email) {
		SubGroupMember e = null;
		e = (SubGroupMember) getSession()
				.createQuery(
						"from " + getDomainClassName() + " x where x.subGroup.id = :idSubGroup "
								+ "and x.groupMember.fabber.email = :email")
				.setInteger("idSubGroup", idSubGroup)
				.setString("email", email).setMaxResults(1).uniqueResult();

		return e;
	}
}
