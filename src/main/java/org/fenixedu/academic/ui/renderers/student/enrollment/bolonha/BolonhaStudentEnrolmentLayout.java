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
package org.fenixedu.academic.ui.renderers.student.enrollment.bolonha;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.AcademicProgram;
import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.GradeScale;
import org.fenixedu.academic.domain.OptionalEnrolment;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicAccessRule;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicOperationType;
import org.fenixedu.academic.domain.curricularRules.CreditsLimit;
import org.fenixedu.academic.domain.curricularRules.CurricularRule;
import org.fenixedu.academic.domain.curricularRules.CurricularRuleType;
import org.fenixedu.academic.domain.curricularRules.ICurricularRule;
import org.fenixedu.academic.domain.degreeStructure.Context;
import org.fenixedu.academic.domain.degreeStructure.CourseGroup;
import org.fenixedu.academic.domain.degreeStructure.CycleCourseGroup;
import org.fenixedu.academic.domain.degreeStructure.CycleType;
import org.fenixedu.academic.domain.degreeStructure.DegreeModule;
import org.fenixedu.academic.domain.enrolment.DegreeModuleToEnrol;
import org.fenixedu.academic.domain.enrolment.EnroledCurriculumModuleWrapper;
import org.fenixedu.academic.domain.enrolment.IDegreeModuleToEvaluate;
import org.fenixedu.academic.domain.groups.PermissionService;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumGroup;
import org.fenixedu.academic.domain.studentCurriculum.CycleCurriculumGroup;
import org.fenixedu.academic.dto.student.enrollment.bolonha.BolonhaStudentEnrollmentBean;
import org.fenixedu.academic.dto.student.enrollment.bolonha.StudentCurriculumEnrolmentBean;
import org.fenixedu.academic.dto.student.enrollment.bolonha.StudentCurriculumGroupBean;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.CurricularRuleLabelFormatter;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.commons.i18n.I18N;

import pt.ist.fenixWebFramework.rendererExtensions.controllers.CopyCheckBoxValuesController;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyArrayConverter;
import pt.ist.fenixWebFramework.renderers.components.HtmlActionLink;
import pt.ist.fenixWebFramework.renderers.components.HtmlBlockContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlCheckBox;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlLink;
import pt.ist.fenixWebFramework.renderers.components.HtmlMultipleHiddenField;
import pt.ist.fenixWebFramework.renderers.components.HtmlTable;
import pt.ist.fenixWebFramework.renderers.components.HtmlTableCell;
import pt.ist.fenixWebFramework.renderers.components.HtmlTableRow;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;
import pt.ist.fenixWebFramework.renderers.components.controllers.HtmlActionLinkController;
import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;
import pt.ist.fenixWebFramework.renderers.model.MetaObject;
import pt.ist.fenixWebFramework.renderers.model.MetaObjectFactory;
import pt.ist.fenixWebFramework.renderers.schemas.Schema;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

public class BolonhaStudentEnrolmentLayout extends Layout {

    private final CopyCheckBoxValuesController enrollmentsController = new CopyCheckBoxValuesController(false);

    private final CopyCheckBoxValuesController degreeModulesToEvaluateController = new CopyCheckBoxValuesController();

    private BolonhaStudentEnrollmentBean bolonhaStudentEnrollmentBean = null;

    protected boolean canPerformStudentEnrolments = false;

    public CopyCheckBoxValuesController getEnrollmentsController() {
        return enrollmentsController;
    }

    private BolonhaStudentEnrollmentInputRenderer renderer;

    public BolonhaStudentEnrollmentInputRenderer getRenderer() {
        return renderer;
    }

    public void setRenderer(final BolonhaStudentEnrollmentInputRenderer renderer) {
        this.renderer = renderer;
    }

    @Override
    public HtmlComponent createComponent(final Object object, final Class type) {
        setBolonhaStudentEnrollmentBean((BolonhaStudentEnrollmentBean) object);

        if (getBolonhaStudentEnrollmentBean() == null) {
            return new HtmlText();
        }

        final HtmlBlockContainer container = new HtmlBlockContainer();

        HtmlMultipleHiddenField hiddenEnrollments = new HtmlMultipleHiddenField();
        hiddenEnrollments.bind(getRenderer().getInputContext().getMetaObject(), "curriculumModulesToRemove");
        hiddenEnrollments.setConverter(new DomainObjectKeyArrayConverter());
        hiddenEnrollments.setController(enrollmentsController);

        HtmlMultipleHiddenField hiddenDegreeModulesToEvaluate = new HtmlMultipleHiddenField();
        hiddenDegreeModulesToEvaluate.bind(getRenderer().getInputContext().getMetaObject(), "degreeModulesToEvaluate");
        hiddenDegreeModulesToEvaluate.setConverter(getBolonhaStudentEnrollmentBean().getDegreeModulesToEvaluateConverter());
        hiddenDegreeModulesToEvaluate.setController(getDegreeModulesToEvaluateController());

        container.addChild(hiddenEnrollments);
        container.addChild(hiddenDegreeModulesToEvaluate);

        generateGroup(container, getBolonhaStudentEnrollmentBean().getDegreeModulesToEvaluate(),
                getBolonhaStudentEnrollmentBean().getStudentCurricularPlan(),
                getBolonhaStudentEnrollmentBean().getRootStudentCurriculumGroupBean(),
                getBolonhaStudentEnrollmentBean().getExecutionPeriod(), 0);

        return container;
    }

