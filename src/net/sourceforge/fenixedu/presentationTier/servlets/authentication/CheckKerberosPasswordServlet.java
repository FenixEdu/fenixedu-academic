package net.sourceforge.fenixedu.presentationTier.servlets.authentication;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoAutenticacao;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidPasswordServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.PasswordExpiredServiceException;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NotAuthorizedActionException;
import net.sourceforge.fenixedu.util.kerberos.KerberosException;

import org.apache.commons.lang.StringUtils;

/**
 * Temporary Kerberos Password Check servlet until we move kerberos module to
 * CAS (Central Authentication Server)
 * 
 * @author naat
 * 
 */

public class CheckKerberosPasswordServlet extends HttpServlet {

    private static final String SUCCESS_MESSAGE = "SUCCESS";

    private static final String UNEXPECTED_ERROR_MESSAGE = "UNEXPECTED_ERROR";

    private List<String> authorizedHosts;

    @Override
    public void init() throws ServletException {
        super.init();
        this.authorizedHosts = Arrays.asList(StringUtils.split(this.getInitParameter("authorizedHosts"),
                ","));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        checkAuthorizationForRequestHost(request);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        checkAuthorizationForRequestHost(request);

        process(request, response);
    }

    private void process(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
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

    private void checkAuthorizationForRequestHost(HttpServletRequest request) throws ServletException {
        if (!this.authorizedHosts.contains(request.getRemoteAddr())
                && !this.authorizedHosts.contains(request.getRemoteHost())) {
            throw new net.sourceforge.fenixedu.accessControl.IllegalDataAccessException();
        }
    }
}
