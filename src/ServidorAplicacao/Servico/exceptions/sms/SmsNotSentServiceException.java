/*
 * Created on 11/Jun/2004
 *  
 */
package ServidorAplicacao.Servico.exceptions.sms;

import ServidorAplicacao.Servico.exceptions.FenixServiceException;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */
public class SmsNotSentServiceException extends FenixServiceException {

    /**
     *  
     */
    public SmsNotSentServiceException() {
        super();
    }

    /**
     * @param errorType
     */
    public SmsNotSentServiceException(int errorType) {
        super(errorType);
    }

    /**
     * @param s
     */
    public SmsNotSentServiceException(String s) {
        super(s);
    }

    /**
     * @param cause
     */
    public SmsNotSentServiceException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public SmsNotSentServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public String toString() {
        String result = "[SmsNotSentServiceException\n";
        result += "message" + this.getMessage() + "\n";
        result += "cause" + this.getCause() + "\n";
        result += "]";
        return result;
    }
}