package org.fablat.resource.serializable;

public class SubGroupMemberResource {

	private Integer idSubGroupMember;
	private String fullName;
	private String email;
	private String imageUrl;

	public Integer getIdSubGroupMember() {
		return idSubGroupMember;
	}

	public void setIdSubGroupMember(Integer idSubGroupMember) {
		this.idSubGroupMember = idSubGroupMember;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
}
