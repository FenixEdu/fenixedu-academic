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
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionUtils;

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

    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
            throws IOException, ServletException {
	final HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        chain.doFilter(new FenixHttpServletRequestWrapper(httpServletRequest), response);
        setSessionTimeout(httpServletRequest);
    }

    private void setSessionTimeout(final HttpServletRequest request) {
	final HttpSession session = request.getSession(false);
	if (session != null) {
	    final IUserView userView = SessionUtils.getUserView(request);
	    final int m = userView == null ? 600 : 7200;
	    session.setMaxInactiveInterval(m);
        }
    }

    public static class FenixHttpServletRequestWrapper extends HttpServletRequestWrapper {

	private static final String PAGE_DEFAULT = "0";

	private static final String[] PAGE_DEFAULT_ARRAY = { PAGE_DEFAULT };
	
	public FenixHttpServletRequestWrapper(HttpServletRequest request) {
            super(request);
        }

	@Override
        public String getRemoteUser() {
            final IUserView userView = SessionUtils.getUserView(this);
            return userView != null ? userView.getUtilizador() : super.getRemoteUser();
        }

        @Override
        public boolean isUserInRole(final String role) {
            final IUserView userView = SessionUtils.getUserView(this);
            final RoleType roleType = RoleType.valueOf(role);
            return userView != null && userView.hasRoleType(roleType);
        }

        @Override
        public Enumeration getParameterNames() {
            final Vector params = new Vector();

            final Enumeration paramEnum = super.getParameterNames();
            boolean gotPageParameter = false;
            while (paramEnum.hasMoreElements()) {
                final String parameterName = (String) paramEnum.nextElement();
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

        @Override
        public String[] getParameterValues(final String parameter) {
            final String[] parameterValues = super.getParameterValues(parameter);
            return parameterValues == null && parameter.equals("page") ? PAGE_DEFAULT_ARRAY : parameterValues;
        }

        @Override
        public String getParameter(final String parameter) {
            final String parameterValue = super.getParameter(parameter);
            return parameterValue == null && parameter.equals("page") ? PAGE_DEFAULT : parameterValue;
        }

//        @Override
//        public Map getParameterMap() {
//            final Map map = super.getParameterMap();
//            if (map.containsKey("page")) {
//        	System.out.println(" returning map: " + map.getClass().getName());
//        	System.out.println(" iterator     : " + map.entrySet().iterator().getClass().getName());
//        	return map;
//            } else {
//        	final Map newMap = new HashMap(map);
//        	newMap.put("page", PAGE_DEFAULT);
//        	System.out.println(" returning map: " + map.getClass().getName());
//        	System.out.println(" iterator     : " + map.entrySet().iterator().getClass().getName());
//        	return newMap;
//            }
//        }

    }
}