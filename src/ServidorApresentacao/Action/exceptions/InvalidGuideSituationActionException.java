/*
 * Created on 21/Abr/2004
 *  
 */
package ServidorApresentacao.Action.exceptions;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionForward;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */
public class InvalidGuideSituationActionException extends FenixActionException {

    public static String key = "error.exception.masterDegree.invalidGuideSituation";

    /**
     * @param actionForward
     */
    public InvalidGuideSituationActionException(ActionForward actionForward) {
        super(actionForward);
        error = new ActionError(key);
    }

    /**
     * @param key
     * @param actionForward
     */
    public InvalidGuideSituationActionException(String key, ActionForward actionForward) {
        super(actionForward);
        error = new ActionError(key);
    }

    /**
     *  
     */
    public InvalidGuideSituationActionException() {
        super(key);
    }

    /**
     * @param key
     */
    public InvalidGuideSituationActionException(String key) {
        super(key);
    }

    /**
     * @param key
     * @param value
     */
    public InvalidGuideSituationActionException(String key, Object value) {
        super(key, value);
    }

    /**
     * @param key
     * @param value0
     * @param value1
     */
    public InvalidGuideSituationActionException(String key, Object value0, Object value1) {
        super(key, value0, value1);
    }

    /**
     * @param key
     * @param value0
     * @param value1
     * @param value2
     */
    public InvalidGuideSituationActionException(String key, Object value0, Object value1, Object value2) {
        super(key, value0, value1, value2);
    }

    /**
     * @param key
     * @param value0
     * @param value1
     * @param value2
     * @param value3
     */
    public InvalidGuideSituationActionException(String key, Object value0, Object value1, Object value2,
            Object value3) {
        super(key, value0, value1, value2, value3);
    }

    /**
     * @param key
     * @param values
     */
    public InvalidGuideSituationActionException(String key, Object[] values) {
        super(key, values);
    }

    /**
     * @param key
     * @param cause
     */
    public InvalidGuideSituationActionException(String key, Throwable cause) {
        super(key, cause);
    }

    /**
     * @param key
     * @param value
     * @param cause
     */
    public InvalidGuideSituationActionException(String key, Object value, Throwable cause) {
        super(key, value, cause);
    }

    /**
     * @param key
     * @param value0
     * @param value1
     * @param cause
     */
    public InvalidGuideSituationActionException(String key, Object value0, Object value1, Throwable cause) {
        super(key, value0, value1, cause);
    }

    /**
     * @param key
     * @param value0
     * @param value1
     * @param value2
     * @param cause
     */
    public InvalidGuideSituationActionException(String key, Object value0, Object value1, Object value2,
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
    public InvalidGuideSituationActionException(String key, Object value0, Object value1, Object value2,
            Object value3, Throwable cause) {
        super(key, value0, value1, value2, value3, cause);
    }

    /**
     * @param key
     * @param values
     * @param cause
     */
    public InvalidGuideSituationActionException(String key, Object[] values, Throwable cause) {
        super(key, values, cause);
    }

    /**
     * @param cause
     */
    public InvalidGuideSituationActionException(Throwable cause) {
        super(cause);
    }

    /**
     * @return Returns the key.
     */
    public static String getKey() {
        return key;
    }

    /**
     * @param key
     *            The key to set.
     */
    public static void setKey(String key) {
        InvalidGuideSituationActionException.key = key;
    }

    public String toString() {
        String result = "[" + getClass().getName() + "n";
        result += "property" + this.getProperty() + "\n";
        result += "error" + this.getError() + "\n";
        result += "cause" + this.getCause() + "\n";
        result += "]";
        return result;
    }

}