/*
 * Created on 6/Mar/2003
 *
 * 
 */
package ServidorAplicacao.Servico.exceptions;

/**
 * @author João Mota
 */
public class notAuthorizedServiceDeleteException extends FenixServiceException {

    /**
     *  
     */
    public notAuthorizedServiceDeleteException() {
        super();
    }

    /**
     * @param s
     */
    public notAuthorizedServiceDeleteException(String s) {
        super(s);
    }

    /**
     * @param cause
     */
    public notAuthorizedServiceDeleteException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public notAuthorizedServiceDeleteException(String message, Throwable cause) {
        super(message, cause);
    }

    public String toString() {
        String result = "[notAuthorizedServiceDeleteException\n";
        result += "message" + this.getMessage() + "\n";
        result += "cause" + this.getCause() + "\n";
        result += "]";
        return result;
    }

}