    // TODO - review all calls to this method
    protected void generateGroup(final HtmlBlockContainer blockContainer,
            final List<IDegreeModuleToEvaluate> degreeModulesToSelect, final StudentCurricularPlan studentCurricularPlan,
            final StudentCurriculumGroupBean studentCurriculumGroupBean, final ExecutionSemester executionSemester,
            final int depth) {

        if (isCycleExternal(studentCurriculumGroupBean)) {

            if (!getRenderer().isAllowedToEnrolInAffinityCycle()) {
                return;
            }

        }

        final HtmlTable groupTable = createGroupTable(blockContainer, depth);
        addGroupHeaderRow(groupTable, studentCurriculumGroupBean, executionSemester);

        if (canPerformStudentEnrolments || !groupIsConcluded(studentCurriculumGroupBean)) {

            if (getRenderer().isEncodeGroupRules()) {
                encodeCurricularRules(groupTable, studentCurriculumGroupBean.getCurriculumModule());
            }

            final HtmlTable coursesTable = createCoursesTable(blockContainer, depth);
            generateEnrolments(studentCurriculumGroupBean, coursesTable);
            generateCurricularCoursesToEnrol(coursesTable, studentCurriculumGroupBean, degreeModulesToSelect, executionSemester);

            generateGroups(blockContainer, degreeModulesToSelect, studentCurriculumGroupBean, studentCurricularPlan,
                    executionSemester, depth);
        }

        if (studentCurriculumGroupBean.isRoot()) {
            generateCycleCourseGroupsToEnrol(blockContainer, executionSemester, studentCurricularPlan, depth);
        }
    }

    static private boolean isCycleExternal(final StudentCurriculumGroupBean studentCurriculumGroupBean) {
        final CurriculumGroup curriculumModule = studentCurriculumGroupBean.getCurriculumModule();
        return curriculumModule.isCycleCurriculumGroup() && ((CycleCurriculumGroup) curriculumModule).isExternal();
    }

    protected boolean groupIsConcluded(final StudentCurriculumGroupBean bean) {
        return bean.getCurriculumModule().isCycleCurriculumGroup() && bean.getCurriculumModule().isConcluded();
    }

    protected void encodeCurricularRules(final HtmlTable groupTable, final CurriculumGroup curriculumGroup) {
        if (curriculumGroup.isNoCourseGroupCurriculumGroup()) {
            return;
        }
        final Context context = getDegreeModuleContext(curriculumGroup, getBolonhaStudentEnrollmentBean().getExecutionPeriod());
        final List<CurricularRule> curricularRules = curriculumGroup.getDegreeModule().getCurricularRules(context,
                getBolonhaStudentEnrollmentBean().getExecutionPeriod());
        if (!curricularRules.isEmpty()) {
            encodeCurricularRules(groupTable, curricularRules);
        }
    }

    protected Context getDegreeModuleContext(final CurriculumGroup curriculumGroup, final ExecutionSemester executionSemester) {
        final DegreeModule degreeModule = curriculumGroup.getDegreeModule();

        if (curriculumGroup.isRoot()) {
            return null;
        }

        final CurriculumGroup parentCurriculumGroup = curriculumGroup.getCurriculumGroup();
        for (final Context context : parentCurriculumGroup.getDegreeModule().getValidChildContexts(executionSemester)) {
            if (context.getChildDegreeModule() == degreeModule) {
                return context;
            }
        }
        return null;
    }

    protected HtmlTable createGroupTable(final HtmlBlockContainer blockContainer, final int depth) {
        final HtmlTable groupTable = new HtmlTable();
        groupTable.setClasses(getRenderer().getTablesClasses());
        groupTable.setStyle("width: " + (getRenderer().getInitialWidth() - depth) + "em; margin-left: " + depth + "em;");

        blockContainer.addChild(groupTable);

        return groupTable;
    }

