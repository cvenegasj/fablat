package org.fablat.resource.util;

import java.util.ArrayList;
import java.util.List;

import org.fablat.resource.entities.Fabber;

import com.fasterxml.jackson.annotation.JsonView;

public class ProjectDetailResource {

	@JsonView(View.Summary.class)
	private Integer idProject;
	@JsonView(View.Summary.class)
	private String name;
	@JsonView(View.Summary.class)
	private String description;
	@JsonView(View.Summary.class)
	private String reunionDay;
	@JsonView(View.Summary.class)
	private String reunionTime;
	@JsonView(View.Summary.class)
	private String lineName;
	@JsonView(View.Summary.class)
	private String currentUserStatus;
	@JsonView(View.Summary.class)
	private Boolean notificationsEnabled;
	@JsonView(View.Summary.class)
	private List<Fabber> coordinators = new ArrayList<Fabber>();
	@JsonView(View.Summary.class)
	private List<Fabber> collaborators = new ArrayList<Fabber>();

	public ProjectDetailResource() {

	}

	public Integer getIdProject() {
		return idProject;
	}

	public void setIdProject(Integer idProject) {
		this.idProject = idProject;
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

	public String getLineName() {
		return lineName;
	}

	public void setLineName(String lineName) {
		this.lineName = lineName;
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
