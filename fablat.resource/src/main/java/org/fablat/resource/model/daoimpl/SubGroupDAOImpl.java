package org.fablat.resource.model.daoimpl;

import java.util.List;

import org.fablat.resource.entities.SubGroup;
import org.fablat.resource.model.dao.SubGroupDAO;
import org.springframework.transaction.annotation.Transactional;

public class SubGroupDAOImpl extends GenericDAOImpl<SubGroup, Integer> implements SubGroupDAO {

	@SuppressWarnings("unchecked")
	@Transactional
	public List<SubGroup> findAllOrderedAsc() {
		List<SubGroup> list = null;
		list = (List<SubGroup>) getSession().createQuery("from " + getDomainClassName() + " x order by x.name asc")
				.list();

		return list;
	}
	
}
