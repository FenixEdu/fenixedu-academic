/*
 * Created on Jun 25, 2004
 *  
 */
package ServidorApresentacao.servlets.filters;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import DataBeans.InfoRole;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import Tools.Profiler;
import Util.RoleType;

/**
 * @author Luis Cruz
 *  
 */
public class RequestWrapperCaptureFilter implements Filter {

    ServletContext servletContext;
    FilterConfig filterConfig;

    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
        this.servletContext = filterConfig.getServletContext();
    }

    public void destroy() {
        this.servletContext = null;
        this.filterConfig = null;
    }

    public void doFilter(ServletRequest req, ServletResponse res,
            FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        Profiler.getInstance();

        // check if was a resource that shouldn't be cached.
        String uri = request.getRequestURI();
        if (uri != null) {
        	// customize to match parameters
        	String queryString = constructQueryString(request);
        	StringBuffer id = new StringBuffer(request.getRequestURI());
        	if (queryString != null) {
        		id.append("?");
        		id.append(queryString);
        	}
        	System.out.println(id);

        	if (id.indexOf("cache") != -1) {
        		Profiler.report();
        	}
        }

        chain.doFilter(new FenixHttpServletRequestWrapper(request), response);
    }

	private String constructQueryString(HttpServletRequest request) {
		StringBuffer queryString = new StringBuffer();
		
		String requestQueryString = request.getQueryString();
		if (requestQueryString != null) {
			queryString.append(requestQueryString);
		}

		Enumeration parameterNames = request.getParameterNames();
		if (parameterNames != null) {
			while(parameterNames.hasMoreElements()) {
				String parameterName = (String) parameterNames.nextElement();
				String parameterValue = request.getParameter(parameterName);
				if (queryString.length() != 0) {
					queryString.append("&");
				}
				queryString.append(parameterName);
				queryString.append("=");
				queryString.append(parameterValue);
			}
		}

		if (queryString.length() != 0) {
			return queryString.toString();
		} 
			return null;
		
	}

	public class FenixHttpServletRequestWrapper extends HttpServletRequestWrapper
	{

		/**
		 * @param request
		 */
		public FenixHttpServletRequestWrapper(HttpServletRequest request)
		{
			super(request);
		}

		/* (non-Javadoc)
		 * @see javax.servlet.http.HttpServletRequest#getRemoteUser()
		 */
		public String getRemoteUser()
		{
			IUserView userView = SessionUtils.getUserView(this);
			return userView != null ? userView.getUtilizador() : super.getRemoteUser();
		}

		/* (non-Javadoc)
		 * @see javax.servlet.http.HttpServletRequest#isUserInRole(java.lang.String)
		 */
		public boolean isUserInRole(String role)
		{
			IUserView userView = SessionUtils.getUserView(this);
			RoleType roleType = RoleType.getEnum(role);
			InfoRole infoRole = new InfoRole();
			infoRole.setRoleType(roleType);
			return userView.getRoles().contains(infoRole);
		}
		/* (non-Javadoc)
		 * @see javax.servlet.ServletRequest#getParameterNames()
		 */
		public Enumeration getParameterNames()
		{
			Vector params = new Vector();

			Enumeration paramEnum = super.getParameterNames();
			boolean gotPageParameter = false;
			while (paramEnum.hasMoreElements())
			{
				String parameterName = (String) paramEnum.nextElement();
				if (paramEnum.equals("page"))
				{
					gotPageParameter = true;
				}
				params.add(parameterName);
			}
			if (!gotPageParameter)
			{
				params.add("page");
			}
			
			return params.elements();
		}

		/* (non-Javadoc)
		 * @see javax.servlet.ServletRequest#getParameterValues(java.lang.String)
		 */
		public String[] getParameterValues(String parameter)
		{

			if (parameter != null && parameter.equals("page"))
			{

				String[] pageDefault = { "0" };

				String[] pageValues = super.getParameterValues("page");
				return pageValues == null ? pageDefault : pageValues;
			}
			return super.getParameterValues(parameter);
		}

	}
}