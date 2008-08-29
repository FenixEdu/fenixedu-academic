package net.sourceforge.fenixedu.domain.contents;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.FileContent;
import net.sourceforge.fenixedu.domain.functionalities.AvailabilityPolicy;
import net.sourceforge.fenixedu.domain.functionalities.Functionality;

/**
 * A <code>Container</code> is a composed content, that is, a grouping of other
 * contents under a certain prefix or context. Containers can have the same
 * information as other basic contentes but are usually important due to their
 * structure and not their name or body.
 * 
 * @author cfgi
 * @author lpec
 * @author pcma
 */
public abstract class Container extends Container_Base {

    public Container() {
	super();
    }

    public boolean isChildAccepted(Content child) {
	return true;
    }

    @Override
    public boolean isParentAccepted(Container parent) {
	return true;
    }

    public Collection<Node> getOrderedDirectChildren() {
	return getOrderedChildrenNodes();
    }

    public Collection<Content> getDirectChildrenAsContent() {
	return getChildrenAsContent();
    }

    public Collection<Content> getChildrenAsContent() {
	List<Content> children = new ArrayList<Content>();
	for (Node node : getOrderedChildrenNodes()) {
	    children.add(node.getChild());
	}

	return children;
    }

    public <T extends Content> Collection<T> getChildren(Class<T> type) {
	List<T> children = new ArrayList<T>();

	for (Node node : getChildren()) {
	    T parent = (T) node.getChild();

	    if (type != null && !type.isAssignableFrom(parent.getClass())) {
		continue;
	    }

	    children.add(parent);
	}

	return children;
    }

    public <T extends Content> Collection<T> getOrderedChildren(Class<T> type) {
	List<T> contents = new ArrayList<T>();

	for (Node node : getOrderedChildrenNodes()) {
	    T parent = (T) node.getChild();

	    if (type != null && !type.isAssignableFrom(parent.getClass())) {
		continue;
	    }

	    contents.add(parent);
	}

	return contents;
    }

    public Collection<Content> getOrderedChildren(final Class... types) {
	final List<Content> contents = new ArrayList<Content>();
	for (final Node node : getOrderedChildrenNodes()) {
	    final Content child = node.getChild();
	    if (isContentAssinableTo(child, types)) {
		contents.add(child);
	    }
	}
	return contents;
    }

    private boolean isContentAssinableTo(Content content, Class... types) {
	final Class clazz = content.getClass();
	for (final Class typeClass : types) {
	    if (typeClass.isAssignableFrom(clazz)) {
		return true;
	    }
	}
	return false;
    }

    public Collection<Node> getOrderedChildrenNodes() {
	return new TreeSet<Node>(getChildren());
    }

    public Collection<Node> getOrderedChildrenNodes(Class<? extends Content> childType) {
	Set<Node> nodes = new TreeSet<Node>();
	for (Node node : getOrderedChildrenNodes()) {
	    if (childType.isAssignableFrom(node.getChild().getClass())) {
		nodes.add(node);
	    }
	}
	return nodes;
    }

    private List<Content> getPathTo(Content topContent, Content bottomContent) {
	if (topContent == bottomContent) {
	    List<Content> contents = new ArrayList<Content>();
	    contents.add((Content) bottomContent);
	    return contents;
	}
	for (Node node : bottomContent.getParents()) {
	    Container content = node.getParent();

	    List<Content> result = content.getPathTo(topContent, content);
	    if (!result.isEmpty()) {
		result.add(bottomContent);

		return result;
	    }

	}

	return Collections.emptyList();
    }

    public List<Content> getPathTo(Content target) {
	return getPathTo(this, target);
    }

    @Override
    protected void disconnect() {
	super.disconnect();
	for (Node node : getChildren()) {
	    removeNode(node);
	}
	final Content content = getInitialContent();
	if (content != null) {
	    removeInitialContent();
	    if (!content.hasAnyParents()) {
		content.delete();
	    }
	}
    }

    @Override
    public Collection<MenuEntry> getMenu() {
	return isContainerMaximizable() ? new ArrayList<MenuEntry>() : new ArrayList<MenuEntry>(getOrderedChildrenNodes());
    }

    public Content getChildByContentId(String id) {
	for (Content child : getChildrenAsContent()) {
	    if (id.equals(child.getContentId())) {
		return child;
	    }
	}
	return null;
    }

    @Override
    public boolean isContainer() {
	return true;
    }

    @Override
    public boolean isElement() {
	return false;
    }

    public boolean isContainerMaximizable() {
	return Boolean.TRUE.equals(getMaximizable());
    }

    protected String getSubPathForSearch(final String path) {
	final int indexOfSlash = path.indexOf('/');
	return indexOfSlash >= 0 ? path.substring(0, indexOfSlash) : path;
    }

    public void addPathContentsForTrailingPath(final List<Content> contents, final String trailingPath) {
	int contentsSize = contents.size();
	for (final Node node : getChildren()) {
	    final Content content = node.getChild();
	    content.addPathContents(contents, trailingPath);
	    if (contentsSize < contents.size()) {
		return;
	    }
	}
	if (getParentsSet().isEmpty()
		&& !(trailingPath.length() == 0 || (trailingPath.length() == 1 && trailingPath.charAt(0) == '/'))) {
	    throw new InvalidContentPathException(this, trailingPath);
	}
    }

    @Override
    public void addPathContents(final List<Content> contents, final String path) {
	final String subPath = getSubPathForSearch(path);
	if (matchesPath(subPath)) {
	    contents.add(this);
	    if (subPath.length() + 1 < path.length()) {
		final String trailingPath = path.substring(subPath.length() + 1);
		final int size = contents.size();
		addPathContentsForTrailingPath(contents, trailingPath);
		if (contents.size() == size
			&& !(trailingPath.length() == 0 || (trailingPath.length() == 1 && trailingPath.charAt(0) == '/'))) {
		    throw new InvalidContentPathException(this, trailingPath);
		}
	    }
	}
    }

    @Override
    public AvailabilityPolicy getAvailabilityPolicy() {
	AvailabilityPolicy policy = super.getAvailabilityPolicy();
	if (policy != null) {
	    return policy;
	}
	//Collection<Node> nodes = getOrderedChildrenNodes();
	Collection<Node> nodes = getChildrenSet();
	return nodes.isEmpty() ? null : nodes.iterator().next().getChild().getAvailabilityPolicy();
    }

    public void addChild(final Content content) {
	createChildNode(content instanceof Functionality ? new FunctionalityCall((Functionality) content) : content);
    }

    protected abstract Node createChildNode(final Content childContent);

    public void removeChild(final Content content) {
	for (Node node : getChildrenSet()) {
	    if (node.getChild() == content) {
		removeNode(node);
		break;
	    }
	}
    }

    protected void removeNode(Node node) {
	Content content = node.getChild();
	if (getInitialContent() == content) {
	    setInitialContent(null);
	}
	node.delete();
	if (!content.hasAnyParents()) {
	    content.delete();
	}
    }

    public Node getChildNode(Content content) {
	for (Node node : getChildren()) {
	    if (node.getChild() == content) {
		return node;
	    }
	}
	return null;
    }

    @Override
    public Content getInitialContent() {
	Content content = super.getInitialContent();
	if (content != null) {
	    return content;
	}
	final Collection<Element> elements = (Collection<Element>) getOrderedChildren(Element.class);
	return elements.isEmpty() ? null : elements.iterator().next();
    }

    public void addFile(final FileContent fileContent) {
	addChild(new Attachment(fileContent));
    }
}
