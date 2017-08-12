package org.fablat.resource.dto;

import java.util.Date;

public class GroupActivityDTO {

	private Integer idGroupActivity;
	private String type;
	private String visibility;
	private String creationDateTime;
	private Date creationDateTimeRaw;
	private Integer groupId;
	private String groupName;
	private Integer fabberId;
	private String fabberFirstName;
	private String fabberLastName;
	// additional
	private String instanceType = "GroupActivity";

	public Integer getIdGroupActivity() {
		return idGroupActivity;
	}

	public void setIdGroupActivity(Integer idGroupActivity) {
		this.idGroupActivity = idGroupActivity;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getVisibility() {
		return visibility;
	}

	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}

	public String getCreationDateTime() {
		return creationDateTime;
	}

	public void setCreationDateTime(String creationDateTime) {
		this.creationDateTime = creationDateTime;
	}

	public Date getCreationDateTimeRaw() {
		return creationDateTimeRaw;
	}

	public void setCreationDateTimeRaw(Date creationDateTimeRaw) {
		this.creationDateTimeRaw = creationDateTimeRaw;
	}

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public Integer getFabberId() {
		return fabberId;
	}

	public void setFabberId(Integer fabberId) {
		this.fabberId = fabberId;
	}

	public String getFabberFirstName() {
		return fabberFirstName;
	}

	public void setFabberFirstName(String fabberFirstName) {
		this.fabberFirstName = fabberFirstName;
	}

	public String getFabberLastName() {
		return fabberLastName;
	}

	public void setFabberLastName(String fabberLastName) {
		this.fabberLastName = fabberLastName;
	}

	public String getInstanceType() {
		return instanceType;
	}

	public void setInstanceType(String instanceType) {
		this.instanceType = instanceType;
	}

}
