package org.fablat.resource.dto;

public class GroupMemberDTO {

	private Integer idGroupMember;
	private String firstName;
	private String lastName;
	private String email;
	private Boolean isCoordinator;
	private Boolean notificationEnabled;
	private String creationDateTime;
	private Integer fabberId;

	public Integer getIdGroupMember() {
		return idGroupMember;
	}

	public void setIdGroupMember(Integer idGroupMember) {
		this.idGroupMember = idGroupMember;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Boolean getIsCoordinator() {
		return isCoordinator;
	}

	public void setIsCoordinator(Boolean isCoordinator) {
		this.isCoordinator = isCoordinator;
	}

	public Boolean getNotificationEnabled() {
		return notificationEnabled;
	}

	public void setNotificationEnabled(Boolean notificationEnabled) {
		this.notificationEnabled = notificationEnabled;
	}

	public String getCreationDateTime() {
		return creationDateTime;
	}

	public void setCreationDateTime(String creationDateTime) {
		this.creationDateTime = creationDateTime;
	}

	public Integer getFabberId() {
		return fabberId;
	}

	public void setFabberId(Integer fabberId) {
		this.fabberId = fabberId;
	}

}
