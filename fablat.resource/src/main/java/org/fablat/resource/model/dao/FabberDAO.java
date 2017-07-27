package org.fablat.resource.model.dao;

import org.fablat.resource.entities.Fabber;

public interface FabberDAO extends GenericDAO<Fabber, Integer> {

	Fabber findByEmail(String email);
	
	Integer countAll();
}
