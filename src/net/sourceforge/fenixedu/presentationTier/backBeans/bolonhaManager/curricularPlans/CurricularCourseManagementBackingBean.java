/*
 * Created on Dec 7, 2005
 */
package net.sourceforge.fenixedu.presentationTier.backBeans.bolonhaManager.curricularPlans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.bolonhaManager.CreateCurricularCourse;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriodType;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRule;
import net.sourceforge.fenixedu.domain.curriculum.CurricularCourseType;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitUtils;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.backBeans.base.FenixBackingBean;
import net.sourceforge.fenixedu.util.CurricularRuleLabelFormatter;

import org.apache.commons.beanutils.BeanComparator;

public class CurricularCourseManagementBackingBean extends FenixBackingBean {
    protected final ResourceBundle bolonhaBundle = getResourceBundle("resources/BolonhaManagerResources");
    protected final ResourceBundle enumerationBundle = getResourceBundle("resources/EnumerationResources");
    protected final ResourceBundle domainExceptionBundle = getResourceBundle("resources/DomainExceptionResources");
    protected final Integer NO_SELECTION = 0;    
    
    private Integer competenceCourseID = null;
    private Integer courseGroupID = null;    
    private Integer curricularYearID = null;
    private Integer curricularSemesterID = null;
    private Integer contextID = null;
    private Integer curricularCourseID = null;
    private Integer executionPeriodOID = null;
    private boolean resetCompetenceCourseID = false;
    private boolean toDelete = false;
    
    private Double weight = null;
    private String prerequisites;
    private String prerequisitesEn;

    private CompetenceCourse competenceCourse = null;
    private DegreeCurricularPlan degreeCurricularPlan = null;
    private CourseGroup courseGroup = null;
    private CurricularCourse curricularCourse = null;
    private Context context = null;

    public List<SelectItem> departmentUnits = null;
    public List<SelectItem> courseGroups = null;
    public List<SelectItem> curricularCourses = null;
    public List<SelectItem> executionYearItems = null;

    public Integer getDegreeCurricularPlanID() {
        return getAndHoldIntegerParameter("degreeCurricularPlanID");
    }
    
    public String getAction() {
        return getAndHoldStringParameter("action");
    }

    public String getShowRules() {
        return getAndHoldStringParameter("showRules");
    }
    
    public String getToOrder() {
        return getAndHoldStringParameter("toOrder");
    }
    
    public String getOrganizeBy() {
        return getAndHoldStringParameter("organizeBy");
    }
    
    public String getHideCourses() {
        return getAndHoldStringParameter("hideCourses");
    }
    
    public Integer getContextID() {
        return (contextID == null) ? (contextID = getAndHoldIntegerParameter("contextID")) : contextID;
    }
    
    public void setContextID(Integer contextID) {
        this.contextID = contextID;
    }
    
    public Integer getContextIDToDelete() {
        return getAndHoldIntegerParameter("contextIDToDelete");
    }

    public Integer getCourseGroupID() {
        if (courseGroupID == null) {
            courseGroupID = getAndHoldIntegerParameter("courseGroupID");
            if (courseGroupID == null) {
                courseGroupID = (getContext(getContextID()) != null) ? getContext(getContextID())
                        .getParentCourseGroup().getIdInternal() : courseGroupID;
            }
        }
        return courseGroupID;
    }

    public void setCourseGroupID(Integer courseGroupID) {
        this.courseGroupID = courseGroupID;
    }
    
    public Integer getCurricularCourseID() {
        return (curricularCourseID == null) ? (curricularCourseID = getAndHoldIntegerParameter("curricularCourseID"))
                : curricularCourseID;
    }
    
    public void setCurricularCourseID(Integer curricularCourseID) {
        this.curricularCourseID = curricularCourseID;
    }
    
    public Integer getExecutionPeriodOID() {
        return (executionPeriodOID == null) ? (executionPeriodOID = getAndHoldIntegerParameter("executionPeriodOID")) : executionPeriodOID;
    }
    
    public void setExecutionPeriodOID(Integer executionPeriodOID) {
        this.executionPeriodOID = executionPeriodOID;
    }
    
