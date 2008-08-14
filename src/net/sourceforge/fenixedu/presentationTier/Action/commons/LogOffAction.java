/*
 * LogOffAction.java
 * 
 * 
 * Created on 09 de Dezembro de 2002, 16:37
 * 
 * 
 * Autores : - Nuno Nunes (nmsn@rnl.ist.utl.pt) - Joana Mota
 * (jccm@rnl.ist.utl.pt)
 *  
 */

package net.sourceforge.fenixedu.presentationTier.Action.commons;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu._development.PropertiesManager;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.servlets.filters.I18NFilter;

public class LogOffAction extends Action {

    private static final boolean useCASAuthentication;

    static {
	useCASAuthentication = Boolean.valueOf(PropertiesManager.getProperty("cas.enabled"));
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws Exception {

	ActionForward result = null;

	if (useCASAuthentication) {
	    if (request.getParameter("logoutFromCAS") != null && request.getParameter("logoutFromCAS").equals("true")) {
		killSession(request);
		result = mapping.findForward("showBlankPage");
	    } else {
		result = getCasLogoutActionForward();
	    }
	} else {
	    killSession(request);
	    result = mapping.findForward("showLoginPage");
	}
	// this way, we always put the locale as the default we want
	I18NFilter.setDefaultLocale(request, request.getSession());
	return result;
    }

    private ActionForward getCasLogoutActionForward() {
	ActionForward actionForward = new ActionForward();

	actionForward.setRedirect(true);
	actionForward.setPath(PropertiesManager.getProperty("cas.logoutUrl"));

	return actionForward;
    }

    private void killSession(HttpServletRequest request) {
	HttpSession session = request.getSession(false);
	if (session != null) {
	    session.invalidate();
	}
    }

}
