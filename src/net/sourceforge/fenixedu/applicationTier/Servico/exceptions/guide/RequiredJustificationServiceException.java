/*
 * Created on 22/Mar/2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.exceptions.guide;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */
public class RequiredJustificationServiceException extends FenixServiceException {

    /**
     *  
     */
    public RequiredJustificationServiceException() {
        super();
    }

    /**
     * @param errorType
     */
    public RequiredJustificationServiceException(int errorType) {
        super(errorType);
    }

    /**
     * @param s
     */
    public RequiredJustificationServiceException(String s) {
        super(s);
    }

    /**
     * @param cause
     */
    public RequiredJustificationServiceException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public RequiredJustificationServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public String toString() {
        String result = "[RequiredJustificationServiceException\n";
        result += "message" + this.getMessage() + "\n";
        result += "cause" + this.getCause() + "\n";
        result += "]";
        return result;
    }
}