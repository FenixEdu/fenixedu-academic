package net.sourceforge.fenixedu.presentationTier.renderers.student.enrollment.bolonha;

import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.dataTransferObject.student.enrollment.bolonha.BolonhaStudentEnrollmentBean;
import net.sourceforge.fenixedu.dataTransferObject.student.enrollment.bolonha.StudentCurriculumEnrolmentBean;
import net.sourceforge.fenixedu.dataTransferObject.student.enrollment.bolonha.StudentCurriculumGroupBean;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.OptionalEnrolment;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curricularRules.CreditsLimit;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRuleType;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.renderers.controllers.CopyCheckBoxValuesController;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyArrayConverter;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.ContentInjectionRewriter;
import pt.ist.fenixWebFramework.renderers.InputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlActionLink;
import pt.ist.fenixWebFramework.renderers.components.HtmlBlockContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlCheckBox;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlLink;
import pt.ist.fenixWebFramework.renderers.components.HtmlLinkWithPreprendedComment;
import pt.ist.fenixWebFramework.renderers.components.HtmlMultipleHiddenField;
import pt.ist.fenixWebFramework.renderers.components.HtmlTable;
import pt.ist.fenixWebFramework.renderers.components.HtmlTableCell;
import pt.ist.fenixWebFramework.renderers.components.HtmlTableRow;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;
import pt.ist.fenixWebFramework.renderers.components.controllers.HtmlActionLinkController;
import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;
import pt.ist.fenixWebFramework.renderers.model.MetaObject;
import pt.ist.fenixWebFramework.renderers.model.MetaObjectFactory;
import pt.ist.fenixWebFramework.renderers.schemas.Schema;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.utl.ist.fenix.tools.util.i18n.Language;
import pt.utl.ist.fenix.tools.util.StringAppender;

public class BolonhaStudentEnrollmentInputRenderer extends InputRenderer {

    private final ResourceBundle enumerationResources = ResourceBundle.getBundle("resources.EnumerationResources", Language
	    .getLocale());

    private final ResourceBundle studentResources = ResourceBundle.getBundle("resources.StudentResources", Language
	    .getLocale());

    private final ResourceBundle applicationResources = ResourceBundle.getBundle("resources.ApplicationResources", Language
	    .getLocale());

    private Integer initialWidth = 70;

    private Integer widthDecreasePerLevel = 3;

    private String tablesClasses = "showinfo3 mvert0";

    private String groupRowClasses = "bgcolor2";

    private String enrolmentClasses = "smalltxt, smalltxt aright, smalltxt aright, smalltxt aright, aright";

    private String temporaryEnrolmentClasses = "smalltxt, smalltxt aright, smalltxt aright, smalltxt aright, aright";

    private String impossibleEnrolmentClasses = "smalltxt, smalltxt aright, smalltxt aright, smalltxt aright, aright";

    private String curricularCoursesToEnrolClasses = "smalltxt, smalltxt aright, smalltxt aright, aright";

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

    private String[] getEnrolmentClasses() {
	return enrolmentClasses.split(",");
    }

    public void setEnrolmentClasses(String enrolmentClasses) {
	this.enrolmentClasses = enrolmentClasses;
    }

    private String getEnrolmentNameClasses() {
	return getEnrolmentClasses()[0];
    }

    private String getEnrolmentYearClasses() {
	return getEnrolmentClasses()[1];
    }

    private String getEnrolmentSemesterClasses() {
	return getEnrolmentClasses()[2];
    }

    private String getEnrolmentEctsClasses() {
	return getEnrolmentClasses()[3];
    }

    private String getEnrolmentCheckBoxClasses() {
	return getEnrolmentClasses()[4];
    }

    private String[] getCurricularCourseToEnrolClasses() {
	return curricularCoursesToEnrolClasses.split(",");
    }

    public void setCurricularCourseToEnrolClasses(String curricularCoursesToEnrol) {
	this.curricularCoursesToEnrolClasses = curricularCoursesToEnrol;
    }

    private String getCurricularCourseToEnrolNameClasses() {
	return getCurricularCourseToEnrolClasses()[0];
    }

    private String getCurricularCourseToEnrolYearClasses() {
	return getCurricularCourseToEnrolClasses()[1];
    }

    private String getCurricularCourseToEnrolEctsClasses() {
	return getCurricularCourseToEnrolClasses()[2];
    }

