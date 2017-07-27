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
@Table(name = "SubGroupMember")
public class SubGroupMember implements java.io.Serializable {

	private Integer idSubGroupMember;
	private Boolean isCoordinator;
	private Boolean notificationsEnabled;
	private Date creationDateTime;
	private SubGroup subGroup;
	private GroupMember groupMember;
	private Set<WorkshopTutor> workshopTutors = new HashSet<WorkshopTutor>();

	public SubGroupMember() {

	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "idSubGroupMember", unique = true, nullable = false)
	public Integer getIdSubGroupMember() {
		return idSubGroupMember;
	}

	public void setIdSubGroupMember(Integer idSubGroupMember) {
		this.idSubGroupMember = idSubGroupMember;
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
	@JoinColumn(name = "idSubGroup", nullable = false)
	public SubGroup getSubGroup() {
		return subGroup;
	}

	public void setSubGroup(SubGroup subGroup) {
		this.subGroup = subGroup;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "idGroupMember", nullable = false)
	public GroupMember getGroupMember() {
		return groupMember;
	}

	public void setGroupMember(GroupMember groupMember) {
		this.groupMember = groupMember;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "subGroupMember")
	@Fetch(FetchMode.JOIN)
	public Set<WorkshopTutor> getWorkshopTutors() {
		return workshopTutors;
	}

	public void setWorkshopTutors(Set<WorkshopTutor> workshopTutors) {
		this.workshopTutors = workshopTutors;
	}
}
