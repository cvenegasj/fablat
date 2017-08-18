package org.fablat.resource.model.dao;

import java.util.List;

import org.fablat.resource.entities.ActivityLog;

public interface ActivityLogDAO extends GenericDAO<ActivityLog, Integer> {

	// External group activities
	List<ActivityLog> findAllExternal();

	// External and Internal group activity
	List<ActivityLog> findAllByGroup(Integer idGroup);

	// External and Internal subgroup activity
	List<ActivityLog> findAllBySubGroup(Integer idSubGroup);

}
