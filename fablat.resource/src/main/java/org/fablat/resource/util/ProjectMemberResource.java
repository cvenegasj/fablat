package org.fablat.resource.util;

public class ProjectMemberResource {

	private Integer idProjectMember;
	private String fullName;
	private String email;
	private String imageUrl;

	public Integer getIdProjectMember() {
		return idProjectMember;
	}

	public void setIdProjectMember(Integer idProjectMember) {
		this.idProjectMember = idProjectMember;
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
