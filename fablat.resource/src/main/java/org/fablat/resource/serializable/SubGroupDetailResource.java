package org.fablat.resource.serializable;

import java.util.ArrayList;
import java.util.List;

import org.fablat.resource.entities.Fabber;
import org.fablat.resource.util.View;
import org.fablat.resource.util.View.Summary;

import com.fasterxml.jackson.annotation.JsonView;

public class SubGroupDetailResource {

	@JsonView(View.Summary.class)
	private Integer idSubGroup;
	@JsonView(View.Summary.class)
	private String name;
	@JsonView(View.Summary.class)
	private String description;
	@JsonView(View.Summary.class)
	private String reunionDay;
	@JsonView(View.Summary.class)
	private String reunionTime;
	@JsonView(View.Summary.class)
	private String groupName;
	@JsonView(View.Summary.class)
	private String currentUserStatus;
	@JsonView(View.Summary.class)
	private Boolean notificationsEnabled;
	@JsonView(View.Summary.class)
	private List<Fabber> coordinators = new ArrayList<Fabber>();
	@JsonView(View.Summary.class)
	private List<Fabber> collaborators = new ArrayList<Fabber>();

	public SubGroupDetailResource() {

	}

	public Integer getIdSubGroup() {
		return idSubGroup;
	}

	public void setIdSubGroup(Integer idSubGroup) {
		this.idSubGroup = idSubGroup;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getReunionDay() {
		return reunionDay;
	}

	public void setReunionDay(String reunionDay) {
		this.reunionDay = reunionDay;
	}

	public String getReunionTime() {
		return reunionTime;
	}

	public void setReunionTime(String reunionTime) {
		this.reunionTime = reunionTime;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getCurrentUserStatus() {
		return currentUserStatus;
	}

	public void setCurrentUserStatus(String currentUserStatus) {
		this.currentUserStatus = currentUserStatus;
	}

	public Boolean getNotificationsEnabled() {
		return notificationsEnabled;
	}

	public void setNotificationsEnabled(Boolean notificationsEnabled) {
		this.notificationsEnabled = notificationsEnabled;
	}

	public List<Fabber> getCoordinators() {
		return coordinators;
	}

	public void setCoordinators(List<Fabber> coordinators) {
		this.coordinators = coordinators;
	}

	public List<Fabber> getCollaborators() {
		return collaborators;
	}

	public void setCollaborators(List<Fabber> collaborators) {
		this.collaborators = collaborators;
	}
}
