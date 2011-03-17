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
import pt.ist.fenixWebFramework.renderers.components.HtmlTableRow;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;
import pt.ist.fenixWebFramework.renderers.components.HtmlTableCell.CellType;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;

/**
 * @author - Ricardo Rodrigues (ricardo.rodrigues@ist.utl.pt)
 * 
 */
public abstract class InquiryBlocksResumeRenderer extends OutputRenderer {

    private boolean showMandatoryQuestions = true;

    public void setShowMandatoryQuestions(boolean showMandatoryQuestions) {
	this.showMandatoryQuestions = showMandatoryQuestions;
    }

    public boolean isShowMandatoryQuestions() {
	return showMandatoryQuestions;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
	return new InquiryQuestionLayout(object);
    }

    protected abstract void createLinksCell(HtmlTableRow tableRow, BlockResumeResult blockResumeResult);

    protected class InquiryQuestionLayout extends Layout {

	public InquiryQuestionLayout(Object object) {
	}

	@Override
	public HtmlComponent createComponent(Object object, Class type) {
	    HtmlBlockContainer blockContainer = new HtmlBlockContainer();
	    final HtmlTable mainTable = new HtmlTable();
	    blockContainer.addChild(mainTable);
	    mainTable.setClasses("tstyle1 thlight tdcenter");
	    mainTable.setStyle("margin-bottom: 0;");
	    List<BlockResumeResult> blocksResume = (List<BlockResumeResult>) object;

	    if (!blocksResume.isEmpty()) {
		createHeader(blocksResume.get(0), mainTable);
	    }
	    for (BlockResumeResult blockResumeResult : blocksResume) {
		Set<InquiryResult> blocksResults = blockResumeResult.getResultBlocks();

		HtmlTableRow tableRow = mainTable.createRow();
		HtmlTableCell firstCell = tableRow.createCell();
		firstCell.setBody(new HtmlText(blockResumeResult.getFirstPresentationName()));
		firstCell.setClasses("col-first");

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

		createLinksCell(tableRow, blockResumeResult);
	    }

	    return blockContainer;
	}

	private String getColoredBar(InquiryResult inquiryResult) {
	    StringBuilder sb = new StringBuilder("<div class='");
	    sb.append("bar-").append(inquiryResult.getResultClassification().name().toLowerCase());
	    sb.append("'><div>&nbsp;</div>");
	    return sb.toString();
	}

	private void createHeader(BlockResumeResult blocksResume, final HtmlTable mainTable) {
	    final Set<InquiryResult> blocksResults = blocksResume.getResultBlocks();
	    final HtmlTableRow headerRow = mainTable.createRow();

	    final HtmlTableCell firstHeaderCell = headerRow.createCell(CellType.HEADER);
	    firstHeaderCell.setBody(new HtmlText(blocksResume.getFirstHeaderName()));
	    firstHeaderCell.setClasses("col-first");

	    for (InquiryResult inquiryResult : blocksResults) {
		final HtmlTableCell firstGrouptInnerCell = headerRow.createCell(CellType.HEADER);
		firstGrouptInnerCell.setBody(new HtmlText(inquiryResult.getInquiryQuestion().getLabel().toString()));
		firstGrouptInnerCell.setClasses("col-bar");
	    }

	    final HtmlTableCell finalCell = headerRow.createCell(CellType.HEADER);
	    finalCell.setClasses("col-actions");
	}
    }
}
