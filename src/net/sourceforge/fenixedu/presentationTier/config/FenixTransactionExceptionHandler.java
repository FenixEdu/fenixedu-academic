/*
 * Created on 26/Fev/2003
 *
 *
 */
package net.sourceforge.fenixedu.presentationTier.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ExceptionHandler;
import org.apache.struts.config.ExceptionConfig;

/**
 * This handler requires that mapping receive should have a forward name
 * 'beginTransaction'
 * 
 * @author João Luz
 */
public class FenixTransactionExceptionHandler extends ExceptionHandler {

    public ActionForward execute(Exception ex, ExceptionConfig ae, ActionMapping mapping,
            ActionForm formInstance, HttpServletRequest request, HttpServletResponse response) {

        ActionForward forward = null;
        ActionError error = null;
        String property = null;

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

        return mapping.findForward("beginTransaction");
    }

}