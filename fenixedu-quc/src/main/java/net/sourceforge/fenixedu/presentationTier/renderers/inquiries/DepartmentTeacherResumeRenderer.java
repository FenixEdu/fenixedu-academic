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
package org.fenixedu.academic.ui.renderers.inquiries;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import org.fenixedu.academic.dto.inquiries.BlockResumeResult;
import org.fenixedu.academic.dto.inquiries.CurricularCourseResumeResult;
import org.fenixedu.academic.dto.inquiries.DepartmentTeacherResultsResume;
import org.fenixedu.academic.dto.inquiries.TeacherShiftTypeGroupsResumeResult;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.inquiries.InquiryGlobalComment;
import org.fenixedu.academic.domain.inquiries.InquiryResult;
import org.fenixedu.academic.domain.inquiries.InquiryResultComment;
import org.fenixedu.academic.domain.inquiries.ResultPersonCategory;
import pt.ist.fenixWebFramework.renderers.components.HtmlInlineContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlLink;
import pt.ist.fenixWebFramework.renderers.components.HtmlLinkWithPreprendedComment;
import pt.ist.fenixWebFramework.renderers.components.HtmlTable;
import pt.ist.fenixWebFramework.renderers.components.HtmlTableCell;
import pt.ist.fenixWebFramework.renderers.components.HtmlTableCell.CellType;
import pt.ist.fenixWebFramework.renderers.components.HtmlTableRow;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter;

public class DepartmentTeacherResumeRenderer extends InquiryBlocksResumeRenderer {

    @Override
    protected void buildTableBody(final HtmlTable mainTable, Object object, int rowSpan) {
        List<DepartmentTeacherResultsResume> departmentTeacherResultsResumeList = (List<DepartmentTeacherResultsResume>) object;
        for (DepartmentTeacherResultsResume departmentTeacherResultsResume : departmentTeacherResultsResumeList) {
            boolean createTeacherCell = true;
            for (List<TeacherShiftTypeGroupsResumeResult> teachersResumeUCList : departmentTeacherResultsResume
                    .getTeacherShiftResumesByUC()) {
                boolean createFirstCellBeforeCommonBody = true;
                for (BlockResumeResult blockResumeResult : teachersResumeUCList) {
                    Set<InquiryResult> blocksResults = blockResumeResult.getResultBlocks();

                    HtmlTableRow tableRow = mainTable.createRow();
                    if (createTeacherCell) {
                        createTeacherCell(departmentTeacherResultsResume, tableRow);
                    }
                    if (createFirstCellBeforeCommonBody) {
                        createFirstCellBeforeCommonBody(teachersResumeUCList.size(), blockResumeResult, tableRow);
                        createFirstCellBeforeCommonBody = false;
                    }
                    createRegularLine(blockResumeResult, blocksResults, tableRow);
                    if (createTeacherCell) {
                        createTeacherActionCell(departmentTeacherResultsResume, tableRow);
                        createTeacherCell = false;
                    }
                }

            }
        }
    }

    private void createTeacherCell(DepartmentTeacherResultsResume departmentTeacherResultsResume, HtmlTableRow tableRow) {
        HtmlTableCell competenceCell = tableRow.createCell();
        competenceCell.setRowspan(departmentTeacherResultsResume.getTeacherShiftTypeGroupsResumeResults().size());
        competenceCell.setClasses("col-first");
        competenceCell.setBody(new HtmlText(departmentTeacherResultsResume.getTeacher().getPerson().getName()));
    }

