package org.fablat.resource.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.fablat.resource.entities.Fabber;

import com.fasterxml.jackson.annotation.JsonView;

public class WorkshopDetailResource {

	@JsonView(View.Summary.class)
	private Integer idWorkshop;
	@JsonView(View.Summary.class)
	private Integer replicationNumber;
	@JsonView(View.Summary.class)
	private String dateTime;
	@JsonView(View.Summary.class)
	private Boolean isPaid;
	@JsonView(View.Summary.class)
	private BigDecimal price;
	@JsonView(View.Summary.class)
	private String currency;
	@JsonView(View.Summary.class)
	private String projectName;
	@JsonView(View.Summary.class)
	private String currentUserStatus;
	@JsonView(View.Summary.class)
	private List<Fabber> coordinators = new ArrayList<Fabber>();
	@JsonView(View.Summary.class)
	private List<Fabber> collaborators = new ArrayList<Fabber>();
	@JsonView(View.Summary.class)
	private List<LocationResource> locations = new ArrayList<LocationResource>();

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

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
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

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getCurrentUserStatus() {
		return currentUserStatus;
	}

	public void setCurrentUserStatus(String currentUserStatus) {
		this.currentUserStatus = currentUserStatus;
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

	public List<LocationResource> getLocations() {
		return locations;
	}

	public void setLocations(List<LocationResource> locations) {
		this.locations = locations;
	}
}
