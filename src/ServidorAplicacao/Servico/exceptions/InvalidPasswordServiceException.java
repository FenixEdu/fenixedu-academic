/*
 * Created on 13/Mar/2003
 *
 */
package ServidorAplicacao.Servico.exceptions;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class InvalidPasswordServiceException extends FenixServiceException {

    /**
     *  
     */
    public InvalidPasswordServiceException() {
        super();

    }

    /**
     * @param s
     */
    public InvalidPasswordServiceException(String s) {
        super(s);

    }

    /**
     * @param cause
     */
    public InvalidPasswordServiceException(Throwable cause) {
        super(cause);

    }

    /**
     * @param message
     * @param cause
     */
    public InvalidPasswordServiceException(String message, Throwable cause) {
        super(message, cause);

    }
}