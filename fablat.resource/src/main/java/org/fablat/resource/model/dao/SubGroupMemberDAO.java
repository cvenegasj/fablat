package org.fablat.resource.model.dao;

import org.fablat.resource.entities.SubGroupMember;

public interface SubGroupMemberDAO extends GenericDAO<SubGroupMember, Integer> {

	SubGroupMember findBySubGroupAndFabber(Integer idSubGroup, String username);
}
