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

public class GrantOrientationTeacherEndDateBeforeBeginDateException extends FenixServiceException {

    public GrantOrientationTeacherEndDateBeforeBeginDateException() {
    }

    public GrantOrientationTeacherEndDateBeforeBeginDateException(String message) {
        super(message);
    }

    public GrantOrientationTeacherEndDateBeforeBeginDateException(Throwable cause) {
        super(cause);
    }

    public GrantOrientationTeacherEndDateBeforeBeginDateException(String message, Throwable cause) {
        super(message, cause);
    }

    public String toString() {
        String result = "[GrantOrientationTeacherEndDateBeforeBeginDateException\n";
        result += "message" + this.getMessage() + "\n";
        result += "cause" + this.getCause() + "\n";
        result += "]";
        return result;
    }
}