package net.sourceforge.fenixedu.domain.exceptions.assiduousness;

import net.sourceforge.fenixedu.domain.exceptions.FenixDomainException;

public class NormalWorkPeriod1StartsBeforeWorkDayException extends FenixDomainException {
    
    public NormalWorkPeriod1StartsBeforeWorkDayException() {
        super();
    }
    
    /**
     * @param errorType
     */
    public NormalWorkPeriod1StartsBeforeWorkDayException(int errorType) {
        super(errorType);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param s
     */
    public NormalWorkPeriod1StartsBeforeWorkDayException(String s) {
        super(s);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param cause
     */
    public NormalWorkPeriod1StartsBeforeWorkDayException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param message
     * @param cause
     */
    public NormalWorkPeriod1StartsBeforeWorkDayException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }


}
