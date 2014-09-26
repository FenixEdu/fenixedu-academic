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

    @Override
    protected void createFinalCells(HtmlTableRow tableRow, BlockResumeResult blockResumeResult) {

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
        delegateLink.setModule("/publico");
        delegateLink.setUrl("/viewQUCInquiryAnswers.do?method=showDelegateInquiry" + delegateParameters);
        delegateLink.setTarget("_blank");
        delegateLink.setText("Relatório do Delegado");

        container.addChild(resultsLink);
        container.addChild(new HtmlText("&nbsp;|&nbsp;", false));
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
