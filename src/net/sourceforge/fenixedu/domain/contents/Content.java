package net.sourceforge.fenixedu.domain.contents;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import net.sourceforge.fenixedu.domain.Language;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accessControl.EveryoneGroup;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.functionalities.AvailabilityPolicy;
import net.sourceforge.fenixedu.domain.functionalities.Functionality;
import net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext;
import net.sourceforge.fenixedu.domain.functionalities.GroupAvailability;
import net.sourceforge.fenixedu.domain.functionalities.Module;
import net.sourceforge.fenixedu.util.MultiLanguageString;

import org.joda.time.DateTime;

import pt.utl.ist.fenix.tools.util.StringNormalizer;

/**
 * A <code>Content</code> is a piece of information, normally created directly
 * by a user, that can be composed and presented in a page or menu.
 * 
 * @author cfgi
 * @author lpec
 * @author pcma
 */
public abstract class Content extends Content_Base {

    public Content() {
	super();

	setRootDomainObject(RootDomainObject.getInstance());
	setOjbConcreteClass(getClass().getName());
	setCreationDate(new DateTime());
	setContentId(UUID.randomUUID().toString());
    }

    public abstract boolean isParentAccepted(Container parent);

    public boolean isAnAnnouncement() {
	return false;
    }

    public boolean isAnAnnouncementBoard() {
	return false;
    }

    public <T extends Container> Collection<T> getParents(Class<T> type) {
	List<T> parents = new ArrayList<T>();

	for (Node node : getParents()) {
	    T parent = (T) node.getParent();

	    if (type != null && !type.isAssignableFrom(parent.getClass())) {
		continue;
	    }

	    parents.add(parent);
	}

	return parents;
    }

    public <T extends Container> T getParent(Class<T> type) {
	Collection<T> parents = getParents(type);
	return parents.isEmpty() ? null : parents.iterator().next();
    }

    public Node getParentNode(Container parent) {
	for (Node node : getParents()) {
	    if (node.getParent() == parent) {
		return node;
	    }
	}

	return null;
    }

    /**
         * Checks is this item is available in the given context, that is, if
         * the person hold in the context may click the item to start an
         * activity or usecase.
         * 
         * @return <code>true</code> if the item is available for the person
         *         hold in the context
         */
    public boolean isAvailable(FunctionalityContext context) {
	if (context == null) {
	    return true;
	}

	if (getAvailabilityPolicy() == null) {
	    return true;
	}

	try {
	    return getAvailabilityPolicy().isAvailable(context);
	} catch (Exception e) {
	    e.printStackTrace();
	    return false;
	}
    }

    public abstract List<Content> getPathTo(Content target);

    /**
         * Deletes this object.
         * 
         * <p>
         * This delete method is a template method for all contents. First
         * {@link #checkDeletion()} is called. If the object is not deletable
         * then a subclass must throw a {@link DomainException} explaining why.
         * If no exception is thrown then {@link #disconnect()} is called to
         * allow the object to remove any specific relations. After that
         * {@link #deleteSelf()} is called to allow object finalization.
         * 
         * <p>
         * After all this the standard relations of a functionality are removed
         * and the object is marked for deletion in the database.
         * 
         * @throws DomainException
         *                 if the content cannot be deleted
         */
    public void delete() {
	checkDeletion();
	disconnect();
	deleteSelf();
	deleteDomainObject();
    }

    /**
         * Verifies if this content can be deleted.
         * 
         * @return <code>true</code> by default
         */
    public boolean isDeletable() {
	return true;
    }

    /**
         * Checks if this content can be deleted and throws and exception if it
         * can't.
         * 
         * @throws DomainException
         *                 if this item cannot be deleted
         */
    protected void checkDeletion() {
	if (!isDeletable()) {
	    throw new DomainException("accessibleItem.delete.notAvailable");
	}
    }

    /**
         * Removes any specific relations the item has. Subclasses that override
         * this method <strong>must</strong> call super to remove all
         * relations.
         * 
         * <p>
         * If other objects should be deleted because of this object beeing
         * deleted, this is the place to do it.
         */
    protected void disconnect() {
	if (hasAvailabilityPolicy()) {
	    getAvailabilityPolicy().delete();
	}

	for (Node node : new ArrayList<Node>(getParents())) {
	    node.delete();
	}
	
	for(Container container : getInitialContainer()) {
	    container.setInitialContent(null);
	}
	
	removeRootDomainObject();
    }

    /**
         * Finalizes the state of this content, that is, does the last
         * finalization after beeing disconnected but before being marked for
         * deleting from the persistent storage.
         */
    protected void deleteSelf() {
	// do nothing
    }

    public boolean isAvailable() {
	return isAvailable(Functionality.getCurrentContext());
    }

    // This method is to determine if the content is publicly available in the contents' structure,
    // i.e., whether it should be subjected to checksum verification.
    public boolean isPublic() {
	if (this instanceof Module) {
	    return false;
	}

	final AvailabilityPolicy availabilityPolicy = getAvailabilityPolicy();
	if (availabilityPolicy == null) {
	    for (final Node node : getParentsSet()) {
		final Content content = node.getParent();
		if (content.isPublic()) {
		    return true;
		}
	    }
	    return getParentsSet().isEmpty();
	} else {
	    return availabilityPolicy.getTargetGroup() instanceof EveryoneGroup;
	}
    }

