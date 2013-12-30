package net.sourceforge.fenixedu.presentationTier.config;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidPasswordServiceException;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.config.ExceptionConfig;
import org.fenixedu.bennu.core.domain.User;

public class LoginInvalidPasswordExceptionHandler extends FenixExceptionHandler {

    @Override
    public ActionForward execute(Exception ex, ExceptionConfig ae, ActionMapping mapping, ActionForm formInstance,
            HttpServletRequest request, HttpServletResponse response) throws ServletException {

        super.execute(ex, ae, mapping, formInstance, request, response);

        ActionMessage error = null;
        User userView = null;
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

        ActionForward forward = mapping.findForward("changePass");
        request.setAttribute(Globals.EXCEPTION_KEY, ex);
        super.storeException(request, errorMessage, error, forward, ae.getScope());
        return forward;
    }

}
