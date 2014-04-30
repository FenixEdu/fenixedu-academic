package net.sourceforge.fenixedu.presentationTier.servlets.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

@WebFilter(urlPatterns = { "*.css", "*.js", "*.gif", "*.png", "*.jpg", "*.jpeg", "*.woff", "*.svg" })
public class StaticCacheFilter implements Filter {

    private final DateTimeFormatter formatter = DateTimeFormat.forPattern("E, d MMM yyyy HH:mm:ss z");

    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
            throws IOException, ServletException {
        final HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Expires", formatter.print(DateTime.now().plusHours(12)));
        httpServletResponse.setHeader("Cache-Control", "max-age=43200");
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }

}