    private String getCurricularCourseToEnrolCheckBoxClasses() {
	return getCurricularCourseToEnrolClasses()[3];
    }

    private String[] getTemporaryEnrolmentClasses() {
	return temporaryEnrolmentClasses.split(",");
    }

    public void setTemporaryEnrolmentClasses(String enrolmentClasses) {
	this.temporaryEnrolmentClasses = enrolmentClasses;
    }

    private String getTemporaryEnrolmentNameClasses() {
	return getTemporaryEnrolmentClasses()[0];
    }

    private String getTemporaryEnrolmentYearClasses() {
	return getTemporaryEnrolmentClasses()[1];
    }

    private String getTemporaryEnrolmentSemesterClasses() {
	return getTemporaryEnrolmentClasses()[2];
    }

    private String getTemporaryEnrolmentEctsClasses() {
	return getTemporaryEnrolmentClasses()[3];
    }

    private String getTemporaryEnrolmentCheckBoxClasses() {
	return getTemporaryEnrolmentClasses()[4];
    }

    private String[] getImpossibleEnrolmentClasses() {
	return impossibleEnrolmentClasses.split(",");
    }

    public void setImpossibleEnrolmentClasses(String enrolmentClasses) {
	this.impossibleEnrolmentClasses = enrolmentClasses;
    }

    private String getImpossibleEnrolmentNameClasses() {
	return getImpossibleEnrolmentClasses()[0];
    }

    private String getImpossibleEnrolmentYearClasses() {
	return getImpossibleEnrolmentClasses()[1];
    }

    private String getImpossibleEnrolmentSemesterClasses() {
	return getImpossibleEnrolmentClasses()[2];
    }

    private String getImpossibleEnrolmentEctsClasses() {
	return getImpossibleEnrolmentClasses()[3];
    }

    private String getImpossibleEnrolmentCheckBoxClasses() {
	return getImpossibleEnrolmentClasses()[4];
    }

    public BolonhaStudentEnrollmentInputRenderer() {
	super();
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
	return new BolonhaStudentEnrolmentLayout();
    }

    private class BolonhaStudentEnrolmentLayout extends Layout {

	private final CopyCheckBoxValuesController enrollmentsController = new CopyCheckBoxValuesController(false);

	private final CopyCheckBoxValuesController degreeModulesToEvaluateController = new CopyCheckBoxValuesController();

	private BolonhaStudentEnrollmentBean bolonhaStudentEnrollmentBean = null;

	@Override
	public HtmlComponent createComponent(Object object, Class type) {
	    bolonhaStudentEnrollmentBean = (BolonhaStudentEnrollmentBean) object;

	    if (bolonhaStudentEnrollmentBean == null) {
		return new HtmlText();
	    }

	    final HtmlBlockContainer container = new HtmlBlockContainer();

	    HtmlMultipleHiddenField hiddenEnrollments = new HtmlMultipleHiddenField();
	    hiddenEnrollments.bind(getInputContext().getMetaObject(), "curriculumModulesToRemove");
	    hiddenEnrollments.setConverter(new DomainObjectKeyArrayConverter());
	    hiddenEnrollments.setController(enrollmentsController);

	    HtmlMultipleHiddenField hiddenDegreeModulesToEvaluate = new HtmlMultipleHiddenField();
	    hiddenDegreeModulesToEvaluate.bind(getInputContext().getMetaObject(), "degreeModulesToEvaluate");
	    hiddenDegreeModulesToEvaluate.setConverter(bolonhaStudentEnrollmentBean.getDegreeModulesToEvaluateConverter());
	    hiddenDegreeModulesToEvaluate.setController(degreeModulesToEvaluateController);

	    container.addChild(hiddenEnrollments);
	    container.addChild(hiddenDegreeModulesToEvaluate);

	    generateGroup(container, bolonhaStudentEnrollmentBean.getStudentCurricularPlan(), bolonhaStudentEnrollmentBean
		    .getRootStudentCurriculumGroupBean(), bolonhaStudentEnrollmentBean.getExecutionPeriod(), 0);

	    return container;
	}

