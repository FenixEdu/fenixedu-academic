package net.sourceforge.fenixedu.presentationTier.servlets.filters;

import pt.ist.fenixWebFramework.security.UserView;

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
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.Authenticate;
import net.sourceforge.fenixedu.domain.AppUserSession;
import net.sourceforge.fenixedu.domain.AuthScope;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.presentationTier.Action.utils.RequestUtils;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.FenixOAuthToken.FenixOAuthTokenException;
import net.sourceforge.fenixedu.webServices.jersey.FenixJerseyPackageResourceConfig;

import org.apache.amber.oauth2.common.exception.OAuthSystemException;
import org.apache.amber.oauth2.common.message.OAuthResponse;
import org.apache.amber.oauth2.common.message.OAuthResponse.OAuthErrorResponseBuilder;
import org.apache.commons.lang.StringUtils;

@WebFilter(urlPatterns = "/jersey/private/*")
public class JerseyOAuth2Filter implements Filter {

    final static String ACCESS_TOKEN = "access_token";

    private static boolean allowIstIds = OAuthProperties.getFenixApiAllowIstIds();

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
                UserView.setUser(null);
            }
        }
    }

    private boolean checkAccessControl(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        if (allowIstIds() && currentUserIsManager()) {
            String istId = request.getParameter("_istid_");
            if (!StringUtils.isBlank(istId)) {
                User user = User.readUserByUserUId(istId);
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
        IUserView userview = UserView.getUser();
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

        String accessToken = request.getHeader(ACCESS_TOKEN);

        if (StringUtils.isBlank(accessToken)) {
            accessToken = request.getParameter(ACCESS_TOKEN);
        }

        try {
            FenixOAuthToken fenixAccessToken = FenixOAuthToken.parse(accessToken);
            AppUserSession appUserSession = fenixAccessToken.getAppUserSession();
            final String uri = request.getRequestURI().substring(RequestUtils.APP_CONTEXT_LENGTH).replace("/jersey/", "");

            if (!validScope(response, appUserSession, uri)) {
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
            sendError(response, "accessTokenInvalidFormat", "Access Token not recognized.");
        }

        return false;
    }

    private void authenticateUser(final HttpServletRequest request, User foundUser) {
        UserView.setUser(Authenticate.mockUser(foundUser.getPerson(), request.getRequestURL().toString()));
    }

    private boolean validScope(final HttpServletResponse response, AppUserSession appUserSession, final String uri)
            throws IOException, ServletException {
        AuthScope scope = FenixJerseyPackageResourceConfig.getScope(uri);
        if (scope != null) {
            if (!appUserSession.getAppUserAuthorization().getApplication().getScopesSet().contains(scope)) {
                return false;
            }
        }
        return true;
    }

    @SuppressWarnings("finally")
	private static boolean sendError(final HttpServletResponse response, String error, String errorDescription) throws IOException,
            ServletException {
        OAuthResponse errorResponse;
        try {
            errorResponse =
                    new OAuthErrorResponseBuilder(401).setError(error).setErrorDescription(errorDescription).buildJSONMessage();
            response.setStatus(errorResponse.getResponseStatus());
            PrintWriter pw = response.getWriter();
            pw.print(errorResponse.getBody());
        } catch (OAuthSystemException e) {
            throw new ServletException(e);
        } finally {
        	return false;
        }
    }

}
