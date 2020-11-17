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
 * Created on Feb 3, 2006
 */
package org.fenixedu.academic.ui.faces.bean.bolonhaManager.curricularRules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.faces.component.UISelectItems;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.curricularRules.AnyCurricularCourse;
import org.fenixedu.academic.domain.curricularRules.CreditsLimit;
import org.fenixedu.academic.domain.curricularRules.CurricularRule;
import org.fenixedu.academic.domain.curricularRules.CurricularRuleType;
import org.fenixedu.academic.domain.curricularRules.DegreeModulesSelectionLimit;
import org.fenixedu.academic.domain.curricularRules.Exclusiveness;
import org.fenixedu.academic.domain.curricularRules.MinimumNumberOfCreditsToEnrol;
import org.fenixedu.academic.domain.curricularRules.PrecedenceRule;
import org.fenixedu.academic.domain.curricularRules.RestrictionBetweenDegreeModules;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.degreeStructure.Context;
import org.fenixedu.academic.domain.degreeStructure.CourseGroup;
import org.fenixedu.academic.domain.degreeStructure.DegreeModule;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.organizationalStructure.PartyTypeEnum;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.domain.organizationalStructure.UnitUtils;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicCalendarRootEntry;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicPeriod;
import org.fenixedu.academic.dto.CurricularPeriodInfoDTO;
import org.fenixedu.academic.dto.bolonhaManager.CurricularRuleParametersDTO;
import org.fenixedu.academic.service.services.bolonhaManager.CreateRule;
import org.fenixedu.academic.service.services.bolonhaManager.DeleteCurricularRule;
import org.fenixedu.academic.service.services.bolonhaManager.EditCurricularRule;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;
import org.fenixedu.academic.ui.faces.bean.base.FenixBackingBean;
import org.fenixedu.academic.ui.struts.action.exceptions.FenixActionException;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.CurricularRuleLabelFormatter;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import pt.ist.fenixframework.FenixFramework;

public class CurricularRulesManagementBackingBean extends FenixBackingBean {
    static protected final String NO_SELECTION_STRING = "-1";

    private String degreeModuleID = null;
    private DegreeModule degreeModule = null;

    private DegreeCurricularPlan degreeCurricularPlan = null;
    private CurricularRule curricularRule = null;

    private UISelectItems curricularRuleTypeItems;
    private UISelectItems degreeModuleItems;
    private UISelectItems courseGroupItems;
    private UISelectItems degreeTypeItems;
    private UISelectItems degreeItems;
    private UISelectItems departmentUnitItems;
    private UISelectItems beginExecutionPeriodItemsForRule;
    private UISelectItems endExecutionPeriodItemsForRule;
    private UISelectItems optionalsConfigurationItems;

    public String getDegreeCurricularPlanID() {
        return getAndHoldStringParameter("degreeCurricularPlanID");
    }

    public String getExecutionYearID() {
        return getAndHoldStringParameter("executionYearID");
    }

    public String getOrganizeBy() {
        return getAndHoldStringParameter("organizeBy");
    }

    public String getShowRules() {
        return getAndHoldStringParameter("showRules");
    }

    public String getHideCourses() {
        return getAndHoldStringParameter("hideCourses");
    }

    public String getAction() {
        return getAndHoldStringParameter("action");
    }

    public String getSchoolAcronym() {
        return Unit.getInstitutionAcronym();
    }

    public String getType() {
        return getAndHoldStringParameter("type");
    }

    public String getDegreeModuleID() {
        if (degreeModuleID == null) {
            degreeModuleID = getAndHoldStringParameter("degreeModuleID");
            if (degreeModuleID == null && getCurricularRule() != null) {
                degreeModuleID = getCurricularRule().getDegreeModuleToApplyRule().getExternalId();
            }
        }
        return degreeModuleID;
    }

    public DegreeModule getDegreeModule() {
        return (degreeModule == null) ? (degreeModule = FenixFramework.getDomainObject(getDegreeModuleID())) : degreeModule;
    }

    public DegreeCurricularPlan getDegreeCurricularPlan() {
        return (degreeCurricularPlan == null) ? (degreeCurricularPlan =
                FenixFramework.getDomainObject(getDegreeCurricularPlanID())) : degreeCurricularPlan;
    }

