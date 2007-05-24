package net.sourceforge.fenixedu.presentationTier.config;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jvstm.cps.ConsistencyException;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ExceptionHandler;
import org.apache.struts.config.ExceptionConfig;

public class FenixConsistencyExceptionHandler extends ExceptionHandler {

    private static final String SKIP_PREFIX = "net.sourceforge.fenixedu.domain.";

    public ActionForward execute(Exception ex, ExceptionConfig exceptionConfig, ActionMapping mapping,
                                 ActionForm actionForm, HttpServletRequest request, HttpServletResponse response)
        throws ServletException {

        ActionForward forward = mapping.getInputForward();

        if (ex instanceof ConsistencyException) {
            ConsistencyException consistencyException = (ConsistencyException) ex;
            String property = consistencyException.getMethodFullname().replace(SKIP_PREFIX, "");
            ActionError error = new ActionError(property, consistencyException.getTarget());
			
            super.storeException(request, property, error, forward, exceptionConfig.getScope());
        }
        return forward;
    }

}
