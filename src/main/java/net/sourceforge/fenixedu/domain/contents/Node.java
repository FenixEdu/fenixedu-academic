package net.sourceforge.fenixedu.domain.contents;

import java.util.Collection;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext;

import org.fenixedu.bennu.core.domain.Bennu;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

/**
 * A <code>Node</code> makes the link between a {@link Container} and other {@link Content}. The node also defines a context for
 * the child content. As
 * each content can have multiple parents, the node defines attributes like the
 * order and visibility of the child content when seen inside the parent
 * container.
 * 
 * @author cfgi
 * @author lpec
 * @author pcma
 */
public abstract class Node extends Node_Base implements MenuEntry, Comparable<Node> {

    protected Node() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    protected void init(Container parent, Content child, Boolean isAscending) {
        if (!parent.isChildAccepted(child)) {
            throw new DomainException("contents.child.not.accepted", child.getName().getContent(), parent.getName().getContent());
        }

        if (!child.isParentAccepted(parent)) {
            throw new DomainException("contents.child.not.accepted", child.getName().getContent(), parent.getName().getContent());
        }

        setParent(parent);
        setChild(child);
        setContentId(parent.getContentId() + ":" + child.getContentId());
        setAscending(isAscending);
    }

    @Override
    public boolean isNodeVisible() {
        return super.getVisible() && getChild().isAvailable();
    }

    /**
     * Deletes this node removing the associating between the container in {@link #getParent()} and the content in
     * {@link #getChild()}.
     * 
     * <p>
     * Sibling nodes are reordered if needed.
     */
    public void delete() {
        final Content child = getChild();
        if (child != null) {
            child.logDeleteNode();
        }

        setRootDomainObject(null);
        setParent(null);
        setChild(null);

        deleteDomainObject();
    }

    @Override
    public String getEntryId() {
        return getChild().getContentId();
    }

    @Override
    public MultiLanguageString getName() {
        return getChild().getName();
    }

    @Override
    public String getPath() {
        return getChild().getPath();
    }

    @Override
    public MultiLanguageString getTitle() {
        return getChild().getTitle();
    }

    @Override
    public boolean isAvailable(FunctionalityContext context) {
        return getChild().isAvailable(context);
    }

    @Override
    public boolean isAvailable() {
        return getChild().isAvailable();
    }

    @Override
    public Collection<MenuEntry> getChildren() {
        return getChild().getMenu();
    }

    @Override
    public Content getReferingContent() {
        return getChild();
    }

    @Deprecated
    public boolean hasParent() {
        return getParent() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasVisible() {
        return getVisible() != null;
    }

    @Deprecated
    public boolean hasContentId() {
        return getContentId() != null;
    }

    @Deprecated
    public boolean hasChild() {
        return getChild() != null;
    }

    @Deprecated
    public boolean hasAscending() {
        return getAscending() != null;
    }

}
