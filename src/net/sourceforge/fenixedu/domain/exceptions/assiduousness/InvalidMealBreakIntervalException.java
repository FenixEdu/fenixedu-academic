package net.sourceforge.fenixedu.domain.exceptions.assiduousness;

import net.sourceforge.fenixedu.domain.exceptions.misc.InvalidIntervalLimitsException;

public class InvalidMealBreakIntervalException extends InvalidIntervalLimitsException {

   public InvalidMealBreakIntervalException() {
        super();
    }
    
    /**
     * @param errorType
     */
    public InvalidMealBreakIntervalException(int errorType) {
        super(errorType);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param s
     */
    public InvalidMealBreakIntervalException(String s) {
        super(s);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param cause
     */
    public InvalidMealBreakIntervalException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param message
     * @param cause
     */
    public InvalidMealBreakIntervalException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

}
