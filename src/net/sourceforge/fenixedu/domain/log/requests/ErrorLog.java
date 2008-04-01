package net.sourceforge.fenixedu.domain.log.requests;

import net.sourceforge.fenixedu.domain.RootDomainObject;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class ErrorLog extends ErrorLog_Base {

    public ErrorLog() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public ErrorLog(String stackTrace, String exceptionType) {
	this();
	setStackTrace(stackTrace);
	setException(ExceptionType.createOrRetrieveExceptionType(exceptionType));
    }

    protected static ErrorLog retrieveOrCreateErrorLog(String stackTrace, String exceptionType, RequestLog requestLog) {
	for (RequestLog existingRequestLog : requestLog.getMapping().getLogs()) {
	    if (existingRequestLog.hasErrorLog()
		    && existingRequestLog.getErrorLog().getException().getType().equals(exceptionType)) {
		if (existingRequestLog.getErrorLog().getStackTrace().equals(stackTrace)) {
		    return existingRequestLog.getErrorLog();
		}
	    }
	}
	return new ErrorLog(stackTrace, exceptionType);
    }

}
