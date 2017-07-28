package org.fablat.resource.dto;

public class WorkshopTutorDTO {

	private Integer idWorkshopTutor;
	private String firstName;
	private String lastName;
	private String email;
	private Integer fabberId;

	public Integer getIdWorkshopTutor() {
		return idWorkshopTutor;
	}

	public void setIdWorkshopTutor(Integer idWorkshopTutor) {
		this.idWorkshopTutor = idWorkshopTutor;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getFabberId() {
		return fabberId;
	}

	public void setFabberId(Integer fabberId) {
		this.fabberId = fabberId;
	}

}
