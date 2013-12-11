package net.sourceforge.fenixedu.domain.log.requests;

import pt.ist.bennu.core.domain.Bennu;

import org.joda.time.DateTime;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class RequestLog extends RequestLog_Base {

    public RequestLog() {
        super();
        setRootDomainObject(Bennu.getInstance());
        setRequestTime(new DateTime());
    }

    public RequestLog(String queryString, String referer, String user, String requestAttributes, String sessionAttributes,
            String path, Boolean post, String... parameters) {
        this();
        setQueryString(queryString);
        setRequester(user);
        setRequestAttributes(requestAttributes);
        setSessionAttributes(sessionAttributes);
        setReferer(referer);
        setMapping(RequestMapping.createOrRetrieveRequestMapping(path, parameters));
        setPost(post);
    }

    public static RequestLog registerError(String path, String referer, String[] parameters, String queryString, String user,
            String requestAttributes, String sessionAttributes, String stackTrace, String exceptionType, Boolean post) {

        RequestLog requestLog =
                new RequestLog(queryString, referer, user, requestAttributes, sessionAttributes, path, post, parameters);
        RequestLogDay.getToday().addLogs(requestLog);
        ErrorLog errorLog = ErrorLog.retrieveOrCreateErrorLog(stackTrace, exceptionType, requestLog);
        requestLog.setErrorLog(errorLog);

        return requestLog;
    }

    @Deprecated
    public boolean hasRequestAttributes() {
        return getRequestAttributes() != null;
    }

    @Deprecated
    public boolean hasErrorLog() {
        return getErrorLog() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasRequester() {
        return getRequester() != null;
    }

    @Deprecated
    public boolean hasPost() {
        return getPost() != null;
    }

    @Deprecated
    public boolean hasReferer() {
        return getReferer() != null;
    }

    @Deprecated
    public boolean hasMapping() {
        return getMapping() != null;
    }

    @Deprecated
    public boolean hasDay() {
        return getDay() != null;
    }

    @Deprecated
    public boolean hasQueryString() {
        return getQueryString() != null;
    }

    @Deprecated
    public boolean hasSessionAttributes() {
        return getSessionAttributes() != null;
    }

    @Deprecated
    public boolean hasRequestTime() {
        return getRequestTime() != null;
    }

}
