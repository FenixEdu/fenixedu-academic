package net.sourceforge.fenixedu.presentationTier.Action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class RespondToInquiriesQuestion extends FenixDispatchAction {

    public final ActionForward showQuestion(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	request.setAttribute("executionPeriod", ExecutionSemester.readActualExecutionSemester());
	return mapping.findForward("respondToInquiriesQuestion");
    }

    private void registerResponse(final HttpServletRequest request, Boolean dontWantToRespond) throws FenixFilterException,
	    FenixServiceException {
	final Object[] args = { getUserView(request).getPerson(), dontWantToRespond };
	executeService("RegisterStudentInquiryResponseIntention", args);
    }

    private ActionForward forward(final String path) {
	final ActionForward actionForward = new ActionForward();
	actionForward.setPath(path);
	actionForward.setRedirect(true);
	return actionForward;
    }

    public final ActionForward registerStudentResponseDontWantToRespond(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	registerResponse(request, Boolean.TRUE);
	return forward("/home.do");
    }

    public final ActionForward registerStudentResponseRespondLater(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	registerResponse(request, Boolean.FALSE);
	return forward("/home.do");
    }

    public final ActionForward respondNow(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	registerResponse(request, Boolean.FALSE);
	return forward("/student/fillInquiries.do?method=prepareCourses&page=0");
    }

}