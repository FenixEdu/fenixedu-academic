package net.sourceforge.fenixedu.presentationTier.renderers;

import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumLine;
import net.sourceforge.fenixedu.renderers.OutputRenderer;
import net.sourceforge.fenixedu.renderers.components.HtmlBlockContainer;
import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlLink;
import net.sourceforge.fenixedu.renderers.components.HtmlTable;
import net.sourceforge.fenixedu.renderers.components.HtmlTableCell;
import net.sourceforge.fenixedu.renderers.components.HtmlTableRow;
import net.sourceforge.fenixedu.renderers.components.HtmlText;
import net.sourceforge.fenixedu.renderers.components.HtmlTableCell.CellType;
import net.sourceforge.fenixedu.renderers.layouts.Layout;

import org.apache.commons.beanutils.BeanComparator;

public class StudentCurricularPlanRenderer extends OutputRenderer {

    private static final ResourceBundle studentResources = ResourceBundle.getBundle("resources.StudentResources");
    
    private static final ResourceBundle enumerationResources = ResourceBundle.getBundle("resources.EnumerationResources");

    private Boolean organizedByGroups;

    private Integer initialWidth = Integer.valueOf(70);

    private Integer widthDecreasePerLevel = Integer.valueOf(3);

    private String tablesClasses = "showinfo3 mvert0";

    private String groupRowClasses = "bgcolor2";

    private String groupNameClasses = "aleft";

    private String enrolmentClasses = "smalltxt aright, smalltxt aright, aright";

    private String enrolmentYearClasses = getEnrolmentClasses()[0];

    private String enrolmentSemesterClasses = getEnrolmentClasses()[1];

    private String enrolmentInfoClasses = getEnrolmentClasses()[2];

    public StudentCurricularPlanRenderer() {
	super();
    }

    public Boolean isOrganizedByGroups() {
	return organizedByGroups;
    }

    public void setOrganizedByGroups(Boolean organizedByGroups) {
	this.organizedByGroups = organizedByGroups;
    }

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

    public String getGroupNameClasses() {
	return groupNameClasses;
    }

    public void setGroupNameClasses(String groupNameClasses) {
	this.groupNameClasses = groupNameClasses;
    }

    private String[] getEnrolmentClasses() {
	return enrolmentClasses.split(",");
    }

    public void setEnrolmentClasses(String enrolmentClasses) {
	this.enrolmentClasses = enrolmentClasses;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
	return new StudentCurricularPlanLayout();
    }

    private class StudentCurricularPlanLayout extends Layout {

	@Override
	public HtmlComponent createComponent(Object object, Class type) {
	    final StudentCurricularPlan studentCurricularPlan = (StudentCurricularPlan) object;

	    final HtmlBlockContainer scpDiv = new HtmlBlockContainer();

	    if (studentCurricularPlan == null) {
		return new HtmlText(studentResources.getString("message.no.curricularplan"));
	    } else if (isOrganizedByGroups() == null) {
		setOrganizedByGroups(studentCurricularPlan.isBolonha() ? Boolean.TRUE : Boolean.FALSE);
	    } else if (isOrganizedByGroups() && !studentCurricularPlan.isBolonha()) {
		return new HtmlText(studentResources.getString("not.applicable"));
	    }

	    if (isOrganizedByGroups()) {
		generateGroup(scpDiv, studentCurricularPlan.getRoot(), 0);
	    } else {
		generateEnrolmentsExecutionPeriods(scpDiv, studentCurricularPlan);
	    }

	    return scpDiv;
	}

