package org.fablat.resource.model.dao;

import org.fablat.resource.entities.Fabber;

public interface FabberDAO extends GenericDAO<Fabber, Long> {

	Fabber findByUsername(String username);
}
