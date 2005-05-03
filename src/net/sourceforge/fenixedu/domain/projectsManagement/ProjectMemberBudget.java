/*
 * Created on Jan 12, 2005
 *
 */
package net.sourceforge.fenixedu.domain.projectsManagement;

import java.io.Serializable;
import java.util.List;

/**
 * @author Susana Fernandes
 * 
 */
public class ProjectMemberBudget implements Serializable, IProjectMemberBudget {

    private Integer projectCode;

    private String institutionCode;

    private String institutionName;

    private String type;

    private String overheads;

    private String transferences;

    private Integer financingPercentage;

    private List rubricBudget;

    public Integer getFinancingPercentage() {
        return financingPercentage;
    }

    public void setFinancingPercentage(Integer financingPercentage) {
        this.financingPercentage = financingPercentage;
    }

    public String getInstitutionCode() {
        return institutionCode;
    }

    public void setInstitutionCode(String institutionCode) {
        this.institutionCode = institutionCode;
    }

    public String getInstitutionName() {
        return institutionName;
    }

    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }

    public String getOverheads() {
        return overheads;
    }

    public void setOverheads(String overheads) {
        this.overheads = overheads;
    }

    public Integer getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(Integer projectCode) {
        this.projectCode = projectCode;
    }

    public List getRubricBudget() {
        return rubricBudget;
    }

    public void setRubricBudget(List rubricBudget) {
        this.rubricBudget = rubricBudget;
    }

    public String getTransferences() {
        return transferences;
    }

    public void setTransferences(String transferences) {
        this.transferences = transferences;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