	private void generateGroup(HtmlBlockContainer scpDiv, final CurriculumGroup group, int depth) {
	    final HtmlTable groupTable = new HtmlTable();
	    scpDiv.addChild(groupTable);
	    groupTable.setClasses(getTablesClasses());
	    groupTable.setStyle("width: " + (getInitialWidth() - depth) + "em; margin-left: " + depth + "em;");

	    final HtmlTableRow groupRow = groupTable.createRow();
	    groupRow.setClasses(getGroupRowClasses());

	    final HtmlTableCell groupNameCell = groupRow.createCell();
	    groupNameCell.setType(CellType.HEADER);
	    groupNameCell.setClasses(getGroupNameClasses());
	    groupNameCell.setColspan(5);
	    groupNameCell.setBody((group.isRoot()) ? generateRootNameLink(group) : new HtmlText(group.getName()));

	    generateGroupLines(scpDiv, depth, group);
	    generateGroupChilds(scpDiv, depth, group);
	}

	private HtmlLink generateRootNameLink(CurriculumGroup root) {
	    final HtmlLink rootLink = new HtmlLink();

	    rootLink.setText(root.getName());

	    rootLink.setUrl("/publico/degreeSite/showDegreeCurricularPlanBolonha.faces?");
	    rootLink.setModuleRelative(false);

	    rootLink.setParameter("degreeID", root.getStudentCurricularPlan().getDegree()
		    .getIdInternal());
	    rootLink.setParameter("degreeCurricularPlanID", root.getStudentCurricularPlan().getDegreeCurricularPlan()
		    .getIdInternal());
	    rootLink.setParameter("executionPeriodOID", ExecutionPeriod.readActualExecutionPeriod()
		    .getIdInternal());
	    rootLink.setParameter("organizeBy", "groups");
	    rootLink.setParameter("showRules", "false");
	    rootLink.setParameter("hideCourses", "false");

	    rootLink.setTarget("_blank");

	    return rootLink;
	}

	private void generateGroupLines(HtmlBlockContainer scpDiv, int depth, CurriculumGroup group) {
	    final Set<CurriculumLine> sortedCurriculumLines = new TreeSet<CurriculumLine>(CurriculumLine.COMPARATOR_BY_NAME);
	    sortedCurriculumLines.addAll(group.getCurriculumLines());
	    
	    if (!sortedCurriculumLines.isEmpty()) {
		final HtmlTable groupLinesTable = new HtmlTable();
		scpDiv.addChild(groupLinesTable);
		groupLinesTable.setClasses(getTablesClasses());
		groupLinesTable.setStyle("width: "
			+ (getInitialWidth() - depth - getWidthDecreasePerLevel()) + "em; margin-left: "
			+ (depth + getWidthDecreasePerLevel()) + "em;");

		for (final CurriculumLine curriculumLine : sortedCurriculumLines) {
		    generateLine(groupLinesTable, curriculumLine);
		}
	    }
	}

	private void generateLine(final HtmlTable groupLinesTable, final CurriculumLine curriculumLine) {
	    final HtmlTableRow lineRow = groupLinesTable.createRow();

	    if (curriculumLine.isEnrolment()) {
		generateEnrolment(curriculumLine, lineRow);
	    }
	}

	private void generateEnrolment(final CurriculumLine curriculumLine, final HtmlTableRow lineRow) {
	    final Enrolment enrolment = (Enrolment) curriculumLine;

	    // Name
	    final HtmlTableCell name = lineRow.createCell();
	    name.setBody(generateExecutionCourseLink(enrolment));

	    // Year
	    final HtmlTableCell yearCell = lineRow.createCell();
	    yearCell.setClasses(enrolmentYearClasses);

	    final StringBuilder year = new StringBuilder();
	    year.append(enrolment.getExecutionPeriod().getExecutionYear().getYear());
	    yearCell.setBody(new HtmlText(year.toString()));

	    // Semester
	    final HtmlTableCell semesterCell = lineRow.createCell();
	    semesterCell.setClasses(enrolmentSemesterClasses);

	    final StringBuilder semester = new StringBuilder();
	    semester.append(enrolment.getExecutionPeriod().getSemester().toString());
	    semester.append(" ");
	    semester.append(enumerationResources.getString("SEMESTER.ABBREVIATION"));
	    semesterCell.setBody(new HtmlText(semester.toString()));

	    // Enrolment
	    final HtmlTableCell enrolmentCell = lineRow.createCell();
	    enrolmentCell.setClasses(enrolmentInfoClasses);

	    if (enrolment.isApproved()) {
		final String grade = enrolment.getLatestEnrolmentEvaluation().getGrade();
		enrolmentCell.setBody(new HtmlText(grade));
	    } else {
		final String enrolmentState = enumerationResources.getString(enrolment
			.getEnrollmentState().toString());
		enrolmentCell.setBody(new HtmlText(enrolmentState));
	    }
	}

