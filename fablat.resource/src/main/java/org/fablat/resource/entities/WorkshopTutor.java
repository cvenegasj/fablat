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
@Table(name = "WorkshopTutor")
public class WorkshopTutor implements java.io.Serializable {

	private Integer idWorkshopTutor;
	private SubGroupMember subGroupMember;
	private Workshop workshop;

	public WorkshopTutor() {

	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "idWorkshopTutor", unique = true, nullable = false)
	public Integer getIdWorkshopTutor() {
		return idWorkshopTutor;
	}

	public void setIdWorkshopTutor(Integer idWorkshopTutor) {
		this.idWorkshopTutor = idWorkshopTutor;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "idSubGroupMember", nullable = false)
	public SubGroupMember getSubGroupMember() {
		return subGroupMember;
	}

	public void setSubGroupMember(SubGroupMember subGroupMember) {
		this.subGroupMember = subGroupMember;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "idWorkshop", nullable = false)
	public Workshop getWorkshop() {
		return workshop;
	}

	public void setWorkshop(Workshop workshop) {
		this.workshop = workshop;
	}

}
