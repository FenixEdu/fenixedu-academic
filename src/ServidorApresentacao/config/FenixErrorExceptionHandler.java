/*
 * Created on 26/Fev/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code Template
 */
package ServidorApresentacao.config;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ExceptionHandler;
import org.apache.struts.config.ExceptionConfig;

/**
 * @author jmota
 */
public class FenixErrorExceptionHandler extends ExceptionHandler {

	/**
			 * Handle the exception.
			 * Return the <code>ActionForward</code> instance (if any) returned by
			 * the called <code>ExceptionHandler</code>.
			 *
			 * @param ex The exception to handle
			 * @param ae The ExceptionConfig corresponding to the exception
			 * @param mapping The ActionMapping we are processing
			 * @param formInstance The ActionForm we are processing
			 * @param request The servlet request we are processing
			 * @param response The servlet response we are creating
			 *
			 * @exception ServletException if a servlet exception occurs
			 *
			 * @since Struts 1.1
			 */
	public ActionForward execute(
		Exception ex,
		ExceptionConfig ae,
		ActionMapping mapping,
		ActionForm formInstance,
		HttpServletRequest request,
		HttpServletResponse response)
		throws ServletException {
		ActionForward forward = null;

		ActionError error = null;
	 	String property = null;

	  // Build the forward from the exception mapping if it exists
	  // or from the form input
	  if (ae.getPath() != null) {
		  forward = new ActionForward(ae.getPath());
		  forward.setContextRelative(true);
	  } else {
	  forward = mapping.getInputForward();
	  }

	  // Figure out the error
	  if (ex instanceof FenixActionException) {
		  error = ((FenixActionException) ex).getError();
		  property = ((FenixActionException) ex).getProperty();
	  } else {
		  error = new ActionError(ae.getKey(), ex.getMessage());
		  property = error.getKey();
	  }

	  // Store the exception
	  request.setAttribute(Globals.EXCEPTION_KEY, ex);
	  super.storeException(request, property, error, forward, ae.getScope());

	
	
		return mapping.getInputForward();
	}
	
	

}
