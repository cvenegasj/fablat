package org.fablat.resource.model.dao;

import java.util.List;

import org.fablat.resource.entities.SubGroupActivity;

public interface SubGroupActivityDAO extends GenericDAO<SubGroupActivity, Integer> {

	List<SubGroupActivity> findAllExternal();

	List<SubGroupActivity> findAllBySubGroup(Integer idSubGroup);

}
