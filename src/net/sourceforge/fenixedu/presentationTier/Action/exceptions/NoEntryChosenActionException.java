/*
 * Created on 21/Abr/2004
 *  
 */
package net.sourceforge.fenixedu.presentationTier.Action.exceptions;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionForward;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */
public class NoEntryChosenActionException extends FenixActionException {

    public static String key = "error.exception.masterDegree.noEntryChosen";

    /**
     * @param actionForward
     */
    public NoEntryChosenActionException(ActionForward actionForward) {
        super(actionForward);
        error = new ActionError(key);
    }

    /**
     * @param key
     * @param actionForward
     */
    public NoEntryChosenActionException(String key, ActionForward actionForward) {
        super(actionForward);
        error = new ActionError(key);
    }

    /**
     *  
     */
    public NoEntryChosenActionException() {
        super(key);
    }

    /**
     * @param key
     */
    public NoEntryChosenActionException(String key) {
        super(key);
    }

    /**
     * @param key
     * @param value
     */
    public NoEntryChosenActionException(String key, Object value) {
        super(key, value);
    }

    /**
     * @param key
     * @param value0
     * @param value1
     */
    public NoEntryChosenActionException(String key, Object value0, Object value1) {
        super(key, value0, value1);
    }

    /**
     * @param key
     * @param value0
     * @param value1
     * @param value2
     */
    public NoEntryChosenActionException(String key, Object value0, Object value1, Object value2) {
        super(key, value0, value1, value2);
    }

    /**
     * @param key
     * @param value0
     * @param value1
     * @param value2
     * @param value3
     */
    public NoEntryChosenActionException(String key, Object value0, Object value1, Object value2,
            Object value3) {
        super(key, value0, value1, value2, value3);
    }

    /**
     * @param key
     * @param values
     */
    public NoEntryChosenActionException(String key, Object[] values) {
        super(key, values);
    }

    /**
     * @param key
     * @param cause
     */
    public NoEntryChosenActionException(String key, Throwable cause) {
        super(key, cause);
    }

    /**
     * @param key
     * @param value
     * @param cause
     */
    public NoEntryChosenActionException(String key, Object value, Throwable cause) {
        super(key, value, cause);
    }

    /**
     * @param key
     * @param value0
     * @param value1
     * @param cause
     */
    public NoEntryChosenActionException(String key, Object value0, Object value1, Throwable cause) {
        super(key, value0, value1, cause);
    }

    /**
     * @param key
     * @param value0
     * @param value1
     * @param value2
     * @param cause
     */
    public NoEntryChosenActionException(String key, Object value0, Object value1, Object value2,
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
    public NoEntryChosenActionException(String key, Object value0, Object value1, Object value2,
            Object value3, Throwable cause) {
        super(key, value0, value1, value2, value3, cause);
    }

    /**
     * @param key
     * @param values
     * @param cause
     */
    public NoEntryChosenActionException(String key, Object[] values, Throwable cause) {
        super(key, values, cause);
    }

    /**
     * @param cause
     */
    public NoEntryChosenActionException(Throwable cause) {
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
        NoEntryChosenActionException.key = key;
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