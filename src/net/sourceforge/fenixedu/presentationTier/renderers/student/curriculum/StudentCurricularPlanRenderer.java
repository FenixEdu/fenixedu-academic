package net.sourceforge.fenixedu.presentationTier.renderers.student.curriculum;

import java.text.DecimalFormat;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Grade;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.OptionalEnrolment;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curriculum.EnrolmentEvaluationType;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.Dismissal;
import net.sourceforge.fenixedu.domain.studentCurriculum.ExternalEnrolment;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.renderers.InputRenderer;
import net.sourceforge.fenixedu.renderers.components.HtmlBlockContainer;
import net.sourceforge.fenixedu.renderers.components.HtmlCheckBox;
import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlContainer;
import net.sourceforge.fenixedu.renderers.components.HtmlImage;
import net.sourceforge.fenixedu.renderers.components.HtmlInlineContainer;
import net.sourceforge.fenixedu.renderers.components.HtmlLink;
import net.sourceforge.fenixedu.renderers.components.HtmlTable;
import net.sourceforge.fenixedu.renderers.components.HtmlTableCell;
import net.sourceforge.fenixedu.renderers.components.HtmlTableRow;
import net.sourceforge.fenixedu.renderers.components.HtmlText;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.util.LanguageUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

public class StudentCurricularPlanRenderer extends InputRenderer {

    public static enum EnrolmentStateFilterType {
	ALL, APPROVED_OR_ENROLED;

	public String getName() {
	    return name();
	}

	public String getFullyQualifiedName() {
	    return getClass().getName() + "." + name();
	}

	public static EnrolmentStateFilterType[] getValues() {
	    return values();
	}

    }

    public static enum ViewType {
	ALL, ENROLMENTS, DISMISSALS;

	public String getName() {
	    return name();
	}

	public String getFullyQualifiedName() {
	    return getClass() + "." + name();
	}

	public static ViewType[] getValues() {
	    return values();
	}

    }

    public static enum OrganizationType {
	GROUPS, EXECUTION_YEARS;

	public String getName() {
	    return name();
	}

	public String getFullyQualifiedName() {
	    return getClass() + "." + name();
	}

	public static OrganizationType[] getValues() {
	    return values();
	}

    }

    private final ResourceBundle studentResources = ResourceBundle.getBundle("resources.StudentResources", LanguageUtils
	    .getLocale());

    private final ResourceBundle enumerationResources = ResourceBundle.getBundle("resources.EnumerationResources", LanguageUtils
	    .getLocale());

    private final ResourceBundle applicationResources = ResourceBundle.getBundle("resources.ApplicationResources", LanguageUtils
	    .getLocale());

    private OrganizationType organizedBy = OrganizationType.GROUPS;

    private EnrolmentStateFilterType enrolmentStateFilter = EnrolmentStateFilterType.ALL;

    private boolean detailed = true;

    private ViewType viewType = ViewType.ALL;

    private String studentCurricularPlanClass = "scplan";

    private String curriculumGroupRowClass = "scplangroup";

    private String enrolmentRowClass = "scplanenrollment";

    private String dismissalRowClass = "scplandismissal";

    private String temporaryDismissalRowClass = "scplantemporarydismissal";

    private String cellClasses = "scplancolident, scplancolcurricularcourse, scplancoldegreecurricularplan, scplancolenrollmentstate, "
	    + "scplancolenrollmenttype, scplancolgrade, scplancolweight, scplancolects, "
	    + "scplancolenrolmentevaluationtype, scplancolyear, scplancolsemester, scplancolexamdate, scplancolgraderesponsible";

    private boolean selectable;

    private String selectionName;

    public StudentCurricularPlanRenderer() {
	super();
    }

    public void setOrganizedBy(String organizedBy) {
	this.organizedBy = OrganizationType.valueOf(organizedBy);
    }

    public void setOrganizedByEnum(final OrganizationType organizationType) {
	this.organizedBy = organizationType;
    }

    public boolean isOrganizedByGroups() {
	return this.organizedBy == OrganizationType.GROUPS;
    }

    public boolean isOrganizedByExecutionYears() {
	return this.organizedBy == OrganizationType.EXECUTION_YEARS;
    }

    public void setEnrolmentStateFilter(final String type) {
	this.enrolmentStateFilter = EnrolmentStateFilterType.valueOf(type);
    }

    public void setEnrolmentStateFilterEnum(final EnrolmentStateFilterType enrolmentStateFilter) {
	this.enrolmentStateFilter = enrolmentStateFilter;
    }

    private boolean isToShowAllEnrolmentStates() {
	return this.enrolmentStateFilter == EnrolmentStateFilterType.ALL;
    }

    private boolean isToShowApprovedOrEnroledStatesOnly() {
	return this.enrolmentStateFilter == EnrolmentStateFilterType.APPROVED_OR_ENROLED;
    }

    public void setViewType(final String type) {
	this.viewType = ViewType.valueOf(type);
    }

    public void setViewTypeEnum(final ViewType viewType) {
	this.viewType = viewType;
    }

