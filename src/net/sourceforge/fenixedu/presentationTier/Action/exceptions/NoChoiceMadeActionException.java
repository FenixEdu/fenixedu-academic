package net.sourceforge.fenixedu.presentationTier.Action.exceptions;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 *  
 */
public class NoChoiceMadeActionException extends FenixActionException {

    public static String key = "error.exception.noChoiceMade";

    public NoChoiceMadeActionException(Throwable cause) {
        super(key, cause);
    }

    public NoChoiceMadeActionException(Object value, Throwable cause) {
        super(key, value, cause);
    }

    public NoChoiceMadeActionException(Object[] values, Throwable cause) {
        super(key, values, cause);
    }

    public static String getKey() {
        return key;
    }

    public static void setKey(String key) {
        NoChoiceMadeActionException.key = key;
    }

}