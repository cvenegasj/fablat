package org.fablat.resource.model.daoimpl;

import java.time.LocalDateTime;
import java.util.List;

import org.fablat.resource.entities.Workshop;
import org.fablat.resource.model.dao.WorkshopDAO;
import org.springframework.transaction.annotation.Transactional;

public class WorkshopDAOImpl extends GenericDAOImpl<Workshop, Integer> implements WorkshopDAO {

	@SuppressWarnings("unchecked")
	@Transactional
	public List<Workshop> findAllAfterDate(LocalDateTime date) {
		List<Workshop> list = null;
		list = (List<Workshop>) getSession()
				.createQuery(
						"select x from " + getDomainClassName() + " x "
								+ "where x.startDateTime > :date "
								+ "order by date(x.startDateTime) asc")
				.setParameter("date", date)
				.list();
		
		return list;
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public List<Workshop> findAllBySubGroup(Integer idSubGroup) {
		List<Workshop> list = null;
		list = (List<Workshop>) getSession()
				.createQuery(
						"select x from " + getDomainClassName() + " x "
								+ "where x.subGroup.id = :idSubGroup ")
				.setParameter("idSubGroup", idSubGroup)
				.list();
		
		return list;
	}

}
