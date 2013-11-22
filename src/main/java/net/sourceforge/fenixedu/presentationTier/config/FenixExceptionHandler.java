/*
 * Created on 25/Fev/2003
 *
 * 
 */
package net.sourceforge.fenixedu.presentationTier.config;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.support.SupportRequestBean;
import net.sourceforge.fenixedu.domain.log.requests.ErrorLog;
import net.sourceforge.fenixedu.domain.log.requests.RequestLog;
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
import org.apache.struts.action.ExceptionHandler;
import org.apache.struts.config.ExceptionConfig;

import pt.ist.fenixframework.Atomic;

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
            return mapping.findForward("firstPage");
        }

        request.setAttribute(PresentationConstants.ORIGINAL_MAPPING_KEY, mapping);
        request.setAttribute(PresentationConstants.EXCEPTION_STACK_TRACE, ex.getStackTrace());

        if (ae.getScope() != "request") {
            ae.setScope("session");
        }

        // Figure out the error
        ActionError error;
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

        ExceptionInformation exceptionInfo = ExceptionInformation.buildExceptionInfo(request, ex);
        String requestContext = exceptionInfo.getRequestContext();
        // String sessionContext = exceptionInfo.getSessionContext();
        // String stackTrace = exceptionInfo.getStackTrace();

        request.setAttribute(PresentationConstants.ORIGINAL_MAPPING_KEY, mapping);
        request.setAttribute(PresentationConstants.EXCEPTION_STACK_TRACE, ex.getStackTrace());
        request.setAttribute(PresentationConstants.REQUEST_CONTEXT, requestContext);

        // String[] parameters =
        // ArrayUtils.toStringArray(request.getParameterNames(),
        // "_request_checksum_", "jsessionid");
        // ErrorLogger errorLogger = new ErrorLogger(request.getRequestURI(),
        // request.getHeader("referer"), parameters, request
        // .getQueryString(), UserView.getUser() == null ? StringUtils.EMPTY :
        // ((IUserView) UserView.getUser())
        // .getUtilizador(), requestContext, sessionContext, stackTrace,
        // ex.getClass().getName(),
        // request.getMethod().equalsIgnoreCase("POST"));

        // errorLogger.start();

        SupportRequestBean requestBean = exceptionInfo.getRequestBean();

        // try {
        // errorLogger.join();
        // requestBean.setErrorLog(errorLogger.getErrorLog());
        // } catch (InterruptedException e) {
        // e.printStackTrace();
        // }

        if (request.getServerName().equals("localhost")) {
            request.setAttribute("debugExceptionInfo", exceptionInfo);
        } else {
            request.setAttribute("requestBean", requestBean);
            request.setAttribute("exceptionInfo", exceptionInfo.getExceptionInfo());
        }

        return super.execute(ex, ae, mapping, formInstance, request, response);
    }

    private static class ErrorLogger extends Thread {

        private final String path;

        private final String referer;

        private final String[] parameters;

        private final String queryString;

        private final String user;

        private final String requestAttributes;

        private final String sessionAttributes;

        private final String stackTrace;

        private final String exceptionType;

        private ErrorLog errorLog;

        private final Boolean post;

        public ErrorLogger(String path, String referer, String[] parameters, String queryString, String user,
                String requestAttributes, String sessionAttributes, String stackTrace, String exceptionType, Boolean post) {
            super();
            this.path = path;
            this.parameters = parameters;
            this.referer = referer;
            this.queryString = queryString;
            this.user = user;
            this.requestAttributes = requestAttributes;
            this.sessionAttributes = sessionAttributes;
            this.stackTrace = stackTrace;
            this.exceptionType = exceptionType;
            this.post = post;
        }

        @Atomic
        @Override
        public void run() {
            errorLog =
                    RequestLog.registerError(path, referer, parameters, queryString, user, requestAttributes, sessionAttributes,
                            stackTrace, exceptionType, post).getErrorLog();
        }

        public ErrorLog getErrorLog() {
            return this.errorLog;
        }

    }

}