    private boolean isToShowDismissals() {
	return this.viewType == ViewType.DISMISSALS || this.viewType == ViewType.ALL;
    }

    private boolean isToShowEnrolments() {
	return this.viewType == ViewType.ENROLMENTS || this.viewType == ViewType.ALL;
    }

    private String getStudentCurricularPlanClass() {
	return studentCurricularPlanClass;
    }

    public void setStudentCurricularPlanClass(String studentCurricularPlanClass) {
	this.studentCurricularPlanClass = studentCurricularPlanClass;
    }

    private String getCurriculumGroupRowClass() {
	return curriculumGroupRowClass;
    }

    public void setCurriculumGroupRowClass(String curriculumGroupRowClass) {
	this.curriculumGroupRowClass = curriculumGroupRowClass;
    }

    private String getEnrolmentRowClass() {
	return enrolmentRowClass;
    }

    public void setEnrolmentRowClass(String enrolmentRowClass) {
	this.enrolmentRowClass = enrolmentRowClass;
    }

    private String getDismissalRowClass() {
	return dismissalRowClass;
    }

    public void setDismissalRowClass(String dismissalRowClass) {
	this.dismissalRowClass = dismissalRowClass;
    }

    private String getTemporaryDismissalRowClass() {
	return temporaryDismissalRowClass;
    }

    public void setTemporaryDismissalRowClass(String temporaryDismissalRowClass) {
	this.temporaryDismissalRowClass = temporaryDismissalRowClass;
    }

    public void setCellClasses(String cellClasses) {
	this.cellClasses = cellClasses;
    }

    private String[] getCellClasses() {
	return this.cellClasses.split(",");
    }

    private String getTabCellClass() {
	return getCellClasses()[0];
    }

    private String getLabelCellClass() {
	return getCellClasses()[1];
    }

    private String getDegreeCurricularPlanCellClass() {
	return getCellClasses()[2];
    }

    private String getEnrolmentStateCellClass() {
	return getCellClasses()[3];
    }

    private String getEnrolmentTypeCellClass() {
	return getCellClasses()[4];
    }

    private String getGradeCellClass() {
	return getCellClasses()[5];
    }

    private String getWeightCellClass() {
	return getCellClasses()[6];
    }

    private String getEctsCreditsCellClass() {
	return getCellClasses()[7];
    }

    private String getLastEnrolmentEvaluationTypeCellClass() {
	return getCellClasses()[8];
    }

    private String getEnrolmentExecutionYearCellClass() {
	return getCellClasses()[9];
    }

    private String getEnrolmentSemesterCellClass() {
	return getCellClasses()[10];
    }

    private String getExamDateCellClass() {
	return getCellClasses()[11];
    }

    private String getGradeResponsibleCellClass() {
	return getCellClasses()[12];
    }

    public boolean isDetailed() {
	return detailed;
    }

    public void setDetailed(boolean detailed) {
	this.detailed = detailed;
    }

    public boolean isSelectable() {
	return selectable;
    }

    public void setSelectable(boolean selectable) {
	this.selectable = selectable;
    }

    public String getSelectionName() {
	return selectionName;
    }

    public void setSelectionName(String selectionName) {
	this.selectionName = selectionName;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
	return new StudentCurricularPlanLayout();
    }

    private class StudentCurricularPlanLayout extends Layout {

	private static final String SPACER_IMAGE_PATH = "/images/scp_spacer.gif";

	private static final int MAX_LINE_SIZE = 25;

	private static final int MAX_COL_SPAN_FOR_TEXT_ON_GROUPS_WITH_CHILDS = 17;

	private static final int MAX_COL_SPAN_FOR_TEXT_ON_CURRICULUM_LINES = 14;

	private static final int HEADERS_SIZE = 3;

	private static final int COLUMNS_BETWEEN_TEXT_AND_GRADE = 3;

	private static final int COLUMNS_BETWEEN_TEXT_AND_ECTS = 5;

	private static final int COLUMNS_FROM_ECTS_AND_ENROLMENT_EVALUATION_DATE = 4;

	private static final int COLUMNS_BETWEEN_ECTS_AND_ENROLMENT_EVALUATION_DATE = COLUMNS_FROM_ECTS_AND_ENROLMENT_EVALUATION_DATE - 1;

	private static final int COLUMNS_BETWEEN_TEXT_AND_ENROLMENT_EVALUATION_DATE = COLUMNS_BETWEEN_TEXT_AND_ECTS
		+ COLUMNS_FROM_ECTS_AND_ENROLMENT_EVALUATION_DATE;

	private static final int LATEST_ENROLMENT_EVALUATION_COLUMNS = 2;

	private static final String DATE_FORMAT = "yyyy/MM/dd";

	private StudentCurricularPlan studentCurricularPlan;

