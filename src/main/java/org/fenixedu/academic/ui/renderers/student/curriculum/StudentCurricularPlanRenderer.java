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
package org.fenixedu.academic.ui.renderers.student.curriculum;

import java.io.IOException;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.fenixedu.academic.domain.AcademicProgram;
import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.EnrolmentEvaluation;
import org.fenixedu.academic.domain.EvaluationSeason;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Grade;
import org.fenixedu.academic.domain.IEnrolment;
import org.fenixedu.academic.domain.OptionalEnrolment;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.accessControl.AcademicAuthorizationGroup;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicAccessRule;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicOperationType;
import org.fenixedu.academic.domain.curricularRules.CreditsLimit;
import org.fenixedu.academic.domain.curricularRules.CurricularRuleType;
import org.fenixedu.academic.domain.degreeStructure.ProgramConclusion;
import org.fenixedu.academic.domain.groups.PermissionService;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.curriculum.ConclusionProcess;
import org.fenixedu.academic.domain.student.curriculum.ICurriculumEntry;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumGroup;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumModule.ConclusionValue;
import org.fenixedu.academic.domain.studentCurriculum.Dismissal;
import org.fenixedu.academic.domain.studentCurriculum.ExternalEnrolment;
import org.fenixedu.academic.domain.studentCurriculum.NoCourseGroupCurriculumGroup;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.academic.service.AcademicPermissionService;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.core.security.Authenticate;
import org.joda.time.YearMonthDay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

import pt.ist.fenixWebFramework.renderers.InputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlBlockContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlCheckBox;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlForm;
import pt.ist.fenixWebFramework.renderers.components.HtmlImage;
import pt.ist.fenixWebFramework.renderers.components.HtmlInlineContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlLink;
import pt.ist.fenixWebFramework.renderers.components.HtmlTable;
import pt.ist.fenixWebFramework.renderers.components.HtmlTableCell;
import pt.ist.fenixWebFramework.renderers.components.HtmlTableRow;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;
import pt.ist.fenixWebFramework.renderers.contexts.InputContext;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;

public class StudentCurricularPlanRenderer extends InputRenderer {

    private static final Logger logger = LoggerFactory.getLogger(StudentCurricularPlanRenderer.class);

    private static final String SCPLANTEMPORARYDISMISSAL = "scplantemporarydismissal";

    private static final String SCPLANDISMISSAL = "scplandismissal";

    private static final String SCPLANENROLLMENT = "scplanenrollment";

    private static final String SCPLANGROUP = "scplangroup";

    private static final String SCPLAN = "scplan";

    private static final String CELL_CLASSES =
            "scplancolident, scplancolcurricularcourse, scplancoldegreecurricularplan, scplancolenrollmentstate, "
                    + "scplancolenrollmenttype, scplancolgrade, scplancolweight, scplancolects, "
                    + "scplancolenrolmentevaluationtype, scplancolyear, scplancolsemester, scplancolexamdate, scplancolgraderesponsible, scplancolstatistics";

    public static enum EnrolmentStateFilterType {
        ALL, APPROVED, APPROVED_OR_ENROLED;

        public String getName() {
            return name();
        }

        public String getFullyQualifiedName() {
            return getClass().getName() + "." + name();
        }

        public static EnrolmentStateFilterType[] getValues() {
            return values();
        }

    }

    public static enum ViewType {
        ALL, ENROLMENTS, DISMISSALS;

        public String getName() {
            return name();
        }

        public String getFullyQualifiedName() {
            return getClass() + "." + name();
        }

        public static ViewType[] getValues() {
            return values();
        }

    }

    public static enum OrganizationType {
        GROUPS, EXECUTION_YEARS;

        public String getName() {
            return name();
        }

        public String getFullyQualifiedName() {
            return getClass() + "." + name();
        }

        public static OrganizationType[] getValues() {
            return values();
        }

    }

    private OrganizationType organizedBy = OrganizationType.GROUPS;

    private EnrolmentStateFilterType enrolmentStateFilter = EnrolmentStateFilterType.ALL;

    private boolean detailed = true;

    private ViewType viewType = ViewType.ALL;

    private String studentCurricularPlanClass = SCPLAN;

    private String curriculumGroupRowClass = SCPLANGROUP;

    private String enrolmentRowClass = SCPLANENROLLMENT;

    private String dismissalRowClass = SCPLANDISMISSAL;

    private String temporaryDismissalRowClass = SCPLANTEMPORARYDISMISSAL;

    private String cellClasses = CELL_CLASSES;

    private boolean selectable;

    private String selectionName;

    public StudentCurricularPlanRenderer() {
        super();
    }

    public void setOrganizedBy(String organizedBy) {
        this.organizedBy = OrganizationType.valueOf(organizedBy);
    }

    public void setOrganizedByEnum(final OrganizationType organizationType) {
        this.organizedBy = organizationType;
    }

    public boolean isOrganizedByGroups() {
        return this.organizedBy == OrganizationType.GROUPS;
    }

    public boolean isOrganizedByExecutionYears() {
        return this.organizedBy == OrganizationType.EXECUTION_YEARS;
    }

    public void setEnrolmentStateFilter(final String type) {
        this.enrolmentStateFilter = EnrolmentStateFilterType.valueOf(type);
    }

    public void setEnrolmentStateFilterEnum(final EnrolmentStateFilterType enrolmentStateFilter) {
        this.enrolmentStateFilter = enrolmentStateFilter;
    }

    public boolean isToShowAllEnrolmentStates() {
        return this.enrolmentStateFilter == EnrolmentStateFilterType.ALL;
    }

    public boolean isToShowApprovedOnly() {
        return this.enrolmentStateFilter == EnrolmentStateFilterType.APPROVED;
    }

    public boolean isToShowApprovedOrEnroledStatesOnly() {
        return this.enrolmentStateFilter == EnrolmentStateFilterType.APPROVED_OR_ENROLED;
    }

    public void setViewType(final String type) {
        this.viewType = ViewType.valueOf(type);
    }

    public void setViewTypeEnum(final ViewType viewType) {
        this.viewType = viewType;
    }

    public boolean isToShowDismissals() {
        return this.viewType == ViewType.DISMISSALS || this.viewType == ViewType.ALL;
    }

    public boolean isToShowEnrolments() {
        return this.viewType == ViewType.ENROLMENTS || this.viewType == ViewType.ALL;
    }

    public String getStudentCurricularPlanClass() {
        return studentCurricularPlanClass;
    }

    public void setStudentCurricularPlanClass(String studentCurricularPlanClass) {
        this.studentCurricularPlanClass = studentCurricularPlanClass;
    }

    public String getCurriculumGroupRowClass() {
        return curriculumGroupRowClass;
    }

    public void setCurriculumGroupRowClass(String curriculumGroupRowClass) {
        this.curriculumGroupRowClass = curriculumGroupRowClass;
    }

    public String getEnrolmentRowClass() {
        return enrolmentRowClass;
    }

    public void setEnrolmentRowClass(String enrolmentRowClass) {
        this.enrolmentRowClass = enrolmentRowClass;
    }

    public String getDismissalRowClass() {
        return dismissalRowClass;
    }

