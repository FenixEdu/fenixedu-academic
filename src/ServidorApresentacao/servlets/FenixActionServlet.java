/*
 * Created on 20/Mar/2003 by jpvl
 *
 */
package ServidorApresentacao.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionServlet;


/**
 * @author jpvl
 */
public class FenixActionServlet extends ActionServlet {
	/**
	 * Tests if the session is valid.
	 * FIXME Set ActionErrors and do forward...
	 * @see javax.servlet.http.HttpServlet#service(HttpServletRequest, HttpServletResponse)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		if ( (session == null || session.isNew()) && (request.getRequestURI().indexOf("login.do") == -1)){
			response.sendRedirect(request.getContextPath()+"/loginPage.jsp");
		}else
			super.service(request, response);
	}

}
