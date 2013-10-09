package net.sourceforge.fenixedu.presentationTier.Action.externalServices;

import static net.sourceforge.fenixedu.presentationTier.Action.externalServices.OAuthUtils.getOAuthProblemResponse;
import static net.sourceforge.fenixedu.presentationTier.Action.externalServices.OAuthUtils.getOAuthResponse;
import static net.sourceforge.fenixedu.presentationTier.Action.externalServices.OAuthUtils.sendOAuthResponse;
import static org.apache.amber.oauth2.common.error.OAuthError.TokenResponse.INVALID_GRANT;
import static org.apache.commons.httpclient.HttpStatus.SC_BAD_REQUEST;
import static org.apache.commons.httpclient.HttpStatus.SC_UNAUTHORIZED;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

import pt.ist.fenixframework.Atomic;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu._development.OAuthProperties;
import net.sourceforge.fenixedu.domain.AppUserAuthorization;
import net.sourceforge.fenixedu.domain.AppUserSession;
import net.sourceforge.fenixedu.domain.ExternalApplication;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.utils.RequestUtils;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.FenixOAuthToken;
import net.sourceforge.fenixedu.util.BundleUtil;
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
import org.apache.http.HttpHeaders;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.codehaus.plexus.util.StringUtils;

@Mapping(module = "external", path = "/oauth", scope = "request", parameter = "method")
@Forwards({ @Forward(name = "showAuthorizationPage", path = "showAuthorizationPage"),
        @Forward(name = "oauthErrorPage", path = "oauthErrorPage") })
public class OAuthAction extends FenixDispatchAction {

    private final static OAuthIssuer OAUTH_ISSUER = new OAuthIssuerImpl(new MD5Generator());

    private static String getDeviceId(HttpServletRequest request) {
        String deviceId = request.getParameter("device_id");
        if (StringUtils.isBlank(deviceId)) {
            UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader(HttpHeaders.USER_AGENT));
            String browserName = userAgent.getBrowser().getName();
            String osName = userAgent.getOperatingSystem().getName();
            deviceId =
                    BundleUtil.getStringFromResourceBundle("resources.ApplicationResources", "oauthapps.label.device.type",
                            browserName, osName);
        }
        return deviceId;
    }

    private static ExternalApplication getExternalApplication(String clientId) {
        return OAuthUtils.getDomainObject(clientId, ExternalApplication.class);
    }

    /** ACTIONS **/
    // http://localhost:8080/ciapl/external/oauth.do?method=getUserPermission&client_id=123123&redirect_uri=http://www.google.com

    public ActionForward getUserPermission(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        Person person = getLoggedPerson(request);
        if (person == null) {
            RequestUtils.sendLoginRedirect(request, response);
            return null;
        } else {
            String clientId = request.getParameter("client_id");
            String redirectUrl = request.getParameter("redirect_uri");

            ExternalApplication clientApplication = getExternalApplication(clientId);
            if (clientApplication == null) {
                return mapping.findForward("oauthErrorPage");
            }

            if (clientApplication.matchesUrl(redirectUrl)) {

                if (!clientApplication.hasAppUserAuthorization(person.getUser())) {
                    request.setAttribute("application", clientApplication);
                    return mapping.findForward("showAuthorizationPage");
                } else {
                    return redirectWithCode(request, response, clientApplication);
                }
            }

            return mapping.findForward("oauthErrorPage");
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

        ExternalApplication clientApplication = getExternalApplication(clientId);

        if (clientApplication.matchesUrl(redirectUrl)) {
            redirectWithCode(request, response, clientApplication);
        }

        return null;
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
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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
            e1.printStackTrace();
        }
        e.printStackTrace();
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
        String redirectUrl = oauthRequest.getRedirectURI();

        FenixOAuthToken fenixRefreshToken = FenixOAuthToken.parse(refreshToken);
        AppUserSession appUserSession = fenixRefreshToken.getAppUserSession();

        ExternalApplication externalApplication = getExternalApplication(clientId);
        if (!externalApplication.matches(redirectUrl, clientSecret)) {
            return null;
        }

        String accessToken = generateToken(appUserSession, OAUTH_ISSUER.accessToken());

        appUserSession.setNewAccessToken(accessToken);

        OAuthResponse r =
                OAuthASResponse.tokenResponse(HttpServletResponse.SC_OK).location(redirectUrl).setAccessToken(accessToken)
                        .setExpiresIn(OAuthProperties.getAccessTokenExpirationSeconds().toString()).buildJSONMessage();

        return sendOAuthResponse(response, r);
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
                            .setExpiresIn(OAuthProperties.getAccessTokenExpirationSeconds().toString())
                            .setRefreshToken(refreshToken).buildJSONMessage();
        } else {

            r = getOAuthProblemResponse(SC_BAD_REQUEST, OAuthError.TokenResponse.INVALID_GRANT, "Code expired");
        }

        return sendOAuthResponse(response, r);
    }
}
