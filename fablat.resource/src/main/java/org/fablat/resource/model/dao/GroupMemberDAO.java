package org.fablat.resource.model.dao;

import java.util.List;

import org.fablat.resource.entities.GroupMember;

public interface GroupMemberDAO extends GenericDAO<GroupMember, Integer> {

	Integer countAllByFabberAsCoordinator(String email);

	Integer countAllByFabberAsCollaborator(String email);
	
	GroupMember findByGroupAndFabber(Integer idGroup, String email);
	
	List<GroupMember> findAllByGroup(Integer idGroup);

}
