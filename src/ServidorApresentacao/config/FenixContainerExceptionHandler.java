/*
 * Created on 2004/04/07
 *
 */
package ServidorApresentacao.config;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.config.ExceptionConfig;

/**
 * 
 * @author Luis Cruz
 */
public class FenixContainerExceptionHandler extends FenixExceptionHandler {

    public ActionForward execute(Exception ex, ExceptionConfig ae, ActionMapping mapping,
            ActionForm formInstance, HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
        super.execute(ex, ae, mapping, formInstance, request, response);
        throw new ServletException(ex);
    }
}