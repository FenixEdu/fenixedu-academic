/*
 * Created on Jan 20, 2005
 *
 */
package net.sourceforge.fenixedu.domain.projectsManagement;

import java.io.Serializable;

import org.apache.struts.util.LabelValueBean;

/**
 * @author Susana Fernandes
 * 
 */
public interface IProject extends Serializable {
    public abstract String getCoordination();

    public abstract void setCoordination(String coordination);

    public abstract String getCoordinatorName();

    public abstract void setCoordinatorName(String coordinatorName);

    public abstract String getCost();

    public abstract void setCost(String cost);

    public abstract Integer getExplorationUnit();

    public abstract void setExplorationUnit(Integer explorationUnit);

    public abstract Integer getCoordinatorCode();

    public abstract void setCoordinatorCode(Integer coordinatorCode);

    public abstract String getOrigin();

    public abstract void setOrigin(String origin);

    public abstract String getProjectCode();

    public abstract void setProjectCode(String projectCode);

    public abstract String getTitle();

    public abstract void setTitle(String title);

    public abstract LabelValueBean getType();

    public abstract void setType(LabelValueBean type);

    public abstract boolean equals(Object obj);
}