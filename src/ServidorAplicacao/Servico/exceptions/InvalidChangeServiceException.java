package ServidorAplicacao.Servico.exceptions;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class InvalidChangeServiceException extends FenixServiceException {

    /**
     *  
     */
    public InvalidChangeServiceException() {

    }

    /**
     * @param s
     */
    public InvalidChangeServiceException(String s) {
        super(s);

    }

    /**
     * @param cause
     */
    public InvalidChangeServiceException(Throwable cause) {
        super(cause);

    }

    /**
     * @param message
     * @param cause
     */
    public InvalidChangeServiceException(String message, Throwable cause) {
        super(message, cause);

    }

    public String toString() {
        String result = "[InvalidChangeServiceException\n";
        result += "message" + this.getMessage() + "\n";
        result += "cause" + this.getCause() + "\n";
        result += "]";
        return result;
    }
}