	@Override
	public HtmlComponent createComponent(Object object, Class type) {
	    getInputContext().getForm().getSubmitButton().setVisible(false);
	    getInputContext().getForm().getCancelButton().setVisible(false);

	    this.studentCurricularPlan = (StudentCurricularPlan) object;

	    final HtmlContainer container = new HtmlBlockContainer();

	    if (this.studentCurricularPlan == null) {
		container.addChild(createHtmlTextItalic(studentResources.getString("message.no.curricularplan")));

		return container;
	    }

	    if (isOrganizedByGroups() && !this.studentCurricularPlan.isBoxStructure()) {
		container.addChild(createHtmlTextItalic(studentResources.getString("not.applicable")));

		return container;
	    }

	    final HtmlTable mainTable = new HtmlTable();
	    container.addChild(mainTable);
	    mainTable.setClasses(getStudentCurricularPlanClass());

	    if (isOrganizedByGroups()) {
		generateRowsForGroupsOrganization(mainTable, this.studentCurricularPlan.getRoot(), 0);
	    } else if (isOrganizedByExecutionYears()) {
		generateRowsForExecutionYearsOrganization(mainTable);
	    } else {
		throw new RuntimeException("Unexpected organization type");
	    }

	    return container;
	}

	@SuppressWarnings("unchecked")
	private void generateRowsForExecutionYearsOrganization(HtmlTable mainTable) {

	    if (isToShowEnrolments()) {
		final Set<ExecutionPeriod> enrolmentExecutionPeriods = new TreeSet<ExecutionPeriod>(
			ExecutionPeriod.EXECUTION_PERIOD_COMPARATOR_BY_SEMESTER_AND_YEAR);
		enrolmentExecutionPeriods.addAll(this.studentCurricularPlan.getEnrolmentsExecutionPeriods());

		for (final ExecutionPeriod enrolmentsExecutionPeriod : enrolmentExecutionPeriods) {
		    generateGroupRowWithText(mainTable, enrolmentsExecutionPeriod.getYear() + ", "
			    + enrolmentsExecutionPeriod.getName(), true, 0, (CurriculumGroup) null);
		    generateEnrolmentRows(mainTable, this.studentCurricularPlan
			    .getEnrolmentsByExecutionPeriod(enrolmentsExecutionPeriod), 0);
		}
	    }

	    if (isToShowDismissals()) {
		final List<Dismissal> dismissals = this.studentCurricularPlan.getDismissals();
		if (!dismissals.isEmpty()) {
		    generateGroupRowWithText(mainTable, studentResources.getString("label.dismissals"), true, 0, (CurriculumGroup) null);
		    generateDismissalRows(mainTable, dismissals, 0);
		}
	    }

	}

	private HtmlText createHtmlTextItalic(final String message) {
	    final HtmlText htmlText = new HtmlText(message);
	    htmlText.setClasses("italic");

	    return htmlText;
	}

	private void generateRowsForGroupsOrganization(final HtmlTable mainTable, final CurriculumGroup curriculumGroup,
		final int level) {

	    generateGroupRowWithText(mainTable, curriculumGroup.getName().getContent(), curriculumGroup.hasCurriculumLines(),
		    level, curriculumGroup);
	    generateCurriculumLineRows(mainTable, curriculumGroup, level + 1);
	    generateChildGroupRows(mainTable, curriculumGroup, level + 1);
	}

	private void generateGroupRowWithText(final HtmlTable mainTable, final String text, boolean addHeaders, final int level, final CurriculumGroup curriculumGroup) {

	    final HtmlTableRow groupRow = mainTable.createRow();
	    groupRow.setClasses(getCurriculumGroupRowClass());
	    addTabsToRow(groupRow, level);

	    final HtmlTableCell cell = groupRow.createCell();
	    cell.setClasses(getLabelCellClass());
	    cell.setBody(curriculumGroup != null && curriculumGroup.isRoot() ? 
		    createDegreeCurricularPlanNameLink(
			    curriculumGroup.getDegreeCurricularPlanOfDegreeModule(), 
			    curriculumGroup.getStudentCurricularPlan().getStartExecutionPeriod()) : 
			new HtmlText(text));

	    if (!addHeaders) {
		cell.setColspan(MAX_LINE_SIZE - level);
	    } else {
		cell.setColspan(MAX_COL_SPAN_FOR_TEXT_ON_GROUPS_WITH_CHILDS - level);
		
		generateHeadersForGradeWeightAndEctsCredits(groupRow);
		final HtmlTableCell cellAfterEcts = groupRow.createCell();
		cellAfterEcts.setColspan(MAX_LINE_SIZE - MAX_COL_SPAN_FOR_TEXT_ON_GROUPS_WITH_CHILDS - HEADERS_SIZE);
	    }
	}

	private void generateCurriculumLineRows(HtmlTable mainTable, CurriculumGroup curriculumGroup, int level) {

	    if (isToShowDismissals()) {
		generateDismissalRows(mainTable, curriculumGroup.getChildDismissals(), level);
	    }

	    if (isToShowEnrolments()) {
		generateEnrolmentRows(mainTable, curriculumGroup.getChildEnrolments(), level);
	    }
	}

	private void generateDismissalRows(HtmlTable mainTable, List<Dismissal> dismissals, int level) {
	    final Set<Dismissal> sortedDismissals = new TreeSet<Dismissal>(Dismissal.COMPARATOR_BY_NAME_AND_ID);
	    sortedDismissals.addAll(dismissals);

	    for (final Dismissal dismissal : sortedDismissals) {
		generateDismissalRow(mainTable, dismissal, level);
	    }
	}

