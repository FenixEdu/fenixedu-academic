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
import net.sourceforge.fenixedu.util.projectsManagement.ExcelStyle;
import net.sourceforge.fenixedu.util.projectsManagement.ReportType;

import org.apache.poi.hssf.usermodel.HSSFSheet;

/**
 * @author Susana Fernandes
 * 
 */
public class InfoOpeningProjectFileReport extends DataTranferObject implements IReportLine {
    private Integer projectCode;

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

    private String parentProjectNumber;

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

    private List projectFinancialEntities;

    private List projectRubricBudget;

    private List projectMembersBudget;

    private List projectInvestigationTeam;

    public String getAcademicUnit() {
        return academicUnit;
    }

    public void setAcademicUnit(String academicUnit) {
        this.academicUnit = academicUnit;
    }

    public String getAcademicUnitDescription() {
        return academicUnitDescription;
    }

    public void setAcademicUnitDescription(String academicUnitDescription) {
        this.academicUnitDescription = academicUnitDescription;
    }

    public Double getAcademicUnitOverhead() {
        return academicUnitOverhead;
    }

    public void setAcademicUnitOverhead(Double academicUnitOverhead) {
        this.academicUnitOverhead = academicUnitOverhead;
    }

    public String getAcronym() {
        return acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getBudgetaryMemberControl() {
        return budgetaryMemberControl;
    }

    public void setBudgetaryMemberControl(String budgetaryMemberControl) {
        this.budgetaryMemberControl = budgetaryMemberControl;
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public String getCoordination() {
        return coordination;
    }

    public void setCoordination(String coordination) {
        this.coordination = coordination;
    }

    public Integer getCoordinatorCode() {
        return coordinatorCode;
    }

    public void setCoordinatorCode(Integer coordinatorCode) {
        this.coordinatorCode = coordinatorCode;
    }

    public String getCoordinatorContact() {
        return coordinatorContact;
    }

    public void setCoordinatorContact(String coordinatorContact) {
        this.coordinatorContact = coordinatorContact;
    }

    public String getCoordinatorName() {
        return coordinatorName;
    }

    public void setCoordinatorName(String coordinatorName) {
        this.coordinatorName = coordinatorName;
    }

    public Double getCoordinatorOverhead() {
        return coordinatorOverhead;
    }

    public void setCoordinatorOverhead(Double coordinatorOverhead) {
        this.coordinatorOverhead = coordinatorOverhead;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getExplorationUnit() {
        return explorationUnit;
    }

    public void setExplorationUnit(Integer explorationUnit) {
        this.explorationUnit = explorationUnit;
    }

    public Double getExplorationUnitOverhead() {
        return explorationUnitOverhead;
    }

    public void setExplorationUnitOverhead(Double explorationUnitOverhead) {
        this.explorationUnitOverhead = explorationUnitOverhead;
    }

    public String getGeneralDirection() {
        return generalDirection;
    }

    public void setGeneralDirection(String generalDirection) {
        this.generalDirection = generalDirection;
    }

    public String getIlegivelTaxControl() {
        return ilegivelTaxControl;
    }

    public void setIlegivelTaxControl(String ilegivelTaxControl) {
        this.ilegivelTaxControl = ilegivelTaxControl;
    }

    public String getInitialDate() {
        return initialDate;
    }

    public void setInitialDate(String initialDate) {
        this.initialDate = initialDate;
    }

    public Double getManagementUnitOverhead() {
        return managementUnitOverhead;
    }

    public void setManagementUnitOverhead(Double managementUnitOverhead) {
        this.managementUnitOverhead = managementUnitOverhead;
    }

    public String getOperationalUnit() {
        return operationalUnit;
    }

    public void setOperationalUnit(String operationalUnit) {
        this.operationalUnit = operationalUnit;
    }

    public String getOperationalUnitDescription() {
        return operationalUnitDescription;
    }

    public void setOperationalUnitDescription(String operationalUnitDescription) {
        this.operationalUnitDescription = operationalUnitDescription;
    }

    public Double getOperationalUnitOverhead() {
        return operationalUnitOverhead;
    }

    public void setOperationalUnitOverhead(Double operationalUnitOverhead) {
        this.operationalUnitOverhead = operationalUnitOverhead;
    }

    public String getOverheadsDate() {
        return overheadsDate;
    }

    public void setOverheadsDate(String overheadsDate) {
        this.overheadsDate = overheadsDate;
    }

    public String getParentProjectNumber() {
        return parentProjectNumber;
    }

    public void setParentProjectNumber(String parentProjectNumber) {
        this.parentProjectNumber = parentProjectNumber;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public String getProgramDescription() {
        return programDescription;
    }

    public void setProgramDescription(String programDescription) {
        this.programDescription = programDescription;
    }

    public Integer getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(Integer projectCode) {
        this.projectCode = projectCode;
    }

    public List getProjectFinancialEntities() {
        return projectFinancialEntities;
    }

    public void setProjectFinancialEntities(List projectFinancialEntities) {
        this.projectFinancialEntities = projectFinancialEntities;
    }

    public List getProjectInvestigationTeam() {
        return projectInvestigationTeam;
    }

    public void setProjectInvestigationTeam(List projectInvestigationTeam) {
        this.projectInvestigationTeam = projectInvestigationTeam;
    }

    public List getProjectMembersBudget() {
        return projectMembersBudget;
    }

    public void setProjectMembersBudget(List projectMembersBudget) {
        this.projectMembersBudget = projectMembersBudget;
    }

    public String getProjectOrigin() {
        return projectOrigin;
    }

    public void setProjectOrigin(String projectOrigin) {
        this.projectOrigin = projectOrigin;
    }

    public List getProjectRubricBudget() {
        return projectRubricBudget;
    }

    public void setProjectRubricBudget(List projectRubricBudget) {
        this.projectRubricBudget = projectRubricBudget;
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getTaxControl() {
        return taxControl;
    }

    public void setTaxControl(String taxControl) {
        this.taxControl = taxControl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void copyFromDomain(IOpeningProjectFileReport openingProjectFileReport) {
        if (openingProjectFileReport != null) {
            setProjectCode(openingProjectFileReport.getProjectCode());
            setAcronym(openingProjectFileReport.getAcronym());
            setExplorationUnit(openingProjectFileReport.getExplorationUnit());
            setCoordinatorCode(openingProjectFileReport.getCoordinatorCode());
            setCoordinatorName(openingProjectFileReport.getCoordinatorName());
            setAcademicUnit(openingProjectFileReport.getAcademicUnit());
            setAcademicUnitDescription(openingProjectFileReport.getAcademicUnitDescription());
            setCoordinatorContact(openingProjectFileReport.getCoordinatorContact());
            setProjectOrigin(openingProjectFileReport.getProjectOrigin());
            setProjectType(openingProjectFileReport.getProjectType());
            setCost(openingProjectFileReport.getCost());
            setCoordination(openingProjectFileReport.getCoordination());
            setOperationalUnit(openingProjectFileReport.getOperationalUnit());
            setOperationalUnitDescription(openingProjectFileReport.getOperationalUnitDescription());
            setCurrency(openingProjectFileReport.getCurrency());
            setBid(openingProjectFileReport.getBid());
            setContractNumber(openingProjectFileReport.getContractNumber());
            setParentProjectNumber(openingProjectFileReport.getParentProjectCode());
            setGeneralDirection(openingProjectFileReport.getGeneralDirection());
            setProgram(openingProjectFileReport.getProgram());
            setProgramDescription(openingProjectFileReport.getProgramDescription());
            setInitialDate(openingProjectFileReport.getInitialDate());
            setDuration(openingProjectFileReport.getDuration());
            setTitle(openingProjectFileReport.getTitle());
            setSummary(openingProjectFileReport.getSummary());
            setBudgetaryMemberControl(openingProjectFileReport.getBudgetaryMemberControl());
            setTaxControl(openingProjectFileReport.getTaxControl());
            setIlegivelTaxControl(openingProjectFileReport.getIlegivelTaxControl());
            setOverheadsDate(openingProjectFileReport.getOverheadsDate());
            setManagementUnitOverhead(openingProjectFileReport.getManagementUnitOverhead());
            setExplorationUnitOverhead(openingProjectFileReport.getExplorationUnitOverhead());
            setAcademicUnitOverhead(openingProjectFileReport.getAcademicUnitOverhead());
            setOperationalUnitOverhead(openingProjectFileReport.getOperationalUnitOverhead());
            setCoordinatorOverhead(openingProjectFileReport.getCoordinatorOverhead());
            if (openingProjectFileReport.getProjectFinancialEntities() != null) {
                setProjectFinancialEntities(new ArrayList());
                for (int i = 0; i < openingProjectFileReport.getProjectFinancialEntities().size(); i++) {
                    getProjectFinancialEntities().add(
                            InfoRubric.newInfoFromDomain((IRubric) openingProjectFileReport.getProjectFinancialEntities().get(i)));
                }
            }

            if (openingProjectFileReport.getProjectRubricBudget() != null) {
                setProjectRubricBudget(new ArrayList());
                for (int i = 0; i < openingProjectFileReport.getProjectRubricBudget().size(); i++) {
                    getProjectRubricBudget().add(InfoRubric.newInfoFromDomain((IRubric) openingProjectFileReport.getProjectRubricBudget().get(i)));
                }
            }

            if (openingProjectFileReport.getProjectMembersBudget() != null) {
                setProjectMembersBudget(new ArrayList());
                for (int i = 0; i < openingProjectFileReport.getProjectMembersBudget().size(); i++) {
                    getProjectMembersBudget().add(
                            InfoProjectMemberBudget.newInfoFromDomain((IProjectMemberBudget) openingProjectFileReport.getProjectMembersBudget()
                                    .get(i)));
                }
            }
            if (openingProjectFileReport.getProjectInvestigationTeam() != null) {
                setProjectInvestigationTeam(new ArrayList());
                for (int i = 0; i < openingProjectFileReport.getProjectInvestigationTeam().size(); i++) {
                    getProjectInvestigationTeam().add(
                            InfoRubric.newInfoFromDomain((IRubric) openingProjectFileReport.getProjectInvestigationTeam().get(i)));
                }
            }
        }
    }

    public static InfoOpeningProjectFileReport newInfoFromDomain(IOpeningProjectFileReport openingProjectFileReport) {
        InfoOpeningProjectFileReport infoOpeningProjectFileReport = null;
        if (openingProjectFileReport != null) {
            infoOpeningProjectFileReport = new InfoOpeningProjectFileReport();
            infoOpeningProjectFileReport.copyFromDomain(openingProjectFileReport);
        }
        return infoOpeningProjectFileReport;
    }

    public int getNumberOfColumns() {
        return 0;
    }

    public void getHeaderToExcel(HSSFSheet sheet, ExcelStyle excelStyle, ReportType reportType) {
    }

    public void getLineToExcel(HSSFSheet sheet, ExcelStyle excelStyle, ReportType reportType) {
    }

    public void getTotalLineToExcel(HSSFSheet sheet, ExcelStyle excelStyle, ReportType reportType) {
    }

    public Double getValue(int column) {
        return null;
    }
}
