package org.fablat.resource.model.daoimpl;

import java.util.Date;
import java.util.List;

import org.fablat.resource.entities.Workshop;
import org.fablat.resource.model.dao.WorkshopDAO;
import org.springframework.transaction.annotation.Transactional;

public class WorkshopDAOImpl extends GenericDAOImpl<Workshop, Integer> implements WorkshopDAO {

	@SuppressWarnings("unchecked")
	@Transactional
	public List<Workshop> findAllBySubGroupAfterDate(Integer idSubGroup, Date date) {
		List<Workshop> list = null;
		list = (List<Workshop>) getSession()
				.createQuery(
						"select x from " + getDomainClassName() + " x "
								+ "where x.subGroup.id = :idSubGroup and x.startDateTime > :date")
				.setInteger("idSubGroup", idSubGroup)
				.setDate("date", date)
				.list();
		
		return list;
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public List<Workshop> findAllAfterDate(Date date) {
		List<Workshop> list = null;
		list = (List<Workshop>) getSession()
				.createQuery(
						"select x from " + getDomainClassName() + " x "
								+ "where x.startDateTime > :date "
								+ "order by date(x.startDateTime) asc")
				.setDate("date", date)
				.list();
		
		return list;
	}

}
