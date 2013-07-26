package net.sourceforge.fenixedu.domain.functionalities;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.domain.contents.Container;
import net.sourceforge.fenixedu.domain.contents.Content;
import pt.ist.bennu.core.domain.User;
import pt.ist.bennu.core.security.Authenticate;

/**
 * Provides the default behaviour for a context.
 * 
 * @author cfgi
 */
public abstract class AbstractFunctionalityContext implements FunctionalityContext {

    private final HttpServletRequest request;
    private User userView;

    protected String encoding = PropertiesManager.DEFAULT_CHARSET;

    public AbstractFunctionalityContext(HttpServletRequest request) {
        super();

        this.request = request;
        this.userView = Authenticate.getUser();
        this.userView = Authenticate.getUser();
    }

    @Override
    public HttpServletRequest getRequest() {
        return this.request;
    }

    @Override
    public User getUserView() {
        return this.userView;
    }

    @Override
    public User getLoggedUser() {
        final User userView = getUserView();
        return userView == null ? null : userView.getPerson().getUser();
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

    @Override
    public Container getSelectedContainer() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public abstract Container getSelectedTopLevelContainer();

    public static FunctionalityContext getCurrentContext(HttpServletRequest request) {
        FunctionalityContext context = (FunctionalityContext) request.getAttribute(FunctionalityContext.CONTEXT_KEY);
        return context;
    }

    @Override
    public Content getLastContentInPath(Class type) {
        return getSelectedContainer();
    }

    @Override
    public List<Content> getSelectedContents() {
        return Collections.emptyList();
    }

    @Override
    public Content getSelectedContent() {
        return null;
    }
}