    public void setDismissalRowClass(String dismissalRowClass) {
        this.dismissalRowClass = dismissalRowClass;
    }

    public String getTemporaryDismissalRowClass() {
        return temporaryDismissalRowClass;
    }

    public void setTemporaryDismissalRowClass(String temporaryDismissalRowClass) {
        this.temporaryDismissalRowClass = temporaryDismissalRowClass;
    }

    public void setCellClasses(String cellClasses) {
        this.cellClasses = cellClasses;
    }

    public String[] getCellClasses() {
        return this.cellClasses.split(",");
    }

    public String getTabCellClass() {
        return getCellClasses()[0];
    }

    public String getLabelCellClass() {
        return getCellClasses()[1];
    }

    public String getDegreeCurricularPlanCellClass() {
        return getCellClasses()[2];
    }

    public String getEnrolmentStateCellClass() {
        return getCellClasses()[3];
    }

    public String getEnrolmentTypeCellClass() {
        return getCellClasses()[4];
    }

    public String getGradeCellClass() {
        return getCellClasses()[5];
    }

    public String getWeightCellClass() {
        return getCellClasses()[6];
    }

    public String getEctsCreditsCellClass() {
        return getCellClasses()[7];
    }

    public String getLastEnrolmentEvaluationTypeCellClass() {
        return getCellClasses()[8];
    }

    public String getEnrolmentExecutionYearCellClass() {
        return getCellClasses()[9];
    }

    public String getEnrolmentSemesterCellClass() {
        return getCellClasses()[10];
    }

    public String getCreationDateCellClass() {
        return getCellClasses()[11];
    }

    public String getCreatorCellClass() {
        return getCellClasses()[12];
    }

    public String getStatisticsLinkCellClass() {
        return getCellClasses()[13];
    }

    public boolean isDetailed() {
        return detailed;
    }

    public void setDetailed(boolean detailed) {
        this.detailed = detailed;
    }

    public boolean isSelectable() {
        return selectable;
    }

    public void setSelectable(boolean selectable) {
        this.selectable = selectable;
    }

    public String getSelectionName() {
        return selectionName;
    }

    public void setSelectionName(String selectionName) {
        this.selectionName = selectionName;
    }

    private static Function<StudentCurricularPlanRenderer, Layout> layoutProvider =
            renderer -> new StudentCurricularPlanLayout(renderer);

    public static void setLayoutProvider(Function<StudentCurricularPlanRenderer, Layout> layoutProvider) {
        StudentCurricularPlanRenderer.layoutProvider = layoutProvider;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
        return layoutProvider.apply(this);
    }

    public static class StudentCurricularPlanLayout extends Layout {

        protected static final String EMPTY_INFO = "-";

        protected static final String SPACER_IMAGE_PATH = "/images/scp_spacer.gif";

        protected static final int MAX_LINE_SIZE = 26;

        protected static final int MAX_COL_SPAN_FOR_TEXT_ON_GROUPS_WITH_CHILDS = 17;

        protected static final int MAX_COL_SPAN_FOR_TEXT_ON_CURRICULUM_LINES = 14;

        protected static final int HEADERS_SIZE = 3;

        protected static final int COLUMNS_BETWEEN_TEXT_AND_GRADE = 3;

        protected static final int COLUMNS_BETWEEN_TEXT_AND_ECTS = 5;

        protected static final int COLUMNS_FROM_ECTS_AND_ENROLMENT_EVALUATION_DATE = 4;

        protected static final int COLUMNS_BETWEEN_TEXT_AND_ENROLMENT_EVALUATION_DATE =
                COLUMNS_BETWEEN_TEXT_AND_ECTS + COLUMNS_FROM_ECTS_AND_ENROLMENT_EVALUATION_DATE;

        protected static final int LATEST_ENROLMENT_EVALUATION_COLUMNS = 3;

        protected static final String DATE_FORMAT = "yyyy/MM/dd";

        protected static final int ENROLLMENT_EVALUATION_TYPE_NEXT_COLUMN_SPAN = 3;

        protected static final int GRADE_NEXT_COLUMN_SPAN = 3;

        protected StudentCurricularPlan studentCurricularPlan;

        protected ExecutionYear executionYearContext;

        protected ExecutionInterval executionPeriodContext;

        protected StudentCurricularPlanRenderer renderer;

        public StudentCurricularPlanLayout(StudentCurricularPlanRenderer renderer) {
            this.renderer = renderer;
        }

        @Override
        public HtmlComponent createComponent(Object object, Class type) {
            final InputContext inputContext = renderer.getInputContext();
            final HtmlForm htmlForm = inputContext.getForm();
            htmlForm.getSubmitButton().setVisible(false);
            htmlForm.getCancelButton().setVisible(false);

            this.studentCurricularPlan = (StudentCurricularPlan) object;

            final HtmlContainer container = new HtmlBlockContainer();
            if (this.studentCurricularPlan == null) {
                container.addChild(createHtmlTextItalic(BundleUtil.getString(Bundle.STUDENT, "message.no.curricularplan")));

                return container;
            }

            this.executionYearContext = initializeExecutionYear();
            this.executionPeriodContext = executionYearContext.getLastExecutionPeriod();

            final HtmlTable mainTable = new HtmlTable();
            container.addChild(mainTable);
            mainTable.setClasses(renderer.getStudentCurricularPlanClass());

            if (renderer.isOrganizedByGroups()) {
                generateRowsForGroupsOrganization(mainTable, this.studentCurricularPlan.getRoot(), 0);
            } else if (renderer.isOrganizedByExecutionYears()) {
                generateRowsForExecutionYearsOrganization(mainTable);
            } else {
                throw new RuntimeException("Unexpected organization type");
            }

            return container;
        }

        protected ExecutionYear initializeExecutionYear() {

            if (!studentCurricularPlan.getRegistration().hasConcluded()) {
                return ExecutionYear.findCurrent(studentCurricularPlan.getDegree().getCalendar());
            }

            final ExecutionYear lastApprovementExecutionYear = studentCurricularPlan.getLastApprovementExecutionYear();
            if (lastApprovementExecutionYear != null) {
                return lastApprovementExecutionYear;
            }

            final ExecutionYear lastSCPExecutionYear = studentCurricularPlan.getLastExecutionYear();
            if (lastSCPExecutionYear != null) {
                return lastSCPExecutionYear;
            }

            return ExecutionYear.findCurrent(studentCurricularPlan.getDegree().getCalendar());
        }

        protected void generateRowsForExecutionYearsOrganization(HtmlTable mainTable) {

            if (renderer.isToShowEnrolments()) {
                final Set<ExecutionInterval> enrolmentExecutionPeriods =
                        new TreeSet<>(ExecutionInterval.COMPARATOR_BY_BEGIN_DATE);
                enrolmentExecutionPeriods.addAll(this.studentCurricularPlan.getEnrolmentsExecutionPeriods());

                for (final ExecutionInterval enrolmentsExecutionPeriod : enrolmentExecutionPeriods) {
                    generateGroupRowWithText(mainTable,
                            enrolmentsExecutionPeriod.getExecutionYear().getYear() + ", " + enrolmentsExecutionPeriod.getName(),
                            true, 0, (CurriculumGroup) null);
                    generateEnrolmentRows(mainTable,
                            this.studentCurricularPlan.getEnrolmentsByExecutionPeriod(enrolmentsExecutionPeriod), 0);
                }
            }

            if (renderer.isToShowDismissals()) {
                final List<Dismissal> dismissals = this.studentCurricularPlan.getDismissals();
                if (!dismissals.isEmpty()) {
                    generateGroupRowWithText(mainTable, BundleUtil.getString(Bundle.STUDENT, "label.dismissals"), true, 0,
                            (CurriculumGroup) null);
                    generateDismissalRows(mainTable, dismissals, 0);
                }
            }

        }

