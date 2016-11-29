package org.fablat.resource.entities;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "WorkshopLocation")
public class WorkshopLocation implements java.io.Serializable {

	private Integer idWorkshopLocation;
	private Workshop workshop;
	private Location location;

	public WorkshopLocation() {

	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "idWorkshopLocation", unique = true, nullable = false)
	public Integer getIdWorkshopLocation() {
		return idWorkshopLocation;
	}

	public void setIdWorkshopLocation(Integer idWorkshopLocation) {
		this.idWorkshopLocation = idWorkshopLocation;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "idWorkshop", nullable = false)
	public Workshop getWorkshop() {
		return workshop;
	}

	public void setWorkshop(Workshop workshop) {
		this.workshop = workshop;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "idLocation", nullable = false)
	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}
}