	private void generateGroup(final HtmlBlockContainer blockContainer, final StudentCurricularPlan studentCurricularPlan,
		final StudentCurriculumGroupBean studentCurriculumGroupBean, final ExecutionSemester executionSemester,
		final int depth) {

	    final HtmlTable groupTable = createGroupTable(blockContainer, depth);
	    addGroupHeaderRow(groupTable, studentCurriculumGroupBean, executionSemester);

	    // TODO: Uncomment when isConcluded is implemented using credits
	    // if (!hasManagerOrAcademicOfficeRole() &&
	    // !studentCurriculumGroupBean.isRoot()
	    // &&
	    // studentCurriculumGroupBean.getCurriculumModule().isConcluded())
                // {
	    // return;
	    // }

	    final HtmlTable coursesTable = createCoursesTable(blockContainer, depth);
	    generateEnrolments(studentCurriculumGroupBean, coursesTable);
	    generateCurricularCoursesToEnrol(coursesTable, studentCurriculumGroupBean);

	    generateGroups(blockContainer, studentCurriculumGroupBean, studentCurricularPlan, executionSemester, depth);

	    if (studentCurriculumGroupBean.isRoot()) {
		generateCycleCourseGroupsToEnrol(blockContainer, studentCurricularPlan, depth);
	    }
	}

	private HtmlTable createGroupTable(final HtmlBlockContainer blockContainer, final int depth) {
	    final HtmlTable groupTable = new HtmlTable();
	    groupTable.setClasses(getTablesClasses());
	    groupTable.setStyle("width: " + (getInitialWidth() - depth) + "em; margin-left: " + depth + "em;");

	    blockContainer.addChild(groupTable);

	    return groupTable;
	}

	private void addGroupHeaderRow(final HtmlTable groupTable, final StudentCurriculumGroupBean studentCurriculumGroupBean,
		final ExecutionSemester executionSemester) {
	    final HtmlTableRow groupHeaderRow = groupTable.createRow();
	    groupHeaderRow.setClasses(getGroupRowClasses());

	    final HtmlTableCell titleCell = groupHeaderRow.createCell();
	    if (studentCurriculumGroupBean.getCurriculumModule().isRoot()) {
		titleCell.setBody(createDegreeCurricularPlanLink(studentCurriculumGroupBean));
	    } else {
		titleCell.setBody(new HtmlText(buildCurriculumGroupLabel(studentCurriculumGroupBean.getCurriculumModule(),
			executionSemester), false));
	    }

	    final HtmlTableCell checkBoxCell = groupHeaderRow.createCell();
	    checkBoxCell.setClasses("aright");

	    final HtmlCheckBox checkBox = new HtmlCheckBox(true) {
		@Override
		public void setChecked(boolean checked) {
		    if (isDisabled()) {
			super.setChecked(true);
		    } else {
			super.setChecked(checked);
		    }
		}
	    };

	    MetaObject enrolmentMetaObject = MetaObjectFactory.createObject(studentCurriculumGroupBean.getCurriculumModule(),
		    new Schema(CurriculumGroup.class));
	    checkBox.setName("enrolmentCheckBox" + studentCurriculumGroupBean.getCurriculumModule().getIdInternal());
	    checkBox.setUserValue(enrolmentMetaObject.getKey().toString());
	    checkBoxCell.setBody(checkBox);

	    if (studentCurriculumGroupBean.isToBeDisabled()) {
		checkBox.setDisabled(true);
	    } else {
		enrollmentsController.addCheckBox(checkBox);
	    }

	}

	private String buildCurriculumGroupLabel(final CurriculumGroup curriculumGroup, final ExecutionSemester executionSemester) {
	    if (curriculumGroup.isNoCourseGroupCurriculumGroup()) {
		return curriculumGroup.getName().getContent();
	    }

	    final StringBuilder result = new StringBuilder(curriculumGroup.getName().getContent());

	    final CreditsLimit creditsLimit = (CreditsLimit) curriculumGroup.getMostRecentActiveCurricularRule(
		    CurricularRuleType.CREDITS_LIMIT, executionSemester);

	    if (creditsLimit != null) {
		result.append(" <span title=\"");
		result.append(applicationResources.getString("label.curriculum.credits.legend.minCredits"));
		result.append(" \">m(");
		result.append(creditsLimit.getMinimumCredits());
		result.append(")</span>,");
	    }

	    result.append(" <span title=\"");
	    result.append(applicationResources.getString("label.curriculum.credits.legend.maxCredits"));
	    result.append(" \"> c(");
	    result.append(curriculumGroup.getCreditsConcluded(executionSemester.getExecutionYear()));
	    result.append(")</span>");

	    if (creditsLimit != null) {
		result.append(", <span title=\"");
		result.append(applicationResources.getString("label.curriculum.credits.legend.creditsConcluded"));
		result.append(" \">M(");
		result.append(creditsLimit.getMaximumCredits());
		result.append(")</span>");
	    }

	    // if (curriculumGroup.isConcluded()) {
	    // result.append(" -
	    // ").append(studentResources.getString("label.curriculum.group.concluded"));
	    // }

	    return result.toString();
	}