        protected HtmlText createHtmlTextItalic(final String message) {
            final HtmlText htmlText = new HtmlText(message);
            htmlText.setClasses("italic");

            return htmlText;
        }

        protected void generateRowsForGroupsOrganization(final HtmlTable mainTable, final CurriculumGroup curriculumGroup,
                final int level) {

            generateGroupRowWithText(mainTable, curriculumGroup.getName().getContent(), curriculumGroup.hasCurriculumLines(),
                    level, curriculumGroup);
            generateCurriculumLineRows(mainTable, curriculumGroup, level + 1);
            generateChildGroupRows(mainTable, curriculumGroup, level + 1);
        }

        protected void generateGroupRowWithText(final HtmlTable mainTable, final String text, boolean addHeaders, final int level,
                final CurriculumGroup curriculumGroup) {

            final HtmlTableRow groupRow = mainTable.createRow();
            groupRow.setClasses(renderer.getCurriculumGroupRowClass());
            addTabsToRow(groupRow, level);

            final HtmlTableCell cell = groupRow.createCell();
            cell.setClasses(renderer.getLabelCellClass());

            final HtmlComponent body;
            if (curriculumGroup != null && curriculumGroup.isRoot()) {
                body = createDegreeCurricularPlanNameLink(curriculumGroup.getDegreeCurricularPlanOfDegreeModule(),
                        executionPeriodContext);
            } else {
                body = new HtmlText(createGroupName(text, curriculumGroup).toString(), false);
            }
            cell.setBody(body);

            if (!addHeaders) {
                cell.setColspan(MAX_LINE_SIZE - level);// - 2);
                // generateRulesInfo(groupRow, curriculumGroup);
            } else {
                cell.setColspan(MAX_COL_SPAN_FOR_TEXT_ON_GROUPS_WITH_CHILDS - level);

                generateHeadersForGradeWeightAndEctsCredits(groupRow);
                final HtmlTableCell cellAfterEcts = groupRow.createCell();
                cellAfterEcts.setColspan(MAX_LINE_SIZE - MAX_COL_SPAN_FOR_TEXT_ON_GROUPS_WITH_CHILDS - HEADERS_SIZE);// -
                // 2);

                // generateRulesInfo(groupRow, curriculumGroup);
            }
        }

        protected StringBuilder createGroupName(final String text, final CurriculumGroup curriculumGroup) {
            final StringBuilder groupName = new StringBuilder(text);
            if (curriculumGroup != null && curriculumGroup.getDegreeModule() != null) {

                final CreditsLimit creditsLimit = (CreditsLimit) curriculumGroup
                        .getMostRecentActiveCurricularRule(CurricularRuleType.CREDITS_LIMIT, executionYearContext);

                if (creditsLimit != null) {
                    groupName.append(" <span title=\"");
                    groupName.append(BundleUtil.getString(Bundle.APPLICATION, "label.curriculum.credits.legend.minCredits"));
                    groupName.append(" \">m(");
                    groupName.append(creditsLimit.getMinimumCredits());
                    groupName.append(")</span>,");
                }

                groupName.append(" <span title=\"");
                groupName.append(BundleUtil.getString(Bundle.APPLICATION, "label.curriculum.credits.legend.creditsConcluded"));
                groupName.append(" \"> c(");
                groupName.append(curriculumGroup.getCreditsConcluded(executionYearContext));
                groupName.append(")</span>");

                if (isViewerAllowedToViewFullStudentCurriculum(studentCurricularPlan)) {
                    groupName.append(" <span title=\"");
                    groupName.append(BundleUtil.getString(Bundle.APPLICATION, "label.curriculum.credits.legend.approvedCredits"));
                    groupName.append(" \">, ca(");
                    groupName.append(curriculumGroup.getAprovedEctsCredits());
                    groupName.append(")</span>");
                }

                if (creditsLimit != null) {
                    groupName.append("<span title=\"");
                    groupName.append(BundleUtil.getString(Bundle.APPLICATION, "label.curriculum.credits.legend.maxCredits"));
                    groupName.append("\">, M(");
                    groupName.append(creditsLimit.getMaximumCredits());
                    groupName.append(") </span>");
                }

                if (isViewerAllowedToViewFullStudentCurriculum(studentCurricularPlan) && creditsLimit != null) {

                    final ConclusionValue value = curriculumGroup.isConcluded(executionYearContext);
                    groupName.append(
                            "<em style=\"background-color:" + getBackgroundColor(value) + "; color:" + getColor(value) + "\"");
                    groupName.append("> ");
                    groupName.append(value.getLocalizedName());
                    groupName.append("</em>");

                    ProgramConclusion programConclusion = curriculumGroup.getDegreeModule().getProgramConclusion();
                    if (programConclusion != null && curriculumGroup == programConclusion
                            .groupFor(this.studentCurricularPlan.getRegistration()).orElse(null)) {
                        final StringBuilder conclusionText = new StringBuilder();

                        ConclusionProcess conclusionProcess = curriculumGroup.getConclusionProcess();
                        if (conclusionProcess != null) {
                            conclusionText.append(" <em style=\"background-color:" + getBackgroundColor(ConclusionValue.CONCLUDED)
                                    + "; color:" + getColor(ConclusionValue.CONCLUDED) + "\"");
                            conclusionText.append(">");

                            String conclusionDate = conclusionProcess.getConclusionDate().toString("yyyy-MM-dd");

                            conclusionText.append(
                                    BundleUtil.getString(Bundle.APPLICATION, "label.curriculum.group.concluded", conclusionDate));
                            conclusionText.append("</em>");
                        } else {
                            conclusionText
                                    .append(" <em style=\"background-color:" + getBackgroundColor(ConclusionValue.NOT_CONCLUDED)
                                            + "; color:" + getColor(ConclusionValue.NOT_CONCLUDED) + "\"");
                            conclusionText.append(">");

                            conclusionText
                                    .append(BundleUtil.getString(Bundle.APPLICATION, "label.curriculum.group.not.concluded"));
                            conclusionText.append("</em>");
                        }

                        final HtmlComponent programConclusionLink = createProgramConclusionLink(conclusionText.toString(),
                                curriculumGroup.getRegistration(), programConclusion);
                        try {
                            StringWriter stringWriter = new StringWriter();
                            programConclusionLink.draw(stringWriter);
                            groupName.append(stringWriter.toString());
                        } catch (IOException e) {
                            logger.error("Can't draw link to writer", e);
                        }
                    }

                }
            }
            return groupName;
        }

