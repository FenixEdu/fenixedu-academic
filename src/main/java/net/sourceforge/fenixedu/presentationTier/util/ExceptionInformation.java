/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.presentationTier.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.dataTransferObject.support.SupportRequestBean;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.portal.domain.MenuFunctionality;
import org.fenixedu.bennu.portal.servlet.BennuPortalDispatcher;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Maps;

public class ExceptionInformation {

    //thread info
    private String threadName;

    //exception dependent info
    private Throwable exception;
    private List<ThrowableInfo> flatExceptionStack;
    private final String formattedStackTrace;
    private Class<?> actionErrorClass;
    private String actionErrorMethod;
    private String actionErrorFile;
    private int actionErrorLine;

    //user dependent info
    private String userName;
    private Collection<RoleType> userRoles;

    //request dependent info
    private String requestURI;
    private String requestFullUrl;
    private String requestURL;
    private String requestMethod;
    private String queryString;
    private Map<String, String> requestParameters;
    private ActionMapping actionMapping;
    private Map<String, Object> requestContextEntries;
    private Map<String, Object> sessionContextEntries;

    //extra info
    private Collection<Object> extraInfo;

    //old info messages
    private final SupportRequestBean requestBean;
    private final String exceptionInfo;
    private final String requestContext;
    private final String sessionContext;
    private final String stackTrace;

    public static class JSPLine {
        public String lineNumber;
        public String line;

        public JSPLine(String lineNumber, String line) {
            this.lineNumber = lineNumber.trim();
            this.line = line;
        }

        public String getLineNumber() {
            return lineNumber;
        }

        public void setLineNumber(String ln) {
            lineNumber = ln;
        }

        public String getLine() {
            return line;
        }

        public void setLine(String ln) {
            line = ln;
        }
    }

    private String jspExceptionMessage;
    private ArrayList<JSPLine> jspExceptionSourceBefore;
    private JSPLine jspExceptionSourceLine;
    private ArrayList<JSPLine> jspExceptionSourceAfter;
    private String jspExceptionLine;

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
        private final boolean isExternalClass;
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
            return StringUtils.startsWith(className, "net.sourceforge.fenixedu") || StringUtils.startsWith(className, "pt.ist")
                    || StringUtils.startsWith(className, "org.fenixedu");
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

    public ExceptionInformation(HttpServletRequest request, Throwable ex) {
        StringBuilder tempBuilder = new StringBuilder();

        StringBuilder exceptionInfo = headerAppend(ex);

        // user
        this.requestBean = userInfoContextAppend(request, exceptionInfo);

        // mapping
        mappingContextAppend(request, exceptionInfo);

        // requestContext
        requestContextAppend(request, tempBuilder);
        this.requestContext = tempBuilder.toString();
        exceptionInfo.append("\n[RequestContext] \n");
        exceptionInfo.append(tempBuilder);
        exceptionInfo.append("\n\n");
        tempBuilder.setLength(0);

        // sessionContext
        exceptionInfo.append("\n[SessionContext]\n");
        sessionContextAppend(request, tempBuilder);
        this.sessionContext = tempBuilder.toString();
        exceptionInfo.append(tempBuilder);
        exceptionInfo.append("\n\n");
        tempBuilder.setLength(0);

        // stackTrace
        stackTrace2StringAppend(ex.getStackTrace(), tempBuilder);
        this.stackTrace = tempBuilder.toString();
        exceptionInfo.append(tempBuilder);

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        String formattedST = sw.toString();
        this.formattedStackTrace = formattedST;

        ActionMapping mapping = this.getActionMapping();
        if (mapping != null) {
            StackTraceElement element = getStackTraceElementForActionMapping(request, mapping, ex.getStackTrace());
            if (element != null) {
                this.actionErrorFile = element.getFileName();
                this.actionErrorLine = element.getLineNumber();
            }
        }
        this.exceptionInfo = exceptionInfo.toString();

        if (ex.getClass().getName().equals("org.apache.jasper.JasperException")) {
            String message = ex.getLocalizedMessage();
            if (message.contains("\n")) {
                String[] name = message.split("\n");
                final Pattern lastIntPattern = Pattern.compile("([0-9]+)$");
                Matcher matcher = lastIntPattern.matcher(name[0]);
                if (matcher.find()) {
                    jspExceptionLine = matcher.group(1);
                    jspExceptionMessage = name[0];
                    setJspExceptionSourceBefore(new ArrayList<JSPLine>());
                    setJspExceptionSourceAfter(new ArrayList<JSPLine>());
                    int state = 0;
                    for (String s : Arrays.copyOfRange(name, 2, name.length - 3)) {
                        int i = s.indexOf(":");
                        JSPLine line = new JSPLine(s.substring(0, i).toString(), s.substring(i + 1, s.length()).toString());
                        switch (state) {
                        case 0:
                            if (s.startsWith(jspExceptionLine)) {

                                setJspExceptionSourceLine(line);
                                state = 1;
                            } else {
                                getJspExceptionSourceBefore().add(line);
                            }
                            break;
                        case 1:
                            getJspExceptionSourceAfter().add(line);
                        default:
                            break;
                        }
                    }
                }
            }
        }
    }

