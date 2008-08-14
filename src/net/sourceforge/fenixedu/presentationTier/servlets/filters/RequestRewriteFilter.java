package net.sourceforge.fenixedu.presentationTier.servlets.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu._development.LogLevel;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.requestWrappers.ResponseWrapper;

public class RequestRewriteFilter implements Filter {

    public static abstract class RequestRewriter {
	protected final HttpServletRequest httpServletRequest;

	public RequestRewriter(final HttpServletRequest httpServletRequest) {
	    this.httpServletRequest = httpServletRequest;
	}

	public abstract StringBuilder rewrite(final StringBuilder source);

	protected int findHrefBodyEnd(final StringBuilder source, final int offset, final char hrefBodyStartChar) {
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

	protected int findSrcBodyEnd(final StringBuilder source, final int offset) {
	    int i = offset;
	    char delimiter = source.charAt(offset - 1);
	    if (delimiter == '"' || delimiter == '\'') {
		for (char c = source.charAt(i); c != delimiter; c = source.charAt(i)) {
		    if (++i == source.length()) {
			return -1;
		    }
		}
	    } else {
		for (char c = source.charAt(i); c != ' ' && c != '>'; c = source.charAt(i)) {
		    if (++i == source.length()) {
			return -1;
		    }
		}
	    }
	    return i;
	}

	protected int findHrefBodyStart(final StringBuilder source, final int offset, int limit) {
	    final int indexOfHref = source.indexOf("href=", offset);
	    if (indexOfHref >= limit) {
		return -1;
	    }
	    final int nextChar = indexOfHref + 5;
	    return source.charAt(nextChar) == '"' || source.charAt(nextChar) == '\'' ? nextChar + 1 : nextChar;
	}

	protected int findSrcBodyStart(final StringBuilder source, final int offset, int limit) {
	    final int indexOfHref = source.indexOf("src=", offset);
	    if (indexOfHref >= limit) {
		return -1;
	    }
	    final int nextChar = indexOfHref + 5;
	    return source.charAt(nextChar) == '"' || source.charAt(nextChar) == '\'' ? nextChar + 1 : nextChar;
	}

	protected int findFormActionBodyEnd(final StringBuilder source, final int offset) {
	    int i = offset;
	    for (char c = source.charAt(i); c != '"' && c != '\'' && c != ' ' && c != '>'; c = source.charAt(i)) {
		if (++i == source.length()) {
		    return -1;
		}
	    }
	    return i;
	}

	protected int findFormActionBodyStart(final StringBuilder source, final int offset, final int limit) {
	    final int indexOfHref = source.indexOf("action=", offset);
	    if (indexOfHref >= limit) {
		return -1;
	    }
	    final int nextChar = indexOfHref + 7;
	    return source.charAt(nextChar) == '"' || source.charAt(nextChar) == '\'' ? nextChar + 1 : nextChar;
	}
    }

    public void init(FilterConfig config) {
    }

    public void destroy() {
    }

    public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain)
	    throws IOException, ServletException {
	final HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
	final HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

	final ResponseWrapper responseWrapper = new ResponseWrapper(httpServletResponse);

	if (LogLevel.DEBUG) {
	    continueChainAndWriteResponseWithTimeLog(filterChain, httpServletRequest, responseWrapper);
	} else {
	    continueChainAndWriteResponse(filterChain, httpServletRequest, responseWrapper);
	}
    }

    private void continueChainAndWriteResponse(final FilterChain filterChain, final HttpServletRequest httpServletRequest,
	    final ResponseWrapper responseWrapper) throws IOException, ServletException {
	filterChain.doFilter(httpServletRequest, responseWrapper);
	writeResponse(filterChain, httpServletRequest, responseWrapper);
    }

    private void continueChainAndWriteResponseWithTimeLog(final FilterChain filterChain,
	    final HttpServletRequest httpServletRequest, final ResponseWrapper responseWrapper) throws IOException,
	    ServletException {
	final long start1 = System.currentTimeMillis();
	filterChain.doFilter(httpServletRequest, responseWrapper);
	final long end1 = System.currentTimeMillis();

	final long start2 = System.currentTimeMillis();
	writeResponse(filterChain, httpServletRequest, responseWrapper);
	final long end2 = System.currentTimeMillis();

	final long time1 = end1 - start1;
	final long time2 = end2 - start2;
	final long percent = time1 == 0 ? 0 : (100 * time2) / time1;
	System.out.println("Actual response took: " + time1 + " ms. Parse and replace took: " + time2 + " ms. Performance loss: "
		+ percent + " %.");
    }

    private void writeResponse(final FilterChain filterChain, final HttpServletRequest httpServletRequest,
	    final ResponseWrapper responseWrapper) throws IOException, ServletException {
	responseWrapper.writeRealResponse(new ContentInjectionRewriter(httpServletRequest), new ChecksumRewriter(
		httpServletRequest));
    }

}