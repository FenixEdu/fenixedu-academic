package net.sourceforge.fenixedu.presentationTier.servlets.filters;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.TreeSet;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu._development.LogLevel;
import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.contents.Container;
import net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionUtils;
import net.sourceforge.fenixedu.presentationTier.Action.utils.RequestUtils;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.functionalities.FilterFunctionalityContext;
import net.sourceforge.fenixedu.presentationTier.util.HostRedirector;

import org.apache.commons.fileupload.FileUpload;

public class RequestChecksumFilter implements Filter {

    private static final boolean APPLY_FILTER = PropertiesManager.getBooleanProperty("filter.request.with.digest");

    public void init(FilterConfig config) {
    }

    public void destroy() {
    }

    public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain)
            throws IOException, ServletException {
	if (APPLY_FILTER) {
	    try {
	        applyFilter(servletRequest, servletResponse, filterChain);
	    } catch (UrlTamperingException ex) {
	        final HttpServletRequest request = (HttpServletRequest) servletRequest;
	        final HttpServletResponse response = (HttpServletResponse) servletResponse;
	        final HttpSession httpSession = request.getSession(false);
	        if (httpSession != null) {
	            httpSession.invalidate();
	        }
	        response.sendRedirect(HostRedirector.getRedirectPageLogin(request.getRequestURL().toString()));
	    }
	} else {
	    filterChain.doFilter(servletRequest, servletResponse);
	}
    }

    private void applyFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain) throws IOException, ServletException {
	final HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
	if (shoudFilterReques(httpServletRequest)) {
	    verifyRequestChecksum(httpServletRequest);
	}

	filterChain.doFilter(servletRequest, servletResponse);
    }

    private boolean shoudFilterReques(final HttpServletRequest httpServletRequest) {
	final String uri = httpServletRequest.getRequestURI().substring(RequestUtils.APP_CONTEXT_LENGTH);
	if (uri.indexOf("domainbrowser/") >= 0) {
	    return false;
	}
	if (uri.indexOf("images/") >= 0) {
	    return false;
	}
        if (uri.indexOf("javaScript/") >= 0) {
            return false;
        }
	if (uri.indexOf("script/") >= 0) {
	    return false;
	}
	if (uri.indexOf("ajax/") >= 0) {
	    return false;
	}
	if (uri.indexOf("redirect.do") >= 0) {
	    return false;
	}
	if (uri.indexOf("home.do") >= 0) {
	    return false;
	}
	if (uri.indexOf("/student/fillInquiries.do") >= 0) {
	    return false;
	}
	if (FileUpload.isMultipartContent(httpServletRequest)) {
	    return false;
	}
	final FilterFunctionalityContext filterFunctionalityContext = getContextAttibute(httpServletRequest);
	if (filterFunctionalityContext != null) {
	    final Container container = filterFunctionalityContext.getSelectedTopLevelContainer();
	    if (container != null && container.getAvailabilityPolicy() == null) {
		return false;
	    }
	}
	if(uri.indexOf("notAuthorized.do") >= 0){
	    return false;
	}
	
	return RequestUtils.isPrivateURI(httpServletRequest);
    }

    private FilterFunctionalityContext getContextAttibute(final HttpServletRequest httpServletRequest) {
	return (FilterFunctionalityContext) httpServletRequest.getAttribute(FunctionalityContext.CONTEXT_KEY);
    }

    public static class UrlTamperingException extends Error {
        public UrlTamperingException() {
            super("error.url.tampering");
        }
    }

    private void verifyRequestChecksum(final HttpServletRequest httpServletRequest) {
	String checksum = httpServletRequest.getParameter(ChecksumRewriter.CHECKSUM_ATTRIBUTE_NAME);
	if (checksum == null || checksum.length() == 0) {
	    checksum = (String) httpServletRequest.getAttribute(ChecksumRewriter.CHECKSUM_ATTRIBUTE_NAME);
	}
	if (!isValidChecksum(httpServletRequest, checksum)) {
	    if (LogLevel.ERROR) {
		final IUserView userView = SessionUtils.getUserView(httpServletRequest);
		final String userString = userView == null ? "<no user logged in>" : userView.getUtilizador();
		final String url = httpServletRequest.getRequestURI() + '?' + httpServletRequest.getQueryString();
		final StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Detected url tampering by user: ");
		stringBuilder.append(userString);
		stringBuilder.append("\n           url: ");
		stringBuilder.append(url);
		stringBuilder.append("\n   decoded url iso-8859-1: ");
		stringBuilder.append(decodeURL(url, "ISO-8859-1"));
		stringBuilder.append("\n   decoded url utf-8: ");
		stringBuilder.append(decodeURL(url, "UTF-8"));
		stringBuilder.append("\n          from: ");
		stringBuilder.append(httpServletRequest.getRemoteHost());
		stringBuilder.append(" (");
		stringBuilder.append(httpServletRequest.getRemoteAddr());
		stringBuilder.append(")");
		for (final Enumeration<String> headerNames = httpServletRequest.getHeaderNames(); headerNames.hasMoreElements(); ) {
		    final String name = headerNames.nextElement();
		    stringBuilder.append("\n        header: ");
		    stringBuilder.append(name);
		    stringBuilder.append(" = ");
		    stringBuilder.append(httpServletRequest.getHeader(name));
		}

		System.out.println(stringBuilder.toString());
	    }
	    throw new UrlTamperingException();
	}
    }

    private String decodeURL(final String url, final String encoding) {
        if (url == null) {
            return null;
        }
        try {
            return URLDecoder.decode(url, encoding);
        } catch (UnsupportedEncodingException e) {
            return url;
        }
    }

    private boolean isValidChecksum(final HttpServletRequest httpServletRequest, final String checksum) {

	final String uri = decodeURL(httpServletRequest.getRequestURI(), "ISO-8859-1");
	final String uriUTF8 = decodeURL(httpServletRequest.getRequestURI(), "UTF-8");

	return isValidChecksum(uri, decodeURL(httpServletRequest.getQueryString(), "ISO-8859-1"),checksum) ||
		isValidChecksum(uriUTF8, decodeURL(httpServletRequest.getQueryString(), "UTF-8"),checksum) ||

		isValidChecksum(uri, httpServletRequest.getQueryString(),checksum) ||
		isValidChecksum(uriUTF8, httpServletRequest.getQueryString(),checksum) ||

		isValidChecksumIgnoringPath(httpServletRequest, checksum, "ISO-8859-1") ||
		isValidChecksumIgnoringPath(httpServletRequest, checksum, "UTF-8");
	
    }

    private boolean isValidChecksum(String uri, String queryString, String checksum) {
	String request = (queryString != null) ? uri + "?" + queryString : uri;
	return checksum != null && checksum.length() > 0 && checksum.equals(ChecksumRewriter.calculateChecksum(request));
    }
    
    private boolean isValidChecksumIgnoringPath(final HttpServletRequest httpServletRequest, final String checksum, final String encoding) {
        final String uri = decodeURL(httpServletRequest.getRequestURI(), encoding);
        if (uri.endsWith(".faces")) {
            final int lastSlash = uri.lastIndexOf('/');
            if (lastSlash >= 0) {
                final String chopedUri = uri.substring(lastSlash + 1);
                final String queryString = decodeURL(httpServletRequest.getQueryString(), encoding);
                final String request = queryString != null ? chopedUri + '?' + queryString : chopedUri;
                final String calculatedChecksum = ChecksumRewriter.calculateChecksum(request);
                return checksum != null && checksum.length() > 0 && checksum.equals(calculatedChecksum);
            }
        }
        return false;
    }

}