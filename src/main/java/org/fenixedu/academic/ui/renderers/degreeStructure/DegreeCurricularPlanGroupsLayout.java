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
package org.fenixedu.academic.ui.renderers.degreeStructure;

import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.curricularPeriod.CurricularPeriod;
import org.fenixedu.academic.domain.degreeStructure.Context;
import org.fenixedu.academic.domain.degreeStructure.CourseGroup;
import org.fenixedu.academic.util.CurricularPeriodLabelFormatter;

import pt.ist.fenixWebFramework.renderers.components.HtmlTable;
import pt.ist.fenixWebFramework.renderers.components.HtmlTableCell;
import pt.ist.fenixWebFramework.renderers.components.HtmlTableRow;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;

public class DegreeCurricularPlanGroupsLayout extends DegreeCurricularPlanLayout {

    public DegreeCurricularPlanGroupsLayout(DegreeCurricularPlanRenderer degreeCurricularPlanRenderer) {
        super(degreeCurricularPlanRenderer);
    }

    @Override
    protected void draw(final DegreeCurricularPlan degreeCurricularPlan, final HtmlTable main) {
        drawCourseGroupRow(degreeCurricularPlan.getRoot(), null, main, 0);
    }

    private void drawCourseGroupRow(final CourseGroup courseGroup, final Context previous, final HtmlTable main, int level) {
        drawCourseGroupName(courseGroup, main, level);
        drawCurricularRulesRows(courseGroup, previous, main, level + 1);
        drawCurricularCourseRows(courseGroup, main, level + 1);
        drawCourseGroupRows(courseGroup, main, level + 1);
    }

    private void drawCourseGroupRows(final CourseGroup courseGroup, final HtmlTable main, int level) {
        for (final Context context : courseGroup.getSortedOpenChildContextsWithCourseGroups(getExecutionInterval())) {
            drawCourseGroupRow((CourseGroup) context.getChildDegreeModule(), context, main, level);
        }
    }

    private void drawCourseGroupName(final CourseGroup courseGroup, final HtmlTable mainTable, final int level) {
        final HtmlTableRow groupRow = mainTable.createRow();
        groupRow.setClasses(getCourseGroupRowClass());
        addTabsToRow(groupRow, level);

        final HtmlTableCell cell = groupRow.createCell();
        cell.setClasses(getLabelCellClass());
        cell.setBody(new HtmlText(courseGroup.getNameI18N().getContent()));

        if (showCourses() && courseGroup.hasAnyChildContextWithCurricularCourse()) {
            cell.setColspan(getMaxColSpanForTextOnGroupsWithChilds() - level);
            drawCurricularPeriodHeader(groupRow);
            drawCourseLoadHeader(groupRow);
            drawEctsCreditsHeader(groupRow);
        } else {
            cell.setColspan(getMaxLineSize() - level);
        }
    }

    private void drawCurricularPeriodHeader(final HtmlTableRow row) {
        final HtmlTableCell cell = row.createCell();
        cell.setClasses(getCourseLoadCellClass());
        cell.setColspan(2);
        cell.setText(getLabel("label.degreeCurricularPlan.renderer.curricular.period"));
    }

    private void drawCourseLoadHeader(final HtmlTableRow row) {
        final HtmlTableCell cell = row.createCell();
        cell.setClasses(getCourseLoadCellClass());
        cell.setText(getLabel("label.degreeCurricularPlan.renderer.course.load"));
    }

    private void drawEctsCreditsHeader(final HtmlTableRow row) {
        final HtmlTableCell cell = row.createCell();
        cell.setClasses(getEctsCreditsCellClass());
        cell.setText(getLabel("label.degreeCurricularPlan.renderer.ects"));
    }

    private void drawCurricularCourseRows(final CourseGroup courseGroup, final HtmlTable main, int level) {
        if (showCourses()) {
            for (final Context context : courseGroup.getSortedOpenChildContextsWithCurricularCourses(getExecutionInterval())) {
                drawCurricularCourseRow(context, main, level);
            }
        }
    }

    private void drawCurricularCourseRow(final Context context, final HtmlTable main, int level) {
        final HtmlTableRow row = main.createRow();
        addTabsToRow(row, level);
        row.setClasses(getCurricularCourseRowClass());

        final CurricularCourse curricularCourse = (CurricularCourse) context.getChildDegreeModule();
        if (curricularCourse.isOptionalCurricularCourse()) {
            drawCurricularCourseName(curricularCourse, row, false, level);
            drawContextInformation(context.getCurricularPeriod(), context.getTerm(), row);
            drawOptionalCellInformation(row);

        } else if (curricularCourse.isSemestrial(getExecutionInterval())) {
            drawCurricularCourseName(curricularCourse, row, isCurricularCourseLinkable(), level);
            drawContextInformation(context.getCurricularPeriod(), context.getTerm(), row);
            drawRegime(curricularCourse, row);
            drawCourseLoad(curricularCourse, context.getCurricularPeriod(), row);
            drawEctsCredits(curricularCourse, context.getCurricularPeriod(), row);

        } else {
            drawAnualCurricularCourseRow(context, row, level);
        }

        drawCurricularRulesRows(curricularCourse, context, main, level);
    }

