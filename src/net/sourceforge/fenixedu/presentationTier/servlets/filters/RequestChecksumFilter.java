package net.sourceforge.fenixedu.presentationTier.servlets.filters;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.TreeSet;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu._development.LogLevel;
import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionUtils;
import net.sourceforge.fenixedu.presentationTier.Action.utils.RequestUtils;
import net.sourceforge.fenixedu.presentationTier.util.HostRedirector;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.fileupload.FileUpload;

public class RequestChecksumFilter implements Filter {

    private static final boolean APPLY_FILTER = PropertiesManager.getBooleanProperty("filter.request.with.digest");

    public static final String CHECKSUM_ATTRIBUTE_NAME = "_request_checksum_";

    private static class BufferedFacadServletOutputStream extends ServletOutputStream {

	final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

	final OutputStream realOutputStream;

	public BufferedFacadServletOutputStream(final OutputStream realOutputStream) {
	    this.realOutputStream = realOutputStream;
	}

	@Override
	public void write(final int value) throws IOException {
	    byteArrayOutputStream.write(value);
	}

	@Override
	public void write(final byte[] value) throws IOException {
	    byteArrayOutputStream.write(value);
	}

	@Override
	public void write(final byte[] value, final int off, final int len) throws IOException {
	    byteArrayOutputStream.write(value, off, len);
	}

	public void writeRealResponse() throws IOException {
	    realOutputStream.write(byteArrayOutputStream.toByteArray());
	    realOutputStream.flush();
	    realOutputStream.close();
	}
    }

    private static class BufferedFacadPrintWriter extends PrintWriter {

	final StringBuilder stringBuilder = new StringBuilder();

	final PrintWriter printWriter;

	public BufferedFacadPrintWriter(final PrintWriter printWriter) {
	    super(printWriter);
	    this.printWriter = printWriter;
	}

	@Override
	public void write(final char[] cbuf) {
	    stringBuilder.append(cbuf);
	}

	@Override
	public void write(final char[] cbuf, final int off, final int len) {
	    stringBuilder.append(cbuf, off, len);
	}

	@Override
	public void write(final int c) {
	    stringBuilder.append((char) c);
	}

	@Override
	public void write(final String str) {
	    stringBuilder.append(str);
	}

	@Override
	public void write(final String str, final int off, final int len) {
	    stringBuilder.append(str, off, len);
	}

	@Override
	public void flush()  {
	}

	@Override
	public void close() {
	}

	public void writeRealResponse() {
	    printWriter.write(getResponseWithCheckSums());
	    printWriter.flush();
	    printWriter.close();	    
	}

	private String getResponseWithCheckSums() {
	    final StringBuilder response = new StringBuilder();

	    rewrite(response, stringBuilder, 0);
//	    response.append(stringBuilder);

	    return response.toString();
	}

