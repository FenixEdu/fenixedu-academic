/*
 * Created on 2/Jun/2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.utils.exceptions;

/**
 * 
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */
public class SmsSendUtilException extends FenixUtilException {

    /**
     *  
     */
    public SmsSendUtilException() {
        super();
    }

    /**
     * @param arg0
     */
    public SmsSendUtilException(String arg0) {
        super(arg0);
    }

    /**
     * @param errorType
     */
    public SmsSendUtilException(int errorType) {
        super(errorType);
    }

    /**
     * @param cause
     */
    public SmsSendUtilException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public SmsSendUtilException(String message, Throwable cause) {
        super(message, cause);
    }

}