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

public class InterceptingActionException extends FenixActionException {

    public static String key = "error.exception.intercepting.lesson";

    public InterceptingActionException(Throwable cause) {
        super(key, cause);
    }

    public InterceptingActionException(Object value, Throwable cause) {
        super(key, value, cause);
    }

    public InterceptingActionException(Object[] values, Throwable cause) {
        super(key, values, cause);
    }

    public static String getKey() {
        return key;
    }

    public static void setKey(String key) {
        InterceptingActionException.key = key;
    }

}