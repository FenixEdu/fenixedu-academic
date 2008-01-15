package net.sourceforge.fenixedu.domain.functionalities;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.domain.contents.Container;
import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionUtils;

/**
 * Provides the default behaviour for a context.
 * 
 * @author cfgi
 */
public abstract class AbstractFunctionalityContext implements FunctionalityContext {

    private HttpServletRequest request;
    private IUserView userView;

    protected String encoding = "ISO-8859-1";

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

    protected String getPath(final String encoding) {
	final String requestedPath = getRequest().getRequestURI().substring(getRequest().getContextPath().length());
	try {
	    if (requestedPath.matches("/dotIstPortal.do")) {
		return getRequest().getParameter("prefix") + getRequest().getParameter("page");
	    }
	    return requestedPath.length() == 0 ? requestedPath : URLDecoder.decode(requestedPath.substring(1), encoding);
	} catch (UnsupportedEncodingException e) {
	    throw new Error(e);
	}
    }

    protected String getParentPath() {
	return getParentPath(getPath(encoding));
    }

    protected String getSubPath() {
	return getSubPath(getPath(encoding));
    }

    protected static String getSubPath(final String path) {
	final int indexOfSlash = path.indexOf('/');
	return indexOfSlash >= 0 ? path.substring(0, indexOfSlash) : path;
    }

    protected static String getParentPath(final String path) {
	final int indexOfLantSlash = path.lastIndexOf('/');
	return indexOfLantSlash >= 0 ? path.substring(0, indexOfLantSlash) : null;
    }

    public Container getSelectedContainer() {
	// TODO Auto-generated method stub
	return null;
    }
    
    public abstract Module getSelectedModule();
    public abstract Functionality getSelectedFunctionality();
    public abstract Container getSelectedTopLevelContainer();
    
    public static FunctionalityContext getCurrentContext(HttpServletRequest request) {
	FunctionalityContext context = (FunctionalityContext) request.getAttribute(FunctionalityContext.CONTEXT_KEY);
	return context;
    }
    
    public Content getLastContentInPath(Class type) {
	return getSelectedContainer();
    }

    public List<Content> getSelectedContents() {
	return Collections.emptyList();
    }
    
    public Content getSelectedContent() {
	return null;
    }
}
