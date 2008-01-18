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

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.util.LanguageUtils;

import org.apache.struts.Globals;

/**
 * 
 * @author Luis Cruz
 */
public class I18NFilter implements Filter {

    ServletContext servletContext;

    FilterConfig filterConfig;

    protected static final Locale DEFAULT_LOCALE;
    static {
	final String language = PropertiesManager.getProperty("language");
	final String location = PropertiesManager.getProperty("location");
	final String variant = PropertiesManager.getProperty("variant");
	DEFAULT_LOCALE = new Locale(language, location, variant);
    }

    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
        this.servletContext = filterConfig.getServletContext();
    }

    public void destroy() {
        this.servletContext = null;
        this.filterConfig = null;
    }

    public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain)
    		throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) servletRequest;
        final HttpServletResponse response = (HttpServletResponse) servletResponse;

        final String localParameter = request.getParameter("locale");
        final HttpSession httpSession;
        final Locale locale;
        if (localParameter != null) {
            final String[] localTokens = localParameter.split("_");
            locale = localTokens.length > 1 ? new Locale(localTokens[0], localTokens[1]) : new Locale(localParameter);
            httpSession = getOrCreateSession(request);
        } else {
            httpSession = request.getSession(true);
            final Locale localeFromSession = (Locale) httpSession.getAttribute(Globals.LOCALE_KEY);
            locale = localeFromSession == null ? DEFAULT_LOCALE : localeFromSession;
        }

        setLocale(request, httpSession, locale);
        filterChain.doFilter(request, response);
    }

    private HttpSession getOrCreateSession(final HttpServletRequest request) {
        final HttpSession httpSession = request.getSession(false);
        return httpSession == null ? request.getSession(true) : httpSession;
    }

    public static void setDefaultLocale(final HttpServletRequest httpServletRequest, final HttpSession httpSession) {
	setLocale(httpServletRequest, httpSession, DEFAULT_LOCALE);
    }

    public static void setLocale(final HttpServletRequest httpServletRequest, final HttpSession httpSession, final Locale locale) {
	httpSession.removeAttribute(Globals.LOCALE_KEY);
        httpSession.setAttribute(Globals.LOCALE_KEY, locale);
        httpServletRequest.removeAttribute(Globals.LOCALE_KEY);
        httpServletRequest.setAttribute(Globals.LOCALE_KEY, locale);
        LanguageUtils.setLocale(locale);	
    }

}