    public Boolean getPublicAvailable() {
	return isPublic();
    }
    
    public abstract Collection<MenuEntry> getMenu();

    public String getPath() {
	return null;
    }

    /*
         * Code that came from SiteElement
         * 
         */

    protected boolean isNameUnique(List<? extends Content> siblings, MultiLanguageString name) {
	Map<Language, Set<String>> names = new Hashtable<Language, Set<String>>();

	for (Content sibling : siblings) {
	    if (sibling == this) {
		continue;
	    }

	    MultiLanguageString siblingName = sibling.getNormalizedName();
	    for (Language language : siblingName.getAllLanguages()) {
		Set<String> languageNames = names.get(language);

		if (languageNames == null) {
		    languageNames = new HashSet<String>();
		    names.put(language, languageNames);
		}

		String localName = siblingName.getContent(language).toLowerCase();
		languageNames.add(localName);
	    }
	}

	for (Language language : name.getAllLanguages()) {
	    Set<String> languageNames = names.get(language);

	    if (languageNames == null) {
		continue;
	    }

	    String localName = name.getContent(language).toLowerCase();
	    if (languageNames.contains(localName)) {
		return false;
	    }
	}

	return true;
    }

    public Group getPermittedGroup() {
	AvailabilityPolicy availability = getAvailabilityPolicy();

	Group result;
	if (availability == null) {
	    result = null;
	} else {
	    result = availability.getTargetGroup();
	}

	return result == null ? new EveryoneGroup() : result;
    }

    public void setPermittedGroup(Group group) {
	if (group == null) {
	    setAvailabilityPolicy(null);
	} else {
	    new GroupAvailability(this, group);

	}
    }

    @Override
    public void setTitle(MultiLanguageString title) {
	if (title == null || title.isEmpty()) {
	    super.setTitle(null);
	} else {
	    super.setTitle(title);
	}
    }

    @Override
    public void setDescription(MultiLanguageString description) {
	if (description == null || description.isEmpty()) {
	    super.setDescription(null);
	} else {
	    super.setDescription(description);
	}
    }

    public boolean isAbleToSpecifyInitialContent() {
	return false;
    }

    public abstract boolean isContainer();

    public abstract boolean isElement();

    public boolean matchesPath(final String path) {
	for (final String name : getNormalizedName().getAllContents()) {
	    if (name.equalsIgnoreCase(path)) {
		return true;
	    }
	}
	return false;
    }

    public void addPathContents(final List<Content> contents, final String path) {
	if (matchesPath(path)) {
	    contents.add(this);
	}
    }

    protected Container findSomeNonModuleParent() {
	for (final Node node : getParentsSet()) {
	    final Container container = node.getParent();
	    if (!(container instanceof Module)) {
		return container;
	    }
	}
	return null;
    }

    public void addPathContentsForReversePath(final List<Content> contents) {
	final Container container = findSomeNonModuleParent();
	if (container != null) {
	    container.addPathContentsForReversePath(contents);
	} else {
	    return;
	}
	contents.add(this);
    }

    public void appendReversePathPart(final StringBuilder stringBuilder) {
	final String name = getNormalizedName().getContent();
	stringBuilder.append("/");
	stringBuilder.append(name);
    }

    public void appendReversePath(final StringBuilder stringBuilder) {
	final Container container = findSomeNonModuleParent();
	if (container != null) {
	    container.appendReversePath(stringBuilder);
	}
	appendReversePathPart(stringBuilder);
    }

    public String getReversePath() {
	final StringBuilder stringBuilder = new StringBuilder();
	appendReversePath(stringBuilder);
	return stringBuilder.toString();
    }

    @Override
    public void setAvailabilityPolicy(AvailabilityPolicy availabilityPolicy) {       
        AvailabilityPolicy currentAvailabilityPolicy = getAvailabilityPolicy();
        if(currentAvailabilityPolicy != null && availabilityPolicy != null) {
            currentAvailabilityPolicy.delete();
        }
        super.setAvailabilityPolicy(availabilityPolicy);
    }

    @Override
    public void setName(final MultiLanguageString name) {
	super.setName(name);
	setNormalizedName(normalize(name));
    }

    public MultiLanguageString getNormalizedName() {
	final MultiLanguageString normalizedName = super.getNormalizedName();
	return normalizedName == null ? normalize(getName()) : normalizedName;
    }

    public static MultiLanguageString normalize(final MultiLanguageString multiLanguageString) {
	if (multiLanguageString == null) {
	    return null;
	}
	final MultiLanguageString result = new MultiLanguageString();
	for (final Language language : multiLanguageString.getAllLanguages()) {
	    result.setContent(language, normalize(multiLanguageString.getContent(language)));
	}
	return result;
    }

    public static String normalize(final String string) {
	return string == null ? null : StringNormalizer.normalize(string).replace(' ', '-');
    }
    
}
