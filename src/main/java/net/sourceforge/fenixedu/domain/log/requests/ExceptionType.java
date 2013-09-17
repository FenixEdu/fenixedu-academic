package net.sourceforge.fenixedu.domain.log.requests;

import net.sourceforge.fenixedu.domain.RootDomainObject;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class ExceptionType extends ExceptionType_Base {

    public ExceptionType() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    private ExceptionType(String type) {
        this();
        setType(type);
    }

    public static ExceptionType createOrRetrieveExceptionType(String type) {
        for (ExceptionType exceptionType : RootDomainObject.getInstance().getExceptionTypes()) {
            if (exceptionType.getType().equals(type)) {
                return exceptionType;
            }
        }
        return new ExceptionType(type);
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.log.requests.ErrorLog> getErrorLogs() {
        return getErrorLogsSet();
    }

    @Deprecated
    public boolean hasAnyErrorLogs() {
        return !getErrorLogsSet().isEmpty();
    }

    @Deprecated
    public boolean hasRootDomainObject() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasType() {
        return getType() != null;
    }

}
