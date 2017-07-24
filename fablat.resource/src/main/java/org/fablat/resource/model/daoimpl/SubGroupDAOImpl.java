package org.fablat.resource.model.daoimpl;

import java.util.List;

import org.fablat.resource.entities.Fabber;
import org.fablat.resource.entities.SubGroup;
import org.fablat.resource.model.dao.SubGroupDAO;
import org.springframework.transaction.annotation.Transactional;

public class SubGroupDAOImpl extends GenericDAOImpl<SubGroup, Integer> implements SubGroupDAO {

	@SuppressWarnings("unchecked")
	@Transactional
	public List<SubGroup> findAllOrderedAsc() {

		List<SubGroup> list = null;

		list = (List<SubGroup>) getSession().createQuery("from " + getDomainClassName() + " x order by x.name asc")
				.list();

		return list;
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public List<SubGroup> findFabberSubGroups(String username) {

		List<SubGroup> list = null;

		list = (List<SubGroup>) getSession()
				.createQuery(
						"select x from " + getDomainClassName() + " x join x.subGroupMembers sm "
								+ "where sm.groupMember.fabber.username = :username")
				.setString("username", username).list();

		return list;
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public List<SubGroup> findManagedSubGroups(String username) {

		List<SubGroup> list = null;

		list = (List<SubGroup>) getSession()
				.createQuery(
						"select x from " + getDomainClassName() + " x join x.subGroupMembers sm "
								+ "where sm.groupMember.fabber.username = :username " + "and sm.isCoordinator = 1")
				.setString("username", username).list();

		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public List<SubGroup> findManagedSubGroups(Long idFabber) {

		List<SubGroup> list = null;

		list = (List<SubGroup>) getSession()
				.createQuery(
						"select x from " + getDomainClassName() + " x join x.subGroupMembers sm "
								+ "where sm.groupMember.fabber.id = :idFabber " + "and sm.isCoordinator = 1")
				.setLong("idFabber", idFabber).list();

		return list;
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public List<SubGroup> findNotManagedSubGroups(String username) {

		List<SubGroup> list = null;

		list = (List<SubGroup>) getSession()
				.createQuery(
						"select x from " + getDomainClassName() + " x join x.subGroupMembers sm "
								+ "where sm.groupMember.fabber.username = :username " + "and sm.isCoordinator = 0")
				.setString("username", username).list();

		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public List<SubGroup> findNotManagedSubGroups(Long idFabber) {

		List<SubGroup> list = null;

		list = (List<SubGroup>) getSession()
				.createQuery(
						"select x from " + getDomainClassName() + " x join x.subGroupMembers sm "
								+ "where sm.groupMember.fabber.id = :idFabber " + "and sm.isCoordinator = 0")
				.setLong("idFabber", idFabber).list();

		return list;
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public List<Fabber> findCoordinators(Integer idSubGroup) {

		List<Fabber> list = null;

		list = (List<Fabber>) getSession()
				.createQuery(
						"select x from Fabber x join x.groupMembers gm join gm.subGroupMembers sm " + "where sm.subGroup.id = :idSubGroup "
								+ "and sm.isCoordinator = 1").setInteger("idSubGroup", idSubGroup).list();

		return list;
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public List<Fabber> findCollaborators(Integer idSubGroup) {

		List<Fabber> list = null;

		list = (List<Fabber>) getSession()
				.createQuery(
						"select x from Fabber x join x.groupMembers gm join gm.subGroupMembers sm " + "where sm.subGroup.id = :idSubGroup "
								+ "and sm.isCoordinator = 0").setInteger("idSubGroup", idSubGroup).list();

		return list;
	}
}
