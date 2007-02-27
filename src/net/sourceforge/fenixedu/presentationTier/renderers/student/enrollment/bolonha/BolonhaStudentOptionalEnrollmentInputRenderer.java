package net.sourceforge.fenixedu.presentationTier.renderers.student.enrollment.bolonha;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;

import net.sourceforge.fenixedu.dataTransferObject.student.enrollment.bolonha.BolonhaStudentOptionalEnrollmentBean;
import net.sourceforge.fenixedu.domain.Branch;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeModuleScope;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.renderers.InputRenderer;
import net.sourceforge.fenixedu.renderers.components.HtmlActionLink;
import net.sourceforge.fenixedu.renderers.components.HtmlBlockContainer;
import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlTable;
import net.sourceforge.fenixedu.renderers.components.HtmlTableCell;
import net.sourceforge.fenixedu.renderers.components.HtmlTableRow;
import net.sourceforge.fenixedu.renderers.components.HtmlText;
import net.sourceforge.fenixedu.renderers.components.controllers.HtmlActionLinkController;
import net.sourceforge.fenixedu.renderers.components.state.IViewState;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.commons.beanutils.BeanComparator;

public class BolonhaStudentOptionalEnrollmentInputRenderer extends InputRenderer {

    private static final ResourceBundle studentResources = ResourceBundle
	    .getBundle("resources.StudentResources");

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

	    if (bolonhaStudentOptionalEnrollmentBean.getDegreeCurricularPlan().isBolonha()) {
		generateCourseGroup(container, bolonhaStudentOptionalEnrollmentBean.getDegreeCurricularPlan().getRoot(), 0);
	    } else {
		generateDCP(container, 0);
	    }

