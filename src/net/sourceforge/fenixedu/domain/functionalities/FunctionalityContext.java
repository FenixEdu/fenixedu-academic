package net.sourceforge.fenixedu.domain.functionalities;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.User;

/**
 * The context in wich a functionality is being executed.
 * 
 * @author cfgi
 */
public interface FunctionalityContext {

    /**
     * @return the current request being served
     */
    public HttpServletRequest getRequest();
    
    /**
     * @return the current <code>UserView</code>
     */
    public IUserView getUserView();
    
    /**
     * The context gives access to the current logged user. 
     * 
     * @return the current logged user or <code>null</code> if the user is anonymous
     */
    public User getLoggedUser();
    
    /**
     * Thie method allows you to obtain the current selected module. The current 
     * selected module is the most specific module currently being used, that is,
     * it's not always the top level module. 
     * 
     * @return the selected module
     */
    public Module getSelectedModule();
    
    /**
     * Allows you to get the last selected functionality. If the user just selected
     * a module then this method returns the same as {@link #getSelectedModule()}.
     * 
     * @return the selected functionality
     */
    public Functionality getSelectedFunctionality();
}
