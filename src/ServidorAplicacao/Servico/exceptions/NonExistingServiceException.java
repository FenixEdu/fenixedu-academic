package ServidorAplicacao.Servico.exceptions;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class NonExistingServiceException extends FenixServiceException {

    public NonExistingServiceException() {
    }

    public NonExistingServiceException(String message) {
        super(message);
    }

    public NonExistingServiceException(Throwable cause) {
        super(cause);
    }

    public NonExistingServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public String toString() {
        String result = "[NonExistingServiceException\n";
        result += "message" + this.getMessage() + "\n";
        result += "cause" + this.getCause() + "\n";
        result += "]";
        return result;
    }

}