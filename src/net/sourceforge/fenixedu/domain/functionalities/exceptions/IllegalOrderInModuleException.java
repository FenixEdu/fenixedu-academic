package net.sourceforge.fenixedu.domain.functionalities.exceptions;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.functionalities.Functionality;
import net.sourceforge.fenixedu.domain.functionalities.Module;

/**
 * This exception occurs when a functionality is added to a module with
 * a order that is invalid in the module. A tipical case is specifying
 * a negative order.
 *
 * @see Functionality#getOrderInModule()
 * 
 * @author cfgi
 */
public class IllegalOrderInModuleException extends DomainException {

    /**
     * Serialization id.
     */
    private static final long serialVersionUID = 1L;

    public IllegalOrderInModuleException(Module module, Functionality functionality) {
        super("functionalities.module.invalid.order", new String[] { 
                module.getName().getContent(), 
                functionality.getName().getContent(),
                String.valueOf(functionality.getOrderInModule()) 
        });
    }

    public IllegalOrderInModuleException(Functionality functionality) {
        super("functionalities.functionality.invalid.order", new String[] { 
                functionality.getName().getContent(),
                String.valueOf(functionality.getOrderInModule()) 
        });
    }
    
}
