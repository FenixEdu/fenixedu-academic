package net.sourceforge.fenixedu.presentationTier.renderers.student.enrollment.bolonha;

import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.dataTransferObject.student.enrollment.bolonha.BolonhaStudentEnrollmentBean;
import net.sourceforge.fenixedu.dataTransferObject.student.enrollment.bolonha.StudentCurriculumEnrolmentBean;
import net.sourceforge.fenixedu.dataTransferObject.student.enrollment.bolonha.StudentCurriculumGroupBean;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.OptionalEnrolment;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;
import net.sourceforge.fenixedu.presentationTier.renderers.controllers.CopyCheckBoxValuesController;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyArrayConverter;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.ContentInjectionRewriter;
import net.sourceforge.fenixedu.renderers.InputRenderer;
import net.sourceforge.fenixedu.renderers.components.HtmlActionLink;
import net.sourceforge.fenixedu.renderers.components.HtmlBlockContainer;
import net.sourceforge.fenixedu.renderers.components.HtmlCheckBox;
import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlLink;
import net.sourceforge.fenixedu.renderers.components.HtmlLinkWithPreprendedComment;
import net.sourceforge.fenixedu.renderers.components.HtmlMultipleHiddenField;
import net.sourceforge.fenixedu.renderers.components.HtmlTable;
import net.sourceforge.fenixedu.renderers.components.HtmlTableCell;
import net.sourceforge.fenixedu.renderers.components.HtmlTableRow;
import net.sourceforge.fenixedu.renderers.components.HtmlText;
import net.sourceforge.fenixedu.renderers.components.controllers.HtmlActionLinkController;
import net.sourceforge.fenixedu.renderers.components.state.IViewState;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.model.MetaObject;
import net.sourceforge.fenixedu.renderers.model.MetaObjectFactory;
import net.sourceforge.fenixedu.renderers.schemas.Schema;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;
import net.sourceforge.fenixedu.util.LanguageUtils;
import pt.utl.ist.fenix.tools.util.StringAppender;

public class BolonhaStudentEnrollmentInputRenderer extends InputRenderer {

    private static final ResourceBundle enumerationResources = ResourceBundle.getBundle("resources.EnumerationResources");

    private static final ResourceBundle studentResources = ResourceBundle.getBundle("resources.StudentResources");

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

    public void setInitialWidth(final Integer initialWidth) {
	this.initialWidth = initialWidth;
    }

    public Integer getWidthDecreasePerLevel() {
	return widthDecreasePerLevel;
    }

    public void setWidthDecreasePerLevel(final Integer widthDecreasePerLevel) {
	this.widthDecreasePerLevel = widthDecreasePerLevel;
    }

    public String getTablesClasses() {
	return tablesClasses;
    }

    public void setTablesClasses(final String tablesClasses) {
	this.tablesClasses = tablesClasses;
    }

    public String getGroupRowClasses() {
	return groupRowClasses;
    }

    public void setGroupRowClasses(final String groupRowClasses) {
	this.groupRowClasses = groupRowClasses;
    }

    private String[] getEnrolmentClasses() {
	return enrolmentClasses.split(",");
    }