    public String getSelectedCurricularRuleType() {
        if (getViewState().getAttribute("selectedCurricularRuleType") == null && getCurricularRule() != null) {
            if (getCurricularRule().getCurricularRuleType() != null) {
                setSelectedCurricularRuleType(getCurricularRule().getCurricularRuleType().getName());
            }
        }
        return (String) getViewState().getAttribute("selectedCurricularRuleType");
    }

    public void setSelectedCurricularRuleType(String selectedCurricularRuleType) {
        getViewState().setAttribute("selectedCurricularRuleType", selectedCurricularRuleType);
    }

    private List<SelectItem> getRuleTypes() {

        final Set<CurricularRuleType> curricularRuleTypes = new HashSet<CurricularRuleType>();

        for (final CurricularRuleType curricularRuleType : CurricularRuleType.values()) {
            switch (curricularRuleType) {

            case CREDITS_LIMIT:
                if (getDegreeModule().isLeaf()) {
                    final CurricularCourse curricularCourse = (CurricularCourse) getDegreeModule();
                    if (curricularCourse.isOptionalCurricularCourse()) {
                        curricularRuleTypes.add(curricularRuleType);
                    }
                } else {
                    curricularRuleTypes.add(curricularRuleType);
                }
                break;

            case DEGREE_MODULES_SELECTION_LIMIT:
                if (!getDegreeModule().isLeaf()) {
                    curricularRuleTypes.add(curricularRuleType);
                }
                break;

            case PRECEDENCY_BETWEEN_DEGREE_MODULES:
                curricularRuleTypes.add(curricularRuleType);
                break;

            case ANY_CURRICULAR_COURSE:
            case ANY_CURRICULAR_COURSE_EXCEPTIONS:
                if (getDegreeModule().isLeaf()) {
                    final CurricularCourse curricularCourse = (CurricularCourse) getDegreeModule();
                    if (curricularCourse.isOptionalCurricularCourse()) {
                        curricularRuleTypes.add(curricularRuleType);
                    }
                }
                break;

            case PRECEDENCY_APPROVED_DEGREE_MODULE:
            case PRECEDENCY_ENROLED_DEGREE_MODULE:
            case RESTRICTION_NOT_ENROLED_DEGREE_MODULE:
            case EVEN_ODD:
                if (getDegreeModule().isLeaf()) {
                    curricularRuleTypes.add(curricularRuleType);
                }
                break;

            case EXCLUSIVENESS:
            case MINIMUM_NUMBER_OF_CREDITS_TO_ENROL:
                curricularRuleTypes.add(curricularRuleType);
                break;

            case ENROLMENT_TO_BE_APPROVED_BY_COORDINATOR:
                if (!getDegreeModule().isRoot()) {
                    curricularRuleTypes.add(curricularRuleType);
                }
                break;

            default:
                break;
            }
        }

        final List<SelectItem> result = new ArrayList<SelectItem>();
        for (final CurricularRuleType iter : curricularRuleTypes) {
            addSelectItemCurricularRuleType(result, iter);
        }
        Collections.sort(result, new BeanComparator("label"));

        result.add(0, new SelectItem(NO_SELECTION_STRING, BundleUtil.getString(Bundle.BOLONHA, "choose")));
        return result;
    }

    static private void addSelectItemCurricularRuleType(final List<SelectItem> result,
            final CurricularRuleType curricularRuleType) {
        result.add(new SelectItem(curricularRuleType.getName(),
                BundleUtil.getString(Bundle.ENUMERATION, curricularRuleType.getName())));
    }

    public List<String> getRulesLabels() {
        final List<String> resultLabels = new ArrayList<String>();
        for (CurricularRule curricularRule : getDegreeModule().getCurricularRulesSet()) {
            resultLabels.add(CurricularRuleLabelFormatter.getLabel(curricularRule));
        }
        return resultLabels;
    }

    public String getRuleLabel() {
        return CurricularRuleLabelFormatter.getLabel(getCurricularRule());
    }

    public UISelectItems getCurricularRuleTypeItems() {
        if (curricularRuleTypeItems == null) {
            curricularRuleTypeItems = new UISelectItems();
            curricularRuleTypeItems.setValue(getRuleTypes());
        }
        return curricularRuleTypeItems;
    }

    public void setCurricularRuleTypeItems(UISelectItems ruleTypeItems) {
        this.curricularRuleTypeItems = ruleTypeItems;
    }

