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

import org.fablat.resource.util.View;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonView;

@SuppressWarnings("serial")
@Entity
@Table(name = "SubGroup")
public class SubGroup implements java.io.Serializable {

	@JsonView(View.Summary.class)
	private Integer idSubGroup;
	@JsonView(View.Summary.class)
	private String name;
	@JsonView(View.Summary.class)
	private String description;
	private String reunionDay;
	private Date reunionTime;
	private String mainUrl;
	private String secondaryUrl;
	private String photoUrl;
	private Date creationDateTime;
	private Boolean enabled;
	@JsonView(View.Summary.class)
	private Group group;
	private Set<SubGroupMember> subGroupMembers = new HashSet<SubGroupMember>();
	private Set<WorkshopEvent> workshopEvents = new HashSet<WorkshopEvent>();

	public SubGroup() {

	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "idSubGroup", unique = true, nullable = false)
	public Integer getIdSubGroup() {
		return idSubGroup;
	}

	public void setIdSubGroup(Integer idSubGroup) {
		this.idSubGroup = idSubGroup;
	}

	@Column(name = "name", nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "description")
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

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "idGroup", nullable = false)
	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "subGroup")
	@Fetch(FetchMode.JOIN)
	public Set<SubGroupMember> getSubGroupMembers() {
		return subGroupMembers;
	}

	public void setSubGroupMembers(Set<SubGroupMember> subGroupMembers) {
		this.subGroupMembers = subGroupMembers;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "subGroup")
	@Fetch(FetchMode.JOIN)
	public Set<WorkshopEvent> getWorkshopEvents() {
		return workshopEvents;
	}

	public void setWorkshopEvents(Set<WorkshopEvent> workshopEvents) {
		this.workshopEvents = workshopEvents;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idSubGroup == null) ? 0 : idSubGroup.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SubGroup other = (SubGroup) obj;
		if (idSubGroup == null) {
			if (other.idSubGroup != null)
				return false;
		} else if (!idSubGroup.equals(other.idSubGroup))
			return false;
		return true;
	}
}
