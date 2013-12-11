package net.sourceforge.fenixedu.domain.log.requests;

import pt.ist.bennu.core.domain.Bennu;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class ExceptionType extends ExceptionType_Base {

    public ExceptionType() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    private ExceptionType(String type) {
        this();
        setType(type);
    }

    public static ExceptionType createOrRetrieveExceptionType(String type) {
        for (ExceptionType exceptionType : Bennu.getInstance().getExceptionTypesSet()) {
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
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasType() {
        return getType() != null;
    }

}
