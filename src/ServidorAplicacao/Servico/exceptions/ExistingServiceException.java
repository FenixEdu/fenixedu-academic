/*
 * ExistingPersistentException.java
 *
 * Febuary 28th, 2003, Sometime in the morning
 */
 
package ServidorAplicacao.Servico.exceptions;

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
	public String toString() {
				String result = "[ExistingServiceException\n";
				result += "message" +this.getMessage()+ "\n";
				result += "cause" +this.getCause()+ "\n";
				result += "]";
				return result;
			}


}