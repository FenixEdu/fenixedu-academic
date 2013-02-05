/*
 * Created on Feb 14, 2005
 *
 */
package net.sourceforge.fenixedu.domain.projectsManagement;

import java.util.List;

/**
 * @author Susana Fernandes
 * 
 */
public interface IMovementReport {
    public abstract String getParentDate();

    public abstract void setParentDate(String parentDate);

    public abstract String getParentDescription();

    public abstract void setParentDescription(String parentDescription);

    public abstract String getParentMovementId();

    public abstract void setParentMovementId(String parentMovementId);

    public abstract String getParentProjectCode();

    public abstract void setParentProjectCode(String parentProjectCode);

    public abstract Integer getParentRubricId();

    public abstract void setParentRubricId(Integer parentRubricId);

    public abstract String getParentType();

    public abstract void setParentType(String parentType);

    public abstract Double getParentValue();

    public abstract void setParentValue(Double parentValue);

    public abstract List getMovements();

    public abstract void setMovements(List movements);
}