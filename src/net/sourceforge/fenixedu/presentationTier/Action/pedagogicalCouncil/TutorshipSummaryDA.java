package net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.TutorshipSummary;
import net.sourceforge.fenixedu.presentationTier.Action.commons.tutorship.ViewStudentsByTutorDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

@Mapping(path = "/tutorshipSummary", module = "pedagogicalCouncil")
@Forwards( { @Forward(name = "searchTeacher", path = "/pedagogicalCouncil/tutorship/tutorTutorships.jsp"),
	@Forward(name = "createSummary", path = "/pedagogicalCouncil/tutorship/createSummary.jsp"),
	@Forward(name = "editSummary", path = "/pedagogicalCouncil/tutorship/editSummary.jsp"),
	@Forward(name = "processCreateSummary", path = "/pedagogicalCouncil/tutorship/processCreateSummary.jsp"),
	@Forward(name = "confirmCreateSummary", path = "/pedagogicalCouncil/tutorship/confirmCreateSummary.jsp"),
	@Forward(name = "viewSummary", path = "/pedagogicalCouncil/tutorship/viewSummary.jsp") })
public class TutorshipSummaryDA extends ViewStudentsByTutorDispatchAction {

    public ActionForward searchTeacher(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	TutorSummaryBean bean = (TutorSummaryBean) getRenderedObject("tutorateBean");

	if (bean == null) {
	    bean = new TutorSummaryBean();
	} else {
	    RenderUtils.invalidateViewState();

	    if (bean.getTeacher() != null) {
		getTutorships(request, bean.getTeacher());

		request.setAttribute("tutor", bean.getTeacher());
	    }
	}
	request.setAttribute("tutorateBean", bean);

	return mapping.findForward("searchTeacher");
    }

    public ActionForward createSummary(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	CreateSummaryBean bean = (CreateSummaryBean) getRenderedObject("createSummaryBean");

	if (bean == null) {

	    bean = getCreateSummaryBean(request);

	    if (bean == null) {
		return searchTeacher(mapping, actionForm, request, response);
	    }

	}

	request.setAttribute("createSummaryBean", bean);

	return mapping.findForward("createSummary");
    }

    protected CreateSummaryBean getCreateSummaryBean(HttpServletRequest request) {
	CreateSummaryBean bean = null;

	String summaryId = (String) getFromRequest(request, "summaryId");

	if (summaryId != null) {
	    TutorshipSummary tutorshipSummary = AbstractDomainObject.fromExternalId(summaryId);

	    if (tutorshipSummary != null) {
		bean = new EditSummaryBean(tutorshipSummary);
	    }
	} else {
	    String teacherId = (String) getFromRequest(request, "teacherId");
	    String degreeId = (String) getFromRequest(request, "degreeId");

	    Teacher teacher = AbstractDomainObject.fromExternalId(teacherId);
	    Degree degree = AbstractDomainObject.fromExternalId(degreeId);
	    ExecutionSemester executionSemester = ExecutionSemester.readActualExecutionSemester();

	    if (teacher != null && degree != null) {
		bean = new CreateSummaryBean(teacher, executionSemester, degree);
	    }
	}

	return bean;
    }

    public ActionForward processCreateSummary(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	CreateSummaryBean bean = (CreateSummaryBean) getRenderedObject("createSummaryBean");

	if (bean == null) {
	    return createSummary(mapping, actionForm, request, response);
	}

	request.setAttribute("createSummaryBean", bean);

	if (getFromRequest(request, "confirm") != null) {
	    bean.save();

	    return mapping.findForward("confirmCreateSummary");
	}

	return mapping.findForward("processCreateSummary");
    }

    public ActionForward viewSummary(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	String summaryId = (String) getFromRequest(request, "summaryId");
	TutorshipSummary tutorshipSummary = AbstractDomainObject.fromExternalId(summaryId);

	request.setAttribute("tutorshipSummary", tutorshipSummary);

	return mapping.findForward("viewSummary");
    }
}
