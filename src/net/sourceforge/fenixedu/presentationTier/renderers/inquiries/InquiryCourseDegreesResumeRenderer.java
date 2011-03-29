/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.renderers.inquiries;

import net.sourceforge.fenixedu.dataTransferObject.inquiries.BlockResumeResult;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.CurricularCourseResumeResult;
import pt.ist.fenixWebFramework.renderers.components.HtmlInlineContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlLink;
import pt.ist.fenixWebFramework.renderers.components.HtmlTableCell;
import pt.ist.fenixWebFramework.renderers.components.HtmlTableRow;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;

/**
 * @author - Ricardo Rodrigues (ricardo.rodrigues@ist.utl.pt)
 * 
 */
public class InquiryCourseDegreesResumeRenderer extends InquiryBlocksResumeRenderer {

    protected void createLinksCell(HtmlTableRow tableRow, BlockResumeResult blockResumeResult) {

	HtmlTableCell linksCell = tableRow.createCell();
	String resultsParameters = buildParametersForResults(blockResumeResult);

	HtmlInlineContainer container = new HtmlInlineContainer();

	HtmlLink resultsLink = new HtmlLink();
	resultsLink.setModule("/publico");
	resultsLink.setUrl("/viewCourseResults.do?" + resultsParameters);
	resultsLink.setTarget("_blank");
	resultsLink.setText("Resultados");

	String delegateParameters = buildParametersForDelegate(blockResumeResult);
	HtmlLink delegateLink = new HtmlLink();
	delegateLink.setModuleRelative(true);
	delegateLink.setUrl("/teachingInquiry.do?method=showDelegateInquiry" + delegateParameters);
	delegateLink.setText("Relatório do Delegado");

	container.addChild(resultsLink);
	container.addChild(new HtmlText(" | "));
	container.addChild(delegateLink);

	linksCell.setBody(container);
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

    private String buildParametersForDelegate(BlockResumeResult blocksResumeResult) {
	CurricularCourseResumeResult courseResume = (CurricularCourseResumeResult) blocksResumeResult;
	StringBuilder builder = new StringBuilder();
	builder.append("&executionCourseOID=").append(courseResume.getExecutionCourse().getExternalId());
	builder.append("&executionDegreeOID=").append(courseResume.getExecutionDegree().getExternalId());
	return builder.toString();
    }

}