    public void onChangeCurricularRuleTypeDropDown(ValueChangeEvent event) {
        getDegreeModuleItems().setValue(readDegreeModules((String) event.getNewValue()));
        getCourseGroupItems().setValue(readParentCourseGroups((String) event.getNewValue()));
        getDegreeItems().setValue(readBolonhaDegrees((String) event.getNewValue(), getSelectedDegreeType()));
        getDepartmentUnitItems().setValue(readDepartmentUnits((String) event.getNewValue()));
    }

    public void onChangeDegreeTypeDropDown(ValueChangeEvent event) {
        getDegreeItems().setValue(readBolonhaDegrees(getSelectedCurricularRuleType(), (String) event.getNewValue()));
    }

    public String getSelectedDegreeModuleID() {
        if (getViewState().getAttribute("selectedDegreeModuleID") == null && getCurricularRule() != null) {
            if (getCurricularRule() instanceof PrecedenceRule) {
                setSelectedDegreeModuleID(((PrecedenceRule) getCurricularRule()).getPrecedenceDegreeModule().getExternalId());
            } else if (getCurricularRule() instanceof Exclusiveness) {
                setSelectedDegreeModuleID(((Exclusiveness) getCurricularRule()).getExclusiveDegreeModule().getExternalId());
            }
        }
        return (String) getViewState().getAttribute("selectedDegreeModuleID");
    }

    public void setSelectedDegreeModuleID(String selectedDegreeModuleID) {
        getViewState().setAttribute("selectedDegreeModuleID", selectedDegreeModuleID);
    }

    public String getSelectedContextCourseGroupID() {
        if (getViewState().getAttribute("selectedContextCourseGroupID") == null && getCurricularRule() != null) {
            if (getCurricularRule().getContextCourseGroup() != null) {
                setSelectedContextCourseGroupID(getCurricularRule().getContextCourseGroup().getExternalId());
            }
        }
        return (String) getViewState().getAttribute("selectedContextCourseGroupID");
    }

    public void setSelectedContextCourseGroupID(String selectedContextCourseGroupID) {
        getViewState().setAttribute("selectedContextCourseGroupID", selectedContextCourseGroupID);
    }

    public Integer getMinimumLimit() {
        if (getViewState().getAttribute("minimumLimit") == null) {
            if (getCurricularRule() != null && getCurricularRule() instanceof DegreeModulesSelectionLimit) {
                setMinimumLimit(((DegreeModulesSelectionLimit) getCurricularRule()).getMinimumLimit());
            } else {
                setMinimumLimit(Integer.valueOf(0));
            }
        }
        return (Integer) getViewState().getAttribute("minimumLimit");
    }

    public void setMinimumLimit(Integer minimumLimit) {
        getViewState().setAttribute("minimumLimit", minimumLimit);
    }

    public Integer getMaximumLimit() {
        if (getViewState().getAttribute("maximumLimit") == null) {
            if (getCurricularRule() != null && getCurricularRule() instanceof DegreeModulesSelectionLimit) {
                setMaximumLimit(((DegreeModulesSelectionLimit) getCurricularRule()).getMaximumLimit());
            } else {
                setMaximumLimit(Integer.valueOf(0));
            }
        }
        return (Integer) getViewState().getAttribute("maximumLimit");
    }

    public void setMaximumLimit(Integer maximumLimit) {
        getViewState().setAttribute("maximumLimit", maximumLimit);
    }

    public Double getMinimumCredits() {
        if (getViewState().getAttribute("minimumCredits") == null) {
            if (getCurricularRule() != null && getCurricularRule() instanceof CreditsLimit) {
                setMinimumCredits(((CreditsLimit) getCurricularRule()).getMinimumCredits());
            } else if (getCurricularRule() != null && getCurricularRule() instanceof RestrictionBetweenDegreeModules) {
                setMinimumCredits(((RestrictionBetweenDegreeModules) getCurricularRule()).getMinimumCredits());
            } else if (getCurricularRule() != null && getCurricularRule() instanceof MinimumNumberOfCreditsToEnrol) {
                setMinimumCredits(((MinimumNumberOfCreditsToEnrol) getCurricularRule()).getMinimumCredits());
            } else {
                setMinimumCredits(Double.valueOf(0));
            }
        }
        return (Double) getViewState().getAttribute("minimumCredits");
    }

    public void setMinimumCredits(Double minimumCredits) {
        getViewState().setAttribute("minimumCredits", minimumCredits);
    }

