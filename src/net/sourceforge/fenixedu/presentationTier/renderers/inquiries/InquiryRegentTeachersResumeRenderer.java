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
import pt.ist.fenixWebFramework.renderers.components.HtmlTableRow;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;
import pt.ist.fenixWebFramework.renderers.components.HtmlTableCell.CellType;

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
    protected void createTeacherCell(int rowSpan, BlockResumeResult blockResumeResult, HtmlTableRow tableRow) {
	HtmlTableCell teacherCell = tableRow.createCell();
	teacherCell.setRowspan(rowSpan);

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
	    RegentTeacherResultsResume regentTeacherResultsResume = regentTeacherResultsResumeList.get(0);

	    final HtmlTableCell firstHeaderCell = headerRow.createCell(CellType.HEADER);
	    firstHeaderCell.setBody(new HtmlText("Docente"));
	    firstHeaderCell.setClasses("col-first");

	    super.createHeader(regentTeacherResultsResume.getTeacherShiftTypeGroupsResumeResults(), headerRow);
	}
    }
}
