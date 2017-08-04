package org.fablat.resource.model.dao;

import java.util.List;

import org.fablat.resource.entities.Location;

public interface LocationDAO extends GenericDAO<Location, Integer> {

	List<Location> findByTerm(String term);

}
