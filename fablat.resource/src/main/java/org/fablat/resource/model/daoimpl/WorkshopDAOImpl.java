package org.fablat.resource.model.daoimpl;

import java.util.Date;
import java.util.List;

import org.fablat.resource.entities.Fabber;
import org.fablat.resource.entities.Workshop;
import org.fablat.resource.model.dao.WorkshopDAO;
import org.springframework.transaction.annotation.Transactional;

public class WorkshopDAOImpl extends GenericDAOImpl<Workshop, Integer> implements WorkshopDAO {

	@SuppressWarnings("unchecked")
	@Transactional
	public List<Workshop> findUpcoming() {

		List<Workshop> workshops = null;

		workshops = (List<Workshop>) getSession()
				.createQuery("select x from " + getDomainClassName() + " x where x.dateTime > :now")
				.setDate("now", new Date()).list();

		return workshops;
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public List<Workshop> findUpcomingByUser(String username) {

		List<Workshop> workshops = null;

		workshops = (List<Workshop>) getSession()
				.createQuery(
						"select x from " + getDomainClassName()
								+ " x join x.workshopMembers wm join wm.projectMember pm " + "where x.dateTime >= :now "
								+ "and pm.fabber.username = :username").setDate("now", new Date())
				.setString("username", username).list();

		return workshops;
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public List<Workshop> findPastByUser(String username) {

		List<Workshop> workshops = null;

		workshops = (List<Workshop>) getSession()
				.createQuery(
						"select x from " + getDomainClassName()
								+ " x join x.workshopMembers wm join wm.projectMember pm " + "where x.dateTime < :now "
								+ "and pm.fabber.username = :username").setDate("now", new Date())
				.setString("username", username).list();

		return workshops;
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public List<Workshop> findUpcomingByProject(Integer idProject) {

		List<Workshop> workshops = null;

		workshops = (List<Workshop>) getSession()
				.createQuery(
						"select x from " + getDomainClassName() + " x where x.project.id = :idProject "
								+ "and x.dateTime >= :now").setInteger("idProject", idProject).setDate("now", new Date())
				.list();

		return workshops;
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public List<Fabber> findCoordinators(Integer idWorkshop) {

		List<Fabber> fabbers = null;

		fabbers = (List<Fabber>) getSession()
				.createQuery(
						"select x from Fabber x join x.projectMembers pm join pm.workshopMembers wm "
								+ "where wm.workshop.id = :idWorkshop " + "and wm.isCoordinator = 1")
				.setInteger("idWorkshop", idWorkshop).list();

		return fabbers;
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public List<Fabber> findCollaborators(Integer idWorkshop) {

		List<Fabber> fabbers = null;

		fabbers = (List<Fabber>) getSession()
				.createQuery(
						"select x from Fabber x join x.projectMembers pm join pm.workshopMembers wm "
								+ "where wm.workshop.id = :idWorkshop " + "and wm.isCoordinator = 0")
				.setInteger("idWorkshop", idWorkshop).list();

		return fabbers;
	}
}