    public Integer getDepartmentUnitID() {
        if (getViewState().getAttribute("departmentUnitID") == null && getCurricularCourse() != null) {
            getViewState().setAttribute("departmentUnitID",
                    getCurricularCourse().getCompetenceCourse().getDepartmentUnit().getIdInternal());
        }
        return (Integer) getViewState().getAttribute("departmentUnitID");
    }

    public void setDepartmentUnitID(Integer departmentUnitID) {
        getViewState().setAttribute("departmentUnitID", departmentUnitID);
    }

    public Integer getCompetenceCourseID() {
        if (competenceCourseID == null && getCurricularCourse() != null) {
            competenceCourseID = getCurricularCourse().getCompetenceCourse().getIdInternal();
        }
        return competenceCourseID;
    }

    public void setCompetenceCourseID(Integer competenceCourseID) {
        this.competenceCourseID = resetCompetenceCourseID ? Integer.valueOf(0) : competenceCourseID;
    }

    public List<SelectItem> getDepartmentUnits() {
        return (departmentUnits == null) ? (departmentUnits = readDepartmentUnits()) : departmentUnits;
    }

    public List<SelectItem> getCompetenceCourses() {
        return readCompetenceCourses();
    }

    public List<SelectItem> getCourseGroups() {
        return (courseGroups == null) ? (courseGroups = readDegreeModules(CourseGroup.class)) : courseGroups;
    }
    
    public List<SelectItem> getCurricularCourses() {
        return (curricularCourses == null) ? (curricularCourses = readDegreeModules(CurricularCourse.class)) : curricularCourses;
    }

    public List<SelectItem> getCurricularYears() {
        final int years = getDegreeCurricularPlan().getDegree().getDegreeType().getYears();
        final List<SelectItem> result = new ArrayList<SelectItem>(years);
        result.add(new SelectItem(this.NO_SELECTION, bolonhaBundle.getString("choose")));
        for (int i = 1; i <= years; i++) {
            result.add(new SelectItem(Integer.valueOf(i), String.valueOf(i) + "�"));
        }
        return result;
    }

    public List<SelectItem> getCurricularSemesters() {
        final List<SelectItem> result = new ArrayList<SelectItem>(2);
        
        result.add(new SelectItem(this.NO_SELECTION, bolonhaBundle.getString("choose")));
        result.add(new SelectItem(Integer.valueOf(1), String.valueOf(1) + "�"));
        result.add(new SelectItem(Integer.valueOf(2), String.valueOf(2) + "�"));
        return result;
    }

    public DegreeCurricularPlan getDegreeCurricularPlan() {
        return (degreeCurricularPlan == null) ? (degreeCurricularPlan = rootDomainObject.readDegreeCurricularPlanByOID(getDegreeCurricularPlanID())) : degreeCurricularPlan;
    }

    public CourseGroup getCourseGroup() {
        return (courseGroup == null) ? (courseGroup = (CourseGroup) rootDomainObject.readDegreeModuleByOID(getCourseGroupID())) : courseGroup;
    }

    public Unit getDepartmentUnit() {
       if (getDepartmentUnitID() != null && !getDepartmentUnitID().equals(0)) {
           return (Unit) rootDomainObject.readPartyByOID(getDepartmentUnitID());
       }
       return null;
    }
    
    public CompetenceCourse getCompetenceCourse() {
        if (competenceCourse == null && getCompetenceCourseID() != null && !getCompetenceCourseID().equals(0)) {
            competenceCourse = rootDomainObject.readCompetenceCourseByOID(getCompetenceCourseID()); 
        }
        return competenceCourse;
    }
    
    public CurricularCourse getCurricularCourse() {
        return (curricularCourse == null && getCurricularCourseID() != null) ? (curricularCourse = (CurricularCourse) rootDomainObject.readDegreeModuleByOID(getCurricularCourseID())) : curricularCourse;
    }

    private Context getContext(Integer contextID) {
        return (context == null && contextID != null) ? (context = rootDomainObject.readContextByOID(contextID)) : context;
    }

