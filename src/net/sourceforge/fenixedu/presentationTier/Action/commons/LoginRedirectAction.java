package net.sourceforge.fenixedu.presentationTier.Action.commons;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class LoginRedirectAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws Exception {

	final HttpSession session = request.getSession(false);

	final String originalURI = (String) session.getAttribute("ORIGINAL_URI");
	if (originalURI != null) {
	    final int seperatorIndex = originalURI.indexOf('?');
	    final int endIndex = (seperatorIndex > 0) ? seperatorIndex : originalURI.length();
	    request.setAttribute("REDIRECT_URL", originalURI.substring(0, endIndex));
	}

	session.removeAttribute("ORIGINAL_URI");
	transferFromSessionToRequest(request, session, "ORIGINAL_PARAMETER_MAP");
	transferFromSessionToRequest(request, session, "ORIGINAL_ATTRIBUTE_MAP");

	return mapping.findForward("show-redirect-page");
    }

    private void transferFromSessionToRequest(final HttpServletRequest request, final HttpSession session, final String key) {
	final Object value = session.getAttribute(key);
	session.removeAttribute(key);
	request.setAttribute(key, value);
    }

}