package org.fablat.resource.model.dao;

import java.util.List;

import org.fablat.resource.entities.Fabber;
import org.fablat.resource.entities.Project;

public interface ProjectDAO extends GenericDAO<Project, Integer> {

	List<Project> findAllOrderedAsc();

	List<Project> findFabberProjects(String username);

	List<Project> findManagedProjects(String username);

	List<Project> findNotManagedProjects(String username);

	List<Fabber> findCoordinators(Integer idProject);

	List<Fabber> findCollaborators(Integer idProject);
}
