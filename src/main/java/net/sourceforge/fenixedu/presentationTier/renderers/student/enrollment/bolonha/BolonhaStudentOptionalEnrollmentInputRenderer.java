package net.sourceforge.fenixedu.presentationTier.renderers.student.enrollment.bolonha;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import net.sourceforge.fenixedu.dataTransferObject.student.enrollment.bolonha.BolonhaStudentOptionalEnrollmentBean;
import net.sourceforge.fenixedu.domain.Branch;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeModuleScope;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.organizationalStructure.DepartmentUnit;
import net.sourceforge.fenixedu.util.StringUtils;

import org.apache.commons.beanutils.BeanComparator;

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
import pt.utl.ist.fenix.tools.util.i18n.Language;

public class BolonhaStudentOptionalEnrollmentInputRenderer extends InputRenderer {

    private final ResourceBundle studentResources = ResourceBundle.getBundle("resources.StudentResources", Language.getLocale());

    private Integer initialWidth = 70;

    private Integer widthDecreasePerLevel = 3;

    private String tablesClasses = "showinfo3 mvert0";

    private String groupRowClasses = "bgcolor2";

    private String curricularCoursesToEnrol = "smalltxt, smalltxt aright, smalltxt aright, aright";

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

    public BolonhaStudentOptionalEnrollmentInputRenderer() {
        super();
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
        return new BolonhaStudentOptionalEnrolmentLayout();
    }

    private class BolonhaStudentOptionalEnrolmentLayout extends Layout {

        private BolonhaStudentOptionalEnrollmentBean bolonhaStudentOptionalEnrollmentBean = null;

        @Override
        public HtmlComponent createComponent(Object object, Class type) {
            bolonhaStudentOptionalEnrollmentBean = (BolonhaStudentOptionalEnrollmentBean) object;

            if (bolonhaStudentOptionalEnrollmentBean == null) {
                return new HtmlText();
            }

            final HtmlBlockContainer container = new HtmlBlockContainer();

            if (bolonhaStudentOptionalEnrollmentBean.getDegreeCurricularPlan().isBoxStructure()) {
                generateCourseGroup(container, bolonhaStudentOptionalEnrollmentBean.getDegreeCurricularPlan().getRoot(), 0);
            } else {
                generateDCP(container, 0);
            }

            return container;
        }

