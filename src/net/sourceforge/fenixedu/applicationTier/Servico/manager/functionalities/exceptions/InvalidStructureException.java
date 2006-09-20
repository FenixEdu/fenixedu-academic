package net.sourceforge.fenixedu.applicationTier.Servico.manager.functionalities.exceptions;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

/**
 * This exception is thrown when an invalid structure is detetected when importing
 * a functionalities structure file.
 * 
 * @author cfgi
 */
public class InvalidStructureException extends DomainException {

    /**
     * Serialization id. 
     */
    private static final long serialVersionUID = 1L;

    public InvalidStructureException(String message) {
        super(message);
    }
    
    public InvalidStructureException(String message, Throwable e) {
        super(message, e, e.getMessage());
    }
    
}
