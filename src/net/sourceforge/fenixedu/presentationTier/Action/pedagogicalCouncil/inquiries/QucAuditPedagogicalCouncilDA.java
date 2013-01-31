package net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.inquiries;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.inquiries.AuditSelectPersonsECBean;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.CompetenceCourseResultsResume;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.ExecutionCourseQucAuditSearchBean;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.inquiries.ExecutionCourseAudit;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

@Mapping(path = "/qucAudit", module = "pedagogicalCouncil")
@Forwards({
		@Forward(
				name = "showExecutionCoursesForAudit",
				path = "/pedagogicalCouncil/inquiries/searchExecutionCourseToAudit.jsp",
				tileProperties = @Tile(title = "private.pedagogiccouncil.control.qucaudit")),
		@Forward(name = "selectPersons", path = "/pedagogicalCouncil/inquiries/selectPersons.jsp"),
		@Forward(name = "viewProcessDetails", path = "/pedagogicalCouncil/inquiries/viewProcessDetails.jsp") })
public class QucAuditPedagogicalCouncilDA extends ViewQucAuditProcessDA {

	public ActionForward searchExecutionCourse(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		ExecutionCourseQucAuditSearchBean executionCourseSearchBean = getRenderedObject("executionCourseSearchBean");
		ExecutionSemester executionSemester = null;
		if (executionCourseSearchBean == null) {
			executionCourseSearchBean = new ExecutionCourseQucAuditSearchBean();
			executionSemester = ExecutionSemester.readActualExecutionSemester().getPreviousExecutionPeriod();
			executionCourseSearchBean.setExecutionPeriod(executionSemester);
		} else {
			executionSemester = executionCourseSearchBean.getExecutionPeriod();
		}
		final Collection<ExecutionCourse> executionCourses = executionCourseSearchBean.search();
		if (executionCourses != null) {
			request.setAttribute("executionCourses", executionCourses);
		}
		List<ExecutionCourseAudit> executionCoursesAudits = getExecutionCoursesAudits(executionSemester);

		request.setAttribute("executionCoursesAudits", executionCoursesAudits);
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

	public ActionForward makeAvailableProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		String executionCourseAuditOID = (String) getFromRequest(request, "executionCourseAuditOID");
		ExecutionCourseAudit executionCourseAudit = AbstractDomainObject.fromExternalId(executionCourseAuditOID);

		executionCourseAudit.makeProcessAvailableToView();
		List<CompetenceCourseResultsResume> competenceCoursesToAudit = getCompetenceCourseResultsBeans(executionCourseAudit);

		request.setAttribute("competenceCoursesToAudit", competenceCoursesToAudit);
		request.setAttribute("executionCourseAudit", executionCourseAudit);
		request.setAttribute("success", "true");
		return mapping.findForward("viewProcessDetails");
	}

	public ActionForward makeUnavailableProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		String executionCourseAuditOID = (String) getFromRequest(request, "executionCourseAuditOID");
		ExecutionCourseAudit executionCourseAudit = AbstractDomainObject.fromExternalId(executionCourseAuditOID);

		executionCourseAudit.makeProcessUnavailableToView();
		List<CompetenceCourseResultsResume> competenceCoursesToAudit = getCompetenceCourseResultsBeans(executionCourseAudit);

		request.setAttribute("competenceCoursesToAudit", competenceCoursesToAudit);
		request.setAttribute("executionCourseAudit", executionCourseAudit);
		request.setAttribute("success", "true");
		return mapping.findForward("viewProcessDetails");
	}

	public ActionForward unsealProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		String executionCourseAuditOID = (String) getFromRequest(request, "executionCourseAuditOID");
		ExecutionCourseAudit executionCourseAudit = AbstractDomainObject.fromExternalId(executionCourseAuditOID);

		executionCourseAudit.unsealProcess();
		List<CompetenceCourseResultsResume> competenceCoursesToAudit = getCompetenceCourseResultsBeans(executionCourseAudit);

		request.setAttribute("competenceCoursesToAudit", competenceCoursesToAudit);
		request.setAttribute("executionCourseAudit", executionCourseAudit);
		request.setAttribute("success", "true");
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
		return searchExecutionCourse(mapping, actionForm, request, response);
	}
}
