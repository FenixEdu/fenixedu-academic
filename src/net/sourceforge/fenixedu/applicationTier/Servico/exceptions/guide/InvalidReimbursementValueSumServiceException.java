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
 *         guide associated with a payment guide and the sum of the new value
 *         with all the other reimbursement guides's values of the payment guide
 *         exceeds the payment guide total.
 */
public class InvalidReimbursementValueSumServiceException extends InvalidArgumentsServiceException {

    /**
     *  
     */
    public InvalidReimbursementValueSumServiceException() {

    }

    /**
     * @param s
     */
    public InvalidReimbursementValueSumServiceException(String s) {
        super(s);

    }

    /**
     * @param cause
     */
    public InvalidReimbursementValueSumServiceException(Throwable cause) {
        super(cause);

    }

    /**
     * @param message
     * @param cause
     */
    public InvalidReimbursementValueSumServiceException(String message, Throwable cause) {
        super(message, cause);

    }

    public String toString() {
        String result = "[InvalidReimbursementValueSumServiceException\n";
        result += "message" + this.getMessage() + "\n";
        result += "cause" + this.getCause() + "\n";
        result += "]";
        return result;
    }
}