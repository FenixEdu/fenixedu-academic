package net.sourceforge.fenixedu.applicationTier.Servico.exceptions;

/**
 * @author David Santos Jan 29, 2004
 */

public class NotAuthorizedBranchChangeException extends FenixServiceException {

    public NotAuthorizedBranchChangeException() {
    }

    public NotAuthorizedBranchChangeException(String message) {
        super(message);
    }

    public NotAuthorizedBranchChangeException(Throwable cause) {
        super(cause);
    }

    public NotAuthorizedBranchChangeException(String message, Throwable cause) {
        super(message, cause);
    }

    public String toString() {
        String result = "[NotAuthorizedBranchChangeException\n";
        result += "message" + this.getMessage() + "\n";
        result += "cause" + this.getCause() + "\n";
        result += "]";
        return result;
    }

}