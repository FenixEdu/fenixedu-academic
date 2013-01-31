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
public class OpeningProjectFileReport implements Serializable, IOpeningProjectFileReport {

	private String projectCode;

	private String acronym;

	private Integer explorationUnit;

	private Integer coordinatorCode;

	private String coordinatorName;

	private String academicUnit;

	private String academicUnitDescription;

	private String coordinatorContact;

	private String projectOrigin;

	private String projectType;

	private String cost;

	private String coordination;

	private String operationalUnit;

	private String operationalUnitDescription;

	private String currency;

	private String bid;

	private String contractNumber;

	private String parentProjectCode;

	private String generalDirection;

	private String program;

	private String programDescription;

	private String initialDate;

	private Integer duration;

	private String title;

	private String summary;

	private String budgetaryMemberControl;

	private String taxControl;

	private String ilegivelTaxControl;

	private String overheadsDate;

	private Double managementUnitOverhead;

	private Double explorationUnitOverhead;

	private Double academicUnitOverhead;

	private Double operationalUnitOverhead;

	private Double coordinatorOverhead;

	private List<IRubric> projectFinancialEntities;

	private List<IRubric> projectRubricBudget;

	private List<IProjectMemberBudget> projectMembersBudget;

	private List<IRubric> projectInvestigationTeam;

	@Override
	public String getAcademicUnit() {
		return academicUnit;
	}

	@Override
	public void setAcademicUnit(String academicUnit) {
		this.academicUnit = academicUnit;
	}

	@Override
	public String getAcademicUnitDescription() {
		return academicUnitDescription;
	}

	@Override
	public void setAcademicUnitDescription(String academicUnitDescription) {
		this.academicUnitDescription = academicUnitDescription;
	}

	@Override
	public Double getAcademicUnitOverhead() {
		return academicUnitOverhead;
	}

	@Override
	public void setAcademicUnitOverhead(Double academicUnitOverhead) {
		this.academicUnitOverhead = academicUnitOverhead;
	}

	@Override
	public String getAcronym() {
		return acronym;
	}

	@Override
	public void setAcronym(String acronym) {
		this.acronym = acronym;
	}

	@Override
	public String getBid() {
		return bid;
	}

	@Override
	public void setBid(String bid) {
		this.bid = bid;
	}

	@Override
	public String getBudgetaryMemberControl() {
		return budgetaryMemberControl;
	}

	@Override
	public void setBudgetaryMemberControl(String budgetaryMemberControl) {
		this.budgetaryMemberControl = budgetaryMemberControl;
	}

	@Override
	public String getContractNumber() {
		return contractNumber;
	}