    public void resetCompetenceCourse(ValueChangeEvent event) {
        resetCompetenceCourseID = true;
    }

    public Double getWeight() {
        if (weight == null) {
            weight = (getCurricularCourse() != null) ? getCurricularCourse().getWeigth() : Double.valueOf(0);
        }
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }
    
    public String getPrerequisites() {
        if (prerequisites == null && getCurricularCourse() != null) {
            prerequisites = getCurricularCourse().getPrerequisites();
        }
        return prerequisites;
    }

    public void setPrerequisites(String prerequisites) {
        this.prerequisites = prerequisites;
    }

    public String getPrerequisitesEn() {
        if (prerequisitesEn == null && getCurricularCourse() != null) {
            prerequisitesEn = getCurricularCourse().getPrerequisitesEn();
        }
        return prerequisitesEn;
    }

    public void setPrerequisitesEn(String prerequisitesEn) {
        this.prerequisitesEn = prerequisitesEn;
    }

    public Integer getCurricularYearID() {
        if (curricularYearID == null && getContext(getContextID()) != null) {            
            curricularYearID = getContext(getContextID()).getCurricularPeriod().getOrderByType(CurricularPeriodType.YEAR);
            if (curricularYearID == null) {
                curricularYearID = Integer.valueOf(1);
            }
        } else if (curricularYearID == null && getAndHoldIntegerParameter("curricularYearID") != null) {
            curricularYearID = getAndHoldIntegerParameter("curricularYearID");
        }
        return curricularYearID;
    }

    public void setCurricularYearID(Integer curricularYearID) {
        this.curricularYearID = curricularYearID;
    }

    public Integer getCurricularSemesterID() {
        if (curricularSemesterID == null && getContext(getContextID()) != null) {
            curricularSemesterID = getContext(getContextID()).getCurricularPeriod().getOrderByType(CurricularPeriodType.SEMESTER);
        } else if (curricularSemesterID == null && getAndHoldIntegerParameter("curricularSemesterID") != null) {
            curricularSemesterID = getAndHoldIntegerParameter("curricularSemesterID");
        }
        return curricularSemesterID;
    }

    public void setCurricularSemesterID(Integer curricularSemesterID) {
        this.curricularSemesterID = curricularSemesterID;
    }
    
    public String getSelectedCurricularCourseType() {
        if (getViewState().getAttribute("selectedCurricularCourseType") == null) {
            if (getCurricularCourse() != null) {
                setSelectedCurricularCourseType(getCurricularCourse().getType().name());
            } else {
                setSelectedCurricularCourseType(CurricularCourseType.NORMAL_COURSE.name());                
            }
        }
        return (String) getViewState().getAttribute("selectedCurricularCourseType");
    }
    
    public void setSelectedCurricularCourseType(String selectedCurricularCourseType) {
        getViewState().setAttribute("selectedCurricularCourseType", selectedCurricularCourseType);
    }
    
    public String getName() {
        if (getViewState().getAttribute("name") == null && getCurricularCourse() != null) {
            setName(getCurricularCourse().getName());
        }
        return (String) getViewState().getAttribute("name");
    }
    
    public void setName(String name) {
        getViewState().setAttribute("name", name);
    }
    
    public String getNameEn() {
        if (getViewState().getAttribute("nameEn") == null && getCurricularCourse() != null) {
            setNameEn(getCurricularCourse().getNameEn());            
        }
        return (String) getViewState().getAttribute("nameEn");
    }
    
    public void setNameEn(String nameEn) {
        getViewState().setAttribute("nameEn", nameEn);
    }
    
    public List<String> getRulesLabels() {
        final List<String> resultLabels = new ArrayList<String>();
        for (final CurricularRule curricularRule : getCurricularCourse().getParticipatingCurricularRules()) {
            resultLabels.add(CurricularRuleLabelFormatter.getLabel(curricularRule));
        }
        return resultLabels;
    }
    
    public boolean isToDelete() {
        if (getCurricularCourse() != null) {
            toDelete = getCurricularCourse().getParentContextsCount() == 1; // Last context?
        }
        return toDelete;
    }
    
