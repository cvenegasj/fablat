package org.fablat.resource.model.dao;

import java.util.List;

import org.fablat.resource.entities.Fabber;

public interface FabberDAO extends GenericDAO<Fabber, Integer> {

	Fabber findByEmail(String email);

	List<Fabber> findByTerm(String term);

	Integer countAll();
	
}
