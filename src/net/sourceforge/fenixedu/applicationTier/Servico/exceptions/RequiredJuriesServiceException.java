package net.sourceforge.fenixedu.applicationTier.Servico.exceptions;

/**
 * 
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 */
public class RequiredJuriesServiceException extends FenixServiceException {

    public RequiredJuriesServiceException() {
    }

    public RequiredJuriesServiceException(String message) {
        super(message);
    }

    public RequiredJuriesServiceException(Throwable cause) {
        super(cause);
    }

    public RequiredJuriesServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public String toString() {
        String result = "[RequiredJuriesServiceException\n";
        result += "message" + this.getMessage() + "\n";
        result += "cause" + this.getCause() + "\n";
        result += "]";
        return result;
    }

}