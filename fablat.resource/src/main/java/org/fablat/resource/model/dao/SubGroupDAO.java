package org.fablat.resource.model.dao;

import java.util.List;

import org.fablat.resource.entities.Fabber;
import org.fablat.resource.entities.SubGroup;

public interface SubGroupDAO extends GenericDAO<SubGroup, Integer> {

	List<SubGroup> findAllOrderedAsc();

	List<SubGroup> findFabberSubGroups(String username);

	List<SubGroup> findManagedSubGroups(String username);
	
	List<SubGroup> findManagedSubGroups(Long idFabber);

	List<SubGroup> findNotManagedSubGroups(String username);
	
	List<SubGroup> findNotManagedSubGroups(Long idFabber);

	List<Fabber> findCoordinators(Integer idSubGroup);

	List<Fabber> findCollaborators(Integer idSubGroup);
}
