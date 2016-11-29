package org.fablat.resource.entities;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "WorkshopMember")
public class WorkshopMember implements java.io.Serializable {

	private Integer idWorkshopMember;
	private Boolean isCoordinator;
	private Workshop workshop;
	private ProjectMember projectMember;

	public WorkshopMember() {

	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "idWorkshopMember", unique = true, nullable = false)
	public Integer getIdWorkshopMember() {
		return idWorkshopMember;
	}

	public void setIdWorkshopMember(Integer idWorkshopMember) {
		this.idWorkshopMember = idWorkshopMember;
	}

	@Column(name = "isCoordinator", nullable = false)
	public Boolean getIsCoordinator() {
		return isCoordinator;
	}

	public void setIsCoordinator(Boolean isCoordinator) {
		this.isCoordinator = isCoordinator;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "idWorkshop", nullable = false)
	public Workshop getWorkshop() {
		return workshop;
	}

	public void setWorkshop(Workshop workshop) {
		this.workshop = workshop;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "idProjectMember", nullable = false)
	public ProjectMember getProjectMember() {
		return projectMember;
	}

	public void setProjectMember(ProjectMember projectMember) {
		this.projectMember = projectMember;
	}
}
