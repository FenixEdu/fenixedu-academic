package net.sourceforge.fenixedu.presentationTier.renderers;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;

import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.studentEnrolment.StudentOptionalEnrolmentBean;
import net.sourceforge.fenixedu.domain.Branch;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeModuleScope;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.renderers.InputRenderer;
import net.sourceforge.fenixedu.renderers.components.HtmlBlockContainer;
import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlLink;
import net.sourceforge.fenixedu.renderers.components.HtmlTable;
import net.sourceforge.fenixedu.renderers.components.HtmlTableCell;
import net.sourceforge.fenixedu.renderers.components.HtmlTableRow;
import net.sourceforge.fenixedu.renderers.components.HtmlText;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.commons.beanutils.BeanComparator;

public class DegreeCurricularPlanOptionalEnrolmentsRenderer extends InputRenderer {
    
    private static final ResourceBundle enumerationResources = ResourceBundle.getBundle("resources.EnumerationResources");
    
    private final ResourceBundle academicAdminOfficeResources = ResourceBundle.getBundle("resources.AcademicAdminOffice");

    
    private Integer initialWidth = 70;

    private Integer widthDecreasePerLevel = 3;

    private String tablesClasses = "showinfo3 mvert0";

    private String groupRowClasses = "bgcolor2";
    
    private String curricularCoursesToEnrol = "smalltxt, smalltxt aright, smalltxt aright, aright";
    
    private String linkURL;
    
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

    public String getLinkURL() {
        return linkURL;
    }

    public void setLinkURL(String linkURL) {
        this.linkURL = linkURL;
    }




    public DegreeCurricularPlanOptionalEnrolmentsRenderer() {
	super();
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
	return new StudentCurricularPlanEnrolmentLayout();
    }

    private class StudentCurricularPlanEnrolmentLayout extends Layout {

