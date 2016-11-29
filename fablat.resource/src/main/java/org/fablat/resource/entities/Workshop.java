package org.fablat.resource.entities;

import static javax.persistence.GenerationType.IDENTITY;

import java.math.BigDecimal;
import java.util.Date;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@SuppressWarnings("serial")
@Entity
@Table(name = "Workshop")
public class Workshop implements java.io.Serializable {

	private Integer idWorkshop;
	private Integer replicationNumber;
	private Date dateTime;
	private Boolean isPaid;
	private BigDecimal price;
	private String currency;
	private Date creationDateTime;
	private Boolean enabled;
	private Project project;
	private Set<WorkshopMember> workshopMembers = new HashSet<WorkshopMember>();
	private Set<WorkshopLocation> workshopLocations = new HashSet<>();

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

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dateTime", nullable = false)
	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
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

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "creationDateTime", nullable = false)
	public Date getCreationDateTime() {
		return creationDateTime;
	}

	public void setCreationDateTime(Date creationDateTime) {
		this.creationDateTime = creationDateTime;
	}

	@Column(name = "enabled", nullable = false)
	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "idProject", nullable = false)
	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "workshop")
	@Fetch(FetchMode.JOIN)
	public Set<WorkshopMember> getWorkshopMembers() {
		return workshopMembers;
	}

	public void setWorkshopMembers(Set<WorkshopMember> workshopMembers) {
		this.workshopMembers = workshopMembers;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "workshop")
	@Fetch(FetchMode.JOIN)
	public Set<WorkshopLocation> getWorkshopLocations() {
		return workshopLocations;
	}

	public void setWorkshopLocations(Set<WorkshopLocation> workshopLocations) {
		this.workshopLocations = workshopLocations;
	}
}
