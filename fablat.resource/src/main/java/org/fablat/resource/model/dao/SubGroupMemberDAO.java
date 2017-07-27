package org.fablat.resource.model.dao;

import org.fablat.resource.entities.SubGroupMember;

public interface SubGroupMemberDAO extends GenericDAO<SubGroupMember, Integer> {
	
	Integer countAllByFabberAsCoordinator(String email);

	Integer countAllByFabberAsCollaborator(String email);

	SubGroupMember findBySubGroupAndFabber(Integer idSubGroup, String username);
	
}
