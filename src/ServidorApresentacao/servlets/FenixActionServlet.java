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
	protected void service(
		HttpServletRequest request,
		HttpServletResponse response)
		throws ServletException, IOException {
		HttpSession session = request.getSession(true);

//		System.out.println("Requested URI:" + request.getRequestURI());
//		System.out.println("Session is new?" + session.isNew());
//		System.out.println("Session is null?" + (session == null));
		String uri = request.getRequestURI();
		if ((session == null
			|| (session.isNew()
				&& !session.getAttributeNames().hasMoreElements()))
			&& (request.getRequestURI().indexOf("login.do") == -1)) {
			System.out.println("Redireccionei!");
			if (uri.indexOf("publico") == -1)
				response.sendRedirect(
					request.getContextPath() + "/loginPage.jsp");
			else
				response.sendRedirect(request.getContextPath() + "/publico");
		} else
			super.service(request, response);
	}

}
