package net.sourceforge.fenixedu.presentationTier.renderers;

import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolment;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.degreeCurricularPlan.DegreeCurricularPlanState;
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
import net.sourceforge.fenixedu.util.EnrollmentStateSelectionType;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang.StringUtils;

public class StudentCurricularPlanRenderer extends OutputRenderer {

    private static final ResourceBundle applicationResources = ResourceBundle.getBundle("resources.ApplicationResources");
    
    private static final ResourceBundle studentResources = ResourceBundle.getBundle("resources.StudentResources");
    
    private static final ResourceBundle enumerationResources = ResourceBundle.getBundle("resources.EnumerationResources");

    private StudentCurricularPlan studentCurricularPlan;
    
    private final HtmlBlockContainer scpDiv = new HtmlBlockContainer();
    
    private Boolean organizedByGroups;

    private Double initialWidth = Double.valueOf(70);

    private Double widthDecreasePerLevel = Double.valueOf(3);

    private String tablesClasses = "showinfo3 mvert0";

    private String groupRowClasses = "bgcolor2";

    private String groupNameClasses = "aleft";

    private String enrolmentClasses = "smalltxt aright, smalltxt aright, smalltxt aright, smalltxt aright, aright";
    
    private Integer enrolmentStateSelectionType = EnrollmentStateSelectionType.ALL_TYPE;

    public StudentCurricularPlanRenderer() {
	super();
    }

    public Boolean isOrganizedByGroups() {
	return organizedByGroups;
    }

    public void setOrganizedByGroups(Boolean organizedByGroups) {
	this.organizedByGroups = organizedByGroups;
    }

    public Double getInitialWidth() {
	return initialWidth;
    }

    public void setInitialWidth(Double initialWidth) {
	this.initialWidth = initialWidth;
    }

    public Double getWidthDecreasePerLevel() {
	return widthDecreasePerLevel;
    }

