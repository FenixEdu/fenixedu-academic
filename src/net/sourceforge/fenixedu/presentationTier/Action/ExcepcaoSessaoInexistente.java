package net.sourceforge.fenixedu.presentationTier.Action;

import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;

public class ExcepcaoSessaoInexistente extends FenixActionException {

    /**
     * Constructor for SessaoInexistente.
     */
    public ExcepcaoSessaoInexistente() {
        super();
    }

    /**
     * Constructor for SessaoInexistente.
     * 
     * @param message
     */
    public ExcepcaoSessaoInexistente(String message) {
        super(message);
    }

    /**
     * Constructor for SessaoInexistente.
     * 
     * @param message
     * @param cause
     */
    public ExcepcaoSessaoInexistente(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor for SessaoInexistente.
     * 
     * @param cause
     */
    public ExcepcaoSessaoInexistente(Throwable cause) {
        super(cause);
    }
}