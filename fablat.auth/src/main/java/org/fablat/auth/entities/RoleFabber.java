package org.fablat.auth.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "RoleFabber")
public class RoleFabber implements java.io.Serializable {

	private Integer idRoleFabber;
	private Role role;
	private Fabber fabber;

	public RoleFabber() {

	}

	@Id
	@Column(name = "idRoleFabber", unique = true, nullable = false)
	public Integer getIdRoleFabber() {
		return idRoleFabber;
	}

	public void setIdRoleFabber(Integer idRoleFabber) {
		this.idRoleFabber = idRoleFabber;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "idRole", nullable = false)
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "idFabber", nullable = false)
	public Fabber getFabber() {
		return fabber;
	}

	public void setFabber(Fabber fabber) {
		this.fabber = fabber;
	}
}
