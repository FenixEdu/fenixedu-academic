package net.sourceforge.fenixedu.applicationTier.Servico;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;

/**
 * @author jorge
 */
public class ExcepcaoAutenticacao extends FenixServiceException {

    /**
     * Constructor for ExcepcaoAutenticacao.
     */
    public ExcepcaoAutenticacao() {
        super();
    }

    /**
     * Constructor for ExcepcaoAutenticacao.
     * 
     * @param message
     */
    public ExcepcaoAutenticacao(String message) {
        super(message);
    }

    /**
     * Constructor for ExcepcaoAutenticacao.
     * 
     * @param message
     * @param cause
     */
    public ExcepcaoAutenticacao(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor for ExcepcaoAutenticacao.
     * 
     * @param cause
     */
    public ExcepcaoAutenticacao(Throwable cause) {
        super(cause);
    }

}