    public Integer getExecutionYearID() {
        if (getViewState().getAttribute("executionYearID") == null) {
            if (getAndHoldIntegerParameter("executionYearID") != null) {
                setExecutionYearID(getAndHoldIntegerParameter("executionYearID"));
            }
        }
        return (Integer) getViewState().getAttribute("executionYearID");
    }
    
    public void setExecutionYearID(Integer executionYearID) {
        getViewState().setAttribute("executionYearID", executionYearID);
    }
    
    public ExecutionYear getExecutionYear() {
        return rootDomainObject.readExecutionYearByOID(getExecutionYearID());
    }
    
    protected InfoExecutionYear getCurrentExecutionYear() {
        return InfoExecutionYear.newInfoFromDomain(ExecutionYear.readCurrentExecutionYear());
    }
    
    public List<SelectItem> getExecutionYearItems() {
        return (executionYearItems == null) ? (executionYearItems = readExecutionYearItems()) : executionYearItems;
    }
    
    public Integer getBeginExecutionPeriodID() {
        if (getViewState().getAttribute("beginExecutionPeriodID") == null && getContext(getContextID()) != null) {
            setBeginExecutionPeriodID(getContext(getContextID()).getBeginExecutionPeriod().getIdInternal());
        }
        return (Integer) getViewState().getAttribute("beginExecutionPeriodID");
    }
    
    public void setBeginExecutionPeriodID(Integer beginExecutionPeriodID) {
        getViewState().setAttribute("beginExecutionPeriodID", beginExecutionPeriodID);
    }
    
    public Integer getEndExecutionPeriodID() {
        if (getViewState().getAttribute("endExecutionPeriodID") == null && getContext(getContextID()) != null) {
            setEndExecutionPeriodID((getContext(getContextID()).getEndExecutionPeriod() != null) ?
                    getContext(getContextID()).getEndExecutionPeriod().getIdInternal() : Integer.valueOf(NO_SELECTION));
        }
        return (Integer) getViewState().getAttribute("endExecutionPeriodID");
    }
    
    public void setEndExecutionPeriodID(Integer endExecutionPeriodID) {
        getViewState().setAttribute("endExecutionPeriodID", endExecutionPeriodID);
    }
    
    public List<SelectItem> getBeginExecutionPeriodItems() {
        return readExecutionPeriodItems();
    }

    public List<SelectItem> getEndExecutionPeriodItems() {
        final List<SelectItem> result = new ArrayList<SelectItem>(readExecutionPeriodItems());
        result.add(0, new SelectItem(NO_SELECTION, bolonhaBundle.getString("opened")));
        return result;
    }
   
    protected List<SelectItem> readExecutionPeriodItems() {
        final List<SelectItem> result = new ArrayList<SelectItem>();
        final ExecutionPeriod currentExecutionPeriod = ExecutionPeriod.readActualExecutionPeriod();
        final List<ExecutionPeriod> notClosedExecutionPeriods = ExecutionPeriod.readNotClosedExecutionPeriods();
        Collections.sort(notClosedExecutionPeriods);
        for (final ExecutionPeriod notClosedExecutionPeriod : notClosedExecutionPeriods) {
            if (notClosedExecutionPeriod.isAfterOrEquals(currentExecutionPeriod)) {                
                result.add(new SelectItem(notClosedExecutionPeriod.getIdInternal(),
                        notClosedExecutionPeriod.getName() + " " + notClosedExecutionPeriod.getExecutionYear().getYear()));
            }
        }
        return result;
    }
    
    public List<Context> getCurricularCourseParentContexts() {
        return getCurricularCourse().getParentContextsByExecutionYear(getExecutionYear());
    }

