package org.fablat.resource.model.dao;

import java.util.Date;
import java.util.List;

import org.fablat.resource.entities.Workshop;

public interface WorkshopDAO extends GenericDAO<Workshop, Integer> {

	Integer countAllBySubGroupBeforeDate(Integer idSubGroup, Date date);

	List<Workshop> findAllBySubGroupAfterDate(Integer idSubGroup, Date date);

}