    public Double getMaximumCredits() {
        if (getViewState().getAttribute("maximumCredits") == null) {
            if (getCurricularRule() != null && getCurricularRule() instanceof CreditsLimit) {
                setMaximumCredits(((CreditsLimit) getCurricularRule()).getMaximumCredits());
            } else {
                setMaximumCredits(Double.valueOf(0));
            }
        }
        return (Double) getViewState().getAttribute("maximumCredits");
    }

    public void setMaximumCredits(Double maximumCredits) {
        getViewState().setAttribute("maximumCredits", maximumCredits);
    }

    public String getSelectedSemester() {
        if (getViewState().getAttribute("selectedSemester") == null) {
            if (getCurricularRule() != null && getCurricularRule() instanceof PrecedenceRule) {
                final PrecedenceRule precedenceRule = (PrecedenceRule) getCurricularRule();
                setSelectedSemester(precedenceRule.getCurricularPeriodOrder() != null ? precedenceRule.getCurricularPeriodOrder()
                        .toString() : "0");
            } else if (getCurricularRule() != null && getCurricularRule() instanceof AnyCurricularCourse) {
                setSelectedSemester(((AnyCurricularCourse) getCurricularRule()).getCurricularPeriodOrder().toString());
            } else {
                setSelectedSemester("0");
            }
        }
        return (String) getViewState().getAttribute("selectedSemester");
    }

    public void setSelectedSemester(String selectedSemester) {
        getViewState().setAttribute("selectedSemester", selectedSemester);
    }

    public void setSelectedEven(String selectedSemester) {
        getViewState().setAttribute("even", selectedSemester);
    }

    public String getSelectedEven() {
        return (String) getViewState().getAttribute("even");
    }

    public UISelectItems getOptionalsConfigurationItems() {
        if (optionalsConfigurationItems == null) {

            final List<SelectItem> values = new ArrayList<SelectItem>(3);
            values.add(0, new SelectItem(NO_SELECTION_STRING, BundleUtil.getString(Bundle.BOLONHA, "all")));
            values.add(1, new SelectItem(Boolean.TRUE.toString(), BundleUtil.getString(Bundle.BOLONHA, "optional")));
            values.add(2, new SelectItem(Boolean.FALSE.toString(), BundleUtil.getString(Bundle.BOLONHA, "mandatory")));

            optionalsConfigurationItems = new UISelectItems();
            optionalsConfigurationItems.setValue(values);
        }

        return optionalsConfigurationItems;
    }

    public void setOptionalsConfigurationItems(final UISelectItems input) {
        this.optionalsConfigurationItems = input;
    }

    public void setSelectedOptionalsConfiguration(final String input) {
        getViewState().setAttribute("optionalsConfiguration", input);
    }

    public String getSelectedOptionalsConfiguration() {
        return (String) getViewState().getAttribute("optionalsConfiguration");
    }

    public String getCurricularRuleID() {
        return getAndHoldStringParameter("curricularRuleID");
    }

    public CurricularRule getCurricularRule() {
        return (curricularRule == null) ? (curricularRule =
                FenixFramework.getDomainObject(getCurricularRuleID())) : curricularRule;
    }

    public UISelectItems getDegreeModuleItems() {
        if (degreeModuleItems == null) {
            degreeModuleItems = new UISelectItems();
            degreeModuleItems.setValue(readDegreeModules(getSelectedCurricularRuleType()));
        }
        return degreeModuleItems;
    }

    public void setDegreeModuleItems(UISelectItems degreeModuleItems) {
        this.degreeModuleItems = degreeModuleItems;
    }

    public UISelectItems getCourseGroupItems() {
        if (courseGroupItems == null) {
            courseGroupItems = new UISelectItems();
            courseGroupItems.setValue(readParentCourseGroups(getSelectedCurricularRuleType()));
        }
        return courseGroupItems;
    }

    public void setCourseGroupItems(UISelectItems courseGroupItems) {
        this.courseGroupItems = courseGroupItems;
    }

    public Double getCredits() {
        if (getViewState().getAttribute("credits") == null) {
            if (getCurricularRule() != null && getCurricularRule() instanceof AnyCurricularCourse) {
                AnyCurricularCourse anyCurricularCourse = (AnyCurricularCourse) getCurricularRule();
                if (anyCurricularCourse.getCredits() != null) {
                    setCredits(anyCurricularCourse.getCredits());
                }
            }
        }
        return (Double) getViewState().getAttribute("credits");
    }

    public void setCredits(Double credits) {
        if (credits == null) {
            getViewState().removeAttribute("credits");
        } else {
            getViewState().setAttribute("credits", credits);
        }
    }

