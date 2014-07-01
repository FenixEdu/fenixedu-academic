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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriod;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRule;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.util.Bundle;
import net.sourceforge.fenixedu.util.CurricularRuleLabelFormatter;

import org.fenixedu.bennu.core.i18n.BundleUtil;

import pt.ist.fenixWebFramework.renderers.components.HtmlBlockContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlImage;
import pt.ist.fenixWebFramework.renderers.components.HtmlLink;
import pt.ist.fenixWebFramework.renderers.components.HtmlTable;
import pt.ist.fenixWebFramework.renderers.components.HtmlTableCell;
import pt.ist.fenixWebFramework.renderers.components.HtmlTableRow;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;
import pt.utl.ist.fenix.tools.util.Pair;

abstract class DegreeCurricularPlanLayout extends Layout {

    static protected final String EMPTY_CELL = "-";
    static protected final String SPACER_IMAGE_PATH = "/images/scp_spacer.gif";

    static private final int MAX_LINE_SIZE = 25;
    static private final int MAX_COL_SPAN_FOR_TEXT_ON_GROUPS_WITH_CHILDS = 21;
    static private final int MAX_COL_SPAN_FOR_TEXT_ON_CURRICULAR_COURSES = 21;

    private DegreeCurricularPlanRenderer degreeCurricularPlanRenderer;

    protected DegreeCurricularPlanLayout(DegreeCurricularPlanRenderer degreeCurricularPlanRenderer) {
        this.degreeCurricularPlanRenderer = degreeCurricularPlanRenderer;
    }

    protected int getMaxLineSize() {
        return MAX_LINE_SIZE;
    }

    protected int getMaxColSpanForTextOnGroupsWithChilds() {
        return MAX_COL_SPAN_FOR_TEXT_ON_GROUPS_WITH_CHILDS;
    }

    protected int getMaxColSpanForTextOnCurricularCourses() {
        return MAX_COL_SPAN_FOR_TEXT_ON_CURRICULAR_COURSES;
    }

    protected int getMaxColSpanForOptionalCurricularCourse() {
        return MAX_LINE_SIZE - getMaxColSpanForTextOnCurricularCourses() - 1;
    }

    protected DegreeCurricularPlan getDegreeCurricularPlan() {
        return degreeCurricularPlanRenderer.getDegreeCurricularPlan();
    }

    protected String getTabCellClass() {
        return degreeCurricularPlanRenderer.getTabCellClass();
    }

    protected String getDegreeCurricularPlanClass() {
        return degreeCurricularPlanRenderer.getDegreeCurricularPlanClass();
    }

    protected String roundValue(Double contactLoad) {
        return new BigDecimal(contactLoad).setScale(2, RoundingMode.HALF_EVEN).toPlainString();
    }

    protected boolean showRules() {
        return degreeCurricularPlanRenderer.showRules();
    }

    protected boolean showCourses() {
        return degreeCurricularPlanRenderer.showCourses();
    }

    protected ExecutionYear getExecutionInterval() {
        return degreeCurricularPlanRenderer.getExecutionInterval();
    }

    protected boolean hasExecutionInterval() {
        return getExecutionInterval() != null;
    }

    protected String getCurricularRuleRowClass() {
        return degreeCurricularPlanRenderer.getCurricularRuleRowClass();
    }

    protected String getCourseGroupRowClass() {
        return degreeCurricularPlanRenderer.getCourseGroupRowClass();
    }

    protected String getCurricularCourseRowClass() {
        return degreeCurricularPlanRenderer.getCurricularCourseRowClass();
    }

    protected String getCurriclarCourseCellClass() {
        return degreeCurricularPlanRenderer.getCurriclarCourseCellClass();
    }

    protected String getLabelCellClass() {
        return degreeCurricularPlanRenderer.getLabelCellClass();
    }

    protected String getCourseLoadCellClass() {
        return degreeCurricularPlanRenderer.getCourseLoadCellClass();
    }

    protected String getCurricularPeriodCellClass() {
        return degreeCurricularPlanRenderer.getCurricularPeriodCellClass();
    }

    protected String getRegimeCellClass() {
        return degreeCurricularPlanRenderer.getRegimeCellClass();
    }

    protected String getOptionalInformationCellClass() {
        return degreeCurricularPlanRenderer.getOptionalInformationCellClass();
    }

    protected String getEctsCreditsCellClass() {
        return degreeCurricularPlanRenderer.getEctsCreditsCellClass();
    }

    protected String getViewCurricularCourseUrl() {
        return degreeCurricularPlanRenderer.getViewCurricularCourseUrl();
    }

    protected List<Pair<String, String>> getViewCurricularCourseUrlParameters() {
        return degreeCurricularPlanRenderer.getViewCurricularCourseUrlParameters();
    }

    protected String getLabel(final String key) {
        return BundleUtil.getString(Bundle.ACADEMIC, key);
    }

    protected String getDegreeModuleIdAttributeName() {
        return degreeCurricularPlanRenderer.getDegreeModuleIdAttributeName();
    }

    protected boolean isCurricularCourseLinkable() {
        return degreeCurricularPlanRenderer.isCurricularCourseLinkable();
    }

    /* methods to draw information */

    @Override
    public HtmlComponent createComponent(Object object, Class type) {
        final HtmlContainer container = new HtmlBlockContainer();
        draw(getDegreeCurricularPlan(), createMainTable(container));
        return container;
    }

