package ServidorAplicacao.Servico.exceptions;

/**
 * This class represents an exception thrown when an invalid service is
 * specified.
 * 
 * @author Joao Pereira
 * @version
 */

public class InvalidServiceException extends RuntimeException {

    public InvalidServiceException() {
    }

    public InvalidServiceException(String s) {
        super(s);
    }
}