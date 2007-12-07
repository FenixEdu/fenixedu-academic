package net.sourceforge.fenixedu.presentationTier.servlets.filters.requestWrappers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext;

public class RequestWrapper extends HttpServletRequestWrapper {

    protected final String path;
    protected final String uri;
    protected final StringBuffer url;
    protected FunctionalityContext context = null;

    public RequestWrapper(final HttpServletRequest request) {
	super(request);
	uri = request.getRequestURI();
	url = request.getRequestURL();
	path = request.getServletPath();
    }

    public RequestWrapper(final HttpServletRequest request, final String path) {
	super(request);
	if (path == null) {
	    throw new NullPointerException("Path cannot be null.");
	}

	this.path = path;

	final String contextPath = request.getContextPath();
	uri = contextPath == null || contextPath.length() == 0 ?
		path : contextPath + path;

	url = new StringBuffer();
	url.append(request.getScheme());
	url.append("://");
	url.append(request.getServerName());
	url.append(':');
	url.append(request.getServerPort());
	url.append(uri);

	final StringBuffer originalUrl = request.getRequestURL();
	final int indexOfQmark = originalUrl.indexOf("?");
	if (indexOfQmark >= 0) {
	    url.append(originalUrl, indexOfQmark, originalUrl.length());
	}
    }

    public RequestWrapper(final HttpServletRequest httpServletRequest, final String path, final FunctionalityContext context) {
	this(httpServletRequest, path);
	this.context = context;
    }

}