        protected String getBackgroundColor(ConclusionValue value) {
            switch (value) {
            case CONCLUDED:
                return "#dfb";

            case UNKNOWN:
                return "#fff7bb";

            case NOT_CONCLUDED:
                return "#ffeadd";

            default:
                return "";
            }
        }

        protected String getColor(ConclusionValue value) {
            switch (value) {
            case CONCLUDED:
                return "rgb(85, 85, 85)";

            case UNKNOWN:
                return "rgb(85, 85, 85)";

            case NOT_CONCLUDED:
                return "#c00";

            default:
                return "";
            }
        }

        protected void generateCurriculumLineRows(HtmlTable mainTable, CurriculumGroup curriculumGroup, int level) {

            if (renderer.isToShowDismissals()) {
                generateDismissalRows(mainTable, curriculumGroup.getChildDismissals(), level);
            }

            if (renderer.isToShowEnrolments()) {
                generateEnrolmentRows(mainTable, curriculumGroup.getChildEnrolments(), level);
            }
        }

        protected void generateDismissalRows(HtmlTable mainTable, List<Dismissal> dismissals, int level) {
            final Set<Dismissal> sortedDismissals =
                    new TreeSet<Dismissal>(Dismissal.COMPARATOR_BY_EXECUTION_PERIOD_AND_NAME_AND_ID);
            sortedDismissals.addAll(dismissals);

            for (final Dismissal dismissal : sortedDismissals) {
                generateDismissalRow(mainTable, dismissal, level);
            }
        }

        protected void generateDismissalRow(HtmlTable mainTable, Dismissal dismissal, int level) {
            final HtmlTableRow dismissalRow = mainTable.createRow();
            addTabsToRow(dismissalRow, level);
            dismissalRow.setClasses(dismissal.getCredits().isTemporary() ? renderer.getTemporaryDismissalRowClass() : renderer
                    .getDismissalRowClass());

            generateDismissalLabelCell(mainTable, dismissalRow, dismissal, level);
            generateCellsBetweenLabelAndGradeCell(dismissalRow);
            generateDismissalGradeCell(dismissalRow, dismissal);
            generateDismissalWeightCell(dismissalRow, dismissal);
            generateDismissalEctsCell(dismissalRow, dismissal);
            generateCellWithText(dismissalRow, EMPTY_INFO, renderer.getLastEnrolmentEvaluationTypeCellClass());
            generateExecutionYearCell(dismissalRow, dismissal);
            generateSemesterCell(dismissalRow, dismissal);
            generateDismissalApprovementlDateIfRequired(dismissalRow, dismissal.getApprovementDate());
            generateCreatorIfRequired(dismissalRow, dismissal.getCreatedBy());
            generateSpacerCellsIfRequired(dismissalRow);
        }

        protected void generateDismissalWeightCell(HtmlTableRow dismissalRow, Dismissal dismissal) {
            generateCellWithText(dismissalRow, EMPTY_INFO, renderer.getWeightCellClass());

        }

        protected void generateDismissalGradeCell(HtmlTableRow dismissalRow, Dismissal dismissal) {
            final String gradeValue =
                    !StringUtils.isEmpty(dismissal.getCredits().getGivenGrade()) ? dismissal.getCredits().getGivenGrade() : null;
            final String gradeString;
            if (gradeValue != null && NumberUtils.isNumber(gradeValue)) {
                final DecimalFormat decimalFormat = new DecimalFormat("##.##");
                gradeString = decimalFormat.format(Double.valueOf(gradeValue));
            } else {
                gradeString = gradeValue != null ? gradeValue : EMPTY_INFO;
            }

            generateCellWithText(dismissalRow, gradeString, renderer.getGradeCellClass());
        }

        protected void generateCellsBetweenLabelAndGradeCell(HtmlTableRow dismissalRow) {
            generateCellsWithText(dismissalRow, COLUMNS_BETWEEN_TEXT_AND_GRADE, EMPTY_INFO,
                    new String[] { renderer.getDegreeCurricularPlanCellClass(), renderer.getEnrolmentTypeCellClass(),
                            renderer.getEnrolmentStateCellClass() });

        }

        protected void generateCellsWithText(final HtmlTableRow row, final int numberOfCells, final String text,
                final String[] cssClasses) {
            for (int i = 0; i < numberOfCells; i++) {
                generateCellWithText(row, EMPTY_INFO, cssClasses[i]);
            }
        }

        protected void generateDismissalEctsCell(HtmlTableRow dismissalRow, Dismissal dismissal) {
            generateCellWithText(dismissalRow,
                    dismissal.getEctsCredits() != null ? dismissal.getEctsCredits().toString() : EMPTY_INFO,
                    renderer.getEctsCreditsCellClass());
        }

        protected void generateDismissalApprovementlDateIfRequired(HtmlTableRow enrolmentRow, YearMonthDay approvementDate) {
            if (isViewerAllowedToViewFullStudentCurriculum(studentCurricularPlan)) {
                if (approvementDate != null) {
                    generateCellWithSpan(enrolmentRow, approvementDate.toString(DATE_FORMAT),
                            BundleUtil.getString(Bundle.APPLICATION, "label.data.avaliacao"),
                            renderer.getCreationDateCellClass());
                } else {
                    generateCellWithText(enrolmentRow, EMPTY_INFO, renderer.getCreationDateCellClass());
                }
            }
        }

        protected void generateEvaluationDateIfRequired(HtmlTableRow externalEnrolmentRow, YearMonthDay evaluationDate) {
            if (isViewerAllowedToViewFullStudentCurriculum(studentCurricularPlan)) {
                if (evaluationDate != null) {
                    generateCellWithSpan(externalEnrolmentRow, evaluationDate.toString(DATE_FORMAT),
                            BundleUtil.getString(Bundle.APPLICATION, "creationDate"), renderer.getCreationDateCellClass());
                } else {
                    generateCellWithText(externalEnrolmentRow, EMPTY_INFO, renderer.getCreationDateCellClass());
                }
            }
        }

        protected void generateCreatorIfRequired(HtmlTableRow enrolmentRow, String createdBy) {
            if (isViewerAllowedToViewFullStudentCurriculum(studentCurricularPlan)) {
                if (!StringUtils.isEmpty(createdBy)) {
                    generateCellWithSpan(enrolmentRow, createdBy, BundleUtil.getString(Bundle.APPLICATION, "creator"),
                            renderer.getCreatorCellClass());
                } else {
                    generateCellWithText(enrolmentRow, EMPTY_INFO, renderer.getCreatorCellClass());
                }
            }
        }

