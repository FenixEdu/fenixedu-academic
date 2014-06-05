/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.presentationTier.renderers.student.curriculum;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Grade;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.OptionalEnrolment;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.accessControl.AcademicAuthorizationGroup;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicOperationType;
import net.sourceforge.fenixedu.domain.curricularRules.CreditsLimit;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRuleType;
import net.sourceforge.fenixedu.domain.curriculum.EnrolmentEvaluationType;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.curriculum.ICurriculumEntry;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule.ConclusionValue;
import net.sourceforge.fenixedu.domain.studentCurriculum.Dismissal;
import net.sourceforge.fenixedu.domain.studentCurriculum.ExternalEnrolment;
import net.sourceforge.fenixedu.domain.studentCurriculum.NoCourseGroupCurriculumGroup;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.util.Bundle;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

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

    private boolean isToShowAllEnrolmentStates() {
        return this.enrolmentStateFilter == EnrolmentStateFilterType.ALL;
    }

    private boolean isToShowApprovedOnly() {
        return this.enrolmentStateFilter == EnrolmentStateFilterType.APPROVED;
    }

    private boolean isToShowApprovedOrEnroledStatesOnly() {
        return this.enrolmentStateFilter == EnrolmentStateFilterType.APPROVED_OR_ENROLED;
    }

    public void setViewType(final String type) {
        this.viewType = ViewType.valueOf(type);
    }

    public void setViewTypeEnum(final ViewType viewType) {
        this.viewType = viewType;
    }

    private boolean isToShowDismissals() {
        return this.viewType == ViewType.DISMISSALS || this.viewType == ViewType.ALL;
    }

    private boolean isToShowEnrolments() {
        return this.viewType == ViewType.ENROLMENTS || this.viewType == ViewType.ALL;
    }

    private String getStudentCurricularPlanClass() {
        return studentCurricularPlanClass;
    }

    public void setStudentCurricularPlanClass(String studentCurricularPlanClass) {
        this.studentCurricularPlanClass = studentCurricularPlanClass;
    }

    private String getCurriculumGroupRowClass() {
        return curriculumGroupRowClass;
    }

    public void setCurriculumGroupRowClass(String curriculumGroupRowClass) {
        this.curriculumGroupRowClass = curriculumGroupRowClass;
    }

    private String getEnrolmentRowClass() {
        return enrolmentRowClass;
    }

    public void setEnrolmentRowClass(String enrolmentRowClass) {
        this.enrolmentRowClass = enrolmentRowClass;
    }

    private String getDismissalRowClass() {
        return dismissalRowClass;
    }

    public void setDismissalRowClass(String dismissalRowClass) {
        this.dismissalRowClass = dismissalRowClass;
    }

    private String getTemporaryDismissalRowClass() {
        return temporaryDismissalRowClass;
    }

    public void setTemporaryDismissalRowClass(String temporaryDismissalRowClass) {
        this.temporaryDismissalRowClass = temporaryDismissalRowClass;
    }

    public void setCellClasses(String cellClasses) {
        this.cellClasses = cellClasses;
    }

    private String[] getCellClasses() {
        return this.cellClasses.split(",");
    }

    private String getTabCellClass() {
        return getCellClasses()[0];
    }

    private String getLabelCellClass() {
        return getCellClasses()[1];
    }

    private String getDegreeCurricularPlanCellClass() {
        return getCellClasses()[2];
    }

    private String getEnrolmentStateCellClass() {
        return getCellClasses()[3];
    }

    private String getEnrolmentTypeCellClass() {
        return getCellClasses()[4];
    }

    private String getGradeCellClass() {
        return getCellClasses()[5];
    }

    private String getWeightCellClass() {
        return getCellClasses()[6];
    }

    private String getEctsCreditsCellClass() {
        return getCellClasses()[7];
    }

    private String getLastEnrolmentEvaluationTypeCellClass() {
        return getCellClasses()[8];
    }

    private String getEnrolmentExecutionYearCellClass() {
        return getCellClasses()[9];
    }

    private String getEnrolmentSemesterCellClass() {
        return getCellClasses()[10];
    }

    private String getCreationDateCellClass() {
        return getCellClasses()[11];
    }

    private String getCreatorCellClass() {
        return getCellClasses()[12];
    }

    private String getStatisticsLinkCellClass() {
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

    @Override
    protected Layout getLayout(Object object, Class type) {
        return new StudentCurricularPlanLayout();
    }

    private class StudentCurricularPlanLayout extends Layout {

        private static final String EMPTY_INFO = "-";

        private static final String SPACER_IMAGE_PATH = "/images/scp_spacer.gif";

        private static final int MAX_LINE_SIZE = 26;

        private static final int MAX_COL_SPAN_FOR_TEXT_ON_GROUPS_WITH_CHILDS = 17;

        private static final int MAX_COL_SPAN_FOR_TEXT_ON_CURRICULUM_LINES = 14;

        private static final int HEADERS_SIZE = 3;

        private static final int COLUMNS_BETWEEN_TEXT_AND_GRADE = 3;

        private static final int COLUMNS_BETWEEN_TEXT_AND_ECTS = 5;

        private static final int COLUMNS_FROM_ECTS_AND_ENROLMENT_EVALUATION_DATE = 4;

        private static final int COLUMNS_BETWEEN_TEXT_AND_ENROLMENT_EVALUATION_DATE = COLUMNS_BETWEEN_TEXT_AND_ECTS
                + COLUMNS_FROM_ECTS_AND_ENROLMENT_EVALUATION_DATE;

        private static final int LATEST_ENROLMENT_EVALUATION_COLUMNS = 3;

        private static final String DATE_FORMAT = "yyyy/MM/dd";

        private static final int ENROLLMENT_EVALUATION_TYPE_NEXT_COLUMN_SPAN = 3;

        private static final int GRADE_NEXT_COLUMN_SPAN = 3;

        private StudentCurricularPlan studentCurricularPlan;

        private ExecutionYear executionYearContext;

        private ExecutionSemester executionPeriodContext;

        @Override
        public HtmlComponent createComponent(Object object, Class type) {
            final InputContext inputContext = getInputContext();
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

            if (isOrganizedByGroups() && !this.studentCurricularPlan.isBoxStructure()) {
                container.addChild(createHtmlTextItalic(BundleUtil.getString(Bundle.STUDENT, "not.applicable")));

                return container;
            }

            final HtmlTable mainTable = new HtmlTable();
            container.addChild(mainTable);
            mainTable.setClasses(getStudentCurricularPlanClass());

            if (isOrganizedByGroups()) {
                generateRowsForGroupsOrganization(mainTable, this.studentCurricularPlan.getRoot(), 0);
            } else if (isOrganizedByExecutionYears()) {
                generateRowsForExecutionYearsOrganization(mainTable);
            } else {
                throw new RuntimeException("Unexpected organization type");
            }

            return container;
        }

        private ExecutionYear initializeExecutionYear() {

            if (!studentCurricularPlan.getRegistration().hasConcluded()) {
                return ExecutionYear.readCurrentExecutionYear();
            }

            final ExecutionYear lastApprovementExecutionYear = studentCurricularPlan.getLastApprovementExecutionYear();
            if (lastApprovementExecutionYear != null) {
                return lastApprovementExecutionYear;
            }

            final ExecutionYear lastSCPExecutionYear = studentCurricularPlan.getLastExecutionYear();
            if (lastSCPExecutionYear != null) {
                return lastSCPExecutionYear;
            }

            return ExecutionYear.readCurrentExecutionYear();
        }

        private void generateRowsForExecutionYearsOrganization(HtmlTable mainTable) {

            if (isToShowEnrolments()) {
                final Set<ExecutionSemester> enrolmentExecutionPeriods =
                        new TreeSet<ExecutionSemester>(ExecutionSemester.COMPARATOR_BY_SEMESTER_AND_YEAR);
                enrolmentExecutionPeriods.addAll(this.studentCurricularPlan.getEnrolmentsExecutionPeriods());

                for (final ExecutionSemester enrolmentsExecutionPeriod : enrolmentExecutionPeriods) {
                    generateGroupRowWithText(mainTable,
                            enrolmentsExecutionPeriod.getYear() + ", " + enrolmentsExecutionPeriod.getName(), true, 0,
                            (CurriculumGroup) null);
                    generateEnrolmentRows(mainTable,
                            this.studentCurricularPlan.getEnrolmentsByExecutionPeriod(enrolmentsExecutionPeriod), 0);
                }
            }

            if (isToShowDismissals()) {
                final List<Dismissal> dismissals = this.studentCurricularPlan.getDismissals();
                if (!dismissals.isEmpty()) {
                    generateGroupRowWithText(mainTable, BundleUtil.getString(Bundle.STUDENT, "label.dismissals"), true, 0,
                            (CurriculumGroup) null);
                    generateDismissalRows(mainTable, dismissals, 0);
                }
            }

        }

        private HtmlText createHtmlTextItalic(final String message) {
            final HtmlText htmlText = new HtmlText(message);
            htmlText.setClasses("italic");

            return htmlText;
        }

        private void generateRowsForGroupsOrganization(final HtmlTable mainTable, final CurriculumGroup curriculumGroup,
                final int level) {

            generateGroupRowWithText(mainTable, curriculumGroup.getName().getContent(), curriculumGroup.hasCurriculumLines(),
                    level, curriculumGroup);
            generateCurriculumLineRows(mainTable, curriculumGroup, level + 1);
            generateChildGroupRows(mainTable, curriculumGroup, level + 1);
        }

        private void generateGroupRowWithText(final HtmlTable mainTable, final String text, boolean addHeaders, final int level,
                final CurriculumGroup curriculumGroup) {

            final HtmlTableRow groupRow = mainTable.createRow();
            groupRow.setClasses(getCurriculumGroupRowClass());
            addTabsToRow(groupRow, level);

            final HtmlTableCell cell = groupRow.createCell();
            cell.setClasses(getLabelCellClass());

            final HtmlComponent body;
            if (curriculumGroup != null && curriculumGroup.isRoot()) {
                body =
                        createDegreeCurricularPlanNameLink(curriculumGroup.getDegreeCurricularPlanOfDegreeModule(),
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

        private StringBuilder createGroupName(final String text, final CurriculumGroup curriculumGroup) {
            final StringBuilder groupName = new StringBuilder(text);
            if (curriculumGroup != null && curriculumGroup.hasDegreeModule()) {

                final CreditsLimit creditsLimit =
                        (CreditsLimit) curriculumGroup.getMostRecentActiveCurricularRule(CurricularRuleType.CREDITS_LIMIT,
                                executionYearContext);

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

                if (isViewerAdministrativeOfficeEmployeeOrManager(studentCurricularPlan)) {
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
                    groupName.append(")</span>");
                }

                if (isViewerAdministrativeOfficeEmployeeOrManager(studentCurricularPlan)
                        && studentCurricularPlan.isBolonhaDegree() && creditsLimit != null) {
                    final ConclusionValue value = curriculumGroup.isConcluded(executionYearContext);
                    groupName.append(" <em style=\"background-color:" + getBackgroundColor(value) + "; color:" + getColor(value)
                            + "\"");
                    groupName.append(">");
                    groupName.append(value.getLocalizedName());
                    groupName.append("</em>");
                }

            }
            return groupName;
        }

        private String getBackgroundColor(ConclusionValue value) {
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

        private String getColor(ConclusionValue value) {
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

        private void generateCurriculumLineRows(HtmlTable mainTable, CurriculumGroup curriculumGroup, int level) {

            if (isToShowDismissals()) {
                generateDismissalRows(mainTable, curriculumGroup.getChildDismissals(), level);
            }

            if (isToShowEnrolments()) {
                generateEnrolmentRows(mainTable, curriculumGroup.getChildEnrolments(), level);
            }
        }

        private void generateDismissalRows(HtmlTable mainTable, List<Dismissal> dismissals, int level) {
            final Set<Dismissal> sortedDismissals =
                    new TreeSet<Dismissal>(Dismissal.COMPARATOR_BY_EXECUTION_PERIOD_AND_NAME_AND_ID);
            sortedDismissals.addAll(dismissals);

            for (final Dismissal dismissal : sortedDismissals) {
                generateDismissalRow(mainTable, dismissal, level);
            }
        }

        private void generateDismissalRow(HtmlTable mainTable, Dismissal dismissal, int level) {
            final HtmlTableRow dismissalRow = mainTable.createRow();
            addTabsToRow(dismissalRow, level);
            dismissalRow
                    .setClasses(dismissal.getCredits().isTemporary() ? getTemporaryDismissalRowClass() : getDismissalRowClass());

            generateDismissalLabelCell(mainTable, dismissalRow, dismissal, level);
            generateCellsBetweenLabelAndGradeCell(dismissalRow);
            generateDismissalGradeCell(dismissalRow, dismissal);
            generateDismissalWeightCell(dismissalRow, dismissal);
            generateDismissalEctsCell(dismissalRow, dismissal);
            generateCellWithText(dismissalRow, EMPTY_INFO, getLastEnrolmentEvaluationTypeCellClass());
            generateExecutionYearCell(dismissalRow, dismissal);
            generateSemesterCell(dismissalRow, dismissal);
            generateCreationDateIfRequired(dismissalRow, dismissal.getCreationDateDateTime());
            generateCreatorIfRequired(dismissalRow, dismissal.getCreatedBy());
            generateSpacerCellsIfRequired(dismissalRow);
        }

        private void generateDismissalWeightCell(HtmlTableRow dismissalRow, Dismissal dismissal) {
            generateCellWithText(dismissalRow, EMPTY_INFO, getWeightCellClass());

        }

        private void generateDismissalGradeCell(HtmlTableRow dismissalRow, Dismissal dismissal) {
            final String gradeValue =
                    !StringUtils.isEmpty(dismissal.getCredits().getGivenGrade()) ? dismissal.getCredits().getGivenGrade() : null;
            final String gradeString;
            if (gradeValue != null && NumberUtils.isNumber(gradeValue)) {
                final DecimalFormat decimalFormat = new DecimalFormat("##.##");
                gradeString = decimalFormat.format(Double.valueOf(gradeValue));
            } else {
                gradeString = gradeValue != null ? gradeValue : EMPTY_INFO;
            }

            generateCellWithText(dismissalRow, gradeString, getGradeCellClass());
        }

        private void generateCellsBetweenLabelAndGradeCell(HtmlTableRow dismissalRow) {
            generateCellsWithText(dismissalRow, COLUMNS_BETWEEN_TEXT_AND_GRADE, EMPTY_INFO, new String[] {
                    getDegreeCurricularPlanCellClass(), getEnrolmentTypeCellClass(), getEnrolmentStateCellClass() });

        }

        private void generateCellsWithText(final HtmlTableRow row, final int numberOfCells, final String text,
                final String[] cssClasses) {
            for (int i = 0; i < numberOfCells; i++) {
                generateCellWithText(row, EMPTY_INFO, cssClasses[i]);
            }
        }

        private void generateDismissalEctsCell(HtmlTableRow dismissalRow, Dismissal dismissal) {
            generateCellWithText(dismissalRow,
                    dismissal.getEctsCredits() != null ? dismissal.getEctsCredits().toString() : EMPTY_INFO,
                    getEctsCreditsCellClass());
        }

        private void generateCreationDateIfRequired(HtmlTableRow enrolmentRow, DateTime creationDate) {
            if (isViewerAdministrativeOfficeEmployeeOrManager(studentCurricularPlan)) {
                if (creationDate != null) {
                    generateCellWithSpan(enrolmentRow, creationDate.toString(DATE_FORMAT),
                            BundleUtil.getString(Bundle.APPLICATION, "creationDate"), getCreationDateCellClass());
                } else {
                    generateCellWithText(enrolmentRow, EMPTY_INFO, getCreationDateCellClass());
                }
            }
        }

        private void generateEvaluationDateIfRequired(HtmlTableRow externalEnrolmentRow, YearMonthDay evaluationDate) {
            if (isViewerAdministrativeOfficeEmployeeOrManager(studentCurricularPlan)) {
                if (evaluationDate != null) {
                    generateCellWithSpan(externalEnrolmentRow, evaluationDate.toString(DATE_FORMAT),
                            BundleUtil.getString(Bundle.APPLICATION, "creationDate"), getCreationDateCellClass());
                } else {
                    generateCellWithText(externalEnrolmentRow, EMPTY_INFO, getCreationDateCellClass());
                }
            }
        }

        private void generateCreatorIfRequired(HtmlTableRow enrolmentRow, String createdBy) {
            if (isViewerAdministrativeOfficeEmployeeOrManager(studentCurricularPlan)) {
                if (!StringUtils.isEmpty(createdBy)) {
                    generateCellWithSpan(enrolmentRow, createdBy, BundleUtil.getString(Bundle.APPLICATION, "creator"),
                            getCreatorCellClass());
                } else {
                    generateCellWithText(enrolmentRow, EMPTY_INFO, getCreatorCellClass());
                }
            }
        }

        private void generateDismissalLabelCell(final HtmlTable mainTable, HtmlTableRow dismissalRow, Dismissal dismissal,
                int level) {
            // if (dismissal.hasCurricularCourse() || loggedPersonIsManager()) {
            final HtmlTableCell cell = dismissalRow.createCell();
            cell.setColspan(MAX_COL_SPAN_FOR_TEXT_ON_CURRICULUM_LINES - level);
            cell.setClasses(getLabelCellClass());
            final HtmlInlineContainer container = new HtmlInlineContainer();
            cell.setBody(container);

            if (isSelectable()) {
                final HtmlCheckBox checkBox = new HtmlCheckBox();
                checkBox.setName(getSelectionName());
                checkBox.setUserValue(dismissal.getExternalId().toString());
                container.addChild(checkBox);
            }

            final HtmlText text =
                    new HtmlText(BundleUtil.getString(Bundle.STUDENT, "label.dismissal."
                            + dismissal.getCredits().getClass().getSimpleName()));
            container.addChild(text);

            final CurricularCourse curricularCourse = dismissal.getCurricularCourse();
            if (curricularCourse != null) {

                String codeAndName = "";
                if (!StringUtils.isEmpty(curricularCourse.getCode())) {
                    codeAndName += curricularCourse.getCode() + " - ";
                }
                codeAndName += dismissal.getName().getContent();
                final HtmlLink curricularCourseLink = createCurricularCourseLink(codeAndName, curricularCourse);
                container.addChild(new HtmlText(": "));
                container.addChild(curricularCourseLink);
            }

            // } else {
            // generateCellWithText(dismissalRow,
            // BundleUtil.getString(Bundle.STUDENT, "label.dismissal." +
            // dismissal.getCredits().getClass().getSimpleName()),
            // getLabelCellClass(),
            // MAX_COL_SPAN_FOR_TEXT_ON_CURRICULUM_LINES - level);
            // }

            if (isDetailed()) {
                generateDismissalDetails(mainTable, dismissal, level);
            }

        }

        private void generateDismissalDetails(final HtmlTable mainTable, Dismissal dismissal, int level) {
            for (final IEnrolment enrolment : dismissal.getSourceIEnrolments()) {
                if (enrolment.isExternalEnrolment()) {
                    generateExternalEnrolmentRow(mainTable, (ExternalEnrolment) enrolment, level + 1, true);
                } else {
                    generateEnrolmentRow(mainTable, (Enrolment) enrolment, level + 1, false, true, true);
                }
            }
        }

        private void generateExternalEnrolmentRow(HtmlTable mainTable, ExternalEnrolment externalEnrolment, int level,
                boolean isFromDetail) {

            final HtmlTableRow externalEnrolmentRow = mainTable.createRow();
            externalEnrolmentRow.setClasses(getEnrolmentRowClass());
            addTabsToRow(externalEnrolmentRow, level);

            generateExternalEnrolmentLabelCell(externalEnrolmentRow, externalEnrolment, level);
            generateCellsBetweenLabelAndGradeCell(externalEnrolmentRow);
            generateEnrolmentGradeCell(externalEnrolmentRow, externalEnrolment);
            generateEnrolmentWeightCell(externalEnrolmentRow, externalEnrolment, isFromDetail);
            generateExternalEnrolmentEctsCell(externalEnrolmentRow, externalEnrolment);
            generateCellWithText(externalEnrolmentRow, EMPTY_INFO, getLastEnrolmentEvaluationTypeCellClass());
            generateExecutionYearCell(externalEnrolmentRow, externalEnrolment);
            generateSemesterCell(externalEnrolmentRow, externalEnrolment);
            generateEvaluationDateIfRequired(externalEnrolmentRow, externalEnrolment.getEvaluationDate());
            generateCreatorIfRequired(externalEnrolmentRow, externalEnrolment.getCreatedBy());
            generateSpacerCellsIfRequired(externalEnrolmentRow);

        }

        private void generateExternalEnrolmentEctsCell(HtmlTableRow externalEnrolmentRow, ExternalEnrolment externalEnrolment) {
            generateCellWithText(externalEnrolmentRow, externalEnrolment.getEctsCredits().toString(), getEctsCreditsCellClass());
        }

        private void generateExternalEnrolmentLabelCell(final HtmlTableRow externalEnrolmentRow,
                final ExternalEnrolment externalEnrolment, final int level) {
            generateCellWithText(externalEnrolmentRow, externalEnrolment.getDescription(), getLabelCellClass(),
                    MAX_COL_SPAN_FOR_TEXT_ON_CURRICULUM_LINES - level);
        }

        private void generateEnrolmentRows(HtmlTable mainTable, List<Enrolment> childEnrolments, int level) {
            final Set<Enrolment> sortedEnrolments =
                    new TreeSet<Enrolment>(Enrolment.COMPARATOR_BY_EXECUTION_PERIOD_AND_NAME_AND_ID);
            sortedEnrolments.addAll(childEnrolments);

            for (final Enrolment enrolment : sortedEnrolments) {
                if (isToShowAllEnrolmentStates()) {
                    generateEnrolmentRow(mainTable, enrolment, level, true, false, false);
                } else if (isToShowApprovedOnly()) {
                    if (enrolment.isApproved()) {
                        generateEnrolmentRow(mainTable, enrolment, level, true, false, false);
                    }
                } else if (isToShowApprovedOrEnroledStatesOnly()) {
                    if (enrolment.isApproved() || enrolment.isEnroled()) {
                        generateEnrolmentRow(mainTable, enrolment, level, true, false, false);
                    }
                } else {
                    throw new RuntimeException("Unexpected enrolment state filter type");
                }
            }
        }

        private void generateEnrolmentRow(HtmlTable mainTable, Enrolment enrolment, int level, boolean allowSelection,
                boolean isFromDetail, boolean isDismissal) {
            final HtmlTableRow enrolmentRow = mainTable.createRow();
            addTabsToRow(enrolmentRow, level);
            enrolmentRow.setClasses(getEnrolmentRowClass());

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

            if (!isDismissal && isDetailed() && isViewerAdministrativeOfficeEmployeeOrManager(studentCurricularPlan)
                    && (enrolment.isSpecialSeason() || enrolment.hasImprovement()) || enrolment.hasNormalEvaluationSecondSeason()) {
                generateEnrolmentEvaluationRows(mainTable, enrolment.getLatestFinalImprovementEnrolmentEvaluation(), level + 1);
                generateEnrolmentEvaluationRows(mainTable, enrolment.getLatestFinalSpecialSeasonEnrolmentEvaluation(), level + 1);
                generateEnrolmentEvaluationRows(mainTable, enrolment.getLatestFinalNormalEnrolmentEvaluationSecondSeason(),
                        level + 1);

                EnrolmentEvaluation firstSeasonEvaluation = enrolment.getLatestFinalNormalEnrolmentEvaluationFirstSeason();
                if (firstSeasonEvaluation == null) {
                    firstSeasonEvaluation = enrolment.getLatestFinalNormalEnrolmentEvaluation();
                }
                generateEnrolmentEvaluationRows(mainTable, firstSeasonEvaluation, level + 1);
            }
        }

        /**
         * List the enrollment evaluations bounded to an enrollment
         * 
         * @param mainTable
         *            - Main HTML Table
         * @param evaluations
         *            - List of enrollment evaluations
         * @param level
         *            - The level of the evaluation rows
         */

        private void generateEnrolmentEvaluationRows(HtmlTable mainTable, EnrolmentEvaluation evaluation, int level) {

            if (evaluation == null) {
                return;
            }

            final HtmlTableRow enrolmentRow = mainTable.createRow();

            addTabsToRow(enrolmentRow, level);
            enrolmentRow.setClasses(getEnrolmentRowClass());

            generateCellWithText(enrolmentRow, evaluation.getEnrolmentEvaluationTypeDescription(), getLabelCellClass(),
                    MAX_COL_SPAN_FOR_TEXT_ON_CURRICULUM_LINES - level);
            generateCellWithText(enrolmentRow, "", getEnrolmentTypeCellClass(), ENROLLMENT_EVALUATION_TYPE_NEXT_COLUMN_SPAN);

            final Grade grade = evaluation.getGrade();
            generateCellWithText(enrolmentRow, grade.isEmpty() ? EMPTY_INFO : grade.getValue(), getGradeCellClass());

            generateCellWithText(enrolmentRow, "", getEctsCreditsCellClass(), GRADE_NEXT_COLUMN_SPAN);

            if (evaluation.getExecutionPeriod() != null) {
                generateCellWithText(enrolmentRow, evaluation.getExecutionPeriod().getExecutionYear().getYear(),
                        getEnrolmentExecutionYearCellClass());
                generateCellWithText(enrolmentRow, evaluation.getExecutionPeriod().getSemester().toString() + " "
                        + BundleUtil.getString(Bundle.APPLICATION, "label.semester.short"), getEnrolmentSemesterCellClass());
            } else {
                generateCellWithText(enrolmentRow, EMPTY_INFO, getEnrolmentSemesterCellClass());
                generateCellWithText(enrolmentRow, EMPTY_INFO, getEnrolmentSemesterCellClass());
            }

            if (evaluation != null && evaluation.getExamDateYearMonthDay() != null) {
                generateCellWithSpan(enrolmentRow, evaluation.getExamDateYearMonthDay().toString(DATE_FORMAT),
                        BundleUtil.getString(Bundle.APPLICATION, "label.data.avaliacao"), getCreationDateCellClass());
            } else {
                generateCellWithText(enrolmentRow, EMPTY_INFO, getCreationDateCellClass());
            }

            if (evaluation.getPersonResponsibleForGrade() != null) {
                final Person person = evaluation.getPersonResponsibleForGrade();
                final String username = getUsername(person);
                generateCellWithSpan(enrolmentRow, username, BundleUtil.getString(Bundle.APPLICATION, "label.grade.responsiblePerson"),
                        getCreatorCellClass());
            } else {
                generateCellWithText(enrolmentRow, EMPTY_INFO, getCreatorCellClass());
            }
        }

        private void generateEnrolmentWithStateEnroled(HtmlTableRow enrolmentRow, Enrolment enrolment, int level,
                boolean allowSelection) {
            generateCurricularCourseCodeAndNameCell(enrolmentRow, enrolment, level, allowSelection);
            generateDegreeCurricularPlanCell(enrolmentRow, enrolment);
            generateEnrolmentTypeCell(enrolmentRow, enrolment);
            generateEnrolmentStateCell(enrolmentRow, enrolment);
            generateCellWithText(enrolmentRow, EMPTY_INFO, getGradeCellClass()); // grade
            generateCellWithText(enrolmentRow, EMPTY_INFO, getWeightCellClass()); // weight
            generateEnrolmentEctsCell(enrolmentRow, enrolment, false);
            generateEnrolmentEvaluationTypeCell(enrolmentRow, enrolment);
            generateExecutionYearCell(enrolmentRow, enrolment);
            generateSemesterCell(enrolmentRow, enrolment);
            if (isViewerAdministrativeOfficeEmployeeOrManager(studentCurricularPlan)) {
                generateCellWithText(enrolmentRow, EMPTY_INFO, getCreationDateCellClass()); // enrolment
                // evaluation
                // date
                generateCellWithText(enrolmentRow, EMPTY_INFO, getCreatorCellClass()); // grade
                // responsible
            }
            generateSpacerCellsIfRequired(enrolmentRow);
        }

        private void generateGradeResponsibleIfRequired(HtmlTableRow enrolmentRow, Enrolment enrolment) {
            if (isViewerAdministrativeOfficeEmployeeOrManager(studentCurricularPlan)) {
                final EnrolmentEvaluation lastEnrolmentEvaluation = enrolment.getLatestEnrolmentEvaluation();
                if (lastEnrolmentEvaluation != null && lastEnrolmentEvaluation.getPersonResponsibleForGrade() != null) {

                    final Person person = lastEnrolmentEvaluation.getPersonResponsibleForGrade();
                    final String username = getUsername(person);
                    generateCellWithSpan(enrolmentRow, username, BundleUtil.getString(Bundle.APPLICATION, "label.grade.responsiblePerson"),
                            getCreatorCellClass());

                } else {
                    generateCellWithText(enrolmentRow, EMPTY_INFO, getCreatorCellClass());
                }
            }

        }

        private String getUsername(final Person person) {
            return person.getUsername();
        }

        private void generateLastEnrolmentEvaluationExamDateCellIfRequired(HtmlTableRow enrolmentRow, Enrolment enrolment) {
            if (isViewerAdministrativeOfficeEmployeeOrManager(studentCurricularPlan)) {
                final EnrolmentEvaluation lastEnrolmentEvaluation = enrolment.getLatestEnrolmentEvaluation();
                if (lastEnrolmentEvaluation != null && lastEnrolmentEvaluation.getExamDateYearMonthDay() != null) {

                    generateCellWithSpan(enrolmentRow, lastEnrolmentEvaluation.getExamDateYearMonthDay().toString(DATE_FORMAT),
                            BundleUtil.getString(Bundle.APPLICATION, "label.data.avaliacao"), getCreationDateCellClass());
                } else {
                    generateCellWithText(enrolmentRow, EMPTY_INFO, getCreationDateCellClass());
                }
            }
        }

        private void generateSpacerCellsIfRequired(final HtmlTableRow row) {
            final int spacerColspan = calculateSpacerColspan();
            if (spacerColspan > 0) {
                final HtmlTableCell spaceCells = row.createCell();
                spaceCells.setColspan(spacerColspan);
                spaceCells.setText("");
            }
        }

        private int calculateSpacerColspan() {
            return MAX_LINE_SIZE
                    - MAX_COL_SPAN_FOR_TEXT_ON_CURRICULUM_LINES
                    - COLUMNS_BETWEEN_TEXT_AND_ENROLMENT_EVALUATION_DATE
                    - (isViewerAdministrativeOfficeEmployeeOrManager(studentCurricularPlan) ? LATEST_ENROLMENT_EVALUATION_COLUMNS : 0);
        }

        private void generateSemesterCell(final HtmlTableRow row, final ICurriculumEntry entry) {
            final String semester;
            if (entry.hasExecutionPeriod()) {
                semester =
                        entry.getExecutionPeriod().getSemester().toString() + " "
                                + BundleUtil.getString(Bundle.APPLICATION, "label.semester.short");
            } else {
                semester = EMPTY_INFO;
            }

            generateCellWithText(row, semester, getEnrolmentSemesterCellClass());
        }

        private void generateStatisticsLinkCell(final HtmlTableRow row, final Enrolment enrolment) {
            if (enrolment.getStudent() == AccessControl.getPerson().getStudent()
                    && enrolment.getStudent().hasAnyActiveRegistration()) {
                ExecutionCourse executionCourse = enrolment.getExecutionCourseFor(enrolment.getExecutionPeriod());
                if (executionCourse != null) {
                    final HtmlInlineContainer inlineContainer = new HtmlInlineContainer();
                    inlineContainer.addChild(createExecutionCourseStatisticsLink(
                            BundleUtil.getString(Bundle.APPLICATION, "label.statistics"), executionCourse));
                    final HtmlTableCell cell = row.createCell();
                    cell.setClasses(getStatisticsLinkCellClass());
                    cell.setBody(inlineContainer);
                }
            }
        }

        private void generateExecutionYearCell(HtmlTableRow row, final ICurriculumEntry entry) {
            generateCellWithText(row, entry.hasExecutionPeriod() ? entry.getExecutionYear().getYear() : EMPTY_INFO,
                    getEnrolmentExecutionYearCellClass());
        }

        private void generateEnrolmentLastEnrolmentEvaluationTypeCell(HtmlTableRow enrolmentRow, Enrolment enrolment) {
            final EnrolmentEvaluation lastEnrolmentEvaluation = enrolment.getLatestEnrolmentEvaluation();
            if (lastEnrolmentEvaluation != null && lastEnrolmentEvaluation.getEnrolmentEvaluationType() != null) {
                generateCellWithSpan(enrolmentRow, BundleUtil.getString(Bundle.ENUMERATION, lastEnrolmentEvaluation
                        .getEnrolmentEvaluationType().getAcronym()), getLastEnrolmentEvaluationTypeCellClass());
            } else {
                generateCellWithText(enrolmentRow, EMPTY_INFO, getLastEnrolmentEvaluationTypeCellClass());
            }

        }

        private void generateEnrolmentEvaluationTypeCell(HtmlTableRow enrolmentRow, Enrolment enrolment) {
            final EnrolmentEvaluationType enrolmentEvaluationType = enrolment.getEnrolmentEvaluationType();
            if (enrolmentEvaluationType != null) {
                generateCellWithSpan(enrolmentRow, BundleUtil.getString(Bundle.ENUMERATION, enrolmentEvaluationType.getAcronym()),
                        getLastEnrolmentEvaluationTypeCellClass());
            } else {
                generateCellWithText(enrolmentRow, EMPTY_INFO, getLastEnrolmentEvaluationTypeCellClass());
            }

        }

        private void generateEnrolmentEctsCell(final HtmlTableRow enrolmentRow, final Enrolment enrolment,
                final boolean isFromDetail) {
            final String ectsCredits =
                    String.valueOf(isFromDetail ? enrolment.getEctsCreditsForCurriculum() : enrolment.getEctsCredits());
            generateCellWithText(enrolmentRow, ectsCredits, getEctsCreditsCellClass());
        }

        private void generateEnrolmentWeightCell(HtmlTableRow enrolmentRow, IEnrolment enrolment, boolean isFromDetail) {
            // Weight is only relevant to show when enrolment has numeric value
            final String weight;
            if (enrolment.getFinalGrade() != null) {
                weight = String.valueOf(isFromDetail ? enrolment.getWeigthForCurriculum() : enrolment.getWeigth());
            } else {
                weight = EMPTY_INFO;
            }

            generateCellWithText(enrolmentRow, weight, getWeightCellClass());
        }

        private void generateEnrolmentGradeCell(HtmlTableRow enrolmentRow, IEnrolment enrolment) {
            final Grade grade = enrolment.getGrade();
            generateCellWithText(enrolmentRow, grade.isEmpty() ? EMPTY_INFO : grade.getValue(), getGradeCellClass());
        }

        private void generateEnrolmentStateCell(HtmlTableRow enrolmentRow, Enrolment enrolment) {
            generateCellWithText(
                    enrolmentRow,
                    enrolment.isApproved() ? EMPTY_INFO : BundleUtil.getString(Bundle.ENUMERATION, enrolment.getEnrollmentState()
                            .getQualifiedName()), getEnrolmentStateCellClass());

        }

        private void generateEnrolmentTypeCell(HtmlTableRow enrolmentRow, Enrolment enrolment) {
            generateCellWithText(
                    enrolmentRow,
                    enrolment.isEnrolmentTypeNormal() ? EMPTY_INFO : BundleUtil.getString(Bundle.ENUMERATION, enrolment
                            .getEnrolmentTypeName()), getEnrolmentTypeCellClass());
        }

        private void generateDegreeCurricularPlanCell(final HtmlTableRow enrolmentRow, final Enrolment enrolment) {

            if (enrolment.isFor(studentCurricularPlan.getRegistration())) {
                generateCellWithText(enrolmentRow, EMPTY_INFO, getDegreeCurricularPlanCellClass());
            } else {
                final HtmlTableCell cell = enrolmentRow.createCell();
                cell.setClasses(getDegreeCurricularPlanCellClass());
                cell.setBody(createDegreeCurricularPlanNameLink(enrolment.getDegreeCurricularPlanOfDegreeModule(),
                        enrolment.getExecutionPeriod()));
            }

        }

        private HtmlComponent createDegreeCurricularPlanNameLink(final DegreeCurricularPlan degreeCurricularPlan,
                ExecutionSemester executionSemester) {
            if (degreeCurricularPlan.isPast() || degreeCurricularPlan.isEmpty()) {
                return new HtmlText(degreeCurricularPlan.getName());
            }

            final HtmlLink result = new HtmlLink();

            result.setText(degreeCurricularPlan.getName());
            result.setModuleRelative(false);
            result.setTarget("_blank");

            if (degreeCurricularPlan.isBoxStructure()) {
                result.setUrl("/publico/degreeSite/showDegreeCurricularPlanBolonha.faces");

                result.setParameter("organizeBy", "groups");
                result.setParameter("showRules", "false");
                result.setParameter("hideCourses", "false");
            } else {
                result.setUrl("/publico/prepareConsultCurricularPlanNew.do");

                result.setParameter("method", "prepare");
                result.setParameter("degreeInitials", degreeCurricularPlan.getDegree().getSigla());
            }

            result.setParameter("degreeID", degreeCurricularPlan.getDegree().getExternalId().toString());
            result.setParameter("degreeCurricularPlanID", degreeCurricularPlan.getExternalId().toString());

            result.setParameter("executionPeriodOID", executionSemester.getExternalId().toString());

            return result;
        }

        private void generateCurricularCourseCodeAndNameCell(final HtmlTableRow enrolmentRow, final Enrolment enrolment,
                final int level, boolean allowSelection) {

            final HtmlInlineContainer inlineContainer = new HtmlInlineContainer();

            if (isSelectable() && allowSelection) {
                final HtmlCheckBox checkBox = new HtmlCheckBox();
                checkBox.setName(getSelectionName());
                checkBox.setUserValue(enrolment.getExternalId().toString());
                inlineContainer.addChild(checkBox);
            }

            inlineContainer.addChild(createCurricularCourseLink(getPresentationNameFor(enrolment),
                    enrolment.getCurricularCourse()));

            final HtmlTableCell cell = enrolmentRow.createCell();
            cell.setClasses(getLabelCellClass());
            cell.setColspan(MAX_COL_SPAN_FOR_TEXT_ON_CURRICULUM_LINES - level);
            cell.setBody(inlineContainer);
        }

        private String getPresentationNameFor(final Enrolment enrolment) {
            final String code =
                    !StringUtils.isEmpty(enrolment.getCurricularCourse().getCode()) ? enrolment.getCurricularCourse().getCode()
                            + " - " : "";

            if (enrolment instanceof OptionalEnrolment) {
                final OptionalEnrolment optionalEnrolment = (OptionalEnrolment) enrolment;
                return optionalEnrolment.getOptionalCurricularCourse().getName() + " (" + code
                        + optionalEnrolment.getCurricularCourse().getName(optionalEnrolment.getExecutionPeriod()) + ")";
            } else {
                return code + enrolment.getName().getContent();
            }
        }

        private HtmlLink createCurricularCourseLink(final String text, final CurricularCourse curricularCourse) {

            final HtmlLink result = new HtmlLink();
            result.setBody(new HtmlText(text));
            result.setModuleRelative(false);
            result.setTarget(HtmlLink.Target.BLANK);

            result.setParameter("degreeID", curricularCourse.getDegreeCurricularPlan().getDegree().getExternalId());
            result.setParameter("curricularCourseID", curricularCourse.getExternalId());
            result.setParameter("degreeCurricularPlanID", curricularCourse.getDegreeCurricularPlan().getExternalId());

            if (curricularCourse.isBolonhaDegree()) {
                result.setParameter("organizeBy", "groups");
                result.setParameter("showRules", "false");
                result.setParameter("hideCourses", "false");
                result.setUrl("/publico/degreeSite/viewCurricularCourse.faces");
            } else {
                result.setUrl("/publico/showCourseSite.do?method=showCurricularCourseSite");
            }

            return result;
        }

        private HtmlLink createExecutionCourseStatisticsLink(final String text, final ExecutionCourse executionCourse) {
            final HtmlLink result = new HtmlLink();
            result.setBody(new HtmlText(text));
            result.setParameter("executionCourseId", executionCourse.getExternalId());
            result.setParameter("method", "showExecutionCourseStatistics");
            result.setModuleRelative(false);
            result.setUrl("/student/showStudentStatistics.do");
            return result;
        }

        private void generateChildGroupRows(HtmlTable mainTable, CurriculumGroup parentGroup, int level) {
            final Set<CurriculumGroup> sortedCurriculumGroups =
                    new TreeSet<CurriculumGroup>(CurriculumGroup.COMPARATOR_BY_CHILD_ORDER_AND_ID);

            sortedCurriculumGroups.addAll(parentGroup.getCurriculumGroups());

            for (final CurriculumGroup childGroup : sortedCurriculumGroups) {
                if (canGenerate(childGroup, studentCurricularPlan)) {
                    generateRowsForGroupsOrganization(mainTable, childGroup, level);
                }
            }
        }

    }

    private boolean canGenerate(final CurriculumGroup curriculumGroup, final StudentCurricularPlan studentCurricularPlan) {
        if (!curriculumGroup.isNoCourseGroupCurriculumGroup()
                || isViewerAdministrativeOfficeEmployeeOrManager(studentCurricularPlan)) {
            return true;
        }
        return ((NoCourseGroupCurriculumGroup) curriculumGroup).isVisible();
    }

    private void addTabsToRow(final HtmlTableRow row, final int level) {
        for (int i = 0; i < level; i++) {
            HtmlLink link = new HtmlLink();
            link.setModuleRelative(false);
            link.setUrl(StudentCurricularPlanLayout.SPACER_IMAGE_PATH);

            final HtmlImage spacerImage = new HtmlImage();
            spacerImage.setSource(link.calculateUrl());

            final HtmlTableCell tabCell = row.createCell();
            tabCell.setClasses(getTabCellClass());
            tabCell.setBody(spacerImage);
        }
    }

    private void generateHeadersForGradeWeightAndEctsCredits(final HtmlTableRow groupRow) {
        generateCellWithText(groupRow, BundleUtil.getString(Bundle.APPLICATION, "label.grade"), getGradeCellClass());

        generateCellWithText(groupRow, BundleUtil.getString(Bundle.APPLICATION, "label.weight"), getWeightCellClass());

        generateCellWithText(groupRow, BundleUtil.getString(Bundle.APPLICATION, "label.ects"), getEctsCreditsCellClass());

    }

    private void generateCellWithText(final HtmlTableRow row, final String text, final String cssClass) {
        generateCellWithText(row, text, cssClass, 1);
    }

    private void generateCellWithText(final HtmlTableRow row, final String text, final String cssClass, Integer colSpan) {
        final HtmlTableCell cell = row.createCell();
        cell.setClasses(cssClass);
        cell.setText(text);
        cell.setColspan(colSpan);
    }

    private void generateCellWithSpan(final HtmlTableRow row, final String text, final String cssClass) {
        generateCellWithSpan(row, text, null, cssClass);
    }

    private void generateCellWithSpan(final HtmlTableRow row, final String text, final String title, final String cssClass) {
        generateCellWithSpan(row, text, title, cssClass, 1);
    }

    private void generateCellWithSpan(final HtmlTableRow row, final String text, final String title, final String cssClass,
            final Integer colSpan) {
        final HtmlTableCell cell = row.createCell();
        cell.setClasses(cssClass);
        cell.setColspan(colSpan);

        final HtmlInlineContainer span = new HtmlInlineContainer();
        span.addChild(new HtmlText(text));
        span.setTitle(title);

        cell.setBody(span);
    }

    private boolean isViewerAdministrativeOfficeEmployeeOrManager(final StudentCurricularPlan studentCurricularPlan) {
        final Person person = AccessControl.getPerson();
        return person.hasRole(RoleType.MANAGER)
                || AcademicAuthorizationGroup.getProgramsForOperation(person, AcademicOperationType.VIEW_FULL_STUDENT_CURRICULUM)
                        .contains(studentCurricularPlan.getDegree());
    }

}
