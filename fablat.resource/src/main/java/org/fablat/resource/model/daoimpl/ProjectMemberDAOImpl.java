package org.fablat.resource.model.daoimpl;

import org.fablat.resource.entities.ProjectMember;
import org.fablat.resource.model.dao.ProjectMemberDAO;
import org.springframework.transaction.annotation.Transactional;

public class ProjectMemberDAOImpl extends GenericDAOImpl<ProjectMember, Integer> implements ProjectMemberDAO {

	@Transactional
	@Override
	public ProjectMember findByProjectAndFabber(Integer idProject, String username) {

		ProjectMember pm = null;

		pm = (ProjectMember) getSession()
				.createQuery(
						"from " + getDomainClassName() + " x where x.project.id = :idProject "
								+ "and x.fabber.username = :username").setInteger("idProject", idProject)
				.setString("username", username).setMaxResults(1).uniqueResult();

		return pm;
	}
}
