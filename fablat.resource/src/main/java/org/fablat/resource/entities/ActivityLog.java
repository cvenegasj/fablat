package org.fablat.resource.entities;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@SuppressWarnings("serial")
@Entity
@Table(name = "ActivityLog")
public class ActivityLog implements java.io.Serializable {

	private Integer idActivityLog;
	private String level;
	private String type;
	private String visibility;
	private Date creationDateTime;
	private Fabber fabber;
	private Group group;
	private SubGroup subGroup;

	public ActivityLog() {

	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "idActivityLog", unique = true, nullable = false)
	public Integer getIdActivityLog() {
		return idActivityLog;
	}

	public void setIdActivityLog(Integer idActivityLog) {
		this.idActivityLog = idActivityLog;
	}

	@Column(name = "level", nullable = false)
	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	@Column(name = "type", nullable = false)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "visibility", nullable = false)
	public String getVisibility() {
		return visibility;
	}

	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "creationDateTime", nullable = false)
	public Date getCreationDateTime() {
		return creationDateTime;
	}

	public void setCreationDateTime(Date creationDateTime) {
		this.creationDateTime = creationDateTime;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "idFabber", nullable = false)
	public Fabber getFabber() {
		return fabber;
	}

	public void setFabber(Fabber fabber) {
		this.fabber = fabber;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "idGroup")
	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "idSubGroup")
	public SubGroup getSubGroup() {
		return subGroup;
	}

	public void setSubGroup(SubGroup subGroup) {
		this.subGroup = subGroup;
	}

}
