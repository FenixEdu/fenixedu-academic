/*
 * Created on Oct 21, 2003
 *
 */
package ServidorApresentacao.Action.exceptions;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionForward;

/**
 * @author : - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 *  
 */
public class ScholarshipNotFinishedActionException extends FenixActionException {
    public static String key = "error.exception.masterDegree.scholarshipNotFinished";

    public ScholarshipNotFinishedActionException(String value) {
        super(key, value);
    }

    public ScholarshipNotFinishedActionException(String key, String value) {
        super(key, value);
    }

    public ScholarshipNotFinishedActionException(Throwable cause) {
        super(key, cause);
    }

    public ScholarshipNotFinishedActionException(Object value, Throwable cause) {
        super(key, value, cause);
    }

    public ScholarshipNotFinishedActionException(String key, Object value, Throwable cause) {
        super(key, value, cause);
    }

    public ScholarshipNotFinishedActionException(Object[] values, Throwable cause) {
        super(key, values, cause);
    }

    public ScholarshipNotFinishedActionException(String key, ActionForward actionForward) {
        super(actionForward);
        error = new ActionError(key);
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
        String result = "[" + getClass().getName() + "\n";
        result += "property" + this.getProperty() + "\n";
        result += "error" + this.getError() + "\n";
        result += "cause" + this.getCause() + "\n";
        result += "]";
        return result;
    }

}