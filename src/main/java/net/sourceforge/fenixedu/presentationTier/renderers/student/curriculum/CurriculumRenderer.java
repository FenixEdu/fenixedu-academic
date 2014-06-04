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

import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.Grade;
import net.sourceforge.fenixedu.domain.OptionalEnrolment;
import net.sourceforge.fenixedu.domain.student.curriculum.Curriculum;
import net.sourceforge.fenixedu.domain.student.curriculum.ICurriculumEntry;
import net.sourceforge.fenixedu.domain.studentCurriculum.Dismissal;
import net.sourceforge.fenixedu.domain.studentCurriculum.ExternalEnrolment;
import net.sourceforge.fenixedu.util.Bundle;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import pt.ist.fenixWebFramework.renderers.InputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlBlockContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlInlineContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlTable;
import pt.ist.fenixWebFramework.renderers.components.HtmlTableCell;
import pt.ist.fenixWebFramework.renderers.components.HtmlTableRow;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;

public class CurriculumRenderer extends InputRenderer {

    private String studentCurricularPlanClass = "scplan";

    private String curriculumGroupRowClass = "scplangroup";

    private String enrolmentRowClass = "scplanenrollment";

    private String dismissalRowClass = "scplandismissal";

    private String temporaryDismissalRowClass = "scplantemporarydismissal";

    private String cellClasses =
            "scplancolident, scplancolcurricularcourse, scplancoldegreecurricularplan, scplancolenrollmentstate, "
                    + "scplancolenrollmenttype, scplancolgrade, scplancolweight, scplancolects, "
                    + "scplancolenrolmentevaluationtype, scplancolyear, scplancolsemester, scplancolexamdate, scplancolgraderesponsible";

    private String selectionName;

    private boolean visibleCurricularYearEntries = true;

    public boolean isVisibleCurricularYearEntries() {
        return visibleCurricularYearEntries;
    }

    public void setVisibleCurricularYearEntries(boolean visibleCurricularYearEntries) {
        this.visibleCurricularYearEntries = visibleCurricularYearEntries;
    }

    public CurriculumRenderer() {
        super();
    }

    private String getTableClass() {
        return studentCurricularPlanClass;
    }

    public void setStudentCurricularPlanClass(String studentCurricularPlanClass) {
        this.studentCurricularPlanClass = studentCurricularPlanClass;
    }