    static private DegreeType getDegreeType(final String input) {
        return StringUtils.isBlank(input) || input.equals(NO_SELECTION_STRING) ? null : FenixFramework
                .<DegreeType> getDomainObject(input);
    }

    public String getSelectedDegreeType() {
        if (getViewState().getAttribute("selectedDegreeType") == null) {
            if (getCurricularRule() != null && getCurricularRule() instanceof AnyCurricularCourse) {
                AnyCurricularCourse anyCurricularCourse = (AnyCurricularCourse) getCurricularRule();
                if (anyCurricularCourse.getBolonhaDegreeType() != null) {
                    setSelectedDegreeType(anyCurricularCourse.getBolonhaDegreeType().getExternalId());
                }
            }
        }
        return (String) getViewState().getAttribute("selectedDegreeType");
    }

    public void setSelectedDegreeType(String selectedDegreeType) {
        getViewState().setAttribute("selectedDegreeType", selectedDegreeType);
    }

    public String getSelectedDegreeID() {
        if (getViewState().getAttribute("selectedDegreeID") == null) {
            if (getCurricularRule() != null && getCurricularRule() instanceof AnyCurricularCourse) {
                AnyCurricularCourse anyCurricularCourse = (AnyCurricularCourse) getCurricularRule();
                setSelectedDegreeID(anyCurricularCourse.getDegree() != null ? anyCurricularCourse.getDegree()
                        .getExternalId() : NO_SELECTION_STRING);
            } else {
                setSelectedDegreeID(NO_SELECTION_STRING);
            }
        }
        return (String) getViewState().getAttribute("selectedDegreeID");
    }

    public void setSelectedDegreeID(String selectedDegreeID) {
        getViewState().setAttribute("selectedDegreeID", selectedDegreeID);
    }

    public String getSelectedDepartmentUnitID() {
        if (getViewState().getAttribute("selectedDepartmentUnitID") == null) {
            if (getCurricularRule() != null && getCurricularRule() instanceof AnyCurricularCourse) {
                AnyCurricularCourse anyCurricularCourse = (AnyCurricularCourse) getCurricularRule();
                setSelectedDepartmentUnitID(anyCurricularCourse.getDepartmentUnit() != null ? anyCurricularCourse
                        .getDepartmentUnit().getExternalId() : NO_SELECTION_STRING);
            } else {
                setSelectedDepartmentUnitID(NO_SELECTION_STRING);
            }
        }
        return (String) getViewState().getAttribute("selectedDepartmentUnitID");
    }

    public void setSelectedDepartmentUnitID(String selectedDepartmentUnitID) {
        getViewState().setAttribute("selectedDepartmentUnitID", selectedDepartmentUnitID);
    }

    public UISelectItems getDegreeTypeItems() {
        if (degreeTypeItems == null) {
            degreeTypeItems = new UISelectItems();

            final List<SelectItem> value = new ArrayList<SelectItem>();
            value.add(new SelectItem(NO_SELECTION_STRING, getSchoolAcronym()));
            for (Iterator<DegreeType> iterator = DegreeType.all().sorted().iterator(); iterator.hasNext();) {
                final DegreeType degreeType = iterator.next();
                value.add(new SelectItem(degreeType.getExternalId(), degreeType.getName().getContent()));
            }

            degreeTypeItems.setValue(value);
        }

        return degreeTypeItems;
    }

    public void setDegreeTypeItems(final UISelectItems input) {
        this.degreeTypeItems = input;
    }

    public UISelectItems getDegreeItems() {
        if (degreeItems == null) {
            degreeItems = new UISelectItems();
            degreeItems.setValue(readBolonhaDegrees(getSelectedCurricularRuleType(), getSelectedDegreeType()));
        }
        return degreeItems;
    }

    public void setDegreeItems(UISelectItems degreeItems) {
        this.degreeItems = degreeItems;
    }

    private List<SelectItem> readBolonhaDegrees(String selectedCurricularRuleType, String selectedDegreeType) {

        final List<SelectItem> result = new ArrayList<SelectItem>();
        if (selectedCurricularRuleType != null
                && selectedCurricularRuleType.equals(CurricularRuleType.ANY_CURRICULAR_COURSE.name())) {

            final List<Degree> allDegrees = Degree.readNotEmptyDegrees();
            final DegreeType bolonhaDegreeType = getDegreeType(selectedDegreeType);
            for (final Degree degree : allDegrees) {
                if (bolonhaDegreeType == null || degree.getDegreeType() == bolonhaDegreeType) {
                    result.add(new SelectItem(degree.getExternalId(),
                            "[" + degree.getDegreeType().getName().getContent() + "] " + degree.getNome()));
                }
            }
            Collections.sort(result, new BeanComparator("label"));
        }
        result.add(0, new SelectItem(NO_SELECTION_STRING, BundleUtil.getString(Bundle.BOLONHA, "any.one")));
        return result;
    }

