package org.fablat.resource.util;

/*
 * Custom wrapper class for mapping post parameters from save request
 */
public class ProjectRequestWrapper {

	private String name;
	private Integer idGroup;
	private String description;
	private String reunionDay;
	private String reunionTime;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getIdGroup() {
		return idGroup;
	}

	public void setIdGroup(Integer idGroup) {
		this.idGroup = idGroup;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getReunionDay() {
		return reunionDay;
	}

	public void setReunionDay(String reunionDay) {
		this.reunionDay = reunionDay;
	}

	public String getReunionTime() {
		return reunionTime;
	}

	public void setReunionTime(String reunionTime) {
		this.reunionTime = reunionTime;
	}
}