	private void generateDismissalRow(HtmlTable mainTable, Dismissal dismissal, int level) {
	    final HtmlTableRow dismissalRow = mainTable.createRow();
	    addTabsToRow(dismissalRow, level);
	    dismissalRow.setClasses(dismissal.getCredits().isTemporary() ? getTemporaryDismissalRowClass()
		    : getDismissalRowClass());

	    generateDismissalLabelCell(mainTable, dismissalRow, dismissal, level);
	    generateCellsBetweenLabelAndGradeCell(dismissalRow);
	    generateDismissalGradeCell(dismissalRow, dismissal);
	    generateDismissalWeightCell(dismissalRow, dismissal);
	    generateDismissalEctsCell(dismissalRow, dismissal);
	    generateCellsBetweenEctsAndEvaluationDate(dismissalRow);
	    generateCellsFromEvaluationDateToEnd(dismissalRow);
	    generateSpacerCellsIfRequired(dismissalRow);
	}

	private void generateDismissalWeightCell(HtmlTableRow dismissalRow, Dismissal dismissal) {
	    generateCellWithText(dismissalRow, "-", getWeightCellClass());

	}

	private void generateDismissalGradeCell(HtmlTableRow dismissalRow, Dismissal dismissal) {
	    final String gradeValue = !StringUtils.isEmpty(dismissal.getCredits().getGivenGrade()) ? dismissal.getCredits()
		    .getGivenGrade() : null;
	    final String gradeString;
	    if (gradeValue != null && NumberUtils.isNumber(gradeValue)) {
		final DecimalFormat decimalFormat = new DecimalFormat("##");
		gradeString = decimalFormat.format(Double.valueOf(gradeValue));
	    } else {
		gradeString = gradeValue != null ? gradeValue : "-";
	    }

	    generateCellWithText(dismissalRow, gradeString, getGradeCellClass());
	}

	private void generateCellsBetweenLabelAndGradeCell(HtmlTableRow dismissalRow) {
	    generateCellsWithText(dismissalRow, COLUMNS_BETWEEN_TEXT_AND_GRADE, "-", new String[] {
		    getDegreeCurricularPlanCellClass(), getEnrolmentTypeCellClass(), getEnrolmentStateCellClass() });

	}

	private void generateCellsFromEvaluationDateToEnd(HtmlTableRow dismissalRow) {
	    if (isToShowLatestEnrolmentEvaluationInfo()) {
		generateCellsWithText(dismissalRow, LATEST_ENROLMENT_EVALUATION_COLUMNS, "-", new String[] {
			getLastEnrolmentEvaluationTypeCellClass(), getGradeResponsibleCellClass() });
	    }

	}

	private void generateCellsBetweenEctsAndEvaluationDate(final HtmlTableRow dismissalRow) {
	    generateCellsWithText(dismissalRow, COLUMNS_BETWEEN_ECTS_AND_ENROLMENT_EVALUATION_DATE, "-", new String[] {
		    getLastEnrolmentEvaluationTypeCellClass(), getEnrolmentExecutionYearCellClass(),
		    getEnrolmentSemesterCellClass() });
	}

	private void generateCellsWithText(final HtmlTableRow row, final int numberOfCells, final String text,
		final String[] cssClasses) {
	    for (int i = 0; i < numberOfCells; i++) {
		generateCellWithText(row, "-", cssClasses[i]);
	    }
	}

	private void generateDismissalEctsCell(HtmlTableRow dismissalRow, Dismissal dismissal) {
	    generateCellWithText(dismissalRow, dismissal.getEctsCredits() != null ? dismissal.getEctsCredits().toString() : "-",
		    getEctsCreditsCellClass());
	}

	private void generateDismissalLabelCell(final HtmlTable mainTable, HtmlTableRow dismissalRow, Dismissal dismissal, int level) {
	    if (dismissal.hasCurricularCourse() || loggedPersonIsManager()) {
		final HtmlTableCell cell = dismissalRow.createCell();
		cell.setColspan(MAX_COL_SPAN_FOR_TEXT_ON_CURRICULUM_LINES - level);
		cell.setClasses(getLabelCellClass());
		final HtmlInlineContainer container = new HtmlInlineContainer();
		cell.setBody(container);

		if (isSelectable()) {
		    final HtmlCheckBox checkBox = new HtmlCheckBox();
		    checkBox.setName(getSelectionName());
		    checkBox.setUserValue(dismissal.getIdInternal().toString());
		    container.addChild(checkBox);
		}

		final HtmlText text = new HtmlText(studentResources.getString("label.dismissal." + dismissal.getCredits().getClass().getSimpleName()));
		container.addChild(text);
		
		final CurricularCourse curricularCourse = dismissal.getCurricularCourse();
		if (curricularCourse != null) {

		    String codeAndName = "";
		    if (!StringUtils.isEmpty(curricularCourse.getCode())) {
			codeAndName += curricularCourse.getCode() + " - ";
		    }
		    codeAndName += dismissal.getName().getContent();
		    final HtmlLink curricularCourseLink = createCurricularCourseLink(codeAndName, curricularCourse);
		    container.addChild(new HtmlText(": "));
		    container.addChild(curricularCourseLink);
		}

	    } else {
		generateCellWithText(dismissalRow, studentResources.getString("label.dismissal." + dismissal.getCredits().getClass().getSimpleName()), getLabelCellClass(),
			MAX_COL_SPAN_FOR_TEXT_ON_CURRICULUM_LINES - level);
	    }

	    if (isDetailed()) {
		generateDismissalDetails(mainTable, dismissal, level);
	    }

	}

