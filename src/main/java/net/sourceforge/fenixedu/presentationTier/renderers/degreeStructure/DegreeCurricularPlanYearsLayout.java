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
package net.sourceforge.fenixedu.presentationTier.renderers.degreeStructure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriod;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import pt.ist.fenixWebFramework.renderers.components.HtmlTable;
import pt.ist.fenixWebFramework.renderers.components.HtmlTableCell;
import pt.ist.fenixWebFramework.renderers.components.HtmlTableRow;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;

class DegreeCurricularPlanYearsLayout extends DegreeCurricularPlanLayout {

    private Map<CurricularPeriod, Collection<Context>> contextsToDrawInNextPeriod =
            new HashMap<CurricularPeriod, Collection<Context>>();

    protected DegreeCurricularPlanYearsLayout(final DegreeCurricularPlanRenderer degreeCurricularPlanRenderer) {
        super(degreeCurricularPlanRenderer);
    }

    @Override
    protected void draw(DegreeCurricularPlan degreeCurricularPlan, HtmlTable mainTable) {

        final CurricularPeriod degreeStructure = degreeCurricularPlan.getDegreeStructure();

        for (final CurricularPeriod curricularPeriod : degreeStructure.getSortedChilds()) {
            /*
             * NOTE: Assuming structure Year - Semesters, and all courses are
             * connected to semesters apart from course regime
             */
            drawTopCurricularPeriods(curricularPeriod, mainTable, 0);
        }
    }

    private void drawTopCurricularPeriods(CurricularPeriod curricularPeriod, HtmlTable mainTable, int level) {

        if (!curricularPeriod.hasAnyChilds()) {
            return;
        }

        for (final CurricularPeriod child : curricularPeriod.getSortedChilds()) {
            drawCurricularPeriodRow(mainTable, child, level);
            drawContexts(child, mainTable, level + 1);

            if (contextsToDrawInNextPeriod.containsKey(child)) {
                drawExtraContextsInNextPeriod(child, mainTable, level + 1);
            }
        }
    }

    private void drawCurricularPeriodRow(HtmlTable mainTable, final CurricularPeriod child, int level) {
        final HtmlTableRow groupRow = mainTable.createRow();
        groupRow.setClasses(getCourseGroupRowClass());
        addTabsToRow(groupRow, level);

        // curricular period full label
        final HtmlTableCell cell1 = groupRow.createCell();
        cell1.setClasses(getLabelCellClass());
        cell1.setBody(new HtmlText(child.getFullLabel()));
        cell1.setColspan(getMaxColSpanForTextOnGroupsWithChilds());

        // Group
        final HtmlTableCell cell2 = groupRow.createCell();
        cell2.setClasses(getLabelCellClass());
        cell2.setBody(new HtmlText(getLabel("label.degreeCurricularPlan.renderer.group")));

        final HtmlTableCell cell3 = groupRow.createCell();
        cell3.setClasses(getLabelCellClass());
        cell3.setColspan(getMaxLineSize() - getMaxColSpanForTextOnGroupsWithChilds() - 1);

    }

    private void drawContexts(CurricularPeriod child, HtmlTable mainTable, int level) {
        for (final Context context : getSortedContextsByCurricularCourseName(child)) {
            if (!hasExecutionInterval() || context.isValid(getExecutionInterval())) {
                drawContext(context, mainTable, level);
            }
        }
    }

    private Collection<Context> getSortedContextsByCurricularCourseName(final CurricularPeriod period) {
        final List<Context> contexts = new ArrayList<Context>(period.getContextsWithCurricularCourses());
        Collections.sort(contexts, Context.COMPARATOR_BY_DEGREE_MODULE_NAME);
        return contexts;
    }

    private void drawContext(Context context, HtmlTable mainTable, int level) {

        final HtmlTableRow row = mainTable.createRow();
        addTabsToRow(row, level);
        row.setClasses(getCurricularCourseRowClass());

        final CurricularCourse curricularCourse = (CurricularCourse) context.getChildDegreeModule();

        drawCurricularCourseName(curricularCourse, row, isCurricularCourseLinkable(), level);
        drawCourseGroupName(context.getParentCourseGroup(), row, level);

        if (curricularCourse.isOptionalCurricularCourse()) {
            drawOptionalCellInformation(row);
        } else {
            drawRegime(curricularCourse, row);
            drawCourseLoad(curricularCourse, context.getCurricularPeriod(), row);
            drawEctsCredits(curricularCourse, context.getCurricularPeriod(), row);
        }

        drawCurricularRulesRows(curricularCourse, null, mainTable, level + 1);

        if (curricularCourse.isAnual(getExecutionInterval()) && context.getCurricularPeriod().getChildOrder() == 1) {
            addContextToProcessInNextPeriod(context, context.getCurricularPeriod().getNext());
        }
    }

    private void addContextToProcessInNextPeriod(Context context, CurricularPeriod period) {

        Collection<Context> collection = contextsToDrawInNextPeriod.get(period);
        if (collection == null) {
            contextsToDrawInNextPeriod.put(period, collection = new HashSet<Context>());
        }
        collection.add(context);
    }

    private void drawCourseGroupName(CourseGroup parentCourseGroup, HtmlTableRow row, int level) {
        final HtmlTableCell cell = row.createCell();
        cell.setClasses(getCurriclarCourseCellClass());
        cell.setBody(new HtmlText(parentCourseGroup.getName()));
    }

    private void drawExtraContextsInNextPeriod(CurricularPeriod period, HtmlTable mainTable, int level) {

        for (final Context context : contextsToDrawInNextPeriod.get(period)) {
            final HtmlTableRow row = mainTable.createRow();
            addTabsToRow(row, level);
            row.setClasses(getCurricularCourseRowClass());

            final CurricularCourse curricularCourse = (CurricularCourse) context.getChildDegreeModule();

            drawCurricularCourseName(curricularCourse, row, false, level);
            drawCourseGroupName(context.getParentCourseGroup(), row, level);

            if (curricularCourse.isOptionalCurricularCourse()) {
                drawOptionalCellInformation(row);
            } else {
                drawRegime(curricularCourse, row);
                drawCourseLoad(curricularCourse, period, row);
                drawEctsCredits(curricularCourse, period, row);
            }

            drawCurricularRulesRows(curricularCourse, null, mainTable, level + 1);
        }

    }
}
