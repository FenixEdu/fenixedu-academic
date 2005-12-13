/*
 * Created on Dec 8, 2005
 */
package net.sourceforge.fenixedu.presentationTier.backBeans.bolonhaManager.competenceCourses;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.faces.model.SelectItem;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
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
        return (employee != null && employee.getDepartmentWorkingPlace() != null) ? employee
                .getDepartmentWorkingPlace().getRealName() : "";
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
        return (employee != null && employee.getDepartmentWorkingPlace() != null) ? employee
                .getDepartmentWorkingPlace().getUnit().getScientificAreaUnits() : new ArrayList();
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
            return "setCompetenceCourseAdditionalData";
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
            Object args[] = {getCompetenceCourseID(), getName(), getEctsCredits(), Boolean.valueOf(isBasic()),
                getTheoreticalHours(), getProblemsHours(), getLabHours(), getProblemsHours(),
                getSeminaryHours(), RegimeType.valueOf(getRegime()), CurricularStage.valueOf(getStage())};
            ServiceUtils.executeService(getUserView(), "EditCompetenceCourse", args);        
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
}