	private HtmlLink createDegreeCurricularPlanLink(final StudentCurriculumGroupBean studentCurriculumGroupBean) {
	    final HtmlLink degreeCurricularPlanLink = new HtmlLinkWithPreprendedComment(
		    ContentInjectionRewriter.HAS_CONTEXT_PREFIX);
	    degreeCurricularPlanLink.setText(studentCurriculumGroupBean.getCurriculumModule().getName().getContent());
	    degreeCurricularPlanLink.setModuleRelative(false);
	    degreeCurricularPlanLink.setTarget("_blank");
	    degreeCurricularPlanLink.setUrl("/publico/degreeSite/showDegreeCurricularPlanBolonha.faces");

	    final StudentCurricularPlan studentCurricularPlan = bolonhaStudentEnrollmentBean.getStudentCurricularPlan();
	    degreeCurricularPlanLink.setParameter(ContentInjectionRewriter.CONTEXT_ATTRIBUTE_NAME, studentCurricularPlan
		    .getDegree().getSite().getReversePath());
	    degreeCurricularPlanLink.setParameter("organizeBy", "groups");
	    degreeCurricularPlanLink.setParameter("showRules", "false");
	    degreeCurricularPlanLink.setParameter("hideCourses", "false");
	    degreeCurricularPlanLink.setParameter("degreeID", studentCurricularPlan.getDegree().getIdInternal());
	    degreeCurricularPlanLink.setParameter("degreeCurricularPlanID", studentCurricularPlan.getDegreeCurricularPlan()
		    .getIdInternal());
	    degreeCurricularPlanLink.setParameter("executionPeriodOID", bolonhaStudentEnrollmentBean.getExecutionPeriod()
		    .getIdInternal());
	    return degreeCurricularPlanLink;
	}

	private HtmlTable createCoursesTable(final HtmlBlockContainer blockContainer, final int depth) {
	    final HtmlTable coursesTable = new HtmlTable();
	    blockContainer.addChild(coursesTable);
	    coursesTable.setClasses(getTablesClasses());
	    coursesTable.setStyle("width: " + (getInitialWidth() - depth - getWidthDecreasePerLevel()) + "em; margin-left: "
		    + (depth + getWidthDecreasePerLevel()) + "em;");
	    return coursesTable;
	}

	private void generateCurricularCoursesToEnrol(HtmlTable groupTable, StudentCurriculumGroupBean studentCurriculumGroupBean) {
	    final List<IDegreeModuleToEvaluate> coursesToEvaluate = studentCurriculumGroupBean.getSortedDegreeModulesToEvaluate();

	    for (final IDegreeModuleToEvaluate degreeModuleToEvaluate : coursesToEvaluate) {
		HtmlTableRow htmlTableRow = groupTable.createRow();
		HtmlTableCell cellName = htmlTableRow.createCell();
		cellName.setClasses(getCurricularCourseToEnrolNameClasses());
		cellName.setBody(new HtmlText(degreeModuleToEvaluate.getName()));

		// Year
		final HtmlTableCell yearCell = htmlTableRow.createCell();
		yearCell.setClasses(getCurricularCourseToEnrolYearClasses());
		yearCell.setColspan(2);
		yearCell.setBody(new HtmlText(degreeModuleToEvaluate.getYearFullLabel()));

		if (!degreeModuleToEvaluate.isOptionalCurricularCourse()) {
		    // Ects
		    final HtmlTableCell ectsCell = htmlTableRow.createCell();
		    ectsCell.setClasses(getCurricularCourseToEnrolEctsClasses());

		    final StringBuilder ects = new StringBuilder();
		    ects.append(degreeModuleToEvaluate.getEctsCredits()).append(" ").append(
			    studentResources.getString("label.credits.abbreviation"));
		    ectsCell.setBody(new HtmlText(ects.toString()));

		    HtmlTableCell checkBoxCell = htmlTableRow.createCell();
		    checkBoxCell.setClasses(getCurricularCourseToEnrolCheckBoxClasses());

		    HtmlCheckBox checkBox = new HtmlCheckBox(false);
		    checkBox.setName("degreeModuleToEnrolCheckBox" + degreeModuleToEvaluate.getKey());
		    checkBox.setUserValue(degreeModuleToEvaluate.getKey());
		    degreeModulesToEvaluateController.addCheckBox(checkBox);
		    checkBoxCell.setBody(checkBox);
		} else {
		    final HtmlTableCell cell = htmlTableRow.createCell();
		    cell.setClasses(getCurricularCourseToEnrolEctsClasses());
		    cell.setBody(new HtmlText(""));

		    HtmlTableCell linkTableCell = htmlTableRow.createCell();
		    linkTableCell.setClasses(getCurricularCourseToEnrolCheckBoxClasses());

		    final HtmlActionLink actionLink = new HtmlActionLink();
		    actionLink.setText(studentResources.getString("label.chooseOptionalCurricularCourse"));
		    actionLink.setController(new OptionalCurricularCourseLinkController(degreeModuleToEvaluate));
		    actionLink.setOnClick("document.forms[0].method.value='prepareChooseOptionalCurricularCourseToEnrol';");
		    actionLink.setName("optionalCurricularCourseLink"
			    + degreeModuleToEvaluate.getCurriculumGroup().getIdInternal() + "_"
			    + degreeModuleToEvaluate.getContext().getIdInternal());
		    linkTableCell.setBody(actionLink);
		}
	    }
	}

