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

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.presentationTier.mapping.MappingUtils;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.cache.ResponseCacheOSCacheImpl;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.cache.ResponseContentEntry;
import pt.ist.fenixframework.pstm.CommitListener;
import pt.ist.fenixframework.pstm.TopLevelTransaction;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.Globals;

import com.opensymphony.oscache.web.filter.CacheHttpServletResponseWrapper;


/**
 * @author Luis Cruz
 *  
 */
public class FenixCacheFilter implements Filter,CommitListener {

    private static final ThreadLocal<ResponseContentEntry> CACHING_RESPONSE = new ThreadLocal<ResponseContentEntry>();

    ServletContext servletContext;

    FilterConfig filterConfig;

    String excludePattern;

    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
        this.servletContext = filterConfig.getServletContext();

        int time;
        try {
            time = PropertiesManager.getIntegerProperty("response.cache.timeout");
        } catch (Exception e) {
            time = 300;
        }

        excludePattern = filterConfig.getInitParameter("exclude-url-pattern");

        ResponseCacheOSCacheImpl.getInstance().setRefreshTimeout(time);

        TopLevelTransaction.addCommitListener(this);
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

	if (! matchesExcludePattern(url)) {

	    ResponseCacheOSCacheImpl cache = ResponseCacheOSCacheImpl.getInstance();

	    ResponseContentEntry respEntry = cache.lookup(url);
	    if ((respEntry != null) && (! respEntry.isValid())) {
		cache.remove(url);
		respEntry = null;
	    }
	    
	    if (respEntry != null) {
		respEntry.getContent().writeTo(response);
	    } else {
		respEntry = new ResponseContentEntry();
		CACHING_RESPONSE.set(respEntry);
		try {
		    CacheHttpServletResponseWrapper cacheResponse = new CacheHttpServletResponseWrapper(response);
		    chain.doFilter(request, cacheResponse);
		    cacheResponse.flushBuffer();
		    
		    // Only cache if the response was 200
		    if (cacheResponse.getStatus() == HttpServletResponse.SC_OK) {
			//Store as the cache content the result of the response
			respEntry.setContent(cacheResponse.getContent());
			cache.cache(url, respEntry);
		    }
		} finally {
		    CACHING_RESPONSE.remove();
		}
	    }
	} else {
	    chain.doFilter(request, response);
	}
    }

    public static String getPageURL(HttpServletRequest request) {
        String urlId = MappingUtils.currentURL(request);
        StringBuilder id = new StringBuilder(urlId);

        HttpSession httpSession = request.getSession(false);
        if (httpSession == null) {
            httpSession = request.getSession();
        }
        Locale locale = (Locale) httpSession.getAttribute(Globals.LOCALE_KEY);
        if (locale == null) {
            locale = Locale.getDefault();
            httpSession.setAttribute(Globals.LOCALE_KEY, locale);
        }
        String language = locale.getLanguage();
        id.append(language);

        return id.toString();
    }

    private boolean matchesExcludePattern(String id) {
	return StringUtils.contains(id, excludePattern);
    }

    public void beforeCommit(TopLevelTransaction tx) {
	ResponseContentEntry entry = CACHING_RESPONSE.get();
	if (entry != null) {
	    entry.addReadSet(tx.getReadSet());
	}
    }

    public void afterCommit(TopLevelTransaction tx) {
	// nop
    }
}
