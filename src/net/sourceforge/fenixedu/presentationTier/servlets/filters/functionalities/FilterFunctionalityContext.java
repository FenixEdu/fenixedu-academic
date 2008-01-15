package net.sourceforge.fenixedu.presentationTier.servlets.filters.functionalities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.contents.Container;
import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.domain.contents.MetaDomainObjectPortal;
import net.sourceforge.fenixedu.domain.contents.Portal;
import net.sourceforge.fenixedu.domain.functionalities.AbstractFunctionalityContext;
import net.sourceforge.fenixedu.domain.functionalities.Functionality;
import net.sourceforge.fenixedu.domain.functionalities.Module;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.ContentInjectionRewriter;

/**
 * This functionality context is created from the
 * {@link CheckAvailabilityFilter filter} to provide a context that allows
 * functionalities to operate. This context wraps the current requests, user
 * view, and selected functionality.
 * 
 * @author cfgi
 * @author pcma
 */
public class FilterFunctionalityContext extends AbstractFunctionalityContext {

    public static final String CONTEXT_ATTRIBUTE_NAME = "contentContextPath";

    List<Content> contents = new ArrayList<Content>();

    String selectedContainerPath;

    boolean hasBeenForwarded = false;

    public FilterFunctionalityContext(final HttpServletRequest request, final List<Content> contentsToAdd) {
	super(request);
	this.contents.addAll(contentsToAdd);
	findSelectedContainerPath();
    }

    public FilterFunctionalityContext(final HttpServletRequest request, final String encodingParam) {
	super(request);

	this.encoding = encodingParam;

	String path = getCurrentContextPathFromRequest();
	if (path == null) {
	    path = getPath(encoding);
	    hasBeenForwarded = false;
	} else {
	    hasBeenForwarded = true;
	}

	final String trailingPath = getTrailingPath(path);
	Portal.getRootPortal().addPathContentsForTrailingPath(contents, trailingPath);
	addInitialContent();
	final Container selectedContainer = getSelectedContainer();
	if (selectedContainer == null) {
	    String queryString = request.getQueryString();
	    String lookupPath = path + (queryString != null && queryString.length() > 0 ? "?" + queryString : "");
	    if (lookupPath.length() > 0 && lookupPath.charAt(0) != '/') {
		lookupPath = "/" + lookupPath;
	    }

	    final Functionality functionality = Functionality.findByExecutionPath(lookupPath);

	    final String pathFromRequest = getCurrentContextPathFromRequest();
	    if (pathFromRequest != null) {
		hasBeenForwarded = true;
		contents.clear();
		Portal.getRootPortal().addPathContentsForTrailingPath(contents, getTrailingPath(pathFromRequest));
		final Content lastContent = contents.isEmpty() ? null : contents.get(contents.size() - 1);
		if (lastContent != null && lastContent instanceof Functionality) {
		    contents.remove(contents.size() - 1);
		}
		if (functionality != null) {
		    // this is probably redundant, i.e.
		    contents.add(functionality);
		}
	    } else if (functionality != null) {
		hasBeenForwarded = true;
		// this will probable never happen now... but just in case...
		contents.clear();
		functionality.addPathContentsForReversePath(contents);
	    }
	}

	findSelectedContainerPath();
    }

    private void findSelectedContainerPath() {
	if (this.contents.isEmpty()) {
	    selectedContainerPath = "";
	} else {
	    final Container actualSelectedContainer = getSelectedContainer();
	    final StringBuilder stringBuilder = new StringBuilder();
	    for (int i = 0; i < contents.size(); i++) {
		final Content content = contents.get(i);
		if (content instanceof MetaDomainObjectPortal
			&& !((MetaDomainObjectPortal) content).getStrategy().keepPortalInContentsPath()) {
		    i++;
		    if(actualSelectedContainer == contents.get(i)) {
			stringBuilder.append("/");
			stringBuilder.append(content.getName().getContent());
			break;
		    }
		}
		stringBuilder.append("/");
		stringBuilder.append(content.getName().getContent());
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
		    contents.add(initialContent);
		}
	    }
	}
    }

    @Override
    public Module getSelectedModule() {
	if (contents.isEmpty()) {
	    return null;
	}

	final Module root = RootDomainObject.getInstance().getRootModule();

	Content content = getSelectedContent();
	Module parent = (content instanceof Functionality) ? ((Functionality) content).getModule() : null;
	while (parent != null && parent.getModule() != root) {
	    parent = parent.getModule();
	}

	return parent == null ? root : parent;

    }

    public List<Content> getSelectedContents() {
	return contents;
    }

    @Override
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

    @Override
    public Container getSelectedTopLevelContainer() {
	return contents.isEmpty() ? null : (Container) contents.get(0);
    }

    @Override
    public Content getSelectedContent() {
	return contents.isEmpty() ? null : contents.get(contents.size() - 1);
    }

    @Override
    public Functionality getSelectedFunctionality() {
	final Content content = contents.isEmpty() ? null : contents.get(contents.size() - 1);
	return content instanceof Functionality ? (Functionality) content : null;
    }

    public List<Content> getPathBetween(Container container, Content content) {
	final int indexOfContainer = contents.indexOf(container);
	final int indexOfContent = contents.indexOf(content);
	return indexOfContainer <= indexOfContent && indexOfContainer >= 0 ? contents.subList(indexOfContainer,
		indexOfContent + 1) : getEmptyList();
    }

    private static List<Content> getEmptyList() {
	return Collections.emptyList();
    }

    protected String getCurrentContextPathFromRequest() {
	final HttpServletRequest httpServletRequest = getRequest();
	String currentContextPath = httpServletRequest.getParameter(ContentInjectionRewriter.CONTEXT_ATTRIBUTE_NAME);
	if (currentContextPath == null || currentContextPath.length() == 0) {
	    currentContextPath = (String) httpServletRequest.getAttribute(ContentInjectionRewriter.CONTEXT_ATTRIBUTE_NAME);
	}
	return currentContextPath == null || currentContextPath.length() == 0 ? null : currentContextPath;
    }

    public String getCurrentContextPath() {
	/*
         * String currentContextPath = getCurrentContextPathFromRequest(); if
         * (currentContextPath == null) {
         */
	final StringBuilder stringBuilder = new StringBuilder();
	for (final Content content : contents) {
	    if (content != RootDomainObject.getInstance().getRootPortal()) {
		final String name = content.getName().getContent();
		if (stringBuilder.length() > 0 || (stringBuilder.length() == 0 && name.length() > 0 && name.charAt(0) != '/')) {
		    stringBuilder.append('/');
		}
		stringBuilder.append(name);
	    }
	}
	String currentContextPath = stringBuilder.toString();

	return currentContextPath.length() > 1 ? currentContextPath : null;
    }

    public boolean hasBeenForwarded() {
	return hasBeenForwarded;
    }

    public void setHasBeenForwarded() {
	this.hasBeenForwarded = true;
    }

    public void addContent(Content content) {
	contents.add(content);
	findSelectedContainerPath();
    }

    public void addAllContent(List<Content> contents) {
	this.contents.addAll(contents);
	findSelectedContainerPath();
    }
    
    @Override
    public Content getLastContentInPath(Class type) {        
	for (int i = contents.size() - 1; i >= 0; i--) {
	    Content content = contents.get(i);
	    if(type.isAssignableFrom(content.getClass())) {
		return content;
	    }
	}	
        return null;
    }
}
