package net.sourceforge.fenixedu.presentationTier.Action.externalServices;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoAutenticacao;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidPasswordServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.PasswordExpiredServiceException;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.BaseAuthenticationAction;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NotAuthorizedActionException;
import net.sourceforge.fenixedu.util.HostAccessControl;
import net.sourceforge.fenixedu.util.kerberos.KerberosException;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class CheckLocalPasswordAction extends FenixAction {

    private static final String SUCCESS_MESSAGE = "CHECK_PASSWORD_OK";

    private static final String UNEXPECTED_ERROR_MESSAGE = "UNEXPECTED_ERROR";

    public final ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	// check hosts accessing this action
	if (!HostAccessControl.isAllowed(this, request)) {
	    throw new NotAuthorizedActionException("error.NotAuthorized");
	}

	if (request.getMethod().equalsIgnoreCase("post")) {

	    final String username = request.getParameter("username");
	    final String password = request.getParameter("password");
	    final String requestURL = request.getRequestURL().toString();
	    final Object authenticationArgs[] = { username, password, requestURL,
		    BaseAuthenticationAction.getRemoteHostName(request) };

	    String result = null;

	    try {
		final IUserView userView = (IUserView) ServiceManagerServiceFactory.executeService(
			"LocalAuthenticate", authenticationArgs);

		result = SUCCESS_MESSAGE;

	    } catch (PasswordExpiredServiceException e) {
		result = e.getMessage();
	    } catch (InvalidPasswordServiceException e) {
		result = e.getMessage();
	    } catch (ExcepcaoAutenticacao e) {
		result = KerberosException.WRONG_PASSWORD;
	    } catch (Throwable e) {
		result = UNEXPECTED_ERROR_MESSAGE;
	    }

	    response.setContentType("text/html");
	    response.getOutputStream().write(result.getBytes());
	    response.getOutputStream().close();
	}

	return null;

    }

}