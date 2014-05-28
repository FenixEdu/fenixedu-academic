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
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu._development.OAuthProperties;
import net.sourceforge.fenixedu.domain.AppUserSession;
import net.sourceforge.fenixedu.domain.AuthScope;
import net.sourceforge.fenixedu.domain.ExternalApplication;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.FenixOAuthToken.FenixOAuthTokenException;
import net.sourceforge.fenixedu.webServices.jersey.api.FenixJerseyAPIConfig;

import org.apache.amber.oauth2.common.exception.OAuthSystemException;
import org.apache.amber.oauth2.common.message.OAuthResponse;
import org.apache.amber.oauth2.common.message.OAuthResponse.OAuthErrorResponseBuilder;
import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

@WebFilter(urlPatterns = "/api/fenix/v1/*")
public class JerseyOAuth2Filter implements Filter {

    final static String ACCESS_TOKEN = "access_token";

    private static boolean allowIstIds = OAuthProperties.getConfiguration().getFenixApiAllowIstIds();

    @Override
    public void destroy() {
    }

    public static synchronized void toggleAllowIstIds() {
        allowIstIds = !allowIstIds;
    }

    public static synchronized boolean allowIstIds() {
        return allowIstIds;
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
        if (checkAccessControl(request, response)) {
            try {
                filterChain.doFilter(request, response);
            } finally {
                Authenticate.unmock();
            }
        }
    }

    private boolean checkAccessControl(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        if (allowIstIds() && currentUserIsManager()) {
            String istId = request.getParameter("_istid_");
            if (!StringUtils.isBlank(istId)) {
                User user = User.findByUsername(istId);
                if (user != null) {
                    authenticateUser(request, user);
                    return true;
                } else {
                    throw new ServletException("user.not.found");
                }
            }
        }
        return checkAccessToken(request, response);
    }

    private boolean currentUserIsManager() {
        User userview = Authenticate.getUser();
        if (userview == null) {
            return false;
        }
        Person person = userview.getPerson();
        if (person == null) {
            return false;
        }

        return person.hasRole(RoleType.MANAGER);
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
    }

    private Boolean checkAccessToken(final HttpServletRequest request, final HttpServletResponse response) throws IOException,
            ServletException {

//        final String uri =
//                StringUtils.removeStart(request.getRequestURI(), request.getContextPath() + request.getServletPath() + "/fenix/");

        final String uri = StringUtils.removeStart(request.getRequestURI(), request.getContextPath() + "/api/");

        if (FenixJerseyAPIConfig.isPublicScope(uri)) {
            return true;
        }

        String accessToken = request.getHeader(ACCESS_TOKEN);

        if (StringUtils.isBlank(accessToken)) {
            accessToken = request.getParameter(ACCESS_TOKEN);
        }

        try {
            FenixOAuthToken fenixAccessToken = FenixOAuthToken.parse(accessToken);
            AppUserSession appUserSession = fenixAccessToken.getAppUserSession();
            ExternalApplication externalApplication = appUserSession.getAppUserAuthorization().getApplication();

            if (externalApplication.isDeleted()) {
                return sendError(response, "accessTokenInvalidFormat", "Access Token not recognized.");
            }

            if (externalApplication.isBanned()) {
                return sendError(response, "appBanned", "The application has been banned.");
            }

            if (!validScope(appUserSession, uri)) {
                return sendError(response, "invalidScope", "Application doesn't have permissions to this endpoint.");
            }

            if (!appUserSession.matchesAccessToken(accessToken)) {
                return sendError(response, "accessTokenInvalid", "Access Token doesn't match.");
            }

            if (!appUserSession.isAccessTokenValid()) {
                return sendError(response, "accessTokenExpired",
                        "The access has expired. Please use the refresh token endpoint to generate a new one.");
            }

            User foundUser = appUserSession.getAppUserAuthorization().getUser();

            authenticateUser(request, foundUser);

            return true;

        } catch (FenixOAuthTokenException foate) {
            return sendError(response, "accessTokenInvalidFormat", "Access Token not recognized.");
        }
    }

    private void authenticateUser(final HttpServletRequest request, User foundUser) {
        Authenticate.mock(foundUser);
    }

    private boolean validScope(AppUserSession appUserSession, String uri) throws IOException, ServletException {
        uri = StringUtils.removeStart(StringUtils.removeEnd(uri, "/"), "/");
        AuthScope scope = FenixJerseyAPIConfig.getScope(uri);
        if (scope != null) {
            if (appUserSession.getAppUserAuthorization().getApplication().getScopesSet().contains(scope)) {
                return true;
            }
        }
        return false;
    }

    private static boolean sendError(final HttpServletResponse response, String error, String errorDescription)
            throws IOException, ServletException {
        OAuthResponse errorResponse;
        try {
            errorResponse =
                    new OAuthErrorResponseBuilder(401).setError(error).setErrorDescription(errorDescription).buildJSONMessage();
            response.setContentType("application/json; charset=UTF-8");
            response.setStatus(errorResponse.getResponseStatus());
            PrintWriter pw = response.getWriter();
            pw.print(errorResponse.getBody());
            return false;
        } catch (OAuthSystemException e) {
            throw new ServletException(e);
        }
    }

}
