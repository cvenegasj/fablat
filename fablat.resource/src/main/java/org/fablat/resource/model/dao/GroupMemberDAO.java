package org.fablat.resource.model.dao;

import org.fablat.resource.entities.GroupMember;

public interface GroupMemberDAO extends GenericDAO<GroupMember, Integer> {

	Integer countAllByFabberAsCoordinator(String email);

	Integer countAllByFabberAsCollaborator(String email);
	
	GroupMember findByGroupAndFabber(Integer idGroup, String email);

}
