/**
 * Oct 4, 2005
 */
package net.sourceforge.fenixedu.presentationTier.config;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.config.ExceptionConfig;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class FenixExceptionMessageHandler extends FenixExceptionHandler {

    public ActionForward execute(Exception ex, ExceptionConfig exceptionConfig, ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws ServletException {

	super.execute(ex, exceptionConfig, mapping, actionForm, request, response);

	ActionForward forward = mapping.getInputForward();
	ActionError error = new ActionError(ex.getMessage());
	super.storeException(request, ex.getMessage(), error, forward, exceptionConfig.getScope());
	return forward;
    }
}
