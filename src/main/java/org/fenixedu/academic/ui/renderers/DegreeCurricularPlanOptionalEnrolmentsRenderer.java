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
package net.sourceforge.fenixedu.presentationTier.renderers;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.studentEnrolment.StudentOptionalEnrolmentBean;
import net.sourceforge.fenixedu.domain.Branch;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeModuleScope;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.util.Bundle;

import org.apache.commons.beanutils.BeanComparator;
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
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

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

            if (studentOptionalEnrolmentBean.getDegreeCurricularPlan().isBoxStructure()) {
                generateCourseGroup(container, studentOptionalEnrolmentBean.getDegreeCurricularPlan().getRoot(), 0);
            } else {
                generateDCP(container, 0);
            }
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
                generateCourseGroup(blockContainer, (CourseGroup) context.getChildDegreeModule(), depth
                        + getWidthDecreasePerLevel());
            }
        }

        private ExecutionSemester getExecutionSemester() {
            return studentOptionalEnrolmentBean.getExecutionPeriod();
        }

        private void generateCurricularCourses(HtmlBlockContainer blockContainer, List<Context> contexts, int depth) {
            final HtmlTable table = new HtmlTable();
            blockContainer.addChild(table);
            table.setClasses(getTablesClasses());
            table.setStyle("width: " + (getInitialWidth() - depth) + "em; margin-left: " + depth + "em;");

            for (Context context : contexts) {
                final CurricularCourse curricularCourse = (CurricularCourse) context.getChildDegreeModule();

                if (!curricularCourse.isOptionalCurricularCourse()) {

                    final HtmlTableRow htmlTableRow = table.createRow();
                    HtmlTableCell cellName = htmlTableRow.createCell();
                    cellName.setClasses(getCurricularCourseNameClasses());
                    cellName.setBody(new HtmlText(curricularCourse.getName(getExecutionSemester())));

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
                    actionLink.setText(BundleUtil.getString(Bundle.ACADEMIC, "link.option.enrol.curricular.course"));
                    actionLink.setName("curricularCourseEnrolLink" + curricularCourse.getExternalId());
                    actionLink.setOnClick(String.format(
                            "$(this).closest('form').find('input[name=\\'method\\']').attr('value', '%s');", getMethodName()));
                    //actionLink.setOnClick(String.format("document.forms[0].method.value='%s';", getMethodName()));
                    actionLink.setController(new UpdateSelectedCurricularCourseController(curricularCourse));
                    linkTableCell.setBody(actionLink);
                }
            }
        }

        private void generateDCP(HtmlBlockContainer container, int depth) {
            Map<Branch, SortedSet<DegreeModuleScope>> branchMap =
                    getBranchMap(studentOptionalEnrolmentBean.getDegreeCurricularPlan(), getExecutionSemester());
            for (Entry<Branch, SortedSet<DegreeModuleScope>> entry : branchMap.entrySet()) {
                generateBranch(container, entry.getKey(), entry.getValue(), depth + getWidthDecreasePerLevel());
            }
        }

        private void generateBranch(HtmlBlockContainer container, final Branch branch, final Set<DegreeModuleScope> scopes,
                int depth) {
            final HtmlTable groupTable = new HtmlTable();
            container.addChild(groupTable);
            groupTable.setClasses(getTablesClasses());
            groupTable.setStyle("width: " + (getInitialWidth() - depth) + "em; margin-left: " + depth + "em;");

            final HtmlTableRow htmlTableRow = groupTable.createRow();
            htmlTableRow.setClasses(getGroupRowClasses());
            String name = branch.getName().trim();
            if (name.length() == 0) {
                name = "Tronco Comum";
            }
            htmlTableRow.createCell().setBody(new HtmlText(name));

            generateBranchScopes(container, scopes, depth + getWidthDecreasePerLevel());
        }

        private void generateBranchScopes(HtmlBlockContainer container, final Set<DegreeModuleScope> scopes, int depth) {
            final HtmlTable table = new HtmlTable();
            container.addChild(table);
            table.setClasses(getTablesClasses());
            table.setStyle("width: " + (getInitialWidth() - depth) + "em; margin-left: " + depth + "em;");

            for (DegreeModuleScope scope : scopes) {
                final HtmlTableRow htmlTableRow = table.createRow();
                HtmlTableCell cellName = htmlTableRow.createCell();
                cellName.setClasses(getCurricularCourseNameClasses());
                cellName.setBody(new HtmlText(scope.getCurricularCourse().getName(getExecutionSemester())));

                // Year
                final HtmlTableCell yearCell = htmlTableRow.createCell();
                yearCell.setClasses(getCurricularCourseYearClasses());
                yearCell.setBody(new HtmlText(RenderUtils.getResourceString("ACADEMIC_OFFICE_RESOURCES",
                        "label.scope.curricular.semester",
                        new Object[] { scope.getCurricularYear(), scope.getCurricularSemester() })));

                // Ects
                final HtmlTableCell ectsCell = htmlTableRow.createCell();
                ectsCell.setClasses(getCurricularCourseEctsClasses());
                final StringBuilder ects = new StringBuilder();
                ects.append(scope.getCurricularCourse().getEctsCredits(getExecutionSemester())).append(" ")
                        .append(BundleUtil.getString(Bundle.ACADEMIC, "credits.abbreviation"));
                ectsCell.setBody(new HtmlText(ects.toString()));

                // enrolment link
                final HtmlTableCell linkTableCell = htmlTableRow.createCell();
                linkTableCell.setClasses(getCurricularCourseLinkClasses());

                final HtmlActionLink actionLink = new HtmlActionLink();
                actionLink.setText(BundleUtil.getString(Bundle.ACADEMIC, "link.option.enrol.curricular.course"));
                actionLink.setName("curricularCourseEnrolLink" + scope.getCurricularCourse().getExternalId());
                actionLink.setOnClick(String.format(
                        "$(this).closest('form').find('input[name=\\'method\\']').attr('value', '%s');", getMethodName()));
                //actionLink.setOnClick(String.format("document.forms[0].method.value='%s';", getMethodName()));
                actionLink.setController(new UpdateSelectedCurricularCourseController(scope.getCurricularCourse()));
                linkTableCell.setBody(actionLink);
            }
        }

        private Map<Branch, SortedSet<DegreeModuleScope>> getBranchMap(final DegreeCurricularPlan degreeCurricularPlan,
                final ExecutionSemester executionSemester) {
            final Map<Branch, SortedSet<DegreeModuleScope>> branchMap =
                    new TreeMap<Branch, SortedSet<DegreeModuleScope>>(new BeanComparator("name"));
            for (final CurricularCourse curricularCourse : degreeCurricularPlan.getCurricularCoursesSet()) {
                for (final CurricularCourseScope scope : curricularCourse.getScopesSet()) {
                    if (scope.isActiveForExecutionPeriod(executionSemester)) {
                        addToMap(branchMap, scope);
                    }
                }
            }
            return branchMap;
        }

        private void addToMap(Map<Branch, SortedSet<DegreeModuleScope>> branchMap, CurricularCourseScope scope) {
            SortedSet<DegreeModuleScope> list = branchMap.get(scope.getBranch());
            if (list == null) {
                list =
                        new TreeSet<DegreeModuleScope>(
                                DegreeModuleScope.COMPARATOR_BY_CURRICULAR_YEAR_AND_SEMESTER_AND_CURRICULAR_COURSE_NAME);
                branchMap.put(scope.getBranch(), list);
            }
            list.add(scope.getDegreeModuleScopeCurricularCourseScope());
        }
    }

    static private class UpdateSelectedCurricularCourseController extends HtmlActionLinkController {

        static private final long serialVersionUID = 1L;
        private final CurricularCourse curricularCourse;

        public UpdateSelectedCurricularCourseController(final CurricularCourse curricularCourse) {
            this.curricularCourse = curricularCourse;
        }

        @Override
        protected boolean isToSkipUpdate() {
            return false;
        }

        @Override
        public void linkPressed(IViewState viewState, HtmlActionLink link) {
            ((StudentOptionalEnrolmentBean) viewState.getMetaObject().getObject())
                    .setSelectedCurricularCourse(this.curricularCourse);
        }

    }
}
