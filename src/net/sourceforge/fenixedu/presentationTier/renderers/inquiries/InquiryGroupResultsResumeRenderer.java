/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.renderers.inquiries;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import net.sourceforge.fenixedu.dataTransferObject.inquiries.GroupResultsSummaryBean;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.QuestionResultsSummaryBean;
import net.sourceforge.fenixedu.domain.inquiries.InquiryQuestionHeader;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResult;
import net.sourceforge.fenixedu.domain.inquiries.ResultClassification;

import org.apache.commons.lang.StringUtils;

import pt.ist.fenixWebFramework.renderers.OutputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlBlockContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
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
public class InquiryGroupResultsResumeRenderer extends OutputRenderer {

    private Double proportionBar = 0.36;

    @Override
    protected Layout getLayout(Object object, Class type) {
	return new InquiryGroupResultLayout(object);
    }

    private class InquiryGroupResultLayout extends Layout {

	private String topClass;

	public InquiryGroupResultLayout(Object object) {
	}

	@Override
	public HtmlComponent createComponent(Object object, Class type) {
	    GroupResultsSummaryBean groupResultsSummaryBean = (GroupResultsSummaryBean) object;

	    HtmlComponent renderedComponent = null;
	    if (groupResultsSummaryBean.getInquiryGroupQuestion().isScaleGroup()) {
		setTopClass("graph");
		renderedComponent = renderScaledGroups(groupResultsSummaryBean);
	    } else {
		if (groupResultsSummaryBean.isLeft()) {
		    setTopClass("workload-left");
		} else {
		    setTopClass("workload-right");
		}
		renderedComponent = renderCheckboxGroups(groupResultsSummaryBean);
	    }
	    return renderedComponent;
	}

	private HtmlComponent renderCheckboxGroups(GroupResultsSummaryBean groupResultsSummaryBean) {
	    HtmlBlockContainer blockContainer = new HtmlBlockContainer();
	    if (groupResultsSummaryBean.isLeft()) {
		blockContainer.setClasses("workload-left");
	    } else {
		blockContainer.setClasses("workload-right");
	    }
	    String groupTitle = null;
	    if (groupResultsSummaryBean.getInquiryGroupQuestion().getResultQuestionHeader() != null) {
		groupTitle = groupResultsSummaryBean.getInquiryGroupQuestion().getResultQuestionHeader().getTitle().toString();
	    } else {
		groupTitle = groupResultsSummaryBean.getInquiryGroupQuestion().getInquiryQuestionHeader().getTitle().toString();
	    }

	    HtmlText groupTitleText = new HtmlText("<p><strong>" + groupTitle + "</strong></p>");
	    groupTitleText.setEscaped(false);
	    blockContainer.addChild(groupTitleText);

	    HtmlBlockContainer innerContainer = new HtmlBlockContainer();
	    innerContainer.setClasses("graph");
	    final HtmlTable mainTable = new HtmlTable();
	    mainTable.setClasses("graph");
	    for (QuestionResultsSummaryBean questionResultsSummaryBean : groupResultsSummaryBean.getQuestionsResults()) {
		HtmlTableRow row = mainTable.createRow();
		HtmlTableCell nameCell = row.createCell(CellType.HEADER);
		nameCell.setBody(new HtmlText(questionResultsSummaryBean.getInquiryQuestion().getLabel().toString()));
		HtmlTableCell valueCell = row.createCell();
		valueCell.setStyle("width: 110px;");
		Double value = 0.0;
		if (questionResultsSummaryBean.getResultClassification() == null) {
		    value = Double.valueOf(questionResultsSummaryBean.getQuestionResult().getValue().replace(",", ".")) * 100.0;
		}
		int roundedValue = (int) StrictMath.round(value);
		HtmlText body = new HtmlText("<div style=\"width: " + ((roundedValue * 2) + 40)
			+ "px;\"><div class=\"graph-bar-horz\" style=\"width:" + roundedValue * 2
			+ "px;\"></div><div class=\"graph-bar-horz-number\">" + roundedValue + "%</div></div>");
		body.setEscaped(false);
		valueCell.setBody(body);
	    }
	    innerContainer.addChild(mainTable);
	    blockContainer.addChild(innerContainer);
	    return blockContainer;
	}

