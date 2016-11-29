package org.fablat.auth.model.dao;

import org.fablat.auth.entities.Fabber;

public interface FabberDAO extends GenericDAO<Fabber, Long> {

	Fabber findByUsername(String username);
}
