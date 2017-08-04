package org.fablat.resource.model.daoimpl;

import java.util.List;

import org.fablat.resource.entities.Location;
import org.fablat.resource.model.dao.LocationDAO;
import org.springframework.transaction.annotation.Transactional;

public class LocationDAOImpl extends GenericDAOImpl<Location, Integer> implements LocationDAO {

	@SuppressWarnings("unchecked")
	@Transactional
	public List<Location> findByTerm(String term) {
		List<Location> list = null;
		list = (List<Location>) getSession()
				.createQuery("select x from " + getDomainClassName() + " x " 
						+ "left join x.lab l "
						+ "where lower(x.address1) like :term or "
						+ "lower(x.address2) like :term or "
						+ "lower(l.name) like :term")
				.setString("term", "%" + term.toLowerCase() + "%")
				.list();
		
		return list;
	}

}
