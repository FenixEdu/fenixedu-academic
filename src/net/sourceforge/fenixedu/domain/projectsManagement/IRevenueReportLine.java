/*
 * Created on Jan 27, 2005
 *
 */
package net.sourceforge.fenixedu.domain.projectsManagement;

import java.io.Serializable;

/**
 * @author Susana Fernandes
 * 
 */
public interface IRevenueReportLine extends Serializable {
    public abstract String getDate();

    public abstract void setDate(String date);

    public abstract String getDescription();

    public abstract void setDescription(String description);

    public abstract String getFinancialEntity();

    public abstract void setFinancialEntity(String financialEntity);

    public abstract String getMovementId();

    public abstract void setMovementId(String movementId);

    public abstract Integer getProjectCode();

    public abstract void setProjectCode(Integer projectCode);

    public abstract Integer getRubric();

    public abstract void setRubric(Integer rubric);

    public abstract Double getValue();

    public abstract void setValue(Double value);
}