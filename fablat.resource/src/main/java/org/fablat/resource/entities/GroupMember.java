package org.fablat.resource.entities;

import static javax.persistence.GenerationType.IDENTITY;

import java.time.LocalDateTime;
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
import javax.persistence.OrderBy;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "GroupMember")
public class GroupMember implements java.io.Serializable {

	private Integer idGroupMember;
	private Boolean isCoordinator;
	private Boolean notificationsEnabled;
	private LocalDateTime creationDateTime;
	private Fabber fabber;
	private Group group;
	private Set<SubGroupMember> subGroupMembers = new HashSet<SubGroupMember>();

	public GroupMember() {

	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "idGroupMember", unique = true, nullable = false)
	public Integer getIdGroupMember() {
		return idGroupMember;
	}

	public void setIdGroupMember(Integer idGroupMember) {
		this.idGroupMember = idGroupMember;
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

	@Column(name = "creationDateTime", nullable = false)
	public LocalDateTime getCreationDateTime() {
		return creationDateTime;
	}

	public void setCreationDateTime(LocalDateTime creationDateTime) {
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
	@JoinColumn(name = "idGroup", nullable = false)
	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "groupMember")
	@OrderBy("date(creationDateTime) asc")
	public Set<SubGroupMember> getSubGroupMembers() {
		return subGroupMembers;
	}

	public void setSubGroupMembers(Set<SubGroupMember> subGroupMembers) {
		this.subGroupMembers = subGroupMembers;
	}
	
}
