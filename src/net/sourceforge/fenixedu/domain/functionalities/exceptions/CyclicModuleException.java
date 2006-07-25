package net.sourceforge.fenixedu.domain.functionalities.exceptions;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

/**
 * This exception is thrown when you try to add a module as a sub-module
 * if itself or one of it's children.
 * 
 * @author cfgi
 */
public class CyclicModuleException extends DomainException {

    /**
     * Serialization id.
     */
    private static final long serialVersionUID = 1L;

    public CyclicModuleException() {
        super("functionalities.module.cycle.invalid", new String[0]);
    }

}
