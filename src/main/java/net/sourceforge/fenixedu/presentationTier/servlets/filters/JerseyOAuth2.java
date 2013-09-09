package net.sourceforge.fenixedu.presentationTier.servlets.filters;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.Authenticate;
import net.sourceforge.fenixedu.domain.AppUserSession;
import net.sourceforge.fenixedu.domain.AuthScope;
import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.presentationTier.Action.utils.RequestUtils;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.FenixOAuthToken.FenixOAuthTokenException;
import net.sourceforge.fenixedu.webServices.jersey.FenixJerseyPackageResourceConfig;

import org.apache.amber.oauth2.common.exception.OAuthSystemException;
import org.apache.amber.oauth2.common.message.OAuthResponse;
import org.apache.amber.oauth2.common.message.OAuthResponse.OAuthErrorResponseBuilder;
import org.apache.commons.lang.StringUtils;

import pt.ist.fenixWebFramework.security.UserView;

public class JerseyOAuth2 implements Filter {

    final static String ACCESS_TOKEN = "access_token";
    private static OAuthErrorResponseBuilder builder = new OAuthErrorResponseBuilder(401);

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
        if (checkAccessControl(request, response)) {
            try {
                filterChain.doFilter(request, response);
            } finally {

            }
        }

    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
    }

    private Boolean checkAccessControl(final HttpServletRequest request, final HttpServletResponse response) throws IOException,
            ServletException {

        String accessToken = request.getHeader(ACCESS_TOKEN);

        if (StringUtils.isBlank(accessToken)) {
            accessToken = request.getParameter(ACCESS_TOKEN);
        }

        try {
            FenixOAuthToken fenixAccessToken = FenixOAuthToken.parse(accessToken);
            AppUserSession appUserSession = fenixAccessToken.getAppUserSession();
            final String uri = request.getRequestURI().substring(RequestUtils.APP_CONTEXT_LENGTH).replace("/jersey/", "");

            validateScope(response, appUserSession, uri);

            if (!appUserSession.matchesAccessToken(accessToken)) {
                sendError(response, "accessTokenInvalid", "Access Token doesn't match.");
                return false;
            }

            if (!appUserSession.isAccessTokenValid()) {
                sendError(response, "accessTokenExpired",
                        "The access has expired. Please use the refresh token endpoint to generate a new one.");
                return false;
            }

            User foundUser = appUserSession.getUser();
            Authenticate as = new Authenticate();
            UserView.setUser(as.mock(foundUser.getPerson(), request.toString()));

            return true;

        } catch (FenixOAuthTokenException foate) {
            sendError(response, "accessTokenInvalidFormat", "Access Token not recognized.");
        }

        return false;
    }

    private void validateScope(final HttpServletResponse response, AppUserSession appUserSession, final String uri)
            throws IOException, ServletException {
        List<AuthScope> appScopes = appUserSession.getApplication().getScopes();
        AuthScope scope = FenixJerseyPackageResourceConfig.getScope(uri);

        if (scope != null) {
            if (!appScopes.contains(scope)) {
                sendError(response, "invalidScope", "Application doesn't have permissions to this endpoint's scope");
            }
        }
    }

    private static void sendError(final HttpServletResponse response, String error, String errorDescription) throws IOException,
            ServletException {
        OAuthResponse errorResponse;
        try {
            errorResponse = builder.setError(error).setErrorDescription(errorDescription).buildJSONMessage();
            response.setStatus(errorResponse.getResponseStatus());
            PrintWriter pw = response.getWriter();
            pw.print(errorResponse.getBody());
            pw.flush();
            pw.close();
        } catch (OAuthSystemException e) {
            throw new ServletException(e);
        }
    }

}
