package org.fablat.resource.model.dao;

import java.util.List;

import org.fablat.resource.entities.Fabber;
import org.fablat.resource.entities.WorkshopEvent;

public interface WorkshopEventDAO extends GenericDAO<WorkshopEvent, Integer> {

	List<WorkshopEvent> findUpcoming();

	List<WorkshopEvent> findUpcomingByUser(String username);
	
	List<WorkshopEvent> findPastByUser(String username);

	List<WorkshopEvent> findUpcomingBySubGroup(Integer idSubGroup);

	List<Fabber> findCoordinators(Integer idWorkshop);

	List<Fabber> findCollaborators(Integer idWorkshop);
}
