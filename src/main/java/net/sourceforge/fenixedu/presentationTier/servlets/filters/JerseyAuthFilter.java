/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.presentationTier.servlets.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixframework.plugins.remote.domain.RemoteHost;
import pt.ist.fenixframework.plugins.remote.domain.RemoteSystem;

@WebFilter(urlPatterns = "/api/fenix/jersey/services/*")
public class JerseyAuthFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(JerseyAuthFilter.class);

    final static String USERNAME_KEY = "__username__";
    final static String PASSWORD_KEY = "__password__";

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        final HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        final HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        doFilter(httpServletRequest, httpServletResponse, filterChain);
    }

    public void doFilter(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain)
            throws IOException, ServletException {
        if (checkAccessControl(request)) {
            filterChain.doFilter(request, response);
        } else {
            throw new ServletException("Not Authorized");
        }
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
    }

    private Boolean checkAccessControl(final HttpServletRequest request) {
        final String url = getClientAddress(request);
        final String username = request.getHeader(USERNAME_KEY);
        final String password = request.getHeader(PASSWORD_KEY);
        Boolean found = Boolean.FALSE;
        for (final RemoteHost remoteHost : RemoteSystem.getInstance().getRemoteHostsSet()) {
            if (remoteHost.matches(url, username, password)) {
                logger.info("[Jersey Server Invoke by client " + url);
                found = Boolean.TRUE;
            }
        }
        logger.info("[Jersey Server] Invoke by client " + url);
        return found;
    }

    private String getClientAddress(final HttpServletRequest request) {
        final String xForwardForHeader = request.getHeader("X-Forwarded-For");
        if (xForwardForHeader != null && !xForwardForHeader.isEmpty()) {
            final int urlSeperator = xForwardForHeader.indexOf(',');
            return urlSeperator > 0 ? xForwardForHeader.substring(0, urlSeperator) : xForwardForHeader;
        }
        return request.getRemoteHost();
    }
}
