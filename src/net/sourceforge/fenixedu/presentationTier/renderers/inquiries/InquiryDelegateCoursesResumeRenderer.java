/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.renderers.inquiries;

import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.inquiries.CurricularCourseResumeResult;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.TeacherShiftTypeResultsBean;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResult;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.ChecksumRewriter;
import pt.ist.fenixWebFramework.renderers.OutputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlBlockContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlInlineContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlLink;
import pt.ist.fenixWebFramework.renderers.components.HtmlMenu;
import pt.ist.fenixWebFramework.renderers.components.HtmlMenuOption;
import pt.ist.fenixWebFramework.renderers.components.HtmlTable;
import pt.ist.fenixWebFramework.renderers.components.HtmlTableCell;
import pt.ist.fenixWebFramework.renderers.components.HtmlTableRow;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;
import pt.ist.fenixWebFramework.renderers.components.HtmlTableCell.CellType;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;

/**
 * @author - Ricardo Rodrigues (ricardo.rodrigues@ist.utl.pt)
 * 
 */
public class InquiryDelegateCoursesResumeRenderer extends OutputRenderer {

    private String columnClasses;

    public String getColumnClasses() {
	return columnClasses;
    }

    public void setColumnClasses(String columnClasses) {
	this.columnClasses = columnClasses;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
	return new InquiryQuestionLayout(object);
    }

    private class InquiryQuestionLayout extends Layout {

	public InquiryQuestionLayout(Object object) {
	}

	@Override
	public HtmlComponent createComponent(Object object, Class type) {
	    HtmlBlockContainer blockContainer = new HtmlBlockContainer();
	    final HtmlTable mainTable = new HtmlTable();
	    blockContainer.addChild(mainTable);
	    mainTable.setClasses("tstyle1 thlight tdcenter");
	    mainTable.setStyle("width: 100%; margin-bottom: 0;");
	    List<CurricularCourseResumeResult> coursesResume = (List<CurricularCourseResumeResult>) object;

	    if (!coursesResume.isEmpty()) {
		createHeader(coursesResume.get(0).getCurricularBlocks(), mainTable);
	    }
	    for (CurricularCourseResumeResult courseResumeResult : coursesResume) {
		Set<InquiryResult> courseBlocksResults = courseResumeResult.getCurricularBlocks();

		HtmlTableRow tableRow = mainTable.createRow();
		HtmlTableCell firstCell = tableRow.createCell();
		firstCell.setBody(new HtmlText(courseResumeResult.getExecutionCourse().getNameI18N().toString()));
		firstCell.setClasses("col-course");

		int iter = 0;
		List<Integer> mandatoryIssues = courseResumeResult.getMandatoryIssues();
		for (InquiryResult inquiryResult : courseBlocksResults) {
		    HtmlTableCell curricularCell = tableRow.createCell();
		    String numberOfIssues = mandatoryIssues.get(iter) != 0 ? String.valueOf(mandatoryIssues.get(iter)) : "-";
		    String presentNumberOfIssues = " (" + numberOfIssues + ")</div>";
		    HtmlText bodyText = new HtmlText(getColoredBar(inquiryResult) + presentNumberOfIssues);
		    bodyText.setEscaped(false);
		    curricularCell.setBody(bodyText);
		    curricularCell.setClasses("col-bar");
		    iter++;
		}

		HtmlTableCell fillingStatus = tableRow.createCell();
		fillingStatus.setBody(new HtmlText(courseResumeResult.getCompletionState()));
		fillingStatus.setClasses("col-fill");

		HtmlInlineContainer container = new HtmlInlineContainer();
		HtmlTableCell linksCell = tableRow.createCell();
		String fillInParameters = buildFillInParameters(courseResumeResult);
		String resultsParameters = buildParametersForResults(courseResumeResult);

		HtmlLink link = new HtmlLink();
		link.setUrl("/delegateInquiry.do?" + resultsParameters
			+ "&method=viewCourseInquiryResults&contentContextPath_PATH=/delegado/delegado");
		link.setEscapeAmpersand(false);

		HtmlMenu menu = new HtmlMenu();
		menu
			.setOnChange("var value=this.options[this.selectedIndex].value; this.selectedIndex=0; if(value!= ''){ window.open(value,'_blank'); }");
		menu.setStyle("width: 150px");
		HtmlMenuOption optionEmpty = menu.createOption("--Ver resultados--");
		HtmlMenuOption optionUC = menu.createOption("Resultados UC");
		String calculatedUrl = link.calculateUrl();
		optionUC.setValue(calculatedUrl + "&_request_checksum_=" + ChecksumRewriter.calculateChecksum(calculatedUrl));

		for (TeacherShiftTypeResultsBean teacherShiftTypeResultsBean : courseResumeResult.getTeachersResults()) {
		    String teacherResultsParameters = buildParametersForTeacherResults(teacherShiftTypeResultsBean);
		    HtmlLink teacherLink = new HtmlLink();
		    teacherLink.setEscapeAmpersand(false);
		    teacherLink.setUrl("/delegateInquiry.do?" + teacherResultsParameters
			    + "&method=viewTeacherShiftTypeInquiryResults&contentContextPath_PATH=/delegado/delegado");
		    calculatedUrl = teacherLink.calculateUrl();

		    HtmlMenuOption optionTeacher = menu.createOption(teacherShiftTypeResultsBean.getShiftType()
			    .getFullNameTipoAula()
			    + " - " + teacherShiftTypeResultsBean.getProfessorship().getPerson().getName());
		    optionTeacher.setValue(calculatedUrl + "&_request_checksum_="
			    + ChecksumRewriter.calculateChecksum(calculatedUrl));
		}

		container.addChild(menu);

		container.addChild(new HtmlText("&nbsp;|&nbsp;", false));

		HtmlLink link1 = new HtmlLink();
		link1.setUrl("/delegateInquiry.do?" + fillInParameters + "&method=showFillInquiryPage");
		link1.setText("Preencher");
		container.addChild(link1);

		linksCell.setBody(container);
		linksCell.setClasses("col-actions");
	    }

	    return blockContainer;
	}

