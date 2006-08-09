package net.sourceforge.fenixedu.presentationTier.servlets.filters;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

/**
 * 17/Fev/2003
 * 
 * @author jpvl
 */
public class RequestWrapperFilter implements Filter {

    public void init(FilterConfig config) {
    }

    public void destroy() {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        chain.doFilter(new FenixHttpServletRequestWrapper((HttpServletRequest) request), response);
        setSessionTimeout((HttpServletRequest) request);
    }

    /**
     * @param request
     */
    private void setSessionTimeout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            if (session.getAttribute(SessionConstants.U_VIEW) == null) {
                session.setMaxInactiveInterval(600);
            } else {
                session.setMaxInactiveInterval(7200);
            }
        }

    }

    public class FenixHttpServletRequestWrapper extends HttpServletRequestWrapper {

        /**
         * @param request
         */
        public FenixHttpServletRequestWrapper(HttpServletRequest request) {
            super(request);
        }

        /*
         * (non-Javadoc)
         * 
         * @see javax.servlet.http.HttpServletRequest#getRemoteUser()
         */
        public String getRemoteUser() {
            IUserView userView = SessionUtils.getUserView(this);
            return userView != null ? userView.getUtilizador() : super.getRemoteUser();
        }

        /*
         * (non-Javadoc)
         * 
         * @see javax.servlet.http.HttpServletRequest#isUserInRole(java.lang.String)
         */
        public boolean isUserInRole(String role) {
            IUserView userView = SessionUtils.getUserView(this);
            RoleType roleType = RoleType.valueOf(role);
            Role infoRole = Role.getRoleByRoleType(roleType);
            return userView.getRoles().contains(infoRole);
        }

        /*
         * (non-Javadoc)
         * 
         * @see javax.servlet.ServletRequest#getParameterNames()
         */
        public Enumeration getParameterNames() {
            Vector params = new Vector();

            Enumeration paramEnum = super.getParameterNames();
            boolean gotPageParameter = false;
            while (paramEnum.hasMoreElements()) {
                String parameterName = (String) paramEnum.nextElement();
                if (paramEnum.equals("page")) {
                    gotPageParameter = true;
                }
                params.add(parameterName);
            }
            if (!gotPageParameter) {
                params.add("page");
            }

            return params.elements();
        }

        /*
         * (non-Javadoc)
         * 
         * @see javax.servlet.ServletRequest#getParameterValues(java.lang.String)
         */
        public String[] getParameterValues(String parameter) {

            if (parameter != null && parameter.equals("page")) {

                String[] pageDefault = { "0" };

                String[] pageValues = super.getParameterValues("page");
                return pageValues == null ? pageDefault : pageValues;
            }
            return super.getParameterValues(parameter);
        }

    }
}