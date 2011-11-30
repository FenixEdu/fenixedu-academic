package net.sourceforge.fenixedu.presentationTier.Action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.inquiries.StudentInquiryTemplate;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class RespondToInquiriesQuestion extends FenixDispatchAction {

    public final ActionForward showQuestion(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	final StudentInquiryTemplate inquiryTemplate = StudentInquiryTemplate.getCurrentTemplate();
	request.setAttribute("executionPeriod", inquiryTemplate == null ? null : inquiryTemplate.getExecutionPeriod());

	return mapping.findForward("respondToInquiriesQuestion");
    }

    public final ActionForward showTeacherQuestion(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	final StudentInquiryTemplate inquiryTemplate = StudentInquiryTemplate.getCurrentTemplate();
	request.setAttribute("executionPeriod", inquiryTemplate == null ? null : inquiryTemplate.getExecutionPeriod());

	return mapping.findForward("respondToTeacherInquiriesQuestion");
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
	return forward(path
		+ "&_request_checksum_="
		+ pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.calculateChecksum(request
			.getContextPath() + path));
    }

}