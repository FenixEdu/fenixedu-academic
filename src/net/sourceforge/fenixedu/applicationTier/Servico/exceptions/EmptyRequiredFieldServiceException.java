/*
 * Created on 13/Mar/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.exceptions;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class EmptyRequiredFieldServiceException extends FenixServiceException {

    /**
     *  
     */
    public EmptyRequiredFieldServiceException() {
        super();

    }

    /**
     * @param s
     */
    public EmptyRequiredFieldServiceException(String s) {
        super(s);

    }

    /**
     * @param cause
     */
    public EmptyRequiredFieldServiceException(Throwable cause) {
        super(cause);

    }

    /**
     * @param message
     * @param cause
     */
    public EmptyRequiredFieldServiceException(String message, Throwable cause) {
        super(message, cause);

    }
}