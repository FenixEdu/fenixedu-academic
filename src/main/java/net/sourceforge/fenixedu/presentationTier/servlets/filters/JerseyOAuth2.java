package net.sourceforge.fenixedu.presentationTier.servlets.filters;

import java.io.IOException;
import java.io.PrintWriter;

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
import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.FenixOAuthToken.FenixOAuthTokenException;

import org.apache.amber.oauth2.common.exception.OAuthSystemException;
import org.apache.amber.oauth2.common.message.OAuthResponse;
import org.apache.amber.oauth2.common.message.OAuthResponse.OAuthErrorResponseBuilder;

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
            filterChain.doFilter(request, response);
        }
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
    }

    private Boolean checkAccessControl(final HttpServletRequest request, final HttpServletResponse response) throws IOException,
            ServletException {

        final String accessToken = request.getHeader(ACCESS_TOKEN);

        try {
            FenixOAuthToken fenixAccessToken = FenixOAuthToken.parse(accessToken);
            AppUserSession appUserSession = fenixAccessToken.getAppUserSession();

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
