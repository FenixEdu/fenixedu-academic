/*
 * Created on 12/Mar/2003
 *
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.exceptions;

/**
 * @author João Mota
 */
public class InvalidArgumentsServiceException extends FenixServiceException {

    /**
     *  
     */
    public InvalidArgumentsServiceException() {

    }

    /**
     * @param s
     */
    public InvalidArgumentsServiceException(String s) {
        super(s);

    }

    /**
     * @param cause
     */
    public InvalidArgumentsServiceException(Throwable cause) {
        super(cause);

    }

    /**
     * @param message
     * @param cause
     */
    public InvalidArgumentsServiceException(String message, Throwable cause) {
        super(message, cause);

    }

    public String toString() {
        String result = "[InvalidArgumentsServiceException\n";
        result += "message" + this.getMessage() + "\n";
        result += "cause" + this.getCause() + "\n";
        result += "]";
        return result;
    }
}