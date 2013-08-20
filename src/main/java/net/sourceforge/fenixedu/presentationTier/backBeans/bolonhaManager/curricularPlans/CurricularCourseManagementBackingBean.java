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

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.bolonhaManager.AddContextToCurricularCourse;
import net.sourceforge.fenixedu.applicationTier.Servico.bolonhaManager.CreateCurricularCourse;
import net.sourceforge.fenixedu.applicationTier.Servico.bolonhaManager.DeleteContextFromDegreeModule;
import net.sourceforge.fenixedu.applicationTier.Servico.bolonhaManager.EditContextFromCurricularCourse;
import net.sourceforge.fenixedu.applicationTier.Servico.bolonhaManager.EditCurricularCourse;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.commons.CurricularCourseByExecutionSemesterBean;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRule;
import net.sourceforge.fenixedu.domain.curriculum.CurricularCourseType;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.CompetenceCourseGroupUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.DepartmentUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.ScientificAreaUnit;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicPeriod;
import net.sourceforge.fenixedu.injectionCode.IllegalDataAccessException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.masterDegree.coordinator.CoordinatedDegreeInfo;
import net.sourceforge.fenixedu.presentationTier.backBeans.base.FenixBackingBean;
import net.sourceforge.fenixedu.util.CurricularRuleLabelFormatter;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang.StringUtils;

