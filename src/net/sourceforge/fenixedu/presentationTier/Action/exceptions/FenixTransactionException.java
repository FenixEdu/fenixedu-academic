/*
 * InterceptingActionException.java
 *
 * March 2nd, 2003, 17h38
 */

package net.sourceforge.fenixedu.presentationTier.Action.exceptions;

/**
 * 
 * @author jpvl
 */

public class FenixTransactionException extends FenixActionException {

    public FenixTransactionException(String key) {

        super(key);
    }
}