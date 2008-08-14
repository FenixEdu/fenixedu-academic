package net.sourceforge.fenixedu.domain.projectsManagement;

import java.io.Serializable;

public class TransferedOverheadsReportLine implements Serializable, ITransferedOverheadsReportLine {

    private Integer explorationUnit;

    private String movementId;

    private String date;

    private String type;

    private String description;

    private Double overheadValue;

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

    public String getMovementId() {
	return movementId;
    }

    public void setMovementId(String movementId) {
	this.movementId = movementId;
    }

    public Double getOverheadValue() {
	return overheadValue;
    }

    public void setOverheadValue(Double overheadValue) {
	this.overheadValue = overheadValue;
    }

    public String getType() {
	return type;
    }

    public void setType(String type) {
	this.type = type;
    }

}