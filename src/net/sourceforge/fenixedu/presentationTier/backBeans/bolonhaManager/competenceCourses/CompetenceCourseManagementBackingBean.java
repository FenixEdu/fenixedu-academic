/*
 * Created on Dec 8, 2005
 */
package net.sourceforge.fenixedu.presentationTier.backBeans.bolonhaManager.competenceCourses;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.bolonhaManager.CourseLoad;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.ICompetenceCourse;
import net.sourceforge.fenixedu.domain.IDepartment;
import net.sourceforge.fenixedu.domain.IEmployee;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.domain.degreeStructure.ICompetenceCourseLoad;
import net.sourceforge.fenixedu.domain.degreeStructure.RegimeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.IUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.backBeans.base.FenixBackingBean;

public class CompetenceCourseManagementBackingBean extends FenixBackingBean {
    private final ResourceBundle bundle = getResourceBundle("ServidorApresentacao/BolonhaManager");

    private Integer competenceCourseID = null;

    private IUnit competenceCourseGroupUnit = null;
    private List<IUnit> scientificAreaUnits = null;
    private ICompetenceCourse competenceCourse = null;
        
    // Competence-Course-Information
    private String name;
    private String nameEn;
    private String acronym;
    private Boolean basic;
    private String regime;
    private Integer numberOfPeriods;
    private boolean setNumberOfPeriods = true;
    
    // Competence-Course-Additional-Data
    private String objectives;
    private String program;
    private String evaluationMethod;
    private String objectivesEn;
    private String programEn;
    private String evaluationMethodEn;    
    private String stage;
    
    public String getAction() {
        return getAndHoldStringParameter("action");
    }
    
    public IDepartment getPersonDepartment() {
        final IEmployee employee = getUserView().getPerson().getEmployee();
        return (employee != null && employee.getCurrentDepartmentWorkingPlace() != null) ? employee
                .getCurrentDepartmentWorkingPlace() : null;
    }
    
    public List<IUnit> getScientificAreaUnits() {
        if (scientificAreaUnits == null) {
            final IDepartment department = getPersonDepartment();
            scientificAreaUnits = (department != null) ? department.getUnit().getScientificAreaUnits() : null;
        }
        return scientificAreaUnits;
    }
    
    public Integer getCompetenceCourseGroupUnitID() {
        return getAndHoldIntegerParameter("competenceCourseGroupUnitID");        
    }
    
    public IUnit getCompetenceCourseGroupUnit() throws FenixFilterException, FenixServiceException {
        if (competenceCourseGroupUnit == null && getCompetenceCourseGroupUnitID() != null) {
            competenceCourseGroupUnit = (IUnit) readDomainObject(Unit.class, getCompetenceCourseGroupUnitID());
        }
        return competenceCourseGroupUnit;
    }

