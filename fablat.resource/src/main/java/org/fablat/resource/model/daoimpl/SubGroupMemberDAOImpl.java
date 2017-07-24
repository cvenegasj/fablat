package org.fablat.resource.model.daoimpl;

import org.fablat.resource.entities.SubGroupMember;
import org.fablat.resource.model.dao.SubGroupMemberDAO;
import org.springframework.transaction.annotation.Transactional;

public class SubGroupMemberDAOImpl extends GenericDAOImpl<SubGroupMember, Integer> implements SubGroupMemberDAO {

	@Transactional
	@Override
	public SubGroupMember findBySubGroupAndFabber(Integer idSubGroup, String username) {

		SubGroupMember o = null;

		o = (SubGroupMember) getSession()
				.createQuery(
						"from " + getDomainClassName() + " x where x.subGroup.id = :idSubGroup "
								+ "and x.groupMember.fabber.username = :username").setInteger("idSubGroup", idSubGroup)
				.setString("username", username).setMaxResults(1).uniqueResult();

		return o;
	}
}