    public UISelectItems getDepartmentUnitItems() {
        if (departmentUnitItems == null) {
            departmentUnitItems = new UISelectItems();
            departmentUnitItems.setValue(readDepartmentUnits(getSelectedCurricularRuleType()));
        }
        return departmentUnitItems;
    }

    public void setDepartmentUnitItems(UISelectItems departmentUnitItems) {
        this.departmentUnitItems = departmentUnitItems;
    }

    public String getBeginExecutionPeriodID() {
        if (getViewState().getAttribute("beginExecutionPeriodID") == null && getCurricularRule() != null) {
            setBeginExecutionPeriodID(getCurricularRule().getBeginInterval().getExternalId());
        }
        return (String) getViewState().getAttribute("beginExecutionPeriodID");
    }

    public void setBeginExecutionPeriodID(String beginExecutionPeriodID) {
        getViewState().setAttribute("beginExecutionPeriodID", beginExecutionPeriodID);
    }

    public String getEndExecutionPeriodID() {
        if (getViewState().getAttribute("endExecutionPeriodID") == null && getCurricularRule() != null) {
            setEndExecutionPeriodID((getCurricularRule().getEndInterval() == null) ? NO_SELECTION_STRING : getCurricularRule()
                    .getEndInterval().getExternalId());
        }
        return (String) getViewState().getAttribute("endExecutionPeriodID");
    }

    public void setEndExecutionPeriodID(String endExecutionPeriodID) {
        getViewState().setAttribute("endExecutionPeriodID", endExecutionPeriodID);
    }

    public UISelectItems getBeginExecutionPeriodItemsForRule() {
        if (beginExecutionPeriodItemsForRule == null) {
            beginExecutionPeriodItemsForRule = new UISelectItems();
            beginExecutionPeriodItemsForRule.setValue(readExecutionPeriodItems());
        }
        return beginExecutionPeriodItemsForRule;
    }

    public void setBeginExecutionPeriodItemsForRule(UISelectItems beginExecutionPeriodItemsForRule) {
        this.beginExecutionPeriodItemsForRule = beginExecutionPeriodItemsForRule;
    }

    public UISelectItems getEndExecutionPeriodItemsForRule() {
        if (endExecutionPeriodItemsForRule == null) {
            endExecutionPeriodItemsForRule = new UISelectItems();
            final List<SelectItem> values = new ArrayList<SelectItem>(readExecutionPeriodItems());
            values.add(0, new SelectItem(NO_SELECTION_STRING, BundleUtil.getString(Bundle.BOLONHA, "opened")));
            endExecutionPeriodItemsForRule.setValue(values);
        }
        return endExecutionPeriodItemsForRule;
    }

    public void setEndExecutionPeriodItemsForRule(UISelectItems endExecutionPeriodItemsForRule) {
        this.endExecutionPeriodItemsForRule = endExecutionPeriodItemsForRule;
    }

    private List<SelectItem> executionPeriodItems;

    protected List<SelectItem> readExecutionPeriodItems() {
        if (executionPeriodItems != null) {
            return executionPeriodItems;
        }
        final List<SelectItem> result = new ArrayList<SelectItem>();

        final AcademicCalendarRootEntry calendar = getDegreeCurricularPlan().getDegree().getCalendar();

        final DegreeModule degreeModule = getDegreeModule();
        final ExecutionInterval currentExecutionPeriod =
                degreeModule != null ? degreeModule.getMinimumExecutionPeriod() : ExecutionInterval
                        .findActiveAggregators(calendar).stream().min(Comparator.naturalOrder()).orElse(null);

        final List<ExecutionInterval> notClosedExecutionPeriods =
                new ArrayList<>(ExecutionInterval.findActiveAggregators(calendar));
        Collections.sort(notClosedExecutionPeriods);

        for (final ExecutionInterval notClosedExecutionPeriod : notClosedExecutionPeriods) {
            if (notClosedExecutionPeriod.isAfterOrEquals(currentExecutionPeriod)) {
                result.add(new SelectItem(notClosedExecutionPeriod.getExternalId(), notClosedExecutionPeriod.getQualifiedName()));
            }
        }
        return (executionPeriodItems = result);
    }

