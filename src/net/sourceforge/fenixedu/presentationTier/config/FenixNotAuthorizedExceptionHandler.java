/*
 * Created on 2/Dez/2003
 *  
 */
package net.sourceforge.fenixedu.presentationTier.config;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ExceptionHandler;
import org.apache.struts.config.ExceptionConfig;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class FenixNotAuthorizedExceptionHandler extends ExceptionHandler {

    /**
     *  
     */
    public FenixNotAuthorizedExceptionHandler() {
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
     * @exception ServletException
     *                if a servlet exception occurs
     * 
     * @since Struts 1.1
     */
    public ActionForward execute(Exception ex, ExceptionConfig exceptionConfig, ActionMapping mapping,
            ActionForm actionForm, HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
        try {
            ActionForward forward = mapping.getInputForward();

            ActionForward newForward = new ActionForward();
            PropertyUtils.copyProperties(newForward, forward);
            StringBuffer path = new StringBuffer();
            path.append("/teacherAdministrationViewer.do?method=instructions").append("&objectCode=")
                    .append(request.getParameter("executionCourseId"));
            newForward.setPath(path.toString());
            // Store the exception
            ActionError actionError = new ActionError(exceptionConfig.getKey());
            super.storeException(request, exceptionConfig.getKey(), actionError, newForward,
                    exceptionConfig.getScope());
            return newForward;
        } catch (Exception e) {
            throw new ServletException(e.getMessage());
        }
    }

}