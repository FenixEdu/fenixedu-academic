package net.sourceforge.fenixedu.presentationTier.Action.exceptions;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class NotAuthorizedActionException extends FenixActionException {

    public static String key = "error.exception.notAuthorized";

    public NotAuthorizedActionException(String value) {
        super(key, value);
    }

    public NotAuthorizedActionException(String key, String value) {
        super(key, value);
    }

    public NotAuthorizedActionException(Throwable cause) {

        super(key, cause);
    }

    public NotAuthorizedActionException(Object value, Throwable cause) {
        super(key, value, cause);
    }

    public NotAuthorizedActionException(String key, Object value, Throwable cause) {
        super(key, value, cause);
    }

    public NotAuthorizedActionException(Object[] values, Throwable cause) {
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
        NonExistingActionException.key = key;
    }

    public String toString() {
        String result = "[NotAuthorizedActionException\n";
        result += "property" + this.getProperty() + "\n";
        result += "error" + this.getError() + "\n";
        result += "cause" + this.getCause() + "\n";
        result += "]";
        return result;
    }
    //TODO find a way of internationalizing the message passed as argument to
    // the exception error message of the resource bundle
}