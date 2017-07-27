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
@Table(name = "SubGroupActivity")
public class SubGroupActivity implements java.io.Serializable {

	private Integer idSubGroupActivity;
	private String type;
	private String visibility;
	private SubGroup subGroup;
	private SubGroupMember subGroupMember;

	public SubGroupActivity() {

	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "idSubGroupActivity", unique = true, nullable = false)
	public Integer getIdSubGroupActivity() {
		return idSubGroupActivity;
	}

	public void setIdSubGroupActivity(Integer idSubGroupActivity) {
		this.idSubGroupActivity = idSubGroupActivity;
	}

	@Column(name = "type", nullable = false)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "visibility", nullable = false)
	public String getVisibility() {
		return visibility;
	}

	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "idSubGroup", nullable = false)
	public SubGroup getSubGroup() {
		return subGroup;
	}

	public void setSubGroup(SubGroup subGroup) {
		this.subGroup = subGroup;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "idSubGroupMember", nullable = false)
	public SubGroupMember getSubGroupMember() {
		return subGroupMember;
	}

	public void setSubGroupMember(SubGroupMember subGroupMember) {
		this.subGroupMember = subGroupMember;
	}

}
