package net.sourceforge.fenixedu.presentationTier.Action.externalServices;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pt.ist.bennu.core.domain.User;
import net.sourceforge.fenixedu.applicationTier.Servico.Authenticate;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoAutenticacao;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidPasswordServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.PasswordExpiredServiceException;
import net.sourceforge.fenixedu.presentationTier.Action.BaseAuthenticationAction;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NotAuthorizedActionException;
import net.sourceforge.fenixedu.util.HostAccessControl;
import net.sourceforge.fenixedu.util.kerberos.KerberosException;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "external", path = "/checkLocalPassword", scope = "request")
@Exceptions(value = { @ExceptionHandling(
        type = net.sourceforge.fenixedu.presentationTier.Action.exceptions.NotAuthorizedActionException.class,
        key = "error.NotAuthorized", handler = org.apache.struts.action.ExceptionHandler.class, path = "/publicNotAuthorized.do",
        scope = "request") })
public class CheckLocalPasswordAction extends FenixAction {

    private static final String SUCCESS_MESSAGE = "CHECK_PASSWORD_OK";

    private static final String UNEXPECTED_ERROR_MESSAGE = "UNEXPECTED_ERROR";

    @Override
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

            String result = null;

            try {
                final User userView =
                        Authenticate.runLocalAuthenticate(username, password, requestURL,
                                BaseAuthenticationAction.getRemoteHostName(request));

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