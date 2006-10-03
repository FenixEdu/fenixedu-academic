package net.sourceforge.fenixedu.presentationTier.renderers;

import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.beanutils.BeanComparator;

import sun.security.krb5.internal.s;

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
import net.sourceforge.fenixedu.util.LanguageUtils;

public class StudentCurricularPlanRenderer extends OutputRenderer {

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
		return new HtmlText();
	    } else if (isOrganizedByGroups() == null) {
		setOrganizedByGroups(studentCurricularPlan.isBolonha() ? Boolean.TRUE : Boolean.FALSE);
	    }

	    if (isOrganizedByGroups()) {
		generateGroup(scpDiv, studentCurricularPlan.getRoot(), 0);
	    } else {
		Set<ExecutionPeriod> executionPeriods = studentCurricularPlan
			.getEnrolmentsExecutionPeriods();

		for (final ExecutionPeriod executionPeriod : executionPeriods) {
		    generateExecutionPeriod(scpDiv, executionPeriod);
		}
	    }

	    return scpDiv;
	}

	private void generateGroup(HtmlBlockContainer scpDiv, final CurriculumGroup group, int depth) {
	    final HtmlTable groupTable = new HtmlTable();
	    scpDiv.addChild(groupTable);
	    groupTable.setClasses(getTablesClasses());
	    groupTable.setStyle("width: " + (getInitialWidth() - depth) + "em; margin-left: " + depth
		    + "em;");

	    final HtmlTableRow groupRow = groupTable.createRow();
	    groupRow.setClasses(getGroupRowClasses());

	    final HtmlTableCell groupNameCell = groupRow.createCell();
	    groupNameCell.setType(CellType.HEADER);
	    groupNameCell.setClasses(getGroupNameClasses());
	    groupNameCell.setColspan(5);
	    groupNameCell.setBody((group.isRoot()) ? generateRootLink(group) : new HtmlText(group
		    .getName()));

	    final Set<CurriculumLine> sortedCurriculumLines = new TreeSet<CurriculumLine>(CurriculumLine.COMPARATOR_BY_NAME);
	    sortedCurriculumLines.addAll(group.getCurriculumLines());
	    generateLines(scpDiv, depth, sortedCurriculumLines);

	    final Set<CurriculumGroup> sortedCurriculumGroups = new TreeSet<CurriculumGroup>(new BeanComparator("childOrder"));
	    sortedCurriculumGroups.addAll(group.getCurriculumGroups());
	    
	    for (final CurriculumGroup curriculumGroup : sortedCurriculumGroups) {
		generateGroup(scpDiv, curriculumGroup, depth + getWidthDecreasePerLevel());
	    }
	}

	private HtmlLink generateRootLink(CurriculumGroup root) {
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

	private void generateLines(HtmlBlockContainer scpDiv, int depth,
		final Set<CurriculumLine> curriculumLines) {
	    if (!curriculumLines.isEmpty()) {
		final HtmlTable groupLinesTable = new HtmlTable();
		scpDiv.addChild(groupLinesTable);
		groupLinesTable.setClasses(getTablesClasses());
		groupLinesTable.setStyle("width: "
			+ (getInitialWidth() - depth - getWidthDecreasePerLevel()) + "em; margin-left: "
			+ (depth + getWidthDecreasePerLevel()) + "em;");

		for (final CurriculumLine curriculumLine : curriculumLines) {
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

	    final ResourceBundle enumerationResources = ResourceBundle.getBundle(
		    "resources.EnumerationResources", LanguageUtils.getLocale());

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

	private HtmlLink generateExecutionCourseLink(Enrolment enrolment) {
	    final HtmlLink rootLink = new HtmlLink();

	    rootLink.setText(enrolment.getName());

	    rootLink.setUrl("/publico/executionCourse.do?method=firstPage");
	    rootLink.setModuleRelative(false);

	    final ExecutionCourse executionCourse = enrolment.getExecutionCourseFor(ExecutionPeriod.readActualExecutionPeriod());
	    rootLink.setParameter("executionCourseID", executionCourse.getIdInternal());

	    rootLink.setTarget(HtmlLink.Target.BLANK);

	    return rootLink;
	}

	private void generateExecutionPeriod(HtmlBlockContainer scpDiv, ExecutionPeriod executionPeriod) {
	    final HtmlTable groupTable = new HtmlTable();
	    scpDiv.addChild(groupTable);
	    groupTable.setClasses(getTablesClasses());
	    groupTable.setStyle("width: " + getInitialWidth() + "em;");

	    final HtmlTableRow groupRow = groupTable.createRow();
	    groupRow.setClasses(getGroupRowClasses());

	    final HtmlTableCell groupNameCell = groupRow.createCell();
	    groupNameCell.setType(CellType.HEADER);
	    groupNameCell.setClasses(getGroupNameClasses());
	    groupNameCell.setColspan(5);
	    groupNameCell.setBody(new HtmlText(executionPeriod.getYear() + ", " + executionPeriod.getName()));

	    // generateLines(scpDiv, depth, group.getCurriculumLines());
	    //
	    // for (final CurriculumGroup curriculumGroup :
                // group.getCurriculumGroups()) {
	    // generateGroup(scpDiv, curriculumGroup, depth +
                // getWidthDecreasePerLevel());
	    //		}
	}

    }

}
