package org.fablat.resource.model.daoimpl;

import java.util.Date;
import java.util.List;

import org.fablat.resource.entities.Fabber;
import org.fablat.resource.entities.WorkshopEvent;
import org.fablat.resource.model.dao.WorkshopEventDAO;
import org.springframework.transaction.annotation.Transactional;

public class WorkshopEventDAOImpl extends GenericDAOImpl<WorkshopEvent, Integer> implements WorkshopEventDAO {

	@SuppressWarnings("unchecked")
	@Transactional
	public List<WorkshopEvent> findUpcoming() {

		List<WorkshopEvent> list = null;

		list = (List<WorkshopEvent>) getSession()
				.createQuery("select x from " + getDomainClassName() + " x where x.dateTime > :now")
				.setDate("now", new Date()).list();

		return list;
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public List<WorkshopEvent> findUpcomingByUser(String username) {

		List<WorkshopEvent> list = null;

		list = (List<WorkshopEvent>) getSession()
				.createQuery(
						"select x from " + getDomainClassName()
								+ " x join x.workshopEventMembers wm join wm.subGroupMember sm " + "where x.dateTime >= :now "
								+ "and sm.groupMember.fabber.username = :username")
				.setDate("now", new Date())
				.setString("username", username).list();

		return list;
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public List<WorkshopEvent> findPastByUser(String username) {

		List<WorkshopEvent> list = null;

		list = (List<WorkshopEvent>) getSession()
				.createQuery(
						"select x from " + getDomainClassName()
								+ " x join x.workshopEventMembers wm join wm.subGroupMember sm " + "where x.dateTime < :now "
								+ "and sm.groupMember.fabber.username = :username")
				.setDate("now", new Date())
				.setString("username", username).list();

		return list;
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public List<WorkshopEvent> findUpcomingBySubGroup(Integer idSubGroup) {

		List<WorkshopEvent> list = null;

		list = (List<WorkshopEvent>) getSession()
				.createQuery(
						"select x from " + getDomainClassName() + " x where x.subGroup.id = :idSubGroup "
								+ "and x.dateTime >= :now").setInteger("idSubGroup", idSubGroup)
				.setDate("now", new Date())
				.list();

		return list;
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public List<Fabber> findCoordinators(Integer idWorkshop) {

		List<Fabber> list = null;

		list = (List<Fabber>) getSession()
				.createQuery(
						"select x from Fabber x join x.groupMembers gm join gm.subGroupMembers sm join sm.workshopEventMembers wm "
								+ "where wm.workshopEvent.id = :idWorkshop " + "and wm.isCoordinator = 1")
				.setInteger("idWorkshop", idWorkshop).list();

		return list;
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public List<Fabber> findCollaborators(Integer idWorkshop) {

		List<Fabber> list = null;

		list = (List<Fabber>) getSession()
				.createQuery(
						"select x from Fabber x join x.groupMembers gm join gm.subGroupMembers sm join sm.workshopEventMembers wm "
								+ "where wm.workshopEvent.id = :idWorkshop " + "and wm.isCoordinator = 0")
				.setInteger("idWorkshop", idWorkshop).list();

		return list;
	}
}
