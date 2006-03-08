/*
 * Created on Dec 7, 2005
 */
package net.sourceforge.fenixedu.presentationTier.backBeans.bolonhaManager.curricularPlans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
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
    private final ResourceBundle bolonhaBundle = getResourceBundle("resources/BolonhaManagerResources");
    private final ResourceBundle enumerationBundle = getResourceBundle("resources/EnumerationResources");
    private final ResourceBundle domainExceptionBundle = getResourceBundle("resources/DomainExceptionResources");
    private final Integer NO_SELECTION = 0;    
    
    private Integer competenceCourseID = null;
    private Integer courseGroupID = null;    
    private Integer curricularYearID = null;
    private Integer curricularSemesterID = null;
    private Integer contextID = null;
    private Integer curricularCourseID = null;
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

    public Integer getCourseGroupID() throws FenixFilterException, FenixServiceException {
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
    
    public Integer getDepartmentUnitID() throws FenixFilterException, FenixServiceException {
        if (getViewState().getAttribute("departmentUnitID") == null && getCurricularCourse() != null) {
            getViewState().setAttribute("departmentUnitID",
                    getCurricularCourse().getCompetenceCourse().getDepartmentUnit().getIdInternal());
        }
        return (Integer) getViewState().getAttribute("departmentUnitID");
    }

    public void setDepartmentUnitID(Integer departmentUnitID) {
        getViewState().setAttribute("departmentUnitID", departmentUnitID);
    }

    public Integer getCompetenceCourseID() throws FenixFilterException, FenixServiceException {
        if (competenceCourseID == null && getCurricularCourse() != null) {
            competenceCourseID = getCurricularCourse().getCompetenceCourse().getIdInternal();
        }
        return competenceCourseID;
    }

    public void setCompetenceCourseID(Integer competenceCourseID) {
        this.competenceCourseID = resetCompetenceCourseID ? Integer.valueOf(0) : competenceCourseID;
    }

    public List<SelectItem> getDepartmentUnits() throws FenixFilterException {
        return (departmentUnits == null) ? (departmentUnits = readDepartmentUnits()) : departmentUnits;
    }

    public List<SelectItem> getCompetenceCourses() throws FenixFilterException, FenixServiceException {
        return readCompetenceCourses();
    }

    public List<SelectItem> getCourseGroups() throws FenixFilterException, FenixServiceException {
        return (courseGroups == null) ? (courseGroups = readDegreeModules(CourseGroup.class)) : courseGroups;
    }
    
    public List<SelectItem> getCurricularCourses() throws FenixFilterException, FenixServiceException {
        return (curricularCourses == null) ? (curricularCourses = readDegreeModules(CurricularCourse.class)) : curricularCourses;
    }

    public List<SelectItem> getCurricularYears() throws FenixFilterException, FenixServiceException {
        final int years = getDegreeCurricularPlan().getDegree().getBolonhaDegreeType().getYears();
        final List<SelectItem> result = new ArrayList<SelectItem>(years);
        result.add(new SelectItem(this.NO_SELECTION, bolonhaBundle.getString("choose")));
        for (int i = 1; i <= years; i++) {
            result.add(new SelectItem(Integer.valueOf(i), String.valueOf(i) + "º"));
        }
        return result;
    }

    public List<SelectItem> getCurricularSemesters() {
        final List<SelectItem> result = new ArrayList<SelectItem>(2);
        
        result.add(new SelectItem(this.NO_SELECTION, bolonhaBundle.getString("choose")));
        result.add(new SelectItem(Integer.valueOf(1), String.valueOf(1) + "º"));
        result.add(new SelectItem(Integer.valueOf(2), String.valueOf(2) + "º"));
        return result;
    }

    public DegreeCurricularPlan getDegreeCurricularPlan() throws FenixFilterException,
            FenixServiceException {
        return (degreeCurricularPlan == null) ? (degreeCurricularPlan = (DegreeCurricularPlan) readDomainObject(
                DegreeCurricularPlan.class, getDegreeCurricularPlanID()))
                : degreeCurricularPlan;
    }

    public CourseGroup getCourseGroup() throws FenixFilterException, FenixServiceException {
        return (courseGroup == null) ? (courseGroup = (CourseGroup) readDomainObject(CourseGroup.class,
                getCourseGroupID())) : courseGroup;
    }

    public Unit getDepartmentUnit() throws FenixFilterException, FenixServiceException {
       if (getDepartmentUnitID() != null && !getDepartmentUnitID().equals(0)) {
           return (Unit) readDomainObject(Unit.class, getDepartmentUnitID());
       }
       return null;
    }
    
    public CompetenceCourse getCompetenceCourse() throws FenixFilterException, FenixServiceException {
        if (competenceCourse == null && getCompetenceCourseID() != null && !getCompetenceCourseID().equals(0)) {
            competenceCourse = (CompetenceCourse) readDomainObject(CompetenceCourse.class, getCompetenceCourseID()); 
        }
        return competenceCourse;
    }
    
    public CurricularCourse getCurricularCourse() throws FenixFilterException, FenixServiceException {
        return (curricularCourse == null && getCurricularCourseID() != null) ? (curricularCourse = (CurricularCourse) readDomainObject(
                CurricularCourse.class, getCurricularCourseID()))
                : curricularCourse;
    }

    private Context getContext(Integer contextID) throws FenixFilterException, FenixServiceException {
        return (context == null && contextID != null) ? (context = (Context) readDomainObject(
                Context.class, contextID)) : context;
    }

    public void resetCompetenceCourse(ValueChangeEvent event) {
        resetCompetenceCourseID = true;
    }

    public Double getWeight() throws FenixFilterException, FenixServiceException {
        if (weight == null) {
            weight = (getCurricularCourse() != null) ? getCurricularCourse().getWeigth() : Double.valueOf(0);
        }
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }
    
    public String getPrerequisites() throws FenixFilterException, FenixServiceException {
        if (prerequisites == null && getCurricularCourse() != null) {
            prerequisites = getCurricularCourse().getPrerequisites();
        }
        return prerequisites;
    }

    public void setPrerequisites(String prerequisites) {
        this.prerequisites = prerequisites;
    }

    public String getPrerequisitesEn() throws FenixFilterException, FenixServiceException {
        if (prerequisitesEn == null && getCurricularCourse() != null) {
            prerequisitesEn = getCurricularCourse().getPrerequisitesEn();
        }
        return prerequisitesEn;
    }

    public void setPrerequisitesEn(String prerequisitesEn) {
        this.prerequisitesEn = prerequisitesEn;
    }

    public Integer getCurricularYearID() throws FenixFilterException, FenixServiceException {
        if (curricularYearID == null && getContext(getContextID()) != null) {            
            curricularYearID = getContext(getContextID()).getCurricularPeriod().getOrderByType(CurricularPeriodType.YEAR);
        } else if (curricularYearID == null && getAndHoldIntegerParameter("curricularYearID") != null) {
            curricularYearID = getAndHoldIntegerParameter("curricularYearID");
        }
        return curricularYearID;
    }

    public void setCurricularYearID(Integer curricularYearID) {
        this.curricularYearID = curricularYearID;
    }

    public Integer getCurricularSemesterID() throws FenixFilterException, FenixServiceException {
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
    
    public String getSelectedCurricularCourseType() throws FenixFilterException, FenixServiceException {
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
    
    public String getName() throws FenixFilterException, FenixServiceException {
        if (getViewState().getAttribute("name") == null && getCurricularCourse() != null) {
            setName(getCurricularCourse().getName());
        }
        return (String) getViewState().getAttribute("name");
    }
    
    public void setName(String name) {
        getViewState().setAttribute("name", name);
    }
    
    public String getNameEn() throws FenixFilterException, FenixServiceException {
        if (getViewState().getAttribute("nameEn") == null && getCurricularCourse() != null) {
            setNameEn(getCurricularCourse().getNameEn());            
        }
        return (String) getViewState().getAttribute("nameEn");
    }
    
    public void setNameEn(String nameEn) {
        getViewState().setAttribute("nameEn", nameEn);
    }
    
    public List<String> getRulesLabels() throws FenixFilterException, FenixServiceException {
        final List<String> resultLabels = new ArrayList<String>();
        for (final CurricularRule curricularRule : getCurricularCourse().getParticipatingCurricularRules()) {
            resultLabels.add(CurricularRuleLabelFormatter.getLabel(curricularRule));
        }
        return resultLabels;
    }
    
    public boolean isToDelete() throws FenixFilterException, FenixServiceException {
        if (getCurricularCourse() != null) {
            toDelete = getCurricularCourse().getParentContextsCount() == 1; // Last context?
        }
        return toDelete;
    }
    
    public String createCurricularCourse() throws FenixFilterException {        
        try {
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
    
    private Object[] getArgumentsToCreate() throws FenixFilterException, FenixServiceException, FenixActionException {
        checkCourseGroup();
        checkCurricularSemesterAndYear();
        final CurricularCourseType curricularCourseType = CurricularCourseType.valueOf(getSelectedCurricularCourseType());
        if (curricularCourseType.equals(CurricularCourseType.NORMAL_COURSE)) {
            checkCompetenceCourse();
            Object args[] = { getWeight(), getPrerequisites(), getPrerequisitesEn(), getCompetenceCourseID(),
                getCourseGroupID(), getCurricularYearID(), getCurricularSemesterID(), getDegreeCurricularPlanID() };
            return args;
        } else if (curricularCourseType.equals(CurricularCourseType.OPTIONAL_COURSE)) {
            checkCurricularCourseNameAndNameEn();
            Object args[] = { getDegreeCurricularPlanID(), getCourseGroupID(), getName(), getNameEn(),
                    getCurricularYearID(), getCurricularSemesterID() };
            return args;
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
    
    private Object[] getArgumentsToEdit() throws FenixFilterException, FenixServiceException, FenixActionException {
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
    
    private void checkCompetenceCourse() throws FenixFilterException, FenixServiceException, FenixActionException  {
        if (getCompetenceCourseID() == null || getCompetenceCourseID().intValue() == 0) {
            throw new FenixActionException("error.mustChooseACompetenceCourse");
        }
    }
    
    private void checkCourseGroup() throws FenixFilterException, FenixServiceException, FenixActionException {
        if (getCourseGroupID() == null || getCourseGroupID().equals(this.NO_SELECTION)) {
            throw new FenixActionException("error.mustChooseACourseGroup");
        }
    }

    private void checkCurricularCourse() throws FenixFilterException, FenixServiceException, FenixActionException {
        if (getCurricularCourseID() == null || getCurricularCourseID().equals(this.NO_SELECTION)) {
            throw new FenixActionException("error.mustChooseACurricularCourse");
        }
    }
    
    private void checkCurricularCourseNameAndNameEn() throws FenixFilterException, FenixServiceException, FenixActionException {
        if (getName() == null || getName().equals("")) {
            throw new FenixActionException("error.mustDefineNameOrNameEn");
        }
        if (getNameEn() == null || getNameEn().equals("")) {
            throw new FenixActionException("error.mustDefineNameOrNameEn");
        }
    }

    private void checkCurricularSemesterAndYear() throws FenixFilterException, FenixServiceException, FenixActionException {
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
            Object args[] = { getCurricularCourse(), getCourseGroup(), getCurricularYearID(),
                    getCurricularSemesterID() };
            ServiceUtils.executeService(getUserView(), "AddContextToDegreeModule", args);
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
            Object args[] = { getCurricularCourse(), getContext(getContextID()), getCourseGroup(),
                    getCurricularYearID(), getCurricularSemesterID() };
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
    
    public void tryDeleteContext(ActionEvent event) throws FenixFilterException, FenixServiceException {
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
    
    public String editCurricularCourseReturnPath() throws FenixFilterException, FenixServiceException {
        return !toDelete ? "" : "deleteCurricularCourseContext";
    }

    private List<SelectItem> readDepartmentUnits() throws FenixFilterException {
        final List<SelectItem> result = new ArrayList<SelectItem>();
        for (final Unit unit : UnitUtils.readAllDepartmentUnits()) {
            result.add(new SelectItem(unit.getIdInternal(), unit.getName()));
        }
        Collections.sort(result, new BeanComparator("label"));
        result.add(0, new SelectItem(this.NO_SELECTION, bolonhaBundle.getString("choose")));
        return result;
    }

    private List<SelectItem> readCompetenceCourses() throws FenixFilterException, FenixServiceException {
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
    
    private List<SelectItem> readDegreeModules(Class<? extends DegreeModule> clazz) throws FenixFilterException, FenixServiceException {
        final List<SelectItem> result = new ArrayList<SelectItem>();
        final List<List<DegreeModule>> degreeModulesSet = getDegreeCurricularPlan().getDcpDegreeModulesIncludingFullPath(clazz);
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
}
