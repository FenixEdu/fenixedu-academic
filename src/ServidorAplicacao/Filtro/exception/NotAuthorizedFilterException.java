/*
 * Created on Dec 16, 2003
 *  
 */
package ServidorAplicacao.Filtro.exception;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class NotAuthorizedFilterException extends FenixFilterException {
    /**
     *  
     */
    public NotAuthorizedFilterException() {
        super();
    }

    /**
     * @param message
     */
    public NotAuthorizedFilterException(String message) {
        super(message);
    }

    /**
     * @param message
     * @param cause
     */
    public NotAuthorizedFilterException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param cause
     */
    public NotAuthorizedFilterException(Throwable cause) {
        super(cause);
    }

}