    public String createCurricularCourse() {        
        try {
            checkCourseGroup();
            checkCurricularSemesterAndYear();
            
            ServiceUtils.executeService(getUserView(), "CreateCurricularCourse", getArgumentsToCreate());
            
        } catch (FenixActionException e) {
            this.addErrorMessage(bolonhaBundle.getString(e.getMessage()));
            return "";
        } catch (FenixFilterException e) {
            this.addErrorMessage(bolonhaBundle.getString("error.notAuthorized"));
            return "buildCurricularPlan";
        } catch (FenixServiceException e) {
            this.addErrorMessage(bolonhaBundle.getString(e.getMessage()));
            return "";
        } catch (DomainException e) {
            this.addErrorMessage(domainExceptionBundle.getString(e.getMessage()));
            return "";
        } catch (Exception e) {
            this.addErrorMessage(bolonhaBundle.getString("general.error"));
            return "buildCurricularPlan";
        }
        addInfoMessage(bolonhaBundle.getString("curricularCourseCreated"));
        return "buildCurricularPlan";
    }
    
    private Object[] getArgumentsToCreate() throws FenixActionException {
        final CurricularCourseType curricularCourseType = CurricularCourseType.valueOf(getSelectedCurricularCourseType());
        if (curricularCourseType.equals(CurricularCourseType.NORMAL_COURSE)) {

            checkCompetenceCourse();
            return new Object[] { new CreateCurricularCourse.CreateCurricularCourseArgs(getWeight(),
                    getPrerequisites(), getPrerequisitesEn(), getCompetenceCourseID(),
                    getCourseGroupID(), getCurricularYearID(), getCurricularSemesterID(),
                    getDegreeCurricularPlanID(), getBeginExecutionPeriodID(), getFinalEndExecutionPeriodID()) };

        } else if (curricularCourseType.equals(CurricularCourseType.OPTIONAL_COURSE)) {

            checkCurricularCourseNameAndNameEn();
            return new Object[] { new CreateCurricularCourse.CreateOptionalCurricularCourseArgs(
                    getDegreeCurricularPlanID(), getCourseGroupID(), getName(), getNameEn(),
                    getCurricularYearID(), getCurricularSemesterID(), getBeginExecutionPeriodID(),
                    getFinalEndExecutionPeriodID()) };

        }
        return null;
    }

    public String editCurricularCourse() throws FenixFilterException {
        try {
            ServiceUtils.executeService(getUserView(), "EditCurricularCourseBolonhaManager", getArgumentsToEdit());
            setContextID(0); // resetContextID
        } catch (FenixServiceException e) {
            addErrorMessage(bolonhaBundle.getString(e.getMessage()));
        } catch (FenixActionException e) {
            addErrorMessage(bolonhaBundle.getString(e.getMessage()));
        }
        addInfoMessage(bolonhaBundle.getString("curricularCourseEdited"));
        return "";
    }
    
    private Object[] getArgumentsToEdit() throws FenixActionException {
        final CurricularCourseType curricularCourseType = CurricularCourseType.valueOf(getSelectedCurricularCourseType());
        if (curricularCourseType.equals(CurricularCourseType.NORMAL_COURSE)) {
            checkCompetenceCourse();
            Object args[] = { getCurricularCourse(), getWeight(), getPrerequisites(), getPrerequisitesEn(), getCompetenceCourse() };
            return args;
        } else if (curricularCourseType.equals(CurricularCourseType.OPTIONAL_COURSE)) {
            Object args[] = { getCurricularCourse(), getName(), getNameEn() };
            return args;
        }
        return null;
    }
    
    private void checkCompetenceCourse() throws FenixActionException  {
        if (getCompetenceCourseID() == null || getCompetenceCourseID().intValue() == 0) {
            throw new FenixActionException("error.mustChooseACompetenceCourse");
        }
    }
    
    private void checkCourseGroup() throws FenixActionException {
        if (getCourseGroupID() == null || getCourseGroupID().equals(this.NO_SELECTION)) {
            throw new FenixActionException("error.mustChooseACourseGroup");
        }
    }

    private void checkCurricularCourse() throws FenixActionException {
        if (getCurricularCourseID() == null || getCurricularCourseID().equals(this.NO_SELECTION)) {
            throw new FenixActionException("error.mustChooseACurricularCourse");
        }
    }
    
