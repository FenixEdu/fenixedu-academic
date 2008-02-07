/*
 * Site.java
 * Mar 10, 2003
 */
package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.accessControl.EveryoneGroup;
import net.sourceforge.fenixedu.domain.accessControl.InternalPersonGroup;
import net.sourceforge.fenixedu.domain.contents.Container;
import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.domain.contents.Element;
import net.sourceforge.fenixedu.domain.contents.ExplicitOrderNode;
import net.sourceforge.fenixedu.domain.contents.MetaDomainObjectPortal;
import net.sourceforge.fenixedu.domain.contents.Node;
import net.sourceforge.fenixedu.domain.functionalities.Functionality;
import net.sourceforge.fenixedu.injectionCode.IGroup;
import net.sourceforge.fenixedu.util.MultiLanguageString;

/**
 * @author Ivo Brandão
 */
public abstract class Site extends Site_Base {

    public Site() {
	super();

	setRootDomainObject(RootDomainObject.getInstance());
    }

    public Section createSection(MultiLanguageString sectionName, Container parentContainer, Integer sectionOrder) {
	return new Section(parentContainer, sectionName, sectionOrder);
    }

    public abstract IGroup getOwner();

    public List<Section> getTopLevelSections() {
	return getAssociatedSections(null);
    }

    public List<Section> getAllAssociatedSections() {
	List<Section> sections = new ArrayList<Section>();
	for (Section section : getTopLevelSections()) {
	    sections.add(section);
	    sections.addAll(section.getSubSections());
	}
	return sections;
    }

    public List<Section> getAssociatedSections(final Section parentSection) {
	final List<Section> result;

	if (parentSection != null) {
	    result = parentSection.getAssociatedSections();
	} else {
	    result = new ArrayList<Section>();
	    result.addAll(getChildren(Section.class));
	}
	return result;
    }

    public SortedSet<Section> getOrderedTopLevelSections() {
	final SortedSet<Section> sections = new TreeSet<Section>(Section.COMPARATOR_BY_ORDER);
	for (final Section section : getChildren(Section.class)) {
	    sections.add(section);
	}
	return sections;
    }

    public int getNumberOfTopLevelSections() {
	return getChildren(Section.class).size();
    }

    public void copySectionsAndItemsFrom(Site siteFrom) {
	for (Section sectionFrom : siteFrom.getAssociatedSections()) {
	    Section sectionTo = this.createSection(sectionFrom.getName(), this, sectionFrom.getSectionOrder());
	    sectionTo.copyItemsFrom(sectionFrom);
	    sectionTo.copySubSectionsAndItemsFrom(sectionFrom);
	}
    }

    /**
     * Obtains a list of all the groups available in the context of this site.
     * 
     * @return
     */
    public List<IGroup> getContextualPermissionGroups() {
	List<IGroup> groups = new ArrayList<IGroup>();

	groups.add(new EveryoneGroup());
	groups.add(new InternalPersonGroup());

	return groups;
    }

    public void setTopLevelSectionsOrder(List<Section> sections) {
	// SECTION_ORDER_ADAPTER.updateOrder(this, sections);
    }

    public String getAuthorName() {
	return null;
    }

    public ExecutionPeriod getExecutionPeriod() {
	return null;
    }

    public MetaDomainObjectPortal getTemplate() {
	MetaDomainObject metaDomainObject = MetaDomainObject.getMeta(this.getClass());
	return metaDomainObject == null ? null : (MetaDomainObjectPortal) metaDomainObject.getAssociatedPortal();
    }

    public boolean isTemplateAvailable() {
	return getTemplate() != null;
    }
    
    public List<Content> getOrderedTemplateSections() {
	List<Content> sections = new ArrayList<Content>();

	MetaDomainObjectPortal template = getTemplate();

	if (template != null) {
	    sections.addAll(template.getChildrenAsContent());
	}

	return sections;
    }

    public List<Content> getTemplateSections() {
	return getOrderedTemplateSections();
    }

    public List<Content> getAllOrderedTopLevelSections() {
	List<Content> sections = getOrderedTemplateSections();
	sections.addAll(getOrderedTopLevelSections());

	return sections;
    }

    public List<Content> getAllTopLevelSections() {
	List<Content> sections = getTemplateSections();
	sections.addAll(getTopLevelSections());

	return sections;
    }

    public static List<Section> getOrderedSections(Collection<Section> sections) {
	List<Section> orderedSections = new ArrayList<Section>(sections);
	Collections.sort(orderedSections, Section.COMPARATOR_BY_ORDER);

	return orderedSections;
    }

    /**
     * If this site has quota policy or not.
     * 
     * @return <code>true</code> if we should not exceed the size in
     *         {@link #getQuota()}
     */
    public boolean hasQuota() {
	return false;
    }

