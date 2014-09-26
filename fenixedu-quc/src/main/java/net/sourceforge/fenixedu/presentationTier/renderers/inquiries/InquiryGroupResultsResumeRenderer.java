/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.renderers.inquiries;

import java.text.NumberFormat;
import java.util.Locale;

import net.sourceforge.fenixedu.dataTransferObject.inquiries.GroupResultsSummaryBean;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.QuestionResultsSummaryBean;
import net.sourceforge.fenixedu.domain.inquiries.InquiryQuestionHeader;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResult;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResultComment;
import net.sourceforge.fenixedu.domain.inquiries.ResultClassification;

import org.apache.commons.lang.StringUtils;

import pt.ist.fenixWebFramework.renderers.InputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlBlockContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlTable;
import pt.ist.fenixWebFramework.renderers.components.HtmlTableCell;
import pt.ist.fenixWebFramework.renderers.components.HtmlTableCell.CellType;
import pt.ist.fenixWebFramework.renderers.components.HtmlTableRow;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;

/**
 * @author - Ricardo Rodrigues (ricardo.rodrigues@ist.utl.pt)
 * 
 */
public class InquiryGroupResultsResumeRenderer extends InputRenderer {

    private Double proportionBar = 0.36;
    private String topClass;
    private boolean showComments = false;

    public void setTopClass(String topClass) {
        this.topClass = topClass;
    }

    public String getTopClass() {
        return topClass;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
        return new InquiryGroupResultLayout(object);
    }

    private class InquiryGroupResultLayout extends Layout {

        public InquiryGroupResultLayout(Object object) {
        }

        @Override
        public HtmlComponent createComponent(Object object, Class type) {
            return renderComponent(object);
        }

        @Override
        public String getClasses() {
            return getTopClass();
        }
    }

