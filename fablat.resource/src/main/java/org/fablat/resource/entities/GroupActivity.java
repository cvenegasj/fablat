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
@Table(name = "GroupActivity")
public class GroupActivity implements java.io.Serializable {

	private Integer idGroupActivity;
	private String type;
	private String visibility;
	private Group group;
	private GroupMember groupMember;

	public GroupActivity() {

	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "idGroupActivity", unique = true, nullable = false)
	public Integer getIdGroupActivity() {
		return idGroupActivity;
	}

	public void setIdGroupActivity(Integer idGroupActivity) {
		this.idGroupActivity = idGroupActivity;
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
	@JoinColumn(name = "idGroup", nullable = false)
	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "idGroupMember", nullable = false)
	public GroupMember getGroupMember() {
		return groupMember;
	}

	public void setGroupMember(GroupMember groupMember) {
		this.groupMember = groupMember;
	}

}
