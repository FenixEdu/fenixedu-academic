package net.sourceforge.fenixedu.presentationTier.Action.exceptions;

public class FinalResulUnreachedActionException extends FenixActionException {

    public static String key = "error.exception.unreached";

    public FinalResulUnreachedActionException(String value) {
        super(key, value);
    }

    public FinalResulUnreachedActionException(Throwable cause) {

        super(key, cause);
    }

    public FinalResulUnreachedActionException(Object value, Throwable cause) {
        super(key, value, cause);
    }

    public FinalResulUnreachedActionException(String key, Object value, Throwable cause) {
        super(key, value, cause);
    }

    public FinalResulUnreachedActionException(Object[] values, Throwable cause) {
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
        FinalResulUnreachedActionException.key = key;
    }

    public String toString() {
        String result = "[error.exception.unreached\n";
        result += "property" + this.getProperty() + "\n";
        result += "error" + this.getError() + "\n";
        result += "cause" + this.getCause() + "\n";
        result += "]";
        return result;
    }
    //TODO find a way of internationalizing the message passed as argument to
    // the exception error message of the resource bundle
}