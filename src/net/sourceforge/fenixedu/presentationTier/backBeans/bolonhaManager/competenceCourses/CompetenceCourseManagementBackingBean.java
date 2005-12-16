/*
 * Created on Dec 8, 2005
 */
package net.sourceforge.fenixedu.presentationTier.backBeans.bolonhaManager.competenceCourses;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.faces.model.SelectItem;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.bolonhaManager.EditCompetenceCourse;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.ICompetenceCourse;
import net.sourceforge.fenixedu.domain.IEmployee;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.domain.degreeStructure.RegimeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.IUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.backBeans.base.FenixBackingBean;

public class CompetenceCourseManagementBackingBean extends FenixBackingBean {
    private final ResourceBundle messages = getResourceBundle("ServidorApresentacao/BolonhaManagerResources");

    private Integer scientificAreaUnitID = null;
    private Integer competenceCourseGroupUnitID = null;
    private Integer competenceCourseID = null;
    private IUnit scientificAreaUnit = null;
    private IUnit competenceCourseGroupUnit = null;
    private ICompetenceCourse competenceCourse = null;

    // CompetenceCourse
    private String name;
    private Double ectsCredits;
    private boolean basic;
    private Double theoreticalHours;
    private Double problemsHours;
    private Double labHours;
    private Double projectHours;
    private Double seminaryHours;
    private String regime;
    private String stage;
    // CompetenceCourse Additional Data
    private String program;
    private String generalObjectives;
    private String operationalObjectives;
    private String evaluationMethod;
    private String prerequisites;
    private String nameEn;
    private String programEn;
    private String generalObjectivesEn;
    private String operationalObjectivesEn;
    private String evaluationMethodEn;
    private String prerequisitesEn;

    public Integer getScientificAreaUnitID() {
        return (scientificAreaUnitID == null) ? scientificAreaUnitID = getAndHoldIntegerParameter("scientificAreaUnitID")
                : scientificAreaUnitID;
    }

    public Integer getCompetenceCourseGroupUnitID() {
        return (competenceCourseGroupUnitID == null) ? competenceCourseGroupUnitID = getAndHoldIntegerParameter("competenceCourseGroupUnitID")
                : competenceCourseGroupUnitID;
    }
    
    public Integer getCompetenceCourseID() {
        return (competenceCourseID == null) ? competenceCourseID = getAndHoldIntegerParameter("competenceCourseID")
                : competenceCourseID;
    }
    
    public void setCompetenceCourseID(Integer competenceCourseID) {
        this.competenceCourseID = competenceCourseID;
    }

    public String getPersonDepartmentName() {
        IEmployee employee = getUserView().getPerson().getEmployee();
        return (employee != null && employee.getCurrentDepartmentWorkingPlace() != null) ? employee
                .getCurrentDepartmentWorkingPlace().getRealName() : "";
    }

    public List<SelectItem> getRegimeTypes() {
        List<SelectItem> result = new ArrayList<SelectItem>(RegimeType.values().length);
        for (RegimeType regimeType : RegimeType.values()) {
            result.add(new SelectItem(regimeType.name(), messages.getString(regimeType.getName())));
        }
        return result;
    }

    public List<SelectItem> getCurricularStageTypes() {
        List<SelectItem> result = new ArrayList<SelectItem>(3);
        result.add(new SelectItem(CurricularStage.DRAFT.name(), messages.getString(CurricularStage.DRAFT
                .getName())));
        result.add(new SelectItem(CurricularStage.PUBLISHED.name(), messages
                .getString(CurricularStage.PUBLISHED.getName())));
        result.add(new SelectItem(CurricularStage.APPROVED.name(), messages.getString(CurricularStage.APPROVED
                .getName())));
        return result;
    }

    public IUnit getScientificAreaUnit() throws FenixFilterException, FenixServiceException {
        return (scientificAreaUnit == null) ? (scientificAreaUnit = (IUnit) readDomainObject(Unit.class,
                getScientificAreaUnitID())) : scientificAreaUnit;
    }

    public IUnit getCompetenceCourseGroupUnit() throws FenixFilterException, FenixServiceException {
        return (competenceCourseGroupUnit == null) ? (competenceCourseGroupUnit = (IUnit) readDomainObject(
                Unit.class, getCompetenceCourseGroupUnitID())) : competenceCourseGroupUnit;
    }

    public List<IUnit> getScientificAreaUnits() {
        IEmployee employee = getUserView().getPerson().getEmployee();
        return (employee != null && employee.getCurrentDepartmentWorkingPlace() != null) ? employee
                .getCurrentDepartmentWorkingPlace().getUnit().getScientificAreaUnits() : new ArrayList();
    }
    
    public ICompetenceCourse getCompetenceCourse() throws FenixFilterException, FenixServiceException {
        return (competenceCourse == null) ? (competenceCourse = (ICompetenceCourse) readDomainObject(
                CompetenceCourse.class, getCompetenceCourseID())) : competenceCourse;
    }