    private Object readDepartmentUnits(String selectedCurricularRuleType) {
        final List<SelectItem> result = new ArrayList<SelectItem>();
        if (selectedCurricularRuleType != null
                && selectedCurricularRuleType.equals(CurricularRuleType.ANY_CURRICULAR_COURSE.name())) {
            for (final Unit unit : UnitUtils.readAllActiveUnitsByType(PartyTypeEnum.DEPARTMENT)) {
                result.add(new SelectItem(unit.getExternalId(), unit.getName()));
            }
            Collections.sort(result, new BeanComparator("label"));
        }
        result.add(0, new SelectItem(NO_SELECTION_STRING, BundleUtil.getString(Bundle.BOLONHA, "choose")));
        return result;
    }

    private Object readParentCourseGroups(String selectedCurricularRuleType) {
        final List<SelectItem> result = new ArrayList<SelectItem>();
        if (selectedCurricularRuleType != null && !selectedCurricularRuleType.equals(NO_SELECTION_STRING)) {
            for (final Context context : getDegreeModule().getParentContextsSet()) {
                final CourseGroup courseGroup = context.getParentCourseGroup();
                if (!courseGroup.isRoot()) {
                    result.add(new SelectItem(courseGroup.getExternalId(), courseGroup.getName()));
                }
            }
            Collections.sort(result, new BeanComparator("label"));
        }
        result.add(0, new SelectItem(NO_SELECTION_STRING, BundleUtil.getString(Bundle.BOLONHA, "all")));
        return result;
    }

    private List<SelectItem> readDegreeModules(String selectedCurricularRuleType) {

        final List<SelectItem> result = new ArrayList<SelectItem>();
        if (selectedCurricularRuleType != null && !selectedCurricularRuleType.equals(NO_SELECTION_STRING)) {
            switch (CurricularRuleType.valueOf(selectedCurricularRuleType)) {
            case PRECEDENCY_APPROVED_DEGREE_MODULE:
            case PRECEDENCY_ENROLED_DEGREE_MODULE:
            case RESTRICTION_NOT_ENROLED_DEGREE_MODULE:
                getDegreeModules(CurricularCourse.class, result);
                break;

            case CREDITS_LIMIT:
            case DEGREE_MODULES_SELECTION_LIMIT:
                getDegreeModules(CourseGroup.class, result);
                break;
            case PRECEDENCY_BETWEEN_DEGREE_MODULES:
                getDegreeModules(DegreeModule.class, result);
                break;

            case EXCLUSIVENESS:
                getDegreeModules(getDegreeModule().getClass(), result);
                break;
            default:
                break;
            }
        }
        result.add(0, new SelectItem(NO_SELECTION_STRING, BundleUtil.getString(Bundle.BOLONHA, "choose")));
        return result;
    }

    private void getDegreeModules(final Class<? extends DegreeModule> clazz, final List<SelectItem> result) {
        final List<List<DegreeModule>> degreeModulesSet =
                getDegreeCurricularPlan().getDcpDegreeModulesIncludingFullPath(clazz, null);
        for (final List<DegreeModule> degreeModules : degreeModulesSet) {
            final DegreeModule lastDegreeModule = (degreeModules.size() > 0) ? degreeModules.get(degreeModules.size() - 1) : null;
            if (lastDegreeModule != getDegreeModule()) {
                final StringBuilder pathName = new StringBuilder();
                for (final DegreeModule degreeModule : degreeModules) {
                    pathName.append((pathName.length() == 0) ? "" : " > ").append(degreeModule.getName());
                }
                result.add(new SelectItem(lastDegreeModule.getExternalId(), pathName.toString()));
            }
        }
        Collections.sort(result, new BeanComparator("label"));
    }

    private String getFinalEndExecutionPeriodID() {
        return (getViewState().getAttribute("endExecutionPeriodID") == null || getViewState().getAttribute("endExecutionPeriodID")
                .equals(NO_SELECTION_STRING)) ? null : (String) getViewState().getAttribute("endExecutionPeriodID");
    }

