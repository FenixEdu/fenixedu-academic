package net.sourceforge.fenixedu.domain.exceptions.assiduousness;

import net.sourceforge.fenixedu.domain.exceptions.FenixDomainException;

public class WorkdayOutOfLegalBoundsException extends FenixDomainException {

    public WorkdayOutOfLegalBoundsException() {
        super();
    }
    
    /**
     * @param errorType
     */
    public WorkdayOutOfLegalBoundsException(int errorType) {
        super(errorType);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param s
     */
    public WorkdayOutOfLegalBoundsException(String s) {
        super(s);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param cause
     */
    public WorkdayOutOfLegalBoundsException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param message
     * @param cause
     */
    public WorkdayOutOfLegalBoundsException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }
    
    
    
}
