package org.fablat.resource.dto;

import java.time.LocalDateTime;

public class ActivityLogDTO {

	private Integer idActivityLog;
	private String level;
	private String type;
	private String visibility;
	private String creationDateTime;
	private Integer fabberId;
	private String fabberFirstName;
	private String fabberLastName;
	private Integer groupId;
	private String groupName;
	private Integer subGroupId;
	private String subGroupName;
	// additional
	private LocalDateTime creationDateTimeRaw;

	public Integer getIdActivityLog() {
		return idActivityLog;
	}

	public void setIdActivityLog(Integer idActivityLog) {
		this.idActivityLog = idActivityLog;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
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

	public Integer getSubGroupId() {
		return subGroupId;
	}

	public void setSubGroupId(Integer subGroupId) {
		this.subGroupId = subGroupId;
	}

	public String getSubGroupName() {
		return subGroupName;
	}

	public void setSubGroupName(String subGroupName) {
		this.subGroupName = subGroupName;
	}

	public LocalDateTime getCreationDateTimeRaw() {
		return creationDateTimeRaw;
	}

	public void setCreationDateTimeRaw(LocalDateTime creationDateTimeRaw) {
		this.creationDateTimeRaw = creationDateTimeRaw;
	}

}
