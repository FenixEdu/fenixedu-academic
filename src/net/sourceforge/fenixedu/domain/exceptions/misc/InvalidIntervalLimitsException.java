package net.sourceforge.fenixedu.domain.exceptions.misc;

import net.sourceforge.fenixedu.domain.exceptions.FenixDomainException;

public class InvalidIntervalLimitsException extends FenixDomainException {

    public InvalidIntervalLimitsException() {
	super();
    }

    /**
     * @param errorType
     */
    public InvalidIntervalLimitsException(int errorType) {
	super(errorType);
	// TODO Auto-generated constructor stub
    }

    /**
     * @param s
     */
    public InvalidIntervalLimitsException(String s) {
	super(s);
	// TODO Auto-generated constructor stub
    }

    /**
     * @param cause
     */
    public InvalidIntervalLimitsException(Throwable cause) {
	super(cause);
	// TODO Auto-generated constructor stub
    }

    /**
     * @param message
     * @param cause
     */
    public InvalidIntervalLimitsException(String message, Throwable cause) {
	super(message, cause);
	// TODO Auto-generated constructor stub
    }

}
