/*
 * Created on Jan 26, 2005
 *
 */
package net.sourceforge.fenixedu.domain.projectsManagement;

import java.io.Serializable;

/**
 * @author Susana Fernandes
 * 
 */
public interface IExpensesReportLine extends Serializable {
    public abstract String getDate();

    public abstract void setDate(String date);

    public abstract String getDescription();

    public abstract void setDescription(String description);

    public abstract String getMember();

    public abstract void setMember(String member);

    public abstract String getMovementId();

    public abstract void setMovementId(String movementId);

    public abstract Integer getProjectCode();

    public abstract void setProjectCode(Integer projectCode);

    public abstract Integer getRubric();

    public abstract void setRubric(Integer rubric);

    public abstract Double getTax();

    public abstract void setTax(Double tax);

    public abstract Double getTotal();

    public abstract void setTotal(Double total);

    public abstract String getType();

    public abstract void setType(String type);

    public abstract Double getValue();

    public abstract void setValue(Double value);
}