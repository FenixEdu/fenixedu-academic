/*
 * ExistingActionException.java
 *
 * Febuary 28th, 2003, Sometime in the morning
 */

package ServidorApresentacao.Action.exceptions;

/**
 * 
 * @author Luis Cruz & Sara Ribeiro
 */

public class InterceptingRoomsActionException extends FenixActionException {

    public static String key = "error.exception.intercepting.rooms";

    public InterceptingRoomsActionException(Throwable cause) {
        super(key, cause);
    }

    public InterceptingRoomsActionException(Object value, Throwable cause) {
        super(key, value, cause);
    }

    public InterceptingRoomsActionException(Object[] values, Throwable cause) {
        super(key, values, cause);
    }

    /**
     * @return String
     */
    public static String getKey() {
        return key;
    }

    /**
     * Sets the key.
     * 
     * @param key
     *            The key to set
     */
    public static void setKey(String key) {
        InterceptingRoomsActionException.key = key;
    }

    //TODO find a way of internationalizing the message passed as argument to
    // the exception error message of the resource bundle
}