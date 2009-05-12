package net.sourceforge.fenixedu.presentationTier.renderers.degreeStructure;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriod;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRule;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.ContentInjectionRewriter;
import net.sourceforge.fenixedu.util.CurricularPeriodLabelFormatter;
import net.sourceforge.fenixedu.util.CurricularRuleLabelFormatter;
import pt.ist.fenixWebFramework.renderers.OutputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlBlockContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlImage;
import pt.ist.fenixWebFramework.renderers.components.HtmlLink;
import pt.ist.fenixWebFramework.renderers.components.HtmlLinkWithPreprendedComment;
import pt.ist.fenixWebFramework.renderers.components.HtmlTable;
import pt.ist.fenixWebFramework.renderers.components.HtmlTableCell;
import pt.ist.fenixWebFramework.renderers.components.HtmlTableRow;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;
import pt.utl.ist.fenix.tools.util.Pair;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public class DegreeCurricularPlanRenderer extends OutputRenderer {

    static private final String CELL_CLASSES = "scplancolident, scplancolcurricularcourse, scplancoldegreecurricularplan, "
	    + "scplancolenrollmentstate, scplancolenrollmenttype, scplancolgrade, scplancolweight, scplancolects, scplancolects";

    // TODO: change this (create constants)

    private final ResourceBundle apr = ResourceBundle.getBundle("resources.ApplicationResources", Language.getLocale());

    private String degreeCurricularPlanClass = "scplan";

    private String courseGroupRowClass = "scplangroup";

    private String curricularCourseRowClass = "scplanenrollment";

    private String curricularRuleRowClass = "xpto";

    private String cellClasses = CELL_CLASSES;

    public String getDegreeCurricularPlanClass() {
	return degreeCurricularPlanClass;
    }

    public void setDegreeCurricularPlanClass(String degreeCurricularPlanClass) {
	this.degreeCurricularPlanClass = degreeCurricularPlanClass;
    }

    public String getCourseGroupRowClass() {
	return courseGroupRowClass;
    }

    public void setCourseGroupRowClass(String courseGroupRowClass) {
	this.courseGroupRowClass = courseGroupRowClass;
    }

    public String getCurricularCourseRowClass() {
	return curricularCourseRowClass;
    }

    public void setCurricularCourseRowClass(String curricularCourseRowClass) {
	this.curricularCourseRowClass = curricularCourseRowClass;
    }

    public String getCurricularRuleRowClass() {
	return curricularRuleRowClass;
    }

    public void setCurricularRuleRowClass(String curricularRuleRowClass) {
	this.curricularRuleRowClass = curricularRuleRowClass;
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

    private String getCurriclarCourseCellClass() {
	return getCellClasses()[2];
    }

    private String getCurricularPeriodCellClass() {
	return getCellClasses()[3];
    }

    private String getRegimeCellClass() {
	return getCellClasses()[4];
    }

    private String getCourseLoadCellClass() {
	return getCellClasses()[5];
    }

    private String getEctsCreditsCellClass() {
	return getCellClasses()[6];
    }

    private String getOptionalInformationCellClass() {
	return getCellClasses()[7];
    }

    private DegreeCurricularPlanRendererConfig config;

    private DegreeCurricularPlan getDegreeCurricularPlan() {
	return config.getDegreeCurricularPlan();
    }

    private ExecutionYear getExecutionInterval() {
	return config.getExecutionInterval();
    }

    // private boolean organizeByGroups() {
    // return config.organizeByGroups();
    // }

    // TODO:
    // private boolean organizeByYears() {
    // return config.organizeByYears();
    // }

    private boolean showRules() {
	return config.isShowRules();
    }

    private boolean showCourses() {
	return config.isShowCourses();
    }

    private String getViewCurricularCourseUrl() {
	return config.getViewCurricularCourseUrl();
    }

    private List<Pair<String, String>> getViewCurricularCourseUrlParameters() {
	return config.getViewCurricularCourseUrlParameters();
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
	return new DegreeCurricularPlanLayout();
    }

    private class DegreeCurricularPlanLayout extends Layout {

	static private final String EMPTY_CELL = "-";
	static private final String SPACER_IMAGE_PATH = "/images/scp_spacer.gif";

	static private final int MAX_LINE_SIZE = 25;
	static private final int MAX_COL_SPAN_FOR_TEXT_ON_GROUPS_WITH_CHILDS = 21;
	static private final int MAX_COL_SPAN_FOR_TEXT_ON_CURRICULAR_COURSES = 21;

	@Override
	public HtmlComponent createComponent(Object object, Class type) {
	    config = (DegreeCurricularPlanRendererConfig) object;

	    final HtmlContainer container = new HtmlBlockContainer();
	    // TODO:
	    // if (organizeByGroups()) {
	    draw(getDegreeCurricularPlan(), createMainTable(container));
	    // }
	    return container;
	}

	private HtmlTable createMainTable(final HtmlContainer container) {
	    final HtmlTable main = new HtmlTable();
	    container.addChild(main);
	    main.setClasses(getDegreeCurricularPlanClass());
	    return main;
	}

	private void draw(final DegreeCurricularPlan degreeCurricularPlan, final HtmlTable main) {
	    drawCourseGroupRow(degreeCurricularPlan.getRoot(), null, main, 0);
	}

	private void drawCourseGroupRow(final CourseGroup courseGroup, final Context previous, final HtmlTable main, int level) {
	    drawCourseGroupName(courseGroup, main, level);
	    drawCurricularRulesRows(courseGroup, previous, main, level);
	    drawCurricularCourseRows(courseGroup, main, level + 1);
	    drawCourseGroupRows(courseGroup, main, level + 1);
	}

	private void drawCurricularRulesRows(final DegreeModule module, final Context previous, final HtmlTable main, int level) {
	    if (showRules()) {
		for (final CurricularRule rule : module.getVisibleCurricularRules(getExecutionInterval())) {
		    if (rule.appliesToContext(previous)) {
			drawCurricularRuleRow(rule, main, level);
		    }
		}
	    }
	}

	private void drawCurricularRuleRow(final CurricularRule rule, final HtmlTable main, int level) {
	    final HtmlTableRow groupRow = main.createRow();
	    groupRow.setClasses(getCurricularRuleRowClass());
	    addTabsToRow(groupRow, level);

	    final HtmlTableCell cell = groupRow.createCell();
	    cell.setClasses(getCurricularRuleRowClass());
	    cell.setColspan(MAX_LINE_SIZE - level);
	    cell.setText(CurricularRuleLabelFormatter.getLabel(rule));
	}

	private void drawCourseGroupRows(final CourseGroup courseGroup, final HtmlTable main, int level) {
	    for (final Context context : courseGroup.getSortedOpenChildContextsWithCourseGroups(getExecutionInterval())) {
		drawCourseGroupRow((CourseGroup) context.getChildDegreeModule(), context, main, level);
	    }
	}

	private void drawCourseGroupName(final CourseGroup courseGroup, final HtmlTable mainTable, final int level) {
	    final HtmlTableRow groupRow = mainTable.createRow();
	    groupRow.setClasses(getCourseGroupRowClass());
	    addTabsToRow(groupRow, level);

	    final HtmlTableCell cell = groupRow.createCell();
	    cell.setClasses(getLabelCellClass());
	    cell.setBody(new HtmlText(courseGroup.getNameI18N().getContent()));

	    if (showCourses() && courseGroup.hasAnyChildContextWithCurricularCourse()) {
		cell.setColspan(MAX_COL_SPAN_FOR_TEXT_ON_GROUPS_WITH_CHILDS - level);
		drawCurricularPeriodHeader(groupRow);
		drawCourseLoadHeader(groupRow);
		drawEctsCreditsHeader(groupRow);
	    } else {
		cell.setColspan(MAX_LINE_SIZE - level);
	    }
	}

	private void drawCurricularPeriodHeader(final HtmlTableRow row) {
	    final HtmlTableCell cell = row.createCell();
	    cell.setClasses(getCourseLoadCellClass());
	    cell.setColspan(2);
	    cell.setText(apr.getString("label.degreeCurricularPlan.renderer.begin"));
	}

	private void drawCourseLoadHeader(final HtmlTableRow row) {
	    final HtmlTableCell cell = row.createCell();
	    cell.setClasses(getCourseLoadCellClass());
	    cell.setText(apr.getString("label.degreeCurricularPlan.renderer.course.load"));
	}

	private void drawEctsCreditsHeader(final HtmlTableRow row) {
	    final HtmlTableCell cell = row.createCell();
	    cell.setClasses(getEctsCreditsCellClass());
	    cell.setText(apr.getString("label.degreeCurricularPlan.renderer.ects"));
	}

	private void drawCurricularCourseRows(final CourseGroup courseGroup, final HtmlTable main, int level) {
	    if (showCourses()) {
		for (final Context context : courseGroup.getSortedOpenChildContextsWithCurricularCourses(getExecutionInterval())) {
		    drawCurricularCourseRow(context, main, level);
		}
	    }
	}

	private void drawCurricularCourseRow(final Context context, final HtmlTable main, int level) {
	    final HtmlTableRow row = main.createRow();
	    addTabsToRow(row, level);
	    row.setClasses(getCurricularCourseRowClass());

	    final CurricularCourse curricularCourse = (CurricularCourse) context.getChildDegreeModule();
	    if (curricularCourse.isOptionalCurricularCourse()) {
		drawCurricularCourseName(curricularCourse, row, false, level);
		drawContextInformation(context.getCurricularPeriod(), row);
		drawOptionalCellInformation(row);

	    } else if (curricularCourse.isSemestrial(getExecutionInterval())) {
		drawCurricularCourseName(curricularCourse, row, true, level);
		drawContextInformation(context.getCurricularPeriod(), row);
		drawRegime(curricularCourse, row);
		drawCourseLoad(curricularCourse, context.getCurricularPeriod(), row);
		drawEctsCredits(curricularCourse, context.getCurricularPeriod(), row);

	    } else {
		drawAnualCurricularCourseRow(context, row, main, level);
	    }

	    drawCurricularRulesRows(curricularCourse, context, main, level);
	}

	private void drawOptionalCellInformation(final HtmlTableRow row) {
	    final HtmlTableCell cell = row.createCell();
	    cell.setClasses(getOptionalInformationCellClass());
	    cell.setColspan(MAX_LINE_SIZE - MAX_COL_SPAN_FOR_TEXT_ON_CURRICULAR_COURSES - 1);
	    cell.setText(apr.getString("label.degreeCurricularPlan.renderer.option"));
	}

	private void drawAnualCurricularCourseRow(final Context context, final HtmlTableRow row, final HtmlTable main,
		final int level) {

	    final CurricularCourse curricularCourse = (CurricularCourse) context.getChildDegreeModule();
	    if (curricularCourse.hasCompetenceCourse()) {

		if (curricularCourse.getCompetenceCourse().hasOneCourseLoad(getExecutionInterval())) {
		    drawCurricularCourseName(curricularCourse, row, true, level);
		    drawContextInformation(context.getCurricularPeriod(), row);
		    drawRegime(curricularCourse, row);
		    drawCourseLoad(curricularCourse, context.getCurricularPeriod(), row);
		    drawEctsCredits(curricularCourse, context.getCurricularPeriod(), row);
		} else {

		    final CurricularPeriod firstCP = context.getCurricularPeriod();
		    final ExecutionSemester firstES = getExecutionInterval().getExecutionSemesterFor(firstCP.getChildOrder());

		    drawCurricularCourseName(curricularCourse, row, true, level);
		    drawContextInformation(firstCP, row);
		    drawRegime(curricularCourse, row);
		    drawCourseLoad(curricularCourse, firstCP, firstES, row);
		    drawEctsCredits(curricularCourse, firstCP, firstES, row);

		    final CurricularPeriod secondCP = context.getCurricularPeriod().getNext();
		    final ExecutionSemester secondES = getExecutionInterval().getExecutionSemesterFor(secondCP.getChildOrder());

		    drawCurricularCourseName(curricularCourse, row, false, level);
		    drawContextInformation(secondCP, row);
		    drawRegime(curricularCourse, row);
		    drawCourseLoad(curricularCourse, secondCP, secondES, row);
		    drawEctsCredits(curricularCourse, secondCP, secondES, row);
		}

	    } else {
		drawCurricularCourseName(curricularCourse, row, true, level);
		drawContextInformation(context.getCurricularPeriod(), row);
		drawRegime(curricularCourse, row);
		drawCourseLoad(curricularCourse, context.getCurricularPeriod(), row);
		drawEctsCredits(curricularCourse, context.getCurricularPeriod(), row);
	    }
	}

	private void drawCurricularCourseName(final CurricularCourse course, final HtmlTableRow row, boolean linkable, int level) {
	    final HtmlTableCell cell = row.createCell();
	    cell.setClasses(getCurriclarCourseCellClass());
	    cell.setColspan(MAX_COL_SPAN_FOR_TEXT_ON_CURRICULAR_COURSES - level);

	    if (linkable) {
		final HtmlLink result = new HtmlLinkWithPreprendedComment(ContentInjectionRewriter.HAS_CONTEXT_PREFIX);

		result.setText(course.getNameI18N(getExecutionInterval()).getContent());
		result.setModuleRelative(true);

		result.setUrl(getViewCurricularCourseUrl());
		result.setParameter("curricularCourseId", course.getExternalId());

		for (final Pair<String, String> param : getViewCurricularCourseUrlParameters()) {
		    result.setParameter(param.getKey(), param.getValue());
		}

		cell.setBody(result);

	    } else {
		cell.setText(course.getNameI18N(getExecutionInterval()).getContent());
	    }
	}

	private void drawContextInformation(final CurricularPeriod period, final HtmlTableRow row) {
	    final HtmlTableCell cell = row.createCell();
	    cell.setClasses(getCurricularPeriodCellClass());
	    cell.setText(CurricularPeriodLabelFormatter.getFullLabel(period, true));
	}

	private void drawRegime(final CurricularCourse course, final HtmlTableRow row) {
	    final HtmlTableCell cell = row.createCell();
	    cell.setClasses(getRegimeCellClass());
	    cell.setText(drawRegime(course) ? course.getRegime(getExecutionInterval()).getAcronym() : EMPTY_CELL);
	    cell.setTitle(apr.getString("label.degreeCurricularPlan.renderer.title.regime"));
	}

	private boolean drawRegime(final CurricularCourse curricularCourse) {
	    return curricularCourse.isOptionalCurricularCourse() || !curricularCourse.hasRegime(getExecutionInterval());
	}

	private void drawCourseLoad(final CurricularCourse course, final CurricularPeriod period, final HtmlTableRow row) {
	    final HtmlTableCell cell = row.createCell();
	    cell.setClasses(getCourseLoadCellClass());

	    if (course.isOptionalCurricularCourse()) {
		cell.setText(EMPTY_CELL);
	    } else {
		final StringBuilder builder = new StringBuilder();

		builder.append(apr.getString("label.degreeCurricularPlan.renderer.acronym.contact.load")).append("-");
		builder.append(roundValue(course.getContactLoad(period, getExecutionInterval()))).append(" ");

		builder.append(apr.getString("label.degreeCurricularPlan.renderer.acronym.autonomous.work")).append("-");
		builder.append(course.getAutonomousWorkHours(period, getExecutionInterval()).toString()).append(" ");

		builder.append(apr.getString("label.degreeCurricularPlan.renderer.acronym.total.load")).append("-");
		builder.append(course.getTotalLoad(period, getExecutionInterval()));

		cell.setText(builder.toString());
	    }

	    cell.setTitle(apr.getString("label.degreeCurricularPlan.renderer.title.course.load"));
	}

	private void drawCourseLoad(final CurricularCourse course, final CurricularPeriod period,
		final ExecutionSemester interval, final HtmlTableRow row) {

	    final HtmlTableCell cell = row.createCell();
	    cell.setClasses(getCourseLoadCellClass());

	    if (course.isOptionalCurricularCourse()) {
		cell.setText(EMPTY_CELL);
	    } else {
		final StringBuilder builder = new StringBuilder();

		builder.append(apr.getString("label.degreeCurricularPlan.renderer.acronym.contact.load")).append("-");
		builder.append(roundValue(course.getContactLoad(period, interval))).append(" ");

		builder.append(apr.getString("label.degreeCurricularPlan.renderer.acronym.autonomous.work")).append("-");
		builder.append(course.getAutonomousWorkHours(period, interval).toString()).append(" ");

		builder.append(apr.getString("label.degreeCurricularPlan.renderer.acronym.total.load")).append("-");
		builder.append(course.getTotalLoad(period, interval));

		cell.setText(builder.toString());
	    }

	    cell.setTitle(apr.getString("label.degreeCurricularPlan.renderer.title.course.load"));
	}

	private void drawEctsCredits(final CurricularCourse course, final CurricularPeriod period, final HtmlTableRow row) {
	    final HtmlTableCell cell = row.createCell();
	    cell.setClasses(getEctsCreditsCellClass());
	    cell.setText(course.isOptionalCurricularCourse() ? EMPTY_CELL : course.getEctsCredits(period, getExecutionInterval())
		    .toString());
	}

	private void drawEctsCredits(final CurricularCourse course, final CurricularPeriod period,
		final ExecutionSemester interval, final HtmlTableRow row) {
	    final HtmlTableCell cell = row.createCell();
	    cell.setClasses(getEctsCreditsCellClass());
	    cell.setText(course.isOptionalCurricularCourse() ? EMPTY_CELL : course.getEctsCredits(period, interval).toString());
	}

	private String roundValue(Double contactLoad) {
	    return new BigDecimal(contactLoad).setScale(2, RoundingMode.HALF_EVEN).toPlainString();
	}

	private void addTabsToRow(final HtmlTableRow row, final int level) {
	    for (int i = 0; i < level; i++) {
		final HtmlLink link = new HtmlLink();
		link.setModuleRelative(false);
		link.setUrl(DegreeCurricularPlanLayout.SPACER_IMAGE_PATH);

		final HtmlImage spacerImage = new HtmlImage();
		spacerImage.setSource(link.calculateUrl());

		final HtmlTableCell tabCell = row.createCell();
		tabCell.setClasses(getTabCellClass());
		tabCell.setBody(spacerImage);
	    }
	}
    }
}
