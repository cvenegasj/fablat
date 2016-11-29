package org.fablat.resource.model.dao;

import java.util.List;

import org.fablat.resource.entities.Lab;

public interface LabDAO extends GenericDAO<Lab, Integer> {

	List<Lab> findByTerm(String term);
}
