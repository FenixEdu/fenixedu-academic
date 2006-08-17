package net.sourceforge.fenixedu.presentationTier.servlets.filters.functionalities;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.domain.functionalities.Functionality;
import net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext;
import net.sourceforge.fenixedu.domain.functionalities.Module;

/**
 * This functionality context is created from the
 * {@link CheckAvailabilityFilter filter} to provide a context that allows
 * functionalities to operate. This context wraps the current requests, user
 * view, and selected functionality.
 * 
 * @author cfgi
 */
public class FilterFunctionalityContext implements FunctionalityContext {

    private HttpServletRequest request;
    private IUserView userView;
    private Functionality functionality;

    public FilterFunctionalityContext(HttpServletRequest request, IUserView userView, Functionality functionality) {
        super();
        
        this.request = request;
        this.userView = userView;
        this.functionality = functionality;
    }

    public HttpServletRequest getRequest() {
        return this.request;
    }

    public IUserView getUserView() {
        return this.userView;
    }

    public User getLoggedUser() {
        if (getUserView().isPublicRequester()) {
            return null;
        }
        else {
            return getUserView().getPerson().getUser();
        }
    }

    public Module getSelectedModule() {
        if (this.functionality instanceof Module) { 
            return (Module) this.functionality;
        }
        else {
            return this.functionality.getModule();
        }
    }

    public Functionality getSelectedFunctionality() {
        return this.functionality;
    }

}
