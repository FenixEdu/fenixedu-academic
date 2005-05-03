package net.sourceforge.fenixedu.domain.projectsManagement;

import java.io.Serializable;
import java.util.List;

public interface IOpeningProjectFileReport extends Serializable {

    public abstract String getAcademicUnit();

    public abstract void setAcademicUnit(String academicUnit);

    public abstract String getAcademicUnitDescription();

    public abstract void setAcademicUnitDescription(String academicUnitDescription);

    public abstract Double getAcademicUnitOverhead();

    public abstract void setAcademicUnitOverhead(Double academicUnitOverhead);

    public abstract String getAcronym();

    public abstract void setAcronym(String acronym);

    public abstract String getBid();

    public abstract void setBid(String bid);

    public abstract String getBudgetaryMemberControl();

    public abstract void setBudgetaryMemberControl(String budgetaryMemberControl);

    public abstract String getContractNumber();

    public abstract void setContractNumber(String contractNumber);

    public abstract String getCoordination();

    public abstract void setCoordination(String coordination);

    public abstract Integer getCoordinatorCode();

    public abstract void setCoordinatorCode(Integer coordinatorCode);

    public abstract String getCoordinatorContact();

    public abstract void setCoordinatorContact(String coordinatorContact);

    public abstract String getCoordinatorName();

    public abstract void setCoordinatorName(String coordinatorName);

    public abstract Double getCoordinatorOverhead();

    public abstract void setCoordinatorOverhead(Double coordinatorOverhead);

    public abstract String getCost();

    public abstract void setCost(String cost);

    public abstract String getCurrency();

    public abstract void setCurrency(String currency);

    public abstract Integer getDuration();

    public abstract void setDuration(Integer duration);

    public abstract Integer getExplorationUnit();

    public abstract void setExplorationUnit(Integer explorationUnit);

    public abstract Double getExplorationUnitOverhead();

    public abstract void setExplorationUnitOverhead(Double explorationUnitOverhead);

    public abstract String getGeneralDirection();

    public abstract void setGeneralDirection(String generalDirection);

    public abstract String getIlegivelTaxControl();

    public abstract void setIlegivelTaxControl(String ilegivelTaxControl);

    public abstract String getInitialDate();

    public abstract void setInitialDate(String initialDate);

    public abstract Double getManagementUnitOverhead();

    public abstract void setManagementUnitOverhead(Double managementUnitOverhead);

    public abstract String getOperationalUnit();

    public abstract void setOperationalUnit(String operationalUnit);

    public abstract String getOperationalUnitDescription();

    public abstract void setOperationalUnitDescription(String operationalUnitDescription);

    public abstract Double getOperationalUnitOverhead();

    public abstract void setOperationalUnitOverhead(Double operationalUnitOverhead);

    public abstract String getOverheadsDate();

    public abstract void setOverheadsDate(String overheadsDate);

    public abstract String getParentProjectCode();

    public abstract void setParentProjectCode(String parentProjectCode);

    public abstract String getProgram();

    public abstract void setProgram(String program);

    public abstract String getProgramDescription();

    public abstract void setProgramDescription(String programDescription);

    public abstract Integer getProjectCode();

    public abstract void setProjectCode(Integer projectCode);

    public abstract List getProjectFinancialEntities();

    public abstract void setProjectFinancialEntities(List projectFinancialEntities);

    public abstract List getProjectInvestigationTeam();

    public abstract void setProjectInvestigationTeam(List projectInvestigationTeam);

    public abstract List getProjectMembersBudget();

    public abstract void setProjectMembersBudget(List projectMembersBudget);

    public abstract String getProjectOrigin();

    public abstract void setProjectOrigin(String projectOrigin);

    public abstract List getProjectRubricBudget();

    public abstract void setProjectRubricBudget(List projectRubricBudget);

    public abstract String getProjectType();

    public abstract void setProjectType(String projectType);

    public abstract String getSummary();

    public abstract void setSummary(String summary);

    public abstract String getTaxControl();

    public abstract void setTaxControl(String taxControl);

    public abstract String getTitle();

    public abstract void setTitle(String title);

}