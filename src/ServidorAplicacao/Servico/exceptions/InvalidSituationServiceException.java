/*
 * Created on 12/Mar/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code Template
 */
package ServidorAplicacao.Servico.exceptions;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class InvalidSituationServiceException extends FenixServiceException {

    /**
     *  
     */
    public InvalidSituationServiceException() {

    }

    /**
     * @param s
     */
    public InvalidSituationServiceException(String s) {
        super(s);

    }

    /**
     * @param cause
     */
    public InvalidSituationServiceException(Throwable cause) {
        super(cause);

    }

    /**
     * @param message
     * @param cause
     */
    public InvalidSituationServiceException(String message, Throwable cause) {
        super(message, cause);

    }

    public String toString() {
        String result = "[InvalidArgumentsServiceException\n";
        result += "message" + this.getMessage() + "\n";
        result += "cause" + this.getCause() + "\n";
        result += "]";
        return result;
    }
}