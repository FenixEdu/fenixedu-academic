package net.sourceforge.fenixedu.domain.projectsManagement;

import java.io.Serializable;

public interface IGeneratedOverheadsReportLine extends Serializable {

    public abstract String getAcronim();

    public abstract void setAcronim(String acronim);

    public abstract String getCoordinatorName();

    public abstract void setCoordinatorName(String coordinatorName);

    public abstract Integer getCoordinatorNumber();

    public abstract void setCoordinatorNumber(Integer coordinatorNumber);

    public abstract String getDate();

    public abstract void setDate(String date);

    public abstract String getDescription();

    public abstract void setDescription(String description);

    public abstract Integer getExplorationUnit();

    public abstract void setExplorationUnit(Integer explorationUnit);

    public abstract Double getOverheadPerscentage();

    public abstract void setOverheadPerscentage(Double overheadPerscentage);

    public abstract Double getOverheadValue();

    public abstract void setOverheadValue(Double overheadValue);

    public abstract Integer getProjectNumber();

    public abstract void setProjectNumber(Integer projectNumber);

    public abstract Double getRevenue();

    public abstract void setRevenue(Double revenue);

    public abstract String getType();

    public abstract void setType(String type);

}