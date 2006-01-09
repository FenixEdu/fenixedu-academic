/*
 * Created on Dec 8, 2005
 */
package net.sourceforge.fenixedu.presentationTier.backBeans.bolonhaManager.competenceCourses;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.bolonhaManager.CourseLoad;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.degreeStructure.CompetenceCourseLoad;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.domain.degreeStructure.RegimeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.backBeans.base.FenixBackingBean;

import org.apache.commons.beanutils.BeanComparator;

public class CompetenceCourseManagementBackingBean extends FenixBackingBean {
    
    private Integer competenceCourseID = null;
    private Unit competenceCourseGroupUnit = null;
    private List<Unit> scientificAreaUnits = null;
    private CompetenceCourse competenceCourse = null;        
    // Competence-Course-Information
    private String name;
    private String nameEn;
    private String acronym;
    private Boolean basic;
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
    
    public Department getPersonDepartment() {
        final Employee employee = getUserView().getPerson().getEmployee();
        return (employee != null && employee.getCurrentDepartmentWorkingPlace() != null) ? employee
                .getCurrentDepartmentWorkingPlace() : null;
    }
    
    public List<Unit> getScientificAreaUnits() {
        if (scientificAreaUnits == null) {
            final Department department = getPersonDepartment();
            scientificAreaUnits = (department != null) ? department.getUnit().getScientificAreaUnits() : null;
        }
        return scientificAreaUnits;
    }
    
    public Integer getCompetenceCourseGroupUnitID() {
        return getAndHoldIntegerParameter("competenceCourseGroupUnitID");        
    }
    
