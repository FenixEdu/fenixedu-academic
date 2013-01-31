package net.sourceforge.fenixedu.domain.projectsManagement;

import java.io.Serializable;

public interface ITransferedOverheadsReportLine extends Serializable {

	public abstract String getDate();

	public abstract void setDate(String date);

	public abstract String getDescription();

	public abstract void setDescription(String description);

	public abstract Integer getExplorationUnit();

	public abstract void setExplorationUnit(Integer explorationUnit);

	public abstract String getMovementId();

	public abstract void setMovementId(String movementId);

	public abstract Double getOverheadValue();

	public abstract void setOverheadValue(Double overheadValue);

	public abstract String getType();

	public abstract void setType(String type);

}