        protected void generateDismissalLabelCell(final HtmlTable mainTable, HtmlTableRow dismissalRow, Dismissal dismissal,
                int level) {
            // if (dismissal.hasCurricularCourse() || loggedPersonIsManager()) {
            final HtmlTableCell cell = dismissalRow.createCell();
            cell.setColspan(MAX_COL_SPAN_FOR_TEXT_ON_CURRICULUM_LINES - level);
            cell.setClasses(renderer.getLabelCellClass());
            final HtmlInlineContainer container = new HtmlInlineContainer();
            cell.setBody(container);

            if (renderer.isSelectable()) {
                final HtmlCheckBox checkBox = new HtmlCheckBox();
                checkBox.setName(renderer.getSelectionName());
                checkBox.setUserValue(dismissal.getExternalId().toString());
                container.addChild(checkBox);
            }

            final HtmlText text = new HtmlText(
                    BundleUtil.getString(Bundle.STUDENT, "label.dismissal." + dismissal.getCredits().getClass().getSimpleName()));
            container.addChild(text);

            final CurricularCourse curricularCourse = dismissal.getCurricularCourse();
            if (curricularCourse != null) {

                String codeAndName = "";
                if (!StringUtils.isEmpty(curricularCourse.getCode())) {
                    codeAndName += curricularCourse.getCode() + " - ";
                }
                codeAndName += dismissal.getName().getContent();

                ExecutionCourse executionCourse = dismissal.getCurricularCourse()
                        .getExecutionCoursesByExecutionPeriod(dismissal.getExecutionInterval()).stream().findAny().orElse(null);

                final HtmlComponent executionCourseLink = createExecutionCourseLink(codeAndName, executionCourse);

                container.addChild(new HtmlText(": "));
                container.addChild(executionCourseLink);

            }

            // } else {
            // generateCellWithText(dismissalRow,
            // BundleUtil.getString(Bundle.STUDENT, "label.dismissal." +
            // dismissal.getCredits().getClass().getSimpleName()),
            // getLabelCellClass(),
            // MAX_COL_SPAN_FOR_TEXT_ON_CURRICULUM_LINES - level);
            // }

            if (renderer.isDetailed()) {
                generateDismissalDetails(mainTable, dismissal, level);
            }

        }

        protected void generateDismissalDetails(final HtmlTable mainTable, Dismissal dismissal, int level) {
            for (final IEnrolment enrolment : dismissal.getSourceIEnrolments()) {
                if (enrolment.isExternalEnrolment()) {
                    generateExternalEnrolmentRow(mainTable, (ExternalEnrolment) enrolment, level + 1, true);
                } else {
                    generateEnrolmentRow(mainTable, (Enrolment) enrolment, level + 1, false, true, true);
                }
            }
        }

        protected void generateExternalEnrolmentRow(HtmlTable mainTable, ExternalEnrolment externalEnrolment, int level,
                boolean isFromDetail) {

            final HtmlTableRow externalEnrolmentRow = mainTable.createRow();
            externalEnrolmentRow.setClasses(renderer.getEnrolmentRowClass());
            addTabsToRow(externalEnrolmentRow, level);

            generateExternalEnrolmentLabelCell(externalEnrolmentRow, externalEnrolment, level);
            generateCellsBetweenLabelAndGradeCell(externalEnrolmentRow);
            generateEnrolmentGradeCell(externalEnrolmentRow, externalEnrolment);
            generateEnrolmentWeightCell(externalEnrolmentRow, externalEnrolment, isFromDetail);
            generateExternalEnrolmentEctsCell(externalEnrolmentRow, externalEnrolment);
            generateCellWithText(externalEnrolmentRow, EMPTY_INFO, renderer.getLastEnrolmentEvaluationTypeCellClass());
            generateExecutionYearCell(externalEnrolmentRow, externalEnrolment);
            generateSemesterCell(externalEnrolmentRow, externalEnrolment);
            generateEvaluationDateIfRequired(externalEnrolmentRow, externalEnrolment.getEvaluationDate());
            generateCreatorIfRequired(externalEnrolmentRow, externalEnrolment.getCreatedBy());
            generateSpacerCellsIfRequired(externalEnrolmentRow);

        }

        protected void generateExternalEnrolmentEctsCell(HtmlTableRow externalEnrolmentRow, ExternalEnrolment externalEnrolment) {
            generateCellWithText(externalEnrolmentRow, externalEnrolment.getEctsCredits().toString(),
                    renderer.getEctsCreditsCellClass());
        }

        protected void generateExternalEnrolmentLabelCell(final HtmlTableRow externalEnrolmentRow,
                final ExternalEnrolment externalEnrolment, final int level) {
            generateCellWithText(externalEnrolmentRow, externalEnrolment.getDescription(), renderer.getLabelCellClass(),
                    MAX_COL_SPAN_FOR_TEXT_ON_CURRICULUM_LINES - level);
        }

        protected void generateEnrolmentRows(HtmlTable mainTable, List<Enrolment> childEnrolments, int level) {
            final Set<Enrolment> sortedEnrolments =
                    new TreeSet<Enrolment>(Enrolment.COMPARATOR_BY_EXECUTION_PERIOD_AND_NAME_AND_ID);
            sortedEnrolments.addAll(childEnrolments);

            for (final Enrolment enrolment : sortedEnrolments) {
                if (renderer.isToShowAllEnrolmentStates()) {
                    generateEnrolmentRow(mainTable, enrolment, level, true, false, false);
                } else if (renderer.isToShowApprovedOnly()) {
                    if (enrolment.isApproved()) {
                        generateEnrolmentRow(mainTable, enrolment, level, true, false, false);
                    }
                } else if (renderer.isToShowApprovedOrEnroledStatesOnly()) {
                    if (enrolment.isApproved() || enrolment.isEnroled()) {
                        generateEnrolmentRow(mainTable, enrolment, level, true, false, false);
                    }
                } else {
                    throw new RuntimeException("Unexpected enrolment state filter type");
                }
            }
        }

        protected void generateEnrolmentRow(HtmlTable mainTable, Enrolment enrolment, int level, boolean allowSelection,
                boolean isFromDetail, boolean isDismissal) {
            final HtmlTableRow enrolmentRow = mainTable.createRow();
            addTabsToRow(enrolmentRow, level);
            enrolmentRow.setClasses(renderer.getEnrolmentRowClass());

            if (enrolment.isEnroled()) {
                generateEnrolmentWithStateEnroled(enrolmentRow, enrolment, level, allowSelection);
            } else {
                generateCurricularCourseCodeAndNameCell(enrolmentRow, enrolment, level, allowSelection);
                generateDegreeCurricularPlanCell(enrolmentRow, enrolment);
                generateEnrolmentTypeCell(enrolmentRow, enrolment);
                generateEnrolmentStateCell(enrolmentRow, enrolment);
                generateEnrolmentGradeCell(enrolmentRow, enrolment);
                generateEnrolmentWeightCell(enrolmentRow, enrolment, isFromDetail);
                generateEnrolmentEctsCell(enrolmentRow, enrolment, isFromDetail);
                generateEnrolmentLastEnrolmentEvaluationTypeCell(enrolmentRow, enrolment);
                generateExecutionYearCell(enrolmentRow, enrolment);
                generateSemesterCell(enrolmentRow, enrolment);
                generateStatisticsLinkCell(enrolmentRow, enrolment);
                generateLastEnrolmentEvaluationExamDateCellIfRequired(enrolmentRow, enrolment);
                generateGradeResponsibleIfRequired(enrolmentRow, enrolment);
                generateSpacerCellsIfRequired(enrolmentRow);
            }

            if (!isDismissal && renderer.isDetailed() && isViewerAllowedToViewFullStudentCurriculum(studentCurricularPlan)
                    && enrolment.getAllFinalEnrolmentEvaluations().size() > 1) {
                EvaluationSeason.all().sorted()
                        .forEachOrdered(s -> enrolment.getFinalEnrolmentEvaluationBySeason(s).ifPresent(eval -> {
                            generateEnrolmentEvaluationRows(mainTable, eval, level + 1);
                        }));
            }
        }

