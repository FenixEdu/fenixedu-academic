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
public class InsuranceNotDefinedServiceException extends FenixServiceException {

    /**
     *  
     */
    public InsuranceNotDefinedServiceException() {
        super();
    }

    /**
     * @param errorType
     */
    public InsuranceNotDefinedServiceException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public InsuranceNotDefinedServiceException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public InsuranceNotDefinedServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public String toString() {
        String result = "[InsuranceNotDefinedServiceException\n";
        result += "message" + this.getMessage() + "\n";
        result += "cause" + this.getCause() + "\n";
        result += "]";
        return result;
    }
}