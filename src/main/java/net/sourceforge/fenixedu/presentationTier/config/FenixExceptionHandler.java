/*
 * Created on 25/Fev/2003
 *
 * 
 */
package net.sourceforge.fenixedu.presentationTier.config;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.InvalidSessionActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;
import net.sourceforge.fenixedu.presentationTier.util.ExceptionInformation;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ExceptionHandler;
import org.apache.struts.config.ExceptionConfig;
import org.fenixedu.bennu.core.util.CoreConfiguration;

/**
 * @author João Mota
 */
public class FenixExceptionHandler extends ExceptionHandler {

    /**
     * Handle the exception. Return the <code>ActionForward</code> instance (if
     * any) returned by the called <code>ExceptionHandler</code>.
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
     * @exception ServletException
     *                if a servlet exception occurs
     * 
     * @since Struts 1.1
     */
    @Override
    public ActionForward execute(Exception ex, ExceptionConfig ae, ActionMapping mapping, ActionForm formInstance,
            HttpServletRequest request, HttpServletResponse response) throws ServletException {

        if (ex instanceof InvalidSessionActionException) {
            ActionErrors errors = new ActionErrors();
            errors.add("error.invalid.session", new ActionError("error.invalid.session"));
            request.setAttribute(Globals.ERROR_KEY, errors);
            return new ActionForward("/loginPage.jsp");
        }

        request.setAttribute(PresentationConstants.ORIGINAL_MAPPING_KEY, mapping);
        request.setAttribute(PresentationConstants.EXCEPTION_STACK_TRACE, ex.getStackTrace());

        if (ae.getScope() != "request") {
            ae.setScope("session");
        }

        // Figure out the error
        ActionMessage error;
        String property;
        if (ex instanceof FenixActionException) {
            error = ((FenixActionException) ex).getError();
            property = ((FenixActionException) ex).getProperty();
        } else {
            error = new ActionError(ae.getKey(), ex.getMessage());
            property = error.getKey();
        }

        // Store the exception
        request.setAttribute(Globals.EXCEPTION_KEY, ex);
        super.storeException(request, property, error, null, ae.getScope());

        ExceptionInformation exceptionInfo = new ExceptionInformation(request, ex);
        String requestContext = exceptionInfo.getRequestContext();

        request.setAttribute(PresentationConstants.ORIGINAL_MAPPING_KEY, mapping);
        request.setAttribute(PresentationConstants.EXCEPTION_STACK_TRACE, ex.getStackTrace());
        request.setAttribute(PresentationConstants.REQUEST_CONTEXT, requestContext);

        if (CoreConfiguration.getConfiguration().developmentMode()) {
            request.setAttribute("debugExceptionInfo", exceptionInfo);
        } else {
            request.setAttribute("requestBean", exceptionInfo.getRequestBean());
            request.setAttribute("exceptionInfo", exceptionInfo.getExceptionInfo());
        }

        return super.execute(ex, ae, mapping, formInstance, request, response);
    }

}
