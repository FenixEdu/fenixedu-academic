package net.sourceforge.fenixedu.applicationTier.Servico.exceptions;

/**
 * 
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 */
public class GuiderAlreadyChosenServiceException extends FenixServiceException {

    public GuiderAlreadyChosenServiceException() {
    }

    public GuiderAlreadyChosenServiceException(String message) {
        super(message);
    }

    public GuiderAlreadyChosenServiceException(Throwable cause) {
        super(cause);
    }

    public GuiderAlreadyChosenServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public String toString() {
        String result = "[RequiredGuidersServiceException\n";
        result += "message" + this.getMessage() + "\n";
        result += "cause" + this.getCause() + "\n";
        result += "]";
        return result;
    }

}