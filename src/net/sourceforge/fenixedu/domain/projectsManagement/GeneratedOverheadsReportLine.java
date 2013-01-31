package net.sourceforge.fenixedu.domain.projectsManagement;

import java.io.Serializable;

public class GeneratedOverheadsReportLine implements Serializable, IGeneratedOverheadsReportLine {

	private Integer explorationUnit;

	private Integer projectNumber;

	private String acronim;

	private Integer coordinatorNumber;

	private String coordinatorName;

	private String type;

	private String date;

	private String description;

	private Double revenue;

	private Double overheadPerscentage;

	private Double overheadValue;

	@Override
	public String getAcronim() {
		return acronim;
	}

	@Override
	public void setAcronim(String acronim) {
		this.acronim = acronim;
	}

	@Override
	public String getCoordinatorName() {
		return coordinatorName;
	}

	@Override
	public void setCoordinatorName(String coordinatorName) {
		this.coordinatorName = coordinatorName;
	}

	@Override
	public Integer getCoordinatorNumber() {
		return coordinatorNumber;
	}

	@Override
	public void setCoordinatorNumber(Integer coordinatorNumber) {
		this.coordinatorNumber = coordinatorNumber;
	}

	@Override
	public String getDate() {
		return date;
	}

	@Override
	public void setDate(String date) {
		this.date = date;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public Integer getExplorationUnit() {
		return explorationUnit;
	}

	@Override
	public void setExplorationUnit(Integer explorationUnit) {
		this.explorationUnit = explorationUnit;
	}

	@Override
	public Double getOverheadPerscentage() {
		return overheadPerscentage;
	}

	@Override
	public void setOverheadPerscentage(Double overheadPerscentage) {
		this.overheadPerscentage = overheadPerscentage;
	}

	@Override
	public Double getOverheadValue() {
		return overheadValue;
	}

	@Override
	public void setOverheadValue(Double overheadValue) {
		this.overheadValue = overheadValue;
	}

	@Override
	public Integer getProjectNumber() {
		return projectNumber;
	}

	@Override
	public void setProjectNumber(Integer projectNumber) {
		this.projectNumber = projectNumber;
	}

	@Override
	public Double getRevenue() {
		return revenue;
	}

	@Override
	public void setRevenue(Double revenue) {
		this.revenue = revenue;
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public void setType(String type) {
		this.type = type;
	}

}