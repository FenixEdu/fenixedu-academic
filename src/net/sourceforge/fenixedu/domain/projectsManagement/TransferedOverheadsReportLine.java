package net.sourceforge.fenixedu.domain.projectsManagement;

import java.io.Serializable;

public class TransferedOverheadsReportLine implements Serializable, ITransferedOverheadsReportLine {

    private Integer explorationUnit;

    private String movementId;

    private String date;

    private String type;

    private String description;

    private Double overheadValue;

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
    public String getMovementId() {
        return movementId;
    }

    @Override
    public void setMovementId(String movementId) {
        this.movementId = movementId;
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
    public String getType() {
        return type;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }

}