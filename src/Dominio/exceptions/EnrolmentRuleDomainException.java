/*
 * Created on Feb 3, 2005
 *
 */
package Dominio.exceptions;

/**
 * @author <a href="mailto:shezad@ist.utl.pt">Shezad Anavarali </a>
 *
 */
public class EnrolmentRuleDomainException extends FenixDomainException {

    /**
     * 
     */
    public EnrolmentRuleDomainException() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @param errorType
     */
    public EnrolmentRuleDomainException(int errorType) {
        super(errorType);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param s
     */
    public EnrolmentRuleDomainException(String s) {
        super(s);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param cause
     */
    public EnrolmentRuleDomainException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param message
     * @param cause
     */
    public EnrolmentRuleDomainException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

}
