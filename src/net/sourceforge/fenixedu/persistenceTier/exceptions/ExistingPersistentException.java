/*
 * ExistingPersistentException.java
 *
 * Febuary 27th, 2003, 19:18
 */

package net.sourceforge.fenixedu.persistenceTier.exceptions;

import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * 
 * @author Luis Cruz & Sara Ribeiro
 */
public class ExistingPersistentException extends ExcepcaoPersistencia {

    /**
     * Creates a new instance of <code>ExistingPersistentException</code>
     * without detail message.
     */
    public ExistingPersistentException() {
    }

    /**
     * Constructs an instance of <code>ExistingPersistentException</code> with
     * the specified detail message.
     * 
     * @param msg
     *            the detail message.
     */
    public ExistingPersistentException(String msg) {
        super(msg);
    }

    public String toString() {
        String result = "[ExistingPersistentException\n";
        result += "message" + this.getMessage() + "\n";
        result += "error" + this.getErrorKey() + "\n";
        result += "cause" + this.getCause() + "\n";
        result += "]";
        return result;
    }
}