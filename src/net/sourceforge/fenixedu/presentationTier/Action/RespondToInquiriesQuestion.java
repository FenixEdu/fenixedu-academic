package net.sourceforge.fenixedu.presentationTier.Action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.inquiries.InquiryResponsePeriod;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.ChecksumRewriter;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class RespondToInquiriesQuestion extends FenixDispatchAction {

    public final ActionForward showQuestion(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	final InquiryResponsePeriod lastPeriod = InquiryResponsePeriod.readOpenPeriod();
	request.setAttribute("executionPeriod", lastPeriod == null ? null : lastPeriod.getExecutionPeriod());

	return mapping.findForward("respondToInquiriesQuestion");
    }

    private ActionForward forward(final String path) {
	final ActionForward actionForward = new ActionForward();
	actionForward.setPath(path);
	actionForward.setRedirect(true);
	return actionForward;
    }

    public final ActionForward registerStudentResponseRespondLater(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	return forward("/home.do");
    }

    public final ActionForward respondNow(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	final String path = "/student/studentInquiry.do?method=showCoursesToAnswer&page=0&contentContextPath_PATH=/estudante/estudante";
	return forward(path + "&_request_checksum_=" + ChecksumRewriter.calculateChecksum(request.getContextPath() + path));
    }

}