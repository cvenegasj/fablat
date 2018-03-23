package org.fablat.resource.model.dao;

import java.util.List;

import org.fablat.resource.entities.Group;

public interface GroupDAO extends GenericDAO<Group, Integer> {

	List<Group> findByTerm(String term);
	
	List<Group> findAllOrderDate();
	
	Integer getMembersCount(Integer idGroup);
	
	Integer getSubGroupsCount(Integer idGroup);
	
	Boolean checkIfMember(Integer idGroup, String email);
	
	Boolean checkIfCoordinator(Integer idGroup, String email);

}
