package net.sourceforge.fenixedu.domain.exceptions.assiduousness;

import net.sourceforge.fenixedu.domain.exceptions.FenixDomainException;

public class NormalWorkPeriodExceedsLegalDayDurationException extends FenixDomainException {
    
    public NormalWorkPeriodExceedsLegalDayDurationException() {
        super();
    }
    
    /**
     * @param errorType
     */
    public NormalWorkPeriodExceedsLegalDayDurationException(int errorType) {
        super(errorType);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param s
     */
    public NormalWorkPeriodExceedsLegalDayDurationException(String s) {
        super(s);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param cause
     */
    public NormalWorkPeriodExceedsLegalDayDurationException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param message
     * @param cause
     */
    public NormalWorkPeriodExceedsLegalDayDurationException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }


}
