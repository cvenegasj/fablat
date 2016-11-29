package org.fablat.resource.model.daoimpl;

import java.util.List;

import org.fablat.resource.entities.Lab;
import org.fablat.resource.model.dao.LabDAO;
import org.springframework.transaction.annotation.Transactional;

public class LabDAOImpl extends GenericDAOImpl<Lab, Integer> implements LabDAO {

	@SuppressWarnings("unchecked")
	@Transactional
	public List<Lab> findByTerm(String term) {

		List<Lab> labs = null;

		labs = (List<Lab>) getSession()
				.createQuery("select x from " + getDomainClassName() + " x " + "where lower(x.name) like :term")
				.setString("term", "%" + term.toLowerCase() + "%").list();

		return labs;
	}
}