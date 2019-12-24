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
package org.fenixedu.academic.ui.renderers;

import java.util.Collections;
import java.util.List;

import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.degreeStructure.Context;
import org.fenixedu.academic.domain.degreeStructure.CourseGroup;
import org.fenixedu.academic.dto.administrativeOffice.studentEnrolment.StudentOptionalEnrolmentBean;
import org.fenixedu.academic.ui.renderers.student.enrollment.bolonha.BolonhaStudentOptionalEnrollmentInputRenderer;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import pt.ist.fenixWebFramework.renderers.InputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlActionLink;
import pt.ist.fenixWebFramework.renderers.components.HtmlBlockContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlTable;
import pt.ist.fenixWebFramework.renderers.components.HtmlTableCell;
import pt.ist.fenixWebFramework.renderers.components.HtmlTableRow;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;
import pt.ist.fenixWebFramework.renderers.components.controllers.HtmlActionLinkController;
import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;

public class DegreeCurricularPlanOptionalEnrolmentsRenderer extends InputRenderer {

    private Integer initialWidth = 70;

    private Integer widthDecreasePerLevel = 3;

    private String tablesClasses = "showinfo3 mvert0";

    private String groupRowClasses = "bgcolor2";

    private String curricularCoursesToEnrol = "smalltxt, smalltxt aright, smalltxt aright, aright";

    private String methodName;

    public Integer getInitialWidth() {
        return initialWidth;
    }

    public void setInitialWidth(Integer initialWidth) {
        this.initialWidth = initialWidth;
    }

    public Integer getWidthDecreasePerLevel() {
        return widthDecreasePerLevel;
    }

    public void setWidthDecreasePerLevel(Integer widthDecreasePerLevel) {
        this.widthDecreasePerLevel = widthDecreasePerLevel;
    }

    public String getTablesClasses() {
        return tablesClasses;
    }

    public void setTablesClasses(String tablesClasses) {
        this.tablesClasses = tablesClasses;
    }

    public String getGroupRowClasses() {
        return groupRowClasses;
    }

    public void setGroupRowClasses(String groupRowClasses) {
        this.groupRowClasses = groupRowClasses;
    }

    private String[] getCurricularCourseClasses() {
        return curricularCoursesToEnrol.split(",");
    }

    public void setCurricularCourseClasses(String curricularCoursesToEnrol) {
        this.curricularCoursesToEnrol = curricularCoursesToEnrol;
    }

    private String getCurricularCourseNameClasses() {
        return getCurricularCourseClasses()[0];
    }

    private String getCurricularCourseYearClasses() {
        return getCurricularCourseClasses()[1];
    }

    private String getCurricularCourseEctsClasses() {
        return getCurricularCourseClasses()[2];
    }

