package net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.inquiries;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.inquiries.CompetenceCourseResultsResume;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.CurricularCourseResumeResult;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.inquiries.ExecutionCourseAudit;
import net.sourceforge.fenixedu.domain.inquiries.ResultPersonCategory;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

@Mapping(path = "/auditResult", module = "pedagogicalCouncil")
@Forwards({ @Forward(name = "viewProcessDetails", path = "/pedagogicalCouncil/inquiries/viewProcessDetailsNoAction.jsp") })
public class ViewQucAuditProcessDA extends FenixDispatchAction {

	public ActionForward viewProcessDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		String executionCourseAuditOID = (String) getFromRequest(request, "executionCourseAuditOID");
		ExecutionCourseAudit executionCourseAudit = AbstractDomainObject.fromExternalId(executionCourseAuditOID);

		List<CompetenceCourseResultsResume> competenceCoursesToAudit = getCompetenceCourseResultsBeans(executionCourseAudit);

		request.setAttribute("executionCourseAudit", executionCourseAudit);
		request.setAttribute("competenceCoursesToAudit", competenceCoursesToAudit);
		return mapping.findForward("viewProcessDetails");
	}

	protected List<CompetenceCourseResultsResume> getCompetenceCourseResultsBeans(ExecutionCourseAudit executionCourseAudit) {
		List<CompetenceCourseResultsResume> competenceCoursesToAudit = new ArrayList<CompetenceCourseResultsResume>();
		for (CompetenceCourse competenceCourse : executionCourseAudit.getExecutionCourse().getCompetenceCourses()) {
			CompetenceCourseResultsResume competenceCourseResultsResume = null;
			for (ExecutionDegree executionDegree : executionCourseAudit.getExecutionCourse().getExecutionDegrees()) {
				CurricularCourseResumeResult courseResumeResult =
						new CurricularCourseResumeResult(executionCourseAudit.getExecutionCourse(), executionDegree,
								"label.inquiry.execution", executionDegree.getDegree().getSigla(), AccessControl.getPerson(),
								ResultPersonCategory.DEPARTMENT_PRESIDENT, false, true, true, true, true);
				if (courseResumeResult.getResultBlocks().size() > 1) {
					if (competenceCourseResultsResume == null) {
						competenceCourseResultsResume = new CompetenceCourseResultsResume(competenceCourse);
						competenceCoursesToAudit.add(competenceCourseResultsResume);
					}
					competenceCourseResultsResume.addCurricularCourseResumeResult(courseResumeResult);
				}
			}
		}
		return competenceCoursesToAudit;
	}
}
