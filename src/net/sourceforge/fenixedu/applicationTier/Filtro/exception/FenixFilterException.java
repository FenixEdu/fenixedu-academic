/*
 * Created on Jan 8, 2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.exception;

import pt.utl.ist.berserk.logic.filterManager.exceptions.FilterException;

/**
 * @author <a href="mailto:lesa@mega.ist.utl.pt">Leonor Almeida </a>
 * @author <a href="mailto:shmc@mega.ist.utl.pt">Sergio Montelobo </a>
 *  
 */
abstract public class FenixFilterException extends FilterException {
    /**
     *  
     */
    public FenixFilterException() {
        super();
    }

    /**
     * @param message
     */
    public FenixFilterException(String message) {
        super(message);
    }

    /**
     * @param message
     * @param cause
     */
    public FenixFilterException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param cause
     */
    public FenixFilterException(Throwable cause) {
        super(cause);
    }

}