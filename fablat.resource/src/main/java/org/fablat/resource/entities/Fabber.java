package org.fablat.resource.entities;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@SuppressWarnings("serial")
@Entity
@Table(name = "Fabber")
public class Fabber implements java.io.Serializable {

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
	private Boolean enabled;
	private Lab lab;
	private Set<GroupMember> groupMembers = new HashSet<GroupMember>();
	private Set<RoleFabber> roleFabbers = new HashSet<RoleFabber>();

	// rest of the attributes are inherited from fablabs.io API

	public Fabber() {

	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "idFabber", unique = true, nullable = false)
	public Integer getIdFabber() {
		return idFabber;
	}

	public void setIdFabber(Integer idFabber) {
		this.idFabber = idFabber;
	}

	@Column(name = "email", unique = true, nullable = false)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "password", nullable = false)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	@Column(name = "isFabAcademyGrad", nullable = false)
	public Boolean getIsFabAcademyGrad() {
		return isFabAcademyGrad;
	}

	public void setIsFabAcademyGrad(Boolean isFabAcademyGrad) {
		this.isFabAcademyGrad = isFabAcademyGrad;
	}

	@Column(name = "fabAcademyGradYear")
	public Integer getFabAcademyGradYear() {
		return fabAcademyGradYear;
	}

	public void setFabAcademyGradYear(Integer fabAcademyGradYear) {
		this.fabAcademyGradYear = fabAcademyGradYear;
	}

	@Column(name = "cellPhoneNumber", nullable = false)
	public String getCellPhoneNumber() {
		return cellPhoneNumber;
	}

	public void setCellPhoneNumber(String cellPhoneNumber) {
		this.cellPhoneNumber = cellPhoneNumber;
	}

	@Column(name = "isNomade", nullable = false)
	public Boolean getIsNomade() {
		return isNomade;
	}

	public void setIsNomade(Boolean isNomade) {
		this.isNomade = isNomade;
	}

	@Column(name = "mainQuote")
	public String getMainQuote() {
		return mainQuote;
	}

	public void setMainQuote(String mainQuote) {
		this.mainQuote = mainQuote;
	}

	@Column(name = "city")
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Column(name = "country")
	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@Column(name = "weekGoal")
	public String getWeekGoal() {
		return weekGoal;
	}

	public void setWeekGoal(String weekGoal) {
		this.weekGoal = weekGoal;
	}

	@Column(name = "avatarUrl")
	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	@Column(name = "enabled", nullable = false)
	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "idLab", nullable = true)
	public Lab getLab() {
		return lab;
	}

	public void setLab(Lab lab) {
		this.lab = lab;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "fabber")
	@Fetch(FetchMode.JOIN)
	public Set<GroupMember> getGroupMembers() {
		return groupMembers;
	}

	public void setGroupMembers(Set<GroupMember> groupMembers) {
		this.groupMembers = groupMembers;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "fabber")
	@Fetch(FetchMode.JOIN)
	public Set<RoleFabber> getRoleFabbers() {
		return roleFabbers;
	}

	public void setRoleFabbers(Set<RoleFabber> roleFabbers) {
		this.roleFabbers = roleFabbers;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idFabber == null) ? 0 : idFabber.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Fabber other = (Fabber) obj;
		if (idFabber == null) {
			if (other.idFabber != null)
				return false;
		} else if (!idFabber.equals(other.idFabber))
			return false;
		return true;
	}
}