	private Boolean loggedPersonIsManager() {
	    return AccessControl.getPerson().hasRole(RoleType.MANAGER);
	}

	private void generateDismissalDetails(final HtmlTable mainTable, Dismissal dismissal, int level) {
	    for (final IEnrolment enrolment : dismissal.getSourceIEnrolments()) {
		if (enrolment.isExternalEnrolment()) {
		    generateExternalEnrolmentRow(mainTable, (ExternalEnrolment) enrolment, level + 1);
		} else {
		    generateEnrolmentRow(mainTable, (Enrolment) enrolment, level + 1, false, true);
		}
	    }
	}

	private void generateExternalEnrolmentRow(HtmlTable mainTable, ExternalEnrolment externalEnrolment, int level) {

	    final HtmlTableRow externalEnrolmentRow = mainTable.createRow();
	    externalEnrolmentRow.setClasses(getEnrolmentRowClass());
	    addTabsToRow(externalEnrolmentRow, level);

	    generateExternalEnrolmentLabelCell(externalEnrolmentRow, externalEnrolment, level);
	    generateCellsBetweenLabelAndGradeCell(externalEnrolmentRow);
	    generateEnrolmentGradeCell(externalEnrolmentRow, externalEnrolment);
	    generateEnrolmentWeightCell(externalEnrolmentRow, externalEnrolment);
	    generateExternalEnrolmentEctsCell(externalEnrolmentRow, externalEnrolment);
	    generateCellsBetweenEctsAndEvaluationDate(externalEnrolmentRow);
	    generateCellsFromEvaluationDateToEnd(externalEnrolmentRow);
	    generateSpacerCellsIfRequired(externalEnrolmentRow);

	}

	private void generateExternalEnrolmentEctsCell(HtmlTableRow externalEnrolmentRow, ExternalEnrolment externalEnrolment) {
	    generateCellWithText(externalEnrolmentRow, externalEnrolment.getEctsCredits().toString(), getEctsCreditsCellClass());
	}

	private void generateExternalEnrolmentLabelCell(final HtmlTableRow externalEnrolmentRow,
		final ExternalEnrolment externalEnrolment, final int level) {
	    generateCellWithText(externalEnrolmentRow, externalEnrolment.getDescription(), getLabelCellClass(),
		    MAX_COL_SPAN_FOR_TEXT_ON_CURRICULUM_LINES - level);
	}

	private void generateEnrolmentRows(HtmlTable mainTable, List<Enrolment> childEnrolments, int level) {
	    final Set<Enrolment> sortedEnrolments = new TreeSet<Enrolment>(Enrolment.COMPARATOR_BY_NAME_AND_ID);
	    sortedEnrolments.addAll(childEnrolments);

	    for (final Enrolment enrolment : sortedEnrolments) {
		if (isToShowAllEnrolmentStates()) {
		    generateEnrolmentRow(mainTable, enrolment, level, true, false);
		} else if (isToShowApprovedOrEnroledStatesOnly()) {
		    if (enrolment.isApproved() || enrolment.isEnroled()) {
			generateEnrolmentRow(mainTable, enrolment, level, true, false);
		    }
		} else {
		    throw new RuntimeException("Unexpected enrolment state filter type");
		}
	    }
	}

	private void generateEnrolmentRow(HtmlTable mainTable, Enrolment enrolment, int level, boolean allowSelection, boolean isFromDetail) {
	    final HtmlTableRow enrolmentRow = mainTable.createRow();
	    addTabsToRow(enrolmentRow, level);
	    enrolmentRow.setClasses(getEnrolmentRowClass());

	    if(enrolment.isEnroled()) {
		generateEnrolmentWithStateEnroled(enrolmentRow, enrolment, level, allowSelection);
	    } else {
		generateCurricularCourseCodeAndNameCell(enrolmentRow, enrolment, level, allowSelection);
		generateDegreeCurricularPlanCell(enrolmentRow, enrolment);
		generateEnrolmentTypeCell(enrolmentRow, enrolment);
		generateEnrolmentStateCell(enrolmentRow, enrolment);
		generateEnrolmentGradeCell(enrolmentRow, enrolment);
		generateEnrolmentWeightCell(enrolmentRow, enrolment);
		generateEnrolmentEctsCell(enrolmentRow, enrolment, isFromDetail);
		generateEnrolmentLastEnrolmentEvaluationTypeCell(enrolmentRow, enrolment);
		generateEnrolmentExecutionYearCell(enrolmentRow, enrolment);
		generateEnrolmentSemesterCell(enrolmentRow, enrolment);
		generateLastEnrolmentEvaluationExamDateCellIfRequired(enrolmentRow, enrolment);
		generateGradeResponsibleIfRequired(enrolmentRow, enrolment);
		generateSpacerCellsIfRequired(enrolmentRow);
	    }

	}

