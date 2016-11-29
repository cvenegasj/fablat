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
@Table(name = "ReplicationAttendant")
public class ReplicationAttendant implements java.io.Serializable {

	private Integer idReplicationAttendant;
	private String status;
	private Attendant attendant;
	private Workshop replication;

	public ReplicationAttendant() {

	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "idReplicationAttendant", unique = true, nullable = false)
	public Integer getIdReplicationAttendant() {
		return idReplicationAttendant;
	}

	public void setIdReplicationAttendant(Integer idReplicationAttendant) {
		this.idReplicationAttendant = idReplicationAttendant;
	}

	@Column(name = "status", nullable = false)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "idAttendant", nullable = false)
	public Attendant getAttendant() {
		return attendant;
	}

	public void setAttendant(Attendant attendant) {
		this.attendant = attendant;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "idReplication", nullable = false)
	public Workshop getReplication() {
		return replication;
	}

	public void setReplication(Workshop replication) {
		this.replication = replication;
	}
}
