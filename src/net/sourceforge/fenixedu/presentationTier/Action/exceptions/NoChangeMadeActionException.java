package net.sourceforge.fenixedu.presentationTier.Action.exceptions;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 *  
 */
public class NoChangeMadeActionException extends FenixActionException {

    public static String key = "error.exception.noChangeMade";

    public NoChangeMadeActionException(Throwable cause) {
        super(key, cause);
    }

    public NoChangeMadeActionException(Object value, Throwable cause) {
        super(key, value, cause);
    }

    public NoChangeMadeActionException(Object[] values, Throwable cause) {
        super(key, values, cause);
    }

    public NoChangeMadeActionException(String key) {
        super(key);
    }

    public static String getKey() {
        return key;
    }

    public static void setKey(String key) {
        NoChangeMadeActionException.key = key;
    }

}