	private void rewrite(final StringBuilder response, final StringBuilder source, final int iOffset) {
	    final int indexOfAopen = source.indexOf("<a ", iOffset);
	    final int indexOfFormOpen = source.indexOf("<form ", iOffset);
	    final int indexOfImgOpen = source.indexOf("<img ", iOffset);
	    final int indexOfAreaOpen = source.indexOf("<area ", iOffset);
	    if (indexOfAopen >= 0 && (indexOfFormOpen < 0 || indexOfAopen < indexOfFormOpen)
		    && (indexOfImgOpen < 0 || indexOfAopen < indexOfImgOpen)
		    && (indexOfAreaOpen < 0 || indexOfAopen < indexOfAreaOpen)) {
		final int indexOfAclose = source.indexOf(">", indexOfAopen);
		if (indexOfAclose >= 0) {
		    final int indexOfHrefBodyStart = findHrefBodyStart(source, indexOfAopen, indexOfAclose);
		    if (indexOfHrefBodyStart >= 0) {
			final char hrefBodyStartChar = source.charAt(indexOfHrefBodyStart - 1);
			final int indexOfHrefBodyEnd = findHrefBodyEnd(source, indexOfHrefBodyStart, hrefBodyStartChar);
			if (indexOfHrefBodyEnd >= 0) {

			    final int indexOfJavaScript = source.indexOf("javascript:", indexOfHrefBodyStart);
			    final boolean isJavascriptLink = !(indexOfJavaScript < 0 || indexOfJavaScript > indexOfHrefBodyEnd);

			    final int indexOfPublic = source.indexOf("publico/", indexOfHrefBodyStart);
			    final boolean isPublicLink = !(indexOfPublic < 0 || indexOfPublic > indexOfHrefBodyEnd);

			    final int indexOfHrefSep = source.indexOf("://", indexOfHrefBodyStart);
			    final int indexOfHrefSepAndFenix = source.indexOf("://fenix", indexOfHrefBodyStart);
			    final boolean isExternalList = !(indexOfHrefSep < 0 || indexOfHrefSep > indexOfHrefBodyEnd) && indexOfHrefSep != indexOfHrefSepAndFenix;

	                     final int indexOfMailto = source.indexOf("mailto:", indexOfHrefBodyStart);
	                     final boolean isMailtoLink = !(indexOfMailto < 0 || indexOfMailto > indexOfHrefBodyEnd);

			    final int indexOfCardinal = source.indexOf("#", indexOfHrefBodyStart);
			    boolean hasCardinal = indexOfCardinal > indexOfHrefBodyStart && indexOfCardinal < indexOfHrefBodyEnd;
			    if (hasCardinal) {
				response.append(source, iOffset, indexOfCardinal);
			    } else {
				response.append(source, iOffset, indexOfHrefBodyEnd);
			    }

			    final String checksum = calculateChecksum(source, indexOfHrefBodyStart, indexOfHrefBodyEnd);
			    final int indexOfQmark = source.indexOf("?", indexOfHrefBodyStart);
			    if (!isJavascriptLink && !isPublicLink && !isExternalList && !isMailtoLink) {
			        if (indexOfQmark == -1 || indexOfQmark > indexOfHrefBodyEnd) {
			            response.append('?');
			        } else {
			            response.append("&amp;");
			        }
			        response.append(CHECKSUM_ATTRIBUTE_NAME);
			        response.append("=");
			        response.append(checksum);
			    }

			    if (hasCardinal) {
				response.append(source, indexOfCardinal, indexOfHrefBodyEnd);
			    }

			    final int nextChar = indexOfAclose + 1;
			    response.append(source, indexOfHrefBodyEnd, nextChar);
			    rewrite(response, source, nextChar);
			    return;
			}
		    } else {
			response.append(source, iOffset, indexOfAopen + 1);
			rewrite(response, source, indexOfAopen + 2);
			return;
		    }
		}
	    } else if (indexOfFormOpen >= 0 && (indexOfImgOpen < 0 || indexOfFormOpen < indexOfImgOpen)
		    && (indexOfAreaOpen < 0 || indexOfFormOpen < indexOfAreaOpen)) {
		final int indexOfFormClose = source.indexOf(">", indexOfFormOpen);
		if (indexOfFormClose >= 0) {
		    final int indexOfFormActionBodyStart = findFormActionBodyStart(source, indexOfFormOpen, indexOfFormClose);
		    if (indexOfFormActionBodyStart >= 0) {
			final int indexOfFormActionBodyEnd = findFormActionBodyEnd(source, indexOfFormActionBodyStart);
			if (indexOfFormActionBodyEnd >= 0) {
			    final int nextChar = indexOfFormClose + 1;
			    response.append(source, iOffset, nextChar);
			    final String checksum = calculateChecksum(source, indexOfFormActionBodyStart, indexOfFormActionBodyEnd);
			    response.append("<input type=\"hidden\" name=\"");
			    response.append(CHECKSUM_ATTRIBUTE_NAME);
			    response.append("\" value=\"");
			    response.append(checksum);
			    response.append("\"/>");
			    rewrite(response, source, nextChar);
			    return;
			}
		    }
		}
	    } else if (indexOfImgOpen >= 0 && (indexOfAreaOpen < 0 || indexOfImgOpen < indexOfAreaOpen)) {
		final int indexOfImgClose = source.indexOf(">", indexOfImgOpen);
		if (indexOfImgClose >= 0) {
		    final int indexOfSrcBodyStart = findSrcBodyStart(source, indexOfImgOpen, indexOfImgClose);
		    if (indexOfSrcBodyStart >= 0) {
			final int indexOfSrcBodyEnd = findSrcBodyEnd(source, indexOfSrcBodyStart);
			if (indexOfSrcBodyEnd >= 0) {
			    response.append(source, iOffset, indexOfSrcBodyEnd);

			    final String checksum = calculateChecksum(source, indexOfSrcBodyStart, indexOfSrcBodyEnd);
			    final int indexOfQmark = source.indexOf("?", indexOfSrcBodyStart);
			    if (indexOfQmark == -1 || indexOfQmark > indexOfSrcBodyEnd) {
				response.append('?');
			    } else {
				response.append("&amp;");
			    }
			    response.append(CHECKSUM_ATTRIBUTE_NAME);
			    response.append("=");
			    response.append(checksum);

			    final int nextChar = indexOfImgClose + 1;
			    response.append(source, indexOfSrcBodyEnd, nextChar);
			    rewrite(response, source, nextChar);
			    return;
			}
		    }
		}
	    } else if (indexOfAreaOpen >= 0) {
		final int indexOfAreaClose = source.indexOf(">", indexOfAreaOpen);
		if (indexOfAreaClose >= 0) {
		    final int indexOfHrefBodyStart = findHrefBodyStart(source, indexOfAreaOpen, indexOfAreaClose);
		    if (indexOfHrefBodyStart >= 0) {
			final char hrefBodyStartChar = source.charAt(indexOfHrefBodyStart - 1);
			final int indexOfHrefBodyEnd = findHrefBodyEnd(source, indexOfHrefBodyStart, hrefBodyStartChar);
			if (indexOfHrefBodyEnd >= 0) {
			    final int indexOfCardinal = source.indexOf("#", indexOfHrefBodyStart);
			    boolean hasCardinal = indexOfCardinal > indexOfHrefBodyStart && indexOfCardinal < indexOfHrefBodyEnd;
			    if (hasCardinal) {
				response.append(source, iOffset, indexOfCardinal);
			    } else {
				response.append(source, iOffset, indexOfHrefBodyEnd);
			    }

			    final String checksum = calculateChecksum(source, indexOfHrefBodyStart, indexOfHrefBodyEnd);
			    final int indexOfQmark = source.indexOf("?", indexOfHrefBodyStart);
			    if (indexOfQmark == -1 || indexOfQmark > indexOfHrefBodyEnd) {
				response.append('?');
			    } else {
				response.append("&amp;");
			    }
			    response.append(CHECKSUM_ATTRIBUTE_NAME);
			    response.append("=");
			    response.append(checksum);

			    if (hasCardinal) {
				response.append(source, indexOfCardinal, indexOfHrefBodyEnd);
			    }

			    final int nextChar = indexOfAreaClose + 1;
			    response.append(source, indexOfHrefBodyEnd, nextChar);
			    rewrite(response, source, nextChar);
			    return;
			}
		    }
		}
	    }
	    response.append(source.substring(iOffset));
	}

