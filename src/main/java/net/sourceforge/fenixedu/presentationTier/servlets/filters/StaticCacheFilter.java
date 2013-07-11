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

import org.joda.time.DateTime;

public class StaticCacheFilter implements Filter {

    private static final String[] staticStuffix = { ".css", ".js", ".gif", ".png", ".jpg", ".jpeg" };

    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
            throws IOException, ServletException {
        final HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        final String uri = httpServletRequest.getRequestURI();
        if (isStaticContent(uri)) {
            final HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            httpServletResponse.setHeader("Expires", new DateTime().plusHours(12).toString("E, d MMM yyyy HH:mm:ss z"));
            httpServletResponse.setHeader("Cache-Control", "max-age=43200");
        }
        chain.doFilter(request, response);
    }

    private boolean isStaticContent(final String uri) {
        for (final String s : staticStuffix) {
            if (uri.endsWith(s)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void destroy() {
    }

}