    private void createTeacherActionCell(DepartmentTeacherResultsResume departmentTeacherResultsResume, HtmlTableRow tableRow) {
        String commentLinkText = RenderUtils.getResourceString("INQUIRIES_RESOURCES", "link.inquiry.comment");
        Person teacher = departmentTeacherResultsResume.getTeacher().getPerson();

        boolean showCommentLink = false;
        if (!departmentTeacherResultsResume.isShowAllComments()) {
            for (TeacherShiftTypeGroupsResumeResult groupsResumeResult : departmentTeacherResultsResume
                    .getTeacherShiftTypeGroupsResumeResults()) {
                if (InquiryResult.hasResultsToImprove(groupsResumeResult.getProfessorship())) {
                    showCommentLink = true;
                    break;
                }
            }
        }

        HtmlTableCell actionCell = tableRow.createCell();
        actionCell.setClasses("col-action");
        actionCell.setRowspan(departmentTeacherResultsResume.getTeacherShiftTypeGroupsResumeResults().size());

        ExecutionSemester executionSemester =
                departmentTeacherResultsResume.getTeacherShiftTypeGroupsResumeResults().iterator().next().getProfessorship()
                        .getExecutionCourse().getExecutionPeriod();
        String fillInParameters =
                buildFillInParameters(teacher, executionSemester, departmentTeacherResultsResume.isBackToResume());
        if (showCommentLink) {
            if (DepartmentTeacherResumeRenderer.hasQucGlobalCommentsMadeBy(teacher, departmentTeacherResultsResume.getPresident(), executionSemester, departmentTeacherResultsResume.getPersonCategory())) {
                commentLinkText = RenderUtils.getResourceString("INQUIRIES_RESOURCES", "link.inquiry.viewComment");
            }
            HtmlLink commentLink = new HtmlLink();
            commentLink.setUrl("/viewQucResults.do?method=showTeacherResultsAndComments&showComment=true&showAllComments=false"
                    + fillInParameters);
            commentLink.setText(commentLinkText);

            actionCell.setBody(commentLink);
        } else {
            HtmlLink commentLink = new HtmlLink();
            commentLink.setUrl("/viewQucResults.do?method=showTeacherResultsAndComments&showComment=false&showAllComments="
                    + departmentTeacherResultsResume.isShowAllComments() + fillInParameters);
            commentLink.setText(RenderUtils.getResourceString("INQUIRIES_RESOURCES", "link.inquiry.viewResults"));
            actionCell.setBody(commentLink);
        }
    }

    private String buildFillInParameters(Person person, ExecutionSemester executionSemester, boolean backToResume) {
        StringBuilder builder = new StringBuilder();
        builder.append("&personOID=").append(person.getExternalId());
        builder.append("&executionSemesterOID=").append(executionSemester.getExternalId());
        builder.append("&backToResume=").append(backToResume);
        return builder.toString();
    }

    @Override
    protected void createFirstCellBeforeCommonBody(int rowSpan, BlockResumeResult blockResumeResult, HtmlTableRow tableRow) {
        HtmlTableCell competenceCell = tableRow.createCell();
        competenceCell.setRowspan(rowSpan);
        competenceCell.setClasses("col-course");

        TeacherShiftTypeGroupsResumeResult teacherShiftTypeGroupsResumeResult =
                (TeacherShiftTypeGroupsResumeResult) blockResumeResult;
        ExecutionCourse executionCourse = teacherShiftTypeGroupsResumeResult.getProfessorship().getExecutionCourse();

        HtmlInlineContainer inlineContainer = new HtmlInlineContainer();
        inlineContainer.addChild(new HtmlText(executionCourse.getName() + " <span class=\"color888\">(", false));

        for (Iterator<ExecutionDegree> iterator =
                teacherShiftTypeGroupsResumeResult.getProfessorship().getExecutionCourse().getExecutionDegrees().iterator(); iterator
                .hasNext();) {
            ExecutionDegree executionDegree = iterator.next();
            HtmlLink ucResultsLink = new HtmlLink();
            ucResultsLink.setUrl("/viewCourseResults.do?executionCourseOID=" + executionCourse.getExternalId()
                    + "&degreeCurricularPlanOID=" + executionDegree.getDegreeCurricularPlan().getExternalId());
            ucResultsLink.setText(executionDegree.getDegree().getSigla());
            ucResultsLink.setModule("/publico");
            ucResultsLink.setTarget("_blank");
            inlineContainer.addChild(ucResultsLink);

            if (iterator.hasNext()) {
                inlineContainer.addChild(new HtmlText(", "));
            }
        }
        inlineContainer.addChild(new HtmlText(")</span> ", false));

        if (executionCourse.getSiteUrl() != null) {
            HtmlLink ucPublicPageLink = new HtmlLinkWithPreprendedComment(GenericChecksumRewriter.NO_CHECKSUM_PREFIX);
            ucPublicPageLink.setUrl(executionCourse.getSiteUrl());
            ucPublicPageLink.setText("Site");
            ucPublicPageLink.setModule("");
            ucPublicPageLink.setTarget("_blank");
            inlineContainer.addChild(ucPublicPageLink);
        }

        competenceCell.setBody(inlineContainer);
    }

