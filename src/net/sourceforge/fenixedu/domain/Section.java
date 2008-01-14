/*
 * Section.java
 * Mar 10, 2003
 */
package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.contents.Container;
import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.domain.contents.Element;
import net.sourceforge.fenixedu.domain.contents.ExplicitOrderNode;
import net.sourceforge.fenixedu.domain.contents.Node;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.functionalities.Functionality;
import net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext;
import net.sourceforge.fenixedu.util.MultiLanguageString;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

/**
 * @author Ivo Brandão
 */
public class Section extends Section_Base {

    public static final Comparator<Section> COMPARATOR_BY_ORDER = new Comparator<Section>() {

	private ComparatorChain chain = null;

	public int compare(Section one, Section other) {
	    if (this.chain == null) {
		chain = new ComparatorChain();

		chain.addComparator(new BeanComparator("sectionOrder"));
		chain.addComparator(new BeanComparator("name"));
		chain.addComparator(DomainObject.COMPARATOR_BY_ID);
	    }

	    return chain.compare(one, other);
	}

    };

    protected Section() {
	super();

	setCreationDate(new DateTime());
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public Section(Container parent, MultiLanguageString name) {
	this();

	if (parent == null) {
	    throw new NullPointerException();
	}

	setName(name);
	new ExplicitOrderNode(parent, this);
	setVisible(true);
    }

    public Section(Container parent, MultiLanguageString name, Integer sectionOrder) {
	this();
	setName(name);
	new ExplicitOrderNode(parent, this, sectionOrder);
	setVisible(true);
    }

    // TODO: check were lastModifiedDate is used and if this edit is really
    // required
    public void edit(MultiLanguageString name, Section nextSection, Group permittedGroup) {
	setModificationDate(new YearMonthDay());

	setName(name);
	setNextSection(nextSection);
	setPermittedGroup(permittedGroup);
    }

    public void setName(MultiLanguageString name) {
	if (name == null || name.isEmpty()) {
	    throw new NullPointerException();
	}

	// NOTE: removed restriction because it introduces techinal problems
	// that are not really necessary or good for any of the parts: developer
	// and user

	// if (! isNameUnique(getSiblings(), name)) {
	// throw new DuplicatedNameException("site.section.name.duplicated");
	// }

	super.setName(name);
    }

    public List<Section> getSiblings() {
	return getSite().getAssociatedSections(getSuperiorSection());
    }

    /**
         * @return the section immediately after this section in the superior
         *         section or <code>null</code> if the section is the last
         */
    public Section getNextSection() {

	Integer order = ((ExplicitOrderNode) getParents().get(0)).getNodeOrder() + 1;
	Container container = ((ExplicitOrderNode) getParents().get(0)).getParent();
	List<Section> sections = new ArrayList<Section>(container.getOrderedChildren(Section.class));
	return order < sections.size() ? sections.get(order) : null;

    }

    /**
         * Changes the order of this section so that the given section is
         * immediately after this section. If the given section is
         * <code>null</code> then this section will be the last
         * 
         * @param section
         *                the section that should appear after this section or
         *                <code>null</code> if this section should be last
         */
    public void setNextSection(Section section) {
	if (section != null) {
	    setSectionOrder(((ExplicitOrderNode) section.getParents().get(0)).getNodeOrder());
	} else {
	    Collection<Section> sections = (Collection<Section>) ((ExplicitOrderNode) getParents().get(0)).getParent()
		    .getOrderedChildren(Section.class);
	    setSectionOrder(sections.size() - 1);
	}
    }

    public void insertItem(MultiLanguageString itemName, MultiLanguageString itemInformation, Integer insertItemOrder) {
	new Item(this, itemName, itemInformation, insertItemOrder);
    }

    public void copyItemsFrom(Section sectionFrom) {
	for (final Item item : sectionFrom.getAssociatedItems()) {
	    this.insertItem(item.getName(), item.getBody(), item.getItemOrder());
	}
    }

    public void copySubSectionsAndItemsFrom(Section sectionFrom) {
	for (final Section subSectionFrom : sectionFrom.getAssociatedSections()) {
	    if (subSectionFrom.getSuperiorSection() != null) {
		Section subSectionTo = this.getSite().createSection(subSectionFrom.getName(), this,
			subSectionFrom.getSectionOrder());
		subSectionTo.copyItemsFrom(subSectionFrom);
		subSectionTo.copySubSectionsAndItemsFrom(subSectionFrom);
	    }
	}
    }

    @Override
    public boolean isDeletable() {
	for (Section subSection : this.getAssociatedSections()) {
	    if (!subSection.isDeletable()) {
		return false;
	    }
	}

	for (Item item : getAssociatedItems()) {
	    if (!item.isDeletable()) {
		return false;
	    }
	}

	return true;
    }

    @Override
    protected void checkDeletion() {
	if (!isDeletable()) {
	    throw new DomainException("site.section.delete.notAllowed");
	}
    }

    @Override
    protected void disconnect() {
	for (Item item : getAssociatedItems()) {
	    item.delete();
	}
	for (Section section : getAssociatedSections()) {
	    section.delete();
	}

	super.disconnect();
    }

    public SortedSet<Section> getOrderedSubSections() {
	final SortedSet<Section> sections = new TreeSet<Section>(Section.COMPARATOR_BY_ORDER);
	sections.addAll(getAssociatedSections());
	return sections;
    }

    public SortedSet<Item> getOrderedItems() {
	final SortedSet<Item> items = new TreeSet<Item>(Item.COMPARATOR_BY_ORDER);
	items.addAll(getAssociatedItems());
	return items;
    }

    /**
         * A section is always visible to the user as an entry in the menu. The
         * content of the section may not be available and that will be checked
         * when the user tries to access the section.
         */

    public boolean isVisible(FunctionalityContext context) {
	return getVisible();
    }

    @Override
    public boolean isAvailable(FunctionalityContext context) {
	if (getSuperiorSection() != null && !getSuperiorSection().isAvailable(context)) {
	    return false;
	}

	return super.isAvailable(context);
    }

    public void setItemsOrder(List<Item> items) {
	// ITEM_ORDER_ADAPTER.updateOrder(this, items);
    }

    public void setSectionsOrder(List<Section> sections) {
	// SECTION_ORDER_ADAPTER.updateOrder(this, sections);
    }

    public boolean isSubSectionAllowed() {
	return true;
    }

    public boolean isItemAllowed() {
	return true;
    }

    public List<Item> getAssociatedItems() {
	return (List<Item>) getChildren(Item.class);
    }

    public List<Section> getAssociatedSections() {
	return (List<Section>) getChildren(Section.class);
    }

    public boolean hasAnyAssociatedSections() {
	return !getAssociatedSections().isEmpty();
    }

    public boolean hasAssociatedSections(Section section) {
	return getAssociatedSections().contains(section);
    }

    public int getAssociatedSectionsCount() {
	return getAssociatedSections().size();
    }

    public int getAssociatedItemsCount() {
	return getAssociatedItems().size();
    }

    public Integer getSectionOrder() {
	return getParents().isEmpty() ? null : ((ExplicitOrderNode) getParents().get(0)).getNodeOrder();
    }

    public void setSectionOrder(Integer order) {
	((ExplicitOrderNode) getParents().get(0)).setNodeOrder(order);
    }

    public Section getSuperiorSection() {
	return getParents().isEmpty() || !(getParents().get(0).getParent() instanceof Section) ? null : (Section) getParents()
		.get(0).getParent();
    }

    public void removeSuperiorSection() {
	if (!getParents().isEmpty()) {
	    getParents().get(0).delete();
	}
    }

    public List<Section> getSubSections() {
	List<Section> sections = new ArrayList<Section>();
	for (Section section : getChildren(Section.class)) {
	    sections.addAll(section.getSubSections());
	}
	return sections;
    }

    public boolean hasSuperiorSection() {
	return getSuperiorSection() != null;
    }

    public void setSuperiorSection(Section section) {
	getParents().clear();
	new ExplicitOrderNode(section, this);
    }

    private Node getSiteNode() {
	for (Node node : getParents()) {
	    if (node.getParent() instanceof Site) {
		return node;
	    }
	    for (Section section : getParents(Section.class)) {
		Node siteNode = section.getSiteNode();
		if (siteNode != null) {
		    return siteNode;
		}
	    }
	}
	return null;
    }

    public Site getSite() {
	Node node = getSiteNode();
	return (node == null) ? null : (Site) node.getParent();
    }

    public void setSite(Site site) {
	Node node = getSiteNode();
	if (node != null) {
	    node.setParent(site);
	} else {
	    new ExplicitOrderNode(site, this);
	}

    }

    public void removeSite() {
	Node node = getSiteNode();
	node.delete();
    }

    public void setFirstParent(Container container) {
	container.addChild(this);
    }

    @Override
    public boolean isAbleToSpecifyInitialContent() {
	return true;
    }

    public void setVisible(Boolean visible) {
	getParents().get(0).setVisible(visible);
    }

    public Boolean getVisible() {
	return getParents().get(0).getVisible();
    }

    @Override
    public boolean hasInitialContent() {
	return super.getInitialContent() != null;
    }

    @Override
    public String getPath() {
	return (isContainerMaximizable() && getInitialContent() != null) ? getInitialContent().getPath() : null;
    }

    public Collection<Functionality> getAssociatedFunctionalities() {
	return getChildren(Functionality.class);
    }

    @Override
    protected Node createChildNode(Content childContent) {
	return new ExplicitOrderNode(this, childContent);
    }
}