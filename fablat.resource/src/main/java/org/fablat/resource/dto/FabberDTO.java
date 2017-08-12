package org.fablat.resource.dto;

public class FabberDTO {

	private Integer idFabber;
	private String email;
	private String password;
	private String firstName;
	private String lastName;
	private Boolean isFabAcademyGrad;
	private Integer fabAcademyGradYear;
	private String cellPhoneNumber;
	private Boolean isNomade;
	private String mainQuote;
	private String city;
	private String country;
	private String weekGoal;
	private String avatarUrl;
	private Integer labId;
	private String labName;
	// additional
	private Integer generalScore;
	private Integer coordinatorScore;
	private Integer collaboratorScore;
	private Integer replicatorScore;

	public Integer getIdFabber() {
		return idFabber;
	}

	public void setIdFabber(Integer idFabber) {
		this.idFabber = idFabber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public Boolean getIsFabAcademyGrad() {
		return isFabAcademyGrad;
	}

	public void setIsFabAcademyGrad(Boolean isFabAcademyGrad) {
		this.isFabAcademyGrad = isFabAcademyGrad;
	}

	public Integer getFabAcademyGradYear() {
		return fabAcademyGradYear;
	}

	public void setFabAcademyGradYear(Integer fabAcademyGradYear) {
		this.fabAcademyGradYear = fabAcademyGradYear;
	}

	public String getCellPhoneNumber() {
		return cellPhoneNumber;
	}

	public void setCellPhoneNumber(String cellPhoneNumber) {
		this.cellPhoneNumber = cellPhoneNumber;
	}

	public Boolean getIsNomade() {
		return isNomade;
	}

	public void setIsNomade(Boolean isNomade) {
		this.isNomade = isNomade;
	}

	public String getMainQuote() {
		return mainQuote;
	}

	public void setMainQuote(String mainQuote) {
		this.mainQuote = mainQuote;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getWeekGoal() {
		return weekGoal;
	}

	public void setWeekGoal(String weekGoal) {
		this.weekGoal = weekGoal;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public Integer getLabId() {
		return labId;
	}

	public void setLabId(Integer labId) {
		this.labId = labId;
	}

	public String getLabName() {
		return labName;
	}

	public void setLabName(String labName) {
		this.labName = labName;
	}

	/* additional properties */
	
	public Integer getGeneralScore() {
		return generalScore;
	}

	public void setGeneralScore(Integer generalScore) {
		this.generalScore = generalScore;
	}

	public Integer getCoordinatorScore() {
		return coordinatorScore;
	}

	public void setCoordinatorScore(Integer coordinatorScore) {
		this.coordinatorScore = coordinatorScore;
	}

	public Integer getCollaboratorScore() {
		return collaboratorScore;
	}

	public void setCollaboratorScore(Integer collaboratorScore) {
		this.collaboratorScore = collaboratorScore;
	}

	public Integer getReplicatorScore() {
		return replicatorScore;
	}

	public void setReplicatorScore(Integer replicatorScore) {
		this.replicatorScore = replicatorScore;
	}

}
