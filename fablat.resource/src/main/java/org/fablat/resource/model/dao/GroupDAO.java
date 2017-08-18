package org.fablat.resource.model.dao;

import java.util.List;

import org.fablat.resource.entities.Group;

public interface GroupDAO extends GenericDAO<Group, Integer> {

	List<Group> findByTerm(String term);
	
	List<Group> findAllOrderDate();

}
