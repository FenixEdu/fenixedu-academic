/*
 * Created on 03/Feb/2004
 *  
 */
package ServidorAplicacao.Servico.exceptions.grant;

import ServidorAplicacao.Servico.exceptions.FenixServiceException;

/**
 * @author Barbosa
 * @author Pica
 */

public class InvalidPartResponsibleTeacherException extends FenixServiceException {

    public InvalidPartResponsibleTeacherException() {
    }

    public InvalidPartResponsibleTeacherException(String message) {
        super(message);
    }

    public InvalidPartResponsibleTeacherException(Throwable cause) {
        super(cause);
    }

    public InvalidPartResponsibleTeacherException(String message, Throwable cause) {
        super(message, cause);
    }

    public String toString() {
        String result = "[InvalidProjectResponsibleTeacherException\n";
        result += "message" + this.getMessage() + "\n";
        result += "cause" + this.getCause() + "\n";
        result += "]";
        return result;
    }
}