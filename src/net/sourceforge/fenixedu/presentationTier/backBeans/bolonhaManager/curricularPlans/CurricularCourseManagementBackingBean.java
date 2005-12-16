/*
 * Created on Dec 7, 2005
 */
package net.sourceforge.fenixedu.presentationTier.backBeans.bolonhaManager.curricularPlans;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ICompetenceCourse;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.IContext;
import net.sourceforge.fenixedu.domain.degreeStructure.ICourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.IDegreeModule;
import net.sourceforge.fenixedu.domain.organizationalStructure.IUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitType;
import net.sourceforge.fenixedu.presentationTier.backBeans.base.FenixBackingBean;

public class CurricularCourseManagementBackingBean extends FenixBackingBean {
    private final ResourceBundle messages = getResourceBundle("ServidorApresentacao/BolonhaManagerResources");

    private Integer degreeCurricularPlanID = null;
    private Integer courseGroupID = null;
    private Integer curricularYearID = null;
    private Integer curricularSemesterID = null;
    private boolean resetCompetenceCourseID = false;
    
    private Double weight = null;
    private String prerequisites;
    private String prerequisitesEn;

    private IDegreeCurricularPlan degreeCurricularPlan = null;
    private ICourseGroup courseGroup = null;

    public List<SelectItem> departmentUnits = null;
    public List<SelectItem> competenceCourses = null;
    public List<SelectItem> courseGroups = null;

    public Integer getDegreeCurricularPlanID() {
        return (degreeCurricularPlanID == null) ? (degreeCurricularPlanID = getAndHoldIntegerParameter("degreeCurricularPlanID"))
                : degreeCurricularPlanID;
    }

    public void setDegreeCurricularPlanID(Integer degreeCurricularPlanID) {
        this.degreeCurricularPlanID = degreeCurricularPlanID;
    }

    public Integer getCourseGroupID() {
        return (courseGroupID == null) ? courseGroupID = getAndHoldIntegerParameter("courseGroupID")
                : courseGroupID;
    }

    public void setCourseGroupID(Integer courseGroupID) {
        this.courseGroupID = courseGroupID;
    }

    public Integer getDepartmentUnitID() {
        return (Integer) getViewState().getAttribute("departmentUnitID");
    }

    public void setDepartmentUnitID(Integer departmentUnitID) {
        getViewState().setAttribute("departmentUnitID", departmentUnitID);
    }

    public Integer getCompetenceCourseID() {
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
        return readCompetenceCourses();
    }

    public List<SelectItem> getCourseGroups() throws FenixFilterException {
        return (courseGroups == null) ? (courseGroups = readCourseGroups()) : courseGroups;
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

    public void resetCompetenceCourses(ValueChangeEvent event) {
        resetCompetenceCourseID = true;
    }

    public Double getWeight() {
        return (weight == null) ? Double.valueOf(0) : weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }
    
    public String getPrerequisites() {
        return prerequisites;
    }

    public void setPrerequisites(String prerequisites) {
        this.prerequisites = prerequisites;
    }

    public String getPrerequisitesEn() {
        return prerequisitesEn;
    }

    public void setPrerequisitesEn(String prerequisitesEn) {
        this.prerequisitesEn = prerequisitesEn;
    }

    public Integer getCurricularYearID() {
        return curricularYearID;
    }

    public void setCurricularYearID(Integer curricularYearID) {
        this.curricularYearID = curricularYearID;
    }

    public Integer getCurricularSemesterID() {
        return curricularSemesterID;
    }

    public void setCurricularSemesterID(Integer curricularSemesterID) {
        this.curricularSemesterID = curricularSemesterID;
    }
    
    public String createCurricularCourse() {
        Object args[] = {getWeight(), getCompetenceCourseID(), getCourseGroupID(), getCurricularYearID(), getCurricularSemesterID()};        
        return "curricularPlansManagement";
    }

    public String editCurricularCourse() {
        return "curricularPlansManagement";
    }

    public String associateCurricularCourse() {
        return "curricularPlansManagement";
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
            // Error reading departmentUnit (empty competenceCourse list)
        }
        return result;
    }

    private List<SelectItem> readCourseGroups() throws FenixFilterException {
        final List<SelectItem> result = new ArrayList<SelectItem>();
        result.add(new SelectItem(Integer.valueOf(0), "[" + messages.getString("chooseOneType") + "]"));
        try {
            final IDegreeModule degreeModule = getDegreeCurricularPlan().getDegreeModule();
            if (degreeModule instanceof ICourseGroup) {
                final ICourseGroup courseGroup = (ICourseGroup) degreeModule;
                collectChildCourseGroups(result, courseGroup, "");
            }
        } catch (FenixServiceException e) {
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
}