    public void setEnrolmentClasses(final String enrolmentClasses) {
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

    public void setCurricularCourseToEnrolClasses(final String curricularCoursesToEnrol) {
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

    public void setTemporaryEnrolmentClasses(final String enrolmentClasses) {
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

    public void setImpossibleEnrolmentClasses(final String enrolmentClasses) {
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
    protected Layout getLayout(final Object object, final Class type) {
	return new BolonhaStudentEnrolmentLayout();
    }

    private class BolonhaStudentEnrolmentLayout extends Layout {

	private final CopyCheckBoxValuesController enrollmentsController = new CopyCheckBoxValuesController(false);

	private final CopyCheckBoxValuesController degreeModulesToEvaluateController = new CopyCheckBoxValuesController();

	private BolonhaStudentEnrollmentBean bolonhaStudentEnrollmentBean = null;

	@Override
	public HtmlComponent createComponent(final Object object, final Class type) {
	    bolonhaStudentEnrollmentBean = (BolonhaStudentEnrollmentBean) object;

	    if (bolonhaStudentEnrollmentBean == null) {
		return new HtmlText();
	    }

	    final HtmlBlockContainer container = new HtmlBlockContainer();

	    final HtmlMultipleHiddenField hiddenEnrollments = new HtmlMultipleHiddenField();
	    hiddenEnrollments.bind(getInputContext().getMetaObject(), "curriculumModulesToRemove");
	    hiddenEnrollments.setConverter(new DomainObjectKeyArrayConverter());
	    hiddenEnrollments.setController(enrollmentsController);

	    final HtmlMultipleHiddenField hiddenDegreeModulesToEvaluate = new HtmlMultipleHiddenField();
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
		final StudentCurriculumGroupBean studentCurriculumGroupBean, final ExecutionPeriod executionPeriod,
		final int depth) {

	    final HtmlTable groupTable = createGroupTable(blockContainer, depth);
	    addGroupHeaderRow(groupTable, studentCurriculumGroupBean);

	    final HtmlTable coursesTable = createCoursesTable(blockContainer, depth);
	    generateEnrolments(studentCurriculumGroupBean, coursesTable);
	    generateCurricularCoursesToEnrol(coursesTable, studentCurriculumGroupBean);

	    generateGroups(blockContainer, studentCurriculumGroupBean, studentCurricularPlan, executionPeriod, depth);

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

	private void addGroupHeaderRow(final HtmlTable groupTable, final StudentCurriculumGroupBean studentCurriculumGroupBean) {
	    final HtmlTableRow groupHeaderRow = groupTable.createRow();
	    groupHeaderRow.setClasses(getGroupRowClasses());

	    final HtmlTableCell titleCell = groupHeaderRow.createCell();
	    if (studentCurriculumGroupBean.getCurriculumModule().isRoot()) {
		titleCell.setBody(createDegreeCurricularPlanLink(studentCurriculumGroupBean));
	    } else {
		titleCell.setBody(new HtmlText(studentCurriculumGroupBean.getCurriculumModule().getName().getContent()));
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

	    final MetaObject enrolmentMetaObject = MetaObjectFactory.createObject(studentCurriculumGroupBean
		    .getCurriculumModule(), new Schema(CurriculumGroup.class));
	    checkBox.setName("enrolmentCheckBox" + studentCurriculumGroupBean.getCurriculumModule().getIdInternal());
	    checkBox.setUserValue(enrolmentMetaObject.getKey().toString());
	    checkBoxCell.setBody(checkBox);

	    if (studentCurriculumGroupBean.isToBeDisabled()) {
		checkBox.setDisabled(true);
	    } else {
		enrollmentsController.addCheckBox(checkBox);
	    }

	}

	private HtmlLink createDegreeCurricularPlanLink(final StudentCurriculumGroupBean studentCurriculumGroupBean) {
	    final HtmlLink degreeCurricularPlanLink = new HtmlLinkWithPreprendedComment(
		    ContentInjectionRewriter.HAS_CONTEXT_PREFIX);
	    degreeCurricularPlanLink.setText(studentCurriculumGroupBean.getCurriculumModule().getName().getContent());
	    degreeCurricularPlanLink.setModuleRelative(false);
	    degreeCurricularPlanLink.setTarget("_blank");

	    final StudentCurricularPlan studentCurricularPlan = bolonhaStudentEnrollmentBean.getStudentCurricularPlan();
	    degreeCurricularPlanLink.setParameter(ContentInjectionRewriter.CONTEXT_ATTRIBUTE_NAME,
		    studentCurricularPlan.getDegree().getSite().getReversePath());

	    degreeCurricularPlanLink.setUrl("/publico/degreeSite/showDegreeCurricularPlanBolonha.faces");

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

	private void generateCurricularCoursesToEnrol(final HtmlTable groupTable,
		final StudentCurriculumGroupBean studentCurriculumGroupBean) {
	    final List<IDegreeModuleToEvaluate> coursesToEvaluate = studentCurriculumGroupBean.getSortedDegreeModulesToEvaluate();

	    for (final IDegreeModuleToEvaluate degreeModuleToEvaluate : coursesToEvaluate) {
		final HtmlTableRow htmlTableRow = groupTable.createRow();
		final HtmlTableCell cellName = htmlTableRow.createCell();
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

		    final HtmlTableCell checkBoxCell = htmlTableRow.createCell();
		    checkBoxCell.setClasses(getCurricularCourseToEnrolCheckBoxClasses());

		    final HtmlCheckBox checkBox = new HtmlCheckBox(false);
		    checkBox.setName("degreeModuleToEnrolCheckBox" + degreeModuleToEvaluate.getKey());
		    checkBox.setUserValue(degreeModuleToEvaluate.getKey());
		    degreeModulesToEvaluateController.addCheckBox(checkBox);
		    checkBoxCell.setBody(checkBox);
		} else {
		    final HtmlTableCell cell = htmlTableRow.createCell();
		    cell.setClasses(getCurricularCourseToEnrolEctsClasses());
		    cell.setBody(new HtmlText(""));

		    final HtmlTableCell linkTableCell = htmlTableRow.createCell();
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

	private void generateEnrolment(final HtmlTable groupTable, final Enrolment enrolment, final String enrolmentNameClasses,
		final String enrolmentYearClasses, final String enrolmentSemesterClasses, final String enrolmentEctsClasses,
		final String enrolmentCheckBoxClasses) {
	    final HtmlTableRow htmlTableRow = groupTable.createRow();
	    final HtmlTableCell cellName = htmlTableRow.createCell();
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
	    ects.append(enrolment.getCurricularCourse().getEctsCredits()).append(" ").append(
		    studentResources.getString("label.credits.abbreviation"));
	    ectsCell.setBody(new HtmlText(ects.toString()));

	    final MetaObject enrolmentMetaObject = MetaObjectFactory.createObject(enrolment, new Schema(Enrolment.class));

	    final HtmlCheckBox checkBox = new HtmlCheckBox(true);
	    checkBox.setName("enrolmentCheckBox" + enrolment.getIdInternal());
	    checkBox.setUserValue(enrolmentMetaObject.getKey().toString());
	    enrollmentsController.addCheckBox(checkBox);

	    final HtmlTableCell cellCheckBox = htmlTableRow.createCell();
	    cellCheckBox.setClasses(enrolmentCheckBoxClasses);
	    cellCheckBox.setBody(checkBox);
	}

	private String getPresentationNameFor(final Enrolment enrolment) {
	    if (enrolment instanceof OptionalEnrolment) {
		final OptionalEnrolment optionalEnrolment = (OptionalEnrolment) enrolment;

		return optionalEnrolment.getOptionalCurricularCourse().getName() + " ("
			+ optionalEnrolment.getCurricularCourse().getName() + ")";
	    } else {
		return enrolment.getName().getContent(LanguageUtils.getLanguage());
	    }
	}

	private void generateGroups(final HtmlBlockContainer blockContainer,
		final StudentCurriculumGroupBean studentCurriculumGroupBean, final StudentCurricularPlan studentCurricularPlan,
		final ExecutionPeriod executionPeriod, final int depth) {
	    final List<IDegreeModuleToEvaluate> courseGroupsToEnrol = studentCurriculumGroupBean
		    .getCourseGroupsToEnrolSortedByContext();
	    final List<StudentCurriculumGroupBean> curriculumGroups = studentCurriculumGroupBean
		    .getEnrolledCurriculumGroupsSortedByOrder(executionPeriod);

	    while (!courseGroupsToEnrol.isEmpty() || !curriculumGroups.isEmpty()) {

		if (!curriculumGroups.isEmpty() && courseGroupsToEnrol.isEmpty()) {
		    generateGroup(blockContainer, studentCurricularPlan, curriculumGroups.get(0), executionPeriod, depth
			    + getWidthDecreasePerLevel());
		    curriculumGroups.remove(0);
		} else if (curriculumGroups.isEmpty() && !courseGroupsToEnrol.isEmpty()) {
		    generateCourseGroupToEnroll(blockContainer, courseGroupsToEnrol.get(0), studentCurricularPlan, depth
			    + getWidthDecreasePerLevel());
		    courseGroupsToEnrol.remove(0);
		} else {
		    final Context context = courseGroupsToEnrol.get(0).getContext();
		    final CurriculumGroup curriculumGroup = curriculumGroups.get(0).getCurriculumModule();
		    if (curriculumGroup.getChildOrder(executionPeriod) <= context.getChildOrder()) {
			generateGroup(blockContainer, studentCurricularPlan, curriculumGroups.get(0), executionPeriod, depth
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

	private void generateCourseGroupToEnroll(final HtmlBlockContainer blockContainer,
		final IDegreeModuleToEvaluate degreeModuleToEnrol, final StudentCurricularPlan studentCurricularPlan,
		final int depth) {
	    final CourseGroup courseGroup = (CourseGroup) degreeModuleToEnrol.getContext().getChildDegreeModule();
	    if (courseGroup.isCycleCourseGroup()) {
		return;
	    }

	    final HtmlTable groupTable = createGroupTable(blockContainer, depth);
	    final HtmlTableRow htmlTableRow = groupTable.createRow();
	    htmlTableRow.setClasses(getGroupRowClasses());
	    htmlTableRow.createCell().setBody(new HtmlText(degreeModuleToEnrol.getContext().getChildDegreeModule().getName()));
	    final HtmlTableCell cell = htmlTableRow.createCell();
	    cell.setClasses("aright");

	    final HtmlCheckBox checkBox = new HtmlCheckBox(false);
	    final String name = StringAppender.append("degreeModuleToEnrolCheckBox", degreeModuleToEnrol.getContext()
		    .getIdInternal().toString(), ":", degreeModuleToEnrol.getCurriculumGroup().getIdInternal().toString());
	    checkBox.setName(name);
	    checkBox.setUserValue(degreeModuleToEnrol.getKey());
	    degreeModulesToEvaluateController.addCheckBox(checkBox);
	    cell.setBody(checkBox);

	}

	private void generateCycleCourseGroupsToEnrol(final HtmlBlockContainer container,
		final StudentCurricularPlan studentCurricularPlan, final int depth) {

	    for (final CycleType cycleType : studentCurricularPlan.getSupportedCycleTypesToEnrol()) {
		generateCycleCourseGroupToEnrol(container, cycleType, depth + getWidthDecreasePerLevel());
	    }
	}

	private void generateCycleCourseGroupToEnrol(final HtmlBlockContainer container, final CycleType cycleType,
		final int depth) {

	    final HtmlTable groupTable = createGroupTable(container, depth);
	    final HtmlTableRow htmlTableRow = groupTable.createRow();
	    htmlTableRow.setClasses(getGroupRowClasses());
	    htmlTableRow.createCell().setBody(new HtmlText(RenderUtils.getEnumString(cycleType)));
	    final HtmlTableCell cell = htmlTableRow.createCell();
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
	private final IDegreeModuleToEvaluate degreeModuleToEnrol;

	public OptionalCurricularCourseLinkController(final IDegreeModuleToEvaluate degreeModuleToEnrol) {
	    this.degreeModuleToEnrol = degreeModuleToEnrol;
	}

	@Override
	protected boolean isToSkipUpdate() {
	    return false;
	}

	@Override
	public void linkPressed(final IViewState viewState, final HtmlActionLink link) {
	    ((BolonhaStudentEnrollmentBean) viewState.getMetaObject().getObject())
		    .setOptionalDegreeModuleToEnrol(this.degreeModuleToEnrol);

	}
    }

    private static class CycleSelectionLinkController extends HtmlActionLinkController {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5469571160954095720L;

	private final CycleType cycleTypeToEnrol;

	public CycleSelectionLinkController(final CycleType cycleTypeToEnrol) {
	    this.cycleTypeToEnrol = cycleTypeToEnrol;
	}

	@Override
	protected boolean isToSkipUpdate() {
	    return false;
	}

	@Override
	public void linkPressed(final IViewState viewState, final HtmlActionLink link) {
	    final BolonhaStudentEnrollmentBean studentEnrollmentBean = (BolonhaStudentEnrollmentBean) viewState.getMetaObject()
		    .getObject();
	    studentEnrollmentBean.setCycleTypeToEnrol(this.cycleTypeToEnrol);

	}
    }

}
