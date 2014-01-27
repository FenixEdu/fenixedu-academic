package net.sourceforge.fenixedu.domain.functionalities;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.contents.Container;
import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.domain.contents.ContentJump;
import net.sourceforge.fenixedu.domain.contents.FunctionalityCall;
import net.sourceforge.fenixedu.domain.contents.InvalidContentPathException;
import net.sourceforge.fenixedu.domain.contents.MetaDomainObjectPortal;
import net.sourceforge.fenixedu.domain.contents.Portal;
import net.sourceforge.fenixedu.domain.contents.Redirect;

import org.fenixedu.bennu.core.domain.Bennu;

/**
 * The context in wich a functionality is being executed.
 * 
 * @author cfgi
 */
public final class FunctionalityContext {

    /**
     * Name of the attribute under which the current functionality context is
     * registered in the request.
     */
    public static final String CONTEXT_KEY = FunctionalityContext.class.getName() + ".CONTEXT";

    private final HttpServletRequest request;

    private String encoding = Charset.defaultCharset().name();

    private final List<Content> contents = new ArrayList<Content>();

    private String selectedContainerPath;

    public FunctionalityContext(final HttpServletRequest request, final List<Content> contentsToAdd) {
        this.request = request;
        this.contents.addAll(contentsToAdd);
        findSelectedContainerPath();
    }

    public FunctionalityContext(final HttpServletRequest request, final String encodingParam) {
        this.request = request;
        this.encoding = encodingParam;

        String path = getRequestedPath();

        if (!path.endsWith(".do") && !path.endsWith(".faces")) {
            final String trailingPath = getTrailingPath(path);
            Portal.getRootPortal().addPathContentsForTrailingPath(contents, trailingPath);
            addInitialContent();
        }

        if (contents.isEmpty()) {
            if (!ableToPopulateContentsUsingContextAttribute()) {
                throw new InvalidContentPathException(null, null);
            }
        }

        findSelectedContainerPath();
    }

    public HttpServletRequest getRequest() {
        return this.request;
    }

    private String getPath(final String encoding) {
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

    private String getRequestedPath() {
        String path = getCurrentContextPathFromRequest();
        if (path == null) {
            path = getPath(encoding);
        }

        return path;
    }

    private boolean ableToPopulateContentsUsingContextAttribute() {
        final String pathFromRequest = getCurrentContextPathFromRequest();

        if (pathFromRequest != null) {
            contents.clear();
            Portal.getRootPortal().addPathContentsForTrailingPath(contents, getTrailingPath(pathFromRequest));
            final Content lastContent = contents.isEmpty() ? null : contents.get(contents.size() - 1);
            if (lastContent != null && lastContent instanceof FunctionalityCall) {
                contents.remove(contents.size() - 1);
            }
        }

        return !contents.isEmpty();
    }

    private void findSelectedContainerPath() {
        if (this.contents.isEmpty()) {
            selectedContainerPath = "";
        } else {
            final Container actualSelectedContainer = getSelectedContainer();
            final StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < contents.size(); i++) {
                final Content content = contents.get(i);
                if (content instanceof ContentJump) {
                    stringBuilder.append("/");
                    stringBuilder.append(content.getNormalizedName().getContent());
                    i++;
                    if (actualSelectedContainer == contents.get(i)) {
                        break;
                    } else {
                        continue;
                    }
                }
                if ((content instanceof MetaDomainObjectPortal && !((MetaDomainObjectPortal) content).getStrategy()
                        .keepPortalInContentsPath())) {
                    i++;
                    if (actualSelectedContainer == contents.get(i)) {
                        stringBuilder.append("/");
                        stringBuilder.append(content.getNormalizedName().getContent());
                        break;
                    }
                }
                stringBuilder.append("/");
                stringBuilder.append(content.getNormalizedName().getContent());
                if (content == actualSelectedContainer) {
                    break;
                }
            }
            selectedContainerPath = stringBuilder.toString();
        }
    }

    private String getTrailingPath(final String path) {
        return path.length() > 0 && path.charAt(0) == '/' ? path.substring(1) : path;
    }

