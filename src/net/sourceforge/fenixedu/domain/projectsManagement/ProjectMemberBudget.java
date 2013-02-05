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

    private String projectCode;

    private String institutionCode;

    private String institutionName;

    private String type;

    private String overheads;

    private String transferences;

    private Integer financingPercentage;

    private List rubricBudget;

    @Override
    public Integer getFinancingPercentage() {
        return financingPercentage;
    }

    @Override
    public void setFinancingPercentage(Integer financingPercentage) {
        this.financingPercentage = financingPercentage;
    }

    @Override
    public String getInstitutionCode() {
        return institutionCode;
    }

    @Override
    public void setInstitutionCode(String institutionCode) {
        this.institutionCode = institutionCode;
    }

    @Override
    public String getInstitutionName() {
        return institutionName;
    }

    @Override
    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }

    @Override
    public String getOverheads() {
        return overheads;
    }

    @Override
    public void setOverheads(String overheads) {
        this.overheads = overheads;
    }

    @Override
    public String getProjectCode() {
        return projectCode;
    }

    @Override
    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    @Override
    public List getRubricBudget() {
        return rubricBudget;
    }

    @Override
    public void setRubricBudget(List rubricBudget) {
        this.rubricBudget = rubricBudget;
    }

    @Override
    public String getTransferences() {
        return transferences;
    }

    @Override
    public void setTransferences(String transferences) {
        this.transferences = transferences;
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