	@Override
	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
	}

	@Override
	public String getCoordination() {
		return coordination;
	}

	@Override
	public void setCoordination(String coordination) {
		this.coordination = coordination;
	}

	@Override
	public Integer getCoordinatorCode() {
		return coordinatorCode;
	}

	@Override
	public void setCoordinatorCode(Integer coordinatorCode) {
		this.coordinatorCode = coordinatorCode;
	}

	@Override
	public String getCoordinatorContact() {
		return coordinatorContact;
	}

	@Override
	public void setCoordinatorContact(String coordinatorContact) {
		this.coordinatorContact = coordinatorContact;
	}

	@Override
	public String getCoordinatorName() {
		return coordinatorName;
	}

	@Override
	public void setCoordinatorName(String coordinatorName) {
		this.coordinatorName = coordinatorName;
	}

	@Override
	public Double getCoordinatorOverhead() {
		return coordinatorOverhead;
	}

	@Override
	public void setCoordinatorOverhead(Double coordinatorOverhead) {
		this.coordinatorOverhead = coordinatorOverhead;
	}

	@Override
	public String getCost() {
		return cost;
	}

	@Override
	public void setCost(String cost) {
		this.cost = cost;
	}

	@Override
	public String getCurrency() {
		return currency;
	}

	@Override
	public void setCurrency(String currency) {
		this.currency = currency;
	}

	@Override
	public Integer getDuration() {
		return duration;
	}

	@Override
	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	@Override
	public Integer getExplorationUnit() {
		return explorationUnit;
	}

	@Override
	public void setExplorationUnit(Integer explorationUnit) {
		this.explorationUnit = explorationUnit;
	}

	@Override
	public Double getExplorationUnitOverhead() {
		return explorationUnitOverhead;
	}

	@Override
	public void setExplorationUnitOverhead(Double explorationUnitOverhead) {
		this.explorationUnitOverhead = explorationUnitOverhead;
	}

	@Override
	public String getGeneralDirection() {
		return generalDirection;
	}

	@Override
	public void setGeneralDirection(String generalDirection) {
		this.generalDirection = generalDirection;
	}

	@Override
	public String getIlegivelTaxControl() {
		return ilegivelTaxControl;
	}

	@Override
	public void setIlegivelTaxControl(String ilegivelTaxControl) {
		this.ilegivelTaxControl = ilegivelTaxControl;
	}

	@Override
	public String getInitialDate() {
		return initialDate;
	}

	@Override
	public void setInitialDate(String initialDate) {
		this.initialDate = initialDate;
	}

	@Override
	public Double getManagementUnitOverhead() {
		return managementUnitOverhead;
	}

	@Override
	public void setManagementUnitOverhead(Double managementUnitOverhead) {
		this.managementUnitOverhead = managementUnitOverhead;
	}

	@Override
	public String getOperationalUnit() {
		return operationalUnit;
	}

	@Override
	public void setOperationalUnit(String operationalUnit) {
		this.operationalUnit = operationalUnit;
	}

	@Override
	public String getOperationalUnitDescription() {
		return operationalUnitDescription;
	}

	@Override
	public void setOperationalUnitDescription(String operationalUnitDescription) {
		this.operationalUnitDescription = operationalUnitDescription;
	}

	@Override
	public Double getOperationalUnitOverhead() {
		return operationalUnitOverhead;
	}

	@Override
	public void setOperationalUnitOverhead(Double operationalUnitOverhead) {
		this.operationalUnitOverhead = operationalUnitOverhead;
	}

	@Override
	public String getOverheadsDate() {
		return overheadsDate;
	}

	@Override
	public void setOverheadsDate(String overheadsDate) {
		this.overheadsDate = overheadsDate;
	}

	@Override
	public String getParentProjectCode() {
		return parentProjectCode;
	}

	@Override
	public void setParentProjectCode(String parentProjectCode) {
		this.parentProjectCode = parentProjectCode;
	}

	@Override
	public String getProgram() {
		return program;
	}

	@Override
	public void setProgram(String program) {
		this.program = program;
	}

	@Override
	public String getProgramDescription() {
		return programDescription;
	}

	@Override
	public void setProgramDescription(String programDescription) {
		this.programDescription = programDescription;
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
	public List<IRubric> getProjectFinancialEntities() {
		return projectFinancialEntities;
	}

	@Override
	public void setProjectFinancialEntities(List<IRubric> projectFinancialEntities) {
		this.projectFinancialEntities = projectFinancialEntities;
	}

	@Override
	public List<IRubric> getProjectInvestigationTeam() {
		return projectInvestigationTeam;
	}

	@Override
	public void setProjectInvestigationTeam(List<IRubric> projectInvestigationTeam) {
		this.projectInvestigationTeam = projectInvestigationTeam;
	}

	@Override
	public List<IProjectMemberBudget> getProjectMembersBudget() {
		return projectMembersBudget;
	}

	@Override
	public void setProjectMembersBudget(List<IProjectMemberBudget> projectMembersBudget) {
		this.projectMembersBudget = projectMembersBudget;
	}

	@Override
	public String getProjectOrigin() {
		return projectOrigin;
	}

	@Override
	public void setProjectOrigin(String projectOrigin) {
		this.projectOrigin = projectOrigin;
	}

	@Override
	public List<IRubric> getProjectRubricBudget() {
		return projectRubricBudget;
	}

	@Override
	public void setProjectRubricBudget(List<IRubric> projectRubricBudget) {
		this.projectRubricBudget = projectRubricBudget;
	}

	@Override
	public String getProjectType() {
		return projectType;
	}

	@Override
	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}

	@Override
	public String getSummary() {
		return summary;
	}

	@Override
	public void setSummary(String summary) {
		this.summary = summary;
	}

	@Override
	public String getTaxControl() {
		return taxControl;
	}

	@Override
	public void setTaxControl(String taxControl) {
		this.taxControl = taxControl;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public void setTitle(String title) {
		this.title = title;
	}

}
