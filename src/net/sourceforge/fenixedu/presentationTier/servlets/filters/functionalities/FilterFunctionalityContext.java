package net.sourceforge.fenixedu.presentationTier.servlets.filters.functionalities;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.domain.functionalities.AbstractFunctionalityContext;
import net.sourceforge.fenixedu.domain.functionalities.Functionality;
import net.sourceforge.fenixedu.domain.functionalities.Module;

/**
 * This functionality context is created from the
 * {@link CheckAvailabilityFilter filter} to provide a context that allows
 * functionalities to operate. This context wraps the current requests, user
 * view, and selected functionality.
 * 
 * @author cfgi
 */
public class FilterFunctionalityContext extends AbstractFunctionalityContext {

    private Functionality functionality;

    public FilterFunctionalityContext(HttpServletRequest request, Functionality functionality) {
        super(request);
        
        this.functionality = functionality;
    }

    @Override
    public Module getSelectedModule() {
        if (this.functionality == null) {
            return null;
        }
        
        if (this.functionality instanceof Module) { 
            return (Module) this.functionality;
        }
        else {
            return this.functionality.getModule();
        }
    }

    @Override
    public Functionality getSelectedFunctionality() {
        return this.functionality;
    }

}
