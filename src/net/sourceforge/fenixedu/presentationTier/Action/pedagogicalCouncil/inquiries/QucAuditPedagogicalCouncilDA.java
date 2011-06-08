package net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.inquiries;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.inquiries.AuditSelectPersonsECBean;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.CompetenceCourseResultsResume;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.CurricularCourseResumeResult;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.ExecutionCourse1st2ndCycleSearchBean;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.inquiries.ExecutionCourseAudit;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResult;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResultType;
import net.sourceforge.fenixedu.domain.inquiries.ResultPersonCategory;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

@Mapping(path = "/qucAudit", module = "pedagogicalCouncil")
@Forwards( {
	@Forward(name = "showExecutionCoursesForAudit", path = "/pedagogicalCouncil/inquiries/searchExecutionCourseToAudit.jsp"),
	@Forward(name = "selectPersons", path = "/pedagogicalCouncil/inquiries/selectPersons.jsp"),
	@Forward(name = "viewProcessDetails", path = "/pedagogicalCouncil/inquiries/viewProcessDetails.jsp") })
public class QucAuditPedagogicalCouncilDA extends FenixDispatchAction {

    public ActionForward searchExecutionCourse(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	ExecutionCourse1st2ndCycleSearchBean executionCourseSearchBean = getRenderedObject();
	ExecutionSemester executionSemester = null;
	Map<ExecutionCourse, Set<ExecutionDegree>> coursesToAuditAndObserve = null;
	if (executionCourseSearchBean == null) {
	    executionCourseSearchBean = new ExecutionCourse1st2ndCycleSearchBean();
	    executionSemester = ExecutionSemester.readActualExecutionSemester().getPreviousExecutionPeriod();
	    executionCourseSearchBean.setExecutionPeriod(executionSemester);
	    coursesToAuditAndObserve = getCoursesToAuditAndObserve(executionSemester);
	} else {
	    executionSemester = executionCourseSearchBean.getExecutionPeriod();
	    final Collection<ExecutionCourse> executionCourses = executionCourseSearchBean.search();
	    if (executionCourses != null) {
		request.setAttribute("executionCourses", executionCourses);
	    } else {
		coursesToAuditAndObserve = getCoursesToAuditAndObserve(executionSemester);
	    }
	}

	List<ExecutionCourseAudit> executionCoursesAudits = getExecutionCoursesAudits(executionSemester);

	request.setAttribute("executionCoursesAudits", executionCoursesAudits);
	request.setAttribute("coursesToAuditAndObserve", coursesToAuditAndObserve);
	request.setAttribute("executionCourseSearchBean", executionCourseSearchBean);
	return mapping.findForward("showExecutionCoursesForAudit");
    }

    private List<ExecutionCourseAudit> getExecutionCoursesAudits(ExecutionSemester executionSemester) {
	List<ExecutionCourseAudit> result = new ArrayList<ExecutionCourseAudit>();
	for (ExecutionCourseAudit executionCourseAudit : RootDomainObject.getInstance().getExecutionCourseAudits()) {
	    if (executionCourseAudit.getExecutionCourse().getExecutionPeriod() == executionSemester) {
		result.add(executionCourseAudit);
	    }
	}
	return result;
    }

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
		if (courseResumeResult.getResultBlocks().size() > 1) {
		    if (competenceCourseResultsResume == null) {
			competenceCourseResultsResume = new CompetenceCourseResultsResume(competenceCourse);
			competenceCoursesToAudit.add(competenceCourseResultsResume);
		    }
		    competenceCourseResultsResume.addCurricularCourseResumeResult(courseResumeResult);
		}
	    }
	}

	request.setAttribute("executionCourseAudit", executionCourseAudit);
	request.setAttribute("competenceCoursesToAudit", competenceCoursesToAudit);
	return mapping.findForward("viewProcessDetails");
    }

    public ActionForward prepareSelectPersons(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	ExecutionCourse executionCourse = null;
	if (request.getAttribute("executionCourse") != null) {
	    executionCourse = (ExecutionCourse) request.getAttribute("executionCourse");
	} else {
	    String executionCourseOID = request.getParameter("executionCourseOID");
	    executionCourse = AbstractDomainObject.fromExternalId(executionCourseOID);
	}

	ExecutionCourseAudit executionCourseAudit = executionCourse.getExecutionCourseAudit();

	AuditSelectPersonsECBean auditProcessBean = null;
	if (executionCourseAudit == null) {
	    auditProcessBean = new AuditSelectPersonsECBean(executionCourse);
	} else {
	    auditProcessBean = new AuditSelectPersonsECBean(executionCourseAudit);
	}

	request.setAttribute("auditProcessBean", auditProcessBean);
	return mapping.findForward("selectPersons");
    }

    public ActionForward selectPersons(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	AuditSelectPersonsECBean auditProcessBean = getRenderedObject("auditProcessBean");

	try {
	    auditProcessBean.savePersons();
	} catch (DomainException e) {
	    addActionMessage(request, e.getMessage());
	    RenderUtils.invalidateViewState();
	    request.setAttribute("auditProcessBean", auditProcessBean);
	    return mapping.findForward("selectPersons");
	}

	request.setAttribute("success", "true");
	request.setAttribute("executionCourse", auditProcessBean.getExecutionCourse());
	return prepareSelectPersons(mapping, actionForm, request, response);
    }

    private Map<ExecutionCourse, Set<ExecutionDegree>> getCoursesToAuditAndObserve(ExecutionSemester executionSemester) {
	Map<ExecutionCourse, Set<ExecutionDegree>> coursesToAuditAndObserve = new HashMap<ExecutionCourse, Set<ExecutionDegree>>();
	for (InquiryResult inquiryResult : executionSemester.getInquiryResults()) {
	    if (InquiryResultType.AUDIT.equals(inquiryResult.getResultType())) {
		Set<ExecutionDegree> executionDegrees = coursesToAuditAndObserve.get(inquiryResult.getExecutionCourse());
		if (executionDegrees == null) {
		    executionDegrees = new HashSet<ExecutionDegree>();
		}
		executionDegrees.add(inquiryResult.getExecutionDegree());
		coursesToAuditAndObserve.put(inquiryResult.getExecutionCourse(), executionDegrees);
	    }
	}
	return coursesToAuditAndObserve;
    }
}
