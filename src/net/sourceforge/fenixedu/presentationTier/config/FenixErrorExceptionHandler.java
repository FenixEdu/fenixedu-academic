/*
 * Created on 26/Fev/2003
 *  
 */
package net.sourceforge.fenixedu.presentationTier.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.EmptyRequiredFieldServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ExceptionHandler;
import org.apache.struts.config.ExceptionConfig;

/**
 * @author João Mota
 */
public class FenixErrorExceptionHandler extends ExceptionHandler {

    /**
     *  
     */
    public FenixErrorExceptionHandler() {
        super();
    }

    /**
     * Handle the exception. Return the <code>ActionForward</code> instance
     * (if any) returned by the called <code>ExceptionHandler</code>.
     * 
     * @param ex
     *            The exception to handle
     * @param ae
     *            The ExceptionConfig corresponding to the exception
     * @param mapping
     *            The ActionMapping we are processing
     * @param formInstance
     *            The ActionForm we are processing
     * @param request
     *            The servlet request we are processing
     * @param response
     *            The servlet response we are creating
     * 
     * @since Struts 1.1
     */
    public ActionForward execute(Exception ex, ExceptionConfig ae, ActionMapping mapping,
            ActionForm formInstance, HttpServletRequest request, HttpServletResponse response) {

        ActionError error = null;
        String property = null;

        // Figure out the error
        ActionForward forward = mapping.getInputForward();
        if (ex instanceof FenixActionException) {
            FenixActionException fenixActionException = (FenixActionException) ex;
            error = ((FenixActionException) ex).getError();
            property = ((FenixActionException) ex).getProperty();
            forward = fenixActionException.getActionForward() != null ? fenixActionException
                    .getActionForward() : mapping.getInputForward();
        } else if (ex instanceof EmptyRequiredFieldServiceException) {
            error = new ActionError(ex.getMessage());
            property = error.getKey();
        } else {
        	String[] args = null;
        	if (ex instanceof FenixServiceException) {
        		final FenixServiceException fenixServiceException = (FenixServiceException) ex;
        		args = fenixServiceException.getArgs();
        	}
        	if (args == null) {
        		error = new ActionError(ae.getKey(), ex.getMessage());
        	} else {
        		error = new ActionError(ae.getKey(), args);
        	}
            property = error.getKey();
        }
        if (ae.getPath() != null && ae.getPath().length() > 0) {
            forward = new ActionForward(ae.getPath());
        }
        request.setAttribute(Globals.EXCEPTION_KEY, ex);
        super.storeException(request, property, error, forward, ae.getScope());
        return forward;
    }

}