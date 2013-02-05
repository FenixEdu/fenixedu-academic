package net.sourceforge.fenixedu.applicationTier.Servico;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;

/**
 * @author jorge
 */
public class ExcepcaoInexistente extends FenixServiceException {

    /**
     * Constructor for ExcepcaoInexistente.
     */
    public ExcepcaoInexistente() {
        super();
    }

    /**
     * Constructor for ExcepcaoInexistente.
     * 
     * @param message
     */
    public ExcepcaoInexistente(String message) {
        super(message);
    }

    /**
     * Constructor for ExcepcaoInexistente.
     * 
     * @param message
     * @param cause
     */
    public ExcepcaoInexistente(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor for ExcepcaoInexistente.
     * 
     * @param cause
     */
    public ExcepcaoInexistente(Throwable cause) {
        super(cause);
    }

}