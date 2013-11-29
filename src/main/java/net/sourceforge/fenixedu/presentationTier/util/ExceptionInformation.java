package net.sourceforge.fenixedu.presentationTier.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.dataTransferObject.support.SupportRequestBean;
import net.sourceforge.fenixedu.domain.functionalities.AbstractFunctionalityContext;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;

import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.security.UserView;

public class ExceptionInformation {

    //thread info
    private String threadName;

    //exception dependent info
    private Throwable exception;
    private List<ThrowableInfo> flatExceptionStack;
    private String formattedStackTrace;
    private String actionErrorClass;
    private String actionErrorMethod;
    private String actionErrorFile;
    private String actionErrorLine;

    //user dependent info
    private String userName;
    private Collection<RoleType> userRoles;

    //request dependent info
    private String requestURI;
    private String requestFullUrl;
    private String requestURL;
    private String requestMethod;
    private String queryString;
    private Map<String, String> queryParameters;
    private ActionMapping actionMapping;
    private Map<String, Object> requestContextEntries;
    private Map<String, Object> sessionContextEntries;

    //extra info
    private Collection<Object> extraInfo;

    //old info messages
    private SupportRequestBean requestBean;
    private String exceptionInfo;
    private String requestContext;
    private String sessionContext;
    private String stackTrace;

    /*
     * TODO Rework this class for efficiency an legibility.
     * 
     */

    /*
     * TODO add to mapping info:
     * 
        ModuleUtils.getInstance().getModuleConfig(request).findActionConfig(arg0)
     * 
     * 
     * */

    public static class ThrowableInfo {
        private boolean cause;
        private boolean suppressed;
        private int level;
        private Throwable subject;

        public ThrowableInfo(boolean isCause, boolean isSurpressed, int level, Throwable subject) {
            super();
            this.cause = isCause;
            this.suppressed = isSurpressed;
            this.level = level;
            this.subject = subject;
        }

        public boolean isCause() {
            return cause;
        }

        public boolean isSuppressed() {
            return suppressed;
        }

        public int getLevel() {
            return level;
        }

        public Throwable getSubject() {
            return subject;
        }

        public static List<ThrowableInfo> getFlatThrowableInfoList(Throwable t) {
            return getFlatThrowableInfoList(t, false, false, 0);
        }

        private static List<ThrowableInfo> getFlatThrowableInfoList(Throwable t, boolean isCause, boolean isSurpressed, int level) {
            List<ThrowableInfo> list = new ArrayList<ThrowableInfo>();
            list.add(new ThrowableInfo(isCause, isSurpressed, level, t));
            for (Throwable supp : t.getSuppressed()) {
                //suppressed are presented one level below
                list.addAll(getFlatThrowableInfoList(supp, false, true, level + 1));
            }
            if (t.getCause() != null) {
                //cause is presented at the same level
                list.addAll(getFlatThrowableInfoList(t.getCause(), true, false, level));
            }
            return list;
        }
    }

    //this method is does too much for non-debug applications. sloowww.
    public static ExceptionInformation buildExceptionInfo(HttpServletRequest request, Throwable ex) {

        ExceptionInformation info = new ExceptionInformation();

        StringBuilder tempBuilder = new StringBuilder();

        StringBuilder exceptionInfo = headerAppend(ex, info);

        // user
        SupportRequestBean requestBean = userInfoContextAppend(request, exceptionInfo, info);
        info.setRequestBean(requestBean);

        // mapping
        mappingContextAppend(request, exceptionInfo, info);

        // requestContext
        requestContextAppend(request, tempBuilder, info);
        info.setRequestContext(tempBuilder.toString());
        exceptionInfo.append("\n[RequestContext] \n");
        exceptionInfo.append(tempBuilder);
        exceptionInfo.append("\n\n");
        tempBuilder.setLength(0);

        // sessionContext
        exceptionInfo.append("\n[SessionContext]\n");
        sessionContextAppend(request, tempBuilder, info);
        info.setSessionContext(tempBuilder.toString());
        exceptionInfo.append(tempBuilder);
        exceptionInfo.append("\n\n");
        tempBuilder.setLength(0);

        // stackTrace
        stackTrace2StringAppend(ex.getStackTrace(), tempBuilder);
        info.setStackTrace(tempBuilder.toString());
        exceptionInfo.append(tempBuilder);

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        String formattedST = sw.toString();
        info.setFormattedStackTrace(formattedST);

        ActionMapping mapping = info.getActionMapping();
        if (mapping != null) {
            info.setActionErrorClass(mapping.getType());
            info.setActionErrorMethod(mapping.getParameter().equals("method") ? info.getQueryParameters().get("method") : "execute");
            String getString = info.getActionErrorClass() + "." + info.getActionErrorMethod();
            String actionError = formattedST.substring(formattedST.indexOf(getString));
            actionError = actionError.substring(0, actionError.indexOf("\n"));

            //This breaks if native method or unknown source. But that is supposed to never happen in this context.
            info.setActionErrorFile(actionError.substring(actionError.indexOf("(") + 1, actionError.indexOf(":")));
            info.setActionErrorLine(actionError.substring(actionError.indexOf(":") + 1, actionError.indexOf(")")));
        }

        info.setExceptionInfo(exceptionInfo.toString());
        return info;
    }