    public String getJspExceptionMessage() {
        return jspExceptionMessage;
    }

    public String getJspExceptionLine() {
        return jspExceptionLine;
    }

    private final StackTraceElement getStackTraceElementForActionMapping(HttpServletRequest request, ActionMapping mapping,
            StackTraceElement[] elements) {
        Class<?> actionClass = actionClass(mapping.getType());
        setActionErrorClass(actionClass);
        String methodName =
                DispatchAction.class.isAssignableFrom(actionClass) ? request.getParameter(mapping.getParameter()) : "execute";
        setActionErrorMethod(methodName);
        for (StackTraceElement element : elements) {
            if (element.getClassName().equals(mapping.getType()) && element.getMethodName().equals(methodName)) {
                return element;
            }
        }
        return null;
    }

    private static final Class<?> actionClass(String type) {
        try {
            return Class.forName(type);
        } catch (ClassNotFoundException e) {
            throw new Error("I can't seem to find the class I was just in");
        }
    }

    private StringBuilder headerAppend(Throwable ex) {
        StringBuilder exceptionInfo = new StringBuilder("- - - - - - - - - - - Error Origin - - - - - - - - - - -\n");
        exceptionInfo.append("\n[Exception] ").append(ex.toString()).append("\n\n");
        setException(ex);
        setThreadName(Thread.currentThread().getName());
        setFlatExceptionStack(ThrowableInfo.getFlatThrowableInfoList(ex));
        return exceptionInfo;
    }

    private SupportRequestBean userInfoContextAppend(HttpServletRequest request, final StringBuilder exceptionInfo) {

        exceptionInfo.append("[UserLoggedIn] ");

        SupportRequestBean requestBean;
        String user;
        User userView = Authenticate.getUser();
        if (userView != null) {
            user = userView.getUsername();
            requestBean = SupportRequestBean.generateExceptionBean(userView.getPerson());
            MenuFunctionality selectedFunctionality = BennuPortalDispatcher.getSelectedFunctionality(request);
            if (selectedFunctionality != null) {
                requestBean.setSelectedFunctionality(selectedFunctionality);
            }
            setUserName(user);
            Set<RoleType> roles = new HashSet<RoleType>();
            for (Role role : userView.getPerson().getPersonRolesSet()) {
                roles.add(role.getRoleType());
            }
            setUserRoles(roles);
        } else {
            user = "No user logged in, or session was lost.\n";
            requestBean = SupportRequestBean.generateExceptionBean(null);
        }
        exceptionInfo.append(user + "\n");
        return requestBean;
    }

