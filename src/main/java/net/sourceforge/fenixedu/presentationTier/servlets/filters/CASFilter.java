package net.sourceforge.fenixedu.presentationTier.servlets.filters;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.presentationTier.Action.utils.RequestUtils;
import pt.ist.bennu.core.security.Authenticate;
import pt.ist.bennu.core.util.ConfigurationManager.CasConfig;

public class CASFilter extends pt.ist.fenixWebFramework.servlets.filters.CASFilter {

    @Override
    protected void redirectToCAS(final CasConfig casConfig, final HttpServletRequest request, final HttpServletResponse response)
            throws IOException, ServletException {
        if (Authenticate.getUser() == null) {
            String pendingRequest = request.getParameter("pendingRequest");
            if (pendingRequest == null) {
                pendingRequest = (String) request.getAttribute("pendingRequest");
            }
            final String barraAsAuthBroker = PropertiesManager.getProperty("barra.as.authentication.broker");
            final boolean useBarraAsAuthenticationBroker = barraAsAuthBroker != null && barraAsAuthBroker.equals("true");
            final String serviceString = encodeUrl(RequestUtils.generateRedirectLink(casConfig.getServiceUrl(), pendingRequest));
            String casLoginUrl = "";
            if (useBarraAsAuthenticationBroker) {
                final String barraLoginUrl = PropertiesManager.getProperty("barra.loginUrl");
                casLoginUrl += barraLoginUrl;
                casLoginUrl += "?next=";
            }
            casLoginUrl += casConfig.getCasLoginUrl();
            final String casLoginString = casLoginUrl + "?service=" + serviceString;
            response.sendRedirect(casLoginString);
        } else {
            response.sendRedirect(request.getContextPath() + "/home.do");
        }
    }

}
