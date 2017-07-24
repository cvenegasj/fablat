package org.fablat.resource.model.dao;

import org.fablat.resource.entities.WorkshopEventTutor;

public interface WorkshopEventTutorDAO extends GenericDAO<WorkshopEventTutor, Integer> {

	WorkshopEventTutor findByWorkshopAndFabber(Integer idWorkshop, String username);

}
