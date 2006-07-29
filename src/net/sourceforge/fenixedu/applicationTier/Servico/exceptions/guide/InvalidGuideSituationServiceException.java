/*
 * Created on 17/Nov/2003
 *
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.exceptions.guide;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;

/**
 * @author <a href="mailto:joao.mota@ist.utl.pt">João Mota </a> <br/>
 *         <strong>Description: </strong> <br/>
 *         This exception is throw when one tries to create a reimbursement
 *         guide associated with a payment guide with an active situation that
 *         is not PAYED, or when one tries to change the reimbursement guide
 *         ssituation state to an invalid one. See EditReimbursementGuide
 *         service for details.
 *  
 */
public class InvalidGuideSituationServiceException extends InvalidArgumentsServiceException {

    /**
     *  
     */
    public InvalidGuideSituationServiceException() {

    }

    /**
     * @param s
     */
    public InvalidGuideSituationServiceException(String s) {
        super(s);

    }

    /**
     * @param cause
     */
    public InvalidGuideSituationServiceException(Throwable cause) {
        super(cause);

    }

    /**
     * @param message
     * @param cause
     */
    public InvalidGuideSituationServiceException(String message, Throwable cause) {
        super(message, cause);

    }

    public String toString() {
        String result = "[InvalidGuideSituationServiceException\n";
        result += "message" + this.getMessage() + "\n";
        result += "cause" + this.getCause() + "\n";
        result += "]";
        return result;
    }
}