    private void addInitialContent() {
        if (!contents.isEmpty()) {
            final Content content = contents.get(contents.size() - 1);
            if (content.isContainer() && !(content instanceof Section && getSelectedContainer() instanceof Site)) {
                final Container container = (Container) content;
                final Content initialContent = container.getInitialContent();
                if (initialContent != null) {
                    List<Content> contents =
                            (initialContent instanceof Functionality) ? findCorrectFunctionalityCall(container,
                                    (Functionality) initialContent) : container.getPathTo(initialContent);
                    if (contents.size() > 1) {
                        this.contents.addAll(contents.subList(1, contents.size()));
                    } else {
                        this.contents.add(initialContent);
                    }
                }
            }
        }
    }

    private List<Content> findCorrectFunctionalityCall(Container container, Functionality initialContent) {
        List<Content> functionalityContents;
        for (FunctionalityCall functionalityCall : initialContent.getFunctionalityCalls()) {
            functionalityContents = container.getPathTo(functionalityCall);
            if (functionalityContents.size() > 1) {
                return functionalityContents;
            }
        }
        return Collections.emptyList();
    }

    public List<Content> getSelectedContents() {
        return contents;
    }

    public Container getSelectedContainer() {
        if (contents.isEmpty()) {
            return null;
        }

        for (int i = contents.size() - 1; i >= 0; i--) {
            Content content = contents.get(i);
            if (content instanceof Container) {
                final Container container = (Container) content;
                if (container.isContainerMaximizable() || container instanceof Site) {
                    return container;
                }
            }
        }
        return getSelectedTopLevelContainer();
    }

    public String getSelectedContainerPath() {
        return selectedContainerPath;
    }

    public Container getSelectedTopLevelContainer() {
        return contents.isEmpty() ? null : (Container) contents.iterator().next();
    }

    public Content getSelectedContent() {
        return contents.isEmpty() ? null : contents.get(contents.size() - 1);
    }

    public List<Content> getPathBetween(Container container, Content content) {
        final int indexOfContainer = contents.indexOf(container);
        final int indexOfContent = contents.indexOf(content);
        return indexOfContainer <= indexOfContent && indexOfContainer >= 0 ? contents.subList(indexOfContainer,
                indexOfContent + 1) : Collections.<Content> emptyList();
    }

    private String getCurrentContextPathFromRequest() {
        throw new UnsupportedOperationException("Request is no more!");
    }

    public String getCurrentContextPath() {
        /*
         * String currentContextPath = getCurrentContextPathFromRequest(); if
         * (currentContextPath == null) {
         */

        boolean hasContentJump = false;
        final StringBuilder stringBuilder = new StringBuilder();
        for (final Content content : contents) {
            if (content instanceof ContentJump) {
                hasContentJump = true;
            }
            if (content instanceof Site && hasContentJump) {
                continue;
            }
            if (content instanceof Redirect) {
                continue;
            }
            if (content != Bennu.getInstance().getRootPortal()) {
                final String name = content.getNormalizedName().getContent();
                if (name.length() > 0 && (stringBuilder.length() > 0 || (stringBuilder.length() == 0 && name.charAt(0) != '/'))) {
                    stringBuilder.append('/');
                }
                stringBuilder.append(name);
            }
        }
        String currentContextPath = stringBuilder.toString();

        return currentContextPath.length() > 1 ? currentContextPath : null;
    }

    public void setHasBeenForwarded() {
    }

    public void addAllContent(List<Content> contents) {
        this.contents.addAll(contents);
        findSelectedContainerPath();
    }

    public Content getLastContentInPath(Class type) {
        for (int i = contents.size() - 1; i >= 0; i--) {
            Content content = contents.get(i);
            if (type.isAssignableFrom(content.getClass())) {
                return content;
            }
        }
        return null;
    }

    public static FunctionalityContext getCurrentContext(HttpServletRequest request) {
        return (FunctionalityContext) request.getAttribute(FunctionalityContext.CONTEXT_KEY);
    }

}