	private int findFormActionBodyEnd(final StringBuilder source, final int offset) {
	    int i = offset;
	    for (char c = source.charAt(i); c != '"' && c != '\'' && c != ' ' && c != '>'; c = source.charAt(i)) {
		if (++i == source.length()) {
		    return -1;
		}
	    }
	    return i;
	}

	private int findFormActionBodyStart(final StringBuilder source, final int offset, final int limit) {
	    final int indexOfHref = source.indexOf("action=", offset);
	    if (indexOfHref >= limit) {
		return -1;
	    }
	    final int nextChar = indexOfHref + 7;
	    return source.charAt(nextChar) == '"' || source.charAt(nextChar) == '\'' ? nextChar + 1 : nextChar;
	}

	private int findHrefBodyEnd(final StringBuilder source, final int offset, final char hrefBodyStartChar) {
	    int i = offset;
	    if (hrefBodyStartChar == '=') {
		for (char c = source.charAt(i); c != ' ' && c != '>'; c = source.charAt(i)) {
		    if (++i == source.length()) {
			return -1;
		    }
		}
	    } else {
		for (char c = source.charAt(i); c != hrefBodyStartChar; c = source.charAt(i)) {
		    if (++i == source.length()) {
			return -1;
		    }
		}		
	    }
	    return i;
	}

	private int findSrcBodyEnd(final StringBuilder source, final int offset) {
	    int i = offset;
	    for (char c = source.charAt(i); c != '"' && c != '\'' && c != ' ' && c != '>'; c = source.charAt(i)) {
		if (++i == source.length()) {
		    return -1;
		}
	    }
	    return i;
	}

