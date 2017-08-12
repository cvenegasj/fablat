package org.fablat.resource.model.dao;

import java.util.List;

import org.fablat.resource.entities.GroupActivity;

public interface GroupActivityDAO extends GenericDAO<GroupActivity, Integer> {
	
	// External group activities
	List<GroupActivity> findAllExternal();

	// External and Internal group activity
	List<GroupActivity> findAllByGroup(Integer idGroup);

}
