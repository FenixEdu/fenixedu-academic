package net.sourceforge.fenixedu.presentationTier.servlets.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import pt.ist.fenixWebFramework.servlets.filters.contentRewrite.ResponseWrapper;

public class RequestRewriteFilter extends pt.ist.fenixWebFramework.servlets.filters.contentRewrite.RequestRewriterFilter implements Filter {

    protected void writeResponse(final FilterChain filterChain, final HttpServletRequest httpServletRequest,
	    final ResponseWrapper responseWrapper) throws IOException, ServletException {
	responseWrapper.writeRealResponse(new ContentInjectionRewriter(httpServletRequest), new ChecksumRewriter(httpServletRequest));
    }

}