/*
 * Created on 5/Mar/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code Template
 */
package ServidorAplicacao.Servico.exceptions;

import ServidorAplicacao.FenixServiceException;

/**
 * @author jmota
 */
public class InvalidTimeIntervalServiceException
	extends FenixServiceException {

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
	public InvalidTimeIntervalServiceException(
		String message,
		Throwable cause) {
		super(message, cause);
		
	}

}
