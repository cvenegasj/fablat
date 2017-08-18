package org.fablat.resource.model.dao;

import java.util.List;

import org.fablat.resource.entities.SubGroupMember;

public interface SubGroupMemberDAO extends GenericDAO<SubGroupMember, Integer> {
	
	Integer countAllByFabberAsCoordinator(String email);

	Integer countAllByFabberAsCollaborator(String email);

	SubGroupMember findBySubGroupAndFabber(Integer idSubGroup, String email);
	
	List<SubGroupMember> findAllBySubGroup(Integer idSubGroup);
	
}
