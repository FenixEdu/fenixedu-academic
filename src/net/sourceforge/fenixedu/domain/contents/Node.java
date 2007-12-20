package net.sourceforge.fenixedu.domain.contents;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.UUID;

import dml.runtime.Relation;
import dml.runtime.RelationAdapter;
import dml.runtime.RelationListener;

import net.sourceforge.fenixedu.util.MultiLanguageString;

/**
 * A <code>Node</code> makes the link between a {@link Container} and other
 * {@link Content}. The node also defines a context for the child content. As
 * each content can have multiple parents, the node defines attributes like the
 * order and visibility of the child content when seen inside the parent
 * container.
 * 
 * @author cfgi
 * @author lpec
 * @author pcma
 */
public abstract class Node extends Node_Base implements MenuEntry, Comparable<Node> {

    static {
	ContentNode.addListener(new RelationAdapter<Node, Content>() {

	    @Override
	    public void afterRemove(Node node, Content content) {
		super.afterRemove(node, content);
		if (node != null && content != null) {
		    if (content.getParents().isEmpty()) {
			content.delete();
		    }
		}
	    }
	});
    }

    protected Node() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setOjbConcreteClass(getClass().getName());
    }

    protected void init(Container parent, Content child, Boolean isAscending) {
	if(!parent.isChildAccepted(child)) { 
	    throw new DomainException("contents.child.not.accepted", child.getName().getContent(), parent.getName().getContent());
	}
	
	if(!child.isParentAccepted(parent)) {
	    throw new DomainException("contents.child.not.accepted", child.getName().getContent(), parent.getName().getContent());
	}
	
	setParent(parent);
	setChild(child);
	setContentId(parent.getContentId() + ":" + child.getContentId());
	setAscending(isAscending);
    }

    @Override
    public Boolean getVisible() {
	return super.getVisible();
    }

    public boolean isVisible() {
	return super.getVisible() && getChild().isAvailable();
    }

    /**
         * Deletes this node removing the associating between the container in
         * {@link #getParent()} and the content in {@link #getChild()}.
         * 
         * <p>
         * Sibling nodes are reordered if needed.
         */
    public void delete() {
	removeRootDomainObject();
	removeParent();
	removeChild();

	deleteDomainObject();
    }

    public String getEntryId() {
	return getChild().getContentId();
    }

    public MultiLanguageString getName() {
	return getChild().getName();
    }

    public String getPath() {
	return getChild().getPath();
    }

    public MultiLanguageString getTitle() {
	return getChild().getTitle();
    }

    public boolean isAvailable(FunctionalityContext context) {
	return getChild().isAvailable(context);
    }
    
    public boolean isAvailable() {
	return getChild().isAvailable();
    }

    public Collection<MenuEntry> getChildren() {
	return getChild().getMenu();
    }

    public Content getReferingContent() {
	return getChild();
    }
}
