package net.sourceforge.fenixedu.presentationTier.servlets.filters;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.util.HostRedirector;

public class CheckUserViewFilter implements Filter {

    private static final int APP_CONTEXT_LENGTH = PropertiesManager.getProperty("app.context").length() + 1;

    public void init(FilterConfig config) {
    }

    public void destroy() {
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        final HttpServletRequest request = (HttpServletRequest) servletRequest;
        final HttpServletResponse response = (HttpServletResponse) servletResponse;

        final String uri = request.getRequestURI();
        final IUserView userView = getUserView(request);
        if (isPrivateURI(uri.substring(APP_CONTEXT_LENGTH)) && !validUserView(userView)) {
        	final HttpSession httpSession = request.getSession(true);
        	httpSession.setAttribute("ORIGINAL_REQUEST", uri);

        	httpSession.setAttribute("ORIGINAL_URI", uri);

            final String relativeURI = uri.substring(APP_CONTEXT_LENGTH);
            final int indexOfSlash = relativeURI.indexOf('/');
            final String module = (indexOfSlash >= 0) ? relativeURI.substring(0, indexOfSlash) : "";

            httpSession.setAttribute("ORIGINAL_MODULE", module);

            final Map<String, Object> parameterMap = new HashMap<String, Object>();
            for (final Entry<String, Object> entry : ((Map<String, Object>) request.getParameterMap()).entrySet()) {
                parameterMap.put(entry.getKey(), entry.getValue());
            }
            httpSession.setAttribute("ORIGINAL_PARAMETER_MAP", parameterMap);

            final Map<String, Object> attributeMap = new HashMap<String, Object>();
        	final Enumeration enumeration = request.getAttributeNames();
        	while (enumeration.hasMoreElements()) {
        		final String attributeName = (String) enumeration.nextElement();
        		attributeMap.put(attributeName, request.getAttribute(attributeName));
        	}
        	httpSession.setAttribute("ORIGINAL_ATTRIBUTE_MAP", attributeMap);

            response.sendRedirect(HostRedirector.getRedirectPageLogin(request.getRequestURL().toString()));
        } else {
            filterChain.doFilter(request, response);
        }
    }

    private IUserView getUserView(final HttpServletRequest request) {
        final HttpSession session = request.getSession(false);
        return (IUserView) ((session != null) ? session.getAttribute(SessionConstants.U_VIEW) : null);
    }

    private boolean isPrivateURI(final String uri) {
        return (uri.length() > 1
        		&& (uri.indexOf("CSS/") == -1)
        		&& (uri.indexOf("images/") == -1)
                && (uri.indexOf("download/") == -1)
                && (uri.indexOf("external/") == -1)
                && (uri.indexOf("index.jsp") == -1)
                && (uri.indexOf("index.html") == -1)
                && (uri.indexOf("login.do") == -1)
                && (uri.indexOf("loginCAS.do") == -1)
                && (uri.indexOf("privado") == -1)
                && (uri.indexOf("loginPage.jsp") == -1)
                && (uri.indexOf("loginExpired.jsp") == -1)
                && (uri.indexOf("loginExpired.do") == -1)                
                && (uri.indexOf("logoff.do") == -1)
                && (uri.indexOf("publico/") == -1)
                && (uri.indexOf("showErrorPage.do") == -1)
                && (uri.indexOf("manager/manageCache.do") == -1)
                && (uri.indexOf("checkPasswordKerberos.do") == -1)
                && (uri.indexOf("siteMap.do") == -1)
                && (uri.indexOf("changeLocaleTo.do") == -1)
                && (uri.indexOf("cms/forwardEmailAction.do") == -1)
                && (uri.indexOf("isAlive.do") == -1));
    }

    private boolean validUserView(final IUserView userView) {
        return userView != null;
    }

}