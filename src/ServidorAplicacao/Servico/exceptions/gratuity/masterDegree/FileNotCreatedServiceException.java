/*
 * Created on Apr 30, 2004
 *
 */
package ServidorAplicacao.Servico.exceptions.gratuity.masterDegree;

import ServidorAplicacao.Servico.exceptions.FenixServiceException;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 */
public class FileNotCreatedServiceException extends FenixServiceException {

    /**
     *  
     */
    public FileNotCreatedServiceException() {
        super();
    }

    /**
     * @param errorType
     */
    public FileNotCreatedServiceException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public FileNotCreatedServiceException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public FileNotCreatedServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public String toString() {
        String result = "[FileNotCreatedServiceException\n";
        result += "message" + this.getMessage() + "\n";
        result += "cause" + this.getCause() + "\n";
        result += "]";
        return result;
    }
}