	private void generateEnrolments(final StudentCurriculumGroupBean studentCurriculumGroupBean, final HtmlTable groupTable) {
	    for (final StudentCurriculumEnrolmentBean studentEnrolmentBean : studentCurriculumGroupBean
		    .getEnrolledCurriculumCourses()) {
		if (studentEnrolmentBean.getCurriculumModule().isTemporary()) {
		    generateEnrolment(groupTable, studentEnrolmentBean.getCurriculumModule(), getTemporaryEnrolmentNameClasses(),
			    getTemporaryEnrolmentYearClasses(), getTemporaryEnrolmentSemesterClasses(),
			    getTemporaryEnrolmentEctsClasses(), getTemporaryEnrolmentCheckBoxClasses());
		} else if (studentEnrolmentBean.getCurriculumModule().isImpossible()) {
		    generateEnrolment(groupTable, studentEnrolmentBean.getCurriculumModule(),
			    getImpossibleEnrolmentNameClasses(), getImpossibleEnrolmentYearClasses(),
			    getImpossibleEnrolmentSemesterClasses(), getImpossibleEnrolmentEctsClasses(),
			    getImpossibleEnrolmentCheckBoxClasses());
		} else {
		    generateEnrolment(groupTable, studentEnrolmentBean.getCurriculumModule(), getEnrolmentNameClasses(),
			    getEnrolmentYearClasses(), getEnrolmentSemesterClasses(), getEnrolmentEctsClasses(),
			    getEnrolmentCheckBoxClasses());
		}
	    }
	}