    public static String buildUncaughtExceptionInfo(HttpServletRequest request, Throwable ex) {

        final StringBuilder exceptionInfo = headerAppend(ex, null);

        // user
        userInfoContextAppend(request, exceptionInfo, null);

        // mappings
        mappingContextAppend(request, exceptionInfo, null);

        // requestContext
        exceptionInfo.append("\n[RequestContext] \n");
        requestContextAppend(request, exceptionInfo, null);
        exceptionInfo.append("\n\n");

        // sessionContext
        exceptionInfo.append("\n[SessionContext]\n");
        sessionContextAppend(request, exceptionInfo, null);
        exceptionInfo.append("\n\n");

        // stackTrace
        stackTrace2StringAppend(ex.getStackTrace(), exceptionInfo);

        return exceptionInfo.toString();
    }

    private static StringBuilder headerAppend(Throwable ex, ExceptionInformation info) {
        StringBuilder exceptionInfo = new StringBuilder("- - - - - - - - - - - Error Origin - - - - - - - - - - -\n");
        exceptionInfo.append("\n[Exception] ").append(ex.toString()).append("\n\n");
        if (info != null) {
            info.setException(ex);
            info.setThreadName(Thread.currentThread().getName());
            info.setFlatExceptionStack(ThrowableInfo.getFlatThrowableInfoList(ex));
        }
        return exceptionInfo;
    }

    private static SupportRequestBean userInfoContextAppend(HttpServletRequest request, final StringBuilder exceptionInfo,
            ExceptionInformation info) {

        exceptionInfo.append("[UserLoggedIn] ");

        SupportRequestBean requestBean;
        String user;
        IUserView userView = UserView.getUser();
        if (userView != null) {
            user = userView.getUtilizador();
            exceptionInfo.append(userView.getUtilizador()).append("\n");
            requestBean = SupportRequestBean.generateExceptionBean(userView.getPerson());
            if (AbstractFunctionalityContext.getCurrentContext(request) != null) {
                requestBean.setRequestContext(AbstractFunctionalityContext.getCurrentContext(request)
                        .getSelectedTopLevelContainer());
            }
            if (info != null) {
                info.setUserName(user);
                info.setUserRoles(userView.getRoleTypes());
            }
        } else {
            user = "No user logged in, or session was lost.\n";
            requestBean = new SupportRequestBean();
        }
        exceptionInfo.append(user + "\n");
        return requestBean;
    }

    private static void mappingContextAppend(HttpServletRequest request, final StringBuilder exceptionInfo,
            ExceptionInformation info) {
        String query = request.getQueryString();
        if (info != null) {
            info.setRequestURI(request.getRequestURI());
            info.setRequestURL(request.getRequestURL().toString());
            info.setRequestFullUrl(getRequestFullUrl(request));
            info.setQueryString(query);
            info.setRequestMethod(request.getMethod());

            String[] params = query.split("&");
            Map<String, String> queryParameters = new HashMap<String, String>();
            for (String param : params) {
                String[] entry = param.split("=");
                String name = entry[0];
                String value = entry[1];
                queryParameters.put(name, value);
            }
            info.setQueryParameters(queryParameters);
        }

        exceptionInfo.append("[RequestURI] ").append(request.getRequestURI()).append("\n");
        exceptionInfo.append("[RequestURL] ").append(request.getRequestURL()).append("\n");
        exceptionInfo.append("[QueryString] ").append(request.getQueryString()).append("\n");

        if (request.getAttribute(PresentationConstants.ORIGINAL_MAPPING_KEY) != null) {
            ActionMapping mapping = (ActionMapping) request.getAttribute(PresentationConstants.ORIGINAL_MAPPING_KEY);
            if (info != null) {
                info.setActionMapping(mapping);
            }
            exceptionInfo.append("[Path] ").append(mapping.getPath()).append("\n");
            exceptionInfo.append("[Name] ").append(mapping.getName()).append("\n");
        } else {
            exceptionInfo.append("[Path|Name] impossible to get (exception through UncaughtExceptionFilter)\n");
        }
    }

    private static String getRequestFullUrl(HttpServletRequest request) {
        StringBuffer requestFullURL = request.getRequestURL();
        String queryString = request.getQueryString();
        return queryString == null ? requestFullURL.toString() : requestFullURL.append('?').append(queryString).toString();
    }

