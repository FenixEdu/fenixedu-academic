package net.sourceforge.fenixedu.presentationTier.servlets.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu._development.PropertiesManager;

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
	final String username = request.getHeader(USERNAME_KEY);
	final String password = request.getHeader(PASSWORD_KEY);
	if (systemUsername.equals(username) && systemPassword.equals(password)) {
	    filterChain.doFilter(request, response);
	} else {
	    throw new ServletException("Not Authorized");
	}
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
    }
}
