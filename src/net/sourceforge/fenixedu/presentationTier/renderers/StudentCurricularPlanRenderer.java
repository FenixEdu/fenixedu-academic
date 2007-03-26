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
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeModuleScope;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.degreeCurricularPlan.DegreeCurricularPlanState;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumLine;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.renderers.controllers.CopyCheckBoxValuesController;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyArrayConverter;
import net.sourceforge.fenixedu.renderers.InputRenderer;
import net.sourceforge.fenixedu.renderers.components.HtmlBlockContainer;
import net.sourceforge.fenixedu.renderers.components.HtmlCheckBox;
import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlInlineContainer;
import net.sourceforge.fenixedu.renderers.components.HtmlLink;
import net.sourceforge.fenixedu.renderers.components.HtmlMultipleHiddenField;
import net.sourceforge.fenixedu.renderers.components.HtmlTable;
import net.sourceforge.fenixedu.renderers.components.HtmlTableCell;
import net.sourceforge.fenixedu.renderers.components.HtmlTableRow;
import net.sourceforge.fenixedu.renderers.components.HtmlText;
import net.sourceforge.fenixedu.renderers.components.HtmlTableCell.CellType;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.model.MetaObject;
import net.sourceforge.fenixedu.renderers.model.MetaObjectFactory;
import net.sourceforge.fenixedu.renderers.model.MetaSlot;
import net.sourceforge.fenixedu.renderers.utils.RenderKit;
import net.sourceforge.fenixedu.util.EnrollmentStateSelectionType;
import net.sourceforge.fenixedu.util.LanguageUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.StringUtils;

public class StudentCurricularPlanRenderer extends InputRenderer {

    private final ResourceBundle studentResources = ResourceBundle.getBundle("resources.StudentResources", LanguageUtils.getLocale());
    
    private final ResourceBundle enumerationResources = ResourceBundle.getBundle("resources.EnumerationResources", LanguageUtils.getLocale());

    private StudentCurricularPlan studentCurricularPlan;
    
    private final HtmlBlockContainer scpDiv = new HtmlBlockContainer();
    
    private String organizedBy;

    private String initialWidth = "65em";
    
    private Double widthDecreasePerLevel = Double.valueOf(3);

    private String tablesClasses = "showinfo3 mvert0";

    private String groupRowClasses = "bgcolor2";

    private String groupNameClasses = "aleft";

    private String enrolmentClasses = "width5em acenter 0, width05em acenter 1, width2em acenter 2, aleft 3, width7em acenter 4, width6em acenter 5, width5em acenter 6, width1em acenter 7, width1em acenter 8, width1em aright 9, width1em aright 10, width1em aright 11";
    
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

    public String getInitialWidth() {
	return initialWidth;
    }
    
    public void setInitialWidth(String initialWidth) {
	this.initialWidth = initialWidth;
    }

    public static enum LengthUnit {

	/**
	 * Relative units
	 */

	FONTSIZE("em"),
	XHEIGHT("ex"),
	PIXEL("px"),
	
        /**
         * Absolute units
         */

	INCHES("in"),
	CENTIMETERS("cm"),
	MILIMETERS("mm"),
	POINTS("pt"),
	PICAS("pc"),
	PERCENTAGE("%");
	
	private String unitIdentifier;

	private LengthUnit(String unitIdentifier) {
	    this.unitIdentifier = unitIdentifier;	    
	}

	public String getUnitIdentifier() {
	    return unitIdentifier;
	}

    };
    
    private Double initialWidthValue;
    
    public Double getInitialWidthValue() {
	if (initialWidthValue == null) {
	    for (final LengthUnit lengthUnit : LengthUnit.values()) {
		if (initialWidth.contains(lengthUnit.getUnitIdentifier())) {
		    initialWidthValue = Double.valueOf(initialWidth.split(lengthUnit.getUnitIdentifier())[0]); 
		}
	    }
	}
	
	if (initialWidthValue == null) {
	    throw new RuntimeException();
	}
	
	return initialWidthValue;
    }

    private String unitIdentifier;
    
