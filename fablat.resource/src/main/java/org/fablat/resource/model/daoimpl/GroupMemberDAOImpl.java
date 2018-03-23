package org.fablat.resource.model.daoimpl;

import java.util.List;

import org.fablat.resource.entities.GroupMember;
import org.fablat.resource.model.dao.GroupMemberDAO;
import org.springframework.transaction.annotation.Transactional;

public class GroupMemberDAOImpl extends GenericDAOImpl<GroupMember, Integer> implements GroupMemberDAO {

	@Transactional
	public Integer countAllByFabberAsCoordinator(String email) {
		Long count = getSession()
				.createQuery(
						"select count(x) from " + getDomainClassName() + " x "
								+ "where x.isCoordinator = 1 and x.fabber.email = :email", Long.class)
				.setParameter("email", email)
				.getSingleResult();
		
		return count.intValue();
	}

	@Transactional
	public Integer countAllByFabberAsCollaborator(String email) {
		Long count = getSession()
				.createQuery(
						"select count(x) from " + getDomainClassName() + " x "
								+ "where x.isCoordinator = 0 and x.fabber.email = :email", Long.class)
				.setParameter("email", email)
				.getSingleResult();
		
		return count.intValue();
	}

	@Transactional
	public GroupMember findByGroupAndFabber(Integer idGroup, String email) {
		GroupMember e = null;
		e = (GroupMember) getSession()
				.createQuery(
						"select x from " + getDomainClassName() + " x "
								+ "where x.group.id = :idGroup and x.fabber.email = :email")
				.setParameter("idGroup", idGroup)
				.setParameter("email", email)
				.setMaxResults(1).uniqueResult();

		return e;
	}
	
	@Transactional
	public GroupMember findByGroupAndFabber(Integer idGroup, Integer idFabber) {
		GroupMember e = null;
		e = (GroupMember) getSession()
				.createQuery(
						"select x from " + getDomainClassName() + " x "
								+ "where x.group.id = :idGroup and x.fabber.id = :idFabber")
				.setParameter("idGroup", idGroup)
				.setParameter("idFabber", idFabber)
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
				.setParameter("idGroup", idGroup)
				.list();

		return list;
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public List<GroupMember> findAllByFabber(String email) {
		List<GroupMember> list = null;
		list = (List<GroupMember>) getSession()
				.createQuery(
						"select x from " + getDomainClassName() + " x "
								+ "where x.fabber.email = :email ")
				.setParameter("email", email)
				.list();

		return list;
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public List<GroupMember> findAllByFabber(Integer idFabber) {
		List<GroupMember> list = null;
		list = (List<GroupMember>) getSession()
				.createQuery(
						"select x from " + getDomainClassName() + " x "
								+ "where x.fabber.id = :idFabber ")
				.setParameter("idFabber", idFabber)
				.list();

		return list;
	}

}