    private String buildParametersForResults(CurricularCourseResumeResult courseResumeResult) {
        StringBuilder builder = new StringBuilder();
        builder.append("degreeCurricularPlanOID=").append(
                courseResumeResult.getExecutionDegree().getDegreeCurricularPlan().getExternalId());
        builder.append("&executionCourseOID=").append(courseResumeResult.getExecutionCourse().getExternalId());
        return builder.toString();
    }

    @Override
    protected void createHeader(Object object, final HtmlTableRow headerRow) {
        List<DepartmentTeacherResultsResume> departmentTeacherResultsResumeList = (List<DepartmentTeacherResultsResume>) object;
        if (!departmentTeacherResultsResumeList.isEmpty()) {
            DepartmentTeacherResultsResume departmentTeacherResultsResume = departmentTeacherResultsResumeList.iterator().next();

            final HtmlTableCell firstHeaderCell = headerRow.createCell(CellType.HEADER);
            firstHeaderCell.setBody(new HtmlText("Docente"));
            firstHeaderCell.setClasses("col-first");

            final HtmlTableCell secondHeaderCell = headerRow.createCell(CellType.HEADER);
            secondHeaderCell.setBody(new HtmlText("Disciplina"));
            secondHeaderCell.setClasses("col-course");

            super.createHeader(departmentTeacherResultsResume.getTeacherShiftTypeGroupsResumeResults(), headerRow);
        }
    }

    @Override
    protected void createFirstPartRegularLine(BlockResumeResult blockResumeResult, HtmlTableRow tableRow) {
        super.createFirstPartRegularLine(blockResumeResult, tableRow);

        TeacherShiftTypeGroupsResumeResult teacherShiftTypeGroupsResumeResult =
                (TeacherShiftTypeGroupsResumeResult) blockResumeResult;
        HtmlTableCell generalClassificationCell = tableRow.createCell();
        generalClassificationCell.setBody(new HtmlText(
                getColoredBar(teacherShiftTypeGroupsResumeResult.getGlobalTeacherResult()), false));
        generalClassificationCell.setClasses("col-bar");
    }

    @Override
    protected void createFirstPartRegularHeader(final HtmlTableRow headerRow, BlockResumeResult blocksResume) {
        super.createFirstPartRegularHeader(headerRow, blocksResume);

        final HtmlTableCell firstHeaderCell = headerRow.createCell(CellType.HEADER);
        firstHeaderCell.setBody(new HtmlText("Geral"));
        firstHeaderCell.setClasses("col-bar");
    }

    @Override
    protected String getFirstColClass() {
        return "col-type";
    }

    @Override
    protected void createFinalCells(HtmlTableRow tableRow, BlockResumeResult blockResumeResult) {
    }

    public static boolean hasQucGlobalCommentsMadeBy(Person person, Person commenter, ExecutionSemester executionSemester,
            ResultPersonCategory personCategory) {
        final InquiryGlobalComment globalComment = InquiryGlobalComment.getInquiryGlobalComment(person, executionSemester);
        if (globalComment != null) {
            for (final InquiryResultComment resultComment : globalComment.getInquiryResultCommentsSet()) {
                if (resultComment.getPerson() == commenter && personCategory.equals(resultComment.getPersonCategory())
                        && !StringUtils.isEmpty(resultComment.getComment())) {
                    return true;
                }
            }
        }
        return false;
    }
}
