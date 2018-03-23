package org.fablat.resource.model.dao;

import java.util.List;

import org.fablat.resource.entities.SubGroup;

public interface SubGroupDAO extends GenericDAO<SubGroup, Integer> {

	List<SubGroup> findAllOrderedAsc();
	
	List<SubGroup> findAllByGroup(Integer idGroup);
	
	Integer getMembersCount(Integer idSubGroup);
	
	Integer getWorkshopsCount(Integer idSubGroup);

}
