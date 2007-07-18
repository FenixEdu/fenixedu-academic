package net.sourceforge.fenixedu.presentationTier.renderers.student.curriculum;

import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.Dismissal;
import net.sourceforge.fenixedu.domain.studentCurriculum.EnrolmentWrapper;
import net.sourceforge.fenixedu.domain.studentCurriculum.ExternalEnrolment;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.renderers.controllers.CopyCheckBoxValuesController;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyArrayConverter;
import net.sourceforge.fenixedu.renderers.InputRenderer;
import net.sourceforge.fenixedu.renderers.components.HtmlBlockContainer;
import net.sourceforge.fenixedu.renderers.components.HtmlCheckBox;
import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlContainer;
import net.sourceforge.fenixedu.renderers.components.HtmlImage;
import net.sourceforge.fenixedu.renderers.components.HtmlInlineContainer;
import net.sourceforge.fenixedu.renderers.components.HtmlLink;
import net.sourceforge.fenixedu.renderers.components.HtmlMultipleHiddenField;
import net.sourceforge.fenixedu.renderers.components.HtmlTable;
import net.sourceforge.fenixedu.renderers.components.HtmlTableCell;
import net.sourceforge.fenixedu.renderers.components.HtmlTableRow;
import net.sourceforge.fenixedu.renderers.components.HtmlText;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.model.MetaObject;
import net.sourceforge.fenixedu.renderers.model.MetaObjectFactory;
import net.sourceforge.fenixedu.renderers.model.MetaSlot;
import net.sourceforge.fenixedu.renderers.utils.RenderKit;
import net.sourceforge.fenixedu.util.LanguageUtils;

