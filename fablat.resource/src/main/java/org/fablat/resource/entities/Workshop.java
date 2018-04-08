package org.fablat.resource.entities;

import static javax.persistence.GenerationType.IDENTITY;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@SuppressWarnings("serial")
@Entity
@Table(name = "Workshop")
public class Workshop implements java.io.Serializable {

	private Integer idWorkshop;
	private Integer replicationNumber;
	private String name;
	private String description;
	private LocalDateTime startDateTime;
	private LocalDateTime endDateTime;
	private Boolean isPaid;
	private BigDecimal price;
	private String currency;
	private String facebookUrl;
	private String ticketsUrl;
	private LocalDateTime creationDateTime;
	private Boolean enabled;
	private Location location;
	private SubGroup subGroup;
	private Set<WorkshopTutor> workshopTutors = new HashSet<WorkshopTutor>();

	public Workshop() {

	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "idWorkshop", unique = true, nullable = false)
	public Integer getIdWorkshop() {
		return idWorkshop;
	}

	public void setIdWorkshop(Integer idWorkshop) {
		this.idWorkshop = idWorkshop;
	}

	@Column(name = "replicationNumber", nullable = false)
	public Integer getReplicationNumber() {
		return replicationNumber;
	}

	public void setReplicationNumber(Integer replicationNumber) {
		this.replicationNumber = replicationNumber;
	}

	@Column(name = "name", nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "description", nullable = false)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "startDateTime", nullable = false)
	public LocalDateTime getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(LocalDateTime startDateTime) {
		this.startDateTime = startDateTime;
	}

	@Column(name = "endDateTime", nullable = false)
	public LocalDateTime getEndDateTime() {
		return endDateTime;
	}

	public void setEndDateTime(LocalDateTime endDateTime) {
		this.endDateTime = endDateTime;
	}

	@Column(name = "isPaid", nullable = false)
	public Boolean getIsPaid() {
		return isPaid;
	}

	public void setIsPaid(Boolean isPaid) {
		this.isPaid = isPaid;
	}

	@Column(name = "price", precision = 8, scale = 2)
	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	@Column(name = "currency")
	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	@Column(name = "facebookUrl")
	public String getFacebookUrl() {
		return facebookUrl;
	}

	public void setFacebookUrl(String facebookUrl) {
		this.facebookUrl = facebookUrl;
	}

	@Column(name = "ticketsUrl")
	public String getTicketsUrl() {
		return ticketsUrl;
	}

	public void setTicketsUrl(String ticketsUrl) {
		this.ticketsUrl = ticketsUrl;
	}

	@Column(name = "creationDateTime", nullable = false)
	public LocalDateTime getCreationDateTime() {
		return creationDateTime;
	}

	public void setCreationDateTime(LocalDateTime creationDateTime) {
		this.creationDateTime = creationDateTime;
	}

	@Column(name = "enabled", nullable = false)
	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "idLocation", nullable = false)
	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "idSubGroup", nullable = false)
	public SubGroup getSubGroup() {
		return subGroup;
	}

	public void setSubGroup(SubGroup subGroup) {
		this.subGroup = subGroup;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "workshop")
	@Fetch(FetchMode.JOIN)
	public Set<WorkshopTutor> getWorkshopTutors() {
		return workshopTutors;
	}

	public void setWorkshopTutors(Set<WorkshopTutor> workshopTutors) {
		this.workshopTutors = workshopTutors;
	}

}