    private String getHeaderRowClass() {
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

    private String getGradeCellClass() {
        return getCellClasses()[5];
    }

    private String getWeightCellClass() {
        return getCellClasses()[6];
    }

    private String getEctsCreditsCellClass() {
        return getCellClasses()[7];
    }

    private String getEnrolmentExecutionYearCellClass() {
        return getCellClasses()[9];
    }

    private String getEnrolmentSemesterCellClass() {
        return getCellClasses()[10];
    }

    public String getSelectionName() {
        return selectionName;
    }

    public void setSelectionName(String selectionName) {
        this.selectionName = selectionName;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
        return new AverageLayout();
    }

    private class AverageLayout extends Layout {

        private static final String SPACER_IMAGE_PATH = "/images/scp_spacer.gif";

        private static final int MAX_LINE_SIZE = 6;

        private static final int MAX_COL_SPAN_FOR_TEXT_ON_GROUPS_WITH_CHILDS = 17;

        private static final int MAX_COL_SPAN_FOR_TEXT_ON_CURRICULUM_LINES = 14;

        private static final int HEADERS_SIZE = 0;

        private static final int COLUMNS_BETWEEN_TEXT_AND_GRADE = 0;

        private static final int COLUMNS_BETWEEN_TEXT_AND_ECTS = 2;

        private static final int COLUMNS_FROM_ECTS_AND_ENROLMENT_EVALUATION_DATE = 3;

        private static final int COLUMNS_BETWEEN_ECTS_AND_ENROLMENT_EVALUATION_DATE =
                COLUMNS_FROM_ECTS_AND_ENROLMENT_EVALUATION_DATE - 1;

        private static final int COLUMNS_BETWEEN_TEXT_AND_ENROLMENT_EVALUATION_DATE = COLUMNS_BETWEEN_TEXT_AND_ECTS
                + COLUMNS_FROM_ECTS_AND_ENROLMENT_EVALUATION_DATE;

        private static final int LATEST_ENROLMENT_EVALUATION_COLUMNS = 2;

        private static final String DATE_FORMAT = "yyyy/MM/dd";

        private Curriculum curriculum;

        @Override
        public HtmlComponent createComponent(Object object, Class type) {
            this.curriculum = (Curriculum) object;

            final HtmlContainer container = new HtmlBlockContainer();

            if (this.curriculum == null) {
                container.addChild(createHtmlTextItalic(BundleUtil.getString(Bundle.STUDENT, "message.no.average")));

                return container;
            }

            if (!this.curriculum.getStudentCurricularPlan().isBoxStructure()) {
                container.addChild(createHtmlTextItalic(BundleUtil.getString(Bundle.STUDENT, "not.applicable")));

                return container;
            }

            if (this.curriculum.isEmpty()) {
                container.addChild(createHtmlTextItalic(BundleUtil.getString(Bundle.STUDENT, "message.no.approvals")));

                return container;
            }

            if (this.curriculum.getCurriculumEntries().isEmpty()) {
                container.addChild(createHtmlTextItalic(BundleUtil.getString(Bundle.STUDENT, "message.empty.curriculum")));
            } else {
                final HtmlContainer averageContainer = new HtmlBlockContainer();
                averageContainer.setStyle("padding-bottom: 3.5em;");
                container.addChild(averageContainer);
                final HtmlTable averageEntriesTable = new HtmlTable();
                averageContainer.addChild(averageEntriesTable);
                averageEntriesTable.setClasses(getTableClass());
                generateAverageRows(averageEntriesTable);
                generateAverageSums(averageEntriesTable);
            }

            if (isVisibleCurricularYearEntries()) {
                final HtmlContainer curricularYearContainer = new HtmlBlockContainer();
                container.addChild(curricularYearContainer);
                final HtmlTable curricularYearTable = new HtmlTable();
                curricularYearContainer.addChild(curricularYearTable);
                curricularYearTable.setClasses(getTableClass());
                generateCurricularYearRows(curricularYearTable);
                generateCurricularYearSums(curricularYearTable);
            }

            return container;
        }

        private HtmlText createHtmlTextItalic(final String message) {
            final HtmlText htmlText = new HtmlText(message);
            htmlText.setClasses("italic");

            return htmlText;
        }

        private void generateAverageRows(final HtmlTable mainTable) {

            final Set<ICurriculumEntry> sortedIAverageEntries =
                    new TreeSet<ICurriculumEntry>(ICurriculumEntry.COMPARATOR_BY_EXECUTION_PERIOD_AND_NAME_AND_ID);
            sortedIAverageEntries.addAll(this.curriculum.getEnrolmentRelatedEntries());
            if (!sortedIAverageEntries.isEmpty()) {
                generateGroupRowWithText(mainTable, "Inscrições", true, 0);
                generateRows(mainTable, sortedIAverageEntries, 0);
            }

            final Set<ICurriculumEntry> sortedEquivalenceEntries =
                    new TreeSet<ICurriculumEntry>(ICurriculumEntry.COMPARATOR_BY_EXECUTION_PERIOD_AND_NAME_AND_ID);
            final Set<ICurriculumEntry> sortedSubstitutionsEntries =
                    new TreeSet<ICurriculumEntry>(ICurriculumEntry.COMPARATOR_BY_EXECUTION_PERIOD_AND_NAME_AND_ID);
            for (final ICurriculumEntry entry : this.curriculum.getDismissalRelatedEntries()) {
                if (entry instanceof Dismissal) {
                    sortedEquivalenceEntries.add(entry);
                } else {
                    sortedSubstitutionsEntries.add(entry);
                }
            }

            if (!sortedSubstitutionsEntries.isEmpty()) {
                generateGroupRowWithText(mainTable, "Substituições", true, 0);
                generateRows(mainTable, sortedSubstitutionsEntries, 0);
            }

            if (!sortedEquivalenceEntries.isEmpty()) {
                generateGroupRowWithText(mainTable, "Equivalências", true, 0);
                generateRows(mainTable, sortedEquivalenceEntries, 0);
            }
        }

        private void generateGroupRowWithText(final HtmlTable mainTable, final String text, boolean addHeaders, final int level) {

            final HtmlTableRow groupRow = mainTable.createRow();
            groupRow.setClasses(getHeaderRowClass());

            final HtmlTableCell textCell = groupRow.createCell();
            textCell.setText(text);
            textCell.setClasses(getLabelCellClass());
            textCell.setRowspan(2);
            textCell.setColspan(MAX_COL_SPAN_FOR_TEXT_ON_CURRICULUM_LINES);

            final HtmlTableCell averageCell = groupRow.createCell();
            averageCell.setText("Média de Curso");
            averageCell.setClasses(getGradeCellClass());
            averageCell.setColspan(3);

            final HtmlTableCell executionYearCell = groupRow.createCell();
            executionYearCell.setText("Ano Lectivo");
            executionYearCell.setClasses(getGradeCellClass());
            executionYearCell.setColspan(2);
            executionYearCell.setRowspan(2);

            final HtmlTableRow groupSubRow = mainTable.createRow();
            groupSubRow.setClasses(getHeaderRowClass());
            generateCellWithText(groupSubRow, BundleUtil.getString(Bundle.APPLICATION, "label.grade"), getGradeCellClass());
            generateCellWithText(groupSubRow, BundleUtil.getString(Bundle.APPLICATION, "label.weight"), getEctsCreditsCellClass());
            generateCellWithText(groupSubRow, "Peso x Classificação", getEctsCreditsCellClass());
        }

        private void generateCellsWithText(final HtmlTableRow row, final int numberOfCells, final String text,
                final String[] cssClasses) {
            for (int i = 0; i < numberOfCells; i++) {
                generateCellWithText(row, "-", cssClasses[i]);
            }
        }

        private void generateRows(HtmlTable mainTable, Set<ICurriculumEntry> entries, int level) {
            for (final ICurriculumEntry entry : entries) {
                generateRow(mainTable, entry, level, true);
            }
        }

        private void generateRow(HtmlTable mainTable, final ICurriculumEntry entry, int level, boolean allowSelection) {
            final HtmlTableRow enrolmentRow = mainTable.createRow();
            enrolmentRow.setClasses(getEnrolmentRowClass());

            generateCodeAndNameCell(enrolmentRow, entry, level, allowSelection);
            if (entry instanceof ExternalEnrolment) {
                generateExternalEnrolmentLabelCell(enrolmentRow, (ExternalEnrolment) entry, level);
            }
            generateGradeCell(enrolmentRow, entry);
            generateWeightCell(enrolmentRow, entry);
            generateWeightTimesGradeCell(enrolmentRow, entry);
            generateExecutionYearCell(enrolmentRow, entry);
            generateSemesterCell(enrolmentRow, entry);
        }

        private void generateCodeAndNameCell(final HtmlTableRow enrolmentRow, final ICurriculumEntry entry, final int level,
                boolean allowSelection) {

            final HtmlInlineContainer inlineContainer = new HtmlInlineContainer();
            inlineContainer.addChild(new HtmlText(getPresentationNameFor(entry)));

            final HtmlTableCell cell = enrolmentRow.createCell();
            cell.setClasses(getLabelCellClass());
            cell.setColspan(MAX_COL_SPAN_FOR_TEXT_ON_CURRICULUM_LINES - (entry instanceof ExternalEnrolment ? 1 : 0) - level);
            cell.setBody(inlineContainer);
        }

        private String getPresentationNameFor(final ICurriculumEntry entry) {
            final String code = !StringUtils.isEmpty(entry.getCode()) ? entry.getCode() + " - " : "";

            if (entry instanceof OptionalEnrolment) {
                final OptionalEnrolment optionalEnrolment = (OptionalEnrolment) entry;
                return code + optionalEnrolment.getCurricularCourse().getName(entry.getExecutionPeriod());
            } else {
                return code + entry.getName().getContent();
            }
        }

        private void generateExternalEnrolmentLabelCell(final HtmlTableRow externalEnrolmentRow,
                final ExternalEnrolment externalEnrolment, final int level) {
            generateCellWithText(externalEnrolmentRow, externalEnrolment.getDescription(), getLabelCellClass(), 1);
        }

        private void generateGradeCell(HtmlTableRow enrolmentRow, final ICurriculumEntry entry) {
            final Grade grade = entry.getGrade();
            generateCellWithText(enrolmentRow, grade.isEmpty() ? "-" : grade.getValue(), getGradeCellClass());
        }

        private void generateWeightCell(HtmlTableRow enrolmentRow, final ICurriculumEntry entry) {
            generateCellWithText(enrolmentRow, entry.getGrade().isNumeric() ? entry.getWeigthForCurriculum().toString() : "-",
                    getWeightCellClass());
        }

        private void generateWeightTimesGradeCell(HtmlTableRow enrolmentRow, final ICurriculumEntry entry) {
            generateCellWithText(enrolmentRow, entry.getGrade().isNumeric() ? entry.getWeigthTimesGrade().toString() : "-",
                    getWeightCellClass());
        }

        private void generateEctsCreditsCell(HtmlTableRow enrolmentRow, final ICurriculumEntry entry) {
            generateCellWithText(enrolmentRow, entry.getEctsCreditsForCurriculum().toString(), getEctsCreditsCellClass());
        }

        private void generateExecutionYearCell(HtmlTableRow enrolmentRow, final ICurriculumEntry entry) {
            generateCellWithText(enrolmentRow, entry.getExecutionYear() == null ? "-" : entry.getExecutionYear().getYear(),
                    getEnrolmentExecutionYearCellClass());
        }

        private void generateSemesterCell(final HtmlTableRow enrolmentRow, final ICurriculumEntry entry) {
            final String semester =
                    entry.getExecutionPeriod() == null ? "-" : entry.getExecutionPeriod().getSemester().toString() + " "
                            + BundleUtil.getString(Bundle.APPLICATION, "label.semester.short");

            generateCellWithText(enrolmentRow, semester, getEnrolmentSemesterCellClass());
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

        private void generateAverageSums(final HtmlTable mainTable) {
            final HtmlTableRow row = mainTable.createRow();
            row.setClasses(getHeaderRowClass());

            final HtmlTableCell sumsCell = row.createCell();
            sumsCell.setText("Somatórios");
            sumsCell.setStyle("text-align: right;");
            sumsCell.setColspan(15);

            final HtmlTableCell sumPiCell = row.createCell();
            sumPiCell.setText(this.curriculum.getSumPi().toString());
            sumPiCell.setClasses(getGradeCellClass());

            final HtmlTableCell sumPiCiCell = row.createCell();
            sumPiCiCell.setText(this.curriculum.getSumPiCi().toString());
            sumPiCiCell.setClasses(getGradeCellClass());

            final HtmlTableCell emptyCell = row.createCell();
            emptyCell.setClasses(getGradeCellClass());
            emptyCell.setColspan(2);
        }

        private void generateCurricularYearRows(final HtmlTable table) {
            final Set<ICurriculumEntry> sortedEntries =
                    new TreeSet<ICurriculumEntry>(ICurriculumEntry.COMPARATOR_BY_EXECUTION_PERIOD_AND_NAME_AND_ID);
            sortedEntries.addAll(this.curriculum.getCurricularYearEntries());
            if (!sortedEntries.isEmpty()) {
                generateCurricularYearHeaderRowWithText(table, "", true, 0);
                generateCurricularYearRows(table, sortedEntries, 0);
            }
        }

        private void generateCurricularYearHeaderRowWithText(final HtmlTable table, final String text, boolean addHeaders,
                final int level) {
            final HtmlTableRow groupRow = table.createRow();
            groupRow.setClasses(getHeaderRowClass());

            final HtmlTableCell textCell = groupRow.createCell();
            textCell.setText(text);
            textCell.setClasses(getLabelCellClass());
            textCell.setRowspan(2);
            textCell.setColspan(MAX_COL_SPAN_FOR_TEXT_ON_CURRICULUM_LINES);

            final HtmlTableCell curricularYearCell = groupRow.createCell();
            curricularYearCell.setText("Ano Curricular");
            curricularYearCell.setClasses(getGradeCellClass());
            curricularYearCell.setColspan(1);

            final HtmlTableCell executionYearCell = groupRow.createCell();
            executionYearCell.setText("Ano Lectivo");
            executionYearCell.setClasses(getGradeCellClass());
            executionYearCell.setColspan(2);
            executionYearCell.setRowspan(2);

            final HtmlTableRow groupSubRow = table.createRow();
            groupSubRow.setClasses(getHeaderRowClass());
            generateCellWithText(groupSubRow, BundleUtil.getString(Bundle.APPLICATION, "label.ects"), getEctsCreditsCellClass());
        }

        private void generateCurricularYearRows(HtmlTable mainTable, Set<ICurriculumEntry> entries, int level) {
            for (final ICurriculumEntry entry : entries) {
                generateCurricularYearRow(mainTable, entry, level, true);
            }
        }

        private void generateCurricularYearRow(HtmlTable mainTable, final ICurriculumEntry entry, int level,
                boolean allowSelection) {
            final HtmlTableRow enrolmentRow = mainTable.createRow();
            enrolmentRow.setClasses(getEnrolmentRowClass());

            generateCodeAndNameCell(enrolmentRow, entry, level, allowSelection);
            if (entry instanceof ExternalEnrolment) {
                generateExternalEnrolmentLabelCell(enrolmentRow, (ExternalEnrolment) entry, level);
            }
            generateEctsCreditsCell(enrolmentRow, entry);
            generateExecutionYearCell(enrolmentRow, entry);
            generateSemesterCell(enrolmentRow, entry);
        }

        private void generateCurricularYearSums(final HtmlTable mainTable) {
            final HtmlTableRow row = mainTable.createRow();
            row.setClasses(getHeaderRowClass());

            final HtmlTableCell sumsCell = row.createCell();
            sumsCell.setText("Somatórios");
            sumsCell.setStyle("text-align: right;");
            sumsCell.setColspan(14);

            final HtmlTableCell sumEctsCreditsCell = row.createCell();
            sumEctsCreditsCell.setText(this.curriculum.getSumEctsCredits().toString());
            sumEctsCreditsCell.setClasses(getGradeCellClass());

            final HtmlTableCell emptyCell = row.createCell();
            emptyCell.setClasses(getGradeCellClass());
            emptyCell.setColspan(2);
        }

    }

}
