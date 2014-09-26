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

import net.sourceforge.fenixedu.dataTransferObject.inquiries.BlockResumeResult;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.RegentTeacherResultsResume;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.TeacherShiftTypeGroupsResumeResult;
import pt.ist.fenixWebFramework.renderers.components.HtmlInlineContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlLink;
import pt.ist.fenixWebFramework.renderers.components.HtmlTable;
import pt.ist.fenixWebFramework.renderers.components.HtmlTableCell;
import pt.ist.fenixWebFramework.renderers.components.HtmlTableCell.CellType;
import pt.ist.fenixWebFramework.renderers.components.HtmlTableRow;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;

public class InquiryRegentTeachersResumeRenderer extends InquiryTeacherShiftTypeResumeRenderer {

    @Override
    protected void buildTableBody(final HtmlTable mainTable, Object object, int rowSpan) {
        List<RegentTeacherResultsResume> regentTeacherResultsResumeList = (List<RegentTeacherResultsResume>) object;
        for (RegentTeacherResultsResume regentTeacherResultsResume : regentTeacherResultsResumeList) {
            super.buildTableBody(mainTable, regentTeacherResultsResume.getOrderedTeacherShiftResumes(),
                    regentTeacherResultsResume.getTeacherShiftTypeGroupsResumeResults().size());
        }
    }

    @Override
    protected void createFirstCellBeforeCommonBody(int rowSpan, BlockResumeResult blockResumeResult, HtmlTableRow tableRow) {
        HtmlTableCell teacherCell = tableRow.createCell();
        teacherCell.setRowspan(rowSpan);
        teacherCell.setClasses("col-first");

        HtmlInlineContainer container = new HtmlInlineContainer();
        HtmlText teacherInquiry1 = new HtmlText(blockResumeResult.getPerson().getName() + "<br/>(");
        teacherInquiry1.setEscaped(false);
        container.addChild(teacherInquiry1);

        String teacherParameters = buildParametersForTeacher(blockResumeResult);
        HtmlLink teacherLink = new HtmlLink();
        teacherLink.setModule("/publico");
        teacherLink.setUrl("/viewQUCInquiryAnswers.do?method=showTeacherInquiry&" + teacherParameters);
        teacherLink.setTarget("_blank");
        teacherLink.setText("Relatório de Docência");
        container.addChild(teacherLink);

        HtmlText teacherInquiry = new HtmlText(")");
        container.addChild(teacherInquiry);

        teacherCell.setBody(container);
    }

    private String buildParametersForTeacher(BlockResumeResult blocksResumeResult) {
        TeacherShiftTypeGroupsResumeResult teacherShiftResume = (TeacherShiftTypeGroupsResumeResult) blocksResumeResult;
        StringBuilder builder = new StringBuilder();
        builder.append("professorshipOID=").append(teacherShiftResume.getProfessorship().getExternalId());
        return builder.toString();
    }

    @Override
    protected void createHeader(Object object, final HtmlTableRow headerRow) {
        List<RegentTeacherResultsResume> regentTeacherResultsResumeList = (List<RegentTeacherResultsResume>) object;
        if (!regentTeacherResultsResumeList.isEmpty()) {
            RegentTeacherResultsResume regentTeacherResultsResume = regentTeacherResultsResumeList.iterator().next();

            final HtmlTableCell firstHeaderCell = headerRow.createCell(CellType.HEADER);
            firstHeaderCell.setBody(new HtmlText("Docente"));
            firstHeaderCell.setClasses("col-first");

            super.createHeader(regentTeacherResultsResume.getTeacherShiftTypeGroupsResumeResults(), headerRow);
        }
    }

    @Override
    protected String getFirstColClass() {
        return "col-coursetype";
    }
}
