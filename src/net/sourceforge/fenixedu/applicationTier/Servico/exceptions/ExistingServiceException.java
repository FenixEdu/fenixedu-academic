/*
 * ExistingPersistentException.java
 *
 * Febuary 28th, 2003, Sometime in the morning
 */

package net.sourceforge.fenixedu.applicationTier.Servico.exceptions;

/**
 * 
 * @author Luis Cruz & Nuno Nunes
 */

public class ExistingServiceException extends FenixServiceException {

    public ExistingServiceException() {
    }

    public ExistingServiceException(String message) {
        super(message);
    }

    public ExistingServiceException(Throwable cause) {
        super(cause);
    }

    public ExistingServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public String toString() {
        String result = "[ExistingServiceException\n";
        result += "message" + this.getMessage() + "\n";
        result += "cause" + this.getCause() + "\n";
        result += "]";
        return result;
    }

}