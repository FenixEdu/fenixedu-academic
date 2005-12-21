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
import net.sourceforge.fenixedu.domain.degreeStructure.IContext;
import net.sourceforge.fenixedu.domain.degreeStructure.ICourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.IDegreeModule;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.IUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitType;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.backBeans.base.FenixBackingBean;

public class CurricularCourseManagementBackingBean extends FenixBackingBean {
    private final ResourceBundle messages = getResourceBundle("ServidorApresentacao/BolonhaManagerResources");

    private Integer degreeCurricularPlanID = null;
    private Integer courseGroupID = null;
    private Integer curricularYearID = null;
    private Integer curricularSemesterID = null;
    private boolean resetCompetenceCourseID = false;
    private Integer curricularCourseID = null;
    private Integer contextID = null;
    private boolean forceDeleteContext = false;
    
    private Double weight = null;
    private String prerequisites;
    private String prerequisitesEn;

    private IDegreeCurricularPlan degreeCurricularPlan = null;
    private ICourseGroup courseGroup = null;

    public List<SelectItem> departmentUnits = null;
    public List<SelectItem> competenceCourses = null;
    public List<SelectItem> courseGroups = null;
    public List<SelectItem> curricularCourses = null;

    public Integer getDegreeCurricularPlanID() {
        return (degreeCurricularPlanID == null) ? (degreeCurricularPlanID = getAndHoldIntegerParameter("degreeCurricularPlanID"))
                : degreeCurricularPlanID;
    }

    public void setDegreeCurricularPlanID(Integer degreeCurricularPlanID) {
        this.degreeCurricularPlanID = degreeCurricularPlanID;
    }

