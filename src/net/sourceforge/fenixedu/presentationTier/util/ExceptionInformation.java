package net.sourceforge.fenixedu.presentationTier.util;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.dataTransferObject.support.SupportRequestBean;
import net.sourceforge.fenixedu.domain.functionalities.AbstractFunctionalityContext;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionConstants;

import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.security.UserView;

public class ExceptionInformation {

    private SupportRequestBean requestBean;
    private String exceptionInfo;
    private String requestContext;
    private String sessionContext;
    private String stackTrace;

    public static ExceptionInformation buildExceptionInfo(HttpServletRequest request, Throwable ex) {

	ExceptionInformation info = new ExceptionInformation();

	StringBuilder tempBuilder = new StringBuilder();

	StringBuilder exceptionInfo = headerAppend(ex);

	// user
	SupportRequestBean requestBean = userInfoContextAppend(request, exceptionInfo);
	info.setRequestBean(requestBean);

	// mapping
	mappingContextAppend(request, exceptionInfo);

	// requestContext
	requestContextAppend(request, tempBuilder);
	info.setRequestContext(tempBuilder.toString());
	exceptionInfo.append("\n[RequestContext] \n");
	exceptionInfo.append(tempBuilder);
	exceptionInfo.append("\n\n");
	tempBuilder.setLength(0);

	// sessionContext
	exceptionInfo.append("\n[SessionContext]\n");
	sessionContextAppend(request, tempBuilder);
	info.setSessionContext(tempBuilder.toString());
	exceptionInfo.append(tempBuilder);
	exceptionInfo.append("\n\n");
	tempBuilder.setLength(0);

	// stackTrace
	stackTrace2StringAppend(ex.getStackTrace(), tempBuilder);
	info.setStackTrace(tempBuilder.toString());
	exceptionInfo.append(tempBuilder);

	info.setExceptionInfo(exceptionInfo.toString());
	return info;
    }

    public static String buildUncaughtExceptionInfo(HttpServletRequest request, Throwable ex) {

	final StringBuilder exceptionInfo = headerAppend(ex);

	// user
	userInfoContextAppend(request, exceptionInfo);

	// mappings
	mappingContextAppend(request, exceptionInfo);

	// requestContext
	exceptionInfo.append("\n[RequestContext] \n");
	requestContextAppend(request, exceptionInfo);
	exceptionInfo.append("\n\n");

	// sessionContext
	exceptionInfo.append("\n[SessionContext]\n");
	sessionContextAppend(request, exceptionInfo);
	exceptionInfo.append("\n\n");

	// stackTrace
	stackTrace2StringAppend(ex.getStackTrace(), exceptionInfo);

	return exceptionInfo.toString();
    }

    private static StringBuilder headerAppend(Throwable ex) {
	StringBuilder exceptionInfo = new StringBuilder("- - - - - - - - - - - Error Origin - - - - - - - - - - -\n");
	exceptionInfo.append("\n[Exception] ").append(ex.toString()).append("\n\n");
	return exceptionInfo;
    }

    private static SupportRequestBean userInfoContextAppend(HttpServletRequest request, final StringBuilder exceptionInfo) {

	exceptionInfo.append("[UserLoggedIn] ");

	SupportRequestBean requestBean;
	IUserView userView = UserView.getUser();
	if (userView != null) {
	    exceptionInfo.append(userView.getUtilizador()).append("\n");
	    requestBean = SupportRequestBean.generateExceptionBean(userView.getPerson());
	    if (AbstractFunctionalityContext.getCurrentContext(request) != null) {
		requestBean.setRequestContext(AbstractFunctionalityContext.getCurrentContext(request)
			.getSelectedTopLevelContainer());
	    }
	} else {
	    exceptionInfo.append("No user logged in, or session was lost.\n");
	    requestBean = new SupportRequestBean();
	}
	exceptionInfo.append("\n");
	return requestBean;
    }

    private static void mappingContextAppend(HttpServletRequest request, final StringBuilder exceptionInfo) {
	exceptionInfo.append("[RequestURI] ").append(request.getRequestURI()).append("\n");
	exceptionInfo.append("[RequestURL] ").append(request.getRequestURL()).append("\n");
	exceptionInfo.append("[QueryString] ").append(request.getQueryString()).append("\n");

	if (request.getAttribute(SessionConstants.ORIGINAL_MAPPING_KEY) != null) {
	    ActionMapping mapping = (ActionMapping) request.getAttribute(SessionConstants.ORIGINAL_MAPPING_KEY);
	    exceptionInfo.append("[Path] ").append(mapping.getPath()).append("\n");
	    exceptionInfo.append("[Name] ").append(mapping.getName()).append("\n");
	} else {
	    exceptionInfo.append("[Path|Name] impossible to get (exception through UncaughtExceptionFilter)\n");
	}
    }

    private static void requestContextAppend(HttpServletRequest request, StringBuilder exceptionInfo) {

	Enumeration requestContents = request.getAttributeNames();
	while (requestContents.hasMoreElements()) {
	    String requestElement = requestContents.nextElement().toString();
	    exceptionInfo.append("RequestElement:").append(requestElement).append("\n");
	    exceptionInfo.append("RequestElement Value:").append(request.getAttribute(requestElement)).append("\n");
	}
    }

    private static void sessionContextAppend(HttpServletRequest request, StringBuilder exceptionInfo) {

	HttpSession session = request.getSession(false);
	Enumeration sessionContents = session.getAttributeNames();
	while (sessionContents.hasMoreElements()) {
	    String sessionElement = sessionContents.nextElement().toString();
	    exceptionInfo.append("Element:").append(sessionElement).append("\n");
	    exceptionInfo.append("Element Value:").append(session.getAttribute(sessionElement)).append("\n");
	}
    }

    private static void stackTrace2StringAppend(StackTraceElement[] stackTrace, StringBuilder exceptionInfo) {

	exceptionInfo.append("StackTrace: \n ");
	if (stackTrace != null) {
	    int i = 0;
	    while (i < stackTrace.length) {
		exceptionInfo.append(stackTrace[i++]).append("\n");
	    }
	}
    }

    public SupportRequestBean getRequestBean() {
	return requestBean;
    }

    public String getRequestContext() {
	return requestContext;
    }

    public String getSessionContext() {
	return sessionContext;
    }

    public String getStackTrace() {
	return stackTrace;
    }

    private void setRequestBean(SupportRequestBean requestBean) {
	this.requestBean = requestBean;
    }

    private void setRequestContext(String requestContext) {
	this.requestContext = requestContext;
    }

    private void setSessionContext(String sessionContext) {
	this.sessionContext = sessionContext;
    }

    private void setStackTrace(String stackTrace) {
	this.stackTrace = stackTrace;
    }

    public String getExceptionInfo() {
	return exceptionInfo;
    }

    private void setExceptionInfo(String exceptionInfo) {
	this.exceptionInfo = exceptionInfo;
    }

}
