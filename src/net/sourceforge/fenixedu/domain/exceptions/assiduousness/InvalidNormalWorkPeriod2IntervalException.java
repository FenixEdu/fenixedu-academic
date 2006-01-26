package net.sourceforge.fenixedu.domain.exceptions.assiduousness;

import net.sourceforge.fenixedu.domain.exceptions.misc.InvalidIntervalLimitsException;

public class InvalidNormalWorkPeriod2IntervalException extends InvalidIntervalLimitsException {

   public InvalidNormalWorkPeriod2IntervalException() {
        super();
    }
    
    /**
     * @param errorType
     */
    public InvalidNormalWorkPeriod2IntervalException(int errorType) {
        super(errorType);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param s
     */
    public InvalidNormalWorkPeriod2IntervalException(String s) {
        super(s);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param cause
     */
    public InvalidNormalWorkPeriod2IntervalException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param message
     * @param cause
     */
    public InvalidNormalWorkPeriod2IntervalException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

}
