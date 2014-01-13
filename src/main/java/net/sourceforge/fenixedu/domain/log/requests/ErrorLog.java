package net.sourceforge.fenixedu.domain.log.requests;

import org.fenixedu.bennu.core.domain.Bennu;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class ErrorLog extends ErrorLog_Base {

    public ErrorLog() {
        super();
        setRootDomainObject(Bennu.getInstance());
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

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.support.SupportException> getSupportExceptions() {
        return getSupportExceptionsSet();
    }

    @Deprecated
    public boolean hasAnySupportExceptions() {
        return !getSupportExceptionsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.log.requests.RequestLog> getRequestLogs() {
        return getRequestLogsSet();
    }

    @Deprecated
    public boolean hasAnyRequestLogs() {
        return !getRequestLogsSet().isEmpty();
    }

    @Deprecated
    public boolean hasException() {
        return getException() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasStackTrace() {
        return getStackTrace() != null;
    }

}
