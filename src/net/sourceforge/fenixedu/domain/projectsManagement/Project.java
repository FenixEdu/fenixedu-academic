/*
 * Created on Jan 11, 2005
 *
 */
package net.sourceforge.fenixedu.domain.projectsManagement;

import java.io.Serializable;

import org.apache.struts.util.LabelValueBean;

/**
 * @author Susana Fernandes
 * 
 */
public class Project implements IProject, Serializable {
    private String projectCode;

    private String title;

    private Integer coordinatorCode;

    private String coordinatorName;

    private String origin;

    private LabelValueBean type;

    private String cost;

    private String coordination;

    private Integer explorationUnit;

    public Project() {
    }

    public String getCoordination() {
        return coordination;
    }

    public void setCoordination(String coordination) {
        this.coordination = coordination;
    }

    public String getCoordinatorName() {
        return coordinatorName;
    }

    public void setCoordinatorName(String coordinatorName) {
        this.coordinatorName = coordinatorName;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public Integer getExplorationUnit() {
        return explorationUnit;
    }

    public void setExplorationUnit(Integer explorationUnit) {
        this.explorationUnit = explorationUnit;
    }

    public Integer getCoordinatorCode() {
        return coordinatorCode;
    }

    public void setCoordinatorCode(Integer coordinatorCode) {
        this.coordinatorCode = coordinatorCode;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LabelValueBean getType() {
        return type;
    }

    public void setType(LabelValueBean type) {
        this.type = type;
    }

    public boolean equals(Object obj) {
        if (obj instanceof IProject) {
            final IProject project = (IProject) obj;
            return this.getProjectCode().equals(project.getProjectCode());
        }
        return false;
    }

}
