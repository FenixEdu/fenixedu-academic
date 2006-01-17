/*
 * Created on Dec 7, 2005
 */
package net.sourceforge.fenixedu.presentationTier.backBeans.bolonhaManager.curricularPlans;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
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
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitType;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.backBeans.base.FenixBackingBean;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

public class CurricularCourseManagementBackingBean extends FenixBackingBean {
    private final ResourceBundle bolonhaBundle = getResourceBundle("ServidorApresentacao/BolonhaManagerResources");
    private final ResourceBundle enumerationBundle = getResourceBundle("ServidorApresentacao/EnumerationResources");
    private final ResourceBundle domainExceptionBundle = getResourceBundle("ServidorApresentacao/DomainExceptionResources");
    private final Integer NO_SELECTION = 0;    
    
    private Integer competenceCourseID = null;
    private Integer courseGroupID = null;    
    private Integer curricularYearID = null;
    private Integer curricularSemesterID = null;
    private Integer contextID = null;
    private Integer curricularCourseID = null;
    private boolean resetCompetenceCourseID = false;
    private boolean confirmDelete = false;
    
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
                        .getCourseGroup().getIdInternal() : courseGroupID;
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
        return (courseGroups == null) ? (courseGroups = readCourseGroups()) : courseGroups;
    }
    
    public List<SelectItem> getCurricularCourses() throws FenixFilterException, FenixServiceException {
        return (curricularCourses == null) ? (curricularCourses = readCurricularCourses()) : curricularCourses;
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
           return  (Unit) readDomainObject(Unit.class, getDepartmentUnitID());
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
        }
        return curricularYearID;
    }

    public void setCurricularYearID(Integer curricularYearID) {
        this.curricularYearID = curricularYearID;
    }

    public Integer getCurricularSemesterID() throws FenixFilterException, FenixServiceException {
        if (curricularSemesterID == null && getContext(getContextID()) != null) {
            curricularSemesterID = getContext(getContextID()).getCurricularPeriod().getOrderByType(CurricularPeriodType.SEMESTER);
        }
        return curricularSemesterID;
    }

    public void setCurricularSemesterID(Integer curricularSemesterID) {
        this.curricularSemesterID = curricularSemesterID;
    }
    
    public String createCurricularCourse() throws FenixFilterException {        
        try {
            checkCompetenceCourse();
            checkCourseGroup();
            checkCurricularSemesterAndYear();
            Object args[] = {getWeight(), getPrerequisites(), getPrerequisitesEn(), getCompetenceCourseID(),
                    getCourseGroupID(), getCurricularYearID(), getCurricularSemesterID(), getDegreeCurricularPlanID()};
            ServiceUtils.executeService(getUserView(), "CreateCurricularCourse", args);
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

    public String editCurricularCourse() throws FenixFilterException {        
        try {
            checkCompetenceCourse();
            Object args[] = {getCurricularCourse(), getWeight(), getPrerequisites(), getPrerequisitesEn(), getCompetenceCourse()};
            ServiceUtils.executeService(getUserView(), "EditCurricularCourse", args);
           
        } catch (FenixServiceException e) {
            addErrorMessage(bolonhaBundle.getString(e.getMessage()));
        } catch (FenixActionException e) {
            addErrorMessage(bolonhaBundle.getString(e.getMessage()));
        }
        
        addInfoMessage(bolonhaBundle.getString("curricularCourseEdited"));
        return "";
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
    
    public void deleteContext(ActionEvent event) throws FenixFilterException, FenixServiceException {
        confirmDelete = getCurricularCourse().getDegreeModuleContextsCount() == 1; // Last context?
        if (!confirmDelete) { 
            forceDeleteContext(event);
        }
    }    
    
    public void forceDeleteContext(ActionEvent event) throws FenixFilterException {        
        try {
            Object args[] = { getCurricularCourse(), getContext(getContextIDToDelete()) };
            ServiceUtils.executeService(getUserView(), "DeleteContextFromCurricularCourse", args);
            setContextID(0); // resetContextID
        } catch (FenixServiceException e) {
            addErrorMessage(e.getMessage());
        } catch (DomainException e) {
            addErrorMessage(domainExceptionBundle.getString(e.getMessage()));
        }
    }
    
    public String editCurricularCourseReturnPath() throws FenixFilterException, FenixServiceException {
        return confirmDelete ? "confirmDeleteCurricularCourse" : "";
    }
    
    public String deleteCurricularCourseContextReturnPath() throws FenixFilterException, FenixServiceException {
        return confirmDelete ? "confirmDeleteCurricularCourse" : "buildCurricularPlan";
    }

    private List<SelectItem> readDepartmentUnits() throws FenixFilterException {
        final List<SelectItem> result = new ArrayList<SelectItem>();
        result.add(new SelectItem(this.NO_SELECTION, bolonhaBundle.getString("choose")));
        try {
            Date now = Calendar.getInstance().getTime();
            for (final Unit unit : (List<Unit>) readAllDomainObjects(Unit.class)) {
                if (unit.isActive(now) && unit.getType() != null
                        && unit.getType().equals(UnitType.DEPARTMENT)) {
                    result.add(new SelectItem(unit.getIdInternal(), unit.getName()));
                }
            }
        } catch (FenixServiceException e) {
            addErrorMessage("error.gettingDepartmentUnits");
        }
        return result;
    }

    private final ComparatorChain comparatorChain = new ComparatorChain(); {
        comparatorChain.addComparator(new BeanComparator("curricularStage"), true);
        comparatorChain.addComparator(new BeanComparator("name"));    
    }
    private List<SelectItem> readCompetenceCourses() throws FenixFilterException, FenixServiceException {
        final List<SelectItem> result = new ArrayList<SelectItem>();
        result.add(new SelectItem(this.NO_SELECTION, bolonhaBundle.getString("choose")));
        final Unit departmentUnit = getDepartmentUnit();
        if (departmentUnit != null) {
            final List<CompetenceCourse> competenceCourses = new ArrayList<CompetenceCourse>();
            for (final Unit scientificAreaUnit : departmentUnit.getScientificAreaUnits()) {
                for (final Unit competenceCourseGroupUnit : scientificAreaUnit.getCompetenceCourseGroupUnits()) {
                    competenceCourses.addAll(competenceCourseGroupUnit.getCompetenceCourses());                    
                    }
                }
            Collections.sort(competenceCourses, comparatorChain);
            for (final CompetenceCourse competenceCourse : competenceCourses) {
                result.add(new SelectItem(competenceCourse.getIdInternal(), competenceCourse.getName() + " ("
                        + enumerationBundle.getString(competenceCourse.getCurricularStage().getName())
                        + ")", "", competenceCourse.getCurricularStage().equals(CurricularStage.DRAFT)));
            }
        }
        return result;
    }

    private List<SelectItem> readCourseGroups() throws FenixFilterException, FenixServiceException {
        final List<SelectItem> result = new ArrayList<SelectItem>();
        result.add(new SelectItem(this.NO_SELECTION, bolonhaBundle.getString("choose")));
        final DegreeModule degreeModule = getDegreeCurricularPlan().getDegreeModule();
        if (degreeModule instanceof CourseGroup) {
            collectChildCourseGroups(result, (CourseGroup) degreeModule, "");
        }
        return result;
    }

    private void collectChildCourseGroups(final List<SelectItem> result, final CourseGroup courseGroup,
            final String previousCourseGroupName) {
        String currentCourseGroupName = "";
        if (!courseGroup.isRoot()) {
            currentCourseGroupName = ((previousCourseGroupName.length() == 0) ? "" : (previousCourseGroupName + " > "))
                    + courseGroup.getName();
            result.add(new SelectItem(courseGroup.getIdInternal(), currentCourseGroupName));
        }
        for (final Context context : courseGroup.getContextsWithCourseGroups()) {
            collectChildCourseGroups(result, (CourseGroup) context.getDegreeModule(),
                    currentCourseGroupName);
        }
    }

    private List<SelectItem> readCurricularCourses() throws FenixFilterException, FenixServiceException {
        final List<SelectItem> result = new ArrayList<SelectItem>();
        result.add(new SelectItem(this.NO_SELECTION, bolonhaBundle.getString("choose")));
        for (final CurricularCourse curricularCourse : getDegreeCurricularPlan()
                .getDcpCurricularCourses()) {
            result.add(new SelectItem(curricularCourse.getIdInternal(), curricularCourse.getName()));
        }
        return result;
    }
}
