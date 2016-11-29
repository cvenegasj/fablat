package org.fablat.resource.model.daoimpl;

import java.util.List;

import org.fablat.resource.entities.Fabber;
import org.fablat.resource.entities.Project;
import org.fablat.resource.model.dao.ProjectDAO;
import org.springframework.transaction.annotation.Transactional;

public class ProjectDAOImpl extends GenericDAOImpl<Project, Integer> implements ProjectDAO {

	@SuppressWarnings("unchecked")
	@Transactional
	public List<Project> findAllOrderedAsc() {

		List<Project> projects = null;

		projects = (List<Project>) getSession().createQuery("from " + getDomainClassName() + " x order by x.name asc")
				.list();

		return projects;
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public List<Project> findFabberProjects(String username) {

		List<Project> projects = null;

		projects = (List<Project>) getSession()
				.createQuery(
						"select x from " + getDomainClassName() + " x join x.projectMembers pm "
								+ "where pm.fabber.username = :username").setString("username", username).list();

		return projects;
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public List<Project> findManagedProjects(String username) {

		List<Project> projects = null;

		projects = (List<Project>) getSession()
				.createQuery(
						"select x from " + getDomainClassName() + " x join x.projectMembers pm "
								+ "where pm.fabber.username = :username " + "and pm.isCoordinator = 1")
				.setString("username", username).list();

		return projects;
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public List<Project> findNotManagedProjects(String username) {

		List<Project> projects = null;

		projects = (List<Project>) getSession()
				.createQuery(
						"select x from " + getDomainClassName() + " x join x.projectMembers pm "
								+ "where pm.fabber.username = :username " + "and pm.isCoordinator = 0")
				.setString("username", username).list();

		return projects;
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public List<Fabber> findCoordinators(Integer idProject) {

		List<Fabber> fabbers = null;

		fabbers = (List<Fabber>) getSession()
				.createQuery(
						"select x from Fabber x join x.projectMembers pm " + "where pm.project.id = :idProject "
								+ "and pm.isCoordinator = 1").setInteger("idProject", idProject).list();

		return fabbers;
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public List<Fabber> findCollaborators(Integer idProject) {

		List<Fabber> fabbers = null;

		fabbers = (List<Fabber>) getSession()
				.createQuery(
						"select x from Fabber x join x.projectMembers pm " + "where pm.project.id = :idProject "
								+ "and pm.isCoordinator = 0").setInteger("idProject", idProject).list();

		return fabbers;
	}
}
