package net.sourceforge.fenixedu.presentationTier.servlets.filters;

import java.io.IOException;
import java.util.IllformedLocaleException;
import java.util.Locale;
import java.util.Locale.Builder;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fenixedu.bennu.core.util.CoreConfiguration;
import org.fenixedu.commons.i18n.I18N;

import pt.utl.ist.fenix.tools.util.i18n.Language;

/**
 * This is used to set the language in the FenixAPI.
 * It was created to use the Bennu API.
 * This filter is deprecated since its inception. This must be adapted to the next bennu release which will make it available for
 * every api call.
 * 
 * 
 */
@Deprecated
public class FenixAPILanguageFilter implements Filter {

    @Override
    public void destroy() {
        // TODO Auto-generated method stub

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    public void doFilter(final HttpServletRequest request, final HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        final String localeTag = request.getParameter("lang");
        if (localeTag != null) {
            try {
                final Locale locale = new Builder().setLanguageTag(localeTag).build();
                if (CoreConfiguration.supportedLocales().contains(locale)) {
                    I18N.setLocale(locale);
                    Language.setLocale(locale);
                }
            } catch (IllformedLocaleException e) {
            }
        }
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
    }
}
