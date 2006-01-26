package net.sourceforge.fenixedu.domain.exceptions.assiduousness;

import net.sourceforge.fenixedu.domain.exceptions.misc.InvalidIntervalLimitsException;

public class InvalidWorkdayIntervalException extends InvalidIntervalLimitsException {

   public InvalidWorkdayIntervalException() {
        super();
    }
    
    /**
     * @param errorType
     */
    public InvalidWorkdayIntervalException(int errorType) {
        super(errorType);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param s
     */
    public InvalidWorkdayIntervalException(String s) {
        super(s);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param cause
     */
    public InvalidWorkdayIntervalException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param message
     * @param cause
     */
    public InvalidWorkdayIntervalException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

    
}
