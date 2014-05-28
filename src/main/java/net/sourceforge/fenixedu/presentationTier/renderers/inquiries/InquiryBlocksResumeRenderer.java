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

import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.inquiries.BlockResumeResult;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResult;
import pt.ist.fenixWebFramework.renderers.OutputRenderer;
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
public abstract class InquiryBlocksResumeRenderer extends OutputRenderer {

    private boolean showMandatoryQuestions = true;
    private boolean extraColumn = false;

    public void setShowMandatoryQuestions(boolean showMandatoryQuestions) {
        this.showMandatoryQuestions = showMandatoryQuestions;
    }

    public boolean isShowMandatoryQuestions() {
        return showMandatoryQuestions;
    }

    public void setExtraColumn(boolean extraColumn) {
        this.extraColumn = extraColumn;
    }

    public boolean isExtraColumn() {
        return extraColumn;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
        return new InquiryQuestionLayout(object);
    }

    protected abstract void createFinalCells(HtmlTableRow tableRow, BlockResumeResult blockResumeResult);

    protected class InquiryQuestionLayout extends Layout {

        public InquiryQuestionLayout(Object object) {
        }

        @Override
        public HtmlComponent createComponent(Object object, Class type) {
            HtmlBlockContainer blockContainer = new HtmlBlockContainer();
            final HtmlTable mainTable = new HtmlTable();
            mainTable.setClasses("tstyle1 thlight tdcenter");
            mainTable.setStyle("margin-bottom: 0;");
            blockContainer.addChild(mainTable);

            HtmlTableRow headerRow = mainTable.createRow();
            createHeader(object, headerRow);

            buildTableBody(mainTable, object, 0);
            return blockContainer;
        }
    }

    protected void buildTableBody(final HtmlTable mainTable, Object object, int rowSpan) {
        List<BlockResumeResult> blocksResume = (List<BlockResumeResult>) object;
        boolean createFirstCellBeforeCommonBody = isExtraColumn();
        for (BlockResumeResult blockResumeResult : blocksResume) {
            Set<InquiryResult> blocksResults = blockResumeResult.getResultBlocks();

            HtmlTableRow tableRow = mainTable.createRow();
            if (createFirstCellBeforeCommonBody) {
                createFirstCellBeforeCommonBody(rowSpan, blockResumeResult, tableRow);
                createFirstCellBeforeCommonBody = false;
            }
            createRegularLine(blockResumeResult, blocksResults, tableRow);
        }
    }

    protected void createRegularLine(BlockResumeResult blockResumeResult, Set<InquiryResult> blocksResults, HtmlTableRow tableRow) {
        createFirstPartRegularLine(blockResumeResult, tableRow);

        int iter = 0;
        List<Integer> mandatoryIssues = blockResumeResult.getMandatoryIssues();
        for (InquiryResult inquiryResult : blocksResults) {
            HtmlTableCell curricularCell = tableRow.createCell();
            String presentNumberOfIssues = "";
            if (isShowMandatoryQuestions()) {
                String numberOfIssues = mandatoryIssues.get(iter) != 0 ? String.valueOf(mandatoryIssues.get(iter)) : "-";
                presentNumberOfIssues = " (" + numberOfIssues + ")";
            }
            HtmlText bodyText = new HtmlText(getColoredBar(inquiryResult) + presentNumberOfIssues + "</div>");
            bodyText.setEscaped(false);
            curricularCell.setBody(bodyText);
            curricularCell.setClasses("col-bar");
            iter++;
        }

        createFinalCells(tableRow, blockResumeResult);
    }

    protected void createFirstPartRegularLine(BlockResumeResult blockResumeResult, HtmlTableRow tableRow) {
        HtmlTableCell firstCell = tableRow.createCell();
        firstCell.setBody(new HtmlText(blockResumeResult.getFirstPresentationName()));
        firstCell.setClasses(getFirstColClass());
    }

    protected String getColoredBar(InquiryResult inquiryResult) {
        StringBuilder sb = new StringBuilder("<div class='");
        sb.append("bar-").append(inquiryResult.getResultClassification().name().toLowerCase());
        sb.append("'><div>&nbsp;</div>");
        return sb.toString();
    }

    protected void createHeader(Object object, final HtmlTableRow headerRow) {
        List<BlockResumeResult> blockResumeResults = (List<BlockResumeResult>) object;
        if (!blockResumeResults.isEmpty()) {
            BlockResumeResult blocksResume = blockResumeResults.iterator().next();

            final Set<InquiryResult> blocksResults = blocksResume.getResultBlocks();

            createFirstPartRegularHeader(headerRow, blocksResume);

            for (InquiryResult inquiryResult : blocksResults) {
                final HtmlTableCell firstGrouptInnerCell = headerRow.createCell(CellType.HEADER);
                firstGrouptInnerCell.setBody(new HtmlText(inquiryResult.getInquiryQuestion().getLabel().toString()));
                firstGrouptInnerCell.setClasses("col-bar");
            }

            createHeaderFinalCells(headerRow);
        }
    }

    protected void createFirstPartRegularHeader(final HtmlTableRow headerRow, BlockResumeResult blocksResume) {
        final HtmlTableCell firstHeaderCell = headerRow.createCell(CellType.HEADER);
        firstHeaderCell.setBody(new HtmlText(blocksResume.getFirstHeaderName()));
        firstHeaderCell.setClasses(getFirstColClass());
    }

    protected void createHeaderFinalCells(final HtmlTableRow headerRow) {
        final HtmlTableCell finalCell = headerRow.createCell(CellType.HEADER);
        finalCell.setClasses("col-actions");
    }

    protected void createFirstCellBeforeCommonBody(int rowSpan, BlockResumeResult blockResumeResult, HtmlTableRow tableRow) {
    }

    protected String getFirstColClass() {
        return "col-first";
    }
}
