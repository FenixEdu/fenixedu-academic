/*
 * Created on 12/Mar/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code Template
 */
package ServidorAplicacao.Servico.exceptions;

import ServidorAplicacao.FenixServiceException;

/**
 * @author jmota
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
					result += "message" +this.getMessage()+ "\n";
					result += "cause" +this.getCause()+ "\n";
					result += "]";
					return result;
				}
}
