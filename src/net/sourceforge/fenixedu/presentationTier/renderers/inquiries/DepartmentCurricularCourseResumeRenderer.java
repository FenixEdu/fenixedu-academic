/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.renderers.inquiries;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.inquiries.BlockResumeResult;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.CompetenceCourseResultsResume;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.CurricularCourseResumeResult;
import pt.ist.fenixWebFramework.renderers.components.HtmlTable;
import pt.ist.fenixWebFramework.renderers.components.HtmlTableCell;
import pt.ist.fenixWebFramework.renderers.components.HtmlTableCell.CellType;
import pt.ist.fenixWebFramework.renderers.components.HtmlTableRow;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;

public class DepartmentCurricularCourseResumeRenderer extends InquiryCoordinatorResumeRenderer {

	@Override
	protected void buildTableBody(final HtmlTable mainTable, Object object, int rowSpan) {
		List<CompetenceCourseResultsResume> competenceCourseResultsResumeList = (List<CompetenceCourseResultsResume>) object;
		for (CompetenceCourseResultsResume competenceCourseResultsResume : competenceCourseResultsResumeList) {
			super.buildTableBody(mainTable, competenceCourseResultsResume.getOrderedCurricularCourseResumes(),
					competenceCourseResultsResume.getCurricularCourseResumeResults().size());
		}
	}

	@Override
	protected void createFirstCellBeforeCommonBody(int rowSpan, BlockResumeResult blockResumeResult, HtmlTableRow tableRow) {
		HtmlTableCell competenceCell = tableRow.createCell();
		competenceCell.setRowspan(rowSpan);
		competenceCell.setClasses("col-first");

		CurricularCourseResumeResult curricularCourseResumeResult = (CurricularCourseResumeResult) blockResumeResult;
		competenceCell.setBody(new HtmlText(curricularCourseResumeResult.getExecutionCourse().getName()));
	}

	@Override
	protected void createHeader(Object object, final HtmlTableRow headerRow) {
		List<CompetenceCourseResultsResume> competenceCourseResultsResumeList = (List<CompetenceCourseResultsResume>) object;
		if (!competenceCourseResultsResumeList.isEmpty()) {
			CompetenceCourseResultsResume competenceCourseResultsResume = competenceCourseResultsResumeList.get(0);

			final HtmlTableCell firstHeaderCell = headerRow.createCell(CellType.HEADER);
			firstHeaderCell.setBody(new HtmlText("Competência"));
			firstHeaderCell.setClasses("col-first");

			super.createHeader(competenceCourseResultsResume.getCurricularCourseResumeResults(), headerRow);
		}
	}

	//    @Override Mostrar sempre os links, pois só assim se conseguem ver os comentários feitos às perguntas das disciplinas que não foram para auditoria
	//    protected void createCommentLink(CurricularCourseResumeResult courseResumeResult, HtmlInlineContainer container,
	//	    ResultClassification forAudit) {
	//	if (forAudit != null) {
	//	super.createCommentLink(courseResumeResult, container, forAudit);
	//	}
	//    }

	@Override
	protected String getFirstColClass() {
		return "col-degree";
	}
}
