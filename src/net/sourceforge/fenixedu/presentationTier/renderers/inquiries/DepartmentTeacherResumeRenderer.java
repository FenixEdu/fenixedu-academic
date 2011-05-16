/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.renderers.inquiries;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.inquiries.BlockResumeResult;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.DepartmentTeacherResultsResume;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.TeacherShiftTypeGroupsResumeResult;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResult;
import pt.ist.fenixWebFramework.renderers.components.HtmlLink;
import pt.ist.fenixWebFramework.renderers.components.HtmlTable;
import pt.ist.fenixWebFramework.renderers.components.HtmlTableCell;
import pt.ist.fenixWebFramework.renderers.components.HtmlTableRow;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;
import pt.ist.fenixWebFramework.renderers.components.HtmlTableCell.CellType;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

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
	for (TeacherShiftTypeGroupsResumeResult groupsResumeResult : departmentTeacherResultsResume
		.getTeacherShiftTypeGroupsResumeResults()) {
	    if (groupsResumeResult.getProfessorship().hasResultsToImprove()) {
		showCommentLink = true;
		break;
	    }
	}

	HtmlTableCell actionCell = tableRow.createCell();
	actionCell.setClasses("col-action");
	actionCell.setRowspan(departmentTeacherResultsResume.getTeacherShiftTypeGroupsResumeResults().size());

	if (showCommentLink) {
	    ExecutionSemester executionSemester = departmentTeacherResultsResume.getTeacherShiftTypeGroupsResumeResults().get(0)
		    .getProfessorship().getExecutionCourse().getExecutionPeriod();

	    if (teacher.hasQucGlobalCommentsMadeBy(departmentTeacherResultsResume.getPresident(), executionSemester,
		    departmentTeacherResultsResume.getPersonCategory())) {
		commentLinkText = RenderUtils.getResourceString("INQUIRIES_RESOURCES", "link.inquiry.viewComment");
	    }

	    String fillInParameters = buildFillInParameters(teacher, executionSemester, departmentTeacherResultsResume
		    .isBackToResume());
	    HtmlLink commentLink = new HtmlLink();
	    commentLink.setUrl("/viewQucResults.do?method=showTeacherResultsAndComments" + fillInParameters);
	    commentLink.setText(commentLinkText);

	    actionCell.setBody(commentLink);
	} else {
	    actionCell.setBody(new HtmlText(""));
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

	TeacherShiftTypeGroupsResumeResult teacherShiftTypeGroupsResumeResult = (TeacherShiftTypeGroupsResumeResult) blockResumeResult;
	StringBuilder executionCourseIdentification = new StringBuilder(teacherShiftTypeGroupsResumeResult.getProfessorship()
		.getExecutionCourse().getName());
	executionCourseIdentification.append(" <span class=\"color888\">(");
	for (Iterator<ExecutionDegree> iterator = teacherShiftTypeGroupsResumeResult.getProfessorship().getExecutionCourse()
		.getExecutionDegrees().iterator(); iterator.hasNext();) {
	    ExecutionDegree executionDegree = iterator.next();
	    executionCourseIdentification.append(executionDegree.getDegree().getSigla());
	    if (iterator.hasNext()) {
		executionCourseIdentification.append(", ");
	    }
	}
	executionCourseIdentification.append(")</span>");

	competenceCell.setBody(new HtmlText(executionCourseIdentification.toString(), false));
    }

    @Override
    protected void createHeader(Object object, final HtmlTableRow headerRow) {
	List<DepartmentTeacherResultsResume> departmentTeacherResultsResumeList = (List<DepartmentTeacherResultsResume>) object;
	if (!departmentTeacherResultsResumeList.isEmpty()) {
	    DepartmentTeacherResultsResume departmentTeacherResultsResume = departmentTeacherResultsResumeList.get(0);

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

	TeacherShiftTypeGroupsResumeResult teacherShiftTypeGroupsResumeResult = (TeacherShiftTypeGroupsResumeResult) blockResumeResult;
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
}
