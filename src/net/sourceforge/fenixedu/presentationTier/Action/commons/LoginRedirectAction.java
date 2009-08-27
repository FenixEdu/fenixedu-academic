package net.sourceforge.fenixedu.presentationTier.Action.commons;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.PendingRequest;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class LoginRedirectAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws Exception {
	PendingRequest pendingRequest = PendingRequest.fromExternalId(request.getParameter("pendingRequest"));

	request.setAttribute("REDIRECT_URL", pendingRequest.getUrl());

	if (pendingRequest.getPost()) {
	    request.setAttribute("ORIGINAL_METHOD", "post");
	} else {
	    request.setAttribute("ORIGINAL_METHOD", "get");
	}

	request.setAttribute("ORIGINAL_PARAMETER_MAP", pendingRequest.getRequestsParams());
	pendingRequest.delete();
	request.setAttribute("ORIGINAL_ATTRIBUTE_MAP", null);
	return mapping.findForward("show-redirect-page");

    }
}