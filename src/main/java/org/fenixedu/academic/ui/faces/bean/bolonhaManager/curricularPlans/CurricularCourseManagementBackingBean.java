/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on Dec 7, 2005
 */
package org.fenixedu.academic.ui.faces.bean.bolonhaManager.curricularPlans;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.CompetenceCourse;
import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.Department;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.domain.curricularRules.CurricularRule;
import org.fenixedu.academic.domain.curriculum.CurricularCourseType;
import org.fenixedu.academic.domain.degreeStructure.Context;
import org.fenixedu.academic.domain.degreeStructure.CourseGroup;
import org.fenixedu.academic.domain.degreeStructure.CurricularStage;
import org.fenixedu.academic.domain.degreeStructure.DegreeModule;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicPeriod;
import org.fenixedu.academic.dto.commons.CurricularCourseByExecutionSemesterBean;
import org.fenixedu.academic.predicate.IllegalDataAccessException;
import org.fenixedu.academic.service.services.bolonhaManager.AddContextToCurricularCourse;
import org.fenixedu.academic.service.services.bolonhaManager.CreateCurricularCourse;
import org.fenixedu.academic.service.services.bolonhaManager.DeleteContextFromDegreeModule;
import org.fenixedu.academic.service.services.bolonhaManager.EditContextFromCurricularCourse;
import org.fenixedu.academic.service.services.bolonhaManager.EditCurricularCourse;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.ui.faces.bean.base.FenixBackingBean;
import org.fenixedu.academic.ui.struts.action.coordinator.DegreeCoordinatorIndex;
import org.fenixedu.academic.ui.struts.action.exceptions.FenixActionException;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.CurricularRuleLabelFormatter;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import pt.ist.fenixframework.FenixFramework;

public class CurricularCourseManagementBackingBean extends FenixBackingBean {

