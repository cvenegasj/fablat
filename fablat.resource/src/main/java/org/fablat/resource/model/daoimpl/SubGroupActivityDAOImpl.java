package org.fablat.resource.model.daoimpl;

import java.util.List;

import org.fablat.resource.entities.SubGroupActivity;
import org.fablat.resource.model.dao.SubGroupActivityDAO;
import org.springframework.transaction.annotation.Transactional;

public class SubGroupActivityDAOImpl extends GenericDAOImpl<SubGroupActivity, Integer> implements SubGroupActivityDAO {

	@SuppressWarnings("unchecked")
	@Transactional
	public List<SubGroupActivity> findAllExternal() {
		List<SubGroupActivity> list = null;
		list = (List<SubGroupActivity>) getSession()
				.createQuery(
						"select x from " + getDomainClassName() + " x "
						+ "where x.visibility = 'EXTERNAL' "
						+ "order by date(x.creationDateTime) desc")
				.list();
		
		return list;
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public List<SubGroupActivity> findAllBySubGroup(Integer idSubGroup) {
		List<SubGroupActivity> list = null;
		list = (List<SubGroupActivity>) getSession()
				.createQuery(
						"select x from " + getDomainClassName() + " x "
						+ "where x.visibility = 'EXTERNAL' "
						+ "and x.subGroup.id = :idSubGroup "
						+ "order by date(x.creationDateTime) desc")
				.setInteger("idSubGroup", idSubGroup)
				.list();
		
		return list;
	}

}
