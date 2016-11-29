package org.fablat.resource.util;

/*
 * Custom class for retrieving necessary data for listing workshops
 */
public class ProjectResource {

	private Integer idProject;
	private String name;
	private String groupName;
	private String currentUserStatus;
	private Boolean notificationsEnabled;
	private Integer membersCount;
	private Integer workshopsNumber;

	public ProjectResource() {

	}

	public ProjectResource(Integer idProject, String name, String groupName, String currentUserStatus,
			Boolean notificationsEnabled, Integer membersCount) {
		this.idProject = idProject;
		this.name = name;
		this.groupName = groupName;
		this.currentUserStatus = currentUserStatus;
		this.notificationsEnabled = notificationsEnabled;
		this.membersCount = membersCount;
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

	public Integer getMembersCount() {
		return membersCount;
	}

	public void setMembersCount(Integer membersCount) {
		this.membersCount = membersCount;
	}

	public Integer getWorkshopsNumber() {
		return workshopsNumber;
	}

	public void setWorkshopsNumber(Integer workshopsNumber) {
		this.workshopsNumber = workshopsNumber;
	}
}