import org.apache.commons.lang.StringUtils;

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

    private final ResourceBundle studentResources = ResourceBundle.getBundle(
	    "resources.StudentResources", LanguageUtils.getLocale());

    private final ResourceBundle enumerationResources = ResourceBundle.getBundle(
	    "resources.EnumerationResources", LanguageUtils.getLocale());

    private final ResourceBundle applicationResources = ResourceBundle.getBundle(
	    "resources.ApplicationResources", LanguageUtils.getLocale());

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
	    + "scplancolextracurricularcheck, scplancolenrollmenttype, scplancolgrade, scplancolweight, scplancolects, "
	    + "scplancolenrolmentevaluationtype, scplancolyear, scplancolsemester, scplancolexamdate, scplancolgraderesponsible";

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

    private String getExtraCurricularCellClass() {
	return getCellClasses()[4];
    }

    private String getEnrolmentTypeCellClass() {
	return getCellClasses()[5];
    }

    private String getGradeCellClass() {
	return getCellClasses()[6];
    }

    private String getWeightCellClass() {
	return getCellClasses()[7];
    }

    private String getEctsCreditsCellClass() {
	return getCellClasses()[8];
    }

    private String getLastEnrolmentEvaluationTypeCellClass() {
	return getCellClasses()[9];
    }

    private String getEnrolmentExecutionYearCellClass() {
	return getCellClasses()[10];
    }

    private String getEnrolmentSemesterCellClass() {
	return getCellClasses()[11];
    }

    private String getExamDateCellClass() {
	return getCellClasses()[12];
    }

    private String getGradeResponsibleCellClass() {
	return getCellClasses()[13];
    }

    public boolean isDetailed() {
	return detailed;
    }

    public void setDetailed(boolean detailed) {
	this.detailed = detailed;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
	return new StudentCurricularPlanLayout();
    }

    private class StudentCurricularPlanLayout extends Layout {

	private static final String SPACER_IMAGE_PATH = "/images/scp_spacer.gif";

	private static final int MAX_LINE_SIZE = 25;

	private static final int MAX_COL_SPAN_FOR_TEXT_ON_GROUPS_WITH_CHILDS = 17;

	private static final int MAX_COL_SPAN_FOR_TEXT_ON_CURRICULUM_LINES = 13;

	private static final int HEADERS_SIZE = 3;

	private static final int COLUMNS_BETWEEN_TEXT_AND_GRADE = 4;

	private static final int COLUMNS_BETWEEN_TEXT_AND_ECTS = 6;

	private static final int COLUMNS_FROM_ECTS_AND_ENROLMENT_EVALUATION_DATE = 4;

	private static final int COLUMNS_BETWEEN_ECTS_AND_ENROLMENT_EVALUATION_DATE = COLUMNS_FROM_ECTS_AND_ENROLMENT_EVALUATION_DATE - 1;

	private static final int COLUMNS_BETWEEN_TEXT_AND_ENROLMENT_EVALUATION_DATE = COLUMNS_BETWEEN_TEXT_AND_ECTS
		+ COLUMNS_FROM_ECTS_AND_ENROLMENT_EVALUATION_DATE;

	private static final int LATEST_ENROLMENT_EVALUATION_COLUMNS = 2;

	private static final String DATE_FORMAT = "yyyy/MM/dd";

	private StudentCurricularPlan studentCurricularPlan;

	private CopyCheckBoxValuesController extraCurricularEnrolmentsController = new CopyCheckBoxValuesController();

	@Override
	public HtmlComponent createComponent(Object object, Class type) {
	    getInputContext().getForm().getSubmitButton().setVisible(false);
	    getInputContext().getForm().getCancelButton().setVisible(false);

	    this.studentCurricularPlan = (StudentCurricularPlan) object;

	    final HtmlContainer container = new HtmlBlockContainer();

	    if (this.studentCurricularPlan == null) {
		container.addChild(createHtmlTextItalic(studentResources
			.getString("message.no.curricularplan")));

		return container;
	    }

	    if (isOrganizedByGroups() && !this.studentCurricularPlan.isBoxStructure()) {
		container.addChild(createHtmlTextItalic(studentResources.getString("not.applicable")));

		return container;
	    }

	    if (this.studentCurricularPlan.getEnrolmentsSet().isEmpty()
		    && this.studentCurricularPlan.getDismissals().isEmpty()) {
		container.addChild(createHtmlTextItalic(studentResources
			.getString("message.no.enrolments")));

		return container;
	    }

	    if (canChangeExtraCurricularStatus()) {
		addExtraCurricularEnrolmentsHiddenField(container);
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

	private void addExtraCurricularEnrolmentsHiddenField(final HtmlContainer container) {
	    final MetaObject metaObject = getInputContext().getMetaObject();
	    metaObject.addSlot(new MetaSlot(metaObject, "extraCurricularEnrolments"));
	    final HtmlMultipleHiddenField hiddenExtraCurricularEnrolments = new HtmlMultipleHiddenField();
	    hiddenExtraCurricularEnrolments.bind(getInputContext().getMetaObject(),
		    "extraCurricularEnrolments");
	    hiddenExtraCurricularEnrolments.setConverter(new DomainObjectKeyArrayConverter());
	    hiddenExtraCurricularEnrolments.setController(extraCurricularEnrolmentsController);

	    container.addChild(hiddenExtraCurricularEnrolments);
	}

	@SuppressWarnings("unchecked")
	private void generateRowsForExecutionYearsOrganization(HtmlTable mainTable) {

	    if (isToShowEnrolments()) {
		final Set<ExecutionPeriod> enrolmentExecutionPeriods = new TreeSet<ExecutionPeriod>(
			ExecutionPeriod.EXECUTION_PERIOD_COMPARATOR_BY_SEMESTER_AND_YEAR);
		enrolmentExecutionPeriods.addAll(this.studentCurricularPlan
			.getEnrolmentsExecutionPeriods());

		for (final ExecutionPeriod enrolmentsExecutionPeriod : enrolmentExecutionPeriods) {
		    generateGroupRowWithText(mainTable, enrolmentsExecutionPeriod.getYear() + ", "
			    + enrolmentsExecutionPeriod.getName(), true, 0);
		    generateEnrolmentRows(mainTable, this.studentCurricularPlan
			    .getEnrolmentsByExecutionPeriod(enrolmentsExecutionPeriod), 0, true);
		}
	    }

	    if (isToShowDismissals()) {
		final List<Dismissal> dismissals = this.studentCurricularPlan.getDismissals();
		if (!dismissals.isEmpty()) {
		    generateGroupRowWithText(mainTable, studentResources.getString("label.dismissals"),
			    true, 0);
		    generateDismissalRows(mainTable, dismissals, 0);
		}
	    }

	}

	private HtmlText createHtmlTextItalic(final String message) {
	    final HtmlText htmlText = new HtmlText(message);
	    htmlText.setClasses("italic");

	    return htmlText;
	}

	private void generateRowsForGroupsOrganization(final HtmlTable mainTable,
		final CurriculumGroup curriculumGroup, final int level) {

	    generateGroupRowWithText(mainTable, curriculumGroup.getName().getContent(), curriculumGroup
		    .hasCurriculumLines(), level);
	    generateCurriculumLineRows(mainTable, curriculumGroup, level + 1);
	    generateChildGroupRows(mainTable, curriculumGroup, level + 1);
	}

	private void generateGroupRowWithText(final HtmlTable mainTable, final String text,
		boolean addHeaders, final int level) {

	    final HtmlTableRow groupRow = mainTable.createRow();
	    groupRow.setClasses(getCurriculumGroupRowClass());
	    addTabsToRow(groupRow, level);

	    if (!addHeaders) {
		generateCellWithText(groupRow, text, getLabelCellClass(), MAX_LINE_SIZE - level);
	    } else {
		generateCellWithText(groupRow, text, getLabelCellClass(),
			MAX_COL_SPAN_FOR_TEXT_ON_GROUPS_WITH_CHILDS - level);
		generateHeadersForGradeWeightAndEctsCredits(groupRow);
		final HtmlTableCell cellAfterEcts = groupRow.createCell();
		cellAfterEcts.setColspan(MAX_LINE_SIZE - MAX_COL_SPAN_FOR_TEXT_ON_GROUPS_WITH_CHILDS
			- HEADERS_SIZE);
	    }
	}

	private void generateCurriculumLineRows(HtmlTable mainTable, CurriculumGroup curriculumGroup,
		int level) {

	    if (isToShowDismissals()) {
		generateDismissalRows(mainTable, curriculumGroup.getChildDismissals(), level);
	    }

	    if (isToShowEnrolments()) {
		generateEnrolmentRows(mainTable, curriculumGroup.getChildEnrolments(), level, true);
	    }
	}

	private void generateDismissalRows(HtmlTable mainTable, List<Dismissal> dismissals, int level) {
	    final Set<Dismissal> sortedDismissals = new TreeSet<Dismissal>(
		    Dismissal.COMPARATOR_BY_NAME_AND_ID);
	    sortedDismissals.addAll(dismissals);

	    for (final Dismissal dismissal : sortedDismissals) {
		generateDismissalRow(mainTable, dismissal, level);
	    }
	}

	private void generateDismissalRow(HtmlTable mainTable, Dismissal dismissal, int level) {
	    final HtmlTableRow dismissalRow = mainTable.createRow();
	    addTabsToRow(dismissalRow, level);
	    dismissalRow
		    .setClasses(dismissal.getCredits().isTemporary() ? getTemporaryDismissalRowClass()
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
	    final String grade = !StringUtils.isEmpty(dismissal.getCredits().getGivenGrade()) ? dismissal
		    .getCredits().getGivenGrade()
		    : "-";
	    generateCellWithText(dismissalRow, grade, getGradeCellClass());

	}

	private void generateCellsBetweenLabelAndGradeCell(HtmlTableRow dismissalRow) {
	    generateCellsWithText(dismissalRow, COLUMNS_BETWEEN_TEXT_AND_GRADE, "-", new String[] {
		    getDegreeCurricularPlanCellClass(), getEnrolmentTypeCellClass(),
		    getExtraCurricularCellClass(), getEnrolmentStateCellClass() });

	}

	private void generateCellsFromEvaluationDateToEnd(HtmlTableRow dismissalRow) {
	    if (isToShowLatestEnrolmentEvaluationInfo()) {
		generateCellsWithText(dismissalRow, LATEST_ENROLMENT_EVALUATION_COLUMNS, "-",
			new String[] { getLastEnrolmentEvaluationTypeCellClass(),
				getGradeResponsibleCellClass() });
	    }

	}

	private void generateCellsBetweenEctsAndEvaluationDate(final HtmlTableRow dismissalRow) {
	    generateCellsWithText(dismissalRow, COLUMNS_BETWEEN_ECTS_AND_ENROLMENT_EVALUATION_DATE, "-",
		    new String[] { getLastEnrolmentEvaluationTypeCellClass(),
			    getEnrolmentExecutionYearCellClass(), getEnrolmentSemesterCellClass() });
	}

	private void generateCellsBetweenLabelAndEctsCell(final HtmlTableRow dismissalRow) {
	    generateCellsWithText(dismissalRow, COLUMNS_BETWEEN_TEXT_AND_ECTS, "-", new String[] {
		    getDegreeCurricularPlanCellClass(), getEnrolmentTypeCellClass(),
		    getExtraCurricularCellClass(), getEnrolmentStateCellClass(), getGradeCellClass(),
		    getWeightCellClass() });
	}

	private void generateCellsWithText(final HtmlTableRow row, final int numberOfCells,
		final String text, final String[] cssClasses) {
	    for (int i = 0; i < numberOfCells; i++) {
		generateCellWithText(row, "-", cssClasses[i]);
	    }
	}

	private void generateDismissalEctsCell(HtmlTableRow dismissalRow, Dismissal dismissal) {
	    generateCellWithText(dismissalRow, dismissal.getEctsCredits() != null ? dismissal
		    .getEctsCredits().toString() : "-", getEctsCreditsCellClass());
	}

	private void generateDismissalLabelCell(final HtmlTable mainTable, HtmlTableRow dismissalRow,
		Dismissal dismissal, int level) {
	    if (dismissal.hasCurricularCourse()) {
		final HtmlTableCell cell = dismissalRow.createCell();
		cell.setColspan(MAX_COL_SPAN_FOR_TEXT_ON_CURRICULUM_LINES - level);
		cell.setClasses(getLabelCellClass());
		final HtmlInlineContainer container = new HtmlInlineContainer();
		cell.setBody(container);
		final HtmlText text = new HtmlText(studentResources.getString("label.dismissal") + ": ");
		container.addChild(text);

		final CurricularCourse curricularCourse = dismissal.getCurricularCourse();
		String codeAndName = "";
		if (!StringUtils.isEmpty(curricularCourse.getCode())) {
		    codeAndName += curricularCourse.getCode() + " - ";
		}
		codeAndName += dismissal.getName().getContent();

		final HtmlLink curricularCourseLink = createCurricularCourseLink(codeAndName,
			curricularCourse);

		container.addChild(curricularCourseLink);

	    } else {
		generateCellWithText(dismissalRow, studentResources.getString("label.creditsDismissal"),
			getLabelCellClass(), MAX_COL_SPAN_FOR_TEXT_ON_CURRICULUM_LINES - level);
	    }

	    if (isDetailed()) {
		generateDismissalDetails(mainTable, dismissal, level);
	    }

	}

	private void generateDismissalDetails(final HtmlTable mainTable, Dismissal dismissal, int level) {
	    for (final EnrolmentWrapper enrolmentWrapper : dismissal.getCredits().getEnrolments()) {
		if (enrolmentWrapper.getIEnrolment().isExternalEnrolment()) {
		    generateExternalEnrolmentRow(mainTable, (ExternalEnrolment) enrolmentWrapper
			    .getIEnrolment(), level + 1);

		} else {
		    generateEnrolmentRow(mainTable, (Enrolment) enrolmentWrapper.getIEnrolment(),
			    level + 1, false);
		}
	    }
	}

	private void generateExternalEnrolmentRow(HtmlTable mainTable,
		ExternalEnrolment externalEnrolment, int level) {

	    final HtmlTableRow externalEnrolmentRow = mainTable.createRow();
	    externalEnrolmentRow.setClasses(getEnrolmentRowClass());
	    addTabsToRow(externalEnrolmentRow, level);

	    generateExternalEnrolmentLabelCell(externalEnrolmentRow, externalEnrolment, level);
	    generateCellsBetweenLabelAndEctsCell(externalEnrolmentRow);
	    generateExternalEnrolmentEctsCell(externalEnrolmentRow, externalEnrolment);
	    generateCellsBetweenEctsAndEvaluationDate(externalEnrolmentRow);
	    generateCellsFromEvaluationDateToEnd(externalEnrolmentRow);
	    generateSpacerCellsIfRequired(externalEnrolmentRow);

	}

	private void generateExternalEnrolmentEctsCell(HtmlTableRow externalEnrolmentRow,
		ExternalEnrolment externalEnrolment) {
	    generateCellWithText(externalEnrolmentRow, externalEnrolment.getEctsCredits().toString(),
		    getEctsCreditsCellClass());
	}

	private void generateExternalEnrolmentLabelCell(final HtmlTableRow externalEnrolmentRow,
		final ExternalEnrolment externalEnrolment, final int level) {
	    generateCellWithText(externalEnrolmentRow, externalEnrolment.getDescription(),
		    getLabelCellClass(), MAX_COL_SPAN_FOR_TEXT_ON_CURRICULUM_LINES - level);
	}

	private void generateEnrolmentRows(HtmlTable mainTable, List<Enrolment> childEnrolments,
		int level, boolean showExtraCurricularCheckBox) {
	    final Set<Enrolment> sortedEnrolments = new TreeSet<Enrolment>(
		    Enrolment.COMPARATOR_BY_NAME_AND_ID);
	    sortedEnrolments.addAll(childEnrolments);

	    for (final Enrolment enrolment : sortedEnrolments) {
		if (isToShowAllEnrolmentStates()) {
		    generateEnrolmentRow(mainTable, enrolment, level, showExtraCurricularCheckBox);
		} else if (isToShowApprovedOrEnroledStatesOnly()) {
		    if (enrolment.isApproved() || enrolment.isEnroled()) {
			generateEnrolmentRow(mainTable, enrolment, level, showExtraCurricularCheckBox);
		    }
		} else {
		    throw new RuntimeException("Unexpected enrolment state filter type");
		}
	    }
	}

	private void generateEnrolmentRow(HtmlTable mainTable, Enrolment enrolment, int level,
		boolean showExtraCurricularCheckBox) {
	    final HtmlTableRow enrolmentRow = mainTable.createRow();
	    addTabsToRow(enrolmentRow, level);
	    enrolmentRow.setClasses(getEnrolmentRowClass());

	    generateCurricularCourseCodeAndNameCell(enrolmentRow, enrolment, level);
	    generateDegreeCurricularPlanCell(enrolmentRow, enrolment);
	    generateEnrolmentTypeCell(enrolmentRow, enrolment);
	    generateEnrolmentExtraCurricularCheckBoxCell(enrolmentRow, enrolment,
		    showExtraCurricularCheckBox);
	    generateEnrolmentStateCell(enrolmentRow, enrolment);
	    generateEnrolmentGradeCell(enrolmentRow, enrolment);
	    generateEnrolmentWeightCell(enrolmentRow, enrolment);
	    generateEnrolmentEctsCell(enrolmentRow, enrolment);
	    generateEnrolmentLastEnrolmentEvaluationTypeCell(enrolmentRow, enrolment);
	    generateEnrolmentExecutionYearCell(enrolmentRow, enrolment);
	    generateEnrolmentSemesterCell(enrolmentRow, enrolment);
	    generateLastEnrolmentEvaluationExamDateCellIfRequired(enrolmentRow, enrolment);
	    generateGradeResponsibleIfRequired(enrolmentRow, enrolment);
	    generateSpacerCellsIfRequired(enrolmentRow);

	}

	private void generateEnrolmentExtraCurricularCheckBoxCell(HtmlTableRow enrolmentRow,
		Enrolment enrolment, boolean showExtraCurricularCheckBox) {
	    if (showExtraCurricularCheckBox && enrolment.isApproved()
		    && canChangeExtraCurricularStatus()) {
		final HtmlTableCell checkBoxCell = enrolmentRow.createCell();
		checkBoxCell.setClasses(getExtraCurricularCellClass());

		final HtmlCheckBox checkBox = new HtmlCheckBox(enrolment.isExtraCurricular());
		checkBox.setName("enrolmentCheckBox" + enrolment.getIdInternal());
		checkBox.setUserValue(generateEnrolmentKeyForCheckBox(enrolment));
		checkBox.setOnChange("this.form.submit();");
		this.extraCurricularEnrolmentsController.addCheckBox(checkBox);

		final HtmlInlineContainer span = new HtmlInlineContainer();
		span.addChild(checkBox);
		span.setTitle(studentResources.getString("label.extraCurricular") + "?");

		checkBoxCell.setBody(span);
	    } else {
		generateCellWithText(enrolmentRow, "-", getExtraCurricularCellClass());
	    }

	}

	private String generateEnrolmentKeyForCheckBox(final Enrolment enrolment) {
	    return MetaObjectFactory.createObject(enrolment,
		    RenderKit.getInstance().findSchema("Enrolment.edit-extra-curricular-status"))
		    .getKey().toString();
	}

	private boolean canChangeExtraCurricularStatus() {
	    return AccessControl.getPerson().isAdministrativeOfficeEmployee()
		    && !studentCurricularPlan.getRegistration().isConcluded();

	}

	private void generateGradeResponsibleIfRequired(HtmlTableRow enrolmentRow, Enrolment enrolment) {
	    if (isToShowLatestEnrolmentEvaluationInfo()) {
		final EnrolmentEvaluation lastEnrolmentEvaluation = enrolment
			.getLatestEnrolmentEvaluation();
		if (lastEnrolmentEvaluation != null
			&& lastEnrolmentEvaluation.getPersonResponsibleForGrade() != null) {

		    final Person person = lastEnrolmentEvaluation.getPersonResponsibleForGrade();
		    final String username = person.hasEmployee() ? person.getEmployee()
			    .getRoleLoginAlias() : person.getIstUsername();
		    generateCellWithSpan(enrolmentRow, username, applicationResources
			    .getString("label.grade.responsiblePerson"), getGradeResponsibleCellClass());

		} else {
		    generateCellWithText(enrolmentRow, "-", getGradeResponsibleCellClass());
		}
	    }

	}

	private void generateLastEnrolmentEvaluationExamDateCellIfRequired(HtmlTableRow enrolmentRow,
		Enrolment enrolment) {
	    if (isToShowLatestEnrolmentEvaluationInfo()) {
		final EnrolmentEvaluation lastEnrolmentEvaluation = enrolment
			.getLatestEnrolmentEvaluation();
		if (lastEnrolmentEvaluation != null
			&& lastEnrolmentEvaluation.getExamDateYearMonthDay() != null) {

		    generateCellWithSpan(enrolmentRow, lastEnrolmentEvaluation.getExamDateYearMonthDay()
			    .toString(DATE_FORMAT), applicationResources
			    .getString("label.data.avaliacao"), getExamDateCellClass());
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
	    return MAX_LINE_SIZE
		    - MAX_COL_SPAN_FOR_TEXT_ON_CURRICULUM_LINES
		    - COLUMNS_BETWEEN_TEXT_AND_ENROLMENT_EVALUATION_DATE
		    - (isToShowLatestEnrolmentEvaluationInfo() ? LATEST_ENROLMENT_EVALUATION_COLUMNS : 0);
	}

	private void generateEnrolmentSemesterCell(final HtmlTableRow enrolmentRow,
		final Enrolment enrolment) {
	    final String semester = enrolment.getExecutionPeriod().getSemester().toString() + " "
		    + applicationResources.getString("label.semester.short");

	    generateCellWithText(enrolmentRow, semester, getEnrolmentSemesterCellClass());
	}

	private void generateEnrolmentExecutionYearCell(HtmlTableRow enrolmentRow, Enrolment enrolment) {
	    generateCellWithText(enrolmentRow, enrolment.getExecutionYear().getYear(),
		    getEnrolmentExecutionYearCellClass());
	}

	private void generateEnrolmentLastEnrolmentEvaluationTypeCell(HtmlTableRow enrolmentRow,
		Enrolment enrolment) {
	    final EnrolmentEvaluation lastEnrolmentEvaluation = enrolment.getLatestEnrolmentEvaluation();
	    if (lastEnrolmentEvaluation != null
		    && lastEnrolmentEvaluation.getEnrolmentEvaluationType() != null) {
		generateCellWithSpan(enrolmentRow, enumerationResources
			.getString(lastEnrolmentEvaluation.getEnrolmentEvaluationType().getAcronym()),
			getLastEnrolmentEvaluationTypeCellClass());
	    } else {
		generateCellWithText(enrolmentRow, "-", getLastEnrolmentEvaluationTypeCellClass());
	    }

	}

	private void generateEnrolmentEctsCell(HtmlTableRow enrolmentRow, Enrolment enrolment) {
	    // Credits are only relevant to show when student is already
	    // approved
	    generateCellWithText(enrolmentRow, enrolment.isApproved() ? enrolment.getEctsCredits()
		    .toString() : "-", getEctsCreditsCellClass());

	}

	private void generateEnrolmentWeightCell(HtmlTableRow enrolmentRow, Enrolment enrolment) {
	    // Weight is only relevant to show when enrolment has numeric value
	    generateCellWithText(enrolmentRow, enrolment.getFinalGrade() != null ? enrolment.getWeigth()
		    .toString() : "-", getWeightCellClass());

	}

	private void generateEnrolmentGradeCell(HtmlTableRow enrolmentRow, Enrolment enrolment) {
	    generateCellWithText(enrolmentRow, enrolment.getGrade().isEmpty() ? "-" : enrolment
		    .getGradeValue(), getGradeCellClass());
	}

	private void generateEnrolmentStateCell(HtmlTableRow enrolmentRow, Enrolment enrolment) {
	    generateCellWithText(enrolmentRow, enrolment.isApproved() ? "-" : enumerationResources
		    .getString(enrolment.getEnrollmentState().getQualifiedName()),
		    getEnrolmentStateCellClass());

	}

	private void generateEnrolmentTypeCell(HtmlTableRow enrolmentRow, Enrolment enrolment) {
	    generateCellWithText(enrolmentRow, enrolment.isEnrolmentTypeNormal() ? "-"
		    : enumerationResources.getString(enrolment.getEnrolmentTypeName()),
		    getEnrolmentTypeCellClass());
	}

	private void generateDegreeCurricularPlanCell(final HtmlTableRow enrolmentRow,
		final Enrolment enrolment) {

	    if (enrolment.isFor(studentCurricularPlan.getDegreeCurricularPlan())) {
		generateCellWithText(enrolmentRow, "-", getDegreeCurricularPlanCellClass());
	    } else {
		final HtmlTableCell cell = enrolmentRow.createCell();
		cell.setClasses(getDegreeCurricularPlanCellClass());
		cell.setBody(createDegreeCurricularPlanNameLink(enrolment
			.getDegreeCurricularPlanOfDegreeModule(), enrolment.getExecutionPeriod()));
	    }

	}

	private HtmlComponent createDegreeCurricularPlanNameLink(
		final DegreeCurricularPlan degreeCurricularPlan, ExecutionPeriod executionPeriod) {
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

	    result.setParameter("executionPeriodOID", executionPeriod != null ? executionPeriod
		    .getIdInternal() : ExecutionPeriod.readActualExecutionPeriod().getIdInternal());

	    return result;
	}

	private void generateCurricularCourseCodeAndNameCell(final HtmlTableRow enrolmentRow,
		final Enrolment enrolment, final int level) {

	    final String code = !StringUtils.isEmpty(enrolment.getCurricularCourse().getCode()) ? enrolment
		    .getCurricularCourse().getCode()
		    + " - "
		    : "";
	    final String codeAndName = code + enrolment.getName().getContent();

	    final HtmlTableCell cell = enrolmentRow.createCell();
	    cell.setClasses(getLabelCellClass());
	    cell.setColspan(MAX_COL_SPAN_FOR_TEXT_ON_CURRICULUM_LINES - level);
	    cell.setBody(createCurricularCourseLink(codeAndName, enrolment.getCurricularCourse()));
	}

	private HtmlLink createCurricularCourseLink(final String text,
		final CurricularCourse curricularCourse) {

	    final HtmlLink result = new HtmlLink();
	    result.setBody(new HtmlText(text));
	    result.setModuleRelative(false);
	    result.setTarget(HtmlLink.Target.BLANK);

	    result.setParameter("degreeID", curricularCourse.getDegreeCurricularPlan().getDegree()
		    .getIdInternal());
	    result.setParameter("curricularCourseID", curricularCourse.getIdInternal());
	    result.setParameter("degreeCurricularPlanID", curricularCourse.getDegreeCurricularPlan()
		    .getIdInternal());

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
	generateCellWithText(groupRow, applicationResources.getString("label.grade"),
		getGradeCellClass());

	generateCellWithText(groupRow, applicationResources.getString("label.weight"),
		getWeightCellClass());

	generateCellWithText(groupRow, applicationResources.getString("label.ects"),
		getEctsCreditsCellClass());

    }

    private void generateCellWithText(final HtmlTableRow row, final String text, final String cssClass) {
	generateCellWithText(row, text, cssClass, 1);
    }

    private void generateCellWithText(final HtmlTableRow row, final String text, final String cssClass,
	    Integer colSpan) {
	final HtmlTableCell cell = row.createCell();
	cell.setClasses(cssClass);
	cell.setText(text);
	cell.setColspan(colSpan);
    }

    private void generateCellWithSpan(final HtmlTableRow row, final String text, final String cssClass) {
	generateCellWithSpan(row, text, null, cssClass);
    }

    private void generateCellWithSpan(final HtmlTableRow row, final String text, final String title,
	    final String cssClass) {
	generateCellWithSpan(row, text, title, cssClass, 1);
    }

    private void generateCellWithSpan(final HtmlTableRow row, final String text, final String title,
	    final String cssClass, final Integer colSpan) {
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