    abstract protected void draw(DegreeCurricularPlan degreeCurricularPlan, HtmlTable createMainTable);

    protected HtmlTable createMainTable(final HtmlContainer container) {
        final HtmlTable main = new HtmlTable();
        container.addChild(main);
        main.setClasses(getDegreeCurricularPlanClass());
        return main;
    }

    protected void addTabsToRow(final HtmlTableRow row, final int level) {
        for (int i = 0; i < level; i++) {
            final HtmlLink link = new HtmlLink();
            link.setModuleRelative(false);
            link.setUrl(DegreeCurricularPlanLayout.SPACER_IMAGE_PATH);

            final HtmlImage spacerImage = new HtmlImage();
            spacerImage.setSource(link.calculateUrl());

            final HtmlTableCell tabCell = row.createCell();
            tabCell.setClasses(getTabCellClass());
            tabCell.setBody(spacerImage);
        }
    }

    protected void drawCurricularCourseName(final CurricularCourse course, final HtmlTableRow row, boolean linkable, int level) {
        final HtmlTableCell cell = row.createCell();
        cell.setClasses(getCurriclarCourseCellClass());
        cell.setColspan(getMaxColSpanForTextOnCurricularCourses() - level);

        if (linkable) {
            final HtmlLink result = new HtmlLink();

            result.setText(course.getNameI18N(getExecutionInterval()).getContent());
            result.setModuleRelative(true);

            result.setUrl(getViewCurricularCourseUrl());
            result.setParameter(getDegreeModuleIdAttributeName(), course.getExternalId());

            for (final Pair<String, String> param : getViewCurricularCourseUrlParameters()) {
                result.setParameter(param.getKey(), param.getValue());
            }

            cell.setBody(result);

        } else {
            cell.setText(course.getNameI18N(getExecutionInterval()).getContent());
        }
    }

    protected void drawOptionalCellInformation(final HtmlTableRow row) {
        final HtmlTableCell cell = row.createCell();
        cell.setClasses(getOptionalInformationCellClass());

        cell.setColspan(getMaxColSpanForOptionalCurricularCourse());
        cell.setText(BundleUtil.getString(Bundle.APPLICATION, "label.degreeCurricularPlan.renderer.option"));
    }

    protected void drawRegime(final CurricularCourse course, final HtmlTableRow row) {
        final HtmlTableCell cell = row.createCell();
        cell.setClasses(getRegimeCellClass());
        cell.setText(hasRegime(course) ? course.getRegime(getExecutionInterval()).getAcronym() : EMPTY_CELL);
        cell.setTitle(BundleUtil.getString(Bundle.APPLICATION, "label.degreeCurricularPlan.renderer.title.regime"));
    }

    private boolean hasRegime(final CurricularCourse curricularCourse) {
        return !curricularCourse.isOptionalCurricularCourse() && curricularCourse.hasRegime(getExecutionInterval());
    }

    protected void drawCourseLoad(final CurricularCourse course, final CurricularPeriod period, final HtmlTableRow row) {
        final HtmlTableCell cell = row.createCell();
        cell.setClasses(getCourseLoadCellClass());

        if (course.isOptionalCurricularCourse()) {
            cell.setText(EMPTY_CELL);
        } else {
            final StringBuilder builder = new StringBuilder();

            builder.append(BundleUtil.getString(Bundle.APPLICATION, "label.degreeCurricularPlan.renderer.acronym.contact.load")).append("-");
            builder.append(roundValue(course.getContactLoad(period, getExecutionInterval()))).append(" ");

            builder.append(BundleUtil.getString(Bundle.APPLICATION, "label.degreeCurricularPlan.renderer.acronym.autonomous.work")).append("-");
            builder.append(course.getAutonomousWorkHours(period, getExecutionInterval()).toString()).append(" ");

            builder.append(BundleUtil.getString(Bundle.APPLICATION, "label.degreeCurricularPlan.renderer.acronym.total.load")).append("-");
            builder.append(course.getTotalLoad(period, getExecutionInterval()));

            cell.setText(builder.toString());
        }

        cell.setTitle(BundleUtil.getString(Bundle.APPLICATION, "label.degreeCurricularPlan.renderer.title.course.load"));
    }

    protected void drawEctsCredits(final CurricularCourse course, final CurricularPeriod period, final HtmlTableRow row) {
        final HtmlTableCell cell = row.createCell();
        cell.setClasses(getEctsCreditsCellClass());
        cell.setText(course.isOptionalCurricularCourse() ? EMPTY_CELL : course.getEctsCredits(period, getExecutionInterval())
                .toString());
    }

    protected void drawCurricularRulesRows(final DegreeModule module, final Context previous, final HtmlTable main, int level) {
        if (showRules()) {
            for (final CurricularRule rule : module.getVisibleCurricularRules(getExecutionInterval())) {
                if (rule.appliesToContext(previous)) {
                    drawCurricularRuleRow(rule, main, level);
                }
            }
        }
    }

    protected void drawCurricularRuleRow(final CurricularRule rule, final HtmlTable main, int level) {
        final HtmlTableRow groupRow = main.createRow();
        groupRow.setClasses(getCurricularRuleRowClass());
        addTabsToRow(groupRow, level);

        final HtmlTableCell cell = groupRow.createCell();
        cell.setClasses(getLabelCellClass());
        cell.setColspan(getMaxLineSize() - level);
        cell.setText(CurricularRuleLabelFormatter.getLabel(rule));

    }
}
