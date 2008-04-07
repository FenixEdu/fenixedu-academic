package net.sourceforge.fenixedu.domain.log.requests;

import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.joda.time.DateTime;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class RequestLog extends RequestLog_Base {

    public RequestLog() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setRequestTime(new DateTime());
    }

    public RequestLog(String queryString, String referer, String user, String requestAttributes, String sessionAttributes, String path,
	    String... parameters) {
	this();
	setQueryString(queryString);
	setRequester(user);
	setRequestAttributes(requestAttributes);
	setSessionAttributes(sessionAttributes);
	setMapping(RequestMapping.createOrRetrieveRequestMapping(path, referer, parameters));
    }

    public static RequestLog registerError(String path, String referer, String[] parameters, String queryString, String user,
	    String requestAttributes, String sessionAttributes, String stackTrace, String exceptionType) {

	RequestLog requestLog = new RequestLog(queryString, referer, user, requestAttributes, sessionAttributes, path, parameters);
	ErrorLog errorLog = ErrorLog.retrieveOrCreateErrorLog(stackTrace, exceptionType, requestLog);
	requestLog.setErrorLog(errorLog);

	return requestLog;
    }

}
