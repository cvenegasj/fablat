package org.fablat.resource.serializable;

import java.math.BigDecimal;
import java.util.Set;

/*
 * Custom wrapper class for mapping post parameters from save request
 */
public class WorkshopRequestWrapper {

	private String name;
	private String date;
	private String time;
	private Boolean isPaid;
	private BigDecimal price;
	private String currency;
	private Set<SubGroupMemberResource> coordinators;
	private Set<SubGroupMemberResource> collaborators;
	private Set<LocationResource> locations;
	private Integer idSubGroup;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
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

	public Set<SubGroupMemberResource> getCoordinators() {
		return coordinators;
	}

	public void setCoordinators(Set<SubGroupMemberResource> coordinators) {
		this.coordinators = coordinators;
	}

	public Set<SubGroupMemberResource> getCollaborators() {
		return collaborators;
	}

	public void setCollaborators(Set<SubGroupMemberResource> collaborators) {
		this.collaborators = collaborators;
	}

	public Set<LocationResource> getLocations() {
		return locations;
	}

	public void setLocations(Set<LocationResource> locations) {
		this.locations = locations;
	}

	public Integer getIdSubGroup() {
		return idSubGroup;
	}

	public void setIdSubGroup(Integer idSubGroup) {
		this.idSubGroup = idSubGroup;
	}
}
