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
 *         guide associated with a payment guide and value of the reimbursement
 *         guide exceeds the payment guide total.
 */
public class InvalidReimbursementValueServiceException extends InvalidArgumentsServiceException {

    /**
     *  
     */
    public InvalidReimbursementValueServiceException() {

    }

    /**
     * @param s
     */
    public InvalidReimbursementValueServiceException(String s) {
        super(s);

    }

    /**
     * @param cause
     */
    public InvalidReimbursementValueServiceException(Throwable cause) {
        super(cause);

    }

    /**
     * @param message
     * @param cause
     */
    public InvalidReimbursementValueServiceException(String message, Throwable cause) {
        super(message, cause);

    }

    public String toString() {
        String result = "[InvalidReimbursementValueServiceException\n";
        result += "message" + this.getMessage() + "\n";
        result += "cause" + this.getCause() + "\n";
        result += "]";
        return result;
    }
}