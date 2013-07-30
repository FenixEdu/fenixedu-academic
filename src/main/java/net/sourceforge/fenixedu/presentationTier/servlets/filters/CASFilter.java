package net.sourceforge.fenixedu.presentationTier.servlets.filters;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.presentationTier.Action.utils.RequestUtils;
import pt.ist.bennu.core.security.Authenticate;
import pt.ist.bennu.core.util.ConfigurationManager;
import pt.ist.bennu.core.util.ConfigurationManager.CasConfig;

public class CASFilter implements Filter {

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

    private boolean isHttpResource(final ServletRequest request, final ServletResponse response) {
        return request instanceof HttpServletRequest && response instanceof HttpServletResponse;
    }

    private boolean notHasTicket(final ServletRequest request) {
        final String ticket = request.getParameter("ticket");
        return ticket == null || ticket.equals("");
    }

    protected String encodeUrl(final String casUrl) throws UnsupportedEncodingException {
        return URLEncoder.encode(casUrl, "UTF-8");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        final CasConfig casConfig = ConfigurationManager.getCasConfig();
        if (casConfig != null && casConfig.isCasEnabled()) {
            if (!isHttpResource(servletRequest, servletResponse)) {
                throw new ServletException("CASFilter only applies to HTTP resources");
            }

            if (notHasTicket(servletRequest)) {
                // send user to CAS to get a ticket
                redirectToCAS(casConfig, (HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse);
                return;
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
    }

    @Override
    public void destroy() {
    }

}
