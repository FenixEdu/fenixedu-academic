/*
 * Created on Oct 20, 2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.exceptions;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 */
public class ScholarshipNotFinishedServiceException extends FenixServiceException {

    /**
     *  
     */
    public ScholarshipNotFinishedServiceException() {
        super();
    }

    /**
     * @param errorType
     */
    public ScholarshipNotFinishedServiceException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public ScholarshipNotFinishedServiceException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public ScholarshipNotFinishedServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public String toString() {
        String result = "[ScholarshipNotFinishedServiceException\n";
        result += "message" + this.getMessage() + "\n";
        result += "cause" + this.getCause() + "\n";
        result += "]";
        return result;
    }
}