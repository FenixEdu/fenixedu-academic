package net.sourceforge.fenixedu.domain.functionalities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.contents.Container;
import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.domain.contents.ExplicitOrderNode;
import net.sourceforge.fenixedu.domain.contents.Node;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.FieldIsRequiredException;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

/**
 * The module is an aggregation of functionalities. It allows to enable or
 * disable a group of functionalities and composes functionalities in an
 * hierarchical structure.
 * 
 * @author cfgi
 */
public class Module extends Module_Base implements IFunctionality {

    //
    // Listeners
    //

    /**
     * Required default constructor.
     */
    protected Module() {
	super();
    }

    /**
     * @see Functionality#Functionality(MultiLanguageString, String)
     */
    public Module(MultiLanguageString name, String prefix) {
	this();

	setName(name);
	setPrefix(prefix);
    }

    public Module getModule() {
	for (Node node : getParents()) {
	    if (node.getParent() instanceof Module) {
		return (Module) node.getParent();
	    }
	}

	return null;
    }

    public void setModule(Module module) {
	Module oldModule = getModule();
	module.addChild(this);
	if (oldModule != null) {
	    oldModule.removeChild(this);
	}
	invalidatePath();
    }

    public void invalidatePath() {
	for (final Node node : getChildrenSet()) {
	    final Content content = node.getChild();
	    if (content instanceof Module) {
		final Module childModule = (Module) content;
		childModule.invalidatePath();
	    } else if (content instanceof Functionality) {
		final Functionality childFunctionality = (Functionality) content;
		childFunctionality.invalidatePath();
	    }
	}
    }

    @Override
    public void setPrefix(String prefix) {
	if (prefix == null || prefix.length() == 0) {
	    throw new FieldIsRequiredException("prefix", "functionalities.module.required.prefix");
	}

	super.setPrefix(prefix);
	// Functionality.checkMatchPath();
    }

    /**
     * The public prefix is the part of the module that is visible by the client
     * and is prefixed to all the sub-functionalities of the module. The
     * difference to {@link #getPrefix() normal prefix} is that this prefix
     * consideres all the parent modules.
     * 
     * @return the prefix of this module as seen be the client
     */
    public String getPublicPrefix() {
	Module parent = getParent(Module.class);

	String prefix = getNormalizedPrefix();
	if (parent != null) {
	    return parent.getPublicPrefix().length() == 1 ? prefix : parent.getPublicPrefix() + prefix;
	} else {
	    return prefix.length() == 0 ? "/" : prefix;
	}
    }

    public void appendPath(final StringBuilder stringBuilder) {
	final Module parent = getParentModule();
	final String prefix = getNormalizedPrefix();
	if (parent != null) {
	    parent.appendPath(stringBuilder);
	}
	if (prefix.length() > 0 && prefix.charAt(0) != '/') {
	    stringBuilder.append('/');
	}
	stringBuilder.append(prefix);
    }

    public Module getParentModule() {
	for (final Node node : getParents()) {
	    final Container container = node.getParent();
	    if (container != null && container instanceof Module) {
		return (Module) container;
	    }
	}
	return null;
    }

    /**
     * @return the {@link #getPrefix() current prefix} but ensuring that starts
     *         but does not end with "/"
     */
    protected String getNormalizedPrefix() {
	String prefix = getPrefix();

	int end = prefix.endsWith("/") ? prefix.length() - 1 : prefix.length();
	return (prefix.startsWith("/") ? "" : "/") + prefix.substring(0, end);
    }

    /**
     * Returns all the functionalities under this module. It makes no distintion
     * between sub functionalities and modules.
     */
    public Collection<Functionality> getFunctionalities() {
	return getChildren(Functionality.class);
    }

    /**
     * This method orders all the sub functionalities according with their order
     * in the module.
     * 
     * @return all the sub functionalities ordered by their order in the module
     * 
     * @see Functionality#getOrder()
     */
    public List<Content> getOrderedFunctionalities() {
	return new ArrayList<Content>(getOrderedChildren(Content.class));
    }

    /**
     * A module is visible if one of it's sub functionalities is visible to the
     * user. This allows a user to see, for example, a top-level module that
     * deep in it's sub-functionalities tree has a visible functionality.
     * 
     * <p>
     * A module is also visible when it has an accessible public path. So if the
     * module has a public path but none of it's children is visible then it is
     * still visible to the user.
     */
    public boolean isVisible(FunctionalityContext context) {
	for (Node node : getChildren()) {
	    if (node.isNodeVisible()) {
		return true;
	    }
	}

	return false;
    }

    public static Module getRootModule() {
	return RootDomainObject.getInstance().getRootModule();
    }

    public Module findModule(UUID uuid) {
	if (uuid.equals(getContentId())) {
	    return this;
	} else {
	    for (Module module : getChildren(Module.class)) {
		Module found = module.findModule(uuid);

		if (found != null) {
		    return found;
		}
	    }
	}

	return null;
    }

    @Override
    public boolean isDeletable() {
	return super.isDeletable() && this != Module.getRootModule();
    }

    @Override
    public String getPath() {
	return getPublicPrefix();
    }

    @Override
    protected Node createChildNode(Content childContent) {
	return new ExplicitOrderNode(this, childContent);
    }

    @Override
    public void addChild(Content content) {
	createChildNode(content);
    }
}
