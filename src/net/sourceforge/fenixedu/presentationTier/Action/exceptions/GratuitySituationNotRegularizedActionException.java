package net.sourceforge.fenixedu.presentationTier.Action.exceptions;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionForward;

/**
 * 
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */
public class GratuitySituationNotRegularizedActionException extends FenixActionException {
    public static String key = "error.exception.masterDegree.gratuityNotRegularized";

    public GratuitySituationNotRegularizedActionException(String value) {
        super(key, value);
    }

    public GratuitySituationNotRegularizedActionException(String key, String value) {
        super(key, value);
    }

    public GratuitySituationNotRegularizedActionException(Throwable cause) {
        super(key, cause);
    }

    public GratuitySituationNotRegularizedActionException(Object value, Throwable cause) {
        super(key, value, cause);
    }

    public GratuitySituationNotRegularizedActionException(String key, Object value, Throwable cause) {
        super(key, value, cause);
    }

    public GratuitySituationNotRegularizedActionException(Object[] values, Throwable cause) {
        super(key, values, cause);
    }

    public GratuitySituationNotRegularizedActionException(String key, ActionForward actionForward) {
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