	private int findBodyStart(final StringBuilder source, final String attribute, final int offset, int limit) {
	    final int indexOfHref = source.indexOf(attribute, offset);
	    if (indexOfHref >= limit) {
		return -1;
	    }
	    final int nextCharPos = indexOfHref + attribute.length();
	    final int indexOfNextDoubleQuote = source.indexOf("\"", nextCharPos);
	    final int indexOfNextQuote = source.indexOf("'", nextCharPos);
	    final int indexOfNextVisibleChar = findVisibleChar(source, nextCharPos);

	    if (indexOfNextDoubleQuote >= 0
		    && (indexOfNextDoubleQuote < indexOfNextQuote || indexOfNextQuote < 0)
		    && indexOfNextDoubleQuote <= indexOfNextVisibleChar) {
		return indexOfNextDoubleQuote + 1;
	    }
	    if (indexOfNextQuote >= 0
		    && (indexOfNextQuote < indexOfNextDoubleQuote || indexOfNextDoubleQuote < 0)
		    && indexOfNextQuote <= indexOfNextVisibleChar) {
		return indexOfNextQuote + 1;
	    }
	    if ((indexOfNextVisibleChar < indexOfNextDoubleQuote || indexOfNextDoubleQuote < 0)
		    && (indexOfNextVisibleChar < indexOfNextQuote || indexOfNextQuote < 0)){
		return indexOfNextVisibleChar;
	    }

	    return nextCharPos;
	}

	private int findVisibleChar(final StringBuilder source, final int offset) {
	    for (int i = offset; i < source.length(); i++) {
		final char c = source.charAt(i);
		if (c != ' ' && c != '\t' && c != '\n') {
		    return i;
		}
	    }
	    return Integer.MAX_VALUE;
	}

	private int findHrefBodyStart(final StringBuilder source, final int offset, int limit) {
	    return findBodyStart(source, "href=", offset, limit);
	}

	private int findSrcBodyStart(final StringBuilder source, final int offset, int limit) {
	    return findBodyStart(source, "src=", offset, limit);
	}

    }

    private static class ResponseWrapper extends HttpServletResponseWrapper {

	final HttpServletResponse httpServletResponse;

	BufferedFacadServletOutputStream bufferedFacadServletOutputStream = null;

	BufferedFacadPrintWriter bufferedFacadPrintWriter = null;

	public ResponseWrapper(final HttpServletResponse httpServletResponse) throws IOException {
	    super(httpServletResponse);
	    this.httpServletResponse = httpServletResponse;
	}

	@Override
	public ServletOutputStream getOutputStream() throws IOException {
	    if (bufferedFacadServletOutputStream == null) {
		bufferedFacadServletOutputStream = new BufferedFacadServletOutputStream(httpServletResponse.getOutputStream());
	    }
	    return bufferedFacadServletOutputStream;
	}

	@Override
	public PrintWriter getWriter() throws IOException {
	    if (bufferedFacadPrintWriter == null) {
		bufferedFacadPrintWriter = new BufferedFacadPrintWriter(httpServletResponse.getWriter());
	    }
	    return bufferedFacadPrintWriter;
	}

	@Override
	public void flushBuffer() throws IOException {
	}

	public void writeRealResponse() throws IOException {
	    if (bufferedFacadServletOutputStream != null) {
		bufferedFacadServletOutputStream.writeRealResponse();
	    }
	    if (bufferedFacadPrintWriter != null) {
		bufferedFacadPrintWriter.writeRealResponse();
	    }
	}
    }

    private static String calculateChecksum(final StringBuilder source, final int start, final int end) {
	return calculateChecksum(source.substring(start, end));
    }

    public static String calculateChecksum(final String requestString) {
	final int indexLastCardinal = requestString.lastIndexOf('#');
	final String string = indexLastCardinal >= 0 ? requestString.substring(0, indexLastCardinal) : requestString;
	final String[] parts = string.split("\\?|&amp;|&");

	final TreeSet<String> strings = new TreeSet<String>();
	for (final String part : parts) {
	    if (isRelevantPart(part)) {
		final int indexOfEquals = part.indexOf('=');
		if (indexOfEquals >= 0) {
		    strings.add(part.substring(0, indexOfEquals));
		    strings.add(part.substring(indexOfEquals + 1, part.length()));
		} else {
		    strings.add(part);
		}
	    }
	}
	return calculateChecksum(strings);
    }

    private static boolean isRelevantPart(final String part) {
	return part.length() > 0
		&& !part.startsWith(CHECKSUM_ATTRIBUTE_NAME)
		&& !part.startsWith("page=")
		&& !part.startsWith("org.apache.struts.action.LOCALE")
		&& !part.startsWith("javax.servlet.request.")
		&& !part.startsWith("ok");
    }

    private static String calculateChecksum(final TreeSet<String> strings) {
	final StringBuilder stringBuilder = new StringBuilder();
	for (final String string : strings) {
	    stringBuilder.append(string);
	}
	final IUserView userView = AccessControl.getUserView();
	stringBuilder.append(userView.getPrivateConstantForDigestCalculation());
	final String checksum = new String(DigestUtils.shaHex(stringBuilder.toString()));
//	System.out.println("Generating checksum for: " + stringBuilder.toString() + " --> " + checksum);
	return checksum;
    }

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
	final HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
	if (shoudFilterReques(httpServletRequest)) {
	    verifyRequestChecksum(httpServletRequest);
	}

