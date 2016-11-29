package org.fablat.resource.model.dao;

import org.fablat.resource.entities.Role;

public interface RoleDAO extends GenericDAO<Role, Integer> {

	Role findByName(String name);
}
