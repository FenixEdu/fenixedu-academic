package net.sourceforge.fenixedu.domain.exceptions.assiduousness;

import net.sourceforge.fenixedu.domain.exceptions.FenixDomainException;

/**
* @author velouria@velouria.org
*/

public class OverlappedScheduleException extends FenixDomainException {
    
    public OverlappedScheduleException() {
        super();
    }
    
    /**
     * @param errorType
     */
    public OverlappedScheduleException(int errorType) {
        super(errorType);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param s
     */
    public OverlappedScheduleException(String s) {
        super(s);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param cause
     */
    public OverlappedScheduleException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param message
     * @param cause
     */
    public OverlappedScheduleException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

}
