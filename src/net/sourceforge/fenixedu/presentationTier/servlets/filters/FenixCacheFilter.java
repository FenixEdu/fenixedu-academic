/*
 * Created on Jun 25, 2004
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

import net.sourceforge.fenixedu.presentationTier.mapping.MappingUtils;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.cache.ResponseCacheOSCacheImpl;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.Action;

import com.opensymphony.oscache.web.filter.CacheHttpServletResponseWrapper;
import com.opensymphony.oscache.web.filter.ResponseContent;

/**
 * @author Luis Cruz
 *  
 */
public class FenixCacheFilter implements Filter {

    ServletContext servletContext;

    FilterConfig filterConfig;

    String excludePattern;

    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
        this.servletContext = filterConfig.getServletContext();

        int time = 300;
        try {
            time = Integer.parseInt(filterConfig.getInitParameter("time"));
        } catch (Exception e) {
        }

        excludePattern = filterConfig.getInitParameter("exclude-url-pattern");

        ResponseCacheOSCacheImpl.getInstance().setRefreshTimeout(time);
    }

    public void destroy() {
        this.servletContext = null;
        this.filterConfig = null;
    }

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException,
            ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        //String uri = request.getRequestURI();


        String url = getPageURL(request);

       	ResponseContent respContent = ResponseCacheOSCacheImpl.getInstance().lookup(url);
       	if (respContent != null && !matchesExcludePattern(url)) {
       		respContent.writeTo(response);
       	} else {
       		CacheHttpServletResponseWrapper cacheResponse = new CacheHttpServletResponseWrapper(response);
       		chain.doFilter(request, cacheResponse);
       		cacheResponse.flushBuffer();

       		// Only cache if the response was 200
       		if (cacheResponse.getStatus() == HttpServletResponse.SC_OK && !matchesExcludePattern(url)) {
       			//Store as the cache content the result of the response
       			ResponseCacheOSCacheImpl.getInstance().cache(url, cacheResponse.getContent());
       		}
        }
    }

    public static String getPageURL(HttpServletRequest request) {
        String urlId = MappingUtils.currentURL(request);
        StringBuffer id = new StringBuffer(urlId);

        HttpSession httpSession = request.getSession(false);
        if (httpSession == null) {
            httpSession = request.getSession();
        }
        Locale locale = (Locale) httpSession.getAttribute(Action.LOCALE_KEY);
        if (locale == null) {
            locale = Locale.getDefault();
            httpSession.setAttribute(Action.LOCALE_KEY, locale);
        }
        String language = locale.getLanguage();
        id.append(language);
        //        System.out.println("local in cache wrapper: " + locale.getLanguage());
//      	// optionally append i18n sensitivity
//       	String localeSensitive = this.filterConfig.getInitParameter("locale-sensitive");
//        if (localeSensitive != null) {
//       		StringWriter ldata = new StringWriter();
//       		Enumeration locales = request.getLocales();
//       		while (locales.hasMoreElements()) {
//       			Locale locale2 = (Locale) locales.nextElement();
//       			ldata.write(locale2.getISO3Language());
//       		}
//       		id.append(ldata.toString());
//       	}

        return id.toString();
    }

	private boolean matchesExcludePattern(String id) {
		return StringUtils.contains(id, excludePattern);
	}


}