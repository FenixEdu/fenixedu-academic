/*
 * Created on 22/Mar/2004
 *  
 */
package ServidorAplicacao.Servico.exceptions.tests;

import ServidorAplicacao.Servico.exceptions.FenixServiceException;

/**
 * @author Susana Fernandes
 *  
 */
public class NotAuthorizedStudentToDoTestException extends FenixServiceException {

    /**
     *  
     */
    public NotAuthorizedStudentToDoTestException() {
        super();
    }

    /**
     * @param errorType
     */
    public NotAuthorizedStudentToDoTestException(int errorType) {
        super(errorType);
    }

    /**
     * @param s
     */
    public NotAuthorizedStudentToDoTestException(String s) {
        super(s);
    }

    /**
     * @param cause
     */
    public NotAuthorizedStudentToDoTestException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public NotAuthorizedStudentToDoTestException(String message, Throwable cause) {
        super(message, cause);
    }

    public String toString() {
        String result = "[NotAuthorizedStudentToDoTestException\n";
        result += "message" + this.getMessage() + "\n";
        result += "cause" + this.getCause() + "\n";
        result += "]";
        return result;
    }
}