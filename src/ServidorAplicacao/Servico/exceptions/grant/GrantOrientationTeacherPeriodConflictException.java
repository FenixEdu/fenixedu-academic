/*
 * Created on 28/Nov/2003
 * 
 */

package ServidorAplicacao.Servico.exceptions.grant;

import ServidorAplicacao.Servico.exceptions.FenixServiceException;

/**
 * 
 * @author Barbosa
 * @author Pica
 */

public class GrantOrientationTeacherPeriodConflictException extends FenixServiceException {

    public GrantOrientationTeacherPeriodConflictException() {
    }

    public GrantOrientationTeacherPeriodConflictException(String message) {
        super(message);
    }

    public GrantOrientationTeacherPeriodConflictException(Throwable cause) {
        super(cause);
    }

    public GrantOrientationTeacherPeriodConflictException(String message, Throwable cause) {
        super(message, cause);
    }

    public String toString() {
        String result = "[GrantOrientationTeacherPeriodConflictException\n";
        result += "message" + this.getMessage() + "\n";
        result += "cause" + this.getCause() + "\n";
        result += "]";
        return result;
    }
}