/*
 * Created on 16/Mai/2003
 */
package net.sourceforge.fenixedu.persistenceTier.exceptions;

import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author lmac1
 */
public class CantDeletePersistentException extends ExcepcaoPersistencia {

    /**
     * Creates a new instance of <code>ExistingPersistentException</code>
     * without detail message.
     */
    public CantDeletePersistentException() {
    }

    /**
     * Constructs an instance of <code>ExistingPersistentException</code> with
     * the specified detail message.
     * 
     * @param msg
     *            the detail message.
     */
    public CantDeletePersistentException(String msg) {
        super(msg);
    }

    public String toString() {
        String result = "[CantDeletePersistentException\n";
        result += "message" + this.getMessage() + "\n";
        result += "error" + this.getErrorKey() + "\n";
        result += "cause" + this.getCause() + "\n";
        result += "]";
        return result;
    }
}