    public void setWidthDecreasePerLevel(Double widthDecreasePerLevel) {
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

    private String getEnrolmentYearClasses() {
        return getEnrolmentClasses()[0];
    }

    private String getEnrolmentSemesterClasses() {
        return getEnrolmentClasses()[1];
    }

    private String getEnrolmentDegreeCurricularPlanClasses() {
        return getEnrolmentClasses()[2];
    }
    
    private String getEnrolmentTypeInfoClasses() {
        return getEnrolmentClasses()[3];
    }

    private String getEnrolmentInfoClasses() {
        return getEnrolmentClasses()[4];
    }

    public Integer getEnrolmentStateSelectionType() {
        return enrolmentStateSelectionType;
    }

    public void setEnrolmentStateSelectionType(Integer enrolmentStateSelectionType) {
        this.enrolmentStateSelectionType = enrolmentStateSelectionType;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
	return new StudentCurricularPlanLayout();
    }

    private class StudentCurricularPlanLayout extends Layout {

	@Override
	public HtmlComponent createComponent(Object object, Class type) {
	    studentCurricularPlan = (StudentCurricularPlan) object;

	    if (studentCurricularPlan == null) {
		return new HtmlText(studentResources.getString("message.no.curricularplan"));
	    } else if (isOrganizedByGroups() == null) {
		setOrganizedByGroups(studentCurricularPlan.isBolonha() ? Boolean.TRUE : Boolean.FALSE);
	    } else if (isOrganizedByGroups() && !studentCurricularPlan.isBolonha()) {
		return new HtmlText(studentResources.getString("not.applicable"));
	    } else if (studentCurricularPlan.getEnrolmentsSet().isEmpty()) {
		return new HtmlText(studentResources.getString("message.no.enrolments"));
	    }

	    if (isOrganizedByGroups()) {
		generateGroup(studentCurricularPlan.getRoot(), 0);
	    } else {
		final HtmlTable scpTable = new HtmlTable();
		scpDiv.addChild(scpTable);
		scpTable.setClasses(getTablesClasses());
		scpTable.setStyle("width: " + getInitialWidth() + "em;");

		final HtmlTableRow scpRow = scpTable.createRow();
		scpRow.setClasses(getGroupRowClasses());

		final HtmlTableCell scpNameCell = scpRow.createCell();
		scpNameCell.setType(CellType.HEADER);
		scpNameCell.setClasses(getGroupNameClasses());
		scpNameCell.setBody(generateDegreeCurricularPlanNameLink(studentCurricularPlan.getDegreeCurricularPlan(), null));
		
		generateEnrolmentsExecutionPeriods();
	    }

	    return scpDiv;
	}

	private void generateGroup(final CurriculumGroup group, double depth) {
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
	    groupNameCell.setBody((group.isRoot()) ? generateDegreeCurricularPlanNameLink(studentCurricularPlan.getDegreeCurricularPlan(), null) : new HtmlText(group.getName()));

	    generateGroupLines(depth, group);
	    generateGroupChilds(depth, group);
	}

	private HtmlComponent generateDegreeCurricularPlanNameLink(final DegreeCurricularPlan degreeCurricularPlan, ExecutionPeriod executionPeriod) {
	    if (degreeCurricularPlan.getState() == DegreeCurricularPlanState.PAST) {
		return new HtmlText(degreeCurricularPlan.getName());
	    }
	    
	    final HtmlLink result = new HtmlLink();

	    result.setText(degreeCurricularPlan.getName());
	    result.setModuleRelative(false);
	    result.setTarget("_blank");

	    if (degreeCurricularPlan.isBolonha()) {
		result.setUrl("/publico/degreeSite/showDegreeCurricularPlanBolonha.faces");

		result.setParameter("organizeBy", "groups");
		result.setParameter("showRules", "false");
		result.setParameter("hideCourses", "false");
	    } else {
		result.setUrl("/publico/prepareConsultCurricularPlanNew.do");
		
		result.setParameter("method", "prepare");
		result.setParameter("degreeInitials", degreeCurricularPlan.getDegree().getSigla());
		result.setParameter("method", "prepare");
		result.setParameter("method", "prepare");
		result.setParameter("method", "prepare");
	    }
	    
	    result.setParameter("degreeID", degreeCurricularPlan.getDegree().getIdInternal());
	    result.setParameter("degreeCurricularPlanID", degreeCurricularPlan.getIdInternal());
	    
	    executionPeriod = (executionPeriod == null) ? ExecutionPeriod.readActualExecutionPeriod() : executionPeriod;  
	    result.setParameter("executionPeriodOID", executionPeriod.getIdInternal());
	    
	    return result;
	}

	private void generateGroupLines(double depth, CurriculumGroup group) {
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
	    if (curriculumLine.isEnrolment()) {
		generateEnrolment(curriculumLine, groupLinesTable);
	    }
	}

	private void generateEnrolment(final CurriculumLine curriculumLine, final HtmlTable parentTable) {
	    final Enrolment enrolment = (Enrolment) curriculumLine;
	    
	    if (getEnrolmentStateSelectionType().intValue() == EnrollmentStateSelectionType.ALL_TYPE 
		    || (getEnrolmentStateSelectionType().intValue() == EnrollmentStateSelectionType.APPROVED_TYPE && 
			    (enrolment.isApproved() || enrolment.isEnroled()))) {
		final HtmlTableRow lineRow = parentTable.createRow();

		// Name
		final HtmlTableCell nameCell = lineRow.createCell();
		nameCell.setBody(generateExecutionCourseLink(enrolment));

		if (isOrganizedByGroups()) {
		    // Year
		    final HtmlTableCell yearCell = lineRow.createCell();
		    yearCell.setClasses(getEnrolmentYearClasses());
		    yearCell.setBody(new HtmlText(enrolment.getExecutionPeriod().getYear()));

		    // Semester
		    final HtmlTableCell semesterCell = lineRow.createCell();
		    semesterCell.setClasses(getEnrolmentSemesterClasses());

		    final StringBuilder semester = new StringBuilder();
		    semester.append(enrolment.getExecutionPeriod().getSemester().toString());
		    semester.append(" ");
		    semester.append(enumerationResources.getString("SEMESTER.ABBREVIATION"));
		    semesterCell.setBody(new HtmlText(semester.toString()));
		}

		// DegreeCurricularPlan
		final HtmlTableCell degreeCurricularPlanCell = lineRow.createCell();
		degreeCurricularPlanCell.setClasses(getEnrolmentDegreeCurricularPlanClasses());
		if (studentCurricularPlan.getDegreeCurricularPlan() == enrolment.getDegreeModule().getParentDegreeCurricularPlan()) {
		    degreeCurricularPlanCell.setBody(new HtmlText(StringUtils.EMPTY));
		} else {
		    degreeCurricularPlanCell.setBody(generateDegreeCurricularPlanNameLink(enrolment.getDegreeModule().getParentDegreeCurricularPlan(), enrolment.getExecutionPeriod()));
		}

		// Enrolment Type Info
		final HtmlTableCell enrolmentTypeInfoCell = lineRow.createCell();
		enrolmentTypeInfoCell.setClasses(getEnrolmentTypeInfoClasses());
		String enrolmentTypeInfo = (enrolment.isNormal()) ? StringUtils.EMPTY : applicationResources.getString(InfoEnrolment.newInfoFromDomain(enrolment).getEnrollmentTypeResourceKey());
		enrolmentTypeInfoCell.setBody(new HtmlText(enrolmentTypeInfo));

		// Enrolment Info
		final HtmlTableCell enrolmentInfoCell = lineRow.createCell();
		enrolmentInfoCell.setClasses(getEnrolmentInfoClasses());

		if (enrolment.isApproved()) {
		    final String grade = enrolment.getLatestEnrolmentEvaluation().getGrade();
		    enrolmentInfoCell.setBody(new HtmlText(grade));
		} else {
		    final String enrolmentState = enumerationResources.getString(enrolment.getEnrollmentState().toString());
		    enrolmentInfoCell.setBody(new HtmlText(enrolmentState));
		    enrolmentInfoCell.setClasses(getEnrolmentInfoClasses() + " smalltxt");
		}
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

	private void generateGroupChilds(double depth, final CurriculumGroup group) {
	    final Set<CurriculumGroup> sortedCurriculumGroups = new TreeSet<CurriculumGroup>(new BeanComparator("childOrder"));
	    sortedCurriculumGroups.addAll(group.getCurriculumGroups());
	    
	    for (final CurriculumGroup curriculumGroup : sortedCurriculumGroups) {
		generateGroup(curriculumGroup, depth + getWidthDecreasePerLevel());
	    }
	}

	private void generateEnrolmentsExecutionPeriods() {
	    final Set<ExecutionPeriod> enrolmentsExecutionPeriods = new TreeSet<ExecutionPeriod>(ExecutionPeriod.EXECUTION_PERIOD_COMPARATOR_BY_SEMESTER_AND_YEAR);
	    enrolmentsExecutionPeriods.addAll(studentCurricularPlan.getEnrolmentsExecutionPeriods());

	    for (final ExecutionPeriod enrolmentsExecutionPeriod : enrolmentsExecutionPeriods) {
	        generateEnrolmentExecutionPeriod(enrolmentsExecutionPeriod);
	    }
	}

	private void generateEnrolmentExecutionPeriod(ExecutionPeriod executionPeriod) {
	    final HtmlTable executionPeriodTable = new HtmlTable();
	    scpDiv.addChild(executionPeriodTable);
	    executionPeriodTable.setClasses(getTablesClasses());
	    executionPeriodTable.setStyle("width: " + (getInitialWidth() - getWidthDecreasePerLevel()) + "em; margin-left: " + getWidthDecreasePerLevel() + "em;");

	    final HtmlTableRow executionPeriodRow = executionPeriodTable.createRow();
	    executionPeriodRow.setClasses(getGroupRowClasses());

	    final HtmlTableCell executionPeriodNameCell = executionPeriodRow.createCell();
	    executionPeriodNameCell.setType(CellType.HEADER);
	    executionPeriodNameCell.setClasses(getGroupNameClasses());
	    executionPeriodNameCell.setColspan(5);
	    executionPeriodNameCell.setBody(new HtmlText(executionPeriod.getYear() + ", " + executionPeriod.getName()));

	    final Set<CurriculumLine> sortedCurriculumLines = new TreeSet<CurriculumLine>(CurriculumLine.COMPARATOR_BY_NAME);
	    sortedCurriculumLines.addAll(studentCurricularPlan.getEnrolmentsByExecutionPeriod(executionPeriod));
	    
	    for (final CurriculumLine curriculumLine : sortedCurriculumLines) {
		generateLine(executionPeriodTable, curriculumLine);
	    }
	}

    }

}
