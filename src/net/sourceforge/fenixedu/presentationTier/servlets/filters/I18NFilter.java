/*
 * Created on 2005/05/13
 * 
 */
package net.sourceforge.fenixedu.presentationTier.servlets.filters;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.Globals;
import org.apache.struts.action.Action;

/**
 * 
 * @author Luis Cruz
 */
public class I18NFilter implements Filter {

    ServletContext servletContext;

    FilterConfig filterConfig;

    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
        this.servletContext = filterConfig.getServletContext();
    }

    public void destroy() {
        this.servletContext = null;
        this.filterConfig = null;
    }

    public void doFilter(final ServletRequest req, final ServletResponse res, final FilterChain chain) throws IOException,
            ServletException {
        final HttpServletRequest request = (HttpServletRequest) req;
        final HttpServletResponse response = (HttpServletResponse) res;

        final String localParameter = request.getParameter("locale");
        if (localParameter != null) {
            final String[] localTokens = localParameter.split("_");
            final Locale locale;
            if (localTokens.length > 1) {
                locale = new Locale(localTokens[0], localTokens[1]);
            } else {
                locale = new Locale(localParameter);
            }

            HttpSession httpSession = request.getSession(false);
            if (httpSession == null) {
                httpSession = request.getSession(true);
            }
            httpSession.removeAttribute(Globals.LOCALE_KEY);
            httpSession.setAttribute(Globals.LOCALE_KEY, locale);

            request.removeAttribute(Globals.LOCALE_KEY);
            request.setAttribute(Globals.LOCALE_KEY, locale);
        } else {
            HttpSession httpSession = request.getSession(false);
            if (httpSession != null && httpSession.getAttribute(Globals.LOCALE_KEY) == null) {
                final Locale locale = new Locale("pt", "PT");
                httpSession.setAttribute(Globals.LOCALE_KEY, locale);

                request.removeAttribute(Globals.LOCALE_KEY);
                request.setAttribute(Globals.LOCALE_KEY, locale);
            }
        }

        chain.doFilter(request, response);
    }

}
