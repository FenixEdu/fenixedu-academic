package ServidorAplicacao.Servico.exceptions;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class NonExistingContributorServiceException extends FenixServiceException {

    public NonExistingContributorServiceException() {
    }

    public NonExistingContributorServiceException(Throwable cause) {
        super(cause);
    }

    public NonExistingContributorServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public String toString() {
        String result = "[NonExistingContributorServiceException\n";
        result += "message" + this.getMessage() + "\n";
        result += "cause" + this.getCause() + "\n";
        result += "]";
        return result;
    }

}