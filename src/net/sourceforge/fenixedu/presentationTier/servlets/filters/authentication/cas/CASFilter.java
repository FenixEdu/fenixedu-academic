package net.sourceforge.fenixedu.presentationTier.servlets.filters.authentication.cas;

import java.io.IOException;
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
import net.sourceforge.fenixedu.util.cas.CASServiceUrlProvider;

/**
 * 
 * @author naat
 * 
 */
public class CASFilter implements Filter {

    private static final String URL_ENCODING = "UTF-8";

    private String casLoginUrl;

    private boolean enabled;

    public void init(FilterConfig config) throws ServletException {
        enabled = Boolean.valueOf(PropertiesManager.getProperty("cas.enabled"));

        if (enabled) {
            casLoginUrl = PropertiesManager.getProperty("cas.loginUrl");
        }

    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain fc)
            throws ServletException, IOException {

        if (enabled) {
            if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
                throw new ServletException("CASFilter only applies to HTTP resources");
            }

            String ticket = request.getParameter("ticket");

            if (ticket == null || ticket.equals("")) {
                // send user to CAS to get a ticket
                redirectToCAS((HttpServletRequest) request, (HttpServletResponse) response);
                return;

            }
        }

        fc.doFilter(request, response);

    }

    /**
     * Redirects the user to CAS, determining the service from the request.
     */
    private void redirectToCAS(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        String serviceString = URLEncoder.encode(CASServiceUrlProvider.getServiceUrl(request
                .getRequestURL().toString()), URL_ENCODING);

        String casLoginString = casLoginUrl + "?service=" + serviceString;

        ((HttpServletResponse) response).sendRedirect(casLoginString);

    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.Filter#destroy()
     */
    public void destroy() {

    }
}