	private String buildParametersForTeacherResults(TeacherShiftTypeResultsBean teacherShiftTypeResultsBean) {
	    StringBuilder builder = new StringBuilder();
	    builder.append("shiftType=").append(teacherShiftTypeResultsBean.getShiftType().name());
	    builder.append("&professorshipOID=").append(teacherShiftTypeResultsBean.getProfessorship().getExternalId());
	    return builder.toString();
	}

	private String buildFillInParameters(CurricularCourseResumeResult courseResumeResult) {
	    StringBuilder builder = new StringBuilder();
	    builder.append("yearDelegateOID=").append(courseResumeResult.getYearDelegate().getExternalId());
	    builder.append("&executionDegreeOID=").append(courseResumeResult.getExecutionDegree().getExternalId());
	    builder.append("&executionCourseOID=").append(courseResumeResult.getExecutionCourse().getExternalId());
	    return builder.toString();
	}

	private String buildParametersForResults(CurricularCourseResumeResult courseResumeResult) {
	    StringBuilder builder = new StringBuilder();
	    builder.append("degreeCurricularPlanOID=").append(
		    courseResumeResult.getExecutionDegree().getDegreeCurricularPlan().getExternalId());
	    builder.append("&executionCourseOID=").append(courseResumeResult.getExecutionCourse().getExternalId());
	    return builder.toString();
	}

	private String getColoredBar(InquiryResult inquiryResult) {
	    StringBuilder sb = new StringBuilder("<div class='");
	    sb.append("bar-").append(inquiryResult.getResultClassification().name().toLowerCase());
	    sb.append("'><div>&nbsp;</div>");
	    return sb.toString();
	}

	private void createHeader(final Set<InquiryResult> courseBlocksResults, final HtmlTable mainTable) {
	    final HtmlTableRow headerRow = mainTable.createRow();

	    final HtmlTableCell firstHeaderCell = headerRow.createCell(CellType.HEADER);
	    firstHeaderCell.setBody(new HtmlText("Unidade Curricular"));
	    firstHeaderCell.setClasses("col-course");

	    for (InquiryResult inquiryResult : courseBlocksResults) {
		final HtmlTableCell firstGrouptInnerCell = headerRow.createCell(CellType.HEADER);
		firstGrouptInnerCell.setBody(new HtmlText(inquiryResult.getInquiryQuestion().getLabel().toString()));
		firstGrouptInnerCell.setClasses("col-bar");
	    }

	    final HtmlTableCell fillingStatus = headerRow.createCell(CellType.HEADER);
	    fillingStatus.setBody(new HtmlText("Estado do preenchimento"));
	    fillingStatus.setClasses("col-fill");

	    final HtmlTableCell finalCell = headerRow.createCell(CellType.HEADER);
	    finalCell.setClasses("col-actions");
	}

	@Override
	public String getClasses() {
	    return "delegate-resume";
	}

	@Override
	public String getStyle() {
	    return "max-width: 950px;";
	}
    }
}
