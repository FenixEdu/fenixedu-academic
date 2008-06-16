package net.sourceforge.fenixedu.presentationTier.servlets.filters.authentication.cas;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu._development.PropertiesManager;

/**
 * 
 * @author naat
 * 
 */
public class CASFilter implements Filter {

    private static final String URL_ENCODING = "UTF-8";

    private String casLoginUrl;

    private boolean enabled;

    private final Map<String, String> serviceUrlsByHostnameMap = new HashMap<String, String>();

    public void init(final FilterConfig config) throws ServletException {
	enabled = Boolean.valueOf(PropertiesManager.getProperty("cas.enabled"));
	casLoginUrl = enabled ? PropertiesManager.getProperty("cas.loginUrl") : null;

	if (enabled) {
	    final String propertiesFilename = "/.casServiceUrlHostnames.properties";
	    try {
		final Properties properties = new Properties();
		PropertiesManager.loadProperties(properties, propertiesFilename);
		for (final Iterator iterator = properties.entrySet().iterator(); iterator.hasNext();) {
		    final Entry entry = (Entry) iterator.next();
		    final String serviceUrlByHostnameKey = (String) entry.getKey();
		    final String serviceUrl = (String) entry.getValue();
		    final String hostname = serviceUrlByHostnameKey.substring("cas.serviceUrl.".length());

		    serviceUrlsByHostnameMap.put(hostname, serviceUrl);
		}
	    } catch (IOException e) {
		throw new RuntimeException(e);
	    }
	}
    }

    public void doFilter(final ServletRequest request, final ServletResponse response, FilterChain fc) throws ServletException,
	    IOException {

	if (enabled) {
	    if (!isHttpResource(request, response)) {
		throw new ServletException("CASFilter only applies to HTTP resources");
	    }

	    if (notHasTicket(request)) {
		// send user to CAS to get a ticket
		redirectToCAS((HttpServletRequest) request, (HttpServletResponse) response);
		return;
	    }
	}

	fc.doFilter(request, response);

    }

    private boolean isHttpResource(final ServletRequest request, final ServletResponse response) {
	return request instanceof HttpServletRequest && response instanceof HttpServletResponse;
    }

    private boolean notHasTicket(final ServletRequest request) {
	final String ticket = request.getParameter("ticket");
	return ticket == null || ticket.equals("");
    }

    protected String encodeUrl(final String url) throws UnsupportedEncodingException {
	return URLEncoder.encode(getServiceUrl(url), URL_ENCODING);
    }

    /**
     * Redirects the user to CAS, determining the service from the request.
     */
    private void redirectToCAS(final HttpServletRequest request, final HttpServletResponse response) throws IOException,
	    ServletException {
	final String serviceString = encodeUrl(request.getRequestURL().toString());
	final String casLoginString = casLoginUrl + "?service=" + serviceString;
	response.sendRedirect(casLoginString);
    }

    public void destroy() {
    }

    private String getServiceUrl(final String requestURL) {
	URL location;
	try {
	    location = new URL(requestURL);
	} catch (MalformedURLException e) {
	    return null;
	}
	for (final Entry<String, String> entry : serviceUrlsByHostnameMap.entrySet()) {
	    final String hostname = entry.getKey();
	    if (location.getHost().startsWith(hostname)) {
		return entry.getValue();
	    }
	}

	return null;
    }

}
