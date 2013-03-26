/*
 * InterceptingServiceException.java
 *
 * March 2nd, 2003, Sometime in the afternoon
 */

package net.sourceforge.fenixedu.applicationTier.Servico.exceptions;

/**
 * 
 * @author Luis Cruz & Sara Ribeiro
 */

public class InterceptingRoomsServiceException extends FenixServiceException {

    public InterceptingRoomsServiceException() {
    }

    public InterceptingRoomsServiceException(Throwable cause) {
        super(cause);
    }

    public InterceptingRoomsServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public InterceptingRoomsServiceException(String message) {
        super(message);
    }

}