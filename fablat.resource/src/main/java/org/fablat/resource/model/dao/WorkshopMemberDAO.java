package org.fablat.resource.model.dao;

import org.fablat.resource.entities.WorkshopMember;

public interface WorkshopMemberDAO extends GenericDAO<WorkshopMember, Integer> {

	WorkshopMember findByWorkshopAndFabber(Integer idWorkshop, String username);

}