	private HtmlComponent renderScaledGroups(GroupResultsSummaryBean groupResultsSummaryBean) {
	    HtmlBlockContainer blockContainer = new HtmlBlockContainer();
	    final HtmlTable mainTable = new HtmlTable();
	    mainTable.setClasses("graph" + (groupResultsSummaryBean.hasClassification() ? " classification" : " neutral"));
	    mainTable.setStyle("clear: both;");
	    createHeaders(groupResultsSummaryBean, mainTable, groupResultsSummaryBean.getInquiryGroupQuestion()
		    .getInquiryQuestionHeader());
	    int absoluteScaleSize = groupResultsSummaryBean.getAbsoluteScaleValuesSize();
	    for (QuestionResultsSummaryBean questionResultsSummaryBean : groupResultsSummaryBean.getQuestionsResults()) {

		HtmlTableRow row = mainTable.createRow();
		HtmlTableCell nameCell = row.createCell(CellType.HEADER);
		nameCell.setBody(new HtmlText(questionResultsSummaryBean.getInquiryQuestion().getLabel().toString()));
		HtmlTableCell numberOfAnswersCell = row.createCell();
		numberOfAnswersCell.setBody(new HtmlText(questionResultsSummaryBean.getNumberOfResponses().getValue()));
		int nrOfAnswers = absoluteScaleSize + 2;
		numberOfAnswersCell.setClasses("x" + nrOfAnswers);
		HtmlTableCell medianCell = row.createCell();
		int medianClass = absoluteScaleSize + 1;
		if (questionResultsSummaryBean.getResultClassification() == null
			|| questionResultsSummaryBean.getResultClassification().isRelevantResult()) {
		    renderColoredTable(absoluteScaleSize, questionResultsSummaryBean, row, medianCell, medianClass);
		} else {
		    renderGreyTable(absoluteScaleSize, row, medianCell, medianClass);
		}
	    }
	    blockContainer.addChild(mainTable);
	    return blockContainer;
	}

	private void renderGreyTable(int absoluteScaleSize, HtmlTableRow row, HtmlTableCell medianCell, int medianClass) {
	    medianCell.setBody(new HtmlText("-"));
	    medianCell.setClasses("x" + medianClass);

	    for (int colClassesIter = absoluteScaleSize; colClassesIter > 0; colClassesIter--) {
		HtmlTableCell absoluteValueCell = row.createCell();
		absoluteValueCell.setBody(new HtmlText("-"));
		absoluteValueCell.setClasses("x" + colClassesIter);
	    }

	    HtmlTableCell scaleCells = row.createCell();
	    final HtmlTable scaleTable = new HtmlTable();
	    scaleCells.setBody(scaleTable);
	    HtmlTableRow scaleRow = scaleTable.createRow();
	    HtmlTableCell scaleCell = scaleRow.createCell();
	    HtmlText greyBody = new HtmlText("<div class=\"graph-bar-grey\"></div>");
	    greyBody.setEscaped(false);
	    scaleCell.setBody(greyBody);
	    scaleCell.setStyle("width: 100%");
	}

	private void renderColoredTable(int absoluteScaleSize, QuestionResultsSummaryBean questionResultsSummaryBean,
		HtmlTableRow row, HtmlTableCell medianCell, int medianClass) {
	    HtmlText medianText = new HtmlText("<b>" + questionResultsSummaryBean.getMedian().getValue() + "</b>");
	    medianText.setEscaped(false);
	    medianCell.setBody(medianText);
	    medianCell.setClasses("x" + medianClass);

	    int colClassesIter = absoluteScaleSize;
	    for (InquiryResult inquiryResult : questionResultsSummaryBean.getAbsoluteScaleValues()) {
		HtmlTableCell absoluteValueCell = row.createCell();
		absoluteValueCell.setBody(new HtmlText(inquiryResult.getValue()));
		absoluteValueCell.setClasses("x" + colClassesIter);
		colClassesIter--;
	    }

	    HtmlTableCell scaleCells = row.createCell();
	    final HtmlTable scaleTable = new HtmlTable();
	    scaleCells.setBody(scaleTable);
	    HtmlTableRow scaleRow = scaleTable.createRow();
	    NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());
	    numberFormat.setMaximumFractionDigits(2);
	    String div = "<div class=\"graph-bar-1" + questionResultsSummaryBean.getScaleValues().size() + "-";
	    double scaleProportion = 100.0 / questionResultsSummaryBean.getScaleValues().size();
	    int iter = 1;
	    int firstCell = 1;
	    for (InquiryResult inquiryResult : questionResultsSummaryBean.getScaleValues()) {
		HtmlTableCell scaleCell = scaleRow.createCell();
		Double value = Double.valueOf(inquiryResult.getValue().replace(",", ".")) * 100.0;
		int roundedValue = (int) StrictMath.round(value);
		String extraBarClass = "";
		if (iter == firstCell) {
		    if (roundedValue != 0) {
			extraBarClass = " first-bar";
		    } else {
			firstCell++;
		    }
		}
		String finalDiv = div + iter + extraBarClass + "\">" + roundedValue + "%</div>";
		if (((double) roundedValue / scaleProportion) <= proportionBar) {
		    finalDiv = div + iter + extraBarClass + "\"></div>";
		}

		HtmlText divText = new HtmlText(finalDiv);
		divText.setEscaped(false);
		scaleCell.setBody(divText);
		scaleCell.setStyle(roundedValue != 0 ? "width: " + roundedValue + "%;" : "display: none;");
		iter++;
	    }
	    setLastBarCellClass(scaleRow, iter);
	}

