/*
 * Created on 22/Mar/2004
 *  
 */
package ServidorAplicacao.Servico.exceptions.gratuity.masterDegree;

import ServidorAplicacao.Servico.exceptions.FenixServiceException;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */
public class GratuityValuesNotDefinedServiceException extends FenixServiceException {

    /**
     *  
     */
    public GratuityValuesNotDefinedServiceException() {
        super();
    }

    /**
     * @param errorType
     */
    public GratuityValuesNotDefinedServiceException(int errorType) {
        super(errorType);
    }

    /**
     * @param s
     */
    public GratuityValuesNotDefinedServiceException(String s) {
        super(s);
    }

    /**
     * @param cause
     */
    public GratuityValuesNotDefinedServiceException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public GratuityValuesNotDefinedServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public String toString() {
        String result = "[GratuityValuesNotDefinedServiceException\n";
        result += "message" + this.getMessage() + "\n";
        result += "cause" + this.getCause() + "\n";
        result += "]";
        return result;
    }
}