	private void generateEnrolmentWithStateEnroled(HtmlTableRow enrolmentRow, Enrolment enrolment, int level,
		boolean allowSelection) {
	    generateCurricularCourseCodeAndNameCell(enrolmentRow, enrolment, level, allowSelection);
	    generateDegreeCurricularPlanCell(enrolmentRow, enrolment);
	    generateEnrolmentTypeCell(enrolmentRow, enrolment);
	    generateEnrolmentStateCell(enrolmentRow, enrolment);
	    generateCellWithText(enrolmentRow, "-", getGradeCellClass()); //grade
	    generateCellWithText(enrolmentRow, "-", getWeightCellClass()); //weight
	    generateCellWithText(enrolmentRow, "-", getEctsCreditsCellClass()); //ects
	    generateEnrolmentEvaluationTypeCell(enrolmentRow, enrolment);
	    generateEnrolmentExecutionYearCell(enrolmentRow, enrolment);
	    generateEnrolmentSemesterCell(enrolmentRow, enrolment);
	    if (isToShowLatestEnrolmentEvaluationInfo()) {
		generateCellWithText(enrolmentRow, "-", getLastEnrolmentEvaluationTypeCellClass()); //enrolment evaluation date
		generateCellWithText(enrolmentRow, "-", getGradeResponsibleCellClass()); //grade responsible
	    }
	    generateSpacerCellsIfRequired(enrolmentRow);
	}

	private void generateGradeResponsibleIfRequired(HtmlTableRow enrolmentRow, Enrolment enrolment) {
	    if (isToShowLatestEnrolmentEvaluationInfo()) {
		final EnrolmentEvaluation lastEnrolmentEvaluation = enrolment.getLatestEnrolmentEvaluation();
		if (lastEnrolmentEvaluation != null && lastEnrolmentEvaluation.getPersonResponsibleForGrade() != null) {

		    final Person person = lastEnrolmentEvaluation.getPersonResponsibleForGrade();
		    final String username = person.hasEmployee() ? person.getEmployee().getRoleLoginAlias() : person
			    .getIstUsername();
		    generateCellWithSpan(enrolmentRow, username, applicationResources.getString("label.grade.responsiblePerson"),
			    getGradeResponsibleCellClass());

		} else {
		    generateCellWithText(enrolmentRow, "-", getGradeResponsibleCellClass());
		}
	    }

	}

	private void generateLastEnrolmentEvaluationExamDateCellIfRequired(HtmlTableRow enrolmentRow, Enrolment enrolment) {
	    if (isToShowLatestEnrolmentEvaluationInfo()) {
		final EnrolmentEvaluation lastEnrolmentEvaluation = enrolment.getLatestEnrolmentEvaluation();
		if (lastEnrolmentEvaluation != null && lastEnrolmentEvaluation.getExamDateYearMonthDay() != null) {

		    generateCellWithSpan(enrolmentRow, lastEnrolmentEvaluation.getExamDateYearMonthDay().toString(DATE_FORMAT),
			    applicationResources.getString("label.data.avaliacao"), getExamDateCellClass());
		} else {
		    generateCellWithText(enrolmentRow, "-", getExamDateCellClass());
		}
	    }
	}

	private void generateSpacerCellsIfRequired(final HtmlTableRow row) {
	    final int spacerColspan = calculateSpacerColspan();
	    if (spacerColspan > 0) {
		final HtmlTableCell spaceCells = row.createCell();
		spaceCells.setColspan(spacerColspan);
		spaceCells.setText("");
	    }
	}

	private int calculateSpacerColspan() {
	    return MAX_LINE_SIZE - MAX_COL_SPAN_FOR_TEXT_ON_CURRICULUM_LINES - COLUMNS_BETWEEN_TEXT_AND_ENROLMENT_EVALUATION_DATE
		    - (isToShowLatestEnrolmentEvaluationInfo() ? LATEST_ENROLMENT_EVALUATION_COLUMNS : 0);
	}

	private void generateEnrolmentSemesterCell(final HtmlTableRow enrolmentRow, final Enrolment enrolment) {
	    final String semester = enrolment.getExecutionPeriod().getSemester().toString() + " "
		    + applicationResources.getString("label.semester.short");

	    generateCellWithText(enrolmentRow, semester, getEnrolmentSemesterCellClass());
	}

	private void generateEnrolmentExecutionYearCell(HtmlTableRow enrolmentRow, Enrolment enrolment) {
	    generateCellWithText(enrolmentRow, enrolment.getExecutionYear().getYear(), getEnrolmentExecutionYearCellClass());
	}

