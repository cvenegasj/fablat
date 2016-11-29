package org.fablat.resource.model.dao;

import java.util.List;

import org.fablat.resource.entities.Fabber;
import org.fablat.resource.entities.Workshop;

public interface WorkshopDAO extends GenericDAO<Workshop, Integer> {

	List<Workshop> findUpcoming();

	List<Workshop> findUpcomingByUser(String username);
	
	List<Workshop> findPastByUser(String username);

	List<Workshop> findUpcomingByProject(Integer idProject);

	List<Fabber> findCoordinators(Integer idWorkshop);

	List<Fabber> findCollaborators(Integer idWorkshop);
}
