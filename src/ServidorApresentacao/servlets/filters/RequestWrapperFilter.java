package ServidorApresentacao.servlets.filters;

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

import DataBeans.InfoRole;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import Util.RoleType;

/**
 *   17/Fev/2003
 *   @author     jpvl
 */
public class RequestWrapperFilter implements Filter
{


	public void init(FilterConfig config) throws ServletException
	{
	}

	public void destroy()
	{
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
	{
		chain.doFilter(new FenixHttpServletRequestWrapper((HttpServletRequest) request), response);
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
			// TODO Auto-generated method stub
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