    protected final String NO_SELECTION_STRING = "-1";
    protected final Integer NO_SELECTION_INTEGER = Integer.valueOf(-1);

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
            curricularCourseSemesterBean = new CurricularCourseByExecutionSemesterBean(getCurricularCourse(),
                    getExecutionYear().getLastExecutionPeriod());
        }
    }

    public CurricularCourseByExecutionSemesterBean getCurricularCourseSemesterBean() {
        return curricularCourseSemesterBean;
    }

    public void setCurricularCourseSemesterBean(CurricularCourseByExecutionSemesterBean curricularCourseSemesterBean) {
        this.curricularCourseSemesterBean = curricularCourseSemesterBean;
    }

    public String getDegreeCurricularPlanID() {
        DegreeCoordinatorIndex.setCoordinatorContext(getRequest());
        String degreeCurricularPlanId = getAndHoldStringParameter("degreeCurricularPlanID");
        if (Strings.isNullOrEmpty(degreeCurricularPlanId)) {
            degreeCurricularPlanId = getCurricularCourse().getDegreeCurricularPlan().getExternalId();
        }
        return degreeCurricularPlanId;
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
                courseGroupID = (getContext(getContextID()) != null) ? getContext(getContextID()).getParentCourseGroup()
                        .getExternalId() : courseGroupID;
            }
        }
        return courseGroupID;
    }

    public void setCourseGroupID(String courseGroupID) {
        this.courseGroupID = courseGroupID;
    }

    public String getCurricularCourseID() {
        return (curricularCourseID == null) ? (curricularCourseID =
                getAndHoldStringParameter("curricularCourseID")) : curricularCourseID;
    }

    public void setCurricularCourseID(String curricularCourseID) {
        this.curricularCourseID = curricularCourseID;
    }

    public String getExecutionPeriodOID() {
        return (executionPeriodOID == null) ? (executionPeriodOID =
                getAndHoldStringParameter("executionPeriodOID")) : executionPeriodOID;
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

    public void setDepartmentUnitID(String departmentUnitID) {
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
        final int years = getDegreeCurricularPlan().getDurationInYears();
        final List<SelectItem> result = new ArrayList<SelectItem>(years);
        result.add(new SelectItem(this.NO_SELECTION_INTEGER, BundleUtil.getString(Bundle.BOLONHA, "choose")));
        for (int i = 1; i <= years; i++) {
            result.add(new SelectItem(Integer.valueOf(i),
                    String.valueOf(i) + BundleUtil.getString(Bundle.BOLONHA, "label.context.period.sign")));
        }
        return result;
    }

    public List<SelectItem> getCurricularSemesters() {
        final List<SelectItem> result = new ArrayList<SelectItem>(2);

        result.add(new SelectItem(this.NO_SELECTION_INTEGER, BundleUtil.getString(Bundle.BOLONHA, "choose")));
        result.add(new SelectItem(Integer.valueOf(1),
                String.valueOf(1) + BundleUtil.getString(Bundle.BOLONHA, "label.context.period.sign")));
        result.add(new SelectItem(Integer.valueOf(2),
                String.valueOf(2) + BundleUtil.getString(Bundle.BOLONHA, "label.context.period.sign")));
        return result;
    }

    public DegreeCurricularPlan getDegreeCurricularPlan() {
        return (degreeCurricularPlan == null) ? (degreeCurricularPlan =
                FenixFramework.getDomainObject(getDegreeCurricularPlanID())) : degreeCurricularPlan;
    }

    public List<String> getGroupMembersLabels() {
        List<String> result = new ArrayList<String>();

        Group curricularPlanMembersGroup = getDegreeCurricularPlan().getCurricularPlanMembersGroup();
        if (curricularPlanMembersGroup != null) {
            curricularPlanMembersGroup.getMembers()
                    .forEach(user -> result.add(user.getPerson().getName() + " (" + user.getUsername() + ")"));
        }

        return result;
    }

    public CourseGroup getCourseGroup() {
        String cg = getCourseGroupID();
        if (Strings.isNullOrEmpty(cg) || cg.equals("-1")) {
            return null;
        }
        return (CourseGroup) FenixFramework.getDomainObject(cg);
    }

    public Unit getDepartmentUnit() {
        if (getDepartmentUnitID() != null && !getDepartmentUnitID().equals(NO_SELECTION_STRING)) {
            return FenixFramework.getDomainObject(getDepartmentUnitID());
        }
        return null;
    }

    public CompetenceCourse getCompetenceCourse() {
        if (competenceCourse == null && getCompetenceCourseID() != null && !getCompetenceCourseID().equals(NO_SELECTION_STRING)) {
            competenceCourse = FenixFramework.getDomainObject(getCompetenceCourseID());
        }
        return competenceCourse;
    }

    public CurricularCourse getCurricularCourse() {
        return (curricularCourse == null && getCurricularCourseID() != null) ? (curricularCourse =
                (CurricularCourse) FenixFramework.getDomainObject(getCurricularCourseID())) : curricularCourse;
    }

    protected Context getContext(String contextID) {
        return (context == null && contextID != null) ? (context = FenixFramework.getDomainObject(contextID)) : context;
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
                setSelectedCurricularCourseType(
                        getCurricularCourse().isOptionalCurricularCourse() ? CurricularCourseType.OPTIONAL_COURSE
                                .name() : CurricularCourseType.NORMAL_COURSE.name());
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
            toDelete = getCurricularCourse().getParentContextsSet().size() == 1; // Last context?
        }
        return toDelete;
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
            return FenixFramework.getDomainObject(executionYearId);
        }

        ExecutionYear currentExecutionYear = ExecutionYear.findCurrent(getDegree().getCalendar());

        if (oldestContextExecutionYear != null && oldestContextExecutionYear.isAfter(currentExecutionYear)) {
            return oldestContextExecutionYear;
        }

        return currentExecutionYear;
    }

    public List<SelectItem> getExecutionYearItems() {
        return (executionYearItems == null) ? (executionYearItems = readExecutionYearItems()) : executionYearItems;
    }

    public String getBeginExecutionPeriodID() {
        if (getViewState().getAttribute("beginExecutionPeriodID") == null && getContext(getContextID()) != null) {
            setBeginExecutionPeriodID(getContext(getContextID()).getBeginExecutionInterval().getExternalId());
        }
        return (String) getViewState().getAttribute("beginExecutionPeriodID");
    }

    public void setBeginExecutionPeriodID(String beginExecutionPeriodID) {
        getViewState().setAttribute("beginExecutionPeriodID", beginExecutionPeriodID);
    }

    public String getEndExecutionPeriodID() {
        if (getViewState().getAttribute("endExecutionPeriodID") == null && getContext(getContextID()) != null) {
            setEndExecutionPeriodID((getContext(getContextID()).getEndExecutionInterval() != null) ? getContext(getContextID())
                    .getEndExecutionInterval().getExternalId() : NO_SELECTION_STRING);
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
        result.add(0, new SelectItem(NO_SELECTION_STRING, BundleUtil.getString(Bundle.BOLONHA, "opened")));
        return result;
    }

    protected List<SelectItem> readExecutionPeriodItems() {

        final ExecutionInterval minimumExecutionPeriod = getMinimumExecutionPeriod();
        final List<ExecutionInterval> notClosedExecutionPeriods =
                new ArrayList<>(ExecutionInterval.findActiveAggregators(getDegree().getCalendar()));
        Collections.sort(notClosedExecutionPeriods);

        final List<SelectItem> result = new ArrayList<SelectItem>();
        for (final ExecutionInterval notClosedExecutionPeriod : notClosedExecutionPeriods) {
            if (minimumExecutionPeriod == null || notClosedExecutionPeriod.isAfterOrEquals(minimumExecutionPeriod)) {
                result.add(new SelectItem(notClosedExecutionPeriod.getExternalId(), notClosedExecutionPeriod.getQualifiedName()));
            }
        }
        return result;
    }

    protected ExecutionInterval getMinimumExecutionPeriod() {
        return (getCourseGroup() == null) ? null : getCourseGroup().getMinimumExecutionPeriod();
    }

    public List<Context> getCurricularCourseParentContexts() {
        List<Context> contexts = Lists.newArrayList(getCurricularCourse().getParentContextsSet());
        Collections.sort(contexts);
        return contexts;
    }

    public String createCurricularCourse() {
        try {
            checkCourseGroup();
            checkCurricularSemesterAndYear();
            runCreateCurricularCourse();

        } catch (FenixActionException e) {
            this.addErrorMessage(BundleUtil.getString(Bundle.BOLONHA, e.getMessage()));
            return "";
        } catch (FenixServiceException e) {
            this.addErrorMessage(BundleUtil.getString(Bundle.BOLONHA, e.getMessage()));
            return "";
        } catch (DomainException e) {
            addErrorMessage(BundleUtil.getString(Bundle.DOMAIN_EXCEPTION, e.getMessage()));
            return "";
        } catch (Exception e) {
            this.addErrorMessage(BundleUtil.getString(Bundle.BOLONHA, "general.error"));
            return "buildCurricularPlan";
        }
        addInfoMessage(BundleUtil.getString(Bundle.BOLONHA, "curricularCourseCreated"));
        return "buildCurricularPlan";
    }

    private void runCreateCurricularCourse() throws FenixActionException, FenixServiceException {
        final CurricularCourseType curricularCourseType = CurricularCourseType.valueOf(getSelectedCurricularCourseType());
        if (curricularCourseType.equals(CurricularCourseType.NORMAL_COURSE)) {

            checkCompetenceCourse();
            CreateCurricularCourse.run(new CreateCurricularCourse.CreateCurricularCourseArgs(getWeight(), getCompetenceCourseID(),
                    getCourseGroupID(), getCurricularYearID(), getCurricularSemesterID(), getDegreeCurricularPlanID(),
                    getBeginExecutionPeriodID(), getFinalEndExecutionPeriodID()));

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
            addInfoMessage(BundleUtil.getString(Bundle.BOLONHA, "curricularCourseEdited"));
        } catch (FenixServiceException e) {
            addErrorMessage(BundleUtil.getString(Bundle.BOLONHA, e.getMessage()));
        } catch (FenixActionException e) {
            addErrorMessage(BundleUtil.getString(Bundle.BOLONHA, e.getMessage()));
        }
        setContextID(null); // resetContextID
        return "";
    }

    private void runEditCurricularCourse() throws FenixActionException, FenixServiceException {
        final CurricularCourseType curricularCourseType = CurricularCourseType.valueOf(getSelectedCurricularCourseType());
        if (curricularCourseType.equals(CurricularCourseType.NORMAL_COURSE)) {
            checkCompetenceCourse();
            EditCurricularCourse.run(getCurricularCourse(), getWeight(), getCompetenceCourse());
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

    protected void checkCurricularCourse() throws FenixActionException {
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
        if (getCurricularSemesterID() == null || getCurricularSemesterID().equals(this.NO_SELECTION_INTEGER)) {
            throw new FenixActionException("error.mustChooseACurricularSemester");
        }
        if (getCurricularYearID() == null || getCurricularYearID().equals(this.NO_SELECTION_INTEGER)) {
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
            addInfoMessage(BundleUtil.getString(Bundle.BOLONHA, "addedNewContextToCurricularCourse"));
        } catch (FenixActionException e) {
            this.addErrorMessage(BundleUtil.getString(Bundle.BOLONHA, e.getMessage()));
            return "";
        } catch (FenixServiceException e) {
            this.addErrorMessage(BundleUtil.getString(Bundle.BOLONHA, e.getMessage()));
            return "";
        } catch (DomainException e) {
            addErrorMessage(BundleUtil.getString(Bundle.BOLONHA, e.getMessage(), e.getArgs()));
            return "";
        } catch (Exception e) {
            this.addErrorMessage(BundleUtil.getString(Bundle.BOLONHA, "general.error"));
            return "buildCurricularPlan";
        }
        setContextID(null); // resetContextID
        return "buildCurricularPlan";
    }

    public String editContext() {
        try {
            checkCourseGroup();
            EditContextFromCurricularCourse.run(getCurricularCourse(), getContext(getContextID()), getCourseGroup(),
                    getCurricularYearID(), getCurricularSemesterID(), getBeginExecutionPeriodID(),
                    getFinalEndExecutionPeriodID());
            setContextID(""); //XXX not using null prevents getter calls to restore the value though getAndHoldRequestParameter
        } catch (IllegalDataAccessException e) {
            addErrorMessage(BundleUtil.getString(Bundle.BOLONHA, "error.notAuthorized"));
        } catch (DomainException e) {
            addErrorMessage(BundleUtil.getString(Bundle.DOMAIN_EXCEPTION, e.getMessage()));
        } catch (FenixActionException e) {
            addErrorMessage(BundleUtil.getString(Bundle.BOLONHA, e.getMessage()));
        }
        return "";
    }

    protected String getFinalEndExecutionPeriodID() {
        return (getViewState().getAttribute("endExecutionPeriodID") == null || getViewState().getAttribute("endExecutionPeriodID")
                .equals(NO_SELECTION_STRING)) ? null : (String) getViewState().getAttribute("endExecutionPeriodID");
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
            addInfoMessage(BundleUtil.getString(Bundle.BOLONHA, "successAction"));
        } catch (IllegalDataAccessException e) {
            this.addErrorMessage(BundleUtil.getString(Bundle.BOLONHA, "error.notAuthorized"));
        } catch (FenixServiceException e) {
            addErrorMessage(e.getMessage());
        } catch (DomainException e) {
            addErrorMessage(BundleUtil.getString(Bundle.DOMAIN_EXCEPTION, e.getKey(), e.getArgs()));
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
        for (final Object departmentObject : Bennu.getInstance().getDepartmentsSet()) {
            Unit departmentUnit = ((Department) departmentObject).getDepartmentUnit();
            result.add(new SelectItem(departmentUnit.getExternalId(), departmentUnit.getName()));
        }
        Collections.sort(result, new BeanComparator("label"));
        result.add(0, new SelectItem(this.NO_SELECTION_STRING, BundleUtil.getString(Bundle.BOLONHA, "choose")));
        return result;
    }

    private List<SelectItem> readAllowedDepartmentUnits() {
        final List<SelectItem> result = new ArrayList<SelectItem>();
        for (final Department department : Bennu.getInstance().getDepartmentsSet()) {
            if (department.getCompetenceCourseMembersGroup() != null
                    && department.getCompetenceCourseMembersGroup().isMember(getUserView())) {
                Unit departmentUnit = department.getDepartmentUnit();
                result.add(new SelectItem(departmentUnit.getExternalId(), departmentUnit.getName()));
            }
        }
        Collections.sort(result, new BeanComparator("label"));
        if (result.size() == 1) {
            Unit personDepartment = getPersonDepartment();
            if (personDepartment != null && !result.get(0).getValue().equals(personDepartment.getExternalId())) {
                result.add(0, new SelectItem(personDepartment.getExternalId(), personDepartment.getName()));
            }
        }
        return result;
    }

    private Unit getPersonDepartment() {
        final User userView = getUserView();
        final Person person = userView == null ? null : userView.getPerson();
        final Teacher teacher = person == null ? null : person.getTeacher();
        return teacher == null ? null : teacher.getUnit().orElse(null);
    }

    private List<SelectItem> readCompetenceCourses() {
        final List<SelectItem> result = new ArrayList<SelectItem>();
        final Unit departmentUnit = getDepartmentUnit();
        if (departmentUnit != null) {
            CompetenceCourse.findByUnit(departmentUnit, true).filter(cc -> cc.getCurricularStage() != CurricularStage.DRAFT)
                    .forEach(competenceCourse -> {
                        final String code =
                                !StringUtils.isEmpty(competenceCourse.getCode()) ? " [" + competenceCourse.getCode() + "]" : "";
                        result.add(new SelectItem(competenceCourse.getExternalId(), competenceCourse.getName() + " ("
                                + BundleUtil.getString(Bundle.ENUMERATION, competenceCourse.getCurricularStage().getName()) + ")"
                                + code));
                    });
            Collections.sort(result, new BeanComparator("label"));
        }
        result.add(0, new SelectItem(this.NO_SELECTION_STRING, BundleUtil.getString(Bundle.BOLONHA, "choose")));
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
        result.add(0, new SelectItem(this.NO_SELECTION_STRING, BundleUtil.getString(Bundle.BOLONHA, "choose")));
        return result;
    }

    protected List<SelectItem> readExecutionYearItems() {
        final List<SelectItem> result = new ArrayList<SelectItem>();

        final Collection<ExecutionDegree> executionDegrees = getDegreeCurricularPlan().getExecutionDegreesSet();

        if (executionDegrees.isEmpty()) {
            final ExecutionYear executionYear =
                    getDegreeCurricularPlan().getRoot().getMinimumExecutionPeriod().getExecutionYear();
            result.add(new SelectItem(executionYear.getExternalId(), executionYear.getYear()));
            return result;
        }

        executionDegrees.stream().map(ed -> ed.getExecutionYear()).sorted(Comparator.reverseOrder()).limit(4)
                .forEach(ey -> result.add(new SelectItem(ey.getExternalId(), ey.getYear())));

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

    public ExecutionInterval getBeginExecutionPeriod() {
        return FenixFramework.getDomainObject(getBeginExecutionPeriodID());
    }

    public ExecutionInterval getEndExecutionPeriod() {
        return getEndExecutionPeriodID() == null ? null : FenixFramework
                .<ExecutionInterval> getDomainObject(getEndExecutionPeriodID());
    }

}