        // Pre-Bolonha Structure
        private void generateDCP(HtmlBlockContainer container, int depth) {
            final Map<Branch, SortedSet<DegreeModuleScope>> branchMap =
                    getBranchMap(bolonhaStudentOptionalEnrollmentBean.getDegreeCurricularPlan(),
                            bolonhaStudentOptionalEnrollmentBean.getExecutionPeriod());
            for (final Entry<Branch, SortedSet<DegreeModuleScope>> entry : branchMap.entrySet()) {
                generateBranch(container, entry.getKey(), entry.getValue(), depth + getWidthDecreasePerLevel());
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

        private void addToMap(final Map<Branch, SortedSet<DegreeModuleScope>> branchMap, final CurricularCourseScope scope) {
            SortedSet<DegreeModuleScope> list = branchMap.get(scope.getBranch());
            if (list == null) {
                list =
                        new TreeSet<DegreeModuleScope>(
                                DegreeModuleScope.COMPARATOR_BY_CURRICULAR_YEAR_AND_SEMESTER_AND_CURRICULAR_COURSE_NAME);
                branchMap.put(scope.getBranch(), list);
            }
            list.add(scope.getDegreeModuleScopeCurricularCourseScope());
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

        private void generateBranchScopes(final HtmlBlockContainer container, final Set<DegreeModuleScope> scopes, int depth) {

            final HtmlTable table = new HtmlTable();
            container.addChild(table);
            table.setClasses(getTablesClasses());
            table.setStyle("width: " + (getInitialWidth() - depth) + "em; margin-left: " + depth + "em;");

            for (final DegreeModuleScope scope : scopes) {
                final HtmlTableRow htmlTableRow = table.createRow();
                HtmlTableCell cellName = htmlTableRow.createCell();
                cellName.setClasses(getCurricularCourseNameClasses());
                cellName.setBody(new HtmlText(scope.getCurricularCourse().getName()));

                // Year
                final HtmlTableCell yearCell = htmlTableRow.createCell();
                yearCell.setClasses(getCurricularCourseYearClasses());
                yearCell.setBody(new HtmlText(RenderUtils.getResourceString("STUDENT_RESOURCES",
                        "label.scope.curricular.semester",
                        new Object[] { scope.getCurricularYear(), scope.getCurricularSemester() })));

                // Ects
                final HtmlTableCell ectsCell = htmlTableRow.createCell();
                ectsCell.setClasses(getCurricularCourseEctsClasses());
                final StringBuilder ects = new StringBuilder();
                ects.append(scope.getCurricularCourse().getEctsCredits()).append(" ")
                        .append(studentResources.getString("label.credits.abbreviation"));
                ectsCell.setBody(new HtmlText(ects.toString()));

                // Enrollment Link
                final HtmlTableCell linkTableCell = htmlTableRow.createCell();
                linkTableCell.setClasses(getCurricularCourseLinkClasses());

                final HtmlActionLink actionLink = new HtmlActionLink();
                actionLink.setText(studentResources.getString("label.enroll"));
                actionLink.setName("optionalCurricularCourseEnrolLink" + scope.getCurricularCourse().getExternalId());
                actionLink
                        .setOnClick("$(this).closest('form').find('input[name=\\'method\\']').attr('value', 'enrolInOptionalCurricularCourse');");
                //actionLink.setOnClick("document.forms[2].method.value='enrolInOptionalCurricularCourse';");
                actionLink.setController(new UpdateSelectedOptionalCurricularCourseController(scope.getCurricularCourse()));
                linkTableCell.setBody(actionLink);
            }
        }

        // Bolonha Structure
        private void generateCourseGroup(HtmlBlockContainer blockContainer, CourseGroup courseGroup, int depth) {
            final HtmlTable groupTable = new HtmlTable();
            blockContainer.addChild(groupTable);
            groupTable.setClasses(getTablesClasses());
            groupTable.setStyle("width: " + (getInitialWidth() - depth) + "em; margin-left: " + depth + "em;");

            final HtmlTableRow htmlTableRow = groupTable.createRow();
            htmlTableRow.setClasses(getGroupRowClasses());
            htmlTableRow.createCell().setBody(new HtmlText(courseGroup.getName()));

            final List<Context> childCourseGroupContexts =
                    courseGroup.getValidChildContexts(CourseGroup.class,
                            bolonhaStudentOptionalEnrollmentBean.getExecutionPeriod());
            final List<Context> childCurricularCourseContexts =
                    courseGroup.getValidChildContexts(CurricularCourse.class,
                            bolonhaStudentOptionalEnrollmentBean.getExecutionPeriod());

            Collections.sort(childCourseGroupContexts, new BeanComparator("childOrder"));
            Collections.sort(childCurricularCourseContexts, new BeanComparator("childOrder"));

            generateCurricularCourses(blockContainer, childCurricularCourseContexts, depth + getWidthDecreasePerLevel());

            for (final Context context : childCourseGroupContexts) {
                generateCourseGroup(blockContainer, (CourseGroup) context.getChildDegreeModule(), depth
                        + getWidthDecreasePerLevel());
            }
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
                    cellName.setBody(new HtmlText(getCurricularCoursePresentationName(curricularCourse)));

                    // Year
                    final HtmlTableCell yearCell = htmlTableRow.createCell();
                    yearCell.setClasses(getCurricularCourseYearClasses());
                    yearCell.setBody(new HtmlText(context.getCurricularPeriod().getFullLabel()));

                    // Ects
                    final HtmlTableCell ectsCell = htmlTableRow.createCell();
                    ectsCell.setClasses(getCurricularCourseEctsClasses());

                    final StringBuilder ects = new StringBuilder();
                    ects.append(curricularCourse.getEctsCredits()).append(" ")
                            .append(studentResources.getString("label.credits.abbreviation"));
                    ectsCell.setBody(new HtmlText(ects.toString()));

                    // Enrollment Link
                    final HtmlTableCell linkTableCell = htmlTableRow.createCell();
                    linkTableCell.setClasses(getCurricularCourseLinkClasses());

                    final HtmlActionLink actionLink = new HtmlActionLink();
                    actionLink.setText(studentResources.getString("label.enroll"));
                    actionLink.setName("optionalCurricularCourseEnrolLink" + curricularCourse.getExternalId() + "_"
                            + context.getExternalId());
                    actionLink
                            .setOnClick("$(this).closest('form').find('input[name=\\'method\\']').attr('value', 'enrolInOptionalCurricularCourse');");
                    //actionLink.setOnClick("document.forms[2].method.value='enrolInOptionalCurricularCourse';");
                    actionLink.setController(new UpdateSelectedOptionalCurricularCourseController(curricularCourse));
                    linkTableCell.setBody(actionLink);
                }
            }
        }

        private String getCurricularCoursePresentationName(final CurricularCourse curricularCourse) {
            String departmentName = null;
            if (curricularCourse.hasCompetenceCourse()) {
                final DepartmentUnit unit = curricularCourse.getCompetenceCourse().getDepartmentUnit();
                if (unit != null) {
                    departmentName = unit.getName();
                }
            }

            return (StringUtils.isEmpty(departmentName) ? "" : departmentName + " - ") + curricularCourse.getName();
        }
    }

    private class UpdateSelectedOptionalCurricularCourseController extends HtmlActionLinkController {

        static private final long serialVersionUID = 1L;
        private final CurricularCourse curricularCourse;

        public UpdateSelectedOptionalCurricularCourseController(final CurricularCourse curricularCourse) {
            this.curricularCourse = curricularCourse;
        }

        @Override
        protected boolean isToSkipUpdate() {
            return false;
        }

        @Override
        public void linkPressed(IViewState viewState, HtmlActionLink link) {
            ((BolonhaStudentOptionalEnrollmentBean) viewState.getMetaObject().getObject())
                    .setSelectedOptionalCurricularCourse(this.curricularCourse);
        }

    }

}
