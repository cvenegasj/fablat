package org.fablat.resource.serializable;

/*
 * Custom class for retrieving necessary data for listing subgroups
 */
public class SubGroupResource {

	private Integer idSubGroup;
	private String name;
	private String groupName;
	private String currentUserStatus;
	private Boolean notificationsEnabled;
	private Integer membersCount;
	private Integer workshopsNumber;

	public SubGroupResource() {

	}

	public SubGroupResource(Integer idSubGroup, String name, String groupName, String currentUserStatus,
			Boolean notificationsEnabled, Integer membersCount) {
		this.idSubGroup = idSubGroup;
		this.name = name;
		this.groupName = groupName;
		this.currentUserStatus = currentUserStatus;
		this.notificationsEnabled = notificationsEnabled;
		this.membersCount = membersCount;
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
