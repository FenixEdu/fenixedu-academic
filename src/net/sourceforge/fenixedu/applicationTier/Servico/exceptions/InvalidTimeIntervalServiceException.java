/*
 * Created on 5/Mar/2003
 *
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.exceptions;

/**
 * @author João Mota
 */
public class InvalidTimeIntervalServiceException extends FenixServiceException {

    /**
     *  
     */
    public InvalidTimeIntervalServiceException() {
        super();

    }

    /**
     * @param s
     */
    public InvalidTimeIntervalServiceException(String s) {
        super(s);

    }

    /**
     * @param cause
     */
    public InvalidTimeIntervalServiceException(Throwable cause) {
        super(cause);

    }

    /**
     * @param message
     * @param cause
     */
    public InvalidTimeIntervalServiceException(String message, Throwable cause) {
        super(message, cause);

    }

}