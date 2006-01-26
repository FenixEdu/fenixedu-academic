package net.sourceforge.fenixedu.domain.exceptions.assiduousness;

import net.sourceforge.fenixedu.domain.exceptions.FenixDomainException;

public class FixedPeriodsExceedPlatformsDurationException extends FenixDomainException {
    
 public FixedPeriodsExceedPlatformsDurationException() {
        super();
    }
    
    /**
     * @param errorType
     */
    public FixedPeriodsExceedPlatformsDurationException(int errorType) {
        super(errorType);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param s
     */
    public FixedPeriodsExceedPlatformsDurationException(String s) {
        super(s);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param cause
     */
    public FixedPeriodsExceedPlatformsDurationException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param message
     * @param cause
     */
    public FixedPeriodsExceedPlatformsDurationException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

    
}
