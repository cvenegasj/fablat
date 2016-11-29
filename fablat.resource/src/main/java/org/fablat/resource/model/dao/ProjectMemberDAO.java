package org.fablat.resource.model.dao;

import org.fablat.resource.entities.ProjectMember;

public interface ProjectMemberDAO extends GenericDAO<ProjectMember, Integer> {

	ProjectMember findByProjectAndFabber(Integer idProject, String username);
}
