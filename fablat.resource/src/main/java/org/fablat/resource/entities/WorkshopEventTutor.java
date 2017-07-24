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
@Table(name = "WorkshopEventTutor")
public class WorkshopEventTutor implements java.io.Serializable {

	private Integer idWorkshopEventTutor;
	private WorkshopEvent workshopEvent;
	private SubGroupMember subGroupMember;

	public WorkshopEventTutor() {

	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "idWorkshopEventTutor", unique = true, nullable = false)
	public Integer getIdWorkshopEventTutor() {
		return idWorkshopEventTutor;
	}

	public void setIdWorkshopEventTutor(Integer idWorkshopEventTutor) {
		this.idWorkshopEventTutor = idWorkshopEventTutor;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "idWorkshopEvent", nullable = false)
	public WorkshopEvent getWorkshopEvent() {
		return workshopEvent;
	}

	public void setWorkshopEvent(WorkshopEvent workshopEvent) {
		this.workshopEvent = workshopEvent;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "idSubGroupMember", nullable = false)
	public SubGroupMember getSubGroupMember() {
		return subGroupMember;
	}

	public void setSubGroupMember(SubGroupMember subGroupMember) {
		this.subGroupMember = subGroupMember;
	}
}
