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

    public String getAcronim() {
	return acronim;
    }

    public void setAcronim(String acronim) {
	this.acronim = acronim;
    }

    public String getCoordinatorName() {
	return coordinatorName;
    }

    public void setCoordinatorName(String coordinatorName) {
	this.coordinatorName = coordinatorName;
    }

    public Integer getCoordinatorNumber() {
	return coordinatorNumber;
    }

    public void setCoordinatorNumber(Integer coordinatorNumber) {
	this.coordinatorNumber = coordinatorNumber;
    }

    public String getDate() {
	return date;
    }

    public void setDate(String date) {
	this.date = date;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public Integer getExplorationUnit() {
	return explorationUnit;
    }

    public void setExplorationUnit(Integer explorationUnit) {
	this.explorationUnit = explorationUnit;
    }

    public Double getOverheadPerscentage() {
	return overheadPerscentage;
    }

    public void setOverheadPerscentage(Double overheadPerscentage) {
	this.overheadPerscentage = overheadPerscentage;
    }

    public Double getOverheadValue() {
	return overheadValue;
    }

    public void setOverheadValue(Double overheadValue) {
	this.overheadValue = overheadValue;
    }

    public Integer getProjectNumber() {
	return projectNumber;
    }

    public void setProjectNumber(Integer projectNumber) {
	this.projectNumber = projectNumber;
    }

    public Double getRevenue() {
	return revenue;
    }

    public void setRevenue(Double revenue) {
	this.revenue = revenue;
    }

    public String getType() {
	return type;
    }

    public void setType(String type) {
	this.type = type;
    }

}