	private StudentOptionalEnrolmentBean studentOptionalEnrolmentBean = null;

	
	@Override
	public HtmlComponent createComponent(Object object, Class type) {
	    studentOptionalEnrolmentBean = (StudentOptionalEnrolmentBean) object;
	    

	    HtmlBlockContainer container = new HtmlBlockContainer();
	    
	    if (studentOptionalEnrolmentBean == null) {
		return new HtmlText();
	    } 

	    if(studentOptionalEnrolmentBean.getDegreeCurricularPlan().isBolonha()) {
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

	    final List<Context> childCourseGroupContexts = courseGroup.getChildContexts(CourseGroup.class, studentOptionalEnrolmentBean.getExecutionPeriod());
	    final List<Context> childCurricularCourseContexts = courseGroup.getChildContexts(CurricularCourse.class, studentOptionalEnrolmentBean.getExecutionPeriod());

	    Collections.sort(childCourseGroupContexts, new BeanComparator("childOrder"));
	    Collections.sort(childCurricularCourseContexts, new BeanComparator("childOrder"));

	    generateCurricularCourses(blockContainer, childCurricularCourseContexts, depth + getWidthDecreasePerLevel());

	    for (Context context : childCourseGroupContexts) {
		generateCourseGroup(blockContainer, (CourseGroup) context.getChildDegreeModule(), depth + getWidthDecreasePerLevel()); 
	    }
	}
	

	private void generateCurricularCourses(HtmlBlockContainer blockContainer, List<Context> contexts, int depth) {
	    final HtmlTable table = new HtmlTable();
	    blockContainer.addChild(table);
	    table.setClasses(getTablesClasses());
	    table.setStyle("width: " + (getInitialWidth() - depth) + "em; margin-left: " + depth + "em;");

	    for (Context context : contexts) {
		final CurricularCourse curricularCourse = (CurricularCourse) context.getChildDegreeModule();
		if(!curricularCourse.isOptionalCurricularCourse()) {

		    final HtmlTableRow htmlTableRow = table.createRow();
		    HtmlTableCell cellName = htmlTableRow.createCell();
		    cellName.setClasses(getCurricularCourseNameClasses());
		    cellName.setBody(new HtmlText(curricularCourse.getName()));

		    //Year
		    final HtmlTableCell yearCell = htmlTableRow.createCell();
		    yearCell.setClasses(getCurricularCourseYearClasses());		
		    yearCell.setBody(new HtmlText(context.getCurricularPeriod().getFullLabel()));

		    //Ects
		    final HtmlTableCell ectsCell = htmlTableRow.createCell();
		    ectsCell.setClasses(getCurricularCourseEctsClasses());

		    final StringBuilder ects = new StringBuilder();
		    ects.append(curricularCourse.getEctsCredits()).append(" ").append(academicAdminOfficeResources.getString("credits.abbreviation"));
		    ectsCell.setBody(new HtmlText(ects.toString()));

		    //link inscrição
		    final HtmlTableCell linkTableCell = htmlTableRow.createCell();
		    linkTableCell.setClasses(getCurricularCourseLinkClasses());
		    final HtmlLink htmlLink = new HtmlLink();
		    linkTableCell.setBody(htmlLink);
		    htmlLink.setText(academicAdminOfficeResources.getString("link.option.enrol.curricular.course"));
		    htmlLink.setUrl(getLinkURL());
		    htmlLink.setParameter("scpID", studentOptionalEnrolmentBean.getStudentCurricularPlan().getIdInternal());
		    htmlLink.setParameter("executionPeriodID", studentOptionalEnrolmentBean.getExecutionPeriod().getIdInternal());
		    htmlLink.setParameter("curriculumGroupID", studentOptionalEnrolmentBean.getCurriculumGroup().getIdInternal());
		    htmlLink.setParameter("contextID", studentOptionalEnrolmentBean.getContex().getIdInternal());
		    htmlLink.setParameter("optionalCCID", context.getChildDegreeModule().getIdInternal());
		    htmlLink.setParameter("degreeType", studentOptionalEnrolmentBean.getDegreeType().toString());
		    htmlLink.setParameter("degreeID", studentOptionalEnrolmentBean.getDegree().getIdInternal().toString());
		    htmlLink.setParameter("dcpID", studentOptionalEnrolmentBean.getDegreeCurricularPlan().getIdInternal().toString());
		}		
	    }	    
	}
	
	private void generateDCP(HtmlBlockContainer container, int depth) {
	    Map<Branch, SortedSet<DegreeModuleScope>> branchMap = getBranchMap(studentOptionalEnrolmentBean.getDegreeCurricularPlan(), studentOptionalEnrolmentBean.getExecutionPeriod());
	    for (Entry<Branch, SortedSet<DegreeModuleScope>> entry : branchMap.entrySet()) {
		generateBranch(container, entry.getKey(), entry.getValue(), depth + getWidthDecreasePerLevel());
	    }
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
	
	private void generateBranchScopes(HtmlBlockContainer container, final Set<DegreeModuleScope> scopes, int depth) {
	    final HtmlTable table = new HtmlTable();
	    container.addChild(table);
	    table.setClasses(getTablesClasses());
	    table.setStyle("width: " + (getInitialWidth() - depth) + "em; margin-left: " + depth + "em;");
	    
	    for (DegreeModuleScope scope : scopes) {
		final HtmlTableRow htmlTableRow = table.createRow();
		HtmlTableCell cellName = htmlTableRow.createCell();
		cellName.setClasses(getCurricularCourseNameClasses());
		cellName.setBody(new HtmlText(scope.getCurricularCourse().getName()));

		//Year
		final HtmlTableCell yearCell = htmlTableRow.createCell();
		yearCell.setClasses(getCurricularCourseYearClasses());
		yearCell.setBody(new HtmlText(RenderUtils.getResourceString("ACADEMIC_OFFICE_RESOURCES", "label.scope.curricular.semester", new Object[] {scope.getCurricularYear(), scope.getCurricularSemester()})));
		
		//Ects
		final HtmlTableCell ectsCell = htmlTableRow.createCell();
		ectsCell.setClasses(getCurricularCourseEctsClasses());
		final StringBuilder ects = new StringBuilder();
		ects.append(scope.getCurricularCourse().getEctsCredits()).append(" ").append(academicAdminOfficeResources.getString("credits.abbreviation"));
		ectsCell.setBody(new HtmlText(ects.toString()));

		//link inscrição
		final HtmlTableCell linkTableCell = htmlTableRow.createCell();
		linkTableCell.setClasses(getCurricularCourseLinkClasses());
		final HtmlLink htmlLink = new HtmlLink();
		linkTableCell.setBody(htmlLink);
		htmlLink.setText(academicAdminOfficeResources.getString("link.option.enrol.curricular.course"));
		htmlLink.setUrl(getLinkURL());
		htmlLink.setParameter("scpID", studentOptionalEnrolmentBean.getStudentCurricularPlan().getIdInternal());
		htmlLink.setParameter("executionPeriodID", studentOptionalEnrolmentBean.getExecutionPeriod().getIdInternal());
		htmlLink.setParameter("curriculumGroupID", studentOptionalEnrolmentBean.getCurriculumGroup().getIdInternal());
		htmlLink.setParameter("contextID", studentOptionalEnrolmentBean.getContex().getIdInternal());
		htmlLink.setParameter("optionalCCID", scope.getCurricularCourse().getIdInternal());
		htmlLink.setParameter("degreeType", studentOptionalEnrolmentBean.getDegreeType().toString());
		htmlLink.setParameter("degreeID", studentOptionalEnrolmentBean.getDegree().getIdInternal().toString());
		htmlLink.setParameter("dcpID", studentOptionalEnrolmentBean.getDegreeCurricularPlan().getIdInternal().toString());

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

	private void addToMap(Map<Branch, SortedSet<DegreeModuleScope>> branchMap, CurricularCourseScope scope) {
	    SortedSet<DegreeModuleScope> list = branchMap.get(scope.getBranch());
	    if(list == null) {
		list = new TreeSet<DegreeModuleScope>(DegreeModuleScope.COMPARATOR_BY_CURRICULAR_YEAR_AND_SEMESTER_AND_CURRICULAR_COURSE_NAME);
		branchMap.put(scope.getBranch(), list);
	    }
	    list.add(scope.getDegreeModuleScopeCurricularCourseScope());
	}

    }
}
