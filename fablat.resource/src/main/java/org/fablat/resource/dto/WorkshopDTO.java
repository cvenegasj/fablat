package org.fablat.resource.dto;

import java.math.BigDecimal;

public class WorkshopDTO {

	private Integer idWorkshop;
	private Integer replicationNumber;
	private String name;
	private String description;
	private String startDate;
	private String startTime;
	private String endDate;
	private String endTime;
	private Boolean isPaid;
	private BigDecimal price;
	private String currency;
	private String facebookUrl;
	private String ticketsUrl;
	private String creationDateTime;
	private Boolean enabled;
	private Integer idLocation;
	private String locationAddress;
	private String labName;
	private String locationLatitude;
	private String locationLongitude;
	private Integer subGroupId;
	private String subGroupName;

	public Integer getIdWorkshop() {
		return idWorkshop;
	}

	public void setIdWorkshop(Integer idWorkshop) {
		this.idWorkshop = idWorkshop;
	}

	public Integer getReplicationNumber() {
		return replicationNumber;
	}

	public void setReplicationNumber(Integer replicationNumber) {
		this.replicationNumber = replicationNumber;
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

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public Boolean getIsPaid() {
		return isPaid;
	}

	public void setIsPaid(Boolean isPaid) {
		this.isPaid = isPaid;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getFacebookUrl() {
		return facebookUrl;
	}

	public void setFacebookUrl(String facebookUrl) {
		this.facebookUrl = facebookUrl;
	}

	public String getTicketsUrl() {
		return ticketsUrl;
	}

	public void setTicketsUrl(String ticketsUrl) {
		this.ticketsUrl = ticketsUrl;
	}

	public String getCreationDateTime() {
		return creationDateTime;
	}

	public void setCreationDateTime(String creationDateTime) {
		this.creationDateTime = creationDateTime;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Integer getIdLocation() {
		return idLocation;
	}

	public void setIdLocation(Integer idLocation) {
		this.idLocation = idLocation;
	}

	public String getLocationAddress() {
		return locationAddress;
	}

	public void setLocationAddress(String locationAddress) {
		this.locationAddress = locationAddress;
	}

	public String getLabName() {
		return labName;
	}

	public void setLabName(String labName) {
		this.labName = labName;
	}

	public String getLocationLatitude() {
		return locationLatitude;
	}

	public void setLocationLatitude(String locationLatitude) {
		this.locationLatitude = locationLatitude;
	}

	public String getLocationLongitude() {
		return locationLongitude;
	}

	public void setLocationLongitude(String locationLongitude) {
		this.locationLongitude = locationLongitude;
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

}
