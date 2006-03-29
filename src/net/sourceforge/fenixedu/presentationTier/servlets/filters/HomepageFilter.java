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
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.domain.homepage.Homepage;

import org.apache.commons.lang.StringUtils;

public class HomepageFilter implements Filter {

    public void init(final FilterConfig filterConfig) { }

    public void destroy() { }

    public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse,
    		final FilterChain filterChain) throws IOException, ServletException {

    	final HttpServletRequest request = (HttpServletRequest) servletRequest;
        final HttpServletResponse response = (HttpServletResponse) servletResponse;

        final String uri = request.getRequestURI();
        final String homepageURL = StringUtils.substringAfter(uri, "/homepage/");

        final String appContext = PropertiesManager.getProperty("app.context");
        final String context = (appContext != null && appContext.length() > 0) ? "/" + appContext : "";

        if (homepageURL != null && homepageURL.length() > 0) {
        	for (final User user : RootDomainObject.getInstance().getUsers()) {
        		if (homepageURL.equalsIgnoreCase(user.getUserUId())) {
        			final Homepage homepage = user.getPerson().getHomepage();
        			if (homepage != null && homepage.getActivated() != null && homepage.getActivated().booleanValue()) {
        				response.sendRedirect(context + "/publico/viewHomepage.do?method=show&homepageID=" + homepage.getIdInternal());
        				return;
        			} else {
        				response.sendRedirect(context + "/publico/viewHomepage.do?method=notFound&homepageURL=" + homepageURL);
        				return;
        			}
        		}
        	}
        }

        response.sendRedirect(context + "/publico/viewHomepage.do?method=notFound&homepageURL=" + homepageURL);
    }

}