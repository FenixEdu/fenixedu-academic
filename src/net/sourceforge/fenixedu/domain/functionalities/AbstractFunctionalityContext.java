package net.sourceforge.fenixedu.domain.functionalities;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

/**
 * Provides the default behaviour for a context.
 * 
 * @author cfgi
 */
public abstract class AbstractFunctionalityContext implements FunctionalityContext {

    private HttpServletRequest request;
    private IUserView userView;
    
    public AbstractFunctionalityContext(HttpServletRequest request) {
        super();
        
        this.request = request;
        this.userView = SessionUtils.getUserView(request);
    }

    public HttpServletRequest getRequest() {
        return this.request;
    }

    public IUserView getUserView() {
        return this.userView;
    }

    public User getLoggedUser() {
        IUserView userView = getUserView();
        
        if (userView == null || userView.isPublicRequester()) {
            return null;
        }
        else {
            return userView.getPerson().getUser();
        }
    }

    public abstract Module getSelectedModule();
    public abstract Functionality getSelectedFunctionality();

}
