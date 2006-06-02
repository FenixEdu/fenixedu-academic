package net.sourceforge.fenixedu.renderers.plugin;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * Inteface used to mark an action as an exception handler for the renderers request processor.
 * 
 * @author cfgi
 */
public interface ExceptionHandler {
    /**
     * This method wil lbe called by the {@link RenderersRequestProcessor} when an exception occurs
     * during the processing of a viewstate. Those exceptions normally represent problems when converting
     * values or when updating certain object in the application's domain.
     * 
     * @param request the current request
     * @param mapping 
     * @param input an action forward that maps to the place were the viewstate was created
     * @param e the exception that occured
     * 
     * @return an action forward representing were to go next or <code>null</code> to let struts decide
     */
    public ActionForward processException(HttpServletRequest request, ActionMapping mapping, ActionForward input, Exception e);
}