	private HtmlComponent generateExecutionCourseLink(Enrolment enrolment) {
	    final ExecutionCourse executionCourse = enrolment.getExecutionCourseFor(enrolment.getExecutionPeriod());
	    
	    if (executionCourse == null) {
		return new HtmlText(enrolment.getName());
	    } else {
		final HtmlLink executionCourseLink = new HtmlLink();

		executionCourseLink.setText(enrolment.getName());

		executionCourseLink.setUrl("/publico/executionCourse.do?method=firstPage");
		executionCourseLink.setModuleRelative(false);

		executionCourseLink.setParameter("executionCourseID", executionCourse.getIdInternal());

		executionCourseLink.setTarget(HtmlLink.Target.BLANK);

		return executionCourseLink;
	    }
	}

	private void generateGroupChilds(HtmlBlockContainer scpDiv, int depth, final CurriculumGroup group) {
	    final Set<CurriculumGroup> sortedCurriculumGroups = new TreeSet<CurriculumGroup>(new BeanComparator("childOrder"));
	    sortedCurriculumGroups.addAll(group.getCurriculumGroups());
	    
	    for (final CurriculumGroup curriculumGroup : sortedCurriculumGroups) {
		generateGroup(scpDiv, curriculumGroup, depth + getWidthDecreasePerLevel());
	    }
	}

	private void generateEnrolmentsExecutionPeriods(final HtmlBlockContainer scpDiv, final StudentCurricularPlan studentCurricularPlan) {
	    final Set<ExecutionPeriod> enrolmentsExecutionPeriods = new TreeSet<ExecutionPeriod>(ExecutionPeriod.EXECUTION_PERIOD_COMPARATOR_BY_SEMESTER_AND_YEAR);
	    enrolmentsExecutionPeriods.addAll(studentCurricularPlan.getEnrolmentsExecutionPeriods());

	    for (final ExecutionPeriod enrolmentsExecutionPeriod : enrolmentsExecutionPeriods) {
	        generateEnrolmentExecutionPeriod(scpDiv, studentCurricularPlan, enrolmentsExecutionPeriod);
	    }
	}

	private void generateEnrolmentExecutionPeriod(HtmlBlockContainer scpDiv, StudentCurricularPlan studentCurricularPlan, ExecutionPeriod executionPeriod) {
	    final HtmlTable executionPeriodTable = new HtmlTable();
	    scpDiv.addChild(executionPeriodTable);
	    executionPeriodTable.setClasses(getTablesClasses());
	    executionPeriodTable.setStyle("width: " + getInitialWidth() + "em;");

	    final HtmlTableRow executionPeriodRow = executionPeriodTable.createRow();
	    executionPeriodRow.setClasses(getGroupRowClasses());

	    final HtmlTableCell executionPeriodNameCell = executionPeriodRow.createCell();
	    executionPeriodNameCell.setType(CellType.HEADER);
	    executionPeriodNameCell.setClasses(getGroupNameClasses());
	    executionPeriodNameCell.setColspan(5);
	    executionPeriodNameCell.setBody(new HtmlText(executionPeriod.getYear() + ", " + executionPeriod.getName()));

	    for (final CurriculumLine curriculumLine : studentCurricularPlan.getEnrolmentsByExecutionPeriod(executionPeriod)) {
		generateLine(executionPeriodTable, curriculumLine);
	    }
	}

    }

}
