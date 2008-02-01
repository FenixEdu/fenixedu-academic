/**
 * 
 */
package net.sourceforge.fenixedu.webServices.exceptions;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class NotAuthorizedException extends Exception {

    public static String key = "error.exception.notAuthorized";

    public NotAuthorizedException() {
	super(key);
    }

    public NotAuthorizedException(Throwable cause) {

	super(key, cause);
    }

    public static String getKey() {
	return key;
    }

}
