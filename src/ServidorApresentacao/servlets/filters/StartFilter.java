package ServidorApresentacao.servlets.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 *   17/Fev/2003
 *   @author     jpvl
 */
public class StartFilter implements Filter {

	private FilterConfig config = null;

	public void init(FilterConfig config) throws ServletException {
		this.config = config;
	}

	public void destroy() {
		config = null;
	}

	public void doFilter(
		ServletRequest request,
		ServletResponse response,
		FilterChain chain)
		throws IOException, ServletException {
		

		HttpServletRequest httpRequest = (HttpServletRequest) request;

		ServletContext context = config.getServletContext();
		
		HttpSession session = httpRequest.getSession(true);
		
		
		/*:FIXME: use other context constant (not SessionConstants.) */
		session.setAttribute(SessionConstants.INFO_EXECUTION_PERIOD_KEY, context.getAttribute(SessionConstants.INFO_EXECUTION_PERIOD_KEY));
		
		chain.doFilter(request, response);
	}

}