    protected void addGroupHeaderRow(final HtmlTable groupTable, final StudentCurriculumGroupBean studentCurriculumGroupBean,
            final ExecutionSemester executionSemester) {

        final HtmlTableRow groupHeaderRow = groupTable.createRow();
        groupHeaderRow.setClasses(getRenderer().getGroupRowClasses());

        final HtmlTableCell titleCell = groupHeaderRow.createCell();
        if (studentCurriculumGroupBean.getCurriculumModule().isRoot()) {
            titleCell.setBody(createDegreeCurricularPlanLink(studentCurriculumGroupBean));
        } else if (studentCurriculumGroupBean.getCurriculumModule().isCycleCurriculumGroup()) {
            setTitleCellInformation(groupHeaderRow, titleCell, studentCurriculumGroupBean, executionSemester);

        } else {
            titleCell.setBody(new HtmlText(
                    buildCurriculumGroupLabel(studentCurriculumGroupBean.getCurriculumModule(), executionSemester), false));
        }

        final HtmlTableCell checkBoxCell = groupHeaderRow.createCell();
        checkBoxCell.setClasses("aright");

        final HtmlCheckBox checkBox = new HtmlCheckBox(true) {
            @Override
            public void setChecked(final boolean checked) {
                if (isDisabled()) {
                    super.setChecked(true);
                } else {
                    super.setChecked(checked);
                }
            }
        };

        MetaObject enrolmentMetaObject = MetaObjectFactory.createObject(studentCurriculumGroupBean.getCurriculumModule(),
                new Schema(CurriculumGroup.class));
        checkBox.setName("enrolmentCheckBox" + studentCurriculumGroupBean.getCurriculumModule().getExternalId());
        checkBox.setUserValue(enrolmentMetaObject.getKey().toString());
        checkBoxCell.setBody(checkBox);

        if (isToDisableEnrolmentOption(studentCurriculumGroupBean)) {
            checkBox.setDisabled(true);
        } else {
            enrollmentsController.addCheckBox(checkBox);
        }
    }

    protected void setTitleCellInformation(final HtmlTableRow groupHeaderRow, final HtmlTableCell titleCell,
            final StudentCurriculumGroupBean studentCurriculumGroupBean, final ExecutionSemester executionSemester) {

        final CycleCurriculumGroup group = (CycleCurriculumGroup) studentCurriculumGroupBean.getCurriculumModule();
        final boolean concluded = group.isConcluded();

        titleCell.setBody(new HtmlText(buildCycleCurriculumGroupLabel(group, concluded, executionSemester), false));

        if (concluded) {
            groupHeaderRow.setClasses(getRenderer().getConcludedGroupRowClasses());
        }
    }

    protected String buildCycleCurriculumGroupLabel(final CycleCurriculumGroup curriculumGroup, final boolean concluded,
            final ExecutionSemester executionSemester) {

        String label = buildCurriculumGroupLabel(curriculumGroup, executionSemester);
        if (concluded) {
            label = label.concat(" - ").concat(BundleUtil.getString(Bundle.APPLICATION, "label.curriculum.cycle.concluded"));
        }

        return label;
    }

    protected String buildCurriculumGroupLabel(final CurriculumGroup curriculumGroup, final ExecutionSemester executionSemester) {
        if (curriculumGroup.isNoCourseGroupCurriculumGroup()) {
            return curriculumGroup.getName().getContent();
        }

        final StringBuilder result = new StringBuilder(curriculumGroup.getName().getContent());

        if (getRenderer().isEncodeGroupRules()) {
            result.append(" <span title=\"");
            result.append(BundleUtil.getString(Bundle.APPLICATION, "label.curriculum.credits.legend.creditsConcluded"));
            result.append(" \"> c(");
            result.append(curriculumGroup.getCreditsConcluded(executionSemester.getExecutionYear()));
            result.append(")</span>");
        } else {
            final CreditsLimit creditsLimit = (CreditsLimit) curriculumGroup
                    .getMostRecentActiveCurricularRule(CurricularRuleType.CREDITS_LIMIT, executionSemester);

            if (creditsLimit != null) {
                result.append(" <span title=\"");
                result.append(BundleUtil.getString(Bundle.APPLICATION, "label.curriculum.credits.legend.minCredits"));
                result.append(" \">m(");
                result.append(creditsLimit.getMinimumCredits());
                result.append(")</span>,");
            }

            result.append(" <span title=\"");
            result.append(BundleUtil.getString(Bundle.APPLICATION, "label.curriculum.credits.legend.creditsConcluded"));
            result.append(" \"> c(");
            result.append(curriculumGroup.getCreditsConcluded(executionSemester.getExecutionYear()));
            result.append(")</span>");

            if (creditsLimit != null) {
                result.append(", <span title=\"");
                result.append(BundleUtil.getString(Bundle.APPLICATION, "label.curriculum.credits.legend.maxCredits"));
                result.append(" \">M(");
                result.append(creditsLimit.getMaximumCredits());
                result.append(")</span>");
            }
        }
        return result.toString();
    }

    protected HtmlLink createDegreeCurricularPlanLink(final StudentCurriculumGroupBean studentCurriculumGroupBean) {
        final HtmlLink degreeCurricularPlanLink = new HtmlLink();
        degreeCurricularPlanLink.setText(studentCurriculumGroupBean.getCurriculumModule().getName().getContent());
        degreeCurricularPlanLink.setModuleRelative(false);
        degreeCurricularPlanLink.setTarget("_blank");
        degreeCurricularPlanLink.setContextRelative(false);
        degreeCurricularPlanLink
                .setUrl(studentCurriculumGroupBean.getCurriculumModule().getStudentCurricularPlan().getDegree().getSiteUrl());

        return degreeCurricularPlanLink;
    }