        /**
         * List the enrollment evaluations bounded to an enrollment
         *
         * @param mainTable - Main HTML Table
         * @param evaluation - List of enrollment evaluations
         * @param level - The level of the evaluation rows
         */

        protected void generateEnrolmentEvaluationRows(HtmlTable mainTable, EnrolmentEvaluation evaluation, int level) {

            if (evaluation == null) {
                return;
            }

            final HtmlTableRow enrolmentRow = mainTable.createRow();

            addTabsToRow(enrolmentRow, level);
            enrolmentRow.setClasses(renderer.getEnrolmentRowClass());

            generateCellWithText(enrolmentRow, evaluation.getEvaluationSeason().getName().getContent(),
                    renderer.getLabelCellClass(), MAX_COL_SPAN_FOR_TEXT_ON_CURRICULUM_LINES - level);
            generateCellWithText(enrolmentRow, "", renderer.getEnrolmentTypeCellClass(),
                    ENROLLMENT_EVALUATION_TYPE_NEXT_COLUMN_SPAN);

            final Grade grade = evaluation.getGrade();
            generateCellWithText(enrolmentRow, grade.isEmpty() ? EMPTY_INFO : grade.getValue(), renderer.getGradeCellClass());

            generateCellWithText(enrolmentRow, "", renderer.getEctsCreditsCellClass(), GRADE_NEXT_COLUMN_SPAN);

            if (evaluation.getExecutionInterval() != null) {
                generateCellWithText(enrolmentRow, evaluation.getExecutionInterval().getExecutionYear().getYear(),
                        renderer.getEnrolmentExecutionYearCellClass());
                generateCellWithText(enrolmentRow,
                        evaluation.getExecutionInterval().getChildOrder().toString() + " "
                                + BundleUtil.getString(Bundle.APPLICATION, "label.semester.short"),
                        renderer.getEnrolmentSemesterCellClass());
            } else {
                generateCellWithText(enrolmentRow, EMPTY_INFO, renderer.getEnrolmentSemesterCellClass());
                generateCellWithText(enrolmentRow, EMPTY_INFO, renderer.getEnrolmentSemesterCellClass());
            }

            if (evaluation != null && evaluation.getExamDateYearMonthDay() != null) {
                generateCellWithSpan(enrolmentRow, evaluation.getExamDateYearMonthDay().toString(DATE_FORMAT),
                        BundleUtil.getString(Bundle.APPLICATION, "label.data.avaliacao"), renderer.getCreationDateCellClass());
            } else {
                generateCellWithText(enrolmentRow, EMPTY_INFO, renderer.getCreationDateCellClass());
            }

            if (evaluation.getPersonResponsibleForGrade() != null) {
                final Person person = evaluation.getPersonResponsibleForGrade();
                final String username = person.getUsername();
                generateCellWithSpan(enrolmentRow, username,
                        BundleUtil.getString(Bundle.APPLICATION, "label.grade.responsiblePerson"),
                        renderer.getCreatorCellClass());
            } else {
                generateCellWithText(enrolmentRow, EMPTY_INFO, renderer.getCreatorCellClass());
            }
        }

        protected void generateEnrolmentWithStateEnroled(HtmlTableRow enrolmentRow, Enrolment enrolment, int level,
                boolean allowSelection) {
            generateCurricularCourseCodeAndNameCell(enrolmentRow, enrolment, level, allowSelection);
            generateDegreeCurricularPlanCell(enrolmentRow, enrolment);
            generateEnrolmentTypeCell(enrolmentRow, enrolment);
            generateEnrolmentStateCell(enrolmentRow, enrolment);
            generateCellWithText(enrolmentRow, EMPTY_INFO, renderer.getGradeCellClass()); // grade
            generateCellWithText(enrolmentRow, EMPTY_INFO, renderer.getWeightCellClass()); // weight
            generateEnrolmentEctsCell(enrolmentRow, enrolment, false);
            generateEnrolmentEvaluationTypeCell(enrolmentRow, enrolment);
            generateExecutionYearCell(enrolmentRow, enrolment);
            generateSemesterCell(enrolmentRow, enrolment);
            if (isViewerAllowedToViewFullStudentCurriculum(studentCurricularPlan)) {
                generateCellWithText(enrolmentRow, EMPTY_INFO, renderer.getCreationDateCellClass()); // enrolment
                // evaluation
                // date
                generateCellWithText(enrolmentRow, EMPTY_INFO, renderer.getCreatorCellClass()); // grade
                // responsible
            }
            generateSpacerCellsIfRequired(enrolmentRow);
        }

        protected void generateGradeResponsibleIfRequired(HtmlTableRow enrolmentRow, Enrolment enrolment) {
            if (isViewerAllowedToViewFullStudentCurriculum(studentCurricularPlan)) {
                final EnrolmentEvaluation lastEnrolmentEvaluation = enrolment.getFinalEnrolmentEvaluation();
                if (lastEnrolmentEvaluation != null && lastEnrolmentEvaluation.getPersonResponsibleForGrade() != null) {

                    final Person person = lastEnrolmentEvaluation.getPersonResponsibleForGrade();
                    final String username = person.getUsername();
                    generateCellWithSpan(enrolmentRow, username,
                            BundleUtil.getString(Bundle.APPLICATION, "label.grade.responsiblePerson"),
                            renderer.getCreatorCellClass());

                } else {
                    generateCellWithText(enrolmentRow, EMPTY_INFO, renderer.getCreatorCellClass());
                }
            }

        }

        protected void generateLastEnrolmentEvaluationExamDateCellIfRequired(HtmlTableRow enrolmentRow, Enrolment enrolment) {
            if (isViewerAllowedToViewFullStudentCurriculum(studentCurricularPlan)) {
                final EnrolmentEvaluation lastEnrolmentEvaluation = enrolment.getFinalEnrolmentEvaluation();
                if (lastEnrolmentEvaluation != null && lastEnrolmentEvaluation.getExamDateYearMonthDay() != null) {

                    generateCellWithSpan(enrolmentRow, lastEnrolmentEvaluation.getExamDateYearMonthDay().toString(DATE_FORMAT),
                            BundleUtil.getString(Bundle.APPLICATION, "label.data.avaliacao"),
                            renderer.getCreationDateCellClass());
                } else {
                    generateCellWithText(enrolmentRow, EMPTY_INFO, renderer.getCreationDateCellClass());
                }
            }
        }

        protected void generateSpacerCellsIfRequired(final HtmlTableRow row) {
            final int spacerColspan = calculateSpacerColspan();
            if (spacerColspan > 0) {
                final HtmlTableCell spaceCells = row.createCell();
                spaceCells.setColspan(spacerColspan);
                spaceCells.setText("");
            }
        }

        protected int calculateSpacerColspan() {
            return MAX_LINE_SIZE - MAX_COL_SPAN_FOR_TEXT_ON_CURRICULUM_LINES - COLUMNS_BETWEEN_TEXT_AND_ENROLMENT_EVALUATION_DATE
                    - (isViewerAllowedToViewFullStudentCurriculum(
                            studentCurricularPlan) ? LATEST_ENROLMENT_EVALUATION_COLUMNS : 0);
        }