    public String getName() throws FenixFilterException, FenixServiceException {      
        if (name == null && getCompetenceCourse() != null) {
            name = getCompetenceCourse().getName();        
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameEn() throws FenixFilterException, FenixServiceException {        
        if (nameEn == null && getCompetenceCourse() != null) {
            nameEn = getCompetenceCourse().getNameEn();
        }
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }
    
    public String getAcronym() throws FenixFilterException, FenixServiceException {
        if (acronym == null && getCompetenceCourse() != null) {
            acronym = getCompetenceCourse().getAcronym();
        }
        return acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public Boolean getBasic() throws FenixFilterException, FenixServiceException {
        if (basic == null && getCompetenceCourse() != null) {
            basic = Boolean.valueOf(getCompetenceCourse().isBasic());
        }
        return basic;
    }

    public void setBasic(Boolean basic) {
        this.basic = basic;
    }

    public String getRegime() {
        if (regime == null) {
            regime = (String) getViewState().getAttribute("regime");
            if (regime == null) {
                regime = "SEMESTER";
            }                        
        }
        return regime;
    }

    public void setRegime(String regime) {
        getViewState().setAttribute("regime", (this.regime = regime));
    }   
    
    public Integer getNumberOfPeriods() {
        if (numberOfPeriods == null) {
            numberOfPeriods = (Integer) getViewState().getAttribute("numberOfPeriods");
            if (numberOfPeriods == null) {
                numberOfPeriods = Integer.valueOf(1);
            }
        }
        return numberOfPeriods;
    }

    public void setNumberOfPeriods(Integer numberOfPeriods) {
        if (setNumberOfPeriods) {
            getViewState().setAttribute("numberOfPeriods", (this.numberOfPeriods = numberOfPeriods));
        }        
    }    
    
    public List<SelectItem> getPeriods() {
        final List<SelectItem> result = new ArrayList<SelectItem>(2);
        result.add(new SelectItem(Integer.valueOf(1), "1"));
        if (getRegime().equals("ANUAL")) {
            result.add(new SelectItem(Integer.valueOf(2), "2"));
        }
        return result;
    }
    
    public List<CourseLoad> getCourseLoads() throws FenixFilterException, FenixServiceException {
        if (getViewState().getAttribute("courseLoads") == null) {
            final List<CourseLoad> courseLoads;
            if (getAction().equals("create")) {
                courseLoads = createNewCourseLoads();
            } else if (getAction().equals("edit") && getCompetenceCourse() != null) {
                courseLoads = getExistingCourseLoads();
            } else {
                courseLoads = new ArrayList<CourseLoad>(0);
            }            
            getViewState().setAttribute("courseLoads", courseLoads);
        }
        return (List<CourseLoad>) getViewState().getAttribute("courseLoads");
    }
    
    private List<CourseLoad> createNewCourseLoads() {
        int numberOfPeriods = getNumberOfPeriods().intValue();
        final List<CourseLoad> courseLoads = new ArrayList<CourseLoad>(numberOfPeriods);        
        for (int i = 0; i < numberOfPeriods; i++) {
            courseLoads.add(new CourseLoad());
        }
        return courseLoads;
    }
    
    private List<CourseLoad> getExistingCourseLoads() throws FenixFilterException, FenixServiceException {
        final List<CourseLoad> courseLoads = new ArrayList<CourseLoad>(getCompetenceCourse().getCompetenceCourseLoads().size());        
        for (final ICompetenceCourseLoad competenceCourseLoad : getCompetenceCourse().getCompetenceCourseLoads()) {
            courseLoads.add(new CourseLoad("edit", competenceCourseLoad));
        }
        return courseLoads;
    }
    
    public void setCourseLoads(List<CourseLoad> courseLoads) {
        getViewState().setAttribute("courseLoads", courseLoads);
    }
    
    public void resetCourseLoad(ValueChangeEvent event) throws FenixFilterException, FenixServiceException {
        calculateCourseLoad((String) event.getNewValue(), 1);
    }
    
    public void resetCorrespondentCourseLoad(ValueChangeEvent event) throws FenixFilterException, FenixServiceException {
        calculateCourseLoad(getRegime(), ((Integer) event.getNewValue()).intValue());
    }
    
    private void calculateCourseLoad(String regime, int newNumberOfPeriods) throws FenixFilterException, FenixServiceException {
        final List<CourseLoad> courseLoads = getCourseLoads();
        if (regime.equals("ANUAL")) {
            if (newNumberOfPeriods == 2 && courseLoads.size() == 1) {
                courseLoads.add(new CourseLoad());                
            } else if (courseLoads.size() > 1) {
                courseLoads.remove(0);
            }
            setCourseLoads(courseLoads);
        } else if (regime.equals("SEMESTER")) {
            if (courseLoads.size() > 1) {
                courseLoads.remove(0);
                setCourseLoads(courseLoads);
            }            
            setNumberOfPeriods(Integer.valueOf(1));
            setNumberOfPeriods = false; // prevent jsf to reset the value
        }
    }
    
    public Integer getCompetenceCourseID() {
        return (competenceCourseID == null) ? (competenceCourseID = getAndHoldIntegerParameter("competenceCourseID")) : competenceCourseID;
    }
    
    public void setCompetenceCourseID(Integer competenceCourseID) {
        this.competenceCourseID = competenceCourseID;
    }
    
    public ICompetenceCourse getCompetenceCourse() throws FenixFilterException, FenixServiceException {
        if (competenceCourse == null && getCompetenceCourseID() != null) {
            competenceCourse = (ICompetenceCourse) readDomainObject(CompetenceCourse.class, getCompetenceCourseID()); 
        }
        return competenceCourse;
    }
    
    public void setCompetenceCourse(ICompetenceCourse competenceCourse) {
        this.competenceCourse = competenceCourse;
    }
    
    public String getObjectives() {
        return objectives;
    }

    public void setObjectives(String objectives) {
        this.objectives = objectives;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public String getEvaluationMethod() {
        return evaluationMethod;
    }

    public void setEvaluationMethod(String evaluationMethod) {
        this.evaluationMethod = evaluationMethod;
    }

    public String getObjectivesEn() {
        return objectivesEn;
    }

    public void setObjectivesEn(String objectivesEn) {
        this.objectivesEn = objectivesEn;
    }

    public String getProgramEn() {
        return programEn;
    }

    public void setProgramEn(String programEn) {
        this.programEn = programEn;
    }

    public String getEvaluationMethodEn() {
        return evaluationMethodEn;
    }

    public void setEvaluationMethodEn(String evaluationMethodEn) {
        this.evaluationMethodEn = evaluationMethodEn;
    }
    
    public String getStage() throws FenixFilterException, FenixServiceException {
        if (stage == null && getCompetenceCourse() != null) {
            stage = getCompetenceCourse().getRegime().getName();
        }
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }
    
    public String createCompetenceCourse() {
        try {
            final Object args[] = { getName(), getNameEn(), getAcronym(), getBasic(),
                    RegimeType.valueOf(getRegime()), getCompetenceCourseGroupUnitID() };
            final ICompetenceCourse competenceCourse = (ICompetenceCourse) ServiceUtils.executeService(
                    getUserView(), "CreateCompetenceCourse", args);
            setCompetenceCourse(competenceCourse);
            return "setCompetenceCourseLoad";
        } catch (FenixFilterException e) {
            setErrorMessage("error.creatingCompetenceCourse");
        } catch (FenixServiceException e) {
            setErrorMessage("error.creatingCompetenceCourse");
        } catch (DomainException e) {
            setErrorMessage(e.getMessage());
        }
        return "";
    }
    
    public String createCompetenceCourseLoad() {
        try {
            setCompetenceCourseLoad();
            return "setCompetenceCourseAdditionalInformation";
        } catch (FenixFilterException e) {
            setErrorMessage("error.editingCompetenceCourse");
        } catch (FenixServiceException e) {
            setErrorMessage("error.editingCompetenceCourse");
        }
        return "";
    }
    
    public String createCompetenceCourseAdditionalInformation() {
        try {
            setCompetenceCourseAdditionalInformation();
            return "competenceCoursesManagement";
        } catch (FenixFilterException e) {
            setErrorMessage("error.editingCompetenceCourse");
        } catch (FenixServiceException e) {
            setErrorMessage("error.editingCompetenceCourse");
        }
        return "";        
    }
    
    public String editCompetenceCourse() {
        try {
            final Object args[] = {getCompetenceCourseID(), getName(), getNameEn(), getAcronym(), getBasic(),
                    CurricularStage.valueOf(getStage()) };
            ServiceUtils.executeService(getUserView(), "EditCompetenceCourse", args);
            return "editCompetenceCourseMainPage";
        } catch (FenixFilterException e) {
            setErrorMessage("error.editingCompetenceCourse");
        } catch (FenixServiceException e) {
            setErrorMessage("error.editingCompetenceCourse");
        }
        return "";
    }   
    
    public String editCompetenceCourseLoad() {
        try {
            setCompetenceCourseLoad();
            return "editCompetenceCourseMainPage";
        } catch (FenixFilterException e) {
            setErrorMessage("error.editingCompetenceCourse");
        } catch (FenixServiceException e) {
            setErrorMessage("error.editingCompetenceCourse");
        }
        return "";
    }
    
    public String editCompetenceCourseAdditionalInformation() {
        try {
            setCompetenceCourseAdditionalInformation();
            return "editCompetenceCourseMainPage";
        } catch (FenixFilterException e) {
            setErrorMessage("error.editingCompetenceCourse");
        } catch (FenixServiceException e) {
            setErrorMessage("error.editingCompetenceCourse");
        }
        return "";
    }
    
    private void setCompetenceCourseLoad() throws FenixFilterException, FenixServiceException {
        Object args[] = { getCompetenceCourseID(), RegimeType.valueOf(getRegime()), getCourseLoads() };
        ServiceUtils.executeService(getUserView(), "EditCompetenceCourseLoad", args);
    }

    private void setCompetenceCourseAdditionalInformation() throws FenixFilterException, FenixServiceException {
        final Object args[] = { getCompetenceCourseID(), getObjectives(), getProgram(),
                getEvaluationMethod(), getObjectivesEn(), getProgramEn(), getEvaluationMethodEn() };
        ServiceUtils.executeService(getUserView(), "EditCompetenceCourse", args);
    }
    
    public String deleteCompetenceCourse() {
        try {
            Object[] args = { getCompetenceCourseID() };
            ServiceUtils.executeService(getUserView(), "DeleteCompetenceCourse", args);
            addInfoMessage(bundle.getString("competenceCourseDeleted"));
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
}