	private void generateEnrolmentLastEnrolmentEvaluationTypeCell(HtmlTableRow enrolmentRow, Enrolment enrolment) {
	    final EnrolmentEvaluation lastEnrolmentEvaluation = enrolment.getLatestEnrolmentEvaluation();
	    if (lastEnrolmentEvaluation != null && lastEnrolmentEvaluation.getEnrolmentEvaluationType() != null) {
		generateCellWithSpan(enrolmentRow, enumerationResources.getString(lastEnrolmentEvaluation
			.getEnrolmentEvaluationType().getAcronym()), getLastEnrolmentEvaluationTypeCellClass());
	    } else {
		generateCellWithText(enrolmentRow, "-", getLastEnrolmentEvaluationTypeCellClass());
	    }

	}
	
	private void generateEnrolmentEvaluationTypeCell(HtmlTableRow enrolmentRow, Enrolment enrolment) {
	    final EnrolmentEvaluationType enrolmentEvaluationType = enrolment.getEnrolmentEvaluationType();
	    if (enrolmentEvaluationType != null) {
		generateCellWithSpan(enrolmentRow, enumerationResources.getString(enrolmentEvaluationType.getAcronym()),
			getLastEnrolmentEvaluationTypeCellClass());
	    } else {
		generateCellWithText(enrolmentRow, "-", getLastEnrolmentEvaluationTypeCellClass());
	    }

	}


	private void generateEnrolmentEctsCell(final HtmlTableRow enrolmentRow, final Enrolment enrolment, final boolean isFromDetail) {
	    final String ectsCredits;
	    if (enrolment.isApproved()) {
		ectsCredits = String.valueOf(isFromDetail ? enrolment.getEctsCreditsForCurriculum() : enrolment.getEctsCredits());
	    } else {
		ectsCredits = "-";
	    }
	    generateCellWithText(enrolmentRow, ectsCredits, getEctsCreditsCellClass());
	}

	private void generateEnrolmentWeightCell(HtmlTableRow enrolmentRow, IEnrolment enrolment) {
	    // Weight is only relevant to show when enrolment has numeric value
	    generateCellWithText(enrolmentRow, enrolment.getFinalGrade() != null ? enrolment.getWeigth().toString() : "-",
		    getWeightCellClass());

	}

	private void generateEnrolmentGradeCell(HtmlTableRow enrolmentRow, IEnrolment enrolment) {
	    final Grade grade = enrolment.getGrade();
	    generateCellWithText(enrolmentRow, grade.isEmpty() ? "-" : grade.getValue(),
		    getGradeCellClass());
	}

	private void generateEnrolmentStateCell(HtmlTableRow enrolmentRow, Enrolment enrolment) {
	    generateCellWithText(enrolmentRow, enrolment.isApproved() ? "-" : enumerationResources.getString(enrolment
		    .getEnrollmentState().getQualifiedName()), getEnrolmentStateCellClass());

	}

	private void generateEnrolmentTypeCell(HtmlTableRow enrolmentRow, Enrolment enrolment) {
	    generateCellWithText(enrolmentRow, enrolment.isEnrolmentTypeNormal() ? "-" : enumerationResources.getString(enrolment
		    .getEnrolmentTypeName()), getEnrolmentTypeCellClass());
	}

	private void generateDegreeCurricularPlanCell(final HtmlTableRow enrolmentRow, final Enrolment enrolment) {

	    if (enrolment.isFor(studentCurricularPlan.getDegreeCurricularPlan())) {
		generateCellWithText(enrolmentRow, "-", getDegreeCurricularPlanCellClass());
	    } else {
		final HtmlTableCell cell = enrolmentRow.createCell();
		cell.setClasses(getDegreeCurricularPlanCellClass());
		cell.setBody(createDegreeCurricularPlanNameLink(enrolment.getDegreeCurricularPlanOfDegreeModule(), enrolment
			.getExecutionPeriod()));
	    }

	}

