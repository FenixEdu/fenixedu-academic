/*
 * Created on 22/Mar/2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.exceptions.sms;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */
public class SmsLimitReachedServiceException extends FenixServiceException {

    /**
     *  
     */
    public SmsLimitReachedServiceException() {
        super();
    }

    /**
     * @param errorType
     */
    public SmsLimitReachedServiceException(int errorType) {
        super(errorType);
    }

    /**
     * @param s
     */
    public SmsLimitReachedServiceException(String s) {
        super(s);
    }

    /**
     * @param cause
     */
    public SmsLimitReachedServiceException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public SmsLimitReachedServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public String toString() {
        String result = "[SmsLimitReachedServiceException\n";
        result += "message" + this.getMessage() + "\n";
        result += "cause" + this.getCause() + "\n";
        result += "]";
        return result;
    }
}