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

import net.sourceforge.fenixedu.dataTransferObject.inquiries.GroupResultsSummaryBean;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.QuestionResultsSummaryBean;
import net.sourceforge.fenixedu.domain.inquiries.InquiryQuestion;
import pt.ist.fenixWebFramework.renderers.OutputRenderer;
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
                String divText =
                        "<div class=\"bar-" + summaryBean.getResultClassification().toString().toLowerCase() + "\">&nbsp;</div>";
                HtmlText divComponent = new HtmlText(divText);
                divComponent.setEscaped(false);
                resultCell.setBody(divComponent);
                HtmlTableCell labelCell = row.createCell(CellType.HEADER);
                String tooltip = getTooltip(summaryBean.getInquiryQuestion());
                HtmlText htmlText = new HtmlText(summaryBean.getInquiryQuestion().getLabel().toString() + tooltip);
                htmlText.setEscaped(false);
                labelCell.setBody(htmlText);
            }
            return mainTable;
        }

        private String getTooltip(InquiryQuestion inquiryQuestion) {
            if (inquiryQuestion.getToolTip() != null) {
                StringBuilder sb = new StringBuilder(" <a href=\"#\" class=\"helpleft\">[?]<span>");
                sb.append(inquiryQuestion.getToolTip().toString()).append("</span></a>");
                return sb.toString();
            }
            return "";
        }

        @Override
        public String getClasses() {
            return "graph general-results";
        }

        @Override
        public String getStyle() {
            return "margin-bottom: 0;";
        }
    }
}
