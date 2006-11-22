package net.sourceforge.fenixedu.presentationTier.renderers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.dataTransferObject.GenericPair;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolment;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeModuleScope;
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
import net.sourceforge.fenixedu.renderers.components.HtmlInlineContainer;
import net.sourceforge.fenixedu.renderers.components.HtmlLink;
import net.sourceforge.fenixedu.renderers.components.HtmlTable;
import net.sourceforge.fenixedu.renderers.components.HtmlTableCell;
import net.sourceforge.fenixedu.renderers.components.HtmlTableRow;
import net.sourceforge.fenixedu.renderers.components.HtmlText;
import net.sourceforge.fenixedu.renderers.components.HtmlTableCell.CellType;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.util.EnrollmentStateSelectionType;
import net.sourceforge.fenixedu.util.LanguageUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.StringUtils;

public class StudentCurricularPlanRenderer extends OutputRenderer {

    private static final ResourceBundle applicationResources = ResourceBundle.getBundle("resources.ApplicationResources");
    
    private static final ResourceBundle studentResources = ResourceBundle.getBundle("resources.StudentResources");
    
    private static final ResourceBundle enumerationResources = ResourceBundle.getBundle("resources.EnumerationResources");

    private StudentCurricularPlan studentCurricularPlan;
    
    private final HtmlBlockContainer scpDiv = new HtmlBlockContainer();
    
    private String organizedBy;

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

    public String getOrganizedBy() {
	return organizedBy;
    }

    public void setOrganizedBy(String organizedBy) {
	this.organizedBy = organizedBy;
    }

    public Boolean isOrganizedByGroups() {
	return organizedBy.equals("groups");
    }

    public Boolean isOrganizedByCurricularYears() {
	return organizedBy.equals("curricularYears");
    }

    public Boolean isOrganizedByExecutionYears() {
	return organizedBy.equals("executionYears");
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
		HtmlText text = new HtmlText(studentResources.getString("message.no.curricularplan"));
		text.setClasses("warning0");
		return text;
	    } 

	    if (isOrganizedByGroups() && !studentCurricularPlan.isBolonha()) {
		HtmlText text = new HtmlText(studentResources.getString("not.applicable"));
		text.setClasses("warning0");
		return text;
	    } 

	    if (studentCurricularPlan.getEnrolmentsSet().isEmpty()) {
		HtmlText text = new HtmlText(studentResources.getString("message.no.enrolments"));
		text.setClasses("warning0");
		return text;
	    }