import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class CurricularCourseManagementBackingBean extends FenixBackingBean {

    protected final ResourceBundle bolonhaBundle = getResourceBundle("resources/BolonhaManagerResources");
    protected final ResourceBundle enumerationBundle = getResourceBundle("resources/EnumerationResources");
    protected final ResourceBundle domainExceptionBundle = getResourceBundle("resources/DomainExceptionResources");
    protected final String NO_SELECTION_STRING = "no_selection";

    private String competenceCourseID = null;
    private String courseGroupID = null;
    private Integer curricularYearID = null;
    private Integer curricularSemesterID = null;
    private String contextID = null;
    private String curricularCourseID = null;
    private String executionPeriodOID = null;
    private boolean resetCompetenceCourseID = false;
    private boolean toDelete = false;

    private Double weight = null;
    private String prerequisites;
    private String prerequisitesEn;

    private CompetenceCourse competenceCourse = null;
    private DegreeCurricularPlan degreeCurricularPlan = null;
    private CurricularCourse curricularCourse = null;
    private Context context = null;

    public List<SelectItem> departmentUnits = null;
    public List<SelectItem> courseGroups = null;
    public List<SelectItem> curricularCourses = null;
    public List<SelectItem> executionYearItems = null;

    private CurricularCourseByExecutionSemesterBean curricularCourseSemesterBean = null;

    public CurricularCourseManagementBackingBean() {
        if (getCurricularCourse() != null && getExecutionYear() != null) {
            curricularCourseSemesterBean =
                    new CurricularCourseByExecutionSemesterBean(getCurricularCourse(),
                            ExecutionSemester.readBySemesterAndExecutionYear(2, getExecutionYear().getYear()));
        }
    }

    public CurricularCourseByExecutionSemesterBean getCurricularCourseSemesterBean() {
        return curricularCourseSemesterBean;
    }

    public void setCurricularCourseSemesterBean(CurricularCourseByExecutionSemesterBean curricularCourseSemesterBean) {
        this.curricularCourseSemesterBean = curricularCourseSemesterBean;
    }

    public String getDegreeCurricularPlanID() {
        CoordinatedDegreeInfo.setCoordinatorContext(getRequest());
        return getAndHoldStringParameter("degreeCurricularPlanID");
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

    public String getContextID() {
        return (contextID == null) ? (contextID = getAndHoldStringParameter("contextID")) : contextID;
    }

    public void setContextID(String contextID) {
        this.contextID = contextID;
    }

    public String getContextIDToDelete() {
        return getAndHoldStringParameter("contextIDToDelete");
    }

    public String getCourseGroupID() {
        if (courseGroupID == null) {
            courseGroupID = getAndHoldStringParameter("courseGroupID");
            if (courseGroupID == null) {
                courseGroupID =
                        (getContext(getContextID()) != null) ? getContext(getContextID()).getParentCourseGroup().getExternalId() : courseGroupID;
            }
        }
        return courseGroupID;
    }

    public void setCourseGroupID(String courseGroupID) {
        this.courseGroupID = courseGroupID;
    }

    public String getCurricularCourseID() {
        return (curricularCourseID == null) ? (curricularCourseID = getAndHoldStringParameter("curricularCourseID")) : curricularCourseID;
    }

    public void setCurricularCourseID(String curricularCourseID) {
        this.curricularCourseID = curricularCourseID;
    }

    public String getExecutionPeriodOID() {
        return (executionPeriodOID == null) ? (executionPeriodOID = getAndHoldStringParameter("executionPeriodOID")) : executionPeriodOID;
    }

    public void setExecutionPeriodOID(String executionPeriodOID) {
        this.executionPeriodOID = executionPeriodOID;
    }

    public String getDepartmentUnitID() {
        if (getViewState().getAttribute("departmentUnitID") == null && getCurricularCourse() != null) {
            getViewState().setAttribute("departmentUnitID",
                    getCurricularCourse().getCompetenceCourse().getDepartmentUnit().getExternalId());
        }
        return (String) getViewState().getAttribute("departmentUnitID");
    }

    public void setDepartmentUnitID(Integer departmentUnitID) {
        getViewState().setAttribute("departmentUnitID", departmentUnitID);
    }

    public String getCompetenceCourseID() {
        if (competenceCourseID == null && getCurricularCourse() != null) {
            competenceCourseID = getCurricularCourse().getCompetenceCourse().getExternalId();
        }
        return competenceCourseID;
    }

    public void setCompetenceCourseID(String competenceCourseID) {
        this.competenceCourseID = resetCompetenceCourseID ? null : competenceCourseID;
    }

    public List<SelectItem> getDepartmentUnits() {
        return (departmentUnits == null) ? (departmentUnits = readDepartmentUnits()) : departmentUnits;
    }

    public List<SelectItem> getAllowedDepartmentUnits() {
        return (departmentUnits == null) ? (departmentUnits = readAllowedDepartmentUnits()) : departmentUnits;
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
        result.add(new SelectItem(this.NO_SELECTION_STRING, bolonhaBundle.getString("choose")));
        for (int i = 1; i <= years; i++) {
            result.add(new SelectItem(Integer.valueOf(i), String.valueOf(i)
                    + bolonhaBundle.getString("label.context.period.sign")));
        }
        return result;
    }

    public List<SelectItem> getCurricularSemesters() {
        final List<SelectItem> result = new ArrayList<SelectItem>(2);

        result.add(new SelectItem(this.NO_SELECTION_STRING, bolonhaBundle.getString("choose")));
        result.add(new SelectItem(Integer.valueOf(1), String.valueOf(1) + bolonhaBundle.getString("label.context.period.sign")));
        result.add(new SelectItem(Integer.valueOf(2), String.valueOf(2) + bolonhaBundle.getString("label.context.period.sign")));
        return result;
    }

    public DegreeCurricularPlan getDegreeCurricularPlan() {
        return (degreeCurricularPlan == null) ? (degreeCurricularPlan =
                AbstractDomainObject.fromExternalId(getDegreeCurricularPlanID())) : degreeCurricularPlan;
    }

    public CourseGroup getCourseGroup() {
        return (CourseGroup) AbstractDomainObject.fromExternalId(getCourseGroupID());
    }

    public DepartmentUnit getDepartmentUnit() {
        if (getDepartmentUnitID() != null && getDepartmentUnitID() != null) {
            return (DepartmentUnit) AbstractDomainObject.fromExternalId(getDepartmentUnitID());
        }
        return null;
    }

    public CompetenceCourse getCompetenceCourse() {
        if (competenceCourse == null && getCompetenceCourseID() != null && !getCompetenceCourseID().equals(0)) {
            competenceCourse = AbstractDomainObject.fromExternalId(getCompetenceCourseID());
        }
        return competenceCourse;
    }

    public CurricularCourse getCurricularCourse() {
        return (curricularCourse == null && getCurricularCourseID() != null) ? (curricularCourse =
                (CurricularCourse) AbstractDomainObject.fromExternalId(getCurricularCourseID())) : curricularCourse;
    }

    protected Context getContext(String contextID) {
        return (context == null && contextID != null) ? (context = AbstractDomainObject.fromExternalId(contextID)) : context;
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
            curricularYearID = getContext(getContextID()).getCurricularPeriod().getOrderByType(AcademicPeriod.YEAR);
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
            curricularSemesterID = getContext(getContextID()).getCurricularPeriod().getOrderByType(AcademicPeriod.SEMESTER);
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
        if (getViewState().getAttribute("nameEn") == null && getCurricularCourse() != null
                && !StringUtils.isEmpty(getCurricularCourse().getNameEn())) {
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

    protected boolean isBolonha() {
        return getDegreeCurricularPlan().isBolonhaDegree();
    }

    public String getExecutionYearID() {
        if (getViewState().getAttribute("executionYearID") == null) {
            if (getAndHoldStringParameter("executionYearID") != null) {
                setExecutionYearID(getAndHoldStringParameter("executionYearID"));
            }
        }
        return (String) getViewState().getAttribute("executionYearID");
    }

    public void setExecutionYearID(String executionYearID) {
        getViewState().setAttribute("executionYearID", executionYearID);
    }

    public ExecutionYear getExecutionYear() {
        String executionYearId = getExecutionYearID();

        ExecutionYear oldestContextExecutionYear = getDegreeCurricularPlan().getOldestContextExecutionYear();

        if (executionYearId != null) {
            return AbstractDomainObject.fromExternalId(executionYearId);
        }

        ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();

        if (oldestContextExecutionYear != null && oldestContextExecutionYear.isAfter(currentExecutionYear)) {
            return oldestContextExecutionYear;
        }

        return currentExecutionYear;
    }

    protected InfoExecutionYear getCurrentExecutionYear() {
        return InfoExecutionYear.newInfoFromDomain(ExecutionYear.readCurrentExecutionYear());
    }

    public List<SelectItem> getExecutionYearItems() {
        return (executionYearItems == null) ? (executionYearItems = readExecutionYearItems()) : executionYearItems;
    }

    public String getBeginExecutionPeriodID() {
        if (getViewState().getAttribute("beginExecutionPeriodID") == null && getContext(getContextID()) != null) {
            setBeginExecutionPeriodID(getContext(getContextID()).getBeginExecutionPeriod().getExternalId());
        }
        return (String) getViewState().getAttribute("beginExecutionPeriodID");
    }

    public void setBeginExecutionPeriodID(String beginExecutionPeriodID) {
        getViewState().setAttribute("beginExecutionPeriodID", beginExecutionPeriodID);
    }

    public String getEndExecutionPeriodID() {
        if (getViewState().getAttribute("endExecutionPeriodID") == null && getContext(getContextID()) != null) {
            setEndExecutionPeriodID((getContext(getContextID()).getEndExecutionPeriod() != null) ? getContext(getContextID())
                    .getEndExecutionPeriod().getExternalId() : NO_SELECTION_STRING);
        }
        return (String) getViewState().getAttribute("endExecutionPeriodID");
    }

    public void setEndExecutionPeriodID(String endExecutionPeriodID) {
        getViewState().setAttribute("endExecutionPeriodID", endExecutionPeriodID);
    }

    public List<SelectItem> getBeginExecutionPeriodItems() {
        return readExecutionPeriodItems();
    }

    public List<SelectItem> getEndExecutionPeriodItems() {
        final List<SelectItem> result = new ArrayList<SelectItem>(readExecutionPeriodItems());
        result.add(0, new SelectItem(NO_SELECTION_STRING, bolonhaBundle.getString("opened")));
        return result;
    }

    protected List<SelectItem> readExecutionPeriodItems() {

        final ExecutionSemester minimumExecutionPeriod = getMinimumExecutionPeriod();
        final List<ExecutionSemester> notClosedExecutionPeriods = ExecutionSemester.readNotClosedExecutionPeriods();
        Collections.sort(notClosedExecutionPeriods);

        final List<SelectItem> result = new ArrayList<SelectItem>();
        for (final ExecutionSemester notClosedExecutionPeriod : notClosedExecutionPeriods) {
            if (minimumExecutionPeriod == null || notClosedExecutionPeriod.isAfterOrEquals(minimumExecutionPeriod)) {
                result.add(new SelectItem(notClosedExecutionPeriod.getExternalId(), notClosedExecutionPeriod.getName() + " "
                        + notClosedExecutionPeriod.getExecutionYear().getYear()));
            }
        }
        return result;
    }

    protected ExecutionSemester getMinimumExecutionPeriod() {
        return (getCourseGroup() == null) ? null : getCourseGroup().getMinimumExecutionPeriod();
    }

    public List<Context> getCurricularCourseParentContexts() {
        return getCurricularCourse().getParentContextsByExecutionYear(getExecutionYear());
    }

    public String createCurricularCourse() {
        try {
            checkCourseGroup();
            checkCurricularSemesterAndYear();
            runCreateCurricularCourse();

        } catch (FenixActionException e) {
            this.addErrorMessage(bolonhaBundle.getString(e.getMessage()));
            return "";
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

    private void runCreateCurricularCourse() throws FenixActionException, FenixServiceException {
        final CurricularCourseType curricularCourseType = CurricularCourseType.valueOf(getSelectedCurricularCourseType());
        if (curricularCourseType.equals(CurricularCourseType.NORMAL_COURSE)) {

            checkCompetenceCourse();
            CreateCurricularCourse.run(new CreateCurricularCourse.CreateCurricularCourseArgs(getWeight(), getPrerequisites(),
                    getPrerequisitesEn(), getCompetenceCourseID(), getCourseGroupID(), getCurricularYearID(),
                    getCurricularSemesterID(), getDegreeCurricularPlanID(), getBeginExecutionPeriodID(),
                    getFinalEndExecutionPeriodID()));

        } else if (curricularCourseType.equals(CurricularCourseType.OPTIONAL_COURSE)) {

            checkCurricularCourseNameAndNameEn();
            CreateCurricularCourse.run(new CreateCurricularCourse.CreateOptionalCurricularCourseArgs(getDegreeCurricularPlanID(),
                    getCourseGroupID(), getName(), getNameEn(), getCurricularYearID(), getCurricularSemesterID(),
                    getBeginExecutionPeriodID(), getFinalEndExecutionPeriodID()));

        }
    }

    public String editCurricularCourse() {
        try {
            runEditCurricularCourse();
            addInfoMessage(bolonhaBundle.getString("curricularCourseEdited"));
        } catch (FenixServiceException e) {
            addErrorMessage(bolonhaBundle.getString(e.getMessage()));
        } catch (FenixActionException e) {
            addErrorMessage(bolonhaBundle.getString(e.getMessage()));
        }
        setContextID(null); // resetContextID
        return "";
    }

    private void runEditCurricularCourse() throws FenixActionException, FenixServiceException {
        final CurricularCourseType curricularCourseType = CurricularCourseType.valueOf(getSelectedCurricularCourseType());
        if (curricularCourseType.equals(CurricularCourseType.NORMAL_COURSE)) {
            checkCompetenceCourse();
            EditCurricularCourse.run(getCurricularCourse(), getWeight(), getPrerequisites(), getPrerequisitesEn(),
                    getCompetenceCourse());
        } else if (curricularCourseType.equals(CurricularCourseType.OPTIONAL_COURSE)) {
            EditCurricularCourse.run(getCurricularCourse(), getName(), getNameEn());
        }
    }

    private void checkCompetenceCourse() throws FenixActionException {
        if (getCompetenceCourseID() == null) {
            throw new FenixActionException("error.mustChooseACompetenceCourse");
        }
    }

    protected void checkCourseGroup() throws FenixActionException {
        if (getCourseGroupID() == null || getCourseGroupID().equals(this.NO_SELECTION_STRING)) {
            throw new FenixActionException("error.mustChooseACourseGroup");
        }
    }

    private void checkCurricularCourse() throws FenixActionException {
        if (getCurricularCourseID() == null || getCurricularCourseID().equals(this.NO_SELECTION_STRING)) {
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

    protected void checkCurricularSemesterAndYear() throws FenixActionException {
        if (getCurricularSemesterID() == null || getCurricularSemesterID().equals(this.NO_SELECTION_STRING)) {
            throw new FenixActionException("error.mustChooseACurricularSemester");
        }
        if (getCurricularYearID() == null || getCurricularYearID().equals(this.NO_SELECTION_STRING)) {
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
            AddContextToCurricularCourse.run(getCurricularCourse(), getCourseGroup(), getBeginExecutionPeriodID(),
                    getFinalEndExecutionPeriodID(), getCurricularYearID(), getCurricularSemesterID());
            addInfoMessage(bolonhaBundle.getString("addedNewContextToCurricularCourse"));
        } catch (FenixActionException e) {
            this.addErrorMessage(bolonhaBundle.getString(e.getMessage()));
            return "";
        } catch (FenixServiceException e) {
            this.addErrorMessage(bolonhaBundle.getString(e.getMessage()));
            return "";
        } catch (DomainException e) {
            this.addErrorMessages(bolonhaBundle, e.getMessage(), e.getArgs());
            return "";
        } catch (Exception e) {
            this.addErrorMessage(bolonhaBundle.getString("general.error"));
            return "buildCurricularPlan";
        }
        setContextID(null); // resetContextID
        return "buildCurricularPlan";
    }

    public String editContext() {
        try {
            checkCourseGroup();
            EditContextFromCurricularCourse
                    .run(getCurricularCourse(), getContext(getContextID()), getCourseGroup(), getCurricularYearID(),
                            getCurricularSemesterID(), getBeginExecutionPeriodID(), getFinalEndExecutionPeriodID());
        } catch (IllegalDataAccessException e) {
            this.addErrorMessage(bolonhaBundle.getString("error.notAuthorized"));
        } catch (DomainException e) {
            addErrorMessage(domainExceptionBundle.getString(e.getMessage()));
        } catch (FenixActionException e) {
            addErrorMessage(bolonhaBundle.getString(e.getMessage()));
        }
        setContextID(null); // resetContextID
        return "";
    }

    protected String getFinalEndExecutionPeriodID() {
        return (getViewState().getAttribute("endExecutionPeriodID") == null || getViewState()
                .getAttribute("endExecutionPeriodID").equals(NO_SELECTION_STRING)) ? null : (String) getViewState().getAttribute(
                "endExecutionPeriodID");
    }

    public void tryDeleteContext(ActionEvent event) {
        if (!isToDelete()) {
            deleteContext(event);
        } else {
            setContextID(getContextIDToDelete());
        }
    }

    public void deleteContext(ActionEvent event) {
        try {
            DeleteContextFromDegreeModule.run(getCurricularCourseID(), getContextIDToDelete());
            addInfoMessage(bolonhaBundle.getString("successAction"));
        } catch (IllegalDataAccessException e) {
            this.addErrorMessage(bolonhaBundle.getString("error.notAuthorized"));
        } catch (FenixServiceException e) {
            addErrorMessage(e.getMessage());
        } catch (DomainException e) {
            addErrorMessage(getFormatedMessage(domainExceptionBundle, e.getKey(), e.getArgs()));
        }
        setContextID(null); // resetContextID
    }

    public String cancel() {
        setContextID(null);
        return "";
    }

    public String editCurricularCourseReturnPath() {
        return !toDelete ? "" : "deleteCurricularCourseContext";
    }

    private List<SelectItem> readDepartmentUnits() {
        final List<SelectItem> result = new ArrayList<SelectItem>();
        for (final Object departmentObject : RootDomainObject.getInstance().getDepartments()) {
            DepartmentUnit departmentUnit = ((Department) departmentObject).getDepartmentUnit();
            result.add(new SelectItem(departmentUnit.getExternalId(), departmentUnit.getName()));
        }
        Collections.sort(result, new BeanComparator("label"));
        result.add(0, new SelectItem(this.NO_SELECTION_STRING, bolonhaBundle.getString("choose")));
        return result;
    }

    private List<SelectItem> readAllowedDepartmentUnits() {
        final List<SelectItem> result = new ArrayList<SelectItem>();
        for (final Department department : RootDomainObject.getInstance().getDepartments()) {
            if (department.getCompetenceCourseMembersGroup() != null
                    && department.getCompetenceCourseMembersGroup().isMember(getUserView().getPerson())) {
                DepartmentUnit departmentUnit = department.getDepartmentUnit();
                result.add(new SelectItem(departmentUnit.getExternalId(), departmentUnit.getName()));
            }
        }
        Collections.sort(result, new BeanComparator("label"));
        if (result.size() == 1) {
            Department personDepartment = getPersonDepartment();
            if (personDepartment != null
                    && !result.get(0).getValue().equals(personDepartment.getDepartmentUnit().getExternalId())) {
                result.add(0, new SelectItem(personDepartment.getDepartmentUnit().getExternalId(), personDepartment.getName()));
            }
        }
        return result;
    }

    private Department getPersonDepartment() {
        final IUserView userView = getUserView();
        final Person person = userView == null ? null : userView.getPerson();
        final Employee employee = person == null ? null : person.getEmployee();
        return employee == null ? null : employee.getCurrentDepartmentWorkingPlace();
    }

    private List<SelectItem> readCompetenceCourses() {
        final List<SelectItem> result = new ArrayList<SelectItem>();
        final DepartmentUnit departmentUnit = getDepartmentUnit();
        if (departmentUnit != null) {
            for (final ScientificAreaUnit scientificAreaUnit : departmentUnit.getScientificAreaUnits()) {
                for (final CompetenceCourseGroupUnit competenceCourseGroupUnit : scientificAreaUnit
                        .getCompetenceCourseGroupUnits()) {
                    for (final CompetenceCourse competenceCourse : competenceCourseGroupUnit.getCompetenceCourses()) {
                        if (competenceCourse.getCurricularStage() != CurricularStage.DRAFT) {
                            result.add(new SelectItem(competenceCourse.getExternalId(), competenceCourse.getName() + " ("
                                    + enumerationBundle.getString(competenceCourse.getCurricularStage().getName()) + ")"));
                        }
                    }
                }
            }
            Collections.sort(result, new BeanComparator("label"));
        }
        result.add(0, new SelectItem(this.NO_SELECTION_STRING, bolonhaBundle.getString("choose")));
        return result;
    }

    private List<SelectItem> readDegreeModules(Class<? extends DegreeModule> clazz) {
        final List<SelectItem> result = new ArrayList<SelectItem>();
        final List<List<DegreeModule>> degreeModulesSet =
                getDegreeCurricularPlan().getDcpDegreeModulesIncludingFullPath(clazz, getExecutionYear());
        for (final List<DegreeModule> degreeModules : degreeModulesSet) {
            final StringBuilder pathName = new StringBuilder();
            for (final DegreeModule degreeModule : degreeModules) {
                pathName.append((pathName.length() == 0) ? "" : " > ").append(degreeModule.getName());
            }
            result.add(new SelectItem(degreeModules.get(degreeModules.size() - 1).getExternalId(), pathName.toString()));
        }
        Collections.sort(result, new BeanComparator("label"));
        result.add(0, new SelectItem(this.NO_SELECTION_STRING, bolonhaBundle.getString("choose")));
        return result;
    }

    protected List<SelectItem> readExecutionYearItems() {
        final List<SelectItem> result = new ArrayList<SelectItem>();

        final List<ExecutionDegree> executionDegrees = getDegreeCurricularPlan().getExecutionDegrees();

        if (executionDegrees.isEmpty()) {
            final ExecutionYear executionYear =
                    getDegreeCurricularPlan().getRoot().getMinimumExecutionPeriod().getExecutionYear();
            result.add(new SelectItem(executionYear.getExternalId(), executionYear.getYear()));
            return result;
        }

        for (ExecutionDegree executionDegree : executionDegrees) {
            result.add(new SelectItem(executionDegree.getExecutionYear().getExternalId(), executionDegree.getExecutionYear()
                    .getYear()));
        }

        if (getExecutionYearID() == null) {
            setExecutionYearID(getDegreeCurricularPlan().getMostRecentExecutionDegree().getExecutionYear().getExternalId());
        }

        return result;
    }

    public Degree getDegree() {
        return getDegreeCurricularPlan().getDegree();
    }

    public String getDegreePresentationName() {
        return getDegree().getPresentationName(getExecutionYear());
    }

    public List<CompetenceCourse> getDegreeCurricularPlanCompetenceCourses() {
        return getDegreeCurricularPlan().getCompetenceCourses(getExecutionYear());
    }

    public ExecutionSemester getBeginExecutionPeriod() {
        return AbstractDomainObject.fromExternalId(getBeginExecutionPeriodID());
    }

    public ExecutionSemester getEndExecutionPeriod() {
        return getEndExecutionPeriodID() == null ? null : AbstractDomainObject
                .<ExecutionSemester> fromExternalId(getEndExecutionPeriodID());
    }
}