	private void setLastBarCellClass(HtmlTableRow scaleRow, int iter) {
	    for (int iterLastBar = scaleRow.getCells().size() - 1; iter >= 0; iterLastBar--) {
		HtmlTableCell tableCell = scaleRow.getCells().get(iterLastBar);
		if (!tableCell.getStyle().contains("none")) {
		    HtmlText body = (HtmlText) tableCell.getBody();
		    String bodyText = body.getText().replaceAll("(graph-bar-1\\d-\\d)", "$1 last-bar");
		    body.setText(bodyText);
		    return;
		}
	    }
	}

	private void createHeaders(GroupResultsSummaryBean groupResultsSummaryBean, HtmlTable mainTable,
		InquiryQuestionHeader questionHeader) {
	    final HtmlTableRow headerRow = mainTable.createRow();
	    headerRow.setClasses("thead");
	    HtmlTableCell firstCell = headerRow.createCell(CellType.HEADER);
	    firstCell.setClasses("first");
	    HtmlTableCell totalNumber = headerRow.createCell(CellType.HEADER);
	    totalNumber.setBody(new HtmlText("N"));
	    HtmlTableCell median = headerRow.createCell(CellType.HEADER);
	    median.setBody(new HtmlText("Mediana"));

	    QuestionResultsSummaryBean questionResultsSummaryBean = getValidQuestionResult(groupResultsSummaryBean
		    .getQuestionsResults());
	    if (questionResultsSummaryBean != null) {
		for (InquiryResult inquiryResult : questionResultsSummaryBean.getAbsoluteScaleValues()) {
		    if (questionHeader == null) {
			questionHeader = inquiryResult.getInquiryQuestion().getInquiryQuestionHeader();
		    }
		    String label = questionHeader.getScaleHeaders().getLabelByValue(inquiryResult.getScaleValue());
		    HtmlTableCell cell = headerRow.createCell(CellType.HEADER);
		    cell.setBody(new HtmlText(label));
		}
	    }
	    HtmlTableCell scaleCell = headerRow.createCell(CellType.HEADER);
	    scaleCell.setAlign("center");
	    HtmlTable legendTable = new HtmlTable();
	    HtmlTableRow legendRow = legendTable.createRow();

	    if (questionHeader == null) {
		if (questionResultsSummaryBean.getScaleValues().size() > 0) {
		    questionHeader = questionResultsSummaryBean.getScaleValues().get(0).getInquiryQuestion()
			    .getInquiryQuestionHeader();
		}
	    }
	    if (questionHeader != null) {
		int endAt = questionResultsSummaryBean.getScaleValues().size();
		for (int iter = 0, startAt = 1; iter < questionHeader.getScaleHeaders().getScaleValues().length; iter++) {
		    String value = questionHeader.getScaleHeaders().getScaleValues()[iter];
		    if (StringUtils.isNumeric(value) && Integer.valueOf(value) > 0) {
			StringBuilder builder = new StringBuilder("<span class=\"legend-bar\">");
			builder.append(questionHeader.getScaleHeaders().getScale()[iter]);
			builder.append("&nbsp;<span class=\"legend-bar-1");
			builder.append(endAt).append("-").append(startAt).append("\">&nbsp;</span>");
			builder.append("</span>");
			HtmlText legendText = new HtmlText(builder.toString());
			legendText.setEscaped(false);
			HtmlTableCell legendCell = legendRow.createCell();
			legendCell.setBody(legendText);
			startAt++;
		    }
		}
		scaleCell.setBody(legendTable);
	    }
	}

	private QuestionResultsSummaryBean getValidQuestionResult(List<QuestionResultsSummaryBean> questionsResults) {
	    for (QuestionResultsSummaryBean questionResultsSummaryBean : questionsResults) {
		if (!ResultClassification.GREY.equals(questionResultsSummaryBean.getResultClassification())) {
		    return questionResultsSummaryBean;
		}
	    }
	    return null;
	}

	@Override
	public String getClasses() {
	    return getTopClass();
	}

	public void setTopClass(String topClass) {
	    this.topClass = topClass;
	}

	public String getTopClass() {
	    return topClass;
	}
    }
}
