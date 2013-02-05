/*
 * InterceptingActionException.java
 *
 * March 2nd, 2003, 17h38
 */

package net.sourceforge.fenixedu.presentationTier.Action.exceptions;

/**
 * 
 * @author Luis Cruz & Sara Ribeiro
 */

public class InvalidChangeActionException extends FenixActionException {

    public static String key = "error.exception.invalid.guideSituationChange";

    public InvalidChangeActionException(Throwable cause) {
        super(key, cause);
    }

    public InvalidChangeActionException(Object value, Throwable cause) {
        super(key, value, cause);
    }

    public InvalidChangeActionException(String key, Throwable cause) {
        super(key, cause);
    }

    public InvalidChangeActionException(Object[] values, Throwable cause) {
        super(key, values, cause);
    }

    public static String getKey() {
        return key;
    }

    public static void setKey(String key) {
        InvalidChangeActionException.key = key;
    }

}