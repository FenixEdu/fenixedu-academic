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
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.util.FenixConfigurationManager;

import org.fenixedu.bennu.core.filters.CasAuthenticationFilter;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.core.util.CoreConfiguration;
import org.fenixedu.bennu.core.util.CoreConfiguration.CasConfig;

public class CASFilter implements Filter {

    protected void redirectToCAS(final CasConfig casConfig, final HttpServletRequest request, final HttpServletResponse response)
            throws IOException, ServletException {
        if (Authenticate.getUser() == null) {
            final String service = encodeUrl(casConfig.getCasServiceUrl());
            String casLoginUrl = casConfig.getCasLoginUrl(service);
            if (FenixConfigurationManager.isBarraAsAuthenticationBroker()) {
                casLoginUrl = FenixConfigurationManager.getConfiguration().barraLoginUrl() + "?next=" + casLoginUrl;
            }
            response.sendRedirect(casLoginUrl);
        } else {
            response.sendRedirect(request.getContextPath() + "/home.do");
        }
    }

    private boolean isHttpResource(final ServletRequest request, final ServletResponse response) {
        return request instanceof HttpServletRequest && response instanceof HttpServletResponse;
    }

    private boolean shouldRedirectToCas(final HttpServletRequest request) {
        return !((Authenticate.isLogged() || request.getAttribute(CasAuthenticationFilter.AUTHENTICATION_EXCEPTION_KEY) != null) && request
                .getRequestURI().endsWith("/loginCAS.do"));
    }

    protected String encodeUrl(final String casUrl) throws UnsupportedEncodingException {
        return URLEncoder.encode(casUrl, "UTF-8");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        final CasConfig casConfig = CoreConfiguration.casConfig();
        if (casConfig != null && casConfig.isCasEnabled()) {
            if (!isHttpResource(servletRequest, servletResponse)) {
                throw new ServletException("CASFilter only applies to HTTP resources");
            }

            if (shouldRedirectToCas((HttpServletRequest) servletRequest)) {
                // send user to CAS to get a ticket
                redirectToCAS(casConfig, (HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse);
                return;
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
    }

    @Override
    public void destroy() {
    }

}