    public String getUnitIdentifier() {
	if (unitIdentifier == null) {
	    for (final LengthUnit lengthUnit : LengthUnit.values()) {
		if (initialWidth.contains(lengthUnit.getUnitIdentifier())) {
		    unitIdentifier = lengthUnit.getUnitIdentifier(); 
		}
	    }
	}

	if (unitIdentifier == null) {
	    throw new RuntimeException();
	}
	
	return unitIdentifier;
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

	private final CopyCheckBoxValuesController enrollmentsController = new CopyCheckBoxValuesController();
	
	private int RENDERED_CELLS_PER_ENROLMENT;
	
	private int RENDERED_CELLS_UNTIL_GRADE;
	    
	@Override
	public HtmlComponent createComponent(Object object, Class type) {
	    getInputContext().getForm().getSubmitButton().setVisible(false);
	    getInputContext().getForm().getCancelButton().setVisible(false);
	    
	    studentCurricularPlan = (StudentCurricularPlan) object;
	    
	    if (studentCurricularPlan == null) {
		scpDiv.addChild(new HtmlText(studentResources.getString("message.no.curricularplan")));
		scpDiv.setClasses("italic");
		return scpDiv;
	    } 

	    if (isOrganizedByGroups() && !studentCurricularPlan.isBolonha()) {
		scpDiv.addChild(new HtmlText(studentResources.getString("not.applicable")));
		scpDiv.setClasses("italic");
		return scpDiv;
	    } 

	    if (studentCurricularPlan.getEnrolmentsSet().isEmpty()) {
		scpDiv.addChild(new HtmlText(studentResources.getString("message.no.enrolments")));
		scpDiv.setClasses("italic");
		return scpDiv;
	    }

	    if (!isOrganizedByGroups() && !isOrganizedByExecutionYears() && !isOrganizedByCurricularYears()) {
		setOrganizedBy("executionYears");
	    }
	    
	    HtmlMultipleHiddenField hiddenEnrollments = new HtmlMultipleHiddenField();
	    MetaObject metaObject = getInputContext().getMetaObject();
	    metaObject.addSlot(new MetaSlot(metaObject, "extraCurricularEnrolments"));
	    hiddenEnrollments.bind(getInputContext().getMetaObject(), "extraCurricularEnrolments"); // slot refered by name
	    hiddenEnrollments.setConverter(new DomainObjectKeyArrayConverter());
	    hiddenEnrollments.setController(enrollmentsController);
	    scpDiv.addChild(hiddenEnrollments);

	    RENDERED_CELLS_PER_ENROLMENT = calculateNumberOfCellsPerEnrolment();
	    RENDERED_CELLS_UNTIL_GRADE = viewLatestEnrolmentEvaluationInfo() ? 6 : 4;

	    if (isOrganizedByGroups()) {
		generateGroup(studentCurricularPlan.getRoot(), 0);
	    } else {
		final HtmlTable scpTable = new HtmlTable();
		scpTable.setBorder("0");
		scpDiv.addChild(scpTable);
		scpTable.setClasses(getTablesClasses() + " " + getInitialWidth());

		final HtmlTableRow scpRow = scpTable.createRow();
		scpRow.setClasses(getGroupRowClasses());

		final HtmlTableCell scpNameCell = scpRow.createCell();
		scpNameCell.setType(CellType.HEADER);
		scpNameCell.setClasses(getGroupNameClasses());
		scpNameCell.setBody(generateDegreeCurricularPlanNameLink(studentCurricularPlan.getDegreeCurricularPlan(), null));
		
		if (isOrganizedByExecutionYears()) {
		    generateEnrolmentsExecutionPeriods();		    
		} else if (isOrganizedByCurricularYears()) {
		    generateEnrolmentsCurricularPeriods();
		}
	    } 
	    
	    return scpDiv;
	}

	private int calculateNumberOfCellsPerEnrolment() {
	    return MAX_CELLS_PER_ENROLMENT - (isOrganizedByGroups() ? 0 : 2) - (viewLatestEnrolmentEvaluationInfo() ? 0 : 2);
	}
	
	public boolean viewLatestEnrolmentEvaluationInfo() {
	    return AccessControl.getPerson().isAdministrativeOfficeEmployee();
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

	private void generateGroup(final CurriculumGroup group, double depth) {
	    final HtmlTable groupTable = new HtmlTable();
	    groupTable.setBorder("0");
	    scpDiv.addChild(groupTable);
	    groupTable.setClasses(getTablesClasses() + " " + getInitialWidth() + (group.isRoot() ? "" : String.valueOf(Double.valueOf(depth).intValue())));

	    final HtmlTableRow groupRow = groupTable.createRow();
	    groupRow.setClasses(getGroupRowClasses());

	    final HtmlTableCell groupNameCell = groupRow.createCell();
	    groupNameCell.setType(CellType.HEADER);
	    groupNameCell.setClasses(getGroupNameClasses());
	    groupNameCell.setColspan(RENDERED_CELLS_PER_ENROLMENT - RENDERED_CELLS_UNTIL_GRADE);
	    groupNameCell.setBody((group.isRoot()) ? generateDegreeCurricularPlanNameLink(studentCurricularPlan.getDegreeCurricularPlan(), null) : new HtmlText(group.getName().getContent(LanguageUtils.getLanguage())));

	    generateGroupLines(depth + 1, group);
	    generateGroupChilds(depth + 1, group);
	}

	private void generateGroupChilds(double depth, final CurriculumGroup group) {
	    final Set<CurriculumGroup> sortedCurriculumGroups = new TreeSet<CurriculumGroup>(new BeanComparator("childOrder"));
	    sortedCurriculumGroups.addAll(group.getCurriculumGroups());
	    
	    for (final CurriculumGroup curriculumGroup : sortedCurriculumGroups) {
		generateGroup(curriculumGroup, depth);
	    }
	}

	private void generateGroupLines(double depth, CurriculumGroup group) {
	    final Set<CurriculumLine> sortedCurriculumLines = group.getCurriculumLines();
	    
	    if (!sortedCurriculumLines.isEmpty()) {
		final HtmlTable groupLinesTable = new HtmlTable();
		groupLinesTable.setBorder("0");
		scpDiv.addChild(groupLinesTable);
		groupLinesTable.setClasses(getTablesClasses() + " " + getInitialWidth() + String.valueOf(Double.valueOf(depth).intValue()));
		
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

	final private int MAX_CELLS_PER_ENROLMENT = 13;
	
	private void generateEnrolment(final CurriculumLine curriculumLine, final HtmlTable parentTable) {
	    final Enrolment enrolment = (Enrolment) curriculumLine;
	    
	    if (getEnrolmentStateSelectionType().intValue() == EnrollmentStateSelectionType.ALL_TYPE 
		    || (getEnrolmentStateSelectionType().intValue() == EnrollmentStateSelectionType.APPROVED_TYPE && 
			    (enrolment.isApproved() || enrolment.isEnroled()))) {
		final HtmlTableRow lineRow = parentTable.createRow();

		// Code
		// Name
		final HtmlTableCell codeNameCell = lineRow.createCell();
		codeNameCell.setClasses(getEnrolmentClasses()[0]);
		final HtmlInlineContainer codeNameSpan =  new HtmlInlineContainer();
		if (!StringUtils.isEmpty(enrolment.getCurricularCourse().getCode())) {
		    codeNameSpan.addChild(new HtmlText(enrolment.getCurricularCourse().getCode() + " - "));
		}
		codeNameSpan.addChild(new HtmlText(enrolment.getName().getContent(LanguageUtils.getLanguage())));
		codeNameCell.setBody(generateEnrolmentLink(codeNameSpan, enrolment));
		
		// Degree Curricular Plan
		generateEnrolmentSmallInfoCell(lineRow, generateEnrolmentDegreeCurricularPlanLink(enrolment), getEnrolmentClasses()[1], null);
		
		// Enrolment Type 
		final HtmlComponent enrolmentType = enrolment.isEnrolmentTypeNormal() ? null : new HtmlText(enumerationResources.getString(enrolment.getEnrolmentTypeName()));
		generateEnrolmentSmallInfoCell(lineRow, enrolmentType, getEnrolmentClasses()[2], null);

		// Change Extra Curricular Status
		final HtmlTableCell checkBoxCell = lineRow.createCell();
		checkBoxCell.setClasses(getEnrolmentClasses()[3]);
		if (enrolment.isApproved() && enableExtraCurricularEditing()) {
		    final MetaObject enrolmentMetaObject = MetaObjectFactory.createObject(enrolment, RenderKit.getInstance().findSchema("Enrolment.edit-extra-curricular-status"));
		    
		    final HtmlCheckBox checkBox = new HtmlCheckBox(enrolment.isExtraCurricular());
		    checkBox.setName("enrolmentCheckBox" + enrolment.getIdInternal());
		    checkBox.setUserValue(enrolmentMetaObject.getKey().toString());
		    checkBox.setOnChange("this.form.submit();");
		    enrollmentsController.addCheckBox(checkBox);
		    
		    final HtmlInlineContainer span = new HtmlInlineContainer();
		    span.addChild(checkBox);
		    span.setTitle("Extra Curricular?");
		    span.setClasses("acronym");
		    
		    checkBoxCell.setBody(span);
		} else {
		    checkBoxCell.setBody(new HtmlText("-"));
		}
		
		// Enrolment State
		final HtmlComponent enrolmentState = enrolment.isEnrolmentStateApproved() ? null : new HtmlText(enumerationResources.getString(enrolment.getEnrollmentState().getQualifiedName()));
		generateEnrolmentSmallInfoCell(lineRow, enrolmentState, getEnrolmentClasses()[4], null);
		
		// Enrolment Grade
		final HtmlTableCell enrolmentEvaluationCell = lineRow.createCell();
		enrolmentEvaluationCell.setClasses(getEnrolmentClasses()[5]);
		final String grade = enrolment.getGrade();
		enrolmentEvaluationCell.setBody(new HtmlText(grade == null ? "-" : grade));
		
		// Enrolment Weight
		final HtmlTableCell enrolmentWeightCell = lineRow.createCell();
		enrolmentWeightCell.setClasses(getEnrolmentClasses()[6]);
		final String enrolmentWeight = enrolment.getFinalGrade() == null ? "-" : enrolment.getEnrolmentWeigth().toString();
		enrolmentWeightCell.setBody(new HtmlText(enrolmentWeight));

		// Enrolment Credits
		final HtmlTableCell enrolmentEctsCreditsCell = lineRow.createCell();
		enrolmentEctsCreditsCell.setClasses(getEnrolmentClasses()[7]);
		final String enrolmentEctsCredits = enrolment.isEnrolmentStateApproved() ? enrolment.getEctsCredits().toString() : "-";
		enrolmentEctsCreditsCell.setBody(new HtmlText(enrolmentEctsCredits));
		
		// Latest Enrolment Evaluation Type
		HtmlText latestEnrolmentEvaluationType = null;
		String latestEnrolmentEvaluationTypeQualifiedName = null;
		final EnrolmentEvaluation latestEnrolmentEvaluation = enrolment.getLatestEnrolmentEvaluation();
		if (latestEnrolmentEvaluation != null && latestEnrolmentEvaluation.getEnrolmentEvaluationType() != null) {
		    latestEnrolmentEvaluationType = new HtmlText(enumerationResources.getString(latestEnrolmentEvaluation.getEnrolmentEvaluationType().getAcronym()));
		    latestEnrolmentEvaluationTypeQualifiedName = enumerationResources.getString(latestEnrolmentEvaluation.getEnrolmentEvaluationType().getQualifiedName());
		}
		generateEnrolmentSmallInfoCell(lineRow, latestEnrolmentEvaluationType, getEnrolmentClasses()[8], latestEnrolmentEvaluationTypeQualifiedName);
		
		// Execution Period
		if (isOrganizedByGroups() || isOrganizedByCurricularYears()) {
		    // Year
		    generateEnrolmentSmallInfoCell(lineRow, new HtmlText(enrolment.getExecutionPeriod().getYear()), getEnrolmentClasses()[9], null);

		    // Semester
		    final StringBuilder semester = new StringBuilder();
		    semester.append(enrolment.getExecutionPeriod().getSemester().toString());
		    semester.append(" ");
		    semester.append(enumerationResources.getString("SEMESTER.ABBREVIATION"));
		    generateEnrolmentSmallInfoCell(lineRow, new HtmlText(semester.toString()), getEnrolmentClasses()[10], null);
		} 

		if (viewLatestEnrolmentEvaluationInfo()) {
		    // Latest Enrolment Evaluation Exam Date
		    generateEnrolmentSmallInfoCell(
			    lineRow,
			    (latestEnrolmentEvaluation == null || latestEnrolmentEvaluation.getExamDateYearMonthDay() == null) ? null : new HtmlText(latestEnrolmentEvaluation.getExamDateYearMonthDay().toString("yyyy/MM/dd")), 
			    getEnrolmentClasses()[11],
			    "Data da Avaliação");

		    // Latest Enrolment Evaluation Person Responsible For
                        // Grade
		    if (latestEnrolmentEvaluation != null && latestEnrolmentEvaluation.getPersonResponsibleForGrade() != null) {
			final Person person = latestEnrolmentEvaluation.getPersonResponsibleForGrade();
			generateEnrolmentSmallInfoCell(
				lineRow, 
				new HtmlText(person.hasEmployee() ? person.getEmployee().getRoleLoginAlias() : person.getIstUsername()), 
				getEnrolmentClasses()[12],
				"Pessoa Responsável pela Nota");
		    } else {
			generateEnrolmentSmallInfoCell(lineRow, null, getEnrolmentClasses()[12], null);
		    }
		}
        		
		// Enrolment Evaluations
//		final HtmlTableRow enrolmentEvaluationsLine = parentTable.createRow();
//		final HtmlTableCell enrolmentEvaluationsCell = enrolmentEvaluationsLine.createCell();
//		enrolmentEvaluationsCell.setColspan(RENDERED_CELLS_PER_ENROLMENT);
//		enrolmentEvaluationsCell.setStyle("padding:0; margin: 0;");
//		
//		final HtmlTable enrolmentEvaluationsTable = new HtmlTable();
//		enrolmentEvaluationsTable.setBorder("0");
//		enrolmentEvaluationsTable.setClasses("smalltxt noborder");
//		enrolmentEvaluationsTable.setWidth("100%");
//		enrolmentEvaluationsCell.setBody(enrolmentEvaluationsTable);
//		
//		final SortedSet<EnrolmentEvaluation> enrolmentEvaluations = new TreeSet<EnrolmentEvaluation>();
//		enrolmentEvaluations.addAll(enrolment.getEvaluations());
//		for (final EnrolmentEvaluation enrolmentEvaluation : enrolmentEvaluations) {
//		    final HtmlTableRow enrolmentEvaluationsRow = enrolmentEvaluationsTable.createRow();
//		    enrolmentEvaluationsRow.setStyle("color: #888;");
//		    
//		    // Grade
//		    generateEnrolmentSmallInfoCell(
//			    enrolmentEvaluationsRow, 
//			    new HtmlText(enrolmentEvaluation.getGrade()), 
//			    "", 
//			    null);
//		    
//		    // EnrolmentEvaluationType
//		    generateEnrolmentSmallInfoCell(
//			    enrolmentEvaluationsRow, 
//			    new HtmlText(enumerationResources.getString(enrolmentEvaluation.getEnrolmentEvaluationType().getAcronym())), 
//			    "", 
//			    enumerationResources.getString(enrolmentEvaluation.getEnrolmentEvaluationType().getQualifiedName()));
//		    
//		    // Exam Date
//		    HtmlComponent examDate = null;
//		    if (enrolmentEvaluation.getExamDateYearMonthDay() != null) {
//			examDate = new HtmlText(enrolmentEvaluation.getExamDateYearMonthDay().toString("yyyy/MM/dd"));
//		    }
//		    generateEnrolmentSmallInfoCell(enrolmentEvaluationsRow, examDate, "", null);
//		    
//		    // Person Responsible
//		    HtmlComponent personResponsible = null;
//		    if (enrolmentEvaluation.getPersonResponsibleForGrade() != null) {
//			personResponsible = new HtmlText(enrolmentEvaluation.getPersonResponsibleForGrade().hasEmployee() ? enrolmentEvaluation.getPersonResponsibleForGrade().getEmployee().getRoleLoginAlias() : enrolmentEvaluation.getPersonResponsibleForGrade().getIstUsername());
//		    }
//		    generateEnrolmentSmallInfoCell(enrolmentEvaluationsRow, personResponsible, "", null);
//		}
	    }
	}
	
	public boolean enableExtraCurricularEditing() {
	    return AccessControl.getPerson().isAdministrativeOfficeEmployee() && !studentCurricularPlan.getRegistration().isConcluded();
	}

	private void generateEnrolmentSmallInfoCell(final HtmlTableRow lineRow, HtmlComponent htmlComponent, final String cellClasses, final String title) {
	    final HtmlTableCell cell = lineRow.createCell();
	    cell.setClasses(cellClasses);
	    if (htmlComponent == null) {
		cell.setBody(new HtmlText("-"));
	    } else {
		final HtmlInlineContainer span = new HtmlInlineContainer();
		span.setClasses("smalltxt" + ((title == null) ? StringUtils.EMPTY : " acronym"));
		span.setTitle(title);
		span.addChild(htmlComponent);
		cell.setBody(span);
	    }
	}

	private HtmlComponent generateEnrolmentLink(final HtmlComponent htmlComponent, final Enrolment enrolment) {
	    final HtmlLink result = new HtmlLink();
	    result.setBody(htmlComponent);
	    result.setModuleRelative(false);
	    result.setTarget(HtmlLink.Target.BLANK);
	    
	    final CurricularCourse curricularCourse = enrolment.getCurricularCourse();

	    if (isOrganizedByCurricularYears()) {
		result.setParameter("degreeID", curricularCourse.getDegreeCurricularPlan().getDegree().getIdInternal());
		result.setParameter("curricularCourseID", curricularCourse.getIdInternal());
		result.setParameter("executionPeriodOID", enrolment.getExecutionPeriod().getIdInternal());
		result.setParameter("degreeCurricularPlanID", curricularCourse.getDegreeCurricularPlan().getIdInternal());
		    
		if (curricularCourse.isBolonha()) {
		    result.setUrl("/publico/degreeSite/viewCurricularCourse.faces");

		    result.setParameter("executionYearID", enrolment.getExecutionPeriod().getExecutionYear().getIdInternal());
		    result.setParameter("organizeBy", "groups");
		    result.setParameter("showRules", "false");
		    result.setParameter("hideCourses", "false");
		} else {
		    result.setUrl("/publico/showCourseSite.do?method=showCurricularCourseSite");		
		}
	    } else {
		final ExecutionCourse executionCourse = enrolment.getExecutionCourseFor(enrolment.getExecutionPeriod());
		if (executionCourse == null) {
		    return htmlComponent;
		} else {
		    result.setUrl("/publico/executionCourse.do?method=firstPage");

		    result.setParameter("executionCourseID", executionCourse.getIdInternal());
		}
	    }
	    
	    return result;
	}

	private HtmlComponent generateEnrolmentDegreeCurricularPlanLink(final Enrolment enrolment) {
	    final HtmlComponent degreeCurricularPlan;
	    if (studentCurricularPlan.getDegreeCurricularPlan() == enrolment.getDegreeModule().getParentDegreeCurricularPlan()) {
	        degreeCurricularPlan = new HtmlText("-");
	    } else {
	        degreeCurricularPlan = generateDegreeCurricularPlanNameLink(enrolment.getDegreeModule().getParentDegreeCurricularPlan(), enrolment.getExecutionPeriod());
	    }
	    return degreeCurricularPlan;
	}

	
	// FOR EXECUTION PERIOD VIEW
	
	private void generateEnrolmentsExecutionPeriods() {
	    final Set<ExecutionPeriod> enrolmentsExecutionPeriods = new TreeSet<ExecutionPeriod>(ExecutionPeriod.EXECUTION_PERIOD_COMPARATOR_BY_SEMESTER_AND_YEAR);
	    enrolmentsExecutionPeriods.addAll(studentCurricularPlan.getEnrolmentsExecutionPeriods());

	    for (final ExecutionPeriod enrolmentsExecutionPeriod : enrolmentsExecutionPeriods) {
	        generateEnrolmentExecutionPeriod(enrolmentsExecutionPeriod);
	    }
	}

	private void generateEnrolmentExecutionPeriod(ExecutionPeriod executionPeriod) {
	    final HtmlTable executionPeriodTable = new HtmlTable();
	    executionPeriodTable.setBorder("0");
	    scpDiv.addChild(executionPeriodTable);
	    executionPeriodTable.setClasses(getTablesClasses() + " " + getInitialWidth() + String.valueOf(1));

	    final HtmlTableRow executionPeriodRow = executionPeriodTable.createRow();
	    executionPeriodRow.setClasses(getGroupRowClasses());

	    final HtmlTableCell executionPeriodNameCell = executionPeriodRow.createCell();
	    executionPeriodNameCell.setType(CellType.HEADER);
	    executionPeriodNameCell.setClasses(getGroupNameClasses());
	    executionPeriodNameCell.setColspan(RENDERED_CELLS_PER_ENROLMENT - RENDERED_CELLS_UNTIL_GRADE);
	    executionPeriodNameCell.setBody(new HtmlText(executionPeriod.getYear() + ", " + executionPeriod.getName()));

	    final HtmlTableCell gradeCell = executionPeriodRow.createCell();
	    gradeCell.setType(CellType.HEADER);
	    gradeCell.setClasses("acenter width1em");
	    final HtmlInlineContainer gradeSpan = new HtmlInlineContainer();
	    gradeSpan.setClasses("smalltxt");
	    gradeSpan.addChild(new HtmlText("Nota"));
	    gradeCell.setBody(gradeSpan);

	    final HtmlTableCell weightCell = executionPeriodRow.createCell();
	    weightCell.setType(CellType.HEADER);
	    final HtmlInlineContainer weightSpan = new HtmlInlineContainer();
	    weightSpan.setClasses("smalltxt");
	    weightSpan.addChild(new HtmlText("Peso"));
	    weightCell.setBody(weightSpan);
	    
	    final HtmlTableCell ectsCreditsCell = executionPeriodRow.createCell();
	    ectsCreditsCell.setType(CellType.HEADER);
	    ectsCreditsCell.setClasses("acenter width1em");
	    final HtmlInlineContainer ectsCreditsSpan = new HtmlInlineContainer();
	    ectsCreditsSpan.setClasses("smalltxt");
	    ectsCreditsSpan.addChild(new HtmlText("ECTS"));
	    ectsCreditsCell.setBody(ectsCreditsSpan);

	    final HtmlTableCell emptyCell = executionPeriodRow.createCell();
	    emptyCell.setType(CellType.HEADER);
	    emptyCell.setClasses(getGroupNameClasses());
	    emptyCell.setColspan(RENDERED_CELLS_UNTIL_GRADE - 3);
	    
	    final ComparatorChain comparatorChain = new ComparatorChain();
	    comparatorChain.addComparator(CurriculumLine.COMPARATOR_BY_NAME);

	    final Set<CurriculumLine> sortedCurriculumLines = new TreeSet<CurriculumLine>(comparatorChain);
	    sortedCurriculumLines.addAll(studentCurricularPlan.getEnrolmentsByExecutionPeriod(executionPeriod));
	    
	    for (final CurriculumLine curriculumLine : sortedCurriculumLines) {
		generateLine(executionPeriodTable, curriculumLine);
	    }
	}

	
	// FOR CURRICULAR PERIOD VIEW
	
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
	    executionPeriodTable.setBorder("0");
	    scpDiv.addChild(executionPeriodTable);
	    executionPeriodTable.setClasses(getTablesClasses() + " " + getInitialWidth() + String.valueOf(1));

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

    }

}