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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.fablat.resource.util.View;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonView;

@SuppressWarnings("serial")
@Entity
@Table(name = "Group")
public class Group implements java.io.Serializable {

	@JsonView(View.Summary.class)
	private Integer idGroup;
	@JsonView(View.Summary.class)
	private String name;
	private String description;
	private String reunionDay;
	private Date reunionTime;
	private String mainUrl;
	private String secondaryUrl;
	private String photoUrl;
	private Date creationDateTime;
	private Boolean enabled;
	private Set<GroupMember> groupMembers = new HashSet<GroupMember>();
	private Set<SubGroup> subGroups = new HashSet<SubGroup>();

	public Group() {

	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "idGroup", unique = true, nullable = false)
	public Integer getIdGroup() {
		return idGroup;
	}

	public void setIdGroup(Integer idGroup) {
		this.idGroup = idGroup;
	}

	@Column(name = "name", nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "description", nullable = false)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "reunionDay")
	public String getReunionDay() {
		return reunionDay;
	}

	public void setReunionDay(String reunionDay) {
		this.reunionDay = reunionDay;
	}

	@Temporal(TemporalType.TIME)
	@Column(name = "reunionTime")
	public Date getReunionTime() {
		return reunionTime;
	}

	public void setReunionTime(Date reunionTime) {
		this.reunionTime = reunionTime;
	}

	@Column(name = "mainUrl")
	public String getMainUrl() {
		return mainUrl;
	}

	public void setMainUrl(String mainUrl) {
		this.mainUrl = mainUrl;
	}

	@Column(name = "secondaryUrl")
	public String getSecondaryUrl() {
		return secondaryUrl;
	}

	public void setSecondaryUrl(String secondaryUrl) {
		this.secondaryUrl = secondaryUrl;
	}

	@Column(name = "photoUrl")
	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "creationDateTime", nullable = false)
	public Date getCreationDateTime() {
		return creationDateTime;
	}

	public void setCreationDateTime(Date creationDateTime) {
		this.creationDateTime = creationDateTime;
	}

	@Column(name = "enabled", nullable = false)
	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "group")
	@Fetch(FetchMode.JOIN)
	public Set<GroupMember> getGroupMembers() {
		return groupMembers;
	}

	public void setGroupMembers(Set<GroupMember> groupMembers) {
		this.groupMembers = groupMembers;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "group")
	@Fetch(FetchMode.JOIN)
	public Set<SubGroup> getSubGroups() {
		return subGroups;
	}

	public void setSubGroups(Set<SubGroup> subGroups) {
		this.subGroups = subGroups;
	}
}
