/*
 * Created on 22/Mar/2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.exceptions;


/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */
public class EnrolmentRuleServiceException extends FenixServiceException {

    /**
     *  
     */
    public EnrolmentRuleServiceException() {
        super();
    }

    /**
     * @param errorType
     */
    public EnrolmentRuleServiceException(int errorType) {
        super(errorType);
    }

    /**
     * @param s
     */
    public EnrolmentRuleServiceException(String s) {
        super(s);
    }
    
    public EnrolmentRuleServiceException(String s, String[] args) {
        super(s, args);
    }

    /**
     * @param cause
     */
    public EnrolmentRuleServiceException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public EnrolmentRuleServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public String toString() {
        String result = "[EnrolmentRuleServiceException\n";
        result += "message" + this.getMessage() + "\n";
        result += "cause" + this.getCause() + "\n";
        result += "]";
        return result;
    }
}