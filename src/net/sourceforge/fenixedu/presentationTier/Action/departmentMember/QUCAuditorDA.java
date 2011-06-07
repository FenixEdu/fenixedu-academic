package net.sourceforge.fenixedu.presentationTier.Action.departmentMember;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.VariantBean;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.AuditProcessBean;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.CompetenceCourseResultsResume;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.CurricularCourseResumeResult;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.inquiries.ExecutionCourseAudit;
import net.sourceforge.fenixedu.domain.inquiries.ResultPersonCategory;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public abstract class QUCAuditorDA extends FenixDispatchAction {

    public ActionForward showAuditProcesses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	VariantBean executionSemesterBean = getRenderedObject("executionSemesterBean");
	ExecutionSemester executionSemester = null;
	if (executionSemesterBean != null) {
	    executionSemester = (ExecutionSemester) executionSemesterBean.getDomainObject();
	} else {
	    executionSemester = ExecutionSemester.readActualExecutionSemester().getPreviousExecutionPeriod();
	    executionSemesterBean = new VariantBean();
	    executionSemesterBean.setDomainObject(executionSemester);
	}

	request.setAttribute("executionCoursesAudits", getExecutionCoursesAudit(executionSemester));
	request.setAttribute("executionSemesterBean", executionSemesterBean);
	return mapping.findForward("viewAuditProcesses");
    }

    protected abstract List<ExecutionCourseAudit> getExecutionCoursesAudit(ExecutionSemester executionSemester);

    public ActionForward viewProcessDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	String executionCourseAuditOID = (String) getFromRequest(request, "executionCourseAuditOID");
	ExecutionCourseAudit executionCourseAudit = AbstractDomainObject.fromExternalId(executionCourseAuditOID);

	List<CompetenceCourseResultsResume> competenceCoursesToAudit = new ArrayList<CompetenceCourseResultsResume>();
	for (CompetenceCourse competenceCourse : executionCourseAudit.getExecutionCourse().getCompetenceCourses()) {
	    CompetenceCourseResultsResume competenceCourseResultsResume = null;
	    for (ExecutionDegree executionDegree : executionCourseAudit.getExecutionCourse().getExecutionDegrees()) {
		CurricularCourseResumeResult courseResumeResult = new CurricularCourseResumeResult(executionCourseAudit
			.getExecutionCourse(), executionDegree, "label.inquiry.execution",
			executionDegree.getDegree().getSigla(), AccessControl.getPerson(),
			ResultPersonCategory.DEPARTMENT_PRESIDENT, false, true, true, true, true);
		if (competenceCourseResultsResume == null) {
		    competenceCourseResultsResume = new CompetenceCourseResultsResume(competenceCourse);
		    competenceCoursesToAudit.add(competenceCourseResultsResume);
		}
		competenceCourseResultsResume.addCurricularCourseResumeResult(courseResumeResult);
	    }
	}

	request.setAttribute("auditProcessBean", new AuditProcessBean(executionCourseAudit));
	request.setAttribute("competenceCoursesToAudit", competenceCoursesToAudit);
	request.setAttribute("approvedBySelf", getApprovedSelf());
	request.setAttribute("approvedByOther", getApprovedOther());
	return mapping.findForward("viewProcessDetails");
    }

    protected abstract String getApprovedSelf();

    protected abstract String getApprovedOther();

    public ActionForward editProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	AuditProcessBean auditProcessBean = getRenderedObject("auditProcessBean");
	try {
	    auditProcessBean.saveData(isTeacher());
	} catch (DomainException e) {
	    addActionMessage(request, e.getMessage());
	    RenderUtils.invalidateViewState();
	    request.setAttribute("executionCourseAuditOID", auditProcessBean.getExecutionCourseAudit().getExternalId());
	    return viewProcessDetails(mapping, form, request, response);
	}

	request.setAttribute("executionCourseAuditOID", auditProcessBean.getExecutionCourseAudit().getExternalId());
	request.setAttribute("success", "true");
	return viewProcessDetails(mapping, form, request, response);
    }

    protected abstract boolean isTeacher();
}
