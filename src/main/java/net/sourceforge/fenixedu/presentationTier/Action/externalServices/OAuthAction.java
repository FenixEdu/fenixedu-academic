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
package net.sourceforge.fenixedu.presentationTier.Action.externalServices;

import static net.sourceforge.fenixedu.presentationTier.Action.externalServices.OAuthUtils.getOAuthProblemResponse;
import static net.sourceforge.fenixedu.presentationTier.Action.externalServices.OAuthUtils.getOAuthResponse;
import static net.sourceforge.fenixedu.presentationTier.Action.externalServices.OAuthUtils.sendOAuthResponse;
import static org.apache.amber.oauth2.common.error.OAuthError.TokenResponse.INVALID_GRANT;
import static org.apache.commons.httpclient.HttpStatus.SC_BAD_REQUEST;
import static org.apache.commons.httpclient.HttpStatus.SC_UNAUTHORIZED;

import java.io.IOException;
import java.util.Base64;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu._development.OAuthProperties;
import net.sourceforge.fenixedu.domain.AppUserAuthorization;
import net.sourceforge.fenixedu.domain.AppUserSession;
import net.sourceforge.fenixedu.domain.ExternalApplication;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.FenixOAuthToken;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.FenixOAuthToken.FenixOAuthTokenException;
import net.sourceforge.fenixedu.util.Bundle;
import nl.bitwalker.useragentutils.UserAgent;

import org.apache.amber.oauth2.as.issuer.MD5Generator;
import org.apache.amber.oauth2.as.issuer.OAuthIssuer;
import org.apache.amber.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.amber.oauth2.as.request.OAuthTokenRequest;
import org.apache.amber.oauth2.as.response.OAuthASResponse;
import org.apache.amber.oauth2.common.error.OAuthError;
import org.apache.amber.oauth2.common.exception.OAuthProblemException;
import org.apache.amber.oauth2.common.exception.OAuthSystemException;
import org.apache.amber.oauth2.common.message.OAuthResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.core.util.CookieReaderUtils;
import org.fenixedu.bennu.core.util.CoreConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

@Mapping(module = "external", path = "/oauth", scope = "request", parameter = "method")
@Forwards({ @Forward(name = "showAuthorizationPage", path = "/auth/showAuthorizationPage.jsp"),
        @Forward(name = "oauthErrorPage", path = "/auth/oauthErrorPage.jsp") })
public class OAuthAction extends FenixDispatchAction {

    private static final Logger logger = LoggerFactory.getLogger(OAuthAction.class);

    private final static OAuthIssuer OAUTH_ISSUER = new OAuthIssuerImpl(new MD5Generator());

    public final static String OAUTH_SESSION_KEY = "OAUTH_CLIENT_ID";

    private static String getDeviceId(HttpServletRequest request) {
        String deviceId = request.getParameter("device_id");
        if (StringUtils.isBlank(deviceId)) {
            UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader(HttpHeaders.USER_AGENT));
            String browserName = userAgent.getBrowser().getName();
            String osName = userAgent.getOperatingSystem().getName();
            deviceId =
                    BundleUtil.getString(Bundle.APPLICATION, "oauthapps.label.device.type",
                            browserName, osName);
        }
        return deviceId;
    }

    private static ExternalApplication getExternalApplication(String clientId) {
        return OAuthUtils.getDomainObject(clientId, ExternalApplication.class);
    }

    /** ACTIONS **/
    // http://localhost:8080/ciapl/external/oauth/userdialog&client_id=123123&redirect_uri=http://www.google.com

    public ActionForward getUserPermission(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String clientId = request.getParameter("client_id");
        String redirectUrl = request.getParameter("redirect_uri");
        Person person = getLoggedPerson(request);

        if (!StringUtils.isEmpty(clientId) && !StringUtils.isEmpty(redirectUrl)) {
            if (person == null) {
                //redirect person to this action with client id in session

                final String cookieValue = clientId + "|" + redirectUrl;
                response.addCookie(new Cookie(OAUTH_SESSION_KEY, Base64.getEncoder().encodeToString(cookieValue.getBytes())));
                if (CoreConfiguration.casConfig().isCasEnabled()) {
                    response.sendRedirect(CoreConfiguration.casConfig().getCasLoginUrl(
                            CoreConfiguration.getConfiguration().applicationUrl() + "/oauth/userdialog"));
                } else {
                    response.sendRedirect(request.getContextPath() + "/oauth/userdialog");
                }
                return null;
            } else {
                return redirectToRedirectUrl(mapping, request, response, person, clientId, redirectUrl);
            }
        } else {
            if (person != null) {
                // this is the request that will recover client id from session
                final Cookie cookie = CookieReaderUtils.getCookieForName(OAUTH_SESSION_KEY, request);
                if (cookie == null) {
                    logger.debug("Cookie can't be null because this a direct request from this action with cookie");
                    return mapping.findForward("oauthErrorPage");
                }
                final String sessionClientId = cookie.getValue();
                if (!StringUtils.isEmpty(sessionClientId)) {
                    return redirectToRedirectUrl(mapping, request, response, person, cookie);
                }
            } else {
                logger.debug("Person should not be null since this a redirect from this action with cookie");
            }
        }

        return mapping.findForward("oauthErrorPage");

    }

    public ActionForward redirectToRedirectUrl(ActionMapping mapping, HttpServletRequest request, HttpServletResponse response,
            Person person, final Cookie cookie) {
        String cookieValue = new String(Base64.getDecoder().decode(cookie.getValue()));
        final int indexOf = cookieValue.indexOf("|");
        String clientApplicationId = cookieValue.substring(0, indexOf);
        String redirectUrl = cookieValue.substring(indexOf + 1, cookieValue.length());

        return redirectToRedirectUrl(mapping, request, response, person, clientApplicationId, redirectUrl);
    }

    public ActionForward redirectToRedirectUrl(ActionMapping mapping, HttpServletRequest request, HttpServletResponse response,
            Person person, String clientApplicationId, String redirectUrl) {

        final ExternalApplication clientApplication = FenixFramework.getDomainObject(clientApplicationId);

        if (!FenixFramework.isDomainObjectValid(clientApplication) || !clientApplication.matchesUrl(redirectUrl)) {
            return mapping.findForward("oauthErrorPage");
        }

        if (clientApplication.isBanned() || clientApplication.isDeleted()) {
            return mapping.findForward("oauthErrorPage");
        }

        if (!clientApplication.hasAppUserAuthorization(person.getUser())) {
            request.setAttribute("application", clientApplication);
            return mapping.findForward("showAuthorizationPage");
        } else {
            return redirectWithCode(request, response, clientApplication);
        }
    }

    // http://localhost:8080/ciapl/external/oauth.do?method=userConfirmation&...
    public ActionForward userConfirmation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        Person person = getLoggedPerson(request);
        if (person == null) {
            return null;
        }

        String clientId = request.getParameter("client_id");
        String redirectUrl = request.getParameter("redirect_uri");

        ExternalApplication externalApplication = getExternalApplication(clientId);

        if (!isValidApplication(response, externalApplication)) {
            return null;
        }

        if (externalApplication.matchesUrl(redirectUrl)) {
            return redirectWithCode(request, response, externalApplication);
        }

        return null;
    }

    private boolean isValidApplication(HttpServletResponse response, ExternalApplication clientApplication) {
        if (clientApplication.isDeleted()) {
            sendOAuthResponse(response,
                    getOAuthProblemResponse(SC_UNAUTHORIZED, INVALID_GRANT, "The application has been deleted."));
            return false;
        }

        if (clientApplication.isBanned()) {
            sendOAuthResponse(response,
                    getOAuthProblemResponse(SC_UNAUTHORIZED, INVALID_GRANT, "The application has been banned."));
            return false;
        }
        return true;
    }

    private ActionForward redirectWithCode(HttpServletRequest request, HttpServletResponse response,
            ExternalApplication clientApplication) {
        try {
            final String code = createAppUserSession(clientApplication, getLoggedPerson(request).getUser(), request, response);
            OAuthResponse resp =
                    OAuthASResponse.authorizationResponse(request, HttpServletResponse.SC_FOUND)
                            .location(clientApplication.getRedirectUrl()).setCode(code).buildQueryMessage();
            response.sendRedirect(resp.getLocationUri());
        } catch (OAuthSystemException e) {
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    @Atomic
    private static String createAppUserSession(ExternalApplication application, User user, HttpServletRequest request,
            HttpServletResponse response) {
        String code = generateCode(response);
        AppUserAuthorization appUserAuthorization = application.getAppUserAuthorization(user);
        if (appUserAuthorization == null) {
            appUserAuthorization = new AppUserAuthorization(user, application);
        }
        AppUserSession appUserSession = new AppUserSession();
        appUserSession.setCode(code);
        appUserSession.setDeviceId(getDeviceId(request));
        appUserSession.setAppUserAuthorization(appUserAuthorization);
        return code;
    }

    private static String generateCode(HttpServletResponse response) {
        try {
            return OAUTH_ISSUER.authorizationCode();
        } catch (OAuthSystemException e) {
            serverError(response, e);
            return null;
        }
    }

    public ActionForward userCancelation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        /*
         * "In the case that the user denies the access request, an error
         * response will be generated, and the user will be redirected to the
         * redirect_uri with a query parameter called error indicating the type
         * of error as access_denied. Additionally, the server can include an
         * error_description message and/or an error_uri indicating the URL of a
         * web page containing more information about the error."
         */

        String redirectUrl = request.getParameter("redirect_uri");
        return redirectToError(response, SC_UNAUTHORIZED, redirectUrl, "access_denied", "User didn't allow the application");
    }

    private ActionForward redirectToError(HttpServletResponse response, int status, String redirectUrl, String errorMessage,
            String errorDescription) {
        try {
            OAuthResponse r =
                    OAuthResponse.errorResponse(status).location(redirectUrl).setError(errorMessage)
                            .setErrorDescription(errorDescription).buildQueryMessage();
            response.sendRedirect(r.getLocationUri());
        } catch (Exception e) {
            return serverError(response, e);
        }
        return null;
    }

    private static ActionForward serverError(HttpServletResponse response, Exception e) {
        try {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Something went wrong. Please contact support team.");
        } catch (IOException e1) {
            logger.error(e1.getMessage(), e1);
        }
        logger.error(e.getMessage(), e);
        return null;
    }

    private String generateToken(AppUserSession appUserSession, String random) {
        return new FenixOAuthToken(appUserSession, random).format();
    }

    public ActionForward refreshAccessToken(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        OAuthTokenRequest oauthRequest = null;
        try {
            oauthRequest = new OAuthTokenRequest(request);
        } catch (OAuthProblemException e) {
            return sendOAuthResponse(response, getOAuthResponse(SC_BAD_REQUEST, e));
        }

        String clientId = oauthRequest.getClientId();
        String clientSecret = oauthRequest.getClientSecret();
        String refreshToken = oauthRequest.getRefreshToken();

        try {
            ExternalApplication externalApplication = getExternalApplication(clientId);

            if (!isValidApplication(response, externalApplication)) {
                return null;
            }

            FenixOAuthToken fenixRefreshToken = FenixOAuthToken.parse(refreshToken);
            AppUserSession appUserSession = fenixRefreshToken.getAppUserSession();

            if (!externalApplication.matchesSecret(clientSecret)) {
                return sendOAuthResponse(response,
                        getOAuthProblemResponse(SC_UNAUTHORIZED, INVALID_GRANT, "Credentials or redirect_uri don't match"));
            }

            if (!appUserSession.matchesRefreshToken(refreshToken)) {
                return sendOAuthResponse(response,
                        getOAuthProblemResponse(SC_UNAUTHORIZED, "refreshTokenInvalid", "Refresh token doesn't match"));
            }

            String accessToken = generateToken(appUserSession, OAUTH_ISSUER.accessToken());

            appUserSession.setNewAccessToken(accessToken);

            OAuthResponse r =
                    OAuthASResponse.tokenResponse(HttpServletResponse.SC_OK).location(externalApplication.getRedirectUrl())
                            .setAccessToken(accessToken)
                            .setExpiresIn(OAuthProperties.getConfiguration().getAccessTokenExpirationSeconds().toString())
                            .buildJSONMessage();

            return sendOAuthResponse(response, r);
        } catch (FenixOAuthTokenException fote) {
            return sendOAuthResponse(response,
                    getOAuthProblemResponse(SC_UNAUTHORIZED, "refreshTokenInvalidFormat", "Refresh Token not recognized."));
        }
    }

    // http://localhost:8080/ciapl/external/oauth.do?method=getTokens&...
    public ActionForward getTokens(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        OAuthTokenRequest oauthRequest = null;

        try {
            oauthRequest = new OAuthTokenRequest(request);
        } catch (OAuthProblemException e) {
            return sendOAuthResponse(response, getOAuthResponse(SC_BAD_REQUEST, e));
        }

        String clientId = oauthRequest.getClientId();
        String clientSecret = oauthRequest.getClientSecret();
        String redirectUrl = oauthRequest.getRedirectURI();
        String authzCode = oauthRequest.getCode();

        ExternalApplication externalApplication = getExternalApplication(clientId);

        if (externalApplication == null) {
            return sendOAuthResponse(response, getOAuthProblemResponse(SC_BAD_REQUEST, INVALID_GRANT, "Client ID not recognized"));
        }

        if (!isValidApplication(response, externalApplication)) {
            return null;
        }

        if (!externalApplication.matches(redirectUrl, clientSecret)) {
            return sendOAuthResponse(response,
                    getOAuthProblemResponse(SC_BAD_REQUEST, INVALID_GRANT, "Credentials or redirect_uri don't match"));
        }

        AppUserSession appUserSession = externalApplication.getAppUserSession(authzCode);

        if (appUserSession == null) {
            return sendOAuthResponse(response, getOAuthProblemResponse(SC_BAD_REQUEST, INVALID_GRANT, "Code Invalid"));

        }

        OAuthResponse r;

        if (appUserSession.isCodeValid()) {
            String accessToken = generateToken(appUserSession, OAUTH_ISSUER.accessToken());
            String refreshToken = generateToken(appUserSession, OAUTH_ISSUER.refreshToken());
            appUserSession.setTokens(accessToken, refreshToken);

            r =
                    OAuthASResponse.tokenResponse(HttpServletResponse.SC_OK).location(redirectUrl).setAccessToken(accessToken)
                            .setExpiresIn(OAuthProperties.getConfiguration().getAccessTokenExpirationSeconds().toString())
                            .setRefreshToken(refreshToken).buildJSONMessage();
        } else {
            r = getOAuthProblemResponse(SC_BAD_REQUEST, OAuthError.TokenResponse.INVALID_GRANT, "Code expired");
        }

        return sendOAuthResponse(response, r);
    }
}