	private void generateEnrolment(final HtmlTable groupTable, Enrolment enrolment, final String enrolmentNameClasses,
		final String enrolmentYearClasses, final String enrolmentSemesterClasses, final String enrolmentEctsClasses,
		final String enrolmentCheckBoxClasses) {
	    HtmlTableRow htmlTableRow = groupTable.createRow();
	    HtmlTableCell cellName = htmlTableRow.createCell();
	    cellName.setClasses(enrolmentNameClasses);
	    cellName.setBody(new HtmlText(getPresentationNameFor(enrolment)));

	    // Year
	    final HtmlTableCell yearCell = htmlTableRow.createCell();
	    yearCell.setClasses(enrolmentYearClasses);

	    final String year = enrolment.getExecutionPeriod().getExecutionYear().getYear();
	    yearCell.setBody(new HtmlText(year));

	    // Semester
	    final HtmlTableCell semesterCell = htmlTableRow.createCell();
	    semesterCell.setClasses(enrolmentSemesterClasses);

	    final StringBuilder semester = new StringBuilder();
	    semester.append(enrolment.getExecutionPeriod().getSemester().toString());
	    semester.append(" ");
	    semester.append(enumerationResources.getString("SEMESTER.ABBREVIATION"));
	    semesterCell.setBody(new HtmlText(semester.toString()));

	    // Ects
	    final HtmlTableCell ectsCell = htmlTableRow.createCell();
	    ectsCell.setClasses(enrolmentEctsClasses);

	    final StringBuilder ects = new StringBuilder();
	    final double ectsCredits = enrolment.isBolonhaDegree() ? enrolment.getAccumulatedEctsCredits(enrolment
		    .getExecutionPeriod()) : enrolment.getEctsCredits();
	    ects.append(ectsCredits).append(" ").append(studentResources.getString("label.credits.abbreviation"));

	    ectsCell.setBody(new HtmlText(ects.toString()));

	    MetaObject enrolmentMetaObject = MetaObjectFactory.createObject(enrolment, new Schema(Enrolment.class));

	    HtmlCheckBox checkBox = new HtmlCheckBox(true);
	    checkBox.setName("enrolmentCheckBox" + enrolment.getIdInternal());
	    checkBox.setUserValue(enrolmentMetaObject.getKey().toString());
	    enrollmentsController.addCheckBox(checkBox);

	    HtmlTableCell cellCheckBox = htmlTableRow.createCell();
	    cellCheckBox.setClasses(enrolmentCheckBoxClasses);
	    cellCheckBox.setBody(checkBox);
	}

	private String getPresentationNameFor(final Enrolment enrolment) {
	    if (enrolment instanceof OptionalEnrolment) {
		final OptionalEnrolment optionalEnrolment = (OptionalEnrolment) enrolment;

		return optionalEnrolment.getOptionalCurricularCourse().getName() + " ("
			+ optionalEnrolment.getCurricularCourse().getName() + ")";
	    } else {
		return enrolment.getName().getContent(Language.getLanguage());
	    }
	}

	private void generateGroups(HtmlBlockContainer blockContainer, StudentCurriculumGroupBean studentCurriculumGroupBean,
		StudentCurricularPlan studentCurricularPlan, ExecutionSemester executionSemester, int depth) {
	    final List<IDegreeModuleToEvaluate> courseGroupsToEnrol = studentCurriculumGroupBean
		    .getCourseGroupsToEnrolSortedByContext();
	    final List<StudentCurriculumGroupBean> curriculumGroups = studentCurriculumGroupBean
		    .getEnrolledCurriculumGroupsSortedByOrder(executionSemester);

	    while (!courseGroupsToEnrol.isEmpty() || !curriculumGroups.isEmpty()) {

		if (!curriculumGroups.isEmpty() && courseGroupsToEnrol.isEmpty()) {
		    generateGroup(blockContainer, studentCurricularPlan, curriculumGroups.get(0), executionSemester, depth
			    + getWidthDecreasePerLevel());
		    curriculumGroups.remove(0);
		} else if (curriculumGroups.isEmpty() && !courseGroupsToEnrol.isEmpty()) {
		    generateCourseGroupToEnroll(blockContainer, courseGroupsToEnrol.get(0), studentCurricularPlan, depth
			    + getWidthDecreasePerLevel());
		    courseGroupsToEnrol.remove(0);
		} else {
		    Context context = courseGroupsToEnrol.get(0).getContext();
		    CurriculumGroup curriculumGroup = (CurriculumGroup) curriculumGroups.get(0).getCurriculumModule();
		    if (curriculumGroup.getChildOrder(executionSemester) <= context.getChildOrder()) {
			generateGroup(blockContainer, studentCurricularPlan, curriculumGroups.get(0), executionSemester, depth
				+ getWidthDecreasePerLevel());
			curriculumGroups.remove(0);
		    } else {
			generateCourseGroupToEnroll(blockContainer, courseGroupsToEnrol.get(0), studentCurricularPlan, depth
				+ getWidthDecreasePerLevel());
			courseGroupsToEnrol.remove(0);
		    }
		}
	    }
	}

