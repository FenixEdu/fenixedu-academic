package net.sourceforge.fenixedu.presentationTier.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.dataTransferObject.support.SupportRequestBean;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.functionalities.AbstractFunctionalityContext;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;
import net.sourceforge.fenixedu.presentationTier.util.ExceptionInformation.ThrowableInfo;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import pt.ist.bennu.core.domain.User;
import pt.ist.bennu.core.security.Authenticate;

public class ExceptionInformation {

    //thread info
    private String threadName;

    //exception dependent info
    private Throwable exception;
    private List<ThrowableInfo> flatExceptionStack;
    private String formattedStackTrace;
    private Class<?> actionErrorClass;
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
        private final boolean cause;
        private final boolean suppressed;
        private final int level;
        private final Throwable subject;
        private final List<ElementInfo> subjectInfo;

        public ThrowableInfo(boolean isCause, boolean isSurpressed, int level, Throwable subject) {
            super();
            this.cause = isCause;
            this.suppressed = isSurpressed;
            this.level = level;
            this.subject = subject;
            this.subjectInfo = getSubjectInfo(subject);
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

        public List<ElementInfo> getSubjectInfo() {
            return subjectInfo;
        }

        private static List<ElementInfo> getSubjectInfo(Throwable subject) {
            List<ElementInfo> subjectInfo = new ArrayList<>();
            for (StackTraceElement element : subject.getStackTrace()) {
                subjectInfo.add(new ElementInfo(element));
            }
            return subjectInfo;
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

    public static class ElementInfo {
        private final StackTraceElement element;
        private boolean isExternalClass;
        private final String simpleClassName;
        private final String methodName;
        private final String packageName;
        private final int line;
        private final boolean isNative;
        private final String fileName;

        public ElementInfo(StackTraceElement element) {
            this.element = element;
            this.simpleClassName = getSimpleClassName(element.getClassName());
            this.packageName = getPackageName(element.getClassName());
            this.isExternalClass = isExternalClass(element.getClassName());
            this.methodName = element.getMethodName();
            this.line = element.getLineNumber();
            this.isNative = element.isNativeMethod();
            this.fileName = element.getFileName();
        }

        private boolean isExternalClass(String className) {
            return StringUtils.startsWith(className, "net.sourceforge.fenixedu") || StringUtils.startsWith(className, "pt.ist");
        }

        private String getSimpleClassName(String className) {
            String[] parse = StringUtils.split(className, ".");
            return parse[parse.length - 1];
        }

        private String getPackageName(String className) {
            return className.substring(0, className.lastIndexOf("."));
        }

        public StackTraceElement getElement() {
            return element;
        }

        public boolean isExternalClass() {
            return isExternalClass;
        }

        public void setExternalClass(boolean isExternalClass) {
            this.isExternalClass = isExternalClass;
        }

        public String getSimpleClassName() {
            return simpleClassName;
        }

        public String getMethodName() {
            return methodName;
        }

        public String getPackageName() {
            return packageName;
        }

        public int getLine() {
            return line;
        }

        public boolean isNative() {
            return isNative;
        }

        public String getFileName() {
            return fileName;
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
            Class<?> actionClass = actionClass(mapping.getType());
            info.setActionErrorClass(actionClass);
            if (DispatchAction.class.isAssignableFrom(actionClass)) {
                // For DispatchActions we can try to better pinpoint the method...
                info.setActionErrorMethod(request.getParameter(mapping.getParameter()));
            } else {
                // ... for the others, we can just look for execute
                info.setActionErrorMethod("execute");
            }
            String getString = info.getActionErrorClass().getName() + "." + info.getActionErrorMethod();
            String actionError = formattedST.substring(formattedST.indexOf(getString));
            actionError = actionError.substring(0, actionError.indexOf("\n"));

            //This breaks if native method or unknown source. But that is supposed to never happen in this context.
            info.setActionErrorFile(actionError.substring(actionError.indexOf("(") + 1, actionError.indexOf(":")));
            info.setActionErrorLine(actionError.substring(actionError.indexOf(":") + 1, actionError.indexOf(")")));
        }

        info.setExceptionInfo(exceptionInfo.toString());
        return info;
    }

    private static final Class<?> actionClass(String type) {
        try {
            return Class.forName(type);
        } catch (ClassNotFoundException e) {
            throw new Error("I can't seem to find the class I was just in");
        }
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
        User userView = Authenticate.getUser();
        if (userView != null) {
            user = userView.getUsername();
            exceptionInfo.append(userView.getUsername()).append("\n");
            requestBean = SupportRequestBean.generateExceptionBean(userView.getPerson());
            if (AbstractFunctionalityContext.getCurrentContext(request) != null) {
                requestBean.setRequestContext(AbstractFunctionalityContext.getCurrentContext(request)
                        .getSelectedTopLevelContainer());
            }
            if (info != null) {
                info.setUserName(user);
                Set<RoleType> roles = new HashSet<RoleType>();
                for (Role role : userView.getPerson().getPersonRolesSet()) {
                    roles.add(role.getRoleType());
                }
                info.setUserRoles(roles);
            }
        } else {
            user = "No user logged in, or session was lost.\n";
            requestBean = SupportRequestBean.generateExceptionBean(null);
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

    public Class<?> getActionErrorClass() {
        return actionErrorClass;
    }

    public String getActionErrorMethod() {
        return actionErrorMethod;
    }

    public String getActionErrorFile() {
        return actionErrorFile;
    }

    private void setActionErrorClass(Class<?> actionErrorClass) {
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
