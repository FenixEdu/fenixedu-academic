/*
 * Created on Dec 7, 2005
 */
package net.sourceforge.fenixedu.presentationTier.backBeans.bolonhaManager.curricularPlans;

import java.util.ArrayList;
import java.util.Calendar;
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
import net.sourceforge.fenixedu.domain.ICompetenceCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.domain.degreeStructure.IContext;
import net.sourceforge.fenixedu.domain.degreeStructure.ICourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.IDegreeModule;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.IUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitType;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.backBeans.base.FenixBackingBean;

public class CurricularCourseManagementBackingBean extends FenixBackingBean {
    private final ResourceBundle messages = getResourceBundle("ServidorApresentacao/BolonhaManagerResources");
    
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

    private ICompetenceCourse competenceCourse = null;
    private IDegreeCurricularPlan degreeCurricularPlan = null;
    private ICourseGroup courseGroup = null;
    private ICurricularCourse curricularCourse = null;
    private IContext context = null;

    public List<SelectItem> departmentUnits = null;
    public List<SelectItem> courseGroups = null;
    public List<SelectItem> curricularCourses = null;

    public Integer getDegreeCurricularPlanID() {
        return getAndHoldIntegerParameter("degreeCurricularPlanID");
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

    public List<SelectItem> getCurricularYears() {
        final List<SelectItem> result = new ArrayList<SelectItem>(5);
        for (int i = 1; i <= 5; i++) {
            result.add(new SelectItem(Integer.valueOf(i), String.valueOf(i) + "º"));
        }
        return result;
    }

    public List<SelectItem> getCurricularSemesters() {
        final List<SelectItem> result = new ArrayList<SelectItem>(2);
        result.add(new SelectItem(Integer.valueOf(1), String.valueOf(1) + "º"));
        result.add(new SelectItem(Integer.valueOf(2), String.valueOf(2) + "º"));
        return result;
    }

    public IDegreeCurricularPlan getDegreeCurricularPlan() throws FenixFilterException,
            FenixServiceException {
        return (degreeCurricularPlan == null) ? (degreeCurricularPlan = (IDegreeCurricularPlan) readDomainObject(
                DegreeCurricularPlan.class, getDegreeCurricularPlanID()))
                : degreeCurricularPlan;
    }

    public ICourseGroup getCourseGroup() throws FenixFilterException, FenixServiceException {
        return (courseGroup == null) ? (courseGroup = (ICourseGroup) readDomainObject(CourseGroup.class,
                getCourseGroupID())) : courseGroup;
    }

    public IUnit getDepartmentUnit() throws FenixFilterException, FenixServiceException {
       if (getDepartmentUnitID() != null && !getDepartmentUnitID().equals(0)) {
           return  (IUnit) readDomainObject(Unit.class, getDepartmentUnitID());
       }
       return null;
    }
    
    public ICompetenceCourse getCompetenceCourse() throws FenixFilterException, FenixServiceException {
        if (competenceCourse == null && getCompetenceCourseID() != null && !getCompetenceCourseID().equals(0)) {
            competenceCourse = (ICompetenceCourse) readDomainObject(CompetenceCourse.class, getCompetenceCourseID()); 
        }
        return competenceCourse;
    }
    
    public ICurricularCourse getCurricularCourse() throws FenixFilterException, FenixServiceException {
        return (curricularCourse == null && getCurricularCourseID() != null) ? (curricularCourse = (ICurricularCourse) readDomainObject(
                CurricularCourse.class, getCurricularCourseID()))
                : curricularCourse;
    }

    private IContext getContext(Integer contextID) throws FenixFilterException, FenixServiceException {
        return (context == null && contextID != null) ? (context = (IContext) readDomainObject(
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
            curricularYearID = getContext(getContextID()).getCurricularSemester().getCurricularYear()
                    .getYear();
        }
        return curricularYearID;
    }

    public void setCurricularYearID(Integer curricularYearID) {
        this.curricularYearID = curricularYearID;
    }

    public Integer getCurricularSemesterID() throws FenixFilterException, FenixServiceException {
        if (curricularSemesterID == null && getContext(getContextID()) != null) {
            curricularSemesterID = getContext(getContextID()).getCurricularSemester().getSemester();
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
            Object args[] = {getWeight(), getPrerequisites(), getPrerequisitesEn(), getCompetenceCourseID(),
                    getCourseGroupID(), getCurricularYearID(), getCurricularSemesterID()};
            ServiceUtils.executeService(getUserView(), "CreateCurricularCourse", args);
            addInfoMessage(messages.getString("curricularCourseCreated"));
            return "buildCurricularPlan";
        } catch (FenixServiceException e) {
          setErrorMessage(e.getMessage());
        } catch (FenixActionException e) {
            setErrorMessage(e.getMessage());
        }
        return "";
    }

    public String editCurricularCourse() throws FenixFilterException {        
        try {
            checkCompetenceCourse();
            Object args[] = {getCurricularCourse(), getWeight(), getPrerequisites(), getPrerequisitesEn(), getCompetenceCourse()};
            ServiceUtils.executeService(getUserView(), "EditCurricularCourse", args);
            addInfoMessage(messages.getString("curricularCourseEdited"));           
        } catch (FenixServiceException e) {
            setErrorMessage(e.getMessage());
        } catch (FenixActionException e) {
            setErrorMessage(e.getMessage());
        }
        return "";
    }
    
    private void checkCompetenceCourse() throws FenixFilterException, FenixServiceException, FenixActionException  {
        if (getCompetenceCourseID() == null || getCompetenceCourseID().intValue() == 0) {
            throw new FenixActionException("error.mustChooseACompetenceCourse");
        }
    }
    
    private void checkCourseGroup() throws FenixFilterException, FenixServiceException, FenixActionException {
        if (getCourseGroupID() == null || getCourseGroupID().intValue() == 0) {
            throw new FenixActionException("error.mustChooseACourseGroup");
        }
    }

    public void addContext(ActionEvent event) throws FenixFilterException {
        try {
            checkCourseGroup();
            Object args[] = { getCurricularCourse(), getCourseGroup(), getCurricularYearID(),
                    getCurricularSemesterID() };
            ServiceUtils.executeService(getUserView(), "AddContextToCurricularCourse", args);
            addInfoMessage(messages.getString("addedNewContextToCurricularCourse"));
            setContextID(0); // resetContextID
        } catch (FenixServiceException e) {
            setErrorMessage(e.getMessage());
        } catch (DomainException e) {
            setErrorMessage(e.getMessage());
        } catch (FenixActionException e) {
            setErrorMessage(e.getMessage());
        }
    }
    
    public String editContext() throws FenixFilterException {
        try {
            checkCourseGroup();
            Object args[] = { getCurricularCourse(), getContext(getContextID()), getCourseGroup(),
                    getCurricularYearID(), getCurricularSemesterID() };
            ServiceUtils.executeService(getUserView(), "EditContextFromCurricularCourse", args);
            setContextID(0); // resetContextID
        } catch (FenixServiceException e) {
            setErrorMessage(e.getMessage());
        } catch (DomainException e) {
            setErrorMessage(e.getMessage());
        } catch (FenixActionException e) {
            setErrorMessage(e.getMessage());
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
            setErrorMessage(e.getMessage());
        } catch (DomainException e) {
            setErrorMessage(e.getMessage());
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
        result.add(new SelectItem(Integer.valueOf(0), "[" + messages.getString("chooseOneType") + "]"));
        try {
            Date now = Calendar.getInstance().getTime();
            for (final IUnit unit : (List<IUnit>) readAllDomainObjects(Unit.class)) {
                if (unit.isActive(now) && unit.getType() != null
                        && unit.getType().equals(UnitType.DEPARTMENT)) {
                    result.add(new SelectItem(unit.getIdInternal(), unit.getName()));
                }
            }
        } catch (FenixServiceException e) {
            setErrorMessage("error.gettingDepartmentUnits");
        }
        return result;
    }

    private List<SelectItem> readCompetenceCourses() throws FenixFilterException, FenixServiceException {
        final List<SelectItem> result = new ArrayList<SelectItem>();
        result.add(new SelectItem(Integer.valueOf(0), "[" + messages.getString("chooseOneType") + "]"));
        final IUnit departmentUnit = getDepartmentUnit();
        if (departmentUnit != null) {
            for (final IUnit scientificAreaUnit : departmentUnit.getScientificAreaUnits()) {
                for (final IUnit competenceCourseGroupUnit : scientificAreaUnit.getCompetenceCourseGroupUnits()) {
                    for (final ICompetenceCourse competenceCourse : competenceCourseGroupUnit.getCompetenceCourses()) {
                        if (!competenceCourse.getCurricularStage().equals(CurricularStage.DRAFT)) {
                            result.add(new SelectItem(competenceCourse.getIdInternal(), competenceCourse.getName()));
                        }                        
                    }
                }
            }
        }
        return result;
    }

    private List<SelectItem> readCourseGroups() throws FenixFilterException, FenixServiceException {
        final List<SelectItem> result = new ArrayList<SelectItem>();
        result.add(new SelectItem(Integer.valueOf(0), "[" + messages.getString("chooseOneType") + "]"));
        final IDegreeModule degreeModule = getDegreeCurricularPlan().getDegreeModule();
        if (degreeModule instanceof ICourseGroup) {
            collectChildCourseGroups(result, (ICourseGroup) degreeModule, "");
        }
        return result;
    }

    private void collectChildCourseGroups(final List<SelectItem> result, final ICourseGroup courseGroup,
            final String previousCourseGroupName) {
        String currentCourseGroupName = "";
        if (!courseGroup.isRoot()) {
            currentCourseGroupName = ((previousCourseGroupName.length() == 0) ? "" : (previousCourseGroupName + " > "))
                    + courseGroup.getName();
            result.add(new SelectItem(courseGroup.getIdInternal(), currentCourseGroupName));
        }
        for (final IContext context : courseGroup.getContextsWithCourseGroups()) {
            collectChildCourseGroups(result, (ICourseGroup) context.getDegreeModule(),
                    currentCourseGroupName);
        }
    }

    private List<SelectItem> readCurricularCourses() throws FenixFilterException, FenixServiceException {
        final List<SelectItem> result = new ArrayList<SelectItem>();
        for (final ICurricularCourse curricularCourse : getDegreeCurricularPlan()
                .getDcpCurricularCourses()) {
            result.add(new SelectItem(curricularCourse.getIdInternal(), curricularCourse.getName()));
        }
        return result;
    }
}