	private void generateCourseGroupToEnroll(HtmlBlockContainer blockContainer, IDegreeModuleToEvaluate degreeModuleToEnrol,
		StudentCurricularPlan studentCurricularPlan, int depth) {
	    final CourseGroup courseGroup = (CourseGroup) degreeModuleToEnrol.getContext().getChildDegreeModule();
	    if (courseGroup.isCycleCourseGroup()) {
		return;
	    }

	    final HtmlTable groupTable = createGroupTable(blockContainer, depth);
	    HtmlTableRow htmlTableRow = groupTable.createRow();
	    htmlTableRow.setClasses(getGroupRowClasses());
	    htmlTableRow.createCell().setBody(new HtmlText(degreeModuleToEnrol.getContext().getChildDegreeModule().getName()));
	    HtmlTableCell cell = htmlTableRow.createCell();
	    cell.setClasses("aright");

	    HtmlCheckBox checkBox = new HtmlCheckBox(false);
	    final String name = StringAppender.append("degreeModuleToEnrolCheckBox", degreeModuleToEnrol.getContext()
		    .getIdInternal().toString(), ":", degreeModuleToEnrol.getCurriculumGroup().getIdInternal().toString());
	    checkBox.setName(name);
	    checkBox.setUserValue(degreeModuleToEnrol.getKey());
	    degreeModulesToEvaluateController.addCheckBox(checkBox);
	    cell.setBody(checkBox);

	}

	private void generateCycleCourseGroupsToEnrol(final HtmlBlockContainer container,
		final StudentCurricularPlan studentCurricularPlan, int depth) {

	    if (studentCurricularPlan.hasConcludedAnyInternalCycle() && !hasManagerOrAcademicOfficeRole()) {
		return;
	    }

	    for (final CycleType cycleType : studentCurricularPlan.getSupportedCycleTypesToEnrol()) {
		generateCycleCourseGroupToEnrol(container, cycleType, depth + getWidthDecreasePerLevel());
	    }
	}

	private boolean hasManagerOrAcademicOfficeRole() {
	    return AccessControl.getPerson().hasRole(RoleType.MANAGER)
		    || AccessControl.getPerson().hasRole(RoleType.ACADEMIC_ADMINISTRATIVE_OFFICE);
	}

	private void generateCycleCourseGroupToEnrol(HtmlBlockContainer container, CycleType cycleType, int depth) {

	    final HtmlTable groupTable = createGroupTable(container, depth);
	    HtmlTableRow htmlTableRow = groupTable.createRow();
	    htmlTableRow.setClasses(getGroupRowClasses());
	    htmlTableRow.createCell().setBody(new HtmlText(RenderUtils.getEnumString(cycleType)));
	    HtmlTableCell cell = htmlTableRow.createCell();
	    cell.setClasses("aright");

	    final HtmlActionLink actionLink = new HtmlActionLink();
	    actionLink.setText(studentResources.getString("label.choose"));
	    actionLink.setController(new CycleSelectionLinkController(cycleType));
	    actionLink.setOnClick("document.forms[0].method.value='prepareChooseCycleCourseGroupToEnrol';");
	    actionLink.setName("cycleLink_" + cycleType.name());
	    cell.setBody(actionLink);

	}
    }

    private static class OptionalCurricularCourseLinkController extends HtmlActionLinkController {

	/**
         * 
         */
	private static final long serialVersionUID = 2760270166511466030L;

	private IDegreeModuleToEvaluate degreeModuleToEnrol;

	public OptionalCurricularCourseLinkController(final IDegreeModuleToEvaluate degreeModuleToEnrol) {
	    this.degreeModuleToEnrol = degreeModuleToEnrol;
	}

	@Override
	protected boolean isToSkipUpdate() {
	    return false;
	}

	@Override
	public void linkPressed(IViewState viewState, HtmlActionLink link) {
	    ((BolonhaStudentEnrollmentBean) viewState.getMetaObject().getObject())
		    .setOptionalDegreeModuleToEnrol(this.degreeModuleToEnrol);

	}
    }

    private static class CycleSelectionLinkController extends HtmlActionLinkController {
	/**
         * 
         */
	private static final long serialVersionUID = -5469571160954095720L;

	private CycleType cycleTypeToEnrol;

	public CycleSelectionLinkController(final CycleType cycleTypeToEnrol) {
	    this.cycleTypeToEnrol = cycleTypeToEnrol;
	}

	@Override
	protected boolean isToSkipUpdate() {
	    return false;
	}

	@Override
	public void linkPressed(IViewState viewState, HtmlActionLink link) {
	    final BolonhaStudentEnrollmentBean studentEnrollmentBean = (BolonhaStudentEnrollmentBean) viewState.getMetaObject()
		    .getObject();
	    studentEnrollmentBean.setCycleTypeToEnrol(this.cycleTypeToEnrol);

	}
    }

}
