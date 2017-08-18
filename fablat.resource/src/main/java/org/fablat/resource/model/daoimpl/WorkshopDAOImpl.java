package org.fablat.resource.model.daoimpl;

import java.util.Date;
import java.util.List;

import org.fablat.resource.entities.Workshop;
import org.fablat.resource.model.dao.WorkshopDAO;
import org.springframework.transaction.annotation.Transactional;

public class WorkshopDAOImpl extends GenericDAOImpl<Workshop, Integer> implements WorkshopDAO {

	@Transactional
	public Integer countAllBySubGroupBeforeDate(Integer idSubGroup, Date date) {
		Integer count = null;
		count = ((Long) getSession()
				.createQuery(
						"select count(x) from " + getDomainClassName() + " x "
								+ "where x.subGroup.id = :idSubGroup and x.startDateTime < :date")
				.setInteger("idSubGroup", idSubGroup)
				.setDate("date", date)
				.iterate().next()).intValue();
		
		return count;
	}

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
								+ "where x.startDateTime > :date")
				.setDate("date", date)
				.list();
		
		return list;
	}

}
