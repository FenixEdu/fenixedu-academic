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
public class DuplicateSibsPaymentFileProcessingServiceException extends FenixServiceException {

    /**
     *  
     */
    public DuplicateSibsPaymentFileProcessingServiceException() {
        super();
    }

    /**
     * @param errorType
     */
    public DuplicateSibsPaymentFileProcessingServiceException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public DuplicateSibsPaymentFileProcessingServiceException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public DuplicateSibsPaymentFileProcessingServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public String toString() {
        String result = "[DuplicateSibsPaymentFileProcessingException\n";
        result += "message" + this.getMessage() + "\n";
        result += "cause" + this.getCause() + "\n";
        result += "]";
        return result;
    }
}