/*
 * InterceptingServiceException.java
 *
 * March 2nd, 2003, Sometime in the afternoon
 */
 
package ServidorAplicacao.Servico.sop.exceptions;

/**
 *
 * @author  Luis Cruz & Sara Ribeiro
 */
import ServidorAplicacao.FenixServiceException;

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