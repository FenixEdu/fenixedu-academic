/*
 * ExistingException.java
 *
 * Febuary 27th, 2003, 19:18
 */

package ServidorPersistente.exceptions;

import ServidorPersistente.ExcepcaoPersistencia;

/**
 *
 * @author  Luis Cruz & Sara Ribeiro
 */
public class ExistingException extends ExcepcaoPersistencia {

	/**
	 * Creates a new instance of <code>ExistingException</code> without detail message.
	 */
	public ExistingException() { }

	/**
	 * Constructs an instance of <code>ExistingException</code> with the specified detail message.
	 * @param msg the detail message.
	 */
	public ExistingException(String msg) {
		super(msg);
	}

}
