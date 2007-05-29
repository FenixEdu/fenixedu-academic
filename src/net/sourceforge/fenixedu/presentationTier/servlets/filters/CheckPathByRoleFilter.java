package net.sourceforge.fenixedu.presentationTier.servlets.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.Action.utils.RequestUtils;

public class CheckPathByRoleFilter implements Filter {

    private class NotAuthorizedAccessException extends RuntimeException {
	public NotAuthorizedAccessException(String string) {
	    super(string);
	}
    }

    public void init(FilterConfig config) {
    }

    public void destroy() {
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
	    FilterChain filterChain) throws IOException, ServletException {

	final HttpServletRequest request = (HttpServletRequest) servletRequest;
	final HttpServletResponse response = (HttpServletResponse) servletResponse;

	final IUserView userView = getUserView(request);
	final String uri = request.getRequestURI().substring(RequestUtils.APP_CONTEXT_LENGTH);
	if (RequestUtils.isPrivateURI(request) && !isJavascript(uri) && userView != null) {
	    if (!validUserView(userView) || !hasRoleForSubApplication(uri, userView)) {
		System.out.println("error.not.authorized.access.attempt: " + userView.getUtilizador()
			+ " to uri: " + uri);
		throw new NotAuthorizedAccessException(
			"error.not.authorized.access.attempt.has.been.logged");
	    }
	}
	filterChain.doFilter(request, response);
    }

    private boolean isJavascript(final String uri) {
	return uri.startsWith("/javaScript/") || uri.startsWith("javaScript/")
		|| uri.startsWith("/ajax/") || uri.startsWith("ajax/");
    }

    private boolean hasRoleForSubApplication(final String uri, final IUserView userView) {
	if (uri.charAt(0) == '/') {
	    final int nextFSlash = uri.indexOf('/', 1);
	    if (nextFSlash > 1) {
		final String subApp = uri.substring(0, nextFSlash);
		for (final RoleType roleType : userView.getRoleTypes()) {
		    final Role role = Role.getRoleByRoleType(roleType);
		    if (role.getPortalSubApplication().equalsIgnoreCase(subApp)) {
			return true;
		    }
		}
	    } else {
		return true;
	    }
	} else {
	    final int nextFSlash = uri.indexOf('/', 1);
	    if (nextFSlash > 1) {
		final String subApp = uri.substring(0, nextFSlash);
		for (final RoleType roleType : userView.getRoleTypes()) {
		    final Role role = Role.getRoleByRoleType(roleType);
		    if (role.getPortalSubApplication().substring(1).equalsIgnoreCase(subApp)) {
			return true;
		    }
		}
	    } else {
		return true;
	    }
	}
	return false;
    }

    private IUserView getUserView(final HttpServletRequest request) {
	final HttpSession session = request.getSession(false);
	return (IUserView) ((session != null) ? session.getAttribute(SessionConstants.U_VIEW) : null);
    }

    private boolean validUserView(final IUserView userView) {
	return userView != null && !userView.isPublicRequester();
    }

}