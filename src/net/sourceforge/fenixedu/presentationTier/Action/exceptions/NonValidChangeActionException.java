package net.sourceforge.fenixedu.presentationTier.Action.exceptions;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class NonValidChangeActionException extends FenixActionException {

    public static String key = "error.exception.invalidGuideSituationChange";

    public NonValidChangeActionException(Throwable cause) {
        super(key, cause);
    }

    public NonValidChangeActionException(Object value, Throwable cause) {
        super(key, value, cause);
    }

    public NonValidChangeActionException(Object[] values, Throwable cause) {
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
        NonValidChangeActionException.key = key;
    }

    public String toString() {
        String result = "[NonValidChangeActionException\n";
        result += "property" + this.getProperty() + "\n";
        result += "error" + this.getError() + "\n";
        result += "cause" + this.getCause() + "\n";
        result += "]";
        return result;
    }

}