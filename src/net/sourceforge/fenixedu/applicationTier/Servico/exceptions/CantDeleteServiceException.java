package net.sourceforge.fenixedu.applicationTier.Servico.exceptions;

/**
 * @author lmac1
 */
public class CantDeleteServiceException extends FenixServiceException {

    public CantDeleteServiceException() {
    }

    public CantDeleteServiceException(Throwable cause) {
        super(cause);
    }

    public CantDeleteServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public String toString() {
        String result = "[CantDeleteServiceException\n";
        result += "message" + this.getMessage() + "\n";
        result += "cause" + this.getCause() + "\n";
        result += "]";
        return result;
    }

}