    public String createCurricularRule() {
        try {
            checkSelectedAttributes();
            CreateRule.run(getDegreeModuleID(), CurricularRuleType.valueOf(getSelectedCurricularRuleType()),
                    buildCurricularRuleParametersDTO(), getBeginExecutionPeriodID(), getFinalEndExecutionPeriodID());
            return "setCurricularRules";
        } catch (FenixActionException e) {
            addErrorMessage(BundleUtil.getString(Bundle.BOLONHA, e.getMessage()));
        } catch (NotAuthorizedException e) {
            addErrorMessage(BundleUtil.getString(Bundle.BOLONHA, "error.notAuthorized"));
        } catch (FenixServiceException e) {
            addErrorMessage(BundleUtil.getString(Bundle.BOLONHA, e.getMessage()));
        } catch (NumberFormatException e) {
            addErrorMessage(BundleUtil.getString(Bundle.BOLONHA, "invalid.minimum.maximum.values"));
        } catch (DomainException e) {
            addErrorMessage(BundleUtil.getString(Bundle.DOMAIN_EXCEPTION, e.getMessage()));
        }
        return "";
    }

    public String editCurricularRule() {
        try {
            EditCurricularRule.run(getCurricularRuleID(), getBeginExecutionPeriodID(), getFinalEndExecutionPeriodID());
            return "setCurricularRules";
        } catch (FenixServiceException e) {
            addErrorMessage(BundleUtil.getString(Bundle.BOLONHA, e.getMessage()));
        } catch (DomainException e) {
            addErrorMessage(BundleUtil.getString(Bundle.DOMAIN_EXCEPTION, e.getMessage()));
        }
        return "";
    }

    public String deleteCurricularRule() {
        try {
            DeleteCurricularRule.run(getCurricularRuleID());
            addInfoMessage(BundleUtil.getString(Bundle.BOLONHA, "curricularRule.deleted"));
            return "setCurricularRules";
        } catch (FenixServiceException e) {
            addErrorMessage(BundleUtil.getString(Bundle.BOLONHA, e.getMessage()));
        } catch (DomainException e) {
            addErrorMessage(BundleUtil.getString(Bundle.DOMAIN_EXCEPTION, e.getKey(), e.getArgs()));
        }
        return "";
    }

    private void checkSelectedAttributes() throws FenixActionException, FenixServiceException {
        if (getSelectedCurricularRuleType() == null || getSelectedCurricularRuleType().equals(NO_SELECTION_STRING)) {
            throw new FenixActionException("must.select.curricular.rule.type");
        }
    }

    private CurricularRuleParametersDTO buildCurricularRuleParametersDTO() throws FenixServiceException, NumberFormatException {
        final CurricularRuleParametersDTO parametersDTO = new CurricularRuleParametersDTO();
        parametersDTO.setSelectedDegreeModuleID(getSelectedDegreeModuleID());
        parametersDTO.setContextCourseGroupID((getSelectedContextCourseGroupID() == null
                || getSelectedContextCourseGroupID().equals(NO_SELECTION_STRING)) ? null : getSelectedContextCourseGroupID());
        parametersDTO.setCurricularPeriodInfoDTO(
                new CurricularPeriodInfoDTO(Integer.valueOf(getSelectedSemester()), AcademicPeriod.SEMESTER));
        parametersDTO.setMinimumCredits(getMinimumCredits());
        parametersDTO.setMaximumCredits(getMaximumCredits());
        parametersDTO.setMinimumLimit(getMinimumLimit());
        parametersDTO.setMaximumLimit(getMaximumLimit());
        parametersDTO.setSelectedDegreeID((getSelectedDegreeID() == null
                || getSelectedDegreeID().equals(NO_SELECTION_STRING)) ? null : getSelectedDegreeID());
        parametersDTO.setSelectedDepartmentUnitID((getSelectedDepartmentUnitID() == null
                || getSelectedDepartmentUnitID().equals(NO_SELECTION_STRING)) ? null : getSelectedDepartmentUnitID());
        parametersDTO.setDegreeType(getDegreeType(getSelectedDegreeType()));
        // must get these values like this to prevent override values
        parametersDTO.setCredits((Double) getViewState().getAttribute("credits"));
        parametersDTO.setEven(Boolean.valueOf(getSelectedEven()));
        parametersDTO.setOptionalsConfiguration(getSelectedOptionalsConfiguration() == null || getSelectedOptionalsConfiguration()
                .equals(NO_SELECTION_STRING) ? null : Boolean.valueOf(getSelectedOptionalsConfiguration()));
        return parametersDTO;
    }

}