    private void mappingContextAppend(HttpServletRequest request, final StringBuilder exceptionInfo) {
        String query = request.getQueryString();
        setRequestURI(request.getRequestURI());
        setRequestURL(request.getRequestURL().toString());
        setRequestFullUrl(getRequestFullUrl(request));
        setQueryString(query);
        setRequestMethod(request.getMethod());
        setRequestParameters(getRequestParameters(request));
        exceptionInfo.append("[RequestURI] ").append(request.getRequestURI()).append("\n");
        exceptionInfo.append("[RequestURL] ").append(request.getRequestURL()).append("\n");
        exceptionInfo.append("[QueryString] ").append(request.getQueryString()).append("\n");
        exceptionInfo.append("[Method] ").append(request.getMethod()).append('\n');

        if (request.getAttribute(PresentationConstants.ORIGINAL_MAPPING_KEY) != null) {
            ActionMapping mapping = (ActionMapping) request.getAttribute(PresentationConstants.ORIGINAL_MAPPING_KEY);
            setActionMapping(mapping);
            exceptionInfo.append("[Path] ").append(mapping.getPath()).append("\n");
            exceptionInfo.append("[Name] ").append(mapping.getName()).append("\n");
        } else {
            exceptionInfo.append("[Path|Name] impossible to get (exception through UncaughtExceptionFilter)\n");
        }
    }

    @SuppressWarnings("unchecked")
    private Map<String, String> getRequestParameters(HttpServletRequest request) {
        Map<String, String[]> parametersMap = request.getParameterMap();
        final Function<String[], String> valuesJoiner = new Function<String[], String>() {
            @Override
            public String apply(String[] parameters) {
                return Joiner.on(", ").join(parameters);
            }
        };
        return Maps.newHashMap(Maps.transformValues(parametersMap, valuesJoiner));
    }

    private static String getRequestFullUrl(HttpServletRequest request) {
        StringBuffer requestFullURL = request.getRequestURL();
        String queryString = request.getQueryString();
        return queryString == null ? requestFullURL.toString() : requestFullURL.append('?').append(queryString).toString();
    }

    private void requestContextAppend(HttpServletRequest request, StringBuilder exceptionInfo) {

        Map<String, Object> requestContext = new HashMap<String, Object>();
        Enumeration requestContents = request.getAttributeNames();
        while (requestContents.hasMoreElements()) {
            String requestElement = requestContents.nextElement().toString();
            requestContext.put(requestElement, request.getAttribute(requestElement));
            exceptionInfo.append("RequestElement:").append(requestElement).append("\n");
            exceptionInfo.append("RequestElement Value:").append(request.getAttribute(requestElement)).append("\n");
        }
        this.requestContextEntries = requestContext;
    }

    private void sessionContextAppend(HttpServletRequest request, StringBuilder exceptionInfo) {

        Map<String, Object> sessionContext = new HashMap<String, Object>();
        HttpSession session = request.getSession(false);
        if (session != null) {
            Enumeration sessionContents = session.getAttributeNames();
            while (sessionContents.hasMoreElements()) {
                String sessionElement = sessionContents.nextElement().toString();
                sessionContext.put(sessionElement, session.getAttribute(sessionElement));
                exceptionInfo.append("Element:").append(sessionElement).append("\n");
                exceptionInfo.append("Element Value:").append(session.getAttribute(sessionElement)).append("\n");
            }
        }
        this.sessionContextEntries = sessionContext;
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

    public Map<String, String> getRequestParameters() {
        return requestParameters;
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

    private void setRequestURI(String requestURI) {
        this.requestURI = requestURI;
    }

    private void setRequestURL(String requestURL) {
        this.requestURL = requestURL;
    }

    private void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    private void setRequestParameters(Map<String, String> requestParameters) {
        this.requestParameters = requestParameters;
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

    public int getActionErrorLine() {
        return actionErrorLine;
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

    public JSPLine getJspExceptionSourceLine() {
        return jspExceptionSourceLine;
    }

    public void setJspExceptionSourceLine(JSPLine jspExceptionSourceLine) {
        this.jspExceptionSourceLine = jspExceptionSourceLine;
    }

    public ArrayList<JSPLine> getJspExceptionSourceBefore() {
        return jspExceptionSourceBefore;
    }

    public void setJspExceptionSourceBefore(ArrayList<JSPLine> jspExceptionSourceBefore) {
        this.jspExceptionSourceBefore = jspExceptionSourceBefore;
    }

    public ArrayList<JSPLine> getJspExceptionSourceAfter() {
        return jspExceptionSourceAfter;
    }

    public void setJspExceptionSourceAfter(ArrayList<JSPLine> jspExceptionSourceAfter) {
        this.jspExceptionSourceAfter = jspExceptionSourceAfter;
    }
}