	    if (!isOrganizedByGroups() && !isOrganizedByExecutionYears() && !isOrganizedByCurricularYears()) {
		setOrganizedBy("curricularYears");
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
		
		if (isOrganizedByCurricularYears()) {
		    generateEnrolmentsCurricularPeriods();
		} else if (isOrganizedByExecutionYears()) {
		    generateEnrolmentsExecutionPeriods();    
		}
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
	    groupNameCell.setBody((group.isRoot()) ? generateDegreeCurricularPlanNameLink(studentCurricularPlan.getDegreeCurricularPlan(), null) : new HtmlText(group.getName().getContent(LanguageUtils.getLanguage())));

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
		nameCell.setBody(isOrganizedByExecutionYears() ? generateExecutionCourseLink(enrolment) : generateCurricularCourseLink(enrolment));

		if (isOrganizedByGroups() || isOrganizedByCurricularYears()) {
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
		    
		    final HtmlInlineContainer span = new HtmlInlineContainer();
		    span.setClasses("smalltxt");
		    span.addChild(new HtmlText(enrolmentState));
		    
		    enrolmentInfoCell.setBody(span);
		}
	    }
	}

	private HtmlComponent generateExecutionCourseLink(Enrolment enrolment) {
	    final ExecutionCourse executionCourse = enrolment.getExecutionCourseFor(enrolment.getExecutionPeriod());
	    
	    if (executionCourse == null) {
		return new HtmlText(enrolment.getName().getContent(LanguageUtils.getLanguage()));
	    } else {
		final HtmlLink executionCourseLink = new HtmlLink();

		executionCourseLink.setText(enrolment.getName().getContent(LanguageUtils.getLanguage()));

		executionCourseLink.setUrl("/publico/executionCourse.do?method=firstPage");
		executionCourseLink.setModuleRelative(false);

		executionCourseLink.setParameter("executionCourseID", executionCourse.getIdInternal());

		executionCourseLink.setTarget(HtmlLink.Target.BLANK);

		return executionCourseLink;
	    }
	}

	private HtmlComponent generateCurricularCourseLink(Enrolment enrolment) {
	    final CurricularCourse curricularCourse = enrolment.getCurricularCourse();
	    
	    final HtmlLink curricularCourseLink = new HtmlLink();

	    curricularCourseLink.setText(enrolment.getName().getContent(LanguageUtils.getLanguage()));
	    curricularCourseLink.setModuleRelative(false);
	    curricularCourseLink.setTarget(HtmlLink.Target.BLANK);
	    curricularCourseLink.setParameter("degreeID", curricularCourse.getDegreeCurricularPlan().getDegree().getIdInternal());
	    curricularCourseLink.setParameter("curricularCourseID", curricularCourse.getIdInternal());
	    curricularCourseLink.setParameter("executionPeriodOID", enrolment.getExecutionPeriod().getIdInternal());
	    curricularCourseLink.setParameter("degreeCurricularPlanID", curricularCourse.getDegreeCurricularPlan().getIdInternal());
	    
	    if (curricularCourse.isBolonha()) {
		curricularCourseLink.setUrl("/publico/degreeSite/viewCurricularCourse.faces");

		curricularCourseLink.setParameter("executionYearID", enrolment.getExecutionPeriod().getExecutionYear().getIdInternal());
		curricularCourseLink.setParameter("organizeBy", "groups");
		curricularCourseLink.setParameter("showRules", "false");
		curricularCourseLink.setParameter("hideCourses", "false");
	    } else {
		curricularCourseLink.setUrl("/publico/showCourseSite.do?method=showCurricularCourseSite");		
	    }

	    return curricularCourseLink;
	}

	private void generateGroupChilds(double depth, final CurriculumGroup group) {
	    final Set<CurriculumGroup> sortedCurriculumGroups = new TreeSet<CurriculumGroup>(new BeanComparator("childOrder"));
	    sortedCurriculumGroups.addAll(group.getCurriculumGroups());
	    
	    for (final CurriculumGroup curriculumGroup : sortedCurriculumGroups) {
		generateGroup(curriculumGroup, depth + getWidthDecreasePerLevel());
	    }
	}

	private void generateEnrolmentsCurricularPeriods() {
	    final Map<GenericPair<Integer,Integer>, Set<Enrolment>> enrolmentsCurricularPeriods = new HashMap<GenericPair<Integer,Integer>, Set<Enrolment>>();
	    final Map<GenericPair<Integer,Integer>, Set<Enrolment>> undefinedEnrolments = new HashMap<GenericPair<Integer,Integer>, Set<Enrolment>>();
	    final Map<GenericPair<Integer,Integer>, Set<Enrolment>> orphanEnrolments = new HashMap<GenericPair<Integer,Integer>, Set<Enrolment>>();
	    
	    for (final Enrolment enrolment : studentCurricularPlan.getEnrolmentsSet()) {
		if (enrolment.getDegreeModule().isLeaf()) {
		    final CurricularCourse curricularCourse = (CurricularCourse) enrolment.getDegreeModule();
		    
		    List<DegreeModuleScope> degreeModuleScopes = curricularCourse.getActiveDegreeModuleScopesInExecutionPeriod(enrolment.getExecutionPeriod());
		    if (degreeModuleScopes.size() == 1) {
			final DegreeModuleScope degreeModuleScope = degreeModuleScopes.iterator().next();
			final GenericPair<Integer,Integer> yearSemester = new GenericPair<Integer,Integer>(degreeModuleScope.getCurricularYear(), degreeModuleScope.getCurricularSemester());
			
			final Set<Enrolment> enrolmentsCurricularPeriod = enrolmentsCurricularPeriods.get(yearSemester);
			if (enrolmentsCurricularPeriod == null) {
			    enrolmentsCurricularPeriods.put(yearSemester, new HashSet<Enrolment>());
			}
			enrolmentsCurricularPeriods.get(yearSemester).add(enrolment);
		    } else if (degreeModuleScopes.isEmpty()) {
			for (final DegreeModuleScope degreeModuleScope : degreeModuleScopes) {
			    final GenericPair<Integer,Integer> yearSemester = new GenericPair<Integer,Integer>(degreeModuleScope.getCurricularYear(), degreeModuleScope.getCurricularSemester());
				
			    final Set<Enrolment> enrolmentsCurricularPeriod = orphanEnrolments.get(yearSemester);
			    if (enrolmentsCurricularPeriod == null) {
				orphanEnrolments.put(yearSemester, new HashSet<Enrolment>());
			    }
			    orphanEnrolments.get(yearSemester).add(enrolment);
			}
		    } else {
			for (final DegreeModuleScope degreeModuleScope : degreeModuleScopes) {
			    final GenericPair<Integer,Integer> yearSemester = new GenericPair<Integer,Integer>(degreeModuleScope.getCurricularYear(), degreeModuleScope.getCurricularSemester());
				
			    final Set<Enrolment> enrolmentsCurricularPeriod = undefinedEnrolments.get(yearSemester);
			    if (enrolmentsCurricularPeriod == null) {
				undefinedEnrolments.put(yearSemester, new HashSet<Enrolment>());
			    }
			    undefinedEnrolments.get(yearSemester).add(enrolment);
			}
		    }
		}
	    }
	    
	    List<GenericPair<Integer, Integer>> curricularPeriods = new ArrayList(enrolmentsCurricularPeriods.keySet());
	    final ComparatorChain comparatorChain = new ComparatorChain();
	    comparatorChain.addComparator(new BeanComparator("left"));
	    comparatorChain.addComparator(new BeanComparator("right"));
	    Collections.sort(curricularPeriods, comparatorChain);
	    for (final GenericPair<Integer, Integer> yearSemester : curricularPeriods) {
		generateEnrolmentCurricularPeriod(yearSemester, enrolmentsCurricularPeriods.get(yearSemester));
	    }

	    final HtmlText orphansInfo = new HtmlText("\nSem Scopes:\n");
	    scpDiv.addChild(orphansInfo);
	    
	    List<GenericPair<Integer, Integer>> orphanCurricularPeriods = new ArrayList(orphanEnrolments.keySet());
	    Collections.sort(orphanCurricularPeriods, comparatorChain);
	    for (final GenericPair<Integer, Integer> yearSemester : orphanCurricularPeriods) {
		generateEnrolmentCurricularPeriod(yearSemester, orphanEnrolments.get(yearSemester));
	    }

	    final HtmlText undefinedInfo = new HtmlText("\nMais do que um:\n");
	    scpDiv.addChild(undefinedInfo);
	    
	    List<GenericPair<Integer, Integer>> undefinedCurricularPeriods = new ArrayList(undefinedEnrolments.keySet());
	    Collections.sort(undefinedCurricularPeriods, comparatorChain);
	    for (final GenericPair<Integer, Integer> yearSemester : undefinedCurricularPeriods) {
		generateEnrolmentCurricularPeriod(yearSemester, undefinedEnrolments.get(yearSemester));
	    }
	}

	private void generateEnrolmentCurricularPeriod(GenericPair<Integer, Integer> yearSemester, Set<Enrolment> enrolmentsByCurricularPeriod) {
	    final HtmlTable executionPeriodTable = new HtmlTable();
	    scpDiv.addChild(executionPeriodTable);
	    executionPeriodTable.setClasses(getTablesClasses());
	    executionPeriodTable.setStyle("width: " + (getInitialWidth() - getWidthDecreasePerLevel()) + "em; margin-left: " + getWidthDecreasePerLevel() + "em;");

	    final HtmlTableRow executionPeriodRow = executionPeriodTable.createRow();
	    executionPeriodRow.setClasses(getGroupRowClasses());

	    final HtmlTableCell executionPeriodNameCell = executionPeriodRow.createCell();
	    executionPeriodNameCell.setType(CellType.HEADER);
	    executionPeriodNameCell.setClasses(getGroupNameClasses());
	    executionPeriodNameCell.setColspan(6);
	    executionPeriodNameCell.setBody(new HtmlText(yearSemester.getLeft() + " Ano, " + yearSemester.getRight() + " Semestre"));

	    final ComparatorChain comparatorChain = new ComparatorChain();
	    comparatorChain.addComparator(CurriculumLine.COMPARATOR_BY_NAME);
	    comparatorChain.addComparator(new BeanComparator("idInternal"));
	    
	    final Set<CurriculumLine> sortedCurriculumLines = new TreeSet<CurriculumLine>(comparatorChain);
	    sortedCurriculumLines.addAll(enrolmentsByCurricularPeriod);
	    
	    for (final CurriculumLine curriculumLine : sortedCurriculumLines) {
		generateLine(executionPeriodTable, curriculumLine);
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

	    
	    final ComparatorChain comparatorChain = new ComparatorChain();
	    comparatorChain.addComparator(CurriculumLine.COMPARATOR_BY_NAME);
	    comparatorChain.addComparator(new BeanComparator("idInternal"));

	    final Set<CurriculumLine> sortedCurriculumLines = new TreeSet<CurriculumLine>(comparatorChain);
	    sortedCurriculumLines.addAll(studentCurricularPlan.getEnrolmentsByExecutionPeriod(executionPeriod));
	    
	    for (final CurriculumLine curriculumLine : sortedCurriculumLines) {
		generateLine(executionPeriodTable, curriculumLine);
	    }
	}

    }

}