        protected void generateSemesterCell(final HtmlTableRow row, final ICurriculumEntry entry) {
            final String semester;
            if (entry.getExecutionInterval() != null) {
                semester = entry.getExecutionInterval().getChildOrder().toString() + " "
                        + BundleUtil.getString(Bundle.APPLICATION, "label.semester.short");
            } else {
                semester = EMPTY_INFO;
            }

            generateCellWithText(row, semester, renderer.getEnrolmentSemesterCellClass());
        }

        protected void generateStatisticsLinkCell(final HtmlTableRow row, final Enrolment enrolment) {
            if (enrolment.getStudent() == AccessControl.getPerson().getStudent() && enrolment.getRegistration().isActive()) {
                ExecutionCourse executionCourse = enrolment.getExecutionCourseFor(enrolment.getExecutionPeriod());
                if (executionCourse != null) {
                    final HtmlInlineContainer inlineContainer = new HtmlInlineContainer();
                    inlineContainer.addChild(createExecutionCourseStatisticsLink(
                            BundleUtil.getString(Bundle.APPLICATION, "label.statistics"), executionCourse));
                    final HtmlTableCell cell = row.createCell();
                    cell.setClasses(renderer.getStatisticsLinkCellClass());
                    cell.setBody(inlineContainer);
                }
            }
        }

        protected void generateExecutionYearCell(HtmlTableRow row, final ICurriculumEntry entry) {
            generateCellWithText(row, entry.getExecutionInterval() != null ? entry.getExecutionYear().getYear() : EMPTY_INFO,
                    renderer.getEnrolmentExecutionYearCellClass());
        }

        protected void generateEnrolmentLastEnrolmentEvaluationTypeCell(HtmlTableRow enrolmentRow, Enrolment enrolment) {
            final EnrolmentEvaluation lastEnrolmentEvaluation = enrolment.getFinalEnrolmentEvaluation();
            if (lastEnrolmentEvaluation != null && lastEnrolmentEvaluation.getEvaluationSeason() != null) {
                generateCellWithSpan(enrolmentRow, lastEnrolmentEvaluation.getEvaluationSeason().getAcronym().getContent(),
                        renderer.getLastEnrolmentEvaluationTypeCellClass());
            } else {
                generateCellWithText(enrolmentRow, EMPTY_INFO, renderer.getLastEnrolmentEvaluationTypeCellClass());
            }

        }

        protected void generateEnrolmentEvaluationTypeCell(HtmlTableRow enrolmentRow, Enrolment enrolment) {
            final EvaluationSeason season = enrolment.getEvaluationSeason();
            if (season != null) {
                generateCellWithSpan(enrolmentRow, season.getAcronym().getContent(),
                        renderer.getLastEnrolmentEvaluationTypeCellClass());
            } else {
                generateCellWithText(enrolmentRow, EMPTY_INFO, renderer.getLastEnrolmentEvaluationTypeCellClass());
            }

        }

        protected void generateEnrolmentEctsCell(final HtmlTableRow enrolmentRow, final Enrolment enrolment,
                final boolean isFromDetail) {
            final String ectsCredits =
                    String.valueOf(isFromDetail ? enrolment.getEctsCreditsForCurriculum() : enrolment.getEctsCredits());
            generateCellWithText(enrolmentRow, ectsCredits, renderer.getEctsCreditsCellClass());
        }

        protected void generateEnrolmentWeightCell(HtmlTableRow enrolmentRow, IEnrolment enrolment, boolean isFromDetail) {
            // Weight is only relevant to show when enrolment has numeric value
            final String weight;
            if (enrolment.getGrade() != null && !enrolment.getGrade().isEmpty()) {
                weight = String.valueOf(isFromDetail ? enrolment.getWeigthForCurriculum() : enrolment.getWeigth());
            } else {
                weight = EMPTY_INFO;
            }

            generateCellWithText(enrolmentRow, weight, renderer.getWeightCellClass());
        }

        protected void generateEnrolmentGradeCell(HtmlTableRow enrolmentRow, IEnrolment enrolment) {
            final Grade grade = enrolment.getGrade();
            generateCellWithText(enrolmentRow, grade.isEmpty() ? EMPTY_INFO : grade.getValue(), renderer.getGradeCellClass());
        }

        protected void generateEnrolmentStateCell(HtmlTableRow enrolmentRow, Enrolment enrolment) {
            generateCellWithText(enrolmentRow, enrolment.isApproved() ? EMPTY_INFO : BundleUtil.getString(Bundle.ENUMERATION,
                    enrolment.getEnrollmentState().getQualifiedName()), renderer.getEnrolmentStateCellClass());

        }

        protected void generateEnrolmentTypeCell(HtmlTableRow enrolmentRow, Enrolment enrolment) {
            generateCellWithText(enrolmentRow, enrolment.isEnrolmentTypeNormal() ? EMPTY_INFO : BundleUtil
                    .getString(Bundle.ENUMERATION, enrolment.getEnrolmentTypeName()), renderer.getEnrolmentTypeCellClass());
        }

        protected void generateDegreeCurricularPlanCell(final HtmlTableRow enrolmentRow, final Enrolment enrolment) {

            if (enrolment.isFor(studentCurricularPlan.getRegistration())) {
                generateCellWithText(enrolmentRow, EMPTY_INFO, renderer.getDegreeCurricularPlanCellClass());
            } else {
                final HtmlTableCell cell = enrolmentRow.createCell();
                cell.setClasses(renderer.getDegreeCurricularPlanCellClass());
                cell.setBody(createDegreeCurricularPlanNameLink(enrolment.getDegreeCurricularPlanOfDegreeModule(),
                        enrolment.getExecutionInterval()));
            }

        }

        protected HtmlComponent createDegreeCurricularPlanNameLink(final DegreeCurricularPlan degreeCurricularPlan,
                ExecutionInterval executionInterval) {
            final String siteUrl = degreeCurricularPlan.getDegree().getSiteUrl();

            if (Strings.isNullOrEmpty(siteUrl)) {
                return new HtmlText(degreeCurricularPlan.getName());
            } else {
                final HtmlLink result = new HtmlLink();
                result.setText(degreeCurricularPlan.getName());
                result.setModuleRelative(false);
                result.setContextRelative(false);
                result.setTarget("_blank");
                result.setUrl(siteUrl);
                return result;
            }
        }

        protected void generateCurricularCourseCodeAndNameCell(final HtmlTableRow enrolmentRow, final Enrolment enrolment,
                final int level, boolean allowSelection) {

            final HtmlInlineContainer inlineContainer = new HtmlInlineContainer();

            if (renderer.isSelectable() && allowSelection) {
                final HtmlCheckBox checkBox = new HtmlCheckBox();
                checkBox.setName(renderer.getSelectionName());
                checkBox.setUserValue(enrolment.getExternalId().toString());
                inlineContainer.addChild(checkBox);
            }

            ExecutionCourse executionCourse = enrolment.getExecutionCourseFor(enrolment.getExecutionPeriod());

            final HtmlComponent executionCourseLink =
                    createExecutionCourseLink(getPresentationNameFor(enrolment), executionCourse);

            inlineContainer.addChild(executionCourseLink);

            final HtmlTableCell cell = enrolmentRow.createCell();
            cell.setClasses(renderer.getLabelCellClass());
            cell.setColspan(MAX_COL_SPAN_FOR_TEXT_ON_CURRICULUM_LINES - level);
            cell.setBody(inlineContainer);
        }

