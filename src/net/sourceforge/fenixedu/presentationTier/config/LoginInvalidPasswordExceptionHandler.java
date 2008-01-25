package net.sourceforge.fenixedu.presentationTier.config;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidPasswordServiceException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionConstants;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.config.ExceptionConfig;

public class LoginInvalidPasswordExceptionHandler extends FenixExceptionHandler {

    public ActionForward execute(Exception ex, ExceptionConfig ae, ActionMapping mapping, ActionForm formInstance,
	    HttpServletRequest request, HttpServletResponse response) throws ServletException {

	super.execute(ex, ae, mapping, formInstance, request, response);

	ActionMessage error = null;
	IUserView userView = null;
	String errorMessage = "message.invalid.password";

	// Figure out the error
	if (ex instanceof InvalidPasswordServiceException) {
	    InvalidPasswordServiceException invalidPasswordServiceException = (InvalidPasswordServiceException) ex;
	    userView = invalidPasswordServiceException.getUserView();
	    error = new ActionMessage(errorMessage, errorMessage);
	}

	// Invalidate existing session if it exists
	HttpSession sessao = request.getSession(false);
	if (sessao != null) {
	    sessao.invalidate();
	}

	// Create a new session for this user
	sessao = request.getSession(true);

	// Store the UserView into the session and return
	sessao.setAttribute(SessionConstants.U_VIEW, userView);
	sessao.setAttribute(SessionConstants.SESSION_IS_VALID, new Boolean(true));

	ActionForward forward = mapping.findForward("changePass");
	request.setAttribute(Globals.EXCEPTION_KEY, ex);
	super.storeException(request, errorMessage, error, forward, ae.getScope());
	return forward;
    }

}
