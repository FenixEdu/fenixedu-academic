/*
 * Created on 19/Mai/2003
 */
package ServidorApresentacao.Action.exceptions;

/**
 * @author lmac1
 */
public class CantDeleteActionException extends FenixActionException {

    public static String key = "errors.invalid.delete.not.empty";

    public CantDeleteActionException(Throwable cause) {
        super(key, cause);
    }

    public CantDeleteActionException(Object value, Throwable cause) {
        super(key, value, cause);
    }

    public CantDeleteActionException(Object[] values, Throwable cause) {
        super(key, values, cause);
    }

    public CantDeleteActionException(String text) {
        super(text);
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
        ExistingActionException.key = key;
    }

    public String toString() {
        String result = "[CantDeleteActionException\n";
        result += "property" + this.getProperty() + "\n";
        result += "error" + this.getError() + "\n";
        result += "cause" + this.getCause() + "\n";
        result += "]";
        return result;
    }
    //TODO find a way of internationalizing the message passed as argument to
    // the exception error message of the resource bundle
}