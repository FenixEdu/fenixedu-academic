package net.sourceforge.fenixedu.domain.exceptions.assiduousness;

import net.sourceforge.fenixedu.domain.exceptions.FenixDomainException;

public class NormalWorkPeriodExceedsConsecutiveWorkPeriodException extends FenixDomainException {
    
    public NormalWorkPeriodExceedsConsecutiveWorkPeriodException() {
        super();
    }
    
    /**
     * @param errorType
     */
    public NormalWorkPeriodExceedsConsecutiveWorkPeriodException(int errorType) {
        super(errorType);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param s
     */
    public NormalWorkPeriodExceedsConsecutiveWorkPeriodException(String s) {
        super(s);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param cause
     */
    public NormalWorkPeriodExceedsConsecutiveWorkPeriodException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param message
     * @param cause
     */
    public NormalWorkPeriodExceedsConsecutiveWorkPeriodException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }


}
