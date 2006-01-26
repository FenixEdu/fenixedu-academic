package net.sourceforge.fenixedu.domain.exceptions.assiduousness;

import net.sourceforge.fenixedu.domain.exceptions.FenixDomainException;

public class NormalWorkPeriod1EndsAfterWorkDayException extends FenixDomainException {
    
    public NormalWorkPeriod1EndsAfterWorkDayException() {
        super();
    }
    
    /**
     * @param errorType
     */
    public NormalWorkPeriod1EndsAfterWorkDayException(int errorType) {
        super(errorType);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param s
     */
    public NormalWorkPeriod1EndsAfterWorkDayException(String s) {
        super(s);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param cause
     */
    public NormalWorkPeriod1EndsAfterWorkDayException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param message
     * @param cause
     */
    public NormalWorkPeriod1EndsAfterWorkDayException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }


}
