/*
 * Created on 25/Fev/2003
 *
 * 
 */
package ServidorApresentacao.config;

import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ExceptionHandler;
import org.apache.struts.config.ExceptionConfig;

import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.exceptions.InvalidSessionActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author João Mota
 */
public class FenixExceptionHandler extends ExceptionHandler {

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
     * @exception ServletException
     *                if a servlet exception occurs
     * 
     * @since Struts 1.1
     */
    public ActionForward execute(Exception ex, ExceptionConfig ae, ActionMapping mapping,
            ActionForm formInstance, HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
        ActionForward forward = null;

        ActionError error = null;

        HttpSession session = request.getSession(false);

        if (ex instanceof InvalidSessionActionException) {

            ActionErrors errors = new ActionErrors();
            error = new ActionError("error.invalid.session");
            errors.add("error.invalid.session", error);
            request.setAttribute(Globals.ERROR_KEY, errors);
            return mapping.findForward("firstPage");

        }

        session.setAttribute(SessionConstants.ORIGINAL_MAPPING_KEY, mapping);

        session.setAttribute(SessionConstants.EXCEPTION_STACK_TRACE, ex.getStackTrace());

        session.setAttribute(SessionConstants.REQUEST_CONTEXT, requestContextGetter(request));

        if (ae.getScope() != "request") {
            ae.setScope("session");
        }

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
        session.setAttribute(Globals.EXCEPTION_KEY, ex);
        super.storeException(request, property, error, forward, ae.getScope());

        // Print info to standard output
        ex.printStackTrace(System.out);
        return super.execute(ex, ae, mapping, formInstance, request, response);
    }

    private String requestContextGetter(HttpServletRequest request) {

        Enumeration requestContents = request.getAttributeNames();
        String context = "";
        while (requestContents.hasMoreElements()) {
            String requestElement = requestContents.nextElement().toString();
            context += "RequestElement:" + requestElement + "\n";
            context += "RequestElement Value:" + request.getAttribute(requestElement) + "\n";
        }

        return context;
    }

}