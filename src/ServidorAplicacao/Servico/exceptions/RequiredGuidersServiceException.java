package ServidorAplicacao.Servico.exceptions;

/**
 * 
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 */
public class RequiredGuidersServiceException extends FenixServiceException {

    public RequiredGuidersServiceException() {
    }

    public RequiredGuidersServiceException(String message) {
        super(message);
    }

    public RequiredGuidersServiceException(Throwable cause) {
        super(cause);
    }

    public RequiredGuidersServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public String toString() {
        String result = "[RequiredGuidersServiceException\n";
        result += "message" + this.getMessage() + "\n";
        result += "cause" + this.getCause() + "\n";
        result += "]";
        return result;
    }

}