    private void checkCurricularCourseNameAndNameEn() throws FenixActionException {
        if (getName() == null || getName().equals("")) {
            throw new FenixActionException("error.mustDefineNameOrNameEn");
        }
        if (getNameEn() == null || getNameEn().equals("")) {
            throw new FenixActionException("error.mustDefineNameOrNameEn");
        }
    }

    private void checkCurricularSemesterAndYear() throws FenixActionException {
        if (getCurricularSemesterID() == null || getCurricularSemesterID().equals(this.NO_SELECTION)) {
            throw new FenixActionException("error.mustChooseACurricularSemester");
        }
        if (getCurricularYearID() == null || getCurricularYearID().equals(this.NO_SELECTION)) {
            throw new FenixActionException("error.mustChooseACurricularYear");
        }
    }
    
    public void addContext(ActionEvent event) {
        addContext();
    }
    
    public String addContext() {
        try {
            checkCourseGroup();
            checkCurricularCourse();
            checkCurricularSemesterAndYear();
            Object args[] = {
                    getCurricularCourse(),
                    getCourseGroup(),
                    getBeginExecutionPeriodID(),
                    getFinalEndExecutionPeriodID(),
                    getCurricularYearID(),
                    getCurricularSemesterID() };
            
            ServiceUtils.executeService(getUserView(), "AddContextToCurricularCourse", args);
            
        } catch (FenixActionException e) {
            this.addErrorMessage(bolonhaBundle.getString(e.getMessage()));
            return "";
        } catch (FenixFilterException e) {
            this.addErrorMessage(bolonhaBundle.getString("error.notAuthorized"));
            return "buildCurricularPlan";
        } catch (FenixServiceException e) {
            this.addErrorMessage(bolonhaBundle.getString(e.getMessage()));
            return "";
        } catch (DomainException e) {
            this.addErrorMessage(domainExceptionBundle.getString(e.getMessage()));
            return "";
        } catch (Exception e) {
            this.addErrorMessage(bolonhaBundle.getString("general.error"));
            return "buildCurricularPlan";
        }        
        addInfoMessage(bolonhaBundle.getString("addedNewContextToCurricularCourse"));
        setContextID(0); // resetContextID
        return "buildCurricularPlan";
    }
    
    public String editContext() throws FenixFilterException {
        try {
            checkCourseGroup();
            Object args[] = {
                    getCurricularCourse(),
                    getContext(getContextID()),
                    getCourseGroup(),
                    getCurricularYearID(),
                    getCurricularSemesterID(),
                    getBeginExecutionPeriodID(),
                    getFinalEndExecutionPeriodID() };
            ServiceUtils.executeService(getUserView(), "EditContextFromCurricularCourse", args);
            setContextID(0); // resetContextID
        } catch (FenixServiceException e) {
            addErrorMessage(e.getMessage());
        } catch (DomainException e) {
            addErrorMessage(domainExceptionBundle.getString(e.getMessage()));
        } catch (FenixActionException e) {
            addErrorMessage(bolonhaBundle.getString(e.getMessage()));
        }
        return "";
    }

    protected Integer getFinalEndExecutionPeriodID() {
        return (getViewState().getAttribute("endExecutionPeriodID") == null || getViewState()
                .getAttribute("endExecutionPeriodID").equals(NO_SELECTION)) ? null
                : (Integer) getViewState().getAttribute("endExecutionPeriodID");
    }
    
    public void tryDeleteContext(ActionEvent event) throws FenixFilterException {
        if (!isToDelete()) {
            deleteContext(event);
        } else {
            setContextID(getContextIDToDelete());
        }
    }
    
    public void deleteContext(ActionEvent event) throws FenixFilterException {
        try {
            Object args[] = { getCurricularCourseID(), getContextIDToDelete() };
            ServiceUtils.executeService(getUserView(), "DeleteContextFromDegreeModule", args);
            setContextID(0); // resetContextID
            addInfoMessage(bolonhaBundle.getString("successAction"));
        } catch (FenixServiceException e) {
            addErrorMessage(e.getMessage());
        } catch (DomainException e) {
            addErrorMessage(getFormatedMessage(domainExceptionBundle, e.getKey(), e.getArgs()));
        }
    }    
 
    public String cancel() {
        setContextID(0);
        return "";
    }
    
    public String editCurricularCourseReturnPath() {
        return !toDelete ? "" : "deleteCurricularCourseContext";
    }

    private List<SelectItem> readDepartmentUnits() {
        final List<SelectItem> result = new ArrayList<SelectItem>();
        for (final Unit unit : UnitUtils.readAllDepartmentUnits()) {
            result.add(new SelectItem(unit.getIdInternal(), unit.getName()));
        }
        Collections.sort(result, new BeanComparator("label"));
        result.add(0, new SelectItem(this.NO_SELECTION, bolonhaBundle.getString("choose")));
        return result;
    }

    private List<SelectItem> readCompetenceCourses() {
        final List<SelectItem> result = new ArrayList<SelectItem>();
        final Unit departmentUnit = getDepartmentUnit();
        if (departmentUnit != null) {
            for (final Unit scientificAreaUnit : departmentUnit.getScientificAreaUnits()) {
                for (final Unit competenceCourseGroupUnit : scientificAreaUnit.getCompetenceCourseGroupUnits()) {
                    for (final CompetenceCourse competenceCourse : competenceCourseGroupUnit.getCompetenceCourses()) {
                        if (competenceCourse.getCurricularStage() != CurricularStage.DRAFT) {
                            result.add(new SelectItem(competenceCourse.getIdInternal(), competenceCourse.getName() + " ("
                                    + enumerationBundle.getString(competenceCourse.getCurricularStage().getName()) + ")"));
                        }
                    }
                }
            }
            Collections.sort(result, new BeanComparator("label"));
        }
        result.add(0, new SelectItem(this.NO_SELECTION, bolonhaBundle.getString("choose")));
        return result;
    }
    
    private List<SelectItem> readDegreeModules(Class<? extends DegreeModule> clazz) {
        final List<SelectItem> result = new ArrayList<SelectItem>();
        final List<List<DegreeModule>> degreeModulesSet = getDegreeCurricularPlan().getDcpDegreeModulesIncludingFullPath(clazz, getExecutionYear());
        for (final List<DegreeModule> degreeModules : degreeModulesSet) {
            final StringBuilder pathName = new StringBuilder();
            for (final DegreeModule degreeModule : degreeModules) {
                pathName.append((pathName.length() == 0) ? "" : " > ").append(degreeModule.getName());
            }
            result.add(new SelectItem(degreeModules.get(degreeModules.size() - 1).getIdInternal(), pathName.toString()));
        }
        Collections.sort(result, new BeanComparator("label"));
        result.add(0, new SelectItem(this.NO_SELECTION, bolonhaBundle.getString("choose")));
        return result;
    }
    
    private List<SelectItem> readExecutionYearItems() {
        final List<SelectItem> result = new ArrayList<SelectItem>();

        ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();
        for (ExecutionDegree executionDegree : getDegreeCurricularPlan().getExecutionDegrees()) {
            result.add(new SelectItem(executionDegree.getExecutionYear().getIdInternal(), executionDegree.getExecutionYear().getYear()));
            if (executionDegree.getExecutionYear().equals(currentExecutionYear)) {
                setExecutionYearID(currentExecutionYear.getIdInternal());
            }
        }
        
        if (getExecutionYearID() == null) {
            setExecutionYearID(getDegreeCurricularPlan().getMostRecentExecutionDegree().getExecutionYear().getIdInternal());
        }
        
        return result;
    }
    
    public Degree getDegree() {
        return getDegreeCurricularPlan().getDegree();
    }
    
    public String getDegreeLocaleSensitiveName() {
        final Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
        return locale.getLanguage().equals(Locale.ENGLISH.getLanguage()) ? getDegree().getNameEn() : getDegree().getNome();
    }

    public List<CompetenceCourse> getDegreeCurricularPlanCompetenceCourses() {
        return getDegreeCurricularPlan().getCompetenceCourses(getExecutionYear());
    }

}