    /**
     * The maximum size that can be occupied by files in this site.
     * 
     * @return the maximum combined sizes (in bytes) of all files in this site.
     */
    public long getQuota() {
	return 0;
    }

    /**
     * Computes the current size (in bytes) occupied by all the files in this
     * site.
     * 
     * @return
     */
    public long getUsedQuota() {
	long size = 0;

	for (Section section : getAssociatedSections()) {
	    for (Item item : section.getAssociatedItems()) {
		for (FileItem file : item.getFileItems()) {
		    size += file.getSize();
		}
	    }
	}

	return size;
    }

    public boolean isFileClassificationSupported() {
	return false;
    }

    public boolean isScormContentAccepted() {
	return false;
    }

    public boolean hasAnyAssociatedSections() {
	return !getAssociatedSections().isEmpty();
    }

    public List<Section> getAssociatedSections() {
	return (List<Section>) getChildren(Section.class);
    }

    public Set<Section> getAssociatedSectionsSet() {
	Set<Section> sections = new HashSet<Section>();
	sections.addAll(getAssociatedSections());
	return sections;

    }

    public int getAssociatedSectionsCount() {
	return getAssociatedSections().size();
    }

    public void addAssociatedSections(Section section) {
	new ExplicitOrderNode(this, section);
    }

    @Override
    public Collection<Node> getOrderedChildrenNodes(Class<? extends Content> childType) {
	List<Node> nodes = getChildren();
	for (Iterator<Node> nodeIterator = nodes.iterator(); nodeIterator.hasNext();) {
	    Node node = nodeIterator.next();
	    if (!childType.isAssignableFrom(node.getClass())) {
		nodeIterator.remove();
	    }
	}
	return nodes;
    }

    @Override
    public <T extends Content> Collection<T> getOrderedChildren(Class<T> type) {
	List<T> contents = new ArrayList<T>();
	for (Node node : new TreeSet<Node>(super.getChildren())) {
	    T parent = (T) node.getChild();

	    if (type != null && !type.isAssignableFrom(parent.getClass())) {
		continue;
	    }

	    contents.add(parent);
	}

	return contents;
    }

    @Override
    public Collection<Node> getOrderedChildrenNodes() {
	List<Node> nodes = new ArrayList<Node>();
	MetaDomainObject template = MetaDomainObject.getMeta(this.getClass());
	if (template != null && template.isPortalAvailable()) {
	    nodes.addAll(template.getAssociatedPortal().getOrderedChildrenNodes());
	}
	nodes.addAll(new TreeSet<Node>(super.getChildren()));
	return nodes;
    }

    @Override
    public List<Node> getChildren() {
	List<Node> nodes = new ArrayList<Node>();
	MetaDomainObject template = MetaDomainObject.getMeta(this.getClass());
	if (template != null && template.isPortalAvailable()) {
	    nodes.addAll(template.getAssociatedPortal().getChildren());
	}
	nodes.addAll(super.getChildren());
	return nodes;
    }

    @Override
    public Collection<Content> getDirectChildrenAsContent() {
	List<Content> contents = new ArrayList<Content>();
	for (Node node : getOrderedDirectChildren()) {
	    contents.add(node.getChild());
	}
	return contents;
    }

    @Override
    public Collection<Node> getOrderedDirectChildren() {
	return new TreeSet<Node>(super.getChildren());
    }

    @Override
    public MultiLanguageString getName() {
	return MultiLanguageString.i18n().add("pt", String.valueOf(getIdInternal())).finish();
    }

    @Override
    protected Container findSomeNonModuleParent() {
	return getTemplate();
    }

    public Collection<Functionality> getAssociatedFunctionalities() {
	List<Functionality> functionalities = new ArrayList<Functionality>();
	for (Content content : getDirectChildrenAsContent()) {
	    if (content instanceof Functionality) {
		functionalities.add((Functionality) content);
	    }
	}
	return functionalities;
    }

    @Override
    public Content getInitialContent() {
	final MetaDomainObjectPortal template = getTemplate();
	Content initialContent = null;
	if (template != null) {
	    
	    initialContent = getTemplate().getInitialContent();
	    if (initialContent != null) {
		return initialContent;
	    }
	    
	    Collection<Element> children = getOrderedChildren(Element.class);
	    if (!children.isEmpty()) {
		return children.iterator().next();
	    }
	
	    for (Container container : getOrderedChildren(Container.class)) {
		initialContent = container.getInitialContent();
		if (initialContent != null) {
		    break;
		}
	    }
	}
	return initialContent;
    }

    @Override
    protected void disconnect() {
	for (Node node : getChildrenSet()) {
	    removeNode(node);
	}
	disconnectContent();
    }

}