    private void drawAnualCurricularCourseRow(final Context context, final HtmlTableRow row, final int level) {

        final CurricularCourse curricularCourse = (CurricularCourse) context.getChildDegreeModule();
        if (curricularCourse.getCompetenceCourse() != null) {

            if (curricularCourse.getCompetenceCourse().hasOneCourseLoad(getExecutionInterval())) {
                drawCurricularCourseName(curricularCourse, row, isCurricularCourseLinkable(), level);
                drawContextInformation(context.getCurricularPeriod(), context.getTerm(), row);
                drawRegime(curricularCourse, row);
                drawCourseLoad(curricularCourse, context.getCurricularPeriod(), row);
                drawEctsCredits(curricularCourse, context.getCurricularPeriod(), row);
            } else {

                final CurricularPeriod firstCP = context.getCurricularPeriod();
                final ExecutionSemester firstES = getExecutionInterval().getExecutionSemesterFor(firstCP.getChildOrder());

                drawCurricularCourseName(curricularCourse, row, isCurricularCourseLinkable(), level);
                drawContextInformation(firstCP, context.getTerm(), row);
                drawRegime(curricularCourse, row);
                drawCourseLoad(curricularCourse, firstCP, firstES, row);
                drawEctsCredits(curricularCourse, firstCP, firstES, row);

                final CurricularPeriod secondCP = context.getCurricularPeriod().getNext();
                final ExecutionSemester secondES = getExecutionInterval().getExecutionSemesterFor(secondCP.getChildOrder());

                drawCurricularCourseName(curricularCourse, row, false, level);
                drawContextInformation(secondCP, context.getTerm(), row);
                drawRegime(curricularCourse, row);
                drawCourseLoad(curricularCourse, secondCP, secondES, row);
                drawEctsCredits(curricularCourse, secondCP, secondES, row);
            }

        } else {
            drawCurricularCourseName(curricularCourse, row, isCurricularCourseLinkable(), level);
            drawContextInformation(context.getCurricularPeriod(), context.getTerm(), row);
            drawRegime(curricularCourse, row);
            drawCourseLoad(curricularCourse, context.getCurricularPeriod(), row);
            drawEctsCredits(curricularCourse, context.getCurricularPeriod(), row);
        }
    }

    private void drawContextInformation(final CurricularPeriod period, final Integer term, final HtmlTableRow row) {
        final HtmlTableCell cell = row.createCell();
        cell.setClasses(getCurricularPeriodCellClass());
        cell.setText(CurricularPeriodLabelFormatter.getFullLabel(period, term,true));
    }

    private void drawCourseLoad(final CurricularCourse course, final CurricularPeriod period, final ExecutionSemester interval,
            final HtmlTableRow row) {

        final HtmlTableCell cell = row.createCell();
        cell.setClasses(getCourseLoadCellClass());

        if (course.isOptionalCurricularCourse()) {
            cell.setText(EMPTY_CELL);
        } else {
            final StringBuilder builder = new StringBuilder();

            builder.append(getLabel("label.degreeCurricularPlan.renderer.acronym.contact.load")).append("-");
            builder.append(roundValue(course.getContactLoad(period, interval))).append(" ");

            builder.append(getLabel("label.degreeCurricularPlan.renderer.acronym.autonomous.work")).append("-");
            builder.append(course.getAutonomousWorkHours(period, interval).toString()).append(" ");

            builder.append(getLabel("label.degreeCurricularPlan.renderer.acronym.total.load")).append("-");
            builder.append(course.getTotalLoad(period, interval));

            cell.setText(builder.toString());
        }

        cell.setTitle(getLabel("label.degreeCurricularPlan.renderer.title.course.load"));
    }

    private void drawEctsCredits(final CurricularCourse course, final CurricularPeriod period, final ExecutionSemester interval,
            final HtmlTableRow row) {
        final HtmlTableCell cell = row.createCell();
        cell.setClasses(getEctsCreditsCellClass());
        cell.setText(course.isOptionalCurricularCourse() ? EMPTY_CELL : course.getEctsCredits(period, interval).toString());
    }

}
