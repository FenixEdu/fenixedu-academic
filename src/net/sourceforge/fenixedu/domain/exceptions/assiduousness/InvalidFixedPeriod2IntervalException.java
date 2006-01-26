package net.sourceforge.fenixedu.domain.exceptions.assiduousness;

import net.sourceforge.fenixedu.domain.exceptions.misc.InvalidIntervalLimitsException;

public class InvalidFixedPeriod2IntervalException extends InvalidIntervalLimitsException {

  public InvalidFixedPeriod2IntervalException() {
        super();
    }
    
    /**
     * @param errorType
     */
    public InvalidFixedPeriod2IntervalException(int errorType) {
        super(errorType);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param s
     */
    public InvalidFixedPeriod2IntervalException(String s) {
        super(s);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param cause
     */
    public InvalidFixedPeriod2IntervalException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param message
     * @param cause
     */
    public InvalidFixedPeriod2IntervalException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

    
}