    protected HtmlTable createCoursesTable(final HtmlBlockContainer blockContainer, final int depth) {
        final HtmlTable coursesTable = new HtmlTable();
        blockContainer.addChild(coursesTable);
        coursesTable.setClasses(getRenderer().getTablesClasses());
        coursesTable.setStyle("width: " + (getRenderer().getInitialWidth() - depth - getRenderer().getWidthDecreasePerLevel())
                + "em; margin-left: " + (depth + getRenderer().getWidthDecreasePerLevel()) + "em;");
        return coursesTable;
    }

    // TODO - review all calls to this method
    protected void generateCurricularCoursesToEnrol(final HtmlTable groupTable,
            final StudentCurriculumGroupBean studentCurriculumGroupBean,
            final List<IDegreeModuleToEvaluate> degreeModulesToSelect, final ExecutionSemester executionSemester) {

        final List<IDegreeModuleToEvaluate> coursesToEvaluate = studentCurriculumGroupBean.getSortedDegreeModulesToEvaluate();

        for (final IDegreeModuleToEvaluate degreeModuleToEvaluate : coursesToEvaluate) {

            HtmlTableRow htmlTableRow = groupTable.createRow();
            HtmlTableCell cellName = htmlTableRow.createCell();
            cellName.setClasses(getRenderer().getCurricularCourseToEnrolNameClasses());

            String degreeModuleName = degreeModuleToEvaluate.getDegreeModule().getNameI18N(executionSemester).getContent();

            if (degreeModuleToEvaluate.getDegreeModule().isLeaf() && !degreeModuleToEvaluate.isOptionalCurricularCourse()) {

                if (!StringUtils.isEmpty(degreeModuleToEvaluate.getDegreeModule().getCode())) {
                    degreeModuleName = degreeModuleToEvaluate.getDegreeModule().getCode() + " - " + degreeModuleName;
                }

                if (canPerformStudentEnrolments) {
                    final CurricularCourse curricularCourse = (CurricularCourse) degreeModuleToEvaluate.getDegreeModule();
                    final GradeScale gradeScaleChain = curricularCourse.getGradeScaleChain();
                    if (gradeScaleChain != GradeScale.TYPE20) {
                        degreeModuleName += " (" + BundleUtil.getString(Bundle.STUDENT, "label.grade.scale") + " - "
                                + gradeScaleChain.getDescription() + ")";
                    }
                }
            }

            cellName.setBody(new HtmlText(degreeModuleName));

            // Year
            final HtmlTableCell yearCell = htmlTableRow.createCell();
            yearCell.setClasses(getRenderer().getCurricularCourseToEnrolYearClasses());
            yearCell.setColspan(2);
            yearCell.setBody(new HtmlText(degreeModuleToEvaluate.getYearFullLabel()));

            if (!degreeModuleToEvaluate.isOptionalCurricularCourse()) {
                // Ects
                final HtmlTableCell ectsCell = htmlTableRow.createCell();
                ectsCell.setClasses(getRenderer().getCurricularCourseToEnrolEctsClasses());

                final StringBuilder ects = new StringBuilder();
                ects.append(degreeModuleToEvaluate.getEctsCredits()).append(" ")
                        .append(BundleUtil.getString(Bundle.STUDENT, "label.credits.abbreviation"));
                ectsCell.setBody(new HtmlText(ects.toString()));

                HtmlTableCell checkBoxCell = htmlTableRow.createCell();
                checkBoxCell.setClasses(getRenderer().getCurricularCourseToEnrolCheckBoxClasses());

                HtmlCheckBox checkBox = new HtmlCheckBox(false);
                if (degreeModulesToSelect.contains(degreeModuleToEvaluate)) {
                    checkBox.setChecked(true);
                }
                checkBox.setName("degreeModuleToEnrolCheckBox" + degreeModuleToEvaluate.getKey());
                checkBox.setUserValue(degreeModuleToEvaluate.getKey());
                checkBox.setVisible(!isToDisableEnrolmentOption(degreeModuleToEvaluate));
                getDegreeModulesToEvaluateController().addCheckBox(checkBox);
                checkBoxCell.setBody(checkBox);
            } else {
                final HtmlTableCell cell = htmlTableRow.createCell();
                cell.setClasses(getRenderer().getCurricularCourseToEnrolEctsClasses());
                cell.setBody(new HtmlText(""));

                HtmlTableCell linkTableCell = htmlTableRow.createCell();
                linkTableCell.setClasses(getRenderer().getCurricularCourseToEnrolCheckBoxClasses());

                final HtmlActionLink actionLink = new HtmlActionLink();
                actionLink.setText(BundleUtil.getString(Bundle.STUDENT, "label.chooseOptionalCurricularCourse"));
                actionLink.setController(new OptionalCurricularCourseLinkController(degreeModuleToEvaluate));
                if (isToDisableEnrolmentOption(degreeModuleToEvaluate)) {
                    actionLink.setOnClick("function(){return false;}");
                    actionLink.setStyle("text-decoration: line-through; color: grey; border-bottom: none;");
                } else {
                    actionLink.setOnClick(
                            "$(this).closest('form').find('input[name=\\'method\\']').attr('value', 'prepareChooseOptionalCurricularCourseToEnrol');");
                }
                // actionLink.setOnClick("document.forms[2].method.value='prepareChooseOptionalCurricularCourseToEnrol';");
                actionLink.setName("optionalCurricularCourseLink" + degreeModuleToEvaluate.getCurriculumGroup().getExternalId()
                        + "_" + degreeModuleToEvaluate.getContext().getExternalId());
                linkTableCell.setBody(actionLink);
            }

            if (getRenderer().isEncodeCurricularRules()) {
                encodeCurricularRules(groupTable, degreeModuleToEvaluate);
            }
        }
    }

