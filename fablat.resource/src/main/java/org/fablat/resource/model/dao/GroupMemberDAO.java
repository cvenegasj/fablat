package org.fablat.resource.model.dao;

import java.util.List;

import org.fablat.resource.entities.GroupMember;

public interface GroupMemberDAO extends GenericDAO<GroupMember, Integer> {

	Integer countAllByFabberAsCoordinator(String email);

	Integer countAllByFabberAsCollaborator(String email);
	
	GroupMember findByGroupAndFabber(Integer idGroup, String email);
	
	GroupMember findByGroupAndFabber(Integer idGroup, Integer idFabber);
	
	List<GroupMember> findAllByGroup(Integer idGroup);
	
	List<GroupMember> findAllByFabber(String email);
	
	List<GroupMember> findAllByFabber(Integer idFabber);

}
