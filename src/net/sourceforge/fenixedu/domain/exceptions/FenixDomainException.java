/*
 * Created on Feb 3, 2005
 *
 */
package net.sourceforge.fenixedu.domain.exceptions;

/**
 * @author <a href="mailto:shezad@ist.utl.pt">Shezad Anavarali </a>
 * 
 */
public class FenixDomainException extends Exception {
    private int errorType;

    /**
     * @return
     */
    public int getErrorType() {
        return this.errorType;
    }

    public FenixDomainException() {
    }

    public FenixDomainException(int errorType) {
        this.errorType = errorType;
    }

    public FenixDomainException(String s) {
        super(s);
    }

    public FenixDomainException(Throwable cause) {
        super(cause);
    }

    public FenixDomainException(String message, Throwable cause) {
        super(message, cause);
    }

    public String toString() {
        String result = "[" + this.getClass().getName() + "\n";
        result += "message " + this.getMessage() + "\n";
        result += "cause " + this.getCause() + "\n";
        result += "]";
        return result;
    }

}
