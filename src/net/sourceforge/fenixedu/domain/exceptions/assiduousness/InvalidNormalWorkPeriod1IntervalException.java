package net.sourceforge.fenixedu.domain.exceptions.assiduousness;

import net.sourceforge.fenixedu.domain.exceptions.misc.InvalidIntervalLimitsException;

public class InvalidNormalWorkPeriod1IntervalException extends InvalidIntervalLimitsException {

   public InvalidNormalWorkPeriod1IntervalException() {
        super();
    }
    
    /**
     * @param errorType
     */
    public InvalidNormalWorkPeriod1IntervalException(int errorType) {
        super(errorType);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param s
     */
    public InvalidNormalWorkPeriod1IntervalException(String s) {
        super(s);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param cause
     */
    public InvalidNormalWorkPeriod1IntervalException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param message
     * @param cause
     */
    public InvalidNormalWorkPeriod1IntervalException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

}