    public Unit getCompetenceCourseGroupUnit() throws FenixFilterException, FenixServiceException {
        if (competenceCourseGroupUnit == null && getCompetenceCourseGroupUnitID() != null) {
            competenceCourseGroupUnit = (Unit) readDomainObject(Unit.class, getCompetenceCourseGroupUnitID());
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

    public String getRegime() throws FenixFilterException, FenixServiceException {
        if (getViewState().getAttribute("regime") == null) {
            if (getCompetenceCourse() != null) {
                setRegime(getCompetenceCourse().getRegime().name());
            } else {
                setRegime("SEMESTER");
            }            
        }
        return (String) getViewState().getAttribute("regime");
    }

    public void setRegime(String regime) {
        getViewState().setAttribute("regime", regime);
    }   
    
    public Integer getNumberOfPeriods() throws FenixFilterException, FenixServiceException {
        if (getViewState().getAttribute("numberOfPeriods") == null) {
            if (getCompetenceCourse() != null && getCompetenceCourse().getCompetenceCourseLoads().size() > 0) {
                setNumberOfPeriods(getCompetenceCourse().getCompetenceCourseLoads().size());
            } else {
                setNumberOfPeriods(Integer.valueOf(1));
            }                    
        }
        return (Integer) getViewState().getAttribute("numberOfPeriods");
    }

    public void setNumberOfPeriods(Integer numberOfPeriods) {
        if (setNumberOfPeriods) {
            getViewState().setAttribute("numberOfPeriods", numberOfPeriods);
        }        
    }    
    
    public List<SelectItem> getPeriods() throws FenixFilterException, FenixServiceException {
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
    
    private List<CourseLoad> createNewCourseLoads() throws FenixFilterException, FenixServiceException {
        int numberOfPeriods = getNumberOfPeriods().intValue();
        final List<CourseLoad> courseLoads = new ArrayList<CourseLoad>(numberOfPeriods);        
        for (int i = 0; i < numberOfPeriods; i++) {
            courseLoads.add(new CourseLoad(i + 1));
        }
        return courseLoads;
    }
    
    private List<CourseLoad> getExistingCourseLoads() throws FenixFilterException, FenixServiceException {
        final List<CourseLoad> courseLoads = new ArrayList<CourseLoad>(getCompetenceCourse().getCompetenceCourseLoads().size());        
        for (final CompetenceCourseLoad competenceCourseLoad : getCompetenceCourse().getCompetenceCourseLoads()) {
            courseLoads.add(new CourseLoad("edit", competenceCourseLoad));
        }
        Collections.sort(courseLoads, new BeanComparator("order"));
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
            if (newNumberOfPeriods > getNumberOfPeriods().intValue()) {
                addCourseLoad(courseLoads);                                
            } else {
                removeCourseLoad(courseLoads);
            }            
        } else if (regime.equals("SEMESTER")) {
            removeCourseLoad(courseLoads);
            setNumberOfPeriods(Integer.valueOf(1));
            // prevent application to reset the value
            setNumberOfPeriods = false; 
        }
        setCourseLoads(courseLoads);
    }

    private void addCourseLoad(final List<CourseLoad> courseLoads) {
        if (getAction().equals("create")) {
            courseLoads.add(new CourseLoad(courseLoads.size() + 1));
        } else if (getAction().equals("edit")) {
            final CourseLoad courseLoad = searchDeleteCourseLoad(courseLoads);
            if (courseLoad != null) {
                courseLoad.setAction("edit");
            } else {
                courseLoads.add(new CourseLoad(courseLoads.size() + 1));
            }
        }        
    }
    
    private CourseLoad searchDeleteCourseLoad(final List<CourseLoad> courseLoads) {       
        for (final CourseLoad courseLoad : courseLoads) {
            if (courseLoad.getAction().equals("delete")) {
                return courseLoad;                
            }
        }
        return null;
    }
    
    private void removeCourseLoad(final List<CourseLoad> courseLoads) {
        if (getAction().equals("create") && courseLoads.size() > 1) {
            courseLoads.remove(courseLoads.size() - 1);
        } else if (getAction().equals("edit") && courseLoads.size() > 1) {
            courseLoads.get(courseLoads.size() - 1).setAction("delete");
        }
    }

    public Integer getCompetenceCourseID() {
        return (competenceCourseID == null) ? (competenceCourseID = getAndHoldIntegerParameter("competenceCourseID")) : competenceCourseID;
    }
    
    public void setCompetenceCourseID(Integer competenceCourseID) {
        this.competenceCourseID = competenceCourseID;
    }
    
    public CompetenceCourse getCompetenceCourse() throws FenixFilterException, FenixServiceException {
        if (competenceCourse == null && getCompetenceCourseID() != null) {
            competenceCourse = (CompetenceCourse) readDomainObject(CompetenceCourse.class, getCompetenceCourseID()); 
        }
        return competenceCourse;
    }
    
    public void setCompetenceCourse(CompetenceCourse competenceCourse) {
        this.competenceCourse = competenceCourse;
    }
    
    public String getObjectives() throws FenixFilterException, FenixServiceException {
        if (objectives == null && getCompetenceCourse() != null) {
            objectives = getCompetenceCourse().getObjectives();
        }
        return objectives;
    }

    public void setObjectives(String objectives) {
        this.objectives = objectives;
    }

    public String getProgram() throws FenixFilterException, FenixServiceException {
        if (program == null && getCompetenceCourse() != null) {
            program = getCompetenceCourse().getProgram();
        }
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public String getEvaluationMethod() throws FenixFilterException, FenixServiceException {
        if (evaluationMethod == null && getCompetenceCourse() != null) {
            evaluationMethod = getCompetenceCourse().getEvaluationMethod();
        }
        return evaluationMethod;
    }

    public void setEvaluationMethod(String evaluationMethod) {
        this.evaluationMethod = evaluationMethod;
    }

    public String getObjectivesEn() throws FenixFilterException, FenixServiceException {
        if (objectivesEn == null && getCompetenceCourse() != null) {
            objectivesEn = getCompetenceCourse().getObjectivesEn();
        }
        return objectivesEn;
    }

    public void setObjectivesEn(String objectivesEn) {
        this.objectivesEn = objectivesEn;
    }

    public String getProgramEn() throws FenixFilterException, FenixServiceException {
        if (programEn == null && getCompetenceCourse() != null) {
            programEn = getCompetenceCourse().getProgramEn();
        }
        return programEn;
    }

    public void setProgramEn(String programEn) {
        this.programEn = programEn;
    }

    public String getEvaluationMethodEn() throws FenixFilterException, FenixServiceException {
        if (evaluationMethodEn == null && getCompetenceCourse() != null) {
            evaluationMethodEn = getCompetenceCourse().getEvaluationMethodEn();
        }
        return evaluationMethodEn;
    }

    public void setEvaluationMethodEn(String evaluationMethodEn) {
        this.evaluationMethodEn = evaluationMethodEn;
    }
    
    public String getStage() throws FenixFilterException, FenixServiceException {
        if (stage == null && getCompetenceCourse() != null) {
            stage = getCompetenceCourse().getCurricularStage().name();
        }
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }
    
    public List<CompetenceCourseLoad> getSortedCompetenceCourseLoads() throws FenixFilterException, FenixServiceException {
        final List<CompetenceCourseLoad> result = new ArrayList<CompetenceCourseLoad>();
        if (getCompetenceCourse() != null) {
            result.addAll(getCompetenceCourse().getCompetenceCourseLoads());
            Collections.sort(result, new BeanComparator("order"));
        }
        return result;
    }
    
    public String createCompetenceCourse() {
        try {
            final Object args[] = { getName(), getNameEn(), getAcronym(), getBasic(),
                    RegimeType.valueOf(getRegime()), getCompetenceCourseGroupUnitID() };
            final CompetenceCourse competenceCourse = (CompetenceCourse) ServiceUtils.executeService(
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
        } catch (DomainException e) {
            setErrorMessage(e.getMessage());            
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
        } catch (DomainException e) {
            setErrorMessage(e.getMessage());            
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
            addInfoMessage(getResourceBundle("ServidorApresentacao/BolonhaManagerResources").getString("competenceCourseDeleted"));
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
