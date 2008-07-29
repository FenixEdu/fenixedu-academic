/*
 * Created on 25/Fev/2003
 *
 * 
 */
package net.sourceforge.fenixedu.presentationTier.config;

import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.dataTransferObject.support.SupportRequestBean;
import net.sourceforge.fenixedu.domain.log.requests.ErrorLog;
import net.sourceforge.fenixedu.domain.log.requests.RequestLog;
import net.sourceforge.fenixedu.domain.support.SupportRequestPriority;
import net.sourceforge.fenixedu.domain.support.SupportRequestType;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.InvalidSessionActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionConstants;
import net.sourceforge.fenixedu.util.ArrayUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ExceptionHandler;
import org.apache.struts.config.ExceptionConfig;

import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixframework.pstm.Transaction;

/**
 * @author João Mota
 */
public class FenixExceptionHandler extends ExceptionHandler {

    /**
     * Handle the exception. Return the <code>ActionForward</code> instance
     * (if any) returned by the called <code>ExceptionHandler</code>.
     * 
     * @param ex
     *                The exception to handle
     * @param ae
     *                The ExceptionConfig corresponding to the exception
     * @param mapping
     *                The ActionMapping we are processing
     * @param formInstance
     *                The ActionForm we are processing
     * @param request
     *                The servlet request we are processing
     * @param response
     *                The servlet response we are creating
     * 
     * @exception ServletException
     *                    if a servlet exception occurs
     * 
     * @since Struts 1.1
     */
    public ActionForward execute(Exception ex, ExceptionConfig ae, ActionMapping mapping, ActionForm formInstance,
	    HttpServletRequest request, HttpServletResponse response) throws ServletException {

	ActionError error = null;

	if (ex instanceof InvalidSessionActionException) {

	    ActionErrors errors = new ActionErrors();
	    error = new ActionError("error.invalid.session");
	    errors.add("error.invalid.session", error);
	    request.setAttribute(Globals.ERROR_KEY, errors);
	    return mapping.findForward("firstPage");
	}

	request.setAttribute(SessionConstants.ORIGINAL_MAPPING_KEY, mapping);

	request.setAttribute(SessionConstants.EXCEPTION_STACK_TRACE, ex.getStackTrace());

	final String requestContext = requestContextGetter(request);
	request.setAttribute(SessionConstants.REQUEST_CONTEXT, requestContext);

	final StringBuilder exceptionInfo = new StringBuilder("Error Origin: \n");
	exceptionInfo.append("Exception: \n" + ex + "\n\n");

	SupportRequestBean requestBean = new SupportRequestBean(SupportRequestType.EXCEPTION, SupportRequestPriority.IMPEDIMENT);
	IUserView userView = UserView.getUser();
	if (userView != null) {
	    exceptionInfo.append("UserLogedIn: " + userView.getUtilizador() + "\n");
	    requestBean.setResponseEmail(userView.getPerson().getDefaultEmailAddress().getValue());
	} else {
	    exceptionInfo.append("No user logged in, or session was lost.\n");
	}

	exceptionInfo.append("RequestURI: " + request.getRequestURI() + "\n");
	exceptionInfo.append("RequestURL: " + request.getRequestURL() + "\n");
	exceptionInfo.append("QueryString: " + request.getQueryString() + "\n");

	exceptionInfo.append("RequestContext: \n" + requestContext + "\n\n\n");
	final String sessionContext = sessionContextGetter(request);
	exceptionInfo.append("SessionContext: \n" + sessionContext + "\n\n\n");

	if (mapping != null) {
	    exceptionInfo.append("Path: " + mapping.getPath() + "\n");
	    exceptionInfo.append("Name: " + mapping.getName() + "\n");
	}

	exceptionInfo.append("\n\n");
	final String stackTrace = stackTrace2String(ex.getStackTrace());
	exceptionInfo.append(stackTrace);

	request.setAttribute("exceptionInfo", exceptionInfo.toString());

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
	request.setAttribute(Globals.EXCEPTION_KEY, ex);
	super.storeException(request, property, error, null, ae.getScope());

	String[] parameters = ArrayUtils.toStringArray(request.getParameterNames(), "_request_checksum_", "jsessionid");
	ErrorLogger errorLogger = new ErrorLogger(request.getRequestURI(), request.getHeader("referer"), parameters, request
		.getQueryString(), userView == null ? StringUtils.EMPTY : userView.getUtilizador(), requestContext,
		sessionContext, stackTrace, ex.getClass().getName());
	errorLogger.start();
	try {
	    errorLogger.join();
	    requestBean.setErrorLog(errorLogger.getErrorLog());
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}

	request.setAttribute("requestBean", requestBean);

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

    private String sessionContextGetter(HttpServletRequest request) {
	HttpSession session = request.getSession(false);
	Enumeration sessionContents = session.getAttributeNames();
	String context = "";
	while (sessionContents.hasMoreElements()) {
	    String sessionElement = sessionContents.nextElement().toString();
	    context += "Element:" + sessionElement + "\n";
	    context += "Element Value:" + session.getAttribute(sessionElement) + "\n";
	}

	return context;
    }

    private String stackTrace2String(StackTraceElement[] stackTrace) {
	String result = "StackTrace: \n ";
	int i = 0;
	if (stackTrace != null) {
	    while (i < stackTrace.length) {
		result += stackTrace[i] + "\n";
		i++;
	    }
	}
	return result;
    }

    private static class ErrorLogger extends Thread {

	private String path;

	private String referer;

	private String[] parameters;

	private String queryString;

	private String user;

	private String requestAttributes;

	private String sessionAttributes;

	private String stackTrace;

	private String exceptionType;

	private ErrorLog errorLog;

	public ErrorLogger(String path, String referer, String[] parameters, String queryString, String user,
		String requestAttributes, String sessionAttributes, String stackTrace, String exceptionType) {
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
	}

	@Override
	public void run() {
	    Transaction.withTransaction(new jvstm.TransactionalCommand() {
		public void doIt() {
		    errorLog = RequestLog.registerError(path, referer, parameters, queryString, user, requestAttributes,
			    sessionAttributes, stackTrace, exceptionType).getErrorLog();
		}
	    });
	}

	public ErrorLog getErrorLog() {
	    return this.errorLog;
	}

    }

}
