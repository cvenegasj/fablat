package org.fablat.resource.entities;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "Attendant")
public class Attendant implements java.io.Serializable {

	private Integer idAttendant;
	private String firstName;
	private String lastName;
	private String email;

	public Attendant() {

	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "idAttendant", unique = true, nullable = false)
	public Integer getIdAttendant() {
		return idAttendant;
	}

	public void setIdAttendant(Integer idAttendant) {
		this.idAttendant = idAttendant;
	}

	@Column(name = "firstName", nullable = false)
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Column(name = "lastName", nullable = false)
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Column(name = "email", nullable = false)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