    private boolean isToDisableEnrolmentOption(final StudentCurriculumGroupBean input) {
        return input.isToBeDisabled() || isStudentLogged()
                && appliesAnyRules(input.getCurriculumModule(), CurricularRuleType.ENROLMENT_TO_BE_APPROVED_BY_COORDINATOR);
    }

    private boolean isToDisableEnrolmentOption(final IDegreeModuleToEvaluate input) {
        if (isStudentLogged()) {

            if (appliesAnyRules(input, CurricularRuleType.ENROLMENT_TO_BE_APPROVED_BY_COORDINATOR)) {
                return true;
            }
        }

        return false;
    }

    private boolean appliesAnyRules(final IDegreeModuleToEvaluate input, final CurricularRuleType... curricularRuleTypes) {

        if (input != null && input.getContext() != null && curricularRuleTypes != null) {

            for (final CurricularRuleType curricularRuleType : curricularRuleTypes) {

                final CourseGroup parentCourseGroup = input.getCurriculumGroup().getDegreeModule();

                // check self rules
                final ExecutionSemester executionInterval = this.bolonhaStudentEnrollmentBean.getExecutionPeriod();
                List<? extends ICurricularRule> rules =
                        input.getDegreeModule().getCurricularRules(curricularRuleType, parentCourseGroup, executionInterval);

                if (!rules.isEmpty() && rules.iterator().next().appliesToContext(input.getContext())) {
                    return true;
                }

                // check parent group rules
                rules = parentCourseGroup.getCurricularRules(curricularRuleType, executionInterval);
                if (!rules.isEmpty() && rules.iterator().next().appliesToContext(input.getContext())) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean appliesAnyRules(final CurriculumGroup input, final CurricularRuleType... curricularRuleTypes) {

        if (input != null && curricularRuleTypes != null) {

            for (final CurricularRuleType curricularRuleType : curricularRuleTypes) {

                final CurriculumGroup parentCurriculumGroup = input.getCurriculumGroup();
                final CourseGroup parentCourseGroup = parentCurriculumGroup.getDegreeModule();

                // check self rules
                final ExecutionSemester executionInterval = this.bolonhaStudentEnrollmentBean.getExecutionPeriod();
                List<? extends ICurricularRule> rules =
                        input.getDegreeModule().getCurricularRules(curricularRuleType, parentCourseGroup, executionInterval);

                if (!rules.isEmpty()) {
                    return true;
                }

                // check parent group rules, recursively until root
                if (!parentCurriculumGroup.isRoot() && appliesAnyRules(parentCurriculumGroup, curricularRuleTypes)) {
                    return true;
                }
            }
        }

        return false;
    }

    protected void encodeCurricularRules(final HtmlTable groupTable, final IDegreeModuleToEvaluate degreeModuleToEvaluate) {
        final DegreeModule degreeModule = degreeModuleToEvaluate.getDegreeModule();
        final List<CurricularRule> curricularRules = degreeModule.getCurricularRules(degreeModuleToEvaluate.getContext(),
                degreeModuleToEvaluate.getExecutionInterval());
        if (!curricularRules.isEmpty()) {
            encodeCurricularRules(groupTable, curricularRules);
        }
    }

    protected void encodeCurricularRules(final HtmlTable groupTable, final List<CurricularRule> curricularRules) {
        final HtmlTableRow htmlTableRow = groupTable.createRow();

        final HtmlTable rulesTable = new HtmlTable();
        final HtmlTableCell cellRules = htmlTableRow.createCell();

        cellRules.setClasses(getRenderer().getCurricularCourseToEnrolNameClasses());
        cellRules.setBody(rulesTable);
        cellRules.setColspan(5);

        rulesTable.setClasses("smalltxt noborder");
        rulesTable.setStyle("width: 100%;");

        for (final CurricularRule curricularRule : curricularRules) {
            final HtmlTableCell cellName = rulesTable.createRow().createCell();
            cellName.setStyle("color: #888");
            cellName.setBody(new HtmlText(CurricularRuleLabelFormatter.getLabel(curricularRule, I18N.getLocale())));
        }
    }

    protected void generateEnrolments(final StudentCurriculumGroupBean studentCurriculumGroupBean, final HtmlTable groupTable) {
        for (final StudentCurriculumEnrolmentBean studentEnrolmentBean : studentCurriculumGroupBean
                .getEnrolledCurriculumCourses()) {
            if (studentEnrolmentBean.getCurriculumModule().isTemporary()) {
                generateEnrolment(groupTable, studentEnrolmentBean.getCurriculumModule(),
                        getRenderer().getTemporaryEnrolmentNameClasses(), getRenderer().getTemporaryEnrolmentYearClasses(),
                        getRenderer().getTemporaryEnrolmentSemesterClasses(), getRenderer().getTemporaryEnrolmentEctsClasses(),
                        getRenderer().getTemporaryEnrolmentCheckBoxClasses());
            } else if (studentEnrolmentBean.getCurriculumModule().isImpossible()) {
                generateEnrolment(groupTable, studentEnrolmentBean.getCurriculumModule(),
                        getRenderer().getImpossibleEnrolmentNameClasses(), getRenderer().getImpossibleEnrolmentYearClasses(),
                        getRenderer().getImpossibleEnrolmentSemesterClasses(), getRenderer().getImpossibleEnrolmentEctsClasses(),
                        getRenderer().getImpossibleEnrolmentCheckBoxClasses());
            } else {
                generateEnrolment(groupTable, studentEnrolmentBean.getCurriculumModule(), getRenderer().getEnrolmentNameClasses(),
                        getRenderer().getEnrolmentYearClasses(), getRenderer().getEnrolmentSemesterClasses(),
                        getRenderer().getEnrolmentEctsClasses(), getRenderer().getEnrolmentCheckBoxClasses());
            }
        }
    }

    protected void generateEnrolment(final HtmlTable groupTable, final Enrolment enrolment, final String enrolmentNameClasses,
            final String enrolmentYearClasses, final String enrolmentSemesterClasses, final String enrolmentEctsClasses,
            final String enrolmentCheckBoxClasses) {

        final EnroledCurriculumModuleWrapper wrapper =
                new EnroledCurriculumModuleWrapper(enrolment, enrolment.getExecutionPeriod());

        HtmlTableRow htmlTableRow = groupTable.createRow();
        HtmlTableCell cellName = htmlTableRow.createCell();
        cellName.setClasses(enrolmentNameClasses);

        final String enrolmentName = getPresentationNameFor(enrolment);
        cellName.setBody(new HtmlText(enrolmentName));

        // Year
        final HtmlTableCell yearCell = htmlTableRow.createCell();
        yearCell.setClasses(enrolmentYearClasses);

        final String year = enrolment.getExecutionInterval().getExecutionYear().getYear();
        yearCell.setBody(new HtmlText(year));

        // Semester
        final HtmlTableCell semesterCell = htmlTableRow.createCell();
        semesterCell.setClasses(enrolmentSemesterClasses);

        final StringBuilder semester = new StringBuilder();
        semester.append(enrolment.getExecutionInterval().getChildOrder().toString());
        semester.append(" ");
        semester.append(BundleUtil.getString(Bundle.ENUMERATION, "SEMESTER.ABBREVIATION"));
        semesterCell.setBody(new HtmlText(semester.toString()));

        // Ects
        final HtmlTableCell ectsCell = htmlTableRow.createCell();
        ectsCell.setClasses(enrolmentEctsClasses);

        final StringBuilder ects = new StringBuilder();
        final double ectsCredits =
                enrolment.isBolonhaDegree() && getBolonhaStudentEnrollmentBean().getCurricularRuleLevel().isNormal() ? enrolment
                        .getAccumulatedEctsCredits(enrolment.getExecutionPeriod()) : enrolment.getEctsCredits();
        ects.append(ectsCredits).append(" ").append(BundleUtil.getString(Bundle.STUDENT, "label.credits.abbreviation"));

        ectsCell.setBody(new HtmlText(ects.toString()));

        MetaObject enrolmentMetaObject = MetaObjectFactory.createObject(enrolment, new Schema(Enrolment.class));

        HtmlCheckBox checkBox = new HtmlCheckBox(true);
        checkBox.setName("enrolmentCheckBox" + enrolment.getExternalId());
        checkBox.setUserValue(enrolmentMetaObject.getKey().toString());
        checkBox.setDisabled(isToDisableEnrolmentOption(wrapper));
        enrollmentsController.addCheckBox(checkBox);

        HtmlTableCell cellCheckBox = htmlTableRow.createCell();
        cellCheckBox.setClasses(enrolmentCheckBoxClasses);
        cellCheckBox.setBody(checkBox);
    }

    protected String getPresentationNameFor(final Enrolment enrolment) {
        String result = null;

        String code = StringUtils.EMPTY;
        if (!StringUtils.isEmpty(enrolment.getCode())) {
            code = enrolment.getCode() + " - ";
        }

        if (enrolment instanceof OptionalEnrolment) {
            final ExecutionInterval executionInterval = enrolment.getExecutionInterval();
            final OptionalEnrolment optionalEnrolment = (OptionalEnrolment) enrolment;

            result = optionalEnrolment.getOptionalCurricularCourse().getNameI18N(executionInterval).getContent() + " (" + code
                    + optionalEnrolment.getCurricularCourse().getNameI18N(executionInterval).getContent() + ")";
        } else {

            result = code + enrolment.getName().getContent();
        }

        if (enrolment.getDegreeModule().isLeaf() && !enrolment.isOptional()) {

            if (canPerformStudentEnrolments) {
                final CurricularCourse curricularCourse = (CurricularCourse) enrolment.getDegreeModule();
                final GradeScale gradeScaleChain = curricularCourse.getGradeScaleChain();
                if (gradeScaleChain != GradeScale.TYPE20) {
                    result += " (" + BundleUtil.getString(Bundle.STUDENT, "label.grade.scale") + " - "
                            + gradeScaleChain.getDescription() + ")";
                }
            }
        }

        return result;
    }

    // TODO - review all calls to this method
    protected void generateGroups(final HtmlBlockContainer container, final List<IDegreeModuleToEvaluate> degreeModulesToSelect,
            final StudentCurriculumGroupBean bean, final StudentCurricularPlan plan, final ExecutionSemester executionSemester,
            final int depth) {

        // first enroled
        for (final StudentCurriculumGroupBean iter : bean.getEnrolledCurriculumGroupsSortedByOrder(executionSemester)) {
            generateGroup(container, degreeModulesToSelect, plan, iter, executionSemester,
                    depth + getRenderer().getWidthDecreasePerLevel());
        }

        // then available to enrol
        for (final IDegreeModuleToEvaluate iter : bean.getCourseGroupsToEnrolSortedByContext()) {
            generateCourseGroupToEnroll(container, iter, plan, depth + getRenderer().getWidthDecreasePerLevel());
        }
    }

    protected void generateCourseGroupToEnroll(final HtmlBlockContainer blockContainer,
            final IDegreeModuleToEvaluate degreeModuleToEnrol, final StudentCurricularPlan studentCurricularPlan,
            final int depth) {

        final CourseGroup courseGroup = (CourseGroup) degreeModuleToEnrol.getContext().getChildDegreeModule();
        if (courseGroup.isCycleCourseGroup()) {
            return;
        }

        generateCourseGroupToEnroll(blockContainer, degreeModuleToEnrol, depth);
    }

    protected void generateCourseGroupToEnroll(final HtmlBlockContainer blockContainer,
            final IDegreeModuleToEvaluate degreeModuleToEnrol, final int depth) {
        final HtmlTable groupTable = createGroupTable(blockContainer, depth);

        HtmlTableRow htmlTableRow = groupTable.createRow();
        htmlTableRow.setClasses(getRenderer().getGroupRowClasses());
        htmlTableRow.createCell().setBody(new HtmlText(degreeModuleToEnrol.getContext().getChildDegreeModule().getName()));
        HtmlTableCell cell = htmlTableRow.createCell();
        cell.setClasses("aright");

        HtmlCheckBox checkBox = new HtmlCheckBox(false);
        final String name = "degreeModuleToEnrolCheckBox" + degreeModuleToEnrol.getContext().getExternalId().toString() + ":"
                + degreeModuleToEnrol.getCurriculumGroup().getExternalId().toString();
        checkBox.setName(name);
        checkBox.setUserValue(degreeModuleToEnrol.getKey());
        getDegreeModulesToEvaluateController().addCheckBox(checkBox);
        cell.setBody(checkBox);

        if (getRenderer().isEncodeGroupRules()) {
            encodeCurricularRules(groupTable, degreeModuleToEnrol);
        }
    }

    private boolean isStudentLogged() {
        return isStudentLogged(bolonhaStudentEnrollmentBean.getStudentCurricularPlan());
    }

    private boolean isStudentLogged(final StudentCurricularPlan studentCurricularPlan) {
        return Authenticate.getUser().getPerson() == studentCurricularPlan.getPerson();
    }

    protected void generateCycleCourseGroupsToEnrol(final HtmlBlockContainer container, final ExecutionSemester executionSemester,
            final StudentCurricularPlan studentCurricularPlan, final int depth) {

        if (canPerformStudentEnrolments) {
            for (final CycleType cycleType : getAllCycleTypesToEnrolPreviousToFirstExistingCycle(studentCurricularPlan)) {
                generateCourseGroupToEnroll(container,
                        buildDegreeModuleToEnrolForCycle(studentCurricularPlan, cycleType, executionSemester),
                        depth + getRenderer().getWidthDecreasePerLevel());

            }
        }

        if (getRenderer().isAllowedToChooseAffinityCycle()) {

            for (final CycleType cycleType : studentCurricularPlan.getSupportedCycleTypesToEnrol()) {
                generateCycleCourseGroupToEnrol(container, cycleType, depth + getRenderer().getWidthDecreasePerLevel());
            }

        }

    }

    protected IDegreeModuleToEvaluate buildDegreeModuleToEnrolForCycle(final StudentCurricularPlan scp, final CycleType cycleType,
            final ExecutionSemester semester) {
        final CycleCourseGroup cycleCourseGroup = scp.getCycleCourseGroup(cycleType);
        final Context context = cycleCourseGroup.getParentContextsByExecutionSemester(semester).iterator().next();
        return new DegreeModuleToEnrol(scp.getRoot(), context, semester);
    }

    protected List<CycleType> getAllCycleTypesToEnrolPreviousToFirstExistingCycle(final StudentCurricularPlan scp) {
        final List<CycleType> result = new ArrayList<>();

        List<CycleType> supportedCyclesToEnrol = new ArrayList<>(scp.getDegreeType().getSupportedCyclesToEnrol());
        Collections.sort(supportedCyclesToEnrol, CycleType.COMPARATOR_BY_LESS_WEIGHT);

        for (final CycleType cycleType : supportedCyclesToEnrol) {
            if (scp.hasCycleCurriculumGroup(cycleType)) {
                break;
            } else {
                result.add(cycleType);
            }
        }

        return result;
    }

    protected void generateCycleCourseGroupToEnrol(final HtmlBlockContainer container, final CycleType cycleType,
            final int depth) {

        final HtmlTable groupTable = createGroupTable(container, depth);
        HtmlTableRow htmlTableRow = groupTable.createRow();
        htmlTableRow.setClasses(getRenderer().getGroupRowClasses());
        htmlTableRow.createCell().setBody(new HtmlText(RenderUtils.getEnumString(cycleType)));
        HtmlTableCell cell = htmlTableRow.createCell();
        cell.setClasses("aright");

        final HtmlActionLink actionLink = new HtmlActionLink();
        actionLink.setText(BundleUtil.getString(Bundle.STUDENT, "label.choose"));
        actionLink.setController(new CycleSelectionLinkController(cycleType));
        actionLink.setOnClick(
                "(function (z){var node = z; while(node.tagName != 'FORM'){ node = node.parentNode; } return node;})(this).method.value='prepareChooseCycleCourseGroupToEnrol';");
        actionLink.setName("cycleLink_" + cycleType.name());
        cell.setBody(actionLink);

    }

    public CopyCheckBoxValuesController getDegreeModulesToEvaluateController() {
        return degreeModulesToEvaluateController;
    }

    public void setBolonhaStudentEnrollmentBean(final BolonhaStudentEnrollmentBean bolonhaStudentEnrollmentBean) {
        this.bolonhaStudentEnrollmentBean = bolonhaStudentEnrollmentBean;
        Set<AcademicProgram> programs = AcademicAccessRule
                .getProgramsAccessibleToFunction(AcademicOperationType.STUDENT_ENROLMENTS, Authenticate.getUser())
                .collect(Collectors.toSet());
        programs.addAll(PermissionService.getDegrees("ACADEMIC_OFFICE_ENROLMENTS", Authenticate.getUser()));
        this.canPerformStudentEnrolments = programs.contains(bolonhaStudentEnrollmentBean.getStudentCurricularPlan().getDegree());
    }

    public BolonhaStudentEnrollmentBean getBolonhaStudentEnrollmentBean() {
        return bolonhaStudentEnrollmentBean;
    }

    protected static class OptionalCurricularCourseLinkController extends HtmlActionLinkController {

        private static final long serialVersionUID = 2760270166511466030L;
        private final IDegreeModuleToEvaluate degreeModuleToEnrol;

        public OptionalCurricularCourseLinkController(final IDegreeModuleToEvaluate degreeModuleToEnrol) {
            this.degreeModuleToEnrol = degreeModuleToEnrol;
        }

        @Override
        protected boolean isToSkipUpdate() {
            return false;
        }

        @Override
        public void linkPressed(final IViewState viewState, final HtmlActionLink link) {
            ((BolonhaStudentEnrollmentBean) viewState.getMetaObject().getObject())
                    .setOptionalDegreeModuleToEnrol(this.degreeModuleToEnrol);

        }
    }

    protected static class CycleSelectionLinkController extends HtmlActionLinkController {

        private static final long serialVersionUID = -5469571160954095720L;
        private final CycleType cycleTypeToEnrol;

        public CycleSelectionLinkController(final CycleType cycleTypeToEnrol) {
            this.cycleTypeToEnrol = cycleTypeToEnrol;
        }

        @Override
        protected boolean isToSkipUpdate() {
            return false;
        }

        @Override
        public void linkPressed(final IViewState viewState, final HtmlActionLink link) {
            final BolonhaStudentEnrollmentBean studentEnrollmentBean =
                    (BolonhaStudentEnrollmentBean) viewState.getMetaObject().getObject();
            studentEnrollmentBean.setCycleTypeToEnrol(this.cycleTypeToEnrol);

        }

    }
}
