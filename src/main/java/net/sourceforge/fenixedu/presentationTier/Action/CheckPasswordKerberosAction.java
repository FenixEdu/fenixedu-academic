package net.sourceforge.fenixedu.presentationTier.Action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.AuthenticateKerberos;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoAutenticacao;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidPasswordServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.PasswordExpiredServiceException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NotAuthorizedActionException;
import net.sourceforge.fenixedu.util.HostAccessControl;
import net.sourceforge.fenixedu.util.kerberos.KerberosException;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class CheckPasswordKerberosAction extends FenixAction {

    private static final String SUCCESS_MESSAGE = "SUCCESS";

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
                final IUserView userView =
                        AuthenticateKerberos.runKerberosExternalAuthentication(username, password, requestURL,
                                BaseAuthenticationAction.getRemoteHostName(request));

                result =
                        SUCCESS_MESSAGE + "\n"
                                + (userView.getPerson().hasIstUsername() ? userView.getPerson().getIstUsername() : username);

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