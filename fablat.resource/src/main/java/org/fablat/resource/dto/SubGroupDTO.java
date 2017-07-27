package org.fablat.resource.dto;

import java.util.List;

public class SubGroupDTO {

	private Integer idSubGroup;
	private String name;
	private String description;
	private String reunionDay;
	private String reunionTime;
	private String mainUrl;
	private String secondaryUrl;
	private String photoUrl;
	private String creationDateTime;
	private Integer idGroup;
	private String groupName;
	private List<WorkshopDTO> workshops;
	// additional
	private Integer membersCount;
	private Boolean amIMember;
	private Boolean amICoordinator;

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

	public String getMainUrl() {
		return mainUrl;
	}

	public void setMainUrl(String mainUrl) {
		this.mainUrl = mainUrl;
	}

	public String getSecondaryUrl() {
		return secondaryUrl;
	}

	public void setSecondaryUrl(String secondaryUrl) {
		this.secondaryUrl = secondaryUrl;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	public String getCreationDateTime() {
		return creationDateTime;
	}

	public void setCreationDateTime(String creationDateTime) {
		this.creationDateTime = creationDateTime;
	}

	public Integer getIdGroup() {
		return idGroup;
	}

	public void setIdGroup(Integer idGroup) {
		this.idGroup = idGroup;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public List<WorkshopDTO> getWorkshops() {
		return workshops;
	}

	public void setWorkshops(List<WorkshopDTO> workshops) {
		this.workshops = workshops;
	}

	public Integer getMembersCount() {
		return membersCount;
	}

	public void setMembersCount(Integer membersCount) {
		this.membersCount = membersCount;
	}

	public Boolean getAmIMember() {
		return amIMember;
	}

	public void setAmIMember(Boolean amIMember) {
		this.amIMember = amIMember;
	}

	public Boolean getAmICoordinator() {
		return amICoordinator;
	}

	public void setAmICoordinator(Boolean amICoordinator) {
		this.amICoordinator = amICoordinator;
	}

}
