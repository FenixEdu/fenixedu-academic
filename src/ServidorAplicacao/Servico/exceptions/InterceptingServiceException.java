/*
 * InterceptingServiceException.java
 *
 * March 2nd, 2003, Sometime in the afternoon
 */

package ServidorAplicacao.Servico.exceptions;

/**
 * 
 * @author Luis Cruz & Sara Ribeiro
 */

public class InterceptingServiceException extends FenixServiceException {

    public InterceptingServiceException() {
    }

    public InterceptingServiceException(Throwable cause) {
        super(cause);
    }

    public InterceptingServiceException(String message, Throwable cause) {
        super(message, cause);
    }

}