package net.sourceforge.fenixedu.presentationTier.Action;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoAutenticacao;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidPasswordServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.PasswordExpiredServiceException;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NotAuthorizedActionException;
import net.sourceforge.fenixedu.util.kerberos.KerberosException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class CheckPasswordKerberosAction extends FenixAction {

    private static final Logger logger = Logger.getLogger(CheckPasswordKerberosAction.class);

    private static final List<String> authorizedHosts;

    private static final String SUCCESS_MESSAGE = "SUCCESS";

    private static final String UNEXPECTED_ERROR_MESSAGE = "UNEXPECTED_ERROR";

    static {
        authorizedHosts = Arrays.asList(StringUtils.split(PropertiesManager
                .getProperty("checkPasswordKerberos.authorizedHosts"), ","));
    }

    public final ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        checkAuthorizationForRequestHost(request);

        if (request.getMethod().equalsIgnoreCase("post")) {

            final String username = request.getParameter("username");
            final String password = request.getParameter("password");
            final String requestURL = request.getRequestURL().toString();
            final Object authenticationArgs[] = { username, password, requestURL };

            String result = null;

            try {
                ServiceManagerServiceFactory.executeService(null, "KerberosExternalAuthentication",
                        authenticationArgs);

                result = SUCCESS_MESSAGE;

            } catch (PasswordExpiredServiceException e) {
                result = e.getMessage();
            } catch (InvalidPasswordServiceException e) {
                result = e.getMessage();
            } catch (ExcepcaoAutenticacao e) {
                result = KerberosException.WRONG_PASSWORD;
            } catch (Exception e) {
                result = UNEXPECTED_ERROR_MESSAGE;
            }

            response.setContentType("text/html");
            response.getOutputStream().write(result.getBytes());
        }

        return null;

    }

    private void checkAuthorizationForRequestHost(HttpServletRequest request)
            throws NotAuthorizedActionException {
        if (!authorizedHosts.contains(request.getRemoteAddr())
                && !authorizedHosts.contains(request.getRemoteHost())) {
            logger.warn("Host " + request.getRemoteHost() + "(" + request.getRemoteAddr() + ")");

            throw new NotAuthorizedActionException("error.NotAuthorized");
        }

    }
}