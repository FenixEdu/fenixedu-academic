/*
 * Created on Jun 25, 2004
 *  
 */
package ServidorApresentacao.servlets.filters;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Enumeration;
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

import ServidorApresentacao.servlets.filters.cache.ResponseCacheOSCacheImpl;

import com.opensymphony.oscache.web.filter.CacheHttpServletResponseWrapper;
import com.opensymphony.oscache.web.filter.ResponseContent;

/**
 * @author Luis Cruz
 *  
 */
public class FenixCacheFilter extends RequestWrapperFilter implements Filter {

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

    public void doFilter(ServletRequest req, ServletResponse res,
            FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        // check if was a resource that shouldn't be cached.
        String uri = request.getRequestURI();
        if ((uri == null) || (uri.indexOf("/publico/") == -1)) {
        	super.doFilter(request, response, chain);
            return;
        }

        // customize to match parameters
        String queryString = constructQueryString(request);
        StringBuffer id = new StringBuffer(request.getRequestURI());
        if (queryString != null) {
        	id.append("?");
        	id.append(queryString);
        }
        // optionally append i18n sensitivity
        String localeSensitive = this.filterConfig.getInitParameter("locale-sensitive");
        if (localeSensitive != null) {
            StringWriter ldata = new StringWriter();
            Enumeration locales = request.getLocales();
            while (locales.hasMoreElements()) {
                Locale locale = (Locale) locales.nextElement();
                ldata.write(locale.getISO3Language());
            }
            id.append(ldata.toString());
        }

        ResponseContent respContent = ResponseCacheOSCacheImpl.getInstance().lookup(id.toString());
        if (respContent != null) {
        	respContent.writeTo(response);
        } else {
            CacheHttpServletResponseWrapper cacheResponse = new CacheHttpServletResponseWrapper(response);
            super.doFilter(request, cacheResponse, chain);
            cacheResponse.flushBuffer();

            // Only cache if the response was 200
            if (cacheResponse.getStatus() == HttpServletResponse.SC_OK) {
                //Store as the cache content the result of the response
            	ResponseCacheOSCacheImpl.getInstance().cache(id.toString(), cacheResponse.getContent());
            }        	
        }
    }

	private String constructQueryString(HttpServletRequest request) {
		StringBuffer queryString = new StringBuffer();
		
		String requestQueryString = request.getQueryString();
		if (requestQueryString != null) {
			queryString.append(requestQueryString);
		}

		Enumeration parameterNames = request.getParameterNames();
		if (parameterNames != null) {
			while(parameterNames.hasMoreElements()) {
				String parameterName = (String) parameterNames.nextElement();
				String parameterValue = request.getParameter(parameterName);
				if (queryString.length() != 0) {
					queryString.append("&");
				}
				queryString.append(parameterName);
				queryString.append("=");
				queryString.append(parameterValue);
			}
		}

		if (queryString.length() != 0) {
			return queryString.toString();
		} 
			return null;
		
	}

}