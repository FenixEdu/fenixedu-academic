/*
 * ExistingPersistentException.java
 *
 * Febuary 28th, 2003, Sometime in the morning
 */
 
package ServidorAplicacao.Servico.sop.exceptions;

/**
 *
 * @author  Luis Cruz & Nuno Nunes
 */
import ServidorAplicacao.FenixServiceException;

public class ExistingServiceException extends FenixServiceException {

    public ExistingServiceException() {
    }
    
	public ExistingServiceException(Throwable cause) {
		super(cause);
	}

	public ExistingServiceException(String message, Throwable cause) {
		super(message, cause);
	}
    

}