	final ResponseWrapper responseWrapper = new ResponseWrapper(httpServletResponse);

//	final long start1 = System.currentTimeMillis();
	if (isRedirectRequest(httpServletRequest)) {
	    filterChain.doFilter(servletRequest, servletResponse);
	} else {
	    filterChain.doFilter(servletRequest, responseWrapper);
	}
//	final long end1 = System.currentTimeMillis();

//	final long start2 = System.currentTimeMillis();
	responseWrapper.writeRealResponse();
//	final long end2 = System.currentTimeMillis();

//	final long time1 = end1 - start1;
//	final long time2 = end2 - start2;
//	final long percent = time1 == 0 ? 0 : (100 * time2) / time1;
//	System.out.println("Actual response took: " + time1 + " ms. Parse and replace took: " + time2 + " ms. Performance loss: " + percent + " %.");
    }

    private boolean isRedirectRequest(final HttpServletRequest httpServletRequest) {
	final String uri = httpServletRequest.getRequestURI().substring(RequestUtils.APP_CONTEXT_LENGTH);
	return uri.indexOf("redirect.do") >= 0;
    }

    private boolean shoudFilterReques(final HttpServletRequest httpServletRequest) {
	final String uri = httpServletRequest.getRequestURI().substring(RequestUtils.APP_CONTEXT_LENGTH);
	if (uri.indexOf("domainbrowser/") >= 0) {
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
	if(uri.indexOf("notAuthorized.do") >= 0){
	    return false;
	}
	
	return RequestUtils.isPrivateURI(httpServletRequest);
    }

    public static class UrlTamperingException extends Error {
        public UrlTamperingException() {
            super("error.url.tampering");
        }
    }

    private void verifyRequestChecksum(final HttpServletRequest httpServletRequest) {
	String checksum = httpServletRequest.getParameter(CHECKSUM_ATTRIBUTE_NAME);
	if (checksum == null || checksum.length() == 0) {
	    checksum = (String) httpServletRequest.getAttribute(CHECKSUM_ATTRIBUTE_NAME);
	}
	if (!isValidChecksum(httpServletRequest, checksum)) {
	    final IUserView userView = SessionUtils.getUserView(httpServletRequest);
	    final String userString = userView == null ? "<no user logged in>" : userView.getUtilizador();
	    if (LogLevel.ERROR) {
		final String url = httpServletRequest.getRequestURI() + '?' + httpServletRequest.getQueryString();
	        System.out.println("Detected url tampering for request: " + url + " by user: " + userString);	        
	        System.out.println(decodeURL(url));
	    }
	    throw new UrlTamperingException();
	}
    }

    private String decodeURL(final String url) {
        if (url == null) {
            return null;
        }
        try {
            return URLDecoder.decode(url, "iso8859-1");
        } catch (UnsupportedEncodingException e) {
            return url;
        }
    }

    private boolean isValidChecksum(final HttpServletRequest httpServletRequest, final String checksum) {
	final String uri = httpServletRequest.getRequestURI();
	final String queryStringEncoded = httpServletRequest.getQueryString();
	final String queryString = decodeURL(queryStringEncoded);
	return isValidChecksum(uri, queryString, checksum) || isValidChecksum(uri, queryStringEncoded, checksum)
	        || isValidChecksumIgnoringPath(httpServletRequest, checksum);
    }

    private boolean isValidChecksumIgnoringPath(final HttpServletRequest httpServletRequest, final String checksum) {
        final String uri = httpServletRequest.getRequestURI();
        if (uri.endsWith(".faces")) {
            final int lastSlash = uri.lastIndexOf('/');
            if (lastSlash >= 0) {
                final String chopedUri = uri.substring(lastSlash + 1);
                final String queryStringEncoded = httpServletRequest.getQueryString();
                final String queryString = decodeURL(queryStringEncoded);
                return isValidChecksum(chopedUri, queryString, checksum) || isValidChecksum(chopedUri, queryStringEncoded, checksum);
            }
        }
        return false;
    }

    private boolean isValidChecksum(final String uri, final String queryString, final String checksum) {
        final String request = queryString != null ? uri + '?' + queryString : uri;
        final String calculatedChecksum = calculateChecksum(request);
        return checksum != null && checksum.length() > 0 && checksum.equals(calculatedChecksum);
    }

}