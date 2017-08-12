package org.fablat.resource.entities;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "FabberInfo")
public class FabberInfo implements java.io.Serializable {

	private Integer idFabberInfo;
	private Integer scoreGeneral;
	private Integer scoreCoordinator;
	private Integer scoreCollaborator;
	private Integer scoreReplicator;
	private Fabber fabber;

	public FabberInfo() {

	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "idFabberInfo", unique = true, nullable = false)
	public Integer getIdFabberInfo() {
		return idFabberInfo;
	}

	public void setIdFabberInfo(Integer idFabberInfo) {
		this.idFabberInfo = idFabberInfo;
	}

	@Column(name = "scoreGeneral", nullable = false)
	public Integer getScoreGeneral() {
		return scoreGeneral;
	}

	public void setScoreGeneral(Integer scoreGeneral) {
		this.scoreGeneral = scoreGeneral;
	}

	@Column(name = "scoreCoordinator", nullable = false)
	public Integer getScoreCoordinator() {
		return scoreCoordinator;
	}

	public void setScoreCoordinator(Integer scoreCoordinator) {
		this.scoreCoordinator = scoreCoordinator;
	}

	@Column(name = "scoreCollaborator", nullable = false)
	public Integer getScoreCollaborator() {
		return scoreCollaborator;
	}

	public void setScoreCollaborator(Integer scoreCollaborator) {
		this.scoreCollaborator = scoreCollaborator;
	}

	@Column(name = "scoreReplicator", nullable = false)
	public Integer getScoreReplicator() {
		return scoreReplicator;
	}

	public void setScoreReplicator(Integer scoreReplicator) {
		this.scoreReplicator = scoreReplicator;
	}

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "idFabber", nullable = false)
	public Fabber getFabber() {
		return fabber;
	}

	public void setFabber(Fabber fabber) {
		this.fabber = fabber;
	}

}
