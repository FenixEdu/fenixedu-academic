package net.sourceforge.fenixedu.presentationTier.Action.exceptions;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 *  
 */
public class InvalidInformationInFormActionException extends FenixActionException {

    public static String key = "error.exception.invalidInformationInForm";

    public InvalidInformationInFormActionException(String key) {
        super(key);
    }

    public InvalidInformationInFormActionException(Throwable cause) {
        super(key, cause);
    }

    public InvalidInformationInFormActionException(Object value, Throwable cause) {
        super(key, value, cause);
    }

    public InvalidInformationInFormActionException(Object[] values, Throwable cause) {
        super(key, values, cause);
    }

    public static String getKey() {
        return key;
    }

    public static void setKey(String key) {
        InvalidInformationInFormActionException.key = key;
    }

}