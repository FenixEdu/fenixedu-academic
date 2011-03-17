/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.renderers.inquiries;

import net.sourceforge.fenixedu.dataTransferObject.inquiries.BlockResumeResult;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.CurricularCourseResumeResult;
import pt.ist.fenixWebFramework.renderers.components.HtmlLink;
import pt.ist.fenixWebFramework.renderers.components.HtmlTableCell;
import pt.ist.fenixWebFramework.renderers.components.HtmlTableRow;

/**
 * @author - Ricardo Rodrigues (ricardo.rodrigues@ist.utl.pt)
 * 
 */
public class InquiryCourseDegreesResumeRenderer extends InquiryBlocksResumeRenderer {

    protected void createLinksCell(HtmlTableRow tableRow, BlockResumeResult blockResumeResult) {

	HtmlTableCell linksCell = tableRow.createCell();
	String resultsParameters = buildParametersForResults(blockResumeResult);

	HtmlLink link = new HtmlLink();
	link.setModule("/publico");
	link.setUrl("/viewCourseResults.do?" + resultsParameters);
	link.setTarget("_blank");
	link.setText("Resultados");

	linksCell.setBody(link);
	linksCell.setClasses("col-actions");
    }

    private String buildParametersForResults(BlockResumeResult blocksResumeResult) {
	CurricularCourseResumeResult courseResume = (CurricularCourseResumeResult) blocksResumeResult;
	StringBuilder builder = new StringBuilder();
	builder.append("degreeCurricularPlanOID=").append(
		courseResume.getExecutionDegree().getDegreeCurricularPlan().getExternalId());
	builder.append("&executionCourseOID=").append(courseResume.getExecutionCourse().getExternalId());
	return builder.toString();
    }

}