        protected String getPresentationNameFor(final Enrolment enrolment) {
            final String code =
                    !StringUtils.isEmpty(enrolment.getCurricularCourse().getCode()) ? enrolment.getCurricularCourse().getCode()
                            + " - " : "";

            if (enrolment instanceof OptionalEnrolment) {
                final OptionalEnrolment optionalEnrolment = (OptionalEnrolment) enrolment;
                return optionalEnrolment.getOptionalCurricularCourse().getName() + " (" + code
                        + optionalEnrolment.getCurricularCourse().getName(optionalEnrolment.getExecutionInterval()) + ")";
            } else {
                return code + enrolment.getName().getContent();
            }
        }

        protected HtmlComponent createExecutionCourseLink(final String text, final ExecutionCourse executionCourse) {

            if (executionCourse != null && executionCourse.getSiteUrl() != null) {
                final HtmlLink result = new HtmlLink();
                result.setText(text);
                result.setModuleRelative(false);
                result.setContextRelative(false);
                result.setTarget(HtmlLink.Target.BLANK);
                result.setUrl(executionCourse.getSiteUrl());
                return result;
            }

            return new HtmlText(text);
        }

        protected HtmlComponent createProgramConclusionLink(final String text, final Registration registration,
                final ProgramConclusion programConclusion) {

            if (registration != null && programConclusion != null) {
                boolean canManageConclusion = AcademicAuthorizationGroup
                        .get(AcademicOperationType.MANAGE_CONCLUSION, registration.getDegree()).isMember(Authenticate.getUser())
                        || AcademicPermissionService.hasAccess("ACADEMIC_OFFICE_CONCLUSION", registration.getDegree(),
                                Authenticate.getUser());
                if (canManageConclusion) {
                    final HtmlLink result = new HtmlLink();
                    result.setText(text);
                    result.setModuleRelative(false);
                    result.setTarget(HtmlLink.Target.BLANK);
                    result.setUrl("/academicAdministration/registration.do");
                    result.setParameter("method", "selectProgramConclusion");
                    result.setParameter("registration", registration.getExternalId());
                    result.setParameter("programConclusion", programConclusion.getExternalId());
                    return result;
                }
            }

            return new HtmlText(text, false);
        }

        protected HtmlLink createExecutionCourseStatisticsLink(final String text, final ExecutionCourse executionCourse) {
            final HtmlLink result = new HtmlLink();
            result.setBody(new HtmlText(text));
            result.setParameter("executionCourseId", executionCourse.getExternalId());
            result.setParameter("method", "showExecutionCourseStatistics");
            result.setModuleRelative(false);
            result.setUrl("/student/showStudentStatistics.do");
            return result;
        }

        protected void generateChildGroupRows(HtmlTable mainTable, CurriculumGroup parentGroup, int level) {
            final Set<CurriculumGroup> sortedCurriculumGroups =
                    new TreeSet<CurriculumGroup>(CurriculumGroup.COMPARATOR_BY_CHILD_ORDER_AND_ID);

            sortedCurriculumGroups.addAll(parentGroup.getCurriculumGroups());

            for (final CurriculumGroup childGroup : sortedCurriculumGroups) {
                if (canGenerate(childGroup, studentCurricularPlan)) {
                    generateRowsForGroupsOrganization(mainTable, childGroup, level);
                }
            }
        }

        protected void addTabsToRow(final HtmlTableRow row, final int level) {
            for (int i = 0; i < level; i++) {
                HtmlLink link = new HtmlLink();
                link.setModuleRelative(false);
                link.setUrl(StudentCurricularPlanLayout.SPACER_IMAGE_PATH);

                final HtmlImage spacerImage = new HtmlImage();
                spacerImage.setSource(link.calculateUrl());

                final HtmlTableCell tabCell = row.createCell();
                tabCell.setClasses(renderer.getTabCellClass());
                tabCell.setBody(spacerImage);
            }
        }

        protected void generateHeadersForGradeWeightAndEctsCredits(final HtmlTableRow groupRow) {
            generateCellWithText(groupRow, BundleUtil.getString(Bundle.APPLICATION, "label.grade"), renderer.getGradeCellClass());

            generateCellWithText(groupRow, BundleUtil.getString(Bundle.APPLICATION, "label.weight"),
                    renderer.getWeightCellClass());

            generateCellWithText(groupRow, BundleUtil.getString(Bundle.APPLICATION, "label.ects"),
                    renderer.getEctsCreditsCellClass());

        }

        protected void generateCellWithText(final HtmlTableRow row, final String text, final String cssClass) {
            generateCellWithText(row, text, cssClass, 1);
        }

        protected void generateCellWithText(final HtmlTableRow row, final String text, final String cssClass, Integer colSpan) {
            final HtmlTableCell cell = row.createCell();
            cell.setClasses(cssClass);
            cell.setText(text);
            cell.setColspan(colSpan);
        }

        protected void generateCellWithSpan(final HtmlTableRow row, final String text, final String cssClass) {
            generateCellWithSpan(row, text, null, cssClass);
        }

        protected void generateCellWithSpan(final HtmlTableRow row, final String text, final String title,
                final String cssClass) {
            generateCellWithSpan(row, text, title, cssClass, 1);
        }

        protected void generateCellWithSpan(final HtmlTableRow row, final String text, final String title, final String cssClass,
                final Integer colSpan) {
            final HtmlTableCell cell = row.createCell();
            cell.setClasses(cssClass);
            cell.setColspan(colSpan);

            final HtmlInlineContainer span = new HtmlInlineContainer();
            span.addChild(new HtmlText(text));
            span.setTitle(title);

            cell.setBody(span);
        }
    }

    public static boolean canGenerate(final CurriculumGroup curriculumGroup, final StudentCurricularPlan studentCurricularPlan) {
        if (!curriculumGroup.isNoCourseGroupCurriculumGroup()
                || isViewerAllowedToViewFullStudentCurriculum(studentCurricularPlan)) {
            return true;
        }
        return ((NoCourseGroupCurriculumGroup) curriculumGroup).isVisible();
    }

    public static boolean isViewerAllowedToViewFullStudentCurriculum(final StudentCurricularPlan studentCurricularPlan) {
        final Person person = AccessControl.getPerson();
        final Degree degree = studentCurricularPlan.getDegree();
        Set<AcademicProgram> programs = AcademicAccessRule
                .getProgramsAccessibleToFunction(AcademicOperationType.VIEW_FULL_STUDENT_CURRICULUM, person.getUser())
                .collect(Collectors.toSet());
        programs.addAll(AcademicPermissionService.getDegrees("ACADEMIC_OFFICE_REGISTRATION_ACCESS", person.getUser()));
        return programs.stream().anyMatch(p -> p == degree);
    }

}
