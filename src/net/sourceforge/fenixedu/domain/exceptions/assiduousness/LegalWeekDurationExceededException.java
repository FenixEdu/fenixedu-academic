package net.sourceforge.fenixedu.domain.exceptions.assiduousness;

import net.sourceforge.fenixedu.domain.exceptions.FenixDomainException;

/*
 * @author velouria@velouria.org
 */

public class LegalWeekDurationExceededException extends FenixDomainException {

    public LegalWeekDurationExceededException() {
        super();
    }
    
    /**
     * @param errorType
     */
    public LegalWeekDurationExceededException(int errorType) {
        super(errorType);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param s
     */
    public LegalWeekDurationExceededException(String s) {
        super(s);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param cause
     */
    public LegalWeekDurationExceededException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param message
     * @param cause
     */
    public LegalWeekDurationExceededException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }
    
}
