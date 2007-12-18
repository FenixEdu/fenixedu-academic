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
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.Action.utils.RequestUtils;

public class CheckUserViewFilter implements Filter {

    public void init(FilterConfig config) {
    }

    public void destroy() {
    }

    public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain)
            throws IOException, ServletException {

        final HttpServletRequest request = (HttpServletRequest) servletRequest;
        final HttpServletResponse response = (HttpServletResponse) servletResponse;

        final IUserView userView = getUserView(request);
        if (!validUserView(userView) && RequestUtils.isPrivateURI(request)) {
            RequestUtils.sendLoginRedirect(request, response);
        } else {
            filterChain.doFilter(request, response);
        }
    }

    private IUserView getUserView(final HttpServletRequest request) {
        final HttpSession session = request.getSession(false);
        return (IUserView) ((session != null) ? session.getAttribute(SessionConstants.U_VIEW) : null);
    }

    private boolean validUserView(final IUserView userView) {
        return userView != null;
    }

}