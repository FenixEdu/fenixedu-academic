/*
 * Created on 3/Nov/2003
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
public class RequiredJuriesActionException extends FenixActionException {
    public static String key = "error.exception.masterDegree.noJuriesSelected";

    public RequiredJuriesActionException(Throwable cause) {
        super(key, cause);
    }

    public RequiredJuriesActionException(Object value, Throwable cause) {
        super(key, value, cause);
    }

    public RequiredJuriesActionException(Object[] values, Throwable cause) {
        super(key, values, cause);
    }

    public RequiredJuriesActionException(String text) {
        super(text);
    }

    public RequiredJuriesActionException(String text, ActionForward actionForward) {
        super(actionForward);
        error = new ActionError(text);
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
        RequiredJuriesActionException.key = key;
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