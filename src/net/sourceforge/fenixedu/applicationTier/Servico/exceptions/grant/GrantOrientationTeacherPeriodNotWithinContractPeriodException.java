/*
 * Created on 28/Nov/2003
 * 
 */

package net.sourceforge.fenixedu.applicationTier.Servico.exceptions.grant;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;

/**
 * 
 * @author Barbosa
 * @author Pica
 */

public class GrantOrientationTeacherPeriodNotWithinContractPeriodException extends FenixServiceException {

    public GrantOrientationTeacherPeriodNotWithinContractPeriodException() {
    }

    public GrantOrientationTeacherPeriodNotWithinContractPeriodException(String message) {
        super(message);
    }

    public GrantOrientationTeacherPeriodNotWithinContractPeriodException(Throwable cause) {
        super(cause);
    }

    public GrantOrientationTeacherPeriodNotWithinContractPeriodException(String message, Throwable cause) {
        super(message, cause);
    }

    public String toString() {
        String result = "[GrantOrientationTeacherPeriodNotWithinContractPeriodException\n";
        result += "message" + this.getMessage() + "\n";
        result += "cause" + this.getCause() + "\n";
        result += "]";
        return result;
    }
}