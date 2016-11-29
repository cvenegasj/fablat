package org.fablat.resource.entities;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@SuppressWarnings("serial")
@Entity
@Table(name = "ProjectMember")
public class ProjectMember implements java.io.Serializable {

	private Integer idProjectMember;
	private Boolean isCoordinator;
	private Boolean notificationsEnabled;
	private Date creationDateTime;
	private Fabber fabber;
	private Project project;
	private Set<WorkshopMember> workshopMembers = new HashSet<WorkshopMember>();

	public ProjectMember() {

	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "idProjectMember", unique = true, nullable = false)
	public Integer getIdProjectMember() {
		return idProjectMember;
	}

	public void setIdProjectMember(Integer idProjectMember) {
		this.idProjectMember = idProjectMember;
	}

	@Column(name = "isCoordinator", nullable = false)
	public Boolean getIsCoordinator() {
		return isCoordinator;
	}

	public void setIsCoordinator(Boolean isCoordinator) {
		this.isCoordinator = isCoordinator;
	}

	@Column(name = "notificationsEnabled", nullable = false)
	public Boolean getNotificationsEnabled() {
		return notificationsEnabled;
	}

	public void setNotificationsEnabled(Boolean notificationsEnabled) {
		this.notificationsEnabled = notificationsEnabled;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "creationDateTime", nullable = false)
	public Date getCreationDateTime() {
		return creationDateTime;
	}

	public void setCreationDateTime(Date creationDateTime) {
		this.creationDateTime = creationDateTime;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "idFabber", nullable = false)
	public Fabber getFabber() {
		return fabber;
	}

	public void setFabber(Fabber fabber) {
		this.fabber = fabber;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "idProject", nullable = false)
	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "projectMember")
	@Fetch(FetchMode.JOIN)
	public Set<WorkshopMember> getWorkshopMembers() {
		return workshopMembers;
	}

	public void setWorkshopMembers(Set<WorkshopMember> workshopMembers) {
		this.workshopMembers = workshopMembers;
	}
}
