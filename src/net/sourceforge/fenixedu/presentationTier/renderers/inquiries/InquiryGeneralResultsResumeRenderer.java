/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.renderers.inquiries;

import net.sourceforge.fenixedu.dataTransferObject.inquiries.GroupResultsSummaryBean;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.QuestionResultsSummaryBean;
import pt.ist.fenixWebFramework.renderers.OutputRenderer;
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
public class InquiryGeneralResultsResumeRenderer extends OutputRenderer {

    @Override
    protected Layout getLayout(Object object, Class type) {
	return new InquiryGeneralGroupLayout(object);
    }

    private class InquiryGeneralGroupLayout extends Layout {

	public InquiryGeneralGroupLayout(Object object) {
	}

	@Override
	public HtmlComponent createComponent(Object object, Class type) {
	    GroupResultsSummaryBean groupResultsSummaryBean = (GroupResultsSummaryBean) object;

	    final HtmlTable mainTable = new HtmlTable();
	    for (QuestionResultsSummaryBean summaryBean : groupResultsSummaryBean.getQuestionsResults()) {
		HtmlTableRow row = mainTable.createRow();
		HtmlTableCell resultCell = row.createCell();
		String divText = "<div class=\"bar-" + summaryBean.getResultClassification().toString().toLowerCase()
			+ "\">&nbsp;</div>";
		HtmlText divComponent = new HtmlText(divText);
		divComponent.setEscaped(false);
		resultCell.setBody(divComponent);
		HtmlTableCell labelCell = row.createCell(CellType.HEADER);
		labelCell.setBody(new HtmlText(summaryBean.getInquiryQuestion().getLabel().toString()));
	    }

	    return mainTable;
	}

	@Override
	public String getClasses() {
	    return "graph";
	}
    }
}
