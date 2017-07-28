package org.fablat.resource.model.dao;

import org.fablat.resource.entities.WorkshopTutor;

public interface WorkshopTutorDAO extends GenericDAO<WorkshopTutor, Integer> {

	Integer countAllByFabber(String email);

	WorkshopTutor findByWorkshopAndFabber(Integer idWorkshop, String email);

}
