package org.fablat.resource.model.dao;

import java.time.LocalDateTime;
import java.util.List;

import org.fablat.resource.entities.Workshop;

public interface WorkshopDAO extends GenericDAO<Workshop, Integer> {
	
	List<Workshop> findAllAfterDate(LocalDateTime date);
	
	List<Workshop> findAllBySubGroup(Integer idSubGroup);

}