    protected HtmlComponent renderComponent(Object object) {
        GroupResultsSummaryBean groupResultsSummaryBean = (GroupResultsSummaryBean) object;

        HtmlComponent renderedComponent = null;
        if (groupResultsSummaryBean.getInquiryGroupQuestion().isScaleGroup()) {
            renderedComponent = prepareAndRenderScaledGroups(groupResultsSummaryBean);
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

    protected HtmlComponent prepareAndRenderScaledGroups(GroupResultsSummaryBean groupResultsSummaryBean) {
        setTopClass("graph");
        return renderScaledGroups(groupResultsSummaryBean);
    }

    protected HtmlComponent renderScaledGroups(GroupResultsSummaryBean groupResultsSummaryBean) {
        HtmlBlockContainer blockContainer = new HtmlBlockContainer();
        final HtmlTable mainTable = new HtmlTable();
        mainTable.setClasses("graph" + (groupResultsSummaryBean.hasClassification() ? " classification" : " neutral"));
        mainTable.setStyle("clear: right;");
        createHeaders(groupResultsSummaryBean, mainTable, groupResultsSummaryBean.getInquiryGroupQuestion()
                .getInquiryQuestionHeader());
        int absoluteScaleSize = groupResultsSummaryBean.getAbsoluteScaleValuesSize();
        for (QuestionResultsSummaryBean questionResultsSummaryBean : groupResultsSummaryBean.getQuestionsResults()) {

            HtmlTableRow row = mainTable.createRow();
            HtmlTableCell nameCell = row.createCell(CellType.HEADER);
            nameCell.setBody(new HtmlText(questionResultsSummaryBean.getInquiryQuestion().getLabel().toString()));
            HtmlTableCell numberOfAnswersCell = row.createCell();
            numberOfAnswersCell.setBody(new HtmlText(
                    questionResultsSummaryBean.getNumberOfResponses() != null ? questionResultsSummaryBean.getNumberOfResponses()
                            .getValue() : "0"));
            int nrOfAnswers = absoluteScaleSize + 2;
            numberOfAnswersCell.setClasses("x" + nrOfAnswers);
            HtmlTableCell medianCell = row.createCell();
            int medianClass = absoluteScaleSize + 1;
            if (!ResultClassification.GREY.equals(questionResultsSummaryBean.getResultClassification())
                    && questionResultsSummaryBean.getNumberOfResponses() != null) {
                renderColoredTable(absoluteScaleSize, questionResultsSummaryBean, row, medianCell, medianClass);
            } else {
                renderGreyTable(absoluteScaleSize, questionResultsSummaryBean, row, medianCell, medianClass);
            }
            if (isShowComments()) {
                renderComments(mainTable, questionResultsSummaryBean, row);
            }
        }
        blockContainer.addChild(mainTable);
        return blockContainer;
    }

    protected void renderComments(final HtmlTable mainTable, QuestionResultsSummaryBean questionResultsSummaryBean,
            HtmlTableRow row) {
        if (questionResultsSummaryBean.getResultClassification() != null
                && questionResultsSummaryBean.getResultClassification().isMandatoryComment()
                && !questionResultsSummaryBean.getResultComments().isEmpty()) {
            HtmlTableRow commentRow = mainTable.createRow();
            HtmlTableCell commentCell = commentRow.createCell();
            commentCell.setColspan(row.getCells().size());
            commentCell.setClasses("comment");
            HtmlBlockContainer comments = createComments(questionResultsSummaryBean);
            commentCell.setBody(comments);
        }
    }

    protected HtmlBlockContainer createComments(QuestionResultsSummaryBean questionResultsSummaryBean) {
        HtmlBlockContainer allCommentsBlock = new HtmlBlockContainer();

        for (InquiryResultComment inquiryResultComment : questionResultsSummaryBean.getResultComments()) {
            HtmlBlockContainer madeCommentBlock = new HtmlBlockContainer();
            madeCommentBlock.setClasses("comment");
            HtmlText madeCommentHeaderText =
                    new HtmlText("<p class=\"mbottom05\"><b>"
                            + QuestionResultsSummaryBean.getMadeCommentHeader(inquiryResultComment) + " : </b></p>");
            madeCommentHeaderText.setEscaped(false);
            String commentMade = inquiryResultComment.getComment();
            if (StringUtils.isBlank(commentMade)) {
                commentMade = "<em>Sem comentário</em>";
            }
            HtmlText madeCommentText = new HtmlText("<p class=\"mtop05\">" + commentMade + "</p>");
            madeCommentText.setEscaped(false);
            madeCommentBlock.addChild(madeCommentHeaderText);
            madeCommentBlock.addChild(madeCommentText);
            allCommentsBlock.addChild(madeCommentBlock);
        }

        return allCommentsBlock;
    }

    protected HtmlComponent renderCheckboxGroups(GroupResultsSummaryBean groupResultsSummaryBean) {
        HtmlBlockContainer blockContainer = new HtmlBlockContainer();
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

        setTotalAnswers(groupResultsSummaryBean, mainTable);

        for (QuestionResultsSummaryBean questionResultsSummaryBean : groupResultsSummaryBean.getQuestionsResults()) {
            HtmlTableRow row = mainTable.createRow();
            HtmlTableCell nameCell = row.createCell(CellType.HEADER);
            nameCell.setBody(new HtmlText(questionResultsSummaryBean.getInquiryQuestion().getLabel().toString()));
            HtmlTableCell valueCell = row.createCell();
            valueCell.setStyle("width: 110px;");
            Double value = 0.0;
            if (questionResultsSummaryBean.getResultClassification() == null) {
                value = Double.valueOf(questionResultsSummaryBean.getQuestionResult().getValue()) * 100.0;
            }
            int roundedValue = (int) StrictMath.round(value);
            HtmlText body =
                    new HtmlText("<div style=\"width: " + ((roundedValue * 2) + 40)
                            + "px;\"><div class=\"graph-bar-horz\" style=\"width:" + roundedValue * 2
                            + "px;\"></div><div class=\"graph-bar-horz-number\">" + roundedValue + "%</div></div>");
            body.setEscaped(false);
            valueCell.setBody(body);
        }
        innerContainer.addChild(mainTable);
        blockContainer.addChild(innerContainer);
        return blockContainer;
    }

    private void setTotalAnswers(GroupResultsSummaryBean groupResultsSummaryBean, final HtmlTable mainTable) {
        HtmlTableRow firstRow = mainTable.createRow();
        firstRow.setClasses("first");
        HtmlTableCell firstRowCell = firstRow.createCell(CellType.HEADER);
        firstRowCell.setColspan(2);
        firstRowCell.setStyle("padding-bottom: 15px;");
        StringBuilder labelAndValue = new StringBuilder();
        InquiryResult resultQuestion = groupResultsSummaryBean.getGroupTotalResult();
        if (resultQuestion != null) {
            labelAndValue.append(resultQuestion.getInquiryQuestion().getLabel().toString()).append(" ")
                    .append(resultQuestion.getValue());
        }
        firstRowCell.setText(labelAndValue.toString());
    }

    private void renderGreyTable(int absoluteScaleSize, QuestionResultsSummaryBean questionResultsSummaryBean, HtmlTableRow row,
            HtmlTableCell medianCell, int medianClass) {
        medianCell.setBody(new HtmlText("-"));
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
            Double value = Double.valueOf(inquiryResult.getValue()) * 100.0;
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
            if ((roundedValue / scaleProportion) <= proportionBar) {
                finalDiv = div + iter + extraBarClass + "\"></div>";
            }

            HtmlText divText = new HtmlText(finalDiv);
            divText.setEscaped(false);
            scaleCell.setBody(divText);
            scaleCell.setStyle(roundedValue != 0 ? "width: " + roundedValue + "%;" : "display: none;");
            iter++;
        }
        setLastBarCellClass(scaleRow);
    }

    private void setLastBarCellClass(HtmlTableRow scaleRow) {
        for (int iterLastBar = scaleRow.getCells().size() - 1; iterLastBar >= 0; iterLastBar--) {
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
        if (groupResultsSummaryBean.getInquiryGroupQuestion().getInquiryQuestionHeader() != null
                && groupResultsSummaryBean.getInquiryGroupQuestion().getInquiryQuestionHeader().getTitle() != null) {
            firstCell.setBody(new HtmlText(groupResultsSummaryBean.getInquiryGroupQuestion().getInquiryQuestionHeader()
                    .getTitle().toString()));
        }
        firstCell.setClasses("first");
        HtmlTableCell totalNumber = headerRow.createCell(CellType.HEADER);
        totalNumber.setBody(new HtmlText("N"));
        HtmlTableCell median = headerRow.createCell(CellType.HEADER);
        median.setBody(new HtmlText("Mediana"));

        QuestionResultsSummaryBean questionResultsSummaryBean = groupResultsSummaryBean.getValidQuestionResult();
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
                questionHeader =
                        questionResultsSummaryBean.getScaleValues().iterator().next().getInquiryQuestion()
                                .getInquiryQuestionHeader();
            }
        }
        if (questionHeader != null) {
            int endAt = getPercentageScaleSize(questionResultsSummaryBean, questionHeader);
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

    private int getPercentageScaleSize(QuestionResultsSummaryBean questionResultsSummaryBean, InquiryQuestionHeader questionHeader) {
        if (questionResultsSummaryBean != null && questionResultsSummaryBean.getScaleValues().size() > 0) {
            return questionResultsSummaryBean.getScaleValues().size();
        } else {
            String[] scaleValues = questionHeader.getScaleHeaders().getScaleValues();
            int counter = 0;
            for (String scaleValue : scaleValues) {
                Integer valueOf = null;
                try {
                    valueOf = Integer.valueOf(scaleValue);
                } catch (NumberFormatException e) {
                    //do nothing
                }
                if (valueOf == null) {
                    counter++;
                } else if (valueOf > 0) {
                    counter++;
                }
            }
            return counter;
        }
    }

    public void setShowComments(boolean showComments) {
        this.showComments = showComments;
    }

    public boolean isShowComments() {
        return showComments;
    }

}
