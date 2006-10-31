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
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.Action.utils.RequestUtils;

public class CheckUserViewFilter implements Filter {

    public void init(FilterConfig config) {
    }

    public void destroy() {
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        final HttpServletRequest request = (HttpServletRequest) servletRequest;
        final HttpServletResponse response = (HttpServletResponse) servletResponse;

        final IUserView userView = getUserView(request);
        if (RequestUtils.isPrivateURI(request) && !validUserView(userView)) {
            RequestUtils.sendLoginRedirect(request, response);
        } else {
            filterChain.doFilter(request, response);
        }
    }

    private IUserView getUserView(final HttpServletRequest request) {
        final HttpSession session = request.getSession(false);
        return (IUserView) ((session != null) ? session.getAttribute(SessionConstants.U_VIEW) : null);
    }

    private boolean isPrivateURI(final String uri) {
        return (uri.length() > 1
        		&& (uri.indexOf("CSS/") == -1)
        		&& (uri.indexOf("images/") == -1)
                && (uri.indexOf("download/") == -1)
                && (uri.indexOf("external/") == -1)
                && (uri.indexOf("index.jsp") == -1)
                && (uri.indexOf("index.html") == -1)
                && (uri.indexOf("login.do") == -1)
                && (uri.indexOf("loginCAS.do") == -1)
                && (uri.indexOf("privado") == -1)
                && (uri.indexOf("loginPage.jsp") == -1)
                && (uri.indexOf("loginExpired.jsp") == -1)
                && (uri.indexOf("loginExpired.do") == -1)                
                && (uri.indexOf("logoff.do") == -1)
                && (uri.indexOf("publico/") == -1)
                && (uri.indexOf("showErrorPage.do") == -1)
                && (uri.indexOf("manager/manageCache.do") == -1)
                && (uri.indexOf("checkPasswordKerberos.do") == -1)
                && (uri.indexOf("siteMap.do") == -1)
                && (uri.indexOf("changeLocaleTo.do") == -1)
                && (uri.indexOf("cms/forwardEmailAction.do") == -1)
                && (uri.indexOf("isAlive.do") == -1));
    }

    private boolean validUserView(final IUserView userView) {
        return userView != null;
    }

}