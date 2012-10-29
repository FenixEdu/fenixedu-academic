package net.sourceforge.fenixedu.presentationTier.Action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class RespondToFirstTimeCycleInquiry extends FenixDispatchAction {

    public final ActionForward showQuestion(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	return mapping.findForward("inquiryNotice");
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
	final String path = "/student/studentCycleInquiry.do?method=prepare&"
		+ net.sourceforge.fenixedu.presentationTier.servlets.filters.ContentInjectionRewriter.CONTEXT_ATTRIBUTE_NAME
		+ "=/estudante/estudante";
	return forward(path
		+ "&_request_checksum_="
		+ pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.calculateChecksum(request
			.getContextPath() + path));
    }

}