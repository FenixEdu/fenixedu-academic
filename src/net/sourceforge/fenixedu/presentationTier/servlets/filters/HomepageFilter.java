package net.sourceforge.fenixedu.presentationTier.servlets.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.domain.homepage.Homepage;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.apache.commons.lang.StringUtils;

public class HomepageFilter implements Filter {

    ServletContext servletContext;

    FilterConfig filterConfig;

    String forwardSiteList;

    String forwardExecutionCourse;

    String notFoundURI;

    String app;

    public void init(final FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
        this.servletContext = filterConfig.getServletContext();
    }

    public void destroy() {
        this.servletContext = null;
        this.filterConfig = null;
    }

    public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse,
    		final FilterChain filterChain) throws IOException, ServletException {

    	final HttpServletRequest request = (HttpServletRequest) servletRequest;
        final HttpServletResponse response = (HttpServletResponse) servletResponse;

        final String uri = request.getRequestURI();
        final String homepageURL = StringUtils.substringAfter(uri, "/homepage/");

        final String appContext = PropertiesManager.getProperty("app.context");
        final String context = (appContext != null && appContext.length() > 0) ? "/" + appContext : "";

        if (homepageURL != null && homepageURL.length() > 0) {
        	for (final Person person : RootDomainObject.readAllPersons()) {
        		final Homepage homepage = person.getHomepage();
        		if (homepage != null && homepage.getMyUrl().equalsIgnoreCase(homepageURL)) {
        			response.sendRedirect(context + "/publico/viewHomepage.do?method=show&homepageID=" + homepage.getIdInternal());
        			return;
        		}
        	}
        }

        response.sendRedirect(context + "/publico/viewHomepage.do?method=notFound&homepageURL=" + homepageURL);
    }

}