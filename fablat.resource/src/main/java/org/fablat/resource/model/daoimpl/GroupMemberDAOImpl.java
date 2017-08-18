package org.fablat.resource.model.daoimpl;

import java.util.List;

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

	@SuppressWarnings("unchecked")
	@Transactional
	public List<GroupMember> findAllByGroup(Integer idGroup) {
		List<GroupMember> list = null;
		list = (List<GroupMember>) getSession()
				.createQuery(
						"select x from " + getDomainClassName() + " x "
								+ "where x.group.id = :idGroup "
								+ "order by case when x.isCoordinator = 1 then 0 else 1 end, "
								+ "date(x.creationDateTime) asc")
				.setInteger("idGroup", idGroup)
				.list();

		return list;
	}

}
