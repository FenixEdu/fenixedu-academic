package ServidorAplicacao.Servico.exceptions;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class NoChangeMadeServiceException extends FenixServiceException {

    public NoChangeMadeServiceException() {
    }

    public NoChangeMadeServiceException(Throwable cause) {
        super(cause);
    }

    public NoChangeMadeServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public String toString() {
        String result = "[NoChangeMadeServiceException\n";
        result += "message" + this.getMessage() + "\n";
        result += "cause" + this.getCause() + "\n";
        result += "]";
        return result;
    }

}