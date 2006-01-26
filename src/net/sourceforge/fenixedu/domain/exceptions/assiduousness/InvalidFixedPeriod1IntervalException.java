package net.sourceforge.fenixedu.domain.exceptions.assiduousness;

import net.sourceforge.fenixedu.domain.exceptions.misc.InvalidIntervalLimitsException;

public class InvalidFixedPeriod1IntervalException extends InvalidIntervalLimitsException {

  public InvalidFixedPeriod1IntervalException() {
        super();
    }
    
    /**
     * @param errorType
     */
    public InvalidFixedPeriod1IntervalException(int errorType) {
        super(errorType);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param s
     */
    public InvalidFixedPeriod1IntervalException(String s) {
        super(s);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param cause
     */
    public InvalidFixedPeriod1IntervalException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param message
     * @param cause
     */
    public InvalidFixedPeriod1IntervalException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

    
}