	private HtmlComponent createDegreeCurricularPlanNameLink(final DegreeCurricularPlan degreeCurricularPlan,
		ExecutionPeriod executionPeriod) {
	    if (degreeCurricularPlan.isPast()) {
		return new HtmlText(degreeCurricularPlan.getName());
	    }

	    final HtmlLink result = new HtmlLink();

	    result.setText(degreeCurricularPlan.getName());
	    result.setModuleRelative(false);
	    result.setTarget("_blank");

	    if (degreeCurricularPlan.isBoxStructure()) {
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

	    result.setParameter("executionPeriodOID", executionPeriod != null ? executionPeriod.getIdInternal() : ExecutionPeriod
		    .readActualExecutionPeriod().getIdInternal());

	    return result;
	}

	private void generateCurricularCourseCodeAndNameCell(final HtmlTableRow enrolmentRow, final Enrolment enrolment,
		final int level, boolean allowSelection) {

	    final HtmlInlineContainer inlineContainer = new HtmlInlineContainer();

	    if (isSelectable() && allowSelection) {
		final HtmlCheckBox checkBox = new HtmlCheckBox();
		checkBox.setName(getSelectionName());
		checkBox.setUserValue(enrolment.getIdInternal().toString());
		inlineContainer.addChild(checkBox);
	    }

	    inlineContainer.addChild(createCurricularCourseLink(getPresentationNameFor(enrolment), enrolment
		    .getCurricularCourse()));

	    final HtmlTableCell cell = enrolmentRow.createCell();
	    cell.setClasses(getLabelCellClass());
	    cell.setColspan(MAX_COL_SPAN_FOR_TEXT_ON_CURRICULUM_LINES - level);
	    cell.setBody(inlineContainer);
	}

	private String getPresentationNameFor(final Enrolment enrolment) {
	    final String code = !StringUtils.isEmpty(enrolment.getCurricularCourse().getCode()) ? enrolment.getCurricularCourse()
		    .getCode()
		    + " - " : "";
		    
	    if (enrolment instanceof OptionalEnrolment) {
		final OptionalEnrolment optionalEnrolment = (OptionalEnrolment) enrolment;
		return optionalEnrolment.getOptionalCurricularCourse().getName() + " (" + code
			+ optionalEnrolment.getCurricularCourse().getName() + ")";
	    } else {
		return code + enrolment.getName().getContent();
	    }
	}

	private HtmlLink createCurricularCourseLink(final String text, final CurricularCourse curricularCourse) {

	    final HtmlLink result = new HtmlLink();
	    result.setBody(new HtmlText(text));
	    result.setModuleRelative(false);
	    result.setTarget(HtmlLink.Target.BLANK);

	    result.setParameter("degreeID", curricularCourse.getDegreeCurricularPlan().getDegree().getIdInternal());
	    result.setParameter("curricularCourseID", curricularCourse.getIdInternal());
	    result.setParameter("degreeCurricularPlanID", curricularCourse.getDegreeCurricularPlan().getIdInternal());

	    if (curricularCourse.isBolonhaDegree()) {
		result.setParameter("organizeBy", "groups");
		result.setParameter("showRules", "false");
		result.setParameter("hideCourses", "false");
		result.setUrl("/publico/degreeSite/viewCurricularCourse.faces");
	    } else {
		result.setUrl("/publico/showCourseSite.do?method=showCurricularCourseSite");
	    }

	    return result;
	}

	private void generateChildGroupRows(HtmlTable mainTable, CurriculumGroup parentGroup, int level) {
	    final Set<CurriculumGroup> sortedCurriculumGroups = new TreeSet<CurriculumGroup>(
		    CurriculumGroup.COMPARATOR_BY_CHILD_ORDER_AND_ID);

	    sortedCurriculumGroups.addAll(parentGroup.getCurriculumGroups());

	    for (final CurriculumGroup childGroup : sortedCurriculumGroups) {
		generateRowsForGroupsOrganization(mainTable, childGroup, level);
	    }
	}

    }

    private void addTabsToRow(final HtmlTableRow row, final int level) {
	for (int i = 0; i < level; i++) {
	    HtmlLink link = new HtmlLink();
	    link.setModuleRelative(false);
	    link.setUrl(StudentCurricularPlanLayout.SPACER_IMAGE_PATH);

	    final HtmlImage spacerImage = new HtmlImage();
	    spacerImage.setSource(link.calculateUrl());

	    final HtmlTableCell tabCell = row.createCell();
	    tabCell.setClasses(getTabCellClass());
	    tabCell.setBody(spacerImage);
	}
    }

    private void generateHeadersForGradeWeightAndEctsCredits(final HtmlTableRow groupRow) {
	generateCellWithText(groupRow, applicationResources.getString("label.grade"), getGradeCellClass());

	generateCellWithText(groupRow, applicationResources.getString("label.weight"), getWeightCellClass());

	generateCellWithText(groupRow, applicationResources.getString("label.ects"), getEctsCreditsCellClass());

    }

    private void generateCellWithText(final HtmlTableRow row, final String text, final String cssClass) {
	generateCellWithText(row, text, cssClass, 1);
    }

    private void generateCellWithText(final HtmlTableRow row, final String text, final String cssClass, Integer colSpan) {
	final HtmlTableCell cell = row.createCell();
	cell.setClasses(cssClass);
	cell.setText(text);
	cell.setColspan(colSpan);
    }

    private void generateCellWithSpan(final HtmlTableRow row, final String text, final String cssClass) {
	generateCellWithSpan(row, text, null, cssClass);
    }

    private void generateCellWithSpan(final HtmlTableRow row, final String text, final String title, final String cssClass) {
	generateCellWithSpan(row, text, title, cssClass, 1);
    }

    private void generateCellWithSpan(final HtmlTableRow row, final String text, final String title, final String cssClass,
	    final Integer colSpan) {
	final HtmlTableCell cell = row.createCell();
	cell.setClasses(cssClass);
	cell.setColspan(colSpan);

	final HtmlInlineContainer span = new HtmlInlineContainer();
	span.addChild(new HtmlText(text));
	span.setTitle(title);

	cell.setBody(span);
    }

    private boolean isToShowLatestEnrolmentEvaluationInfo() {
	return AccessControl.getPerson().isAdministrativeOfficeEmployee();
    }

}
