/*
 * Created on 23/Mar/2003 by jpvl
 *
 */
package ServidorApresentacao.processor;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.RequestProcessor;
import org.apache.struts.util.RequestUtils;

/**
 * @author jpvl
 */
public class FenixRequestProcessor extends RequestProcessor {

	/* (non-Javadoc)
	 * @see org.apache.struts.action.RequestProcessor#processPreprocess(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected boolean processPreprocess(
		HttpServletRequest request,
		HttpServletResponse response) {
			HttpSession session = request.getSession(true);
			String uri = request.getRequestURI();
			if ((session == null) || (session.isNew())
				&& (uri.indexOf("login.do") == -1) && (uri.indexOf("/publico/index.do") == -1)) {
				ActionErrors errors = new ActionErrors ();
			
				errors.add("error.invalid.session", new ActionError("error.invalid.session"));
				request.setAttribute(Action.ERROR_KEY, errors);
			
				String uri2Forward = "/loginPage.jsp";
				String applicationPrefix ="";
				if (uri.indexOf("publico") != -1){
					applicationPrefix = "/publico";
					uri2Forward = "/publico/index.do";
				}

				RequestUtils.selectApplication(applicationPrefix,request, this.getServletContext());

				try {
					doForward(uri2Forward, request, response);
				} catch (IOException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				} catch (ServletException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
				return false;
			} else
				return true;
	}

}