	    return container;
	}
	
	// Pre-Bolonha Structure
	private void generateDCP(HtmlBlockContainer container, int depth) {
	    final Map<Branch, SortedSet<DegreeModuleScope>> branchMap = getBranchMap(bolonhaStudentOptionalEnrollmentBean.getDegreeCurricularPlan(), bolonhaStudentOptionalEnrollmentBean.getExecutionPeriod());
	    for (final Entry<Branch, SortedSet<DegreeModuleScope>> entry : branchMap.entrySet()) {
		generateBranch(container, entry.getKey(), entry.getValue(), depth + getWidthDecreasePerLevel());
	    }
	}
	
	private Map<Branch, SortedSet<DegreeModuleScope>> getBranchMap(final DegreeCurricularPlan degreeCurricularPlan, final ExecutionPeriod executionPeriod){
	    final Map<Branch, SortedSet<DegreeModuleScope>> branchMap = new TreeMap<Branch, SortedSet<DegreeModuleScope>>(new BeanComparator("name"));
	    for (final CurricularCourse curricularCourse : degreeCurricularPlan.getCurricularCoursesSet()) {
		for (final CurricularCourseScope scope : curricularCourse.getScopesSet()) {
		    if(scope.isActiveForExecutionPeriod(executionPeriod)) {
			addToMap(branchMap, scope);
		    }
		}
	    }
	    return branchMap;
	}
	
	private void addToMap(final Map<Branch, SortedSet<DegreeModuleScope>> branchMap, final CurricularCourseScope scope) {
	    SortedSet<DegreeModuleScope> list = branchMap.get(scope.getBranch());
	    if(list == null) {
		list = new TreeSet<DegreeModuleScope>(DegreeModuleScope.COMPARATOR_BY_CURRICULAR_YEAR_AND_SEMESTER_AND_CURRICULAR_COURSE_NAME);
		branchMap.put(scope.getBranch(), list);
	    }
	    list.add(scope.getDegreeModuleScopeCurricularCourseScope());
	}
	
	private void generateBranch(HtmlBlockContainer container, final Branch branch, final Set<DegreeModuleScope> scopes, int depth) {
	    final HtmlTable groupTable = new HtmlTable();
	    container.addChild(groupTable);
	    groupTable.setClasses(getTablesClasses());
	    groupTable.setStyle("width: " + (getInitialWidth() - depth) + "em; margin-left: " + depth + "em;");

	    final HtmlTableRow htmlTableRow = groupTable.createRow();
	    htmlTableRow.setClasses(getGroupRowClasses());
	    String name = branch.getName().trim();
	    if(name.length() == 0) {
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
		yearCell.setBody(new HtmlText(RenderUtils.getResourceString("STUDENT_RESOURCES", "label.scope.curricular.semester", new Object[] { scope.getCurricularYear(), scope.getCurricularSemester() })));

		// Ects
		final HtmlTableCell ectsCell = htmlTableRow.createCell();
		ectsCell.setClasses(getCurricularCourseEctsClasses());
		final StringBuilder ects = new StringBuilder();
		ects.append(scope.getCurricularCourse().getEctsCredits()).append(" ").append(studentResources.getString("label.credits.abbreviation"));
		ectsCell.setBody(new HtmlText(ects.toString()));

		// Enrollment Link
		final HtmlTableCell linkTableCell = htmlTableRow.createCell();
		linkTableCell.setClasses(getCurricularCourseLinkClasses());

		final HtmlActionLink actionLink = new HtmlActionLink();
		actionLink.setText(studentResources.getString("label.enroll"));
		actionLink.setName("optionalCurricularCourseEnrolLink" + scope.getCurricularCourse().getIdInternal());
		actionLink.setOnClick("document.forms[0].method.value='enrolInOptionalCurricularCourse';");
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

	    final List<Context> childCourseGroupContexts = courseGroup.getChildContexts(CourseGroup.class, bolonhaStudentOptionalEnrollmentBean.getExecutionPeriod());
	    final List<Context> childCurricularCourseContexts = courseGroup.getChildContexts(CurricularCourse.class, bolonhaStudentOptionalEnrollmentBean.getExecutionPeriod());

	    Collections.sort(childCourseGroupContexts, new BeanComparator("childOrder"));
	    Collections.sort(childCurricularCourseContexts, new BeanComparator("childOrder"));

	    generateCurricularCourses(blockContainer, childCurricularCourseContexts, depth + getWidthDecreasePerLevel());

	    for (final Context context : childCourseGroupContexts) {
		generateCourseGroup(blockContainer, (CourseGroup) context.getChildDegreeModule(), depth + getWidthDecreasePerLevel());
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
		    cellName.setBody(new HtmlText(curricularCourse.getName()));

		    // Year
		    final HtmlTableCell yearCell = htmlTableRow.createCell();
		    yearCell.setClasses(getCurricularCourseYearClasses());
		    yearCell.setBody(new HtmlText(context.getCurricularPeriod().getFullLabel()));

		    // Ects
		    final HtmlTableCell ectsCell = htmlTableRow.createCell();
		    ectsCell.setClasses(getCurricularCourseEctsClasses());

		    final StringBuilder ects = new StringBuilder();
		    ects.append(curricularCourse.getEctsCredits()).append(" ").append(studentResources.getString("label.credits.abbreviation"));
		    ectsCell.setBody(new HtmlText(ects.toString()));

		    // Enrollment Link
		    final HtmlTableCell linkTableCell = htmlTableRow.createCell();
		    linkTableCell.setClasses(getCurricularCourseLinkClasses());

		    final HtmlActionLink actionLink = new HtmlActionLink();
		    actionLink.setText(studentResources.getString("label.enroll"));
		    actionLink.setName("optionalCurricularCourseEnrolLink" + curricularCourse.getIdInternal());
		    actionLink.setOnClick("document.forms[0].method.value='enrolInOptionalCurricularCourse';");
		    actionLink.setController(new UpdateSelectedOptionalCurricularCourseController(curricularCourse));
		    linkTableCell.setBody(actionLink);
		}
	    }
	}
    }

    private static class UpdateSelectedOptionalCurricularCourseController extends
	    HtmlActionLinkController {

	private CurricularCourse curricularCourse;

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