    public String createCompetenceCourse() {
        try {
            Object args[] = { getName(), getEctsCredits(), Boolean.valueOf(isBasic()),
                    getTheoreticalHours(), getProblemsHours(), getLabHours(), getProjectHours(),
                    getSeminaryHours(), RegimeType.valueOf(getRegime()), getCompetenceCourseGroupUnitID() };            
            ICompetenceCourse course = (ICompetenceCourse) 
            ServiceUtils.executeService(getUserView(), "CreateCompetenceCourse", args);
            setCompetenceCourseID(course.getIdInternal());
            return "editCompetenceCourseAdditionalInformation";
        } catch (FenixFilterException e) {
            setErrorMessage("error.creatingCompetenceCourse");
        } catch (FenixServiceException e) {
            setErrorMessage("error.creatingCompetenceCourse");         
        } catch (DomainException e) {
            setErrorMessage(e.getMessage());         
        } 
        return "";
    }
    
    public String editCompetenceCourse() {
        try {
            Object argsEdit[] = { new EditCompetenceCourse.CompetenceCourseInformation(
                    getCompetenceCourseID(), getName(), getEctsCredits(), Boolean.valueOf(isBasic()),
                    getTheoreticalHours(), getProblemsHours(), getLabHours(), getProblemsHours(),
                    getSeminaryHours(), RegimeType.valueOf(getRegime()), CurricularStage
                            .valueOf(getStage()), getProgram(), getGeneralObjectives(),
                    getOperationalObjectives(), getEvaluationMethod(), getPrerequisites(), getNameEn(),
                    getProgramEn(), getGeneralObjectivesEn(), getOperationalObjectivesEn(),
                    getEvaluationMethodEn(), getPrerequisitesEn()) };
            ServiceUtils.executeService(getUserView(), "EditCompetenceCourse", argsEdit);
            return "competenceCoursesManagement";
        } catch (FenixFilterException e) {
            setErrorMessage("error.editingCompetenceCourse");
        } catch (FenixServiceException e) {
            setErrorMessage(e.getMessage());
        } catch (DomainException e) {
            setErrorMessage(e.getMessage());
        }
        return "";
    }

    public String deleteCompetenceCourse() {
        try {
            Object[] args = { getCompetenceCourseID() };
            ServiceUtils.executeService(getUserView(), "DeleteCompetenceCourse", args);
            setErrorMessage("competenceCourseDeleted");
            return "competenceCoursesManagement";
        } catch (FenixFilterException e) {
            setErrorMessage("error.deletingCompetenceCourse");
        } catch (FenixServiceException e) {
            setErrorMessage("error.deletingCompetenceCourse");
        } catch (DomainException e) {
            setErrorMessage(e.getMessage());         
        }
        return "";
    }