    private String getCurricularCourseLinkClasses() {
        return getCurricularCourseClasses()[3];
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public DegreeCurricularPlanOptionalEnrolmentsRenderer() {
        super();
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
        return new DegreeCurricularPlanOptionalEnrolmentsLayout();
    }

    private class DegreeCurricularPlanOptionalEnrolmentsLayout extends Layout {

        private StudentOptionalEnrolmentBean studentOptionalEnrolmentBean = null;

        @Override
        public HtmlComponent createComponent(Object object, Class type) {

            studentOptionalEnrolmentBean = (StudentOptionalEnrolmentBean) object;

            HtmlBlockContainer container = new HtmlBlockContainer();

            if (studentOptionalEnrolmentBean == null) {
                return new HtmlText();
            }

            generateCourseGroup(container, studentOptionalEnrolmentBean.getDegreeCurricularPlan().getRoot(), 0);
            return container;
        }

        private void generateCourseGroup(HtmlBlockContainer blockContainer, CourseGroup courseGroup, int depth) {
            final HtmlTable groupTable = new HtmlTable();
            blockContainer.addChild(groupTable);
            groupTable.setClasses(getTablesClasses());
            groupTable.setStyle("width: " + (getInitialWidth() - depth) + "em; margin-left: " + depth + "em;");

            final HtmlTableRow htmlTableRow = groupTable.createRow();
            htmlTableRow.setClasses(getGroupRowClasses());
            htmlTableRow.createCell().setBody(new HtmlText(courseGroup.getName()));

            final List<Context> childCourseGroupContexts =
                    courseGroup.getValidChildContexts(CourseGroup.class, getExecutionSemester());
            final List<Context> childCurricularCourseContexts =
                    courseGroup.getValidChildContexts(CurricularCourse.class, getExecutionSemester());

            Collections.sort(childCourseGroupContexts);
            Collections.sort(childCurricularCourseContexts);

            generateCurricularCourses(blockContainer, childCurricularCourseContexts, depth + getWidthDecreasePerLevel());

            for (Context context : childCourseGroupContexts) {
                generateCourseGroup(blockContainer, (CourseGroup) context.getChildDegreeModule(),
                        depth + getWidthDecreasePerLevel());
            }
        }

        private ExecutionInterval getExecutionSemester() {
            return studentOptionalEnrolmentBean.getExecutionPeriod();
        }

        private void generateCurricularCourses(HtmlBlockContainer blockContainer, List<Context> contexts, int depth) {

            final HtmlTable table = new HtmlTable();
            blockContainer.addChild(table);
            table.setClasses(getTablesClasses());
            table.setStyle("width: " + (getInitialWidth() - depth) + "em; margin-left: " + depth + "em;");

            for (final Context context : contexts) {
                final CurricularCourse curricularCourse = (CurricularCourse) context.getChildDegreeModule();
                if (!curricularCourse.isOptionalCurricularCourse()) {

                    final HtmlTableRow htmlTableRow = table.createRow();
                    HtmlTableCell cellName = htmlTableRow.createCell();
                    cellName.setClasses(getCurricularCourseNameClasses());
                    cellName.setBody(BolonhaStudentOptionalEnrollmentInputRenderer.generateCurricularCourseNameComponent(
                            curricularCourse, this.studentOptionalEnrolmentBean.getExecutionPeriod()));

                    // Year
                    final HtmlTableCell yearCell = htmlTableRow.createCell();
                    yearCell.setClasses(getCurricularCourseYearClasses());
                    yearCell.setBody(new HtmlText(context.getCurricularPeriod().getFullLabel()));

                    // Ects
                    final HtmlTableCell ectsCell = htmlTableRow.createCell();
                    ectsCell.setClasses(getCurricularCourseEctsClasses());

                    final StringBuilder ects = new StringBuilder();
                    ects.append(curricularCourse.getEctsCredits(getExecutionSemester())).append(" ")
                            .append(BundleUtil.getString(Bundle.ACADEMIC, "credits.abbreviation"));
                    ectsCell.setBody(new HtmlText(ects.toString()));

                    // enrolment link
                    final HtmlTableCell linkTableCell = htmlTableRow.createCell();
                    linkTableCell.setClasses(getCurricularCourseLinkClasses());

                    final HtmlActionLink actionLink = new HtmlActionLink();
                    final String name = "contextEnrolLink" + context.getExternalId();
                    actionLink.setText(BundleUtil.getString(Bundle.ACADEMIC, "link.option.enrol.curricular.course"));
                    actionLink.setName(name);
                    actionLink.setOnClick(String.format(
                            "$(this).closest('form').find('input[name=\\'method\\']').attr('value', '%s');"
                            		+ " var els=document.getElementsByName('" + name + "');"
                            		+ " for (var i=0;i<els.length;els[i++].value = '" + name + "');"
                            		+ "", getMethodName()));
                    actionLink.setController(new UpdateSelectedCurricularCourseController(context));
                    linkTableCell.setBody(actionLink);
                }
            }
        }
    }

    static private class UpdateSelectedCurricularCourseController extends HtmlActionLinkController {

        static private final long serialVersionUID = 1L;
        private final Context context;

        public UpdateSelectedCurricularCourseController(final Context context) {
            this.context = context;
        }

        @Override
        protected boolean isToSkipUpdate() {
            return false;
        }

        @Override
        public void linkPressed(IViewState viewState, HtmlActionLink link) {
            ((StudentOptionalEnrolmentBean) viewState.getMetaObject().getObject())
                    .setSelectedCurricularCourse((CurricularCourse) this.context.getChildDegreeModule());
        }

    }
}
