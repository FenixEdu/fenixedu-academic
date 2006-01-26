package net.sourceforge.fenixedu.domain.exceptions.assiduousness;

import net.sourceforge.fenixedu.domain.exceptions.FenixDomainException;

public class NormalWorkPeriod2EndsAfterWorkDayException extends FenixDomainException {
    
    public NormalWorkPeriod2EndsAfterWorkDayException() {
        super();
    }
    
    /**
     * @param errorType
     */
    public NormalWorkPeriod2EndsAfterWorkDayException(int errorType) {
        super(errorType);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param s
     */
    public NormalWorkPeriod2EndsAfterWorkDayException(String s) {
        super(s);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param cause
     */
    public NormalWorkPeriod2EndsAfterWorkDayException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param message
     * @param cause
     */
    public NormalWorkPeriod2EndsAfterWorkDayException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }


}