    public String getName() throws FenixFilterException, FenixServiceException {
        return (name == null && getCompetenceCourse() != null) ? (name = getCompetenceCourse().getName())
                : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getEctsCredits() throws FenixFilterException, FenixServiceException {
        return (ectsCredits == null && getCompetenceCourse() != null) ? (ectsCredits = getCompetenceCourse()
                .getEctsCredits())
                : ectsCredits;
    }

    public void setEctsCredits(Double ectsCredits) {
        this.ectsCredits = ectsCredits;
    }

    public boolean isBasic() throws FenixFilterException, FenixServiceException {
        return (getCompetenceCourse() != null) ? (basic = getCompetenceCourse().isBasic()) : basic;        
    }

    public void setBasic(boolean basic) {
        this.basic = basic;
    }

    public Double getTheoreticalHours() throws FenixFilterException, FenixServiceException {        
        return (theoreticalHours != null) ? theoreticalHours
                : ((getCompetenceCourse() != null) ? (theoreticalHours = getCompetenceCourse()
                        .getTheoreticalHours()) : Double.valueOf(0)); 
    }

    public void setTheoreticalHours(Double theoreticalHours) {
        this.theoreticalHours = theoreticalHours;
    }

    public Double getProblemsHours() throws FenixFilterException, FenixServiceException {
        return (problemsHours != null) ? problemsHours
                : ((getCompetenceCourse() != null) ? (problemsHours = getCompetenceCourse()
                        .getProblemsHours()) : Double.valueOf(0));
    }

    public void setProblemsHours(Double problemsHours) {
        this.problemsHours = problemsHours;
    }

    public Double getLabHours() throws FenixFilterException, FenixServiceException {
        return (labHours != null) ? labHours
                : ((getCompetenceCourse() != null) ? (labHours = getCompetenceCourse()
                        .getLaboratorialHours()) : Double.valueOf(0));
    }

    public void setLabHours(Double labHours) {
        this.labHours = labHours;
    }

    public Double getProjectHours() throws FenixFilterException, FenixServiceException {
        return (projectHours != null) ? projectHours
                : ((getCompetenceCourse() != null) ? (projectHours = getCompetenceCourse()
                        .getProjectHours()) : Double.valueOf(0));
    }

    public void setProjectHours(Double projectHours) {
        this.projectHours = projectHours;
    }

    public Double getSeminaryHours() throws FenixFilterException, FenixServiceException {
        return (seminaryHours != null) ? seminaryHours
                : ((getCompetenceCourse() != null) ? (seminaryHours = getCompetenceCourse()
                        .getSeminaryHours()) : Double.valueOf(0));
    }

    public void setSeminaryHours(Double seminaryHours) {
        this.seminaryHours = seminaryHours;
    }

    public String getRegime() throws FenixFilterException, FenixServiceException {
        return (regime == null && getCompetenceCourse() != null) ? (regime = getCompetenceCourse()
                .getRegime().getName()) : regime;
    }

    public void setRegime(String regime) {
        this.regime = regime;
    }
    
    public String getStage() throws FenixFilterException, FenixServiceException {
        return (stage == null && getCompetenceCourse() != null) ? (stage = getCompetenceCourse()
                .getCurricularStage().getName()) : stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getProgram() throws FenixFilterException, FenixServiceException {
        return (program == null && getCompetenceCourse() != null) ? (program = getCompetenceCourse()
                .getProgram()) : program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public String getGeneralObjectives() throws FenixFilterException, FenixServiceException {
        return (generalObjectives == null && getCompetenceCourse() != null) ? (generalObjectives = getCompetenceCourse()
                .getGeneralObjectives())
                : generalObjectives;
    }

    public void setGeneralObjectives(String generalObjectives) {
        this.generalObjectives = generalObjectives;
    }

    public String getOperationalObjectives() throws FenixFilterException, FenixServiceException {
        return (operationalObjectives == null && getCompetenceCourse() != null) ? (operationalObjectives = getCompetenceCourse()
                .getOperationalObjectives())
                : operationalObjectives;
    }

    public void setOperationalObjectives(String operationalObjectives) {
        this.operationalObjectives = operationalObjectives;
    }

    public String getEvaluationMethod() throws FenixFilterException, FenixServiceException {
        return (evaluationMethod == null && getCompetenceCourse() != null) ? (evaluationMethod = getCompetenceCourse()
                .getEvaluationMethod())
                : evaluationMethod;
    }

    public void setEvaluationMethod(String evaluationMethod) {
        this.evaluationMethod = evaluationMethod;
    }

    public String getPrerequisites() throws FenixFilterException, FenixServiceException {
        return (prerequisites == null && getCompetenceCourse() != null) ? (prerequisites = getCompetenceCourse()
                .getPrerequisites())
                : prerequisites;
    }

    public void setPrerequisites(String prerequisites) {
        this.prerequisites = prerequisites;
    }

    public String getNameEn() throws FenixFilterException, FenixServiceException {
        return (nameEn == null && getCompetenceCourse() != null) ? (nameEn = getCompetenceCourse()
                .getNameEn()) : nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getProgramEn() throws FenixFilterException, FenixServiceException {
        return (programEn == null && getCompetenceCourse() != null) ? (programEn = getCompetenceCourse()
                .getProgramEn()) : programEn;
    }

    public void setProgramEn(String programEn) {
        this.programEn = programEn;
    }

    public String getGeneralObjectivesEn() throws FenixFilterException, FenixServiceException {
        return (generalObjectivesEn == null && getCompetenceCourse() != null) ? (generalObjectivesEn = getCompetenceCourse()
                .getGeneralObjectivesEn())
                : generalObjectivesEn;
    }

    public void setGeneralObjectivesEn(String generalObjectivesEn) {
        this.generalObjectivesEn = generalObjectivesEn;
    }

    public String getOperationalObjectivesEn() throws FenixFilterException, FenixServiceException {
        return (operationalObjectivesEn == null && getCompetenceCourse() != null) ? (operationalObjectivesEn = getCompetenceCourse()
                .getOperationalObjectivesEn())
                : operationalObjectivesEn;
    }

    public void setOperationalObjectivesEn(String operationalObjectivesEn) {
        this.operationalObjectivesEn = operationalObjectivesEn;
    }

    public String getEvaluationMethodEn() throws FenixFilterException, FenixServiceException {
        return (evaluationMethodEn == null && getCompetenceCourse() != null) ? (evaluationMethodEn = getCompetenceCourse()
                .getEvaluationMethodEn())
                : evaluationMethodEn;
    }

    public void setEvaluationMethodEn(String evaluationMethodEn) {
        this.evaluationMethodEn = evaluationMethodEn;
    }

    public String getPrerequisitesEn() throws FenixFilterException, FenixServiceException {
        return (prerequisitesEn == null && getCompetenceCourse() != null) ? (prerequisitesEn = getCompetenceCourse()
                .getPrerequisitesEn())
                : prerequisitesEn;
    }

    public void setPrerequisitesEn(String prerequisitesEn) {
        this.prerequisitesEn = prerequisitesEn;
    }
}
