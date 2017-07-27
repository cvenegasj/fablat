package org.fablat.resource.model.daoimpl;

import org.fablat.resource.entities.GroupMember;
import org.fablat.resource.model.dao.GroupMemberDAO;
import org.springframework.transaction.annotation.Transactional;

public class GroupMemberDAOImpl extends GenericDAOImpl<GroupMember, Integer> implements GroupMemberDAO {

	@Transactional
	public Integer countAllByFabberAsCoordinator(String email) {
		Integer count = null;
		count = ((Long) getSession()
				.createQuery(
						"select count(x) from " + getDomainClassName() + " x "
								+ "where x.isCoordinator = 1 and x.fabber.email = :email").setString("email", email)
				.iterate().next()).intValue();
		
		return count;
	}

	@Transactional
	public Integer countAllByFabberAsCollaborator(String email) {
		Integer count = null;
		count = ((Long) getSession()
				.createQuery(
						"select count(x) from " + getDomainClassName() + " x "
								+ "where x.isCoordinator = 0 and x.fabber.email = :email")
				.setString("email", email)
				.iterate().next()).intValue();
		
		return count;
	}

	@Transactional
	public GroupMember findByGroupAndFabber(Integer idGroup, String email) {
		GroupMember e = null;
		e = (GroupMember) getSession()
				.createQuery(
						"select x from " + getDomainClassName() + " x "
								+ "where x.group.id = :idGroup and x.fabber.email = :email")
				.setInteger("idGroup", idGroup)
				.setString("email", email)
				.setMaxResults(1).uniqueResult();

		return e;
	}

}
