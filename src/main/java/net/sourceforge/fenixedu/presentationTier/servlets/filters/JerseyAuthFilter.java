package net.sourceforge.fenixedu.presentationTier.servlets.filters;

import pt.ist.fenixframework.plugins.remote.domain.RemoteHost;
import pt.ist.fenixframework.plugins.remote.domain.RemoteSystem;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu._development.PropertiesManager;

@WebFilter(urlPatterns = "/jersey/*")
public class JerseyAuthFilter implements Filter {

    final static String systemUsername = PropertiesManager.getProperty("jersey.username");
    final static String systemPassword = PropertiesManager.getProperty("jersey.password");
    final static String USERNAME_KEY = "__username__";
    final static String PASSWORD_KEY = "__password__";

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        final HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        final HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        doFilter(httpServletRequest, httpServletResponse, filterChain);
    }

    public void doFilter(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain)
            throws IOException, ServletException {
        if (isOauth2(request) || checkAccessControl(request)) {
            filterChain.doFilter(request, response);
        } else {
            throw new ServletException("Not Authorized");
        }
    }

    private boolean isOauth2(HttpServletRequest request) {
        return request.getRequestURI().contains("/jersey/private/");
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
    }

    private Boolean checkAccessControl(final HttpServletRequest request) {
        final String url = getClientAddress(request);
        final String username = request.getHeader(USERNAME_KEY);
        final String password = request.getHeader(PASSWORD_KEY);
        Boolean found = Boolean.FALSE;
        for (final RemoteHost remoteHost : RemoteSystem.getInstance().getRemoteHostsSet()) {
            if (remoteHost.matches(url, username, password)) {
                System.out.println("[Jersey Server Invoke by client " + url);
                found = Boolean.TRUE;
            }
        }
        System.out.println("[Jersey Server] Invoke by client " + url);
        return found;
    }

    private String getClientAddress(final HttpServletRequest request) {
        final String xForwardForHeader = request.getHeader("X-Forwarded-For");
        if (xForwardForHeader != null && !xForwardForHeader.isEmpty()) {
            final int urlSeperator = xForwardForHeader.indexOf(',');
            return urlSeperator > 0 ? xForwardForHeader.substring(0, urlSeperator) : xForwardForHeader;
        }
        return request.getRemoteHost();
    }
}
