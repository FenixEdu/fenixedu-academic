package net.sourceforge.fenixedu.presentationTier.Action.externalServices;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.domain.AppUserSession;
import net.sourceforge.fenixedu.domain.ExternalApplication;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.utils.RequestUtils;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.FenixOAuthToken;

import org.apache.amber.oauth2.as.issuer.MD5Generator;
import org.apache.amber.oauth2.as.issuer.OAuthIssuer;
import org.apache.amber.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.amber.oauth2.as.request.OAuthTokenRequest;
import org.apache.amber.oauth2.as.response.OAuthASResponse;
import org.apache.amber.oauth2.common.exception.OAuthSystemException;
import org.apache.amber.oauth2.common.message.OAuthResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

@Mapping(module = "external", path = "/oauth", scope = "request", parameter = "method")
@Forwards({ @Forward(name = "showAuthorizationPage", path = "showAuthorizationPage"),
        @Forward(name = "oauthErrorPage", path = "/auth/oauthErrorPage.jsp") })
public class OAuthAction extends FenixDispatchAction {

    private final static String ACCESS_TOKEN_EXPIRATION = PropertiesManager
            .getProperty("fenix.api.oauth.access.token.timeout.seconds");

    private static class OAuthNotFoundException extends Exception {

    }

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

            try {
                ExternalApplication clientApplication = getExternalApplication(clientId);
                if (clientApplication.matchesUrl(redirectUrl)) {
                    request.setAttribute("app", clientApplication);
                    return mapping.findForward("showAuthorizationPage");
                }
                throw new OAuthNotFoundException();
            } catch (OAuthNotFoundException onfe) {
                return mapping.findForward("oauthErrorPage");
            }
        }

    }

    private ExternalApplication getExternalApplication(String clientId) throws OAuthNotFoundException {
        //check if it is a number
        DomainObject domainObject = AbstractDomainObject.fromExternalId(clientId);
        if (domainObject == null || !(domainObject instanceof ExternalApplication)) {
            throw new OAuthNotFoundException();
        }
        return (ExternalApplication) domainObject;
    }

    // http://localhost:8080/ciapl/external/oauth.do?method=userConfirmation&...
    public ActionForward userConfirmation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());

        Person person = getLoggedPerson(request);
        if (person == null) {
            return null;
        }

        String clientId = request.getParameter("client_id");
        String redirectUrl = request.getParameter("redirect_uri");
        String deviceId = request.getParameter("device_id");

        ExternalApplication clientApplication = getExternalApplication(clientId);

        String applicationUrl = clientApplication.getUrl();
        if (!clientApplication.matchesUrl(redirectUrl)) {
            return null;
        }
        try {
            String code = oauthIssuerImpl.authorizationCode();

            User user = person.getUser();
            createAppUserSession(clientApplication, code, user, deviceId);

            OAuthResponse resp =
                    OAuthASResponse.authorizationResponse(request, HttpServletResponse.SC_FOUND).location(applicationUrl)
                            .setCode(code).buildQueryMessage();

            response.sendRedirect(resp.getLocationUri());
        } catch (OAuthSystemException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Service
    private void createAppUserSession(ExternalApplication clientApplication, String code, User user, String deviceId) {
        clientApplication.addUser(user);
        AppUserSession appUserSession = new AppUserSession();
        appUserSession.setCode(code);
        appUserSession.setUser(user);
        appUserSession.setDeviceId(deviceId);
        clientApplication.addAppUserSession(appUserSession);
    }

    public void userCancelation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        /*
         * "In the case that the user denies the access request, an error
         * response will be generated, and the user will be redirected to the
         * redirect_uri with a query parameter called error indicating the type
         * of error as access_denied. Additionally, the server can include an
         * error_description message and/or an error_uri indicating the URL of a
         * web page containing more information about the error."
         */

        try {
            String redirectUrl = request.getParameter("redirect_uri");

            OAuthResponse r =
                    OAuthResponse.errorResponse(401).setErrorUri(redirectUrl).setError("Permission denied")
                            .setErrorDescription("User didn't allow the application").buildJSONMessage();

            response.setStatus(r.getResponseStatus());

            response.sendError(401);
        } catch (OAuthSystemException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private String generateToken(AppUserSession appUserSession, String random) {
        return new FenixOAuthToken(appUserSession, random).format();
    }

    // http://localhost:8080/ciapl/external/oauth.do?method=getTokens&...
    public ActionForward getTokens(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        OAuthTokenRequest oauthRequest = new OAuthTokenRequest(request);
        OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());

        String clientId = oauthRequest.getClientId();
        String clientSecret = oauthRequest.getClientSecret();
        String redirectUrl = oauthRequest.getRedirectURI();
        String authzCode = oauthRequest.getCode();

        ExternalApplication externalApplication = getExternalApplication(clientId);

        if (!externalApplication.matches(redirectUrl, clientSecret)) {
            return null;
        }

        AppUserSession appUserSession = externalApplication.getAppUserSession(authzCode);

        if (appUserSession == null) {
            return null;
        }

        String accessToken = generateToken(appUserSession, oauthIssuerImpl.accessToken());
        String refreshToken = generateToken(appUserSession, oauthIssuerImpl.refreshToken());
        appUserSession.setTokens(accessToken, refreshToken);

        OAuthResponse r =
                OAuthASResponse.tokenResponse(HttpServletResponse.SC_OK).location(redirectUrl).setAccessToken(accessToken)
                        .setExpiresIn(ACCESS_TOKEN_EXPIRATION).setRefreshToken(refreshToken).buildJSONMessage();

        response.setStatus(r.getResponseStatus());
        PrintWriter pw = response.getWriter();
        pw.print(r.getBody());
        pw.flush();
        pw.close();
        return null;
    }

    public ActionForward refreshAccessToken(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        OAuthTokenRequest oauthRequest = new OAuthTokenRequest(request);
        OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());

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

        String accessToken = generateToken(appUserSession, oauthIssuerImpl.accessToken());

        appUserSession.setNewAccessToken(accessToken);

        OAuthResponse r =
                OAuthASResponse.tokenResponse(HttpServletResponse.SC_OK).location(redirectUrl).setAccessToken(accessToken)
                        .setExpiresIn(ACCESS_TOKEN_EXPIRATION).buildJSONMessage();

        response.setStatus(r.getResponseStatus());
        PrintWriter pw = response.getWriter();
        pw.print(r.getBody());
        pw.flush();
        pw.close();
        return null;

    }
}