    public Integer getCourseGroupID() throws FenixFilterException {
        if (courseGroupID == null) {
            if ((courseGroupID = getAndHoldIntegerParameter("courseGroupID")) == null) {
                try {
                    courseGroupID = getContext(getContextID()).getCourseGroup().getIdInternal();
                } catch (FenixServiceException e) {
                    return courseGroupID;
                }
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
    
    public Integer getContextID() {
        return (contextID == null) ? (contextID = getAndHoldIntegerParameter("contextID")) : contextID;
    }

    public void setContextID(Integer contextID) {
        this.contextID = contextID;
    }
    
    public Integer getContextIDToDelete() {
        return getAndHoldIntegerParameter("contextIDToDelete");
    }
    
    public Integer getDepartmentUnitID() throws FenixFilterException {
        if (getViewState().getAttribute("departmentUnitID") == null) {
            try {
                getViewState().setAttribute("departmentUnitID",
                        getCurricularCourse().getCompetenceCourse().getDepartmentUnit().getIdInternal());
            } catch (FenixServiceException e) { //Ignore value              
            }
        }
        return (Integer) getViewState().getAttribute("departmentUnitID");
    }

    public void setDepartmentUnitID(Integer departmentUnitID) {
        getViewState().setAttribute("departmentUnitID", departmentUnitID);
    }

    public Integer getCompetenceCourseID() throws FenixFilterException {
        if (getViewState().getAttribute("competenceCourseID") == null) {
            try {
                getViewState().setAttribute("competenceCourseID",
                        getCurricularCourse().getCompetenceCourse().getIdInternal());
            } catch (FenixServiceException e) { //Ignore value       
            }
        }
        return (Integer) getViewState().getAttribute("competenceCourseID");
    }

    public void setCompetenceCourseID(Integer competenceCourseID) {
        getViewState().setAttribute("competenceCourseID",
                resetCompetenceCourseID ? Integer.valueOf(0) : competenceCourseID);
    }

    public List<SelectItem> getDepartmentUnits() throws FenixFilterException {
        return (departmentUnits == null) ? (departmentUnits = readDepartmentUnits()) : departmentUnits;
    }

    public List<SelectItem> getCompetenceCourses() throws FenixFilterException {
        return (competenceCourses == null) ? (competenceCourses = readCompetenceCourses()) : competenceCourses;
    }

    public List<SelectItem> getCourseGroups() throws FenixFilterException {
        return (courseGroups == null) ? (courseGroups = readCourseGroups()) : courseGroups;
    }
    
    public List<SelectItem> getCurricularCourses() throws FenixFilterException {
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
            IUnit departmentUnit = (IUnit) readDomainObject(Unit.class, getDepartmentUnitID());
            if (departmentUnit == null) {
                throw new FenixServiceException("error.gettingDepartmentUnit");
            }
            return departmentUnit;
        }
        throw new FenixServiceException("error.gettingDepartmentUnit");
    }
    
    public ICompetenceCourse getCompetenceCourse() throws FenixFilterException, FenixServiceException {
        if (getCompetenceCourseID() !=  null && !getCompetenceCourseID().equals(0)) {
            ICompetenceCourse competenceCourse = (ICompetenceCourse) readDomainObject(CompetenceCourse.class, getCompetenceCourseID());
            if (competenceCourse == null) {
                throw new FenixServiceException("error.noCompetenceCourse");
            }
            return competenceCourse;
        }
        throw new FenixServiceException("error.noCompetenceCourse");
    }
    
    public ICurricularCourse getCurricularCourse() throws FenixFilterException, FenixServiceException {
        if (getCurricularCourseID() != null) {
            ICurricularCourse curricularCourse = (ICurricularCourse) readDomainObject(CurricularCourse.class, getCurricularCourseID());
            if (curricularCourse == null) {
                throw new FenixServiceException("error.noCurricularCourse");
            }
            return curricularCourse;
        }
        throw new FenixServiceException("error.noCurricularCourse");
    }
    
    private IContext getContext(Integer contextID) throws FenixFilterException, FenixServiceException {
        if (contextID != null) {
            IContext context = (IContext) readDomainObject(Context.class, contextID);
            if (context == null) {
                throw new FenixServiceException("error.noContext");
            }
            return context;
        }
        throw new FenixServiceException("error.noContext");
    }

    public void resetCompetenceCourse(ValueChangeEvent event) {
        resetCompetenceCourseID = true;
        competenceCourses = null;
    }

    public Double getWeight() throws FenixFilterException {
        try {
            return (weight == null) ? (weight = getCurricularCourse().getWeigth()) : weight;
        } catch (FenixServiceException e) {
            return Double.valueOf(0);
        }
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }
    
    public String getPrerequisites() throws FenixFilterException {
        try {
            return (prerequisites == null) ? (prerequisites = getCurricularCourse().getPrerequisites())
                    : prerequisites;
        } catch (FenixServiceException e) {
            return prerequisites;
        }
    }

    public void setPrerequisites(String prerequisites) {
        this.prerequisites = prerequisites;
    }

    public String getPrerequisitesEn() throws FenixFilterException {
        try {
            return (prerequisitesEn == null) ? (prerequisitesEn = getCurricularCourse()
                    .getPrerequisitesEn()) : prerequisitesEn;
        } catch (FenixServiceException e) {
            return prerequisitesEn;
        }
    }

    public void setPrerequisitesEn(String prerequisitesEn) {
        this.prerequisitesEn = prerequisitesEn;
    }

    public Integer getCurricularYearID() throws FenixFilterException {
        try {
            return (curricularYearID == null) ? (curricularYearID = getContext(getContextID()).getCurricularSemester()
                    .getCurricularYear().getYear()) : curricularYearID;
        } catch (FenixServiceException e) {
            return curricularYearID;
        }
    }

    public void setCurricularYearID(Integer curricularYearID) {
        this.curricularYearID = curricularYearID;
    }

    public Integer getCurricularSemesterID() throws FenixFilterException {
        try {
            return (curricularSemesterID == null) ? (curricularSemesterID = getContext(getContextID())
                    .getCurricularSemester().getSemester()) : curricularSemesterID;
        } catch (FenixServiceException e) {
            return curricularSemesterID;
        }
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
            return "curricularPlansManagement";
        } catch (FenixServiceException e) {
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
        }
        return "";
    }
    
    private void checkCompetenceCourse() throws FenixFilterException, FenixServiceException {
        if (getCompetenceCourseID() == null || getCompetenceCourseID().intValue() == 0) {
            throw new FenixServiceException("error.mustChooseACompetenceCourse");
        }
    }
    
    private void checkCourseGroup() throws FenixFilterException, FenixServiceException {
        if (getCourseGroupID() == null || getCourseGroupID().intValue() == 0) {
            throw new FenixServiceException("error.mustChooseACourseGroup");
        }
    }

    public void addContext(ActionEvent event) throws FenixFilterException {
        try {
            checkCourseGroup();
            Object args[] = { getCurricularCourse(), getCourseGroup(), getCurricularYearID(),
                    getCurricularSemesterID() };
            ServiceUtils.executeService(getUserView(), "AddContext", args);
            addInfoMessage(messages.getString("addedNewContextToCurricularCourse"));
            setContextID(0); //resetContextID
        } catch (FenixServiceException e) {
            setErrorMessage(e.getMessage());
        } catch (DomainException e) {
            setErrorMessage(e.getMessage());
        }
    }
    
    public String editContext() throws FenixFilterException {
        try {
            checkCourseGroup();
            Object args[] = { getCurricularCourse(), getContext(getContextID()), getCourseGroup(),
                    getCurricularYearID(), getCurricularSemesterID() };
            ServiceUtils.executeService(getUserView(), "EditContext", args);
            setContextID(0); // resetContextID
        } catch (FenixServiceException e) {
            setErrorMessage(e.getMessage());
        } catch (DomainException e) {
            setErrorMessage(e.getMessage());
        }
        return "";
    }
    
    public String deleteContext() throws FenixFilterException {
        try {
            if (!forceDeleteContext && getCurricularCourse().getDegreeModuleContextsCount() == 1) {
                return "confirmDeleteCurricularCourse";
            }
            Object args[] = {getCurricularCourse(), getContext(getContextIDToDelete())};
            ServiceUtils.executeService(getUserView(), "DeleteContext", args);            
            setContextID(0); // resetContextID
        } catch (FenixServiceException e) {
            setErrorMessage(e.getMessage());
        } catch (DomainException e) {
            setErrorMessage(e.getMessage());
        }
        return forceDeleteContext ? "curricularPlansManagement" : "";
    }
    
    public void setForceDeleteContext(ActionEvent event) {
        forceDeleteContext = true;
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

    private List<SelectItem> readCompetenceCourses() throws FenixFilterException {
        final List<SelectItem> result = new ArrayList<SelectItem>();
        result.add(new SelectItem(Integer.valueOf(0), "[" + messages.getString("chooseOneType") + "]"));
        try {
            final IUnit departmentUnit = getDepartmentUnit();
            for (final IUnit scientificAreaUnit : departmentUnit.getScientificAreaUnits()) {
                for (final IUnit competenceCourseGroupUnit : scientificAreaUnit
                        .getCompetenceCourseGroupUnits()) {
                    for (final ICompetenceCourse competenceCourse : competenceCourseGroupUnit
                            .getCompetenceCourses()) {
                        result.add(new SelectItem(competenceCourse.getIdInternal(), competenceCourse
                                .getName()));
                    }
                }
            }
        } catch (FenixServiceException e) { 
            // Error reading departmentUnit (empty competenceCourse result)
        }
        return result;
    }

    private List<SelectItem> readCourseGroups() throws FenixFilterException {
        final List<SelectItem> result = new ArrayList<SelectItem>();
        result.add(new SelectItem(Integer.valueOf(0), "[" + messages.getString("chooseOneType") + "]"));
        try {
            final IDegreeModule degreeModule = getDegreeCurricularPlan().getDegreeModule();
            if (degreeModule instanceof ICourseGroup) {
                collectChildCourseGroups(result, (ICourseGroup) degreeModule, "");
            }
        } catch (FenixServiceException e) {
            // Error reading degreeCurricularPlan (empty courseGroup result)
        }
        return result;
    }

    private void collectChildCourseGroups(final List<SelectItem> result, ICourseGroup courseGroup,
            String previousCourseGroupName) {
        String currentCourseGroupName = "";
        if (courseGroup.getNewDegreeCurricularPlan() == null) { // not root node
            currentCourseGroupName = ((previousCourseGroupName.length() == 0) ? ""
                    : (previousCourseGroupName + " > "))
                    + courseGroup.getName();
            result.add(new SelectItem(courseGroup.getIdInternal(), currentCourseGroupName));
        }
        for (final IContext context : courseGroup.getContextsWithCourseGroups()) {
            collectChildCourseGroups(result, (ICourseGroup) context.getDegreeModule(),
                    currentCourseGroupName);
        }
    }

    private List<SelectItem> readCurricularCourses() throws FenixFilterException {        
        final List<SelectItem> result = new ArrayList<SelectItem>();
        try {
            for (final ICurricularCourse curricularCourse : getDegreeCurricularPlan().getDcpCurricularCourses()) {
                result.add(new SelectItem(curricularCourse.getIdInternal(), curricularCourse.getName()));
            }
        } catch (FenixServiceException e) {
            // Error reading degreeCurricularPlan (empty curricularCourse result)
        }
        return result;
    }
}
