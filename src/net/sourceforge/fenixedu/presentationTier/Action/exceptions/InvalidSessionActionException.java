/*
 * Created on 5/Mar/2003
 *
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.exceptions;

/**
 * @author João Mota
 */
public class InvalidSessionActionException extends FenixActionException {
    public static String key = "error.invalid.session";

    /**
     *  
     */
    public InvalidSessionActionException() {
        super();

    }

    public InvalidSessionActionException(Throwable cause) {
        super(key, cause);
    }

    /**
     * @param key
     */
    public InvalidSessionActionException(String key) {
        super(key);

    }

    /**
     * @param key
     * @param value
     */
    public InvalidSessionActionException(String key, Object value) {
        super(key, value);

    }

    /**
     * @param key
     * @param value0
     * @param value1
     */
    public InvalidSessionActionException(String key, Object value0, Object value1) {
        super(key, value0, value1);

    }

    /**
     * @param key
     * @param value0
     * @param value1
     * @param value2
     */
    public InvalidSessionActionException(String key, Object value0, Object value1, Object value2) {
        super(key, value0, value1, value2);

    }

    /**
     * @param key
     * @param value0
     * @param value1
     * @param value2
     * @param value3
     */
    public InvalidSessionActionException(String key, Object value0, Object value1, Object value2,
            Object value3) {
        super(key, value0, value1, value2, value3);

    }

    /**
     * @param key
     * @param values
     */
    public InvalidSessionActionException(String key, Object[] values) {
        super(key, values);

    }

    /**
     * @param key
     * @param cause
     */
    public InvalidSessionActionException(String key, Throwable cause) {
        super(key, cause);

    }

    /**
     * @param key
     * @param value
     * @param cause
     */
    public InvalidSessionActionException(String key, Object value, Throwable cause) {
        super(key, value, cause);

    }

    /**
     * @param key
     * @param value0
     * @param value1
     * @param cause
     */
    public InvalidSessionActionException(String key, Object value0, Object value1, Throwable cause) {
        super(key, value0, value1, cause);

    }

    /**
     * @param key
     * @param value0
     * @param value1
     * @param value2
     * @param cause
     */
    public InvalidSessionActionException(String key, Object value0, Object value1, Object value2,
            Throwable cause) {
        super(key, value0, value1, value2, cause);

    }

    /**
     * @param key
     * @param value0
     * @param value1
     * @param value2
     * @param value3
     * @param cause
     */
    public InvalidSessionActionException(String key, Object value0, Object value1, Object value2,
            Object value3, Throwable cause) {
        super(key, value0, value1, value2, value3, cause);

    }

    /**
     * @param key
     * @param values
     * @param cause
     */
    public InvalidSessionActionException(String key, Object[] values, Throwable cause) {
        super(key, values, cause);

    }

    public String toString() {
        String result = "[InvalidSessionActionException\n";
        result += "property" + this.getProperty() + "\n";
        result += "error" + this.getError() + "\n";
        result += "cause" + this.getCause() + "\n";
        result += "]";
        return result;
    }
}