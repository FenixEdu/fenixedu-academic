/*
 * Created on Jan 12, 2005
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.projectsManagement;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.DataTranferObject;
import net.sourceforge.fenixedu.domain.projectsManagement.IOpeningProjectFileReport;
import net.sourceforge.fenixedu.domain.projectsManagement.IProjectMemberBudget;
import net.sourceforge.fenixedu.domain.projectsManagement.IRubric;

/**
 * @author Susana Fernandes
 * 
 */
public class InfoProjectMemberBudget extends DataTranferObject {

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

    public void copyFromDomain(IProjectMemberBudget projectMemberBudget) {
        if (projectMemberBudget != null) {
            setProjectCode(projectMemberBudget.getProjectCode());
            setInstitutionCode(projectMemberBudget.getInstitutionCode());
            setInstitutionName(projectMemberBudget.getInstitutionName());
            setType(projectMemberBudget.getType());
            setOverheads(projectMemberBudget.getOverheads());
            setTransferences(projectMemberBudget.getTransferences());
            setFinancingPercentage(projectMemberBudget.getFinancingPercentage());

            if (projectMemberBudget.getRubricBudget() != null) {
                setRubricBudget(new ArrayList());
                for (int i = 0; i < projectMemberBudget.getRubricBudget().size(); i++) {
                    getRubricBudget().add(InfoRubric.newInfoFromDomain((IRubric) projectMemberBudget.getRubricBudget().get(i)));
                }
            }
        }
    }

    public static InfoProjectMemberBudget newInfoFromDomain(IProjectMemberBudget projectMemberBudget) {
        InfoProjectMemberBudget infoProjectMemberBudget = null;
        if (projectMemberBudget != null) {
            infoProjectMemberBudget = new InfoProjectMemberBudget();
            infoProjectMemberBudget.copyFromDomain(projectMemberBudget);
        }
        return infoProjectMemberBudget;
    }

}