    private static void requestContextAppend(HttpServletRequest request, StringBuilder exceptionInfo, ExceptionInformation info) {

        Map<String, Object> requestContext = new HashMap<String, Object>();
        Enumeration requestContents = request.getAttributeNames();
        while (requestContents.hasMoreElements()) {
            String requestElement = requestContents.nextElement().toString();
            if (info != null) {
                requestContext.put(requestElement, request.getAttribute(requestElement));
            }
            exceptionInfo.append("RequestElement:").append(requestElement).append("\n");
            exceptionInfo.append("RequestElement Value:").append(request.getAttribute(requestElement)).append("\n");
        }
        if (info != null) {
            info.setRequestContextEntries(requestContext);
        }
    }

    private static void sessionContextAppend(HttpServletRequest request, StringBuilder exceptionInfo, ExceptionInformation info) {

        Map<String, Object> sessionContext = new HashMap<String, Object>();
        HttpSession session = request.getSession(false);
        if (session != null) {
            Enumeration sessionContents = session.getAttributeNames();
            while (sessionContents.hasMoreElements()) {
                String sessionElement = sessionContents.nextElement().toString();
                if (info != null) {
                    sessionContext.put(sessionElement, session.getAttribute(sessionElement));
                }
                exceptionInfo.append("Element:").append(sessionElement).append("\n");
                exceptionInfo.append("Element Value:").append(session.getAttribute(sessionElement)).append("\n");
            }
            if (info != null) {
                info.setSessionContextEntries(sessionContext);
            }
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

    public String getThreadName() {
        return threadName;
    }

    public Throwable getException() {
        return exception;
    }

    public List<ThrowableInfo> getFlatExceptionStack() {
        return flatExceptionStack;
    }

    public String getFormattedStackTrace() {
        return formattedStackTrace;
    }

    public String getRequestURI() {
        return requestURI;
    }

    public String getRequestURL() {
        return requestURL;
    }

    public String getQueryString() {
        return queryString;
    }

    public Map<String, String> getQueryParameters() {
        return queryParameters;
    }

    public ActionMapping getActionMapping() {
        return actionMapping;
    }

    public Map<String, Object> getRequestContextEntries() {
        return requestContextEntries;
    }

    public Map<String, Object> getSessionContextEntries() {
        return sessionContextEntries;
    }

    public Collection<Object> getExtraInfo() {
        return extraInfo;
    }

    public SupportRequestBean getRequestBean() {
        return requestBean;
    }

    public String getExceptionInfo() {
        return exceptionInfo;
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

    private void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    private void setException(Throwable exception) {
        this.exception = exception;
    }

    private void setFlatExceptionStack(List<ThrowableInfo> flatExceptionStack) {
        this.flatExceptionStack = flatExceptionStack;
    }

    private void setFormattedStackTrace(String formattedStackTrace) {
        this.formattedStackTrace = formattedStackTrace;
    }

    private void setRequestURI(String requestURI) {
        this.requestURI = requestURI;
    }

    private void setRequestURL(String requestURL) {
        this.requestURL = requestURL;
    }

    private void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    private void setQueryParameters(Map<String, String> queryParameters) {
        this.queryParameters = queryParameters;
    }

    private void setActionMapping(ActionMapping actionMapping) {
        this.actionMapping = actionMapping;
    }

    private void setRequestContextEntries(Map<String, Object> requestContextEntries) {
        this.requestContextEntries = requestContextEntries;
    }

    private void setSessionContextEntries(Map<String, Object> sessionContextEntries) {
        this.sessionContextEntries = sessionContextEntries;
    }

    public void setExtraInfo(Collection<Object> extraInfo) {
        this.extraInfo = extraInfo;
    }

    public void addExtraInfo(Object extraInfo) {
        this.extraInfo.add(extraInfo);
    }

    public void addExtraInfo(Collection<Object> extraInfo) {
        this.extraInfo.addAll(extraInfo);
    }

    private void setRequestBean(SupportRequestBean requestBean) {
        this.requestBean = requestBean;
    }

    private void setExceptionInfo(String exceptionInfo) {
        this.exceptionInfo = exceptionInfo;
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

    public String getActionErrorLine() {
        return actionErrorLine;
    }

    private void setActionErrorLine(String actionErrorLine) {
        this.actionErrorLine = actionErrorLine;
    }

    public String getActionErrorClass() {
        return actionErrorClass;
    }

    public String getActionErrorMethod() {
        return actionErrorMethod;
    }

    public String getActionErrorFile() {
        return actionErrorFile;
    }

    private void setActionErrorClass(String actionErrorClass) {
        this.actionErrorClass = actionErrorClass;
    }

    private void setActionErrorMethod(String actionErrorMethod) {
        this.actionErrorMethod = actionErrorMethod;
    }

    private void setActionErrorFile(String actionErrorFile) {
        this.actionErrorFile = actionErrorFile;
    }

    public Collection<RoleType> getUserRoles() {
        return userRoles;
    }

    private void setUserRoles(Collection<RoleType> userRoles) {
        this.userRoles = userRoles;
    }

    public String getUserName() {
        return userName;
    }

    private void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRequestFullUrl() {
        return requestFullUrl;
    }

    public void setRequestFullUrl(String requestFullUrl) {
        this.requestFullUrl = requestFullUrl;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }
}
