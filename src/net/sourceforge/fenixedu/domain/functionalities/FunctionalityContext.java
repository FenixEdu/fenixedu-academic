package net.sourceforge.fenixedu.domain.functionalities;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.domain.contents.Container;
import net.sourceforge.fenixedu.domain.contents.Content;

/**
 * The context in wich a functionality is being executed.
 * 
 * @author cfgi
 */
public interface FunctionalityContext {

    /**
     * Name of the attribute under which the current functionality context is
     * registered in the request.
     */
    public static final String CONTEXT_KEY = FunctionalityContext.class.getName() + ".CONTEXT";

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
     * @return the current logged user or <code>null</code> if the user is
     *         anonymous
     */
    public User getLoggedUser();

    public Content getSelectedContent();
    
    public Container getSelectedContainer();
    
    public Container getSelectedTopLevelContainer();
    
    public Content getLastContentInPath(Class type);

    public String getCurrentContextPath();

    public List<